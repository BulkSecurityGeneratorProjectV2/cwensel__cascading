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

package cascading.flow.stream.duct;

/**
 *
 */
public class Fork<Incoming, Outgoing> extends Duct<Incoming, Outgoing>
  {
  protected final Duct[] allNext; // ordinal in array must match ordinal expected downstream

  public Fork( Duct[] allNext )
    {
    this.allNext = allNext;
    }

  public Duct[] getAllNext()
    {
    return allNext;
    }

  @Override
  public Duct getNext()
    {
    // doesn't matter, next after a fork always takes value fields, not grouping fields.
    return allNext[ 0 ];
    }

  @Override
  public void start( Duct previous )
    {
    for( int i = 0; i < allNext.length; i++ )
      allNext[ i ].start( previous );
    }

  @Override
  public void receive( Duct previous, int ordinal, Incoming incoming )
    {
    for( int i = 0; i < allNext.length; i++ )
      allNext[ i ].receive( previous, i, incoming );
    }

  @Override
  public void complete( Duct previous )
    {
    for( int i = 0; i < allNext.length; i++ )
      allNext[ i ].complete( previous );
    }
  }
