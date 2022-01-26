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

package cascading.flow.stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cascading.flow.stream.duct.Duct;
import cascading.flow.stream.duct.Stage;

/**
 *
 */
public class TestSourceStage<Outgoing> extends Stage<Void, Outgoing>
  {
  List<Outgoing> values = new ArrayList<Outgoing>();

  public TestSourceStage( Collection<Outgoing> collection )
    {
    values.addAll( collection );
    }

  @Override
  public void receive( Duct previous, int ordinal, Void value )
    {
    next.start( this );

    for( Outgoing item : values )
      next.receive( this, 0, item );

    next.complete( this );
    }
  }
