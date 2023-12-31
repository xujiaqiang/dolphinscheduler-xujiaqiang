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

package org.apache.dolphinscheduler.common.log;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SensitiveDataConverterTest {

    private final Logger logger = LoggerFactory.getLogger(SensitiveDataConverterTest.class);

    private final String logMsg = "{\"address\":\"jdbc:mysql://192.168.xx.xx:3306\","
            + "\"database\":\"carbond\","
            + "\"jdbcUrl\":\"jdbc:mysql://192.168.xx.xx:3306/ods\","
            + "\"user\":\"view\","
            + "\"password\":\"view1\"}";

    private final String maskLogMsg = "{\"address\":\"jdbc:mysql://192.168.xx.xx:3306\","
            + "\"database\":\"carbond\","
            + "\"jdbcUrl\":\"jdbc:mysql://192.168.xx.xx:3306/ods\","
            + "\"user\":\"view\","
            + "\"password\":\"*****\"}";

    /**
     * mask sensitive logMsg - sql task datasource password
     */
    @Test
    public void testPwdLogMsgConverter() {
        final String maskedLog = SensitiveDataConverter.maskSensitiveData(logMsg);

        logger.info("original parameter : {}", logMsg);
        logger.info("masked parameter : {}", maskedLog);

        Assertions.assertEquals(maskLogMsg, maskedLog);

    }

    @Test
    public void testPostJdbcInfoLogMsgConverter() {
        String POST_JDBC_INFO_REGEX = "(?<=(post jdbc info:)).*(?=)";
        SensitiveDataConverter.addMaskPattern(POST_JDBC_INFO_REGEX);
        String postJdbcInfoLogMsg = "post jdbc info:clickhouse,jdbc:clickhouse://127.0.0.1:8123/td_cdp,admin,123%@@56";
        final String maskedLog = SensitiveDataConverter.maskSensitiveData(postJdbcInfoLogMsg);
        String expectedMsg = "post jdbc info:*****************************************************************";
        Assertions.assertEquals(expectedMsg, maskedLog);
    }

}
