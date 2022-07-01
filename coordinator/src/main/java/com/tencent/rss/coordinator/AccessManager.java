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

package com.tencent.rss.coordinator;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tencent.rss.common.util.Constants;
import com.tencent.rss.common.util.RssUtils;

public class AccessManager {

  private static final Logger LOG = LoggerFactory.getLogger(AccessManager.class);

  private final CoordinatorConf coordinatorConf;
  private final ClusterManager clusterManager;
  private final Configuration hadoopConf;
  private List<AccessChecker> accessCheckers = Lists.newArrayList();

  public AccessManager(
      CoordinatorConf conf, ClusterManager clusterManager, Configuration hadoopConf) throws RuntimeException {
    this.coordinatorConf = conf;
    this.clusterManager = clusterManager;
    this.hadoopConf = hadoopConf;
    init();
  }

  private void init() throws RuntimeException {
    String checkers = coordinatorConf.get(CoordinatorConf.COORDINATOR_ACCESS_CHECKERS);
    if (StringUtils.isEmpty(checkers)) {
      LOG.warn("Access checkers is empty, will not init any checkers.");
      return;
    }

    String[] names = checkers.trim().split(",");
    accessCheckers = RssUtils.loadExtensions(AccessChecker.class, Arrays.asList(names), this);
  }

  public AccessCheckResult handleAccessRequest(AccessInfo accessInfo) {
    CoordinatorMetrics.counterTotalAccessRequest.inc();
    for (AccessChecker checker : accessCheckers) {
      AccessCheckResult accessCheckResult = checker.check(accessInfo);
      if (!accessCheckResult.isSuccess()) {
        return accessCheckResult;
      }
    }

    return new AccessCheckResult(true, Constants.COMMON_SUCCESS_MESSAGE);
  }

  public CoordinatorConf getCoordinatorConf() {
    return coordinatorConf;
  }

  public ClusterManager getClusterManager() {
    return clusterManager;
  }

  public List<AccessChecker> getAccessCheckers() {
    return accessCheckers;
  }

  public Configuration getHadoopConf() {
    return hadoopConf;
  }

  public void close() throws IOException {
    for (AccessChecker checker : accessCheckers) {
      checker.close();
    }
  }
}
