/*
 * Copyright (c) 2016-2021 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
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

package cascading.nested.json;

import java.beans.ConstructorProperties;

import cascading.nested.core.CopySpec;
import cascading.nested.core.NestedBaseCopyFunction;
import cascading.tuple.Fields;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * Class JSONCopyIntoFunction provides for the ability to update an existing JSON objects from an existing
 * JSON object.
 *
 * @see <a href=https://tools.ietf.org/html/draft-ietf-appsawg-json-pointer-03">draft-ietf-appsawg-json-pointer-03</a>
 * @see NestedBaseCopyFunction for more details.
 */
public class JSONCopyIntoFunction extends NestedBaseCopyFunction<JsonNode, ArrayNode>
  {
  /**
   * Creates a new JSONCopyIntoFunction instance.
   *
   * @param fieldDeclaration of Fields
   * @param copySpecs        of CopySpec...
   */
  @ConstructorProperties({"fieldDeclaration", "copySpecs"})
  public JSONCopyIntoFunction( Fields fieldDeclaration, CopySpec... copySpecs )
    {
    super( JSONCoercibleType.TYPE, fieldDeclaration, copySpecs );
    }

  /**
   * Creates a new JSONCopyIntoFunction instance.
   *
   * @param coercibleType    of JSONCoercibleType
   * @param fieldDeclaration of Fields
   * @param copySpecs        of CopySpec...
   */
  @ConstructorProperties({"coercibleType", "fieldDeclaration", "copySpecs"})
  public JSONCopyIntoFunction( JSONCoercibleType coercibleType, Fields fieldDeclaration, CopySpec... copySpecs )
    {
    super( coercibleType, fieldDeclaration, copySpecs );
    }

  @Override
  protected boolean isInto()
    {
    return true;
    }
  }
