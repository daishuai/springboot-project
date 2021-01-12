package com.daishuai.elasticsearch.client.rest.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author Daishuai
 * @date 2020/10/23 16:45
 */
@Data
@ConfigurationProperties(prefix = "spring.elasticsearch")
public class ElasticSearchProperties {

    private String defaultCluster;

    private Map<String, ClusterInfo> clusters;

    private BulkProcessorConfig bulkProcessor;


    public static class ClusterInfo {
        private String restNodes;

        private String pathPrefix;

        private int threadNum;

        private String username;

        private String password;

        private boolean sniff;

        private int sniffIntervalMillis;

        private int connectionTimeout;

        public String getRestNodes() {
            return restNodes;
        }

        public void setRestNodes(String restNodes) {
            this.restNodes = restNodes;
        }

        public String getPathPrefix() {
            return pathPrefix;
        }

        public void setPathPrefix(String pathPrefix) {
            this.pathPrefix = pathPrefix;
        }

        public int getThreadNum() {
            return threadNum;
        }

        public void setThreadNum(int threadNum) {
            this.threadNum = threadNum;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean getSniff() {
            return sniff;
        }

        public void setSniff(boolean sniff) {
            this.sniff = sniff;
        }

        public int getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public int getSniffIntervalMillis() {
            return sniffIntervalMillis;
        }

        public void setSniffIntervalMillis(int sniffIntervalMillis) {
            this.sniffIntervalMillis = sniffIntervalMillis;
        }
    }

    @Data
    public static class BulkProcessorConfig {

        private int bulkActions = 1000;

        private int bulkSize = 5;

        private int flushInterval = 5;

        private int concurrentRequests = 5;
    }
}
