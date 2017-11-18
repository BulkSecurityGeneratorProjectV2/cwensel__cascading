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

package cascading.flow.planner.graph;

import java.util.IdentityHashMap;

import cascading.flow.FlowElement;
import cascading.flow.planner.Scope;
import cascading.util.EnumMultiMap;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.specifics.DirectedEdgeContainer;
import org.jgrapht.graph.specifics.DirectedSpecifics;

import static cascading.flow.planner.graph.ElementGraphs.directed;

/**
 *
 */
public class ElementMultiGraph extends BaseAnnotatedElementGraph implements ElementGraph, AnnotatedGraph
  {
  public ElementMultiGraph()
    {
    graph = new DirectedMultiGraph();
    }

  public ElementMultiGraph( ElementGraph parent )
    {
    graph = new DirectedMultiGraph( directed( parent ) );

    addParentAnnotations( parent );
    }

  public ElementMultiGraph( ElementGraph parent, EnumMultiMap annotations )
    {
    this( parent );

    getAnnotations().addAll( annotations );
    }

  @Override
  public ElementGraph copyElementGraph()
    {
    return new ElementMultiGraph( this );
    }

  protected class DirectedMultiGraph extends DirectedMultigraph<FlowElement, Scope>
    {
    public DirectedMultiGraph()
      {
      super( Scope.class );
      }

    public DirectedMultiGraph( Graph<FlowElement, Scope> parent )
      {
      this();

      // safe to assume there are no unconnected vertices
      for( Scope edge : parent.edgeSet() )
        {
        FlowElement s = parent.getEdgeSource( edge );
        FlowElement t = parent.getEdgeTarget( edge );
        addVertex( s );
        addVertex( t );
        addEdge( s, t, edge );
        }
      }

    @Override
    protected DirectedSpecifics createSpecifics( boolean directed )
      {
      return new DirectedSpecifics( this, new IdentityHashMap<FlowElement, DirectedEdgeContainer<FlowElement, Scope>>() );
      }
    }
  }
