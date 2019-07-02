package com.sfy.quartz;

import com.alibaba.druid.pool.DruidDataSource;
import com.lorne.tx.compensate.repository.CompensateDataSource;
import com.lorne.tx.db.LCNDataSourceProxy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@MapperScan(basePackages = "com.sfy.quartz.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Autowired
//    private Environment env;
//
//    @Bean
//    public CompensateDataSource compensateDataSource() {
//
//        DruidDataSource dataSource = new DruidDataSource();
//        dataSource.setUrl(env.getProperty("spring.datasource.url"));
//        dataSource.setUsername(env.getProperty("spring.datasource.username"));//用户名
//        dataSource.setPassword(env.getProperty("spring.datasource.password"));//密码
//        dataSource.setInitialSize(1);
//        dataSource.setMaxActive(5);
//        dataSource.setMinIdle(0);
//        dataSource.setMaxWait(60000);
//        dataSource.setValidationQuery("SELECT 1");
//        dataSource.setTestOnBorrow(false);
//        dataSource.setTestWhileIdle(true);
//        dataSource.setPoolPreparedStatements(false);
//
//        CompensateDataSource compensateDataSource = new CompensateDataSource();
//        compensateDataSource.setDataSource(dataSource);
//        return compensateDataSource;
//    }
//
//    @Bean
//    public DataSource dataSource() {
//        DruidDataSource dataSource = new DruidDataSource();
//        dataSource.setUrl(env.getProperty("spring.datasource.url"));
//        dataSource.setUsername(env.getProperty("spring.datasource.username"));//用户名
//        dataSource.setPassword(env.getProperty("spring.datasource.password"));//密码
//        dataSource.setInitialSize(2);
//        dataSource.setMaxActive(20);
//        dataSource.setMinIdle(0);
//        dataSource.setMaxWait(60000);
//        dataSource.setValidationQuery("SELECT 1");
//        dataSource.setTestOnBorrow(false);
//        dataSource.setTestWhileIdle(true);
//        dataSource.setPoolPreparedStatements(false);
//
//        LCNDataSourceProxy dataSourceProxy = new LCNDataSourceProxy();
//        dataSourceProxy.setDataSource(dataSource);
//        dataSourceProxy.setMaxCount(10);
//        return dataSourceProxy;
//    }
}
