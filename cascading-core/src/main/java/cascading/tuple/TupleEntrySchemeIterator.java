/*
 * Copyright (c) 2007-2022 The Cascading Authors. All Rights Reserved.
 *
 * Project and contact information: https://cascading.wensel.net/
 *
 * This file is part of the Cascading project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cascading.tuple;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

import cascading.flow.FlowProcess;
import cascading.scheme.ConcreteCall;
import cascading.scheme.Scheme;
import cascading.tap.Tap;
import cascading.util.CloseableIterator;
import cascading.util.SingleCloseableInputIterator;
import cascading.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class TupleEntrySchemeIterator is a helper class for wrapping a {@link Scheme} instance, calling
 * {@link Scheme#source(cascading.flow.FlowProcess, cascading.scheme.SourceCall)} on every call to
 * {@link #next()}. The behavior can be controlled via properties defined in {@link TupleEntrySchemeIteratorProps}.
 * <p>
 * Use this class inside a custom {@link cascading.tap.Tap} when overriding the
 * {@link cascading.tap.Tap#openForRead(cascading.flow.FlowProcess)} method.
 */
public class TupleEntrySchemeIterator<Config, Input> extends TupleEntryIterator
  {
  /** Field LOG */
  private static final Logger LOG = LoggerFactory.getLogger( TupleEntrySchemeIterator.class );

  private final FlowProcess<? extends Config> flowProcess;
  private final Scheme<Config, Input, ?, Object, ?> scheme;
  private final CloseableIterator<Input> inputIterator;
  private final Set<Class<? extends Exception>> permittedExceptions;
  private ConcreteCall sourceCall;

  private Supplier<String> loggableIdentifier = () -> "'unknown'";
  private boolean isComplete = false;
  private boolean hasWaiting = false;
  private TupleException currentException;

  @Deprecated
  public TupleEntrySchemeIterator( FlowProcess<? extends Config> flowProcess, Scheme scheme, Input input )
    {
    this( flowProcess, scheme, input, null );
    }

  @Deprecated
  public TupleEntrySchemeIterator( FlowProcess<? extends Config> flowProcess, Scheme scheme, Input input, String loggableIdentifier )
    {
    this( flowProcess, scheme, (CloseableIterator<Input>) new SingleCloseableInputIterator( (Closeable) input ), loggableIdentifier );
    }

  @Deprecated
  public TupleEntrySchemeIterator( FlowProcess<? extends Config> flowProcess, Scheme scheme, CloseableIterator<Input> inputIterator )
    {
    this( flowProcess, scheme, inputIterator, null );
    }

  @Deprecated
  public TupleEntrySchemeIterator( FlowProcess<? extends Config> flowProcess, Scheme scheme, CloseableIterator<Input> inputIterator, String loggableIdentifier )
    {
    this( flowProcess, null, scheme, inputIterator, loggableIdentifier );
    }

  public TupleEntrySchemeIterator( FlowProcess<? extends Config> flowProcess, Tap tap, Scheme scheme, Input input )
    {
    this( flowProcess, tap, scheme, input, (Supplier<String>) null );
    }

  public TupleEntrySchemeIterator( FlowProcess<? extends Config> flowProcess, Tap tap, Scheme scheme, Input input, String loggableIdentifier )
    {
    this( flowProcess, tap, scheme, (CloseableIterator<Input>) new SingleCloseableInputIterator( (Closeable) input ), loggableIdentifier );
    }

  public TupleEntrySchemeIterator( FlowProcess<? extends Config> flowProcess, Tap tap, Scheme scheme, Input input, Supplier<String> loggableIdentifier )
    {
    this( flowProcess, tap, scheme, (CloseableIterator<Input>) new SingleCloseableInputIterator( (Closeable) input ), loggableIdentifier );
    }

  public TupleEntrySchemeIterator( FlowProcess<? extends Config> flowProcess, Tap tap, Scheme scheme, CloseableIterator<Input> inputIterator )
    {
    this( flowProcess, tap, scheme, inputIterator, (Supplier<String>) null );
    }

  public TupleEntrySchemeIterator( FlowProcess<? extends Config> flowProcess, Tap tap, Scheme scheme, CloseableIterator<Input> inputIterator, String loggableIdentifier )
    {
    this( flowProcess, tap, scheme, inputIterator, loggableIdentifier == null ? null : () -> loggableIdentifier );
    }

  public TupleEntrySchemeIterator( FlowProcess<? extends Config> flowProcess, Tap tap, Scheme scheme, CloseableIterator<Input> inputIterator, Supplier<String> loggableIdentifier )
    {
    super( scheme.getSourceFields() );
    this.flowProcess = flowProcess;
    this.scheme = scheme;
    this.inputIterator = inputIterator;

    Object permittedExceptions = flowProcess.getProperty( TupleEntrySchemeIteratorProps.PERMITTED_EXCEPTIONS );

    if( permittedExceptions != null )
      this.permittedExceptions = Util.asClasses( permittedExceptions.toString(), "unable to load permitted exception class" );
    else
      this.permittedExceptions = Collections.emptySet();

    // honor provided loggableIdentifier value
    if( tap != null && loggableIdentifier == null )
      this.loggableIdentifier = tap::getIdentifier;
    else if( loggableIdentifier != null )
      this.loggableIdentifier = loggableIdentifier;

    if( !inputIterator.hasNext() )
      {
      isComplete = true;
      return;
      }

    sourceCall = createSourceCall();

    sourceCall.setTap( tap );
    sourceCall.setIncomingEntry( getTupleEntry() );
    sourceCall.setInput( wrapInput( inputIterator.next() ) );

    try
      {
      this.scheme.sourcePrepare( flowProcess, sourceCall );
      }
    catch( IOException exception )
      {
      throw new TupleException( "unable to prepare source for input identifier: " + this.loggableIdentifier.get(), exception );
      }
    }

  /**
   * Override to provide custom ConcreteCall implementation to expose Tap level resources to the underlying Scheme.
   *
   * @return a new ConcreteCall instance
   */
  protected <Context, IO> ConcreteCall<Context, IO> createSourceCall()
    {
    return new ConcreteCall<>();
    }

  protected FlowProcess<? extends Config> getFlowProcess()
    {
    return flowProcess;
    }

  protected Input wrapInput( Input input )
    {
    try
      {
      return scheme.sourceWrap( flowProcess, input );
      }
    catch( IOException exception )
      {
      throw new TupleException( "unable to wrap source for input identifier: " + this.loggableIdentifier.get(), exception );
      }
    }

  @Override
  public boolean hasNext()
    {
    if( currentException != null )
      return true;

    if( isComplete )
      return false;

    if( hasWaiting )
      return true;

    try
      {
      getNext();
      }
    catch( Exception exception )
      {
      if( permittedExceptions.contains( exception.getClass() ) )
        {
        LOG.warn( "Caught permitted exception while reading {}", loggableIdentifier.get(), exception );
        return false;
        }

      currentException = new TupleException( "unable to read from input identifier: " + loggableIdentifier.get(), exception );

      return true;
      }

    if( !hasWaiting )
      isComplete = true;

    return !isComplete;
    }

  private TupleEntry getNext() throws IOException
    {
    Tuples.asModifiable( sourceCall.getIncomingEntry().getTuple() );
    hasWaiting = scheme.source( flowProcess, sourceCall );

    while( !hasWaiting && inputIterator.hasNext() )
      {
      sourceCall.setInput( wrapInput( inputIterator.next() ) );

      try
        {
        scheme.sourceRePrepare( flowProcess, sourceCall );
        }
      catch( IOException exception )
        {
        throw new TupleException( "unable to prepare source for input identifier: " + loggableIdentifier.get(), exception );
        }

      Tuples.asModifiable( sourceCall.getIncomingEntry().getTuple() );
      hasWaiting = scheme.source( flowProcess, sourceCall );
      }

    return getTupleEntry();
    }

  @Override
  public TupleEntry next()
    {
    try
      {
      if( currentException != null )
        throw currentException;
      }
    finally
      {
      currentException = null; // data may be trapped
      }

    if( isComplete )
      throw new IllegalStateException( "no next element" );

    try
      {
      if( hasWaiting )
        return getTupleEntry();

      return getNext();
      }
    catch( Exception exception )
      {
      throw new TupleException( "unable to source from input identifier: " + loggableIdentifier.get(), exception );
      }
    finally
      {
      hasWaiting = false;
      }
    }

  @Override
  public void remove()
    {
    throw new UnsupportedOperationException( "may not remove elements from this iterator" );
    }

  @Override
  public void close() throws IOException
    {
    try
      {
      if( sourceCall != null )
        scheme.sourceCleanup( flowProcess, sourceCall );
      }
    finally
      {
      inputIterator.close();
      }
    }
  }
