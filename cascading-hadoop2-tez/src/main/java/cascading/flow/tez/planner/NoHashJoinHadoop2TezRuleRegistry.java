/*
 * Copyright (c) 2007-2014 Concurrent, Inc. All Rights Reserved.
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

package cascading.flow.tez.planner;

import cascading.flow.planner.rule.RuleRegistry;
import cascading.flow.planner.rule.assertion.BufferAfterEveryAssert;
import cascading.flow.planner.rule.assertion.EveryAfterBufferAssert;
import cascading.flow.planner.rule.assertion.LoneGroupAssert;
import cascading.flow.planner.rule.assertion.MissingGroupAssert;
import cascading.flow.planner.rule.assertion.SplitBeforeEveryAssert;
import cascading.flow.planner.rule.partitioner.WholeGraphStepPartitioner;
import cascading.flow.planner.rule.transformer.ApplyAssertionLevelTransformer;
import cascading.flow.planner.rule.transformer.ApplyDebugLevelTransformer;
import cascading.flow.planner.rule.transformer.RemoveNoOpPipeTransformer;
import cascading.flow.tez.planner.rule.assertion.NoHashJoinAssert;
import cascading.flow.tez.planner.rule.partitioner.ConsecutiveGroupOrMergesNodePartitioner;
import cascading.flow.tez.planner.rule.partitioner.SplitJoinBoundariesNodeRePartitioner;
import cascading.flow.tez.planner.rule.partitioner.TopDownBoundariesNodePartitioner;
import cascading.flow.tez.planner.rule.transformer.BoundaryBalanceCheckpointTransformer;
import cascading.flow.tez.planner.rule.transformer.BoundaryBalanceGroupSplitMergeGroupTransformer;

/**
 * The NoHashJoinHadoop2TezRuleRegistry assumes the plan has no {@link cascading.pipe.HashJoin} Pipes in the
 * assembly, otherwise an planner failure will be thrown.
 * <p/>
 * This rule registry can be used if the default registry is failing or producing less than optimal plans.
 *
 * @see cascading.flow.tez.planner.HashJoinHadoop2TezRuleRegistry
 */
public class NoHashJoinHadoop2TezRuleRegistry extends RuleRegistry
  {
  public NoHashJoinHadoop2TezRuleRegistry()
    {
//    enableDebugLogging();

    // PreBalance
    addRule( new NoHashJoinAssert() ); // fail if we encounter a HashJoin

    addRule( new LoneGroupAssert() );
    addRule( new MissingGroupAssert() );
    addRule( new BufferAfterEveryAssert() );
    addRule( new EveryAfterBufferAssert() );
    addRule( new SplitBeforeEveryAssert() );

    addRule( new BoundaryBalanceGroupSplitMergeGroupTransformer() );
    addRule( new BoundaryBalanceCheckpointTransformer() );

    // PreResolve
    addRule( new RemoveNoOpPipeTransformer() );
    addRule( new ApplyAssertionLevelTransformer() );
    addRule( new ApplyDebugLevelTransformer() );

    // PostResolve

    // PartitionSteps
    addRule( new WholeGraphStepPartitioner() );

    // PostSteps

    // PartitionNodes
    addRule( new TopDownBoundariesNodePartitioner() );
    addRule( new ConsecutiveGroupOrMergesNodePartitioner() );
    addRule( new SplitJoinBoundariesNodeRePartitioner() ); // testCoGroupSelf - compensates for tez-1190

    // PostNodes
    }
  }