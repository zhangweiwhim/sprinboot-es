package com.zhangweiwhim.es.config;

import org.apache.http.HttpHost;


import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;

import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class ElasticsearchConfig {

    //    @Value("${elasticsearch.host}")
//    private String host;
//
    @Value("${elasticsearch.port}")
    private int port;

    @Value("${elasticsearch.username}")
    private String userName;

    @Value("${elasticsearch.password}")
    private String password;

    @Value("#{'${elasticsearch.hosts}'.split(',')}")
    private String[] hosts;

//    @Value("#{'${elasticsearch.ports}'.split(',')}")
//    private int[] ports;

    @Value("${elasticsearch.httpType}")
    private String httpType;

    @Value("${elasticsearch.conn_time_out}")
    private int connTimeOut;

    @Value("${elasticsearch.socket_time_out}")
    private int socketTimeOut;

    @Value("${elasticsearch.conn_request_time_out}")
    private int connRequestTimeOut;

    @Value("${elasticsearch.max_conn_num}")
    private int maxConnNum;

    @Value("${elasticsearch.max_conn_per_route}")
    private int maxConnPerRoute;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient restClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(userName, password));


        List<HttpHost> httpHosts = new ArrayList<>();
        for (String host : hosts) {
            httpHosts.add(new HttpHost(host, port));
        }

        RestClientBuilder builder = RestClient.builder(httpHosts.toArray(new HttpHost[httpHosts.size()])).setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectionRequestTimeout(connRequestTimeOut);
            requestConfigBuilder.setSocketTimeout(socketTimeOut);
            requestConfigBuilder.setConnectTimeout(connTimeOut);
            return requestConfigBuilder;
        }).setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(maxConnNum);
            httpClientBuilder.setMaxConnPerRoute(maxConnPerRoute);
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            return httpClientBuilder;
        });
        return new RestHighLevelClient(builder);
    }


}
