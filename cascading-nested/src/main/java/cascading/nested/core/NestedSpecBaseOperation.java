/*
 * Copyright (c) 2016-2017 Chris K Wensel. All Rights Reserved.
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

package cascading.nested.core;

import cascading.operation.FunctionCall;
import cascading.operation.OperationCall;
import cascading.tuple.Fields;
import cascading.tuple.TupleEntry;

/**
 *
 */
public abstract class NestedSpecBaseOperation<Node, Result, Context> extends NestedBaseOperation<Node, Result, Context>
  {
  public NestedSpecBaseOperation( NestedCoercibleType<Node, Result> nestedCoercibleType, Fields fieldDeclaration )
    {
    super( nestedCoercibleType, fieldDeclaration );
    }

  public NestedSpecBaseOperation( NestedCoercibleType<Node, Result> nestedCoercibleType, int numArgs, Fields fieldDeclaration )
    {
    super( nestedCoercibleType, numArgs, fieldDeclaration );
    }

  protected abstract boolean isInto();

  protected Node getResultNode( OperationCall<?> operationCall )
    {
    if( isInto() )
      return getArgument( operationCall );
    else
      return getRootNode();
    }

  protected Node getArgument( OperationCall<?> operationCall )
    {
    TupleEntry arguments = ( (FunctionCall<Object>) operationCall ).getArguments();

    Node object = (Node) arguments.getObject( arguments.size() - 1, getCoercibleType() );

    return deepCopy( object );
    }
  }
