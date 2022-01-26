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
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import cascading.nested.core.NestedRegexFilter;

/**
 * Class JSONRegexFilter provides for the ability to to filter a tuple stream based on the values in a JSON object.
 *
 * @see <a href=https://tools.ietf.org/html/draft-ietf-appsawg-json-pointer-03">draft-ietf-appsawg-json-pointer-03</a>
 * @see NestedRegexFilter for more details.
 */
public class JSONRegexFilter extends NestedRegexFilter
  {
  /**
   * Creates a new JSONRegexFilter instance.
   *
   * @param pointer of String
   * @param pattern of Pattern
   */
  @ConstructorProperties({"pointer", "pattern"})
  public JSONRegexFilter( String pointer, Pattern pattern )
    {
    this( pointer, Collections.singletonList( pattern ) );
    }

  /**
   * Creates a new JSONRegexFilter instance.
   *
   * @param pointer  of String
   * @param patterns of List
   */
  @ConstructorProperties({"pointer", "patterns"})
  public JSONRegexFilter( String pointer, List<Pattern> patterns )
    {
    super( JSONCoercibleType.TYPE, pointer, patterns, true );
    }

  /**
   * Creates a new JSONRegexFilter instance.
   *
   * @param coercibleType of JSONCoercibleType
   * @param pointer       of String
   * @param pattern       of Pattern
   */
  @ConstructorProperties({"coercibleType", "pointer", "pattern"})
  public JSONRegexFilter( JSONCoercibleType coercibleType, String pointer, Pattern pattern )
    {
    this( coercibleType, pointer, Collections.singletonList( pattern ) );
    }

  /**
   * Creates a new JSONRegexFilter instance.
   *
   * @param coercibleType of JSONCoercibleType
   * @param pointer       of String
   * @param patterns      of List
   */
  @ConstructorProperties({"coercibleType", "pointer", "patterns"})
  public JSONRegexFilter( JSONCoercibleType coercibleType, String pointer, List<Pattern> patterns )
    {
    super( coercibleType, pointer, patterns, true );
    }

  /**
   * Creates a new JSONRegexFilter instance.
   *
   * @param pointer           of String
   * @param pattern           of Pattern
   * @param failOnMissingNode of Boolean
   */
  @ConstructorProperties({"pointer", "pattern", "failOnMissingNode"})
  public JSONRegexFilter( String pointer, Pattern pattern, boolean failOnMissingNode )
    {
    this( pointer, Collections.singletonList( pattern ), failOnMissingNode );
    }

  /**
   * Creates a new JSONRegexFilter instance.
   *
   * @param pointer           of String
   * @param patterns          of List
   * @param failOnMissingNode of Boolean
   */
  @ConstructorProperties({"pointer", "patterns", "failOnMissingNode"})
  public JSONRegexFilter( String pointer, List<Pattern> patterns, boolean failOnMissingNode )
    {
    super( JSONCoercibleType.TYPE, pointer, patterns, failOnMissingNode );
    }

  /**
   * Creates a new JSONRegexFilter instance.
   *
   * @param coercibleType     of JSONCoercibleType
   * @param pointer           of String
   * @param pattern           of Pattern
   * @param failOnMissingNode of Boolean
   */
  @ConstructorProperties({"coercibleType", "pointer", "pattern", "failOnMissingNode"})
  public JSONRegexFilter( JSONCoercibleType coercibleType, String pointer, Pattern pattern, boolean failOnMissingNode )
    {
    this( coercibleType, pointer, Collections.singletonList( pattern ), failOnMissingNode );
    }

  /**
   * Creates a new JSONRegexFilter instance.
   *
   * @param coercibleType     of JSONCoercibleType
   * @param pointer           of String
   * @param patterns          of List
   * @param failOnMissingNode of Boolean
   */
  @ConstructorProperties({"coercibleType", "pointer", "patterns", "failOnMissingNode"})
  public JSONRegexFilter( JSONCoercibleType coercibleType, String pointer, List<Pattern> patterns, boolean failOnMissingNode )
    {
    super( coercibleType, pointer, patterns, failOnMissingNode );
    }
  }
