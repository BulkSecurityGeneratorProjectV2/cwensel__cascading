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

package cascading.local.tap.neo4j;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class GraphSpecTest
  {
  @Test
  public void serialization() throws IOException
    {

    JSONGraphSpec graphSpec = new JSONGraphSpec( "Span" );

    graphSpec
      .setValuesPointer( "/0" )
      .addProperty( "trace_id", "/trace_id", null )
      .addProperty( "id", "/id", null );

    // only add one edge or the test will flip flop
    graphSpec
      .addEdge( "PARENT" )
      .addTargetLabel( "Span" )
      .addTargetProperty( "id", "/parent_id", null );

    ObjectMapper mapper = new ObjectMapper();

    String initJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString( graphSpec );

//        System.out.println("json = " + initJson);

    JSONGraphSpec result = mapper.readerFor( JSONGraphSpec.class ).readValue( initJson );

    String resultJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString( result );

//        System.out.println("resultJson = " + resultJson);

    assertEquals( initJson, resultJson );
    }
  }
