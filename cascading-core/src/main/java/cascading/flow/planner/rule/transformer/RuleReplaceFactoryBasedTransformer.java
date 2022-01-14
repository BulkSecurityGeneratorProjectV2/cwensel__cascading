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

package cascading.flow.planner.rule.transformer;

import cascading.flow.planner.iso.expression.ExpressionGraph;
import cascading.flow.planner.iso.transformer.ReplaceGraphFactoryBasedTransformer;
import cascading.flow.planner.rule.PlanPhase;
import cascading.flow.planner.rule.RuleExpression;
import cascading.flow.planner.rule.RuleTransformer;

/**
 * RuleTransformer that uses the supplied expression to fetch the FlowElement to replace.
 * The replacement FlowElement is created using the supplied factory.
 */
public class RuleReplaceFactoryBasedTransformer extends RuleTransformer
  {
  public RuleReplaceFactoryBasedTransformer( PlanPhase phase, RuleExpression ruleExpression, String factoryName )
    {
    super( phase, ruleExpression );

    ExpressionGraph matchExpression = ruleExpression.getMatchExpression();

    if( subGraphTransformer != null )
      graphTransformer = new ReplaceGraphFactoryBasedTransformer( subGraphTransformer, matchExpression, factoryName );
    else if( contractedTransformer != null )
      graphTransformer = new ReplaceGraphFactoryBasedTransformer( contractedTransformer, matchExpression, factoryName );
    else
      graphTransformer = new ReplaceGraphFactoryBasedTransformer( matchExpression, factoryName );
    }
  }
