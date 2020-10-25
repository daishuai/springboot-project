package com.daishuai.elasticsearch.client.rest.config;

import com.daishuai.elasticsearch.client.rest.service.ElasticSearchApi;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.sniff.Sniffer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Daishuai
 * @date 2020/10/23 17:38
 */
@Component
public class ElasticSearchClientBuilder implements InitializingBean {

    @Autowired
    private ElasticSearchProperties properties;


    private static final Map<String, RestHighLevelClient> CLIENT_MAP = new ConcurrentHashMap<>();


    public RestHighLevelClient builderClient(String clusterName) {
        return CLIENT_MAP.get(clusterName);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, ElasticSearchProperties.ClusterInfo> clusters = properties.getClusters();
        clusters.forEach((clusterName, clusterInfo) -> {
            List<HttpHost> httpHosts = new LinkedList<>();
            String restNodes = clusterInfo.getRestNodes();
            String[] hostAndPorts = StringUtils.split( restNodes,",");
            for (String hostAndPort : hostAndPorts) {
                String schema = null;
                int a = hostAndPort.indexOf("://");
                if (a != -1) {
                    schema = hostAndPort.substring(0, a);
                    hostAndPort = hostAndPort.substring(a + 3);
                }
                String host;
                int port = -1;
                String[] strs = hostAndPort.split(":");
                if (strs.length == 1) {
                    host = strs[0];
                } else {
                    host = strs[0];
                    port = Integer.parseInt(strs[1]);
                }
                httpHosts.add(new HttpHost(host, port, schema));
            }
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            String username = clusterInfo.getUsername();
            String password = clusterInfo.getPassword();
            if (!StringUtils.isAnyBlank(username, password)) {
                credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
            }
            RestClientBuilder restClientBuilder = RestClient.builder(httpHosts.toArray(new HttpHost[0]));
            String pathPrefix = clusterInfo.getPathPrefix();
            if (StringUtils.isNotBlank(pathPrefix)) {
                restClientBuilder.setPathPrefix(pathPrefix);
            }
            RestClient restClient = restClientBuilder.setHttpClientConfigCallback(
                    httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                            .setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(clusterInfo.getThreadNum()).build())
            ).setRequestConfigCallback(
                    requestConfigBuilder -> requestConfigBuilder.setConnectTimeout(clusterInfo.getConnectionTimeout())
                            .setSocketTimeout(clusterInfo.getConnectionTimeout())
                            .setConnectionRequestTimeout(clusterInfo.getConnectionTimeout())
            ).build();
            if (Boolean.TRUE.equals(clusterInfo.getSniff())) {
                Sniffer.builder(restClient).setSniffIntervalMillis(clusterInfo.getSniffIntervalMillis()).build();
            }
            RestHighLevelClient client = new RestHighLevelClient(restClientBuilder);
            CLIENT_MAP.put(clusterName, client);
        });
    }
}
