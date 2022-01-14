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

package cascading.nested.json;

import java.beans.ConstructorProperties;

import cascading.nested.core.BuildSpec;
import cascading.nested.core.NestedBaseBuildAggregator;
import cascading.tuple.Fields;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * Class JSONBuildAsAggregator provides for the ability to create new JSON objects from aggregated tuple values.
 *
 * @see <a href=https://tools.ietf.org/html/draft-ietf-appsawg-json-pointer-03">draft-ietf-appsawg-json-pointer-03</a>
 * @see NestedBaseBuildAggregator for more details.
 */
public class JSONBuildAsAggregator extends NestedBaseBuildAggregator<JsonNode, ArrayNode>
  {
  /**
   * Creates a new JSONBuildAsAggregator instance.
   *
   * @param fieldDeclaration of Fields
   * @param buildSpecs       of BuildSpec...
   */
  @ConstructorProperties({"fieldDeclaration", "buildSpecs"})
  public JSONBuildAsAggregator( Fields fieldDeclaration, BuildSpec... buildSpecs )
    {
    super( JSONCoercibleType.TYPE, fieldDeclaration, buildSpecs );
    }

  /**
   * Creates a new JSONBuildAsAggregator instance.
   *
   * @param coercibleType    of JSONCoercibleType
   * @param fieldDeclaration of Fields
   * @param buildSpecs       of BuildSpec...
   */
  @ConstructorProperties({"coercibleType", "fieldDeclaration", "buildSpecs"})
  public JSONBuildAsAggregator( JSONCoercibleType coercibleType, Fields fieldDeclaration, BuildSpec... buildSpecs )
    {
    super( coercibleType, fieldDeclaration, buildSpecs );
    }

  @Override
  protected boolean isInto()
    {
    return false;
    }
  }
