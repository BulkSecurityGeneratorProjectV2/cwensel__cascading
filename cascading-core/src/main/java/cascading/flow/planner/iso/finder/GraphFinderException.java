/*
 * Copyright (c) 2016-2017 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 * Copyright (c) 2007-2017 Xplenty, Inc. All Rights Reserved.
 *
 * Project and contact information: http://www.cascading.org/
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

package cascading.flow.planner.iso.finder;

import cascading.CascadingException;
import cascading.util.Util;
import org.jgrapht.Graph;

/**
 *
 */
public class GraphFinderException extends CascadingException
  {
  private Graph graph;

  public GraphFinderException()
    {
    }

  public GraphFinderException( String message )
    {
    super( message );
    }

  public GraphFinderException( String message, Throwable throwable )
    {
    super( message, throwable );
    }

  public GraphFinderException( Throwable throwable )
    {
    super( throwable );
    }

  public GraphFinderException( String message, Graph graph )
    {
    this( message );
    this.graph = graph;
    }

  public Graph getGraph()
    {
    return graph;
    }

  public void writeDOT( String filename )
    {
    Util.invokeInstanceMethod( graph, "writeDOT", new Object[]{filename}, new Class[]{String.class} );
    }
  }
