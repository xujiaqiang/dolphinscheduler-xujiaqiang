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

package org.apache.dolphinscheduler.dao.utils;

import org.apache.dolphinscheduler.common.enums.Flag;
import org.apache.dolphinscheduler.dao.entity.MonitorRecord;
import org.apache.dolphinscheduler.spi.enums.DbType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostgreSQLPerformance extends BaseDBPerformance {

    /**
     * get monitor record
     *
     * @param conn connection
     * @return MonitorRecord
     */
    @Override
    public MonitorRecord getMonitorRecord(Connection conn) {
        MonitorRecord monitorRecord = new MonitorRecord();
        monitorRecord.setDate(new Date());
        monitorRecord.setState(Flag.YES);
        monitorRecord.setDbType(DbType.POSTGRESQL);

        try (Statement pstmt = conn.createStatement()) {
            try (ResultSet rs1 = pstmt.executeQuery("select count(*) from pg_stat_activity;")) {
                if (rs1.next()) {
                    monitorRecord.setThreadsConnections(rs1.getInt("count"));
                }
            }

            try (ResultSet rs2 = pstmt.executeQuery("show max_connections")) {
                if (rs2.next()) {
                    monitorRecord.setMaxConnections(rs2.getInt("max_connections"));
                }
            }

            try (
                    ResultSet rs3 =
                            pstmt.executeQuery("select count(*) from pg_stat_activity pg where pg.state = 'active';")) {
                if (rs3.next()) {
                    monitorRecord.setThreadsRunningConnections(rs3.getInt("count"));
                }
            }
        } catch (Exception e) {
            monitorRecord.setState(Flag.NO);
            log.error("SQLException ", e);
        }
        return monitorRecord;
    }
}
