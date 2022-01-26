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

package cascading.flow.planner.iso.expression;

import cascading.flow.FlowElement;
import cascading.flow.planner.PlannerContext;
import cascading.flow.planner.graph.ElementGraph;

/**
 *
 */
public class NotElementExpression extends ElementExpression
  {
  public static ElementExpression not( ElementExpression elementMatcher )
    {
    return new NotElementExpression( elementMatcher );
    }

  ElementExpression delegate;

  public NotElementExpression( ElementExpression delegate )
    {
    this.delegate = delegate;
    }

  @Override
  public boolean applies( PlannerContext plannerContext, ElementGraph elementGraph, FlowElement flowElement )
    {
    return !delegate.applies( plannerContext, elementGraph, flowElement );
    }

  @Override
  public ElementCapture getCapture()
    {
    return delegate.getCapture();
    }

  @Override
  public String toString()
    {
    final StringBuilder sb = new StringBuilder( "Not{" );
    sb.append( delegate );
    sb.append( '}' );
    return sb.toString();
    }
  }
