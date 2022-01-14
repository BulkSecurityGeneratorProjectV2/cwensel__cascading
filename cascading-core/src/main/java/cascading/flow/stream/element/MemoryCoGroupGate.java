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

package cascading.flow.stream.element;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cascading.flow.FlowProcess;
import cascading.flow.stream.StopDataNotificationException;
import cascading.flow.stream.duct.Duct;
import cascading.pipe.Splice;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;
import cascading.tuple.Tuples;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class MemoryCoGroupGate extends MemorySpliceGate
  {
  private static final Logger LOG = LoggerFactory.getLogger( MemoryCoGroupGate.class );

  public MemoryCoGroupGate( FlowProcess flowProcess, Splice splice )
    {
    super( flowProcess, splice );
    }

  @Override
  protected boolean isBlockingStreamed()
    {
    return true;
    }

  @Override
  public void start( Duct previous )
    {
    }

  @Override
  public void receive( Duct previous, int ordinal, TupleEntry incomingEntry )
    {
    Tuple valuesTuple = incomingEntry.getTupleCopy();
    Tuple groupTuple = keyBuilder[ ordinal ].makeResult( valuesTuple, null ); // view on valuesTuple

    groupTuple = getDelegatedTuple( groupTuple ); // wrap so hasher/comparator is honored

    keys.add( groupTuple );
    keyValues[ ordinal ].get( groupTuple ).add( valuesTuple );
    }

  @Override
  public void complete( Duct previous )
    {
    if( count.decrementAndGet() != 0 )
      return;

    next.start( this );

    Collection<Tuple>[] collections = new Collection[ keyValues.length ];
    Iterator<Tuple> keyIterator = keys.iterator();

    Set<Tuple> seenNulls = new HashSet<Tuple>();

    while( keyIterator.hasNext() )
      {
      Tuple keysTuple = keyIterator.next();

      keyIterator.remove();

      // provides sql like semantics
      if( nullsAreNotEqual && Tuples.frequency( keysTuple, null ) != 0 )
        {
        if( seenNulls.contains( keysTuple ) )
          continue;

        seenNulls.add( keysTuple );

        for( int i = 0; i < keyValues.length; i++ )
          {
          Collection<Tuple> values = keyValues[ i ].remove( keysTuple );

          if( values == null )
            continue;

          for( int j = 0; j < keyValues.length; j++ )
            collections[ j ] = Collections.emptyList();

          collections[ i ] = values;

          try
            {
            push( collections, keysTuple );
            }
          catch( StopDataNotificationException exception )
            {
            LOG.info( "received stop data notification: {}", exception.getMessage() );
            break;
            }
          }
        }
      else
        {
        // drain the keys and keyValues collections to preserve memory
        for( int i = 0; i < keyValues.length; i++ )
          {
          collections[ i ] = keyValues[ i ].remove( keysTuple );

          if( collections[ i ] == null )
            collections[ i ] = Collections.emptyList();
          }

        try
          {
          push( collections, keysTuple );
          }
        catch( StopDataNotificationException exception )
          {
          LOG.info( "received stop data notification: {}", exception.getMessage() );
          break;
          }
        }
      }

    keys = createKeySet();
    keyValues = createKeyValuesArray();

    count.set( numIncomingEventingPaths );

    next.complete( this );
    }

  private void push( Collection<Tuple>[] collections, Tuple keysTuple )
    {
    closure.reset( collections );

    keyEntry.setTuple( closure.getGroupTuple( keysTuple ) );

    // create Closure type here
    tupleEntryIterator.reset( splice.getJoiner().getIterator( closure ) );

    next.receive( this, 0, grouping );
    }
  }
