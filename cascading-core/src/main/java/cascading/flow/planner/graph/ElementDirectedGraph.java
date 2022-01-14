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

package cascading.flow.planner.graph;

import java.util.IdentityHashMap;

import cascading.flow.FlowElement;
import cascading.flow.planner.Scope;
import cascading.util.EnumMultiMap;
import org.jgrapht.Graphs;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.specifics.DirectedEdgeContainer;
import org.jgrapht.graph.specifics.DirectedSpecifics;

import static cascading.flow.planner.graph.ElementGraphs.directed;

/**
 *
 */
public class ElementDirectedGraph extends BaseAnnotatedElementGraph implements AnnotatedGraph
  {
  public ElementDirectedGraph()
    {
    this.graph = new DirectedGraph();
    }

  public ElementDirectedGraph( ElementGraph parent )
    {
    if( parent == null )
      {
      this.graph = new DirectedGraph();
      return;
      }

    this.graph = new DirectedGraph( directed( parent ) );

    addParentAnnotations( parent );
    }

  public ElementDirectedGraph( ElementGraph parent, EnumMultiMap annotations )
    {
    this( parent );

    getAnnotations().addAll( annotations );
    }

  @Override
  public ElementGraph copyElementGraph()
    {
    return new ElementDirectedGraph( this );
    }

  private class DirectedGraph extends SimpleDirectedGraph<FlowElement, Scope>
    {
    public DirectedGraph()
      {
      super( Scope.class );
      }

    public DirectedGraph( org.jgrapht.Graph<FlowElement, Scope> parent )
      {
      this();

      Graphs.addGraph( this, parent );
      }

    @Override
    protected DirectedSpecifics createSpecifics( boolean directed )
      {
      return new DirectedSpecifics( this, new IdentityHashMap<FlowElement, DirectedEdgeContainer<FlowElement, Scope>>() );
      }
    }
  }
