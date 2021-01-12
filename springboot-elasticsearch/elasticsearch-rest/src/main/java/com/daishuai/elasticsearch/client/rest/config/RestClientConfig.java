package com.daishuai.elasticsearch.client.rest.config;

import com.daishuai.elasticsearch.client.rest.handler.DefaultEsFailureHandler;
import com.daishuai.elasticsearch.client.rest.handler.EsFailureHandler;
import com.daishuai.elasticsearch.client.rest.service.RestElasticSearchApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Daishuai
 * @date 2020/10/23 15:54
 */
@Configuration
@EnableConfigurationProperties(value = ElasticSearchProperties.class)
public class RestClientConfig {

    @Autowired
    private ElasticSearchProperties properties;

    @Autowired
    private ElasticSearchClientBuilder builder;

    @Bean
    @ConditionalOnMissingBean
    public RestElasticSearchApi elasticSearchApi() {
        return new RestElasticSearchApi(properties.getDefaultCluster(), builder);
    }

    @Bean
    @ConditionalOnMissingBean
    public EsFailureHandler esFailureHandler() {
        return new DefaultEsFailureHandler();
    }
}
