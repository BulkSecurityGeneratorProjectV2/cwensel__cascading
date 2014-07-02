/*
 * Copyright (c) 2007-2017 Concurrent, Inc. All Rights Reserved.
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

package cascading.management.annotation;

/**
 * Sanitize is an interface to be used in conjunction with {@link cascading.management.annotation.PropertySanitze}.
 * Value sanitization is applied to a value for a given visibility.
 * <br>
 * Implementations of this interfaces must provide a default no-args Constructor.
 */
public interface Sanitize
  {
  /**
   * Applies the custom sanitization to the given value for the given visibility.
   *
   * @param visibility The visibility of the property value.
   * @param value      The value to sanitize.
   * @return A sanitized version of the value.
   */
  String apply( Visibility visibility, String value );
  }
