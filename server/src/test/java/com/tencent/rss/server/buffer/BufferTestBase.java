/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tencent.rss.server.buffer;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import com.tencent.rss.common.ShufflePartitionedBlock;
import com.tencent.rss.common.ShufflePartitionedData;
import com.tencent.rss.common.util.ChecksumUtils;
import com.tencent.rss.server.ShuffleServerMetrics;

public abstract class BufferTestBase {

  @BeforeAll
  public static void setup() {
    ShuffleServerMetrics.register();
  }

  @AfterAll
  public static void clear() {
    ShuffleServerMetrics.clear();
  }

  private static AtomicLong atomBlockId = new AtomicLong(0);

  protected ShufflePartitionedData createData(int len) {
    return createData(1, len);
  }

  protected ShufflePartitionedData createData(int partitionId, int len) {
    byte[] buf = new byte[len];
    new Random().nextBytes(buf);
    ShufflePartitionedBlock block = new ShufflePartitionedBlock(
        len, len, ChecksumUtils.getCrc32(buf), atomBlockId.incrementAndGet(), 0, buf);
    ShufflePartitionedData data = new ShufflePartitionedData(
        partitionId, new ShufflePartitionedBlock[]{block});
    return data;
  }

}
