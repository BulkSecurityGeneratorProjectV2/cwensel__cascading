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

package cascading.tuple.hadoop.io;

import java.io.IOException;
import java.util.Map;

import cascading.tuple.hadoop.TupleSerialization;
import cascading.tuple.io.IndexTuple;

public class IndexTupleSerializer<T extends IndexTuple> extends BaseSerializer<T>
  {
  protected Map<Integer, Class[]> typeMap;

  public IndexTupleSerializer( TupleSerialization.SerializationElementWriter elementWriter )
    {
    super( elementWriter );
    }

  public void serialize( T tuple ) throws IOException
    {
    outputStream.writeIntInternal( tuple.getIndex() );

    Class[] types = getTypesFor( tuple.getIndex() );

    if( types == null )
      outputStream.writeUnTyped( tuple.getTuple() );
    else
      outputStream.writeTyped( types, tuple.getTuple() );
    }

  protected Class[] getTypesFor( int ordinal )
    {
    if( typeMap == null )
      return null;

    return typeMap.get( ordinal );
    }
  }