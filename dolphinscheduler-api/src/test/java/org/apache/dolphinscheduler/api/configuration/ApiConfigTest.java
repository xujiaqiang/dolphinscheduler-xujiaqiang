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

package org.apache.dolphinscheduler.api.configuration;

import org.apache.dolphinscheduler.api.controller.AbstractControllerTest;

import org.apache.commons.collections4.MapUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ApiConfigTest extends AbstractControllerTest {

    @Autowired
    private ApiConfig apiConfig;

    @Test
    public void testIsAuditEnable() {
        Assertions.assertTrue(apiConfig.isAuditEnable());
    }

    @Test
    public void testGetTrafficControlConfig() {
        ApiConfig.TrafficConfiguration trafficControl = apiConfig.getTrafficControl();

        Assertions.assertFalse(trafficControl.isGlobalSwitch());
        Assertions.assertEquals(299, (int) trafficControl.getMaxGlobalQpsRate());
        Assertions.assertFalse(trafficControl.isTenantSwitch());
        Assertions.assertEquals(9, (int) trafficControl.getDefaultTenantQpsRate());
        Assertions.assertTrue(MapUtils.isEmpty(trafficControl.getCustomizeTenantQpsRate()));

    }

    @Test
    public void testGetPythonGateway() {
        ApiConfig.PythonGatewayConfiguration pythonGateway = apiConfig.getPythonGateway();

        Assertions.assertFalse(pythonGateway.isEnabled());
    }

}
