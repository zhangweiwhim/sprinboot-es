package com.zhangweiwhim.es.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Description: sprinboot-es
 * Created by zhangwei on 2021/2/3 14:24
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration(); // 设置允许跨域请求的域名
        config.addAllowedOriginPattern("*");// 2.4新版本后使用
        //config.addAllowedOrigin("*"); // 是否允许证书 不再默认开启
        config.setAllowCredentials(true); // 设置允许的方法
        config.addAllowedMethod("GET"); // 允许任何头
        config.addAllowedMethod("POST"); // 允许任何头
        config.addAllowedMethod("OPTIONS"); // 允许任何头
        config.addAllowedHeader("*"); // 预检请求头信息描述
        config.addExposedHeader("token");
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
    }


}
