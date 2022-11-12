package com.example.webflux.springbootwebflux.config;

import dev.miku.r2dbc.mysql.MySqlConnectionConfiguration;
import dev.miku.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version 1.0
 * @Aythor lucksoul 王吉慧
 * @date 2022/10/12 17:59
 * @description
 */
@Configuration
public class R2dbcConfig {
    @Bean
    public ConnectionFactory connectionFactory() {
        return MySqlConnectionFactory.from(MySqlConnectionConfiguration.builder()
                .host("127.0.0.1")         // 主机地址
                .port(3306)                // 端口
                .username("root")          // 用户名
                .password("331224")        // 密码
                .database("reactive_db")  // 连接的数据库名称
                .build());
    }

}
