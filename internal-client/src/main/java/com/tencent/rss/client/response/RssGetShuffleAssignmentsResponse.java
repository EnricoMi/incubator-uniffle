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

package com.tencent.rss.client.response;

import java.util.List;
import java.util.Map;

import com.tencent.rss.common.PartitionRange;
import com.tencent.rss.common.ShuffleServerInfo;

public class RssGetShuffleAssignmentsResponse extends ClientResponse {

  private Map<Integer, List<ShuffleServerInfo>> partitionToServers;
  private Map<ShuffleServerInfo, List<PartitionRange>> serverToPartitionRanges;

  public RssGetShuffleAssignmentsResponse(ResponseStatusCode statusCode) {
    super(statusCode);
  }

  public Map<Integer, List<ShuffleServerInfo>> getPartitionToServers() {
    return partitionToServers;
  }

  public void setPartitionToServers(
      Map<Integer, List<ShuffleServerInfo>> partitionToServers) {
    this.partitionToServers = partitionToServers;
  }

  public Map<ShuffleServerInfo, List<PartitionRange>> getServerToPartitionRanges() {
    return serverToPartitionRanges;
  }

  public void setServerToPartitionRanges(
      Map<ShuffleServerInfo, List<PartitionRange>> serverToPartitionRanges) {
    this.serverToPartitionRanges = serverToPartitionRanges;
  }
}
