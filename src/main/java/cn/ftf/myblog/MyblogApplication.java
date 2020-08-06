package cn.ftf.myblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MyblogApplication /*extends SpringBootServletInitializer*/ {
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)    {
//        return application.sources(MyblogApplication.class);
//    }

    //配置RestTemplate交给spring管理
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
    public static void main(String[] args) {
        SpringApplication.run(MyblogApplication.class, args);
    }
}
