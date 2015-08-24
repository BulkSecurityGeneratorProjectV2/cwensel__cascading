/*
 * Copyright (c) 2007-2015 Concurrent, Inc. All Rights Reserved.
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

package cascading.stats.hadoop;

import cascading.stats.CascadingStats;
import cascading.stats.CounterCache;
import org.apache.hadoop.conf.Configuration;

/**
 *
 */
public abstract class HadoopCounterCache<JobStatus, Counters> extends CounterCache<Configuration, JobStatus, Counters>
  {
  public HadoopCounterCache( CascadingStats stats, Configuration configuration )
    {
    super( stats, configuration );
    }

  protected int getIntProperty( String property, int defaultValue )
    {
    return configuration.getInt( property, defaultValue );
    }
  }
