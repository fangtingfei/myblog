package cn.ftf.myblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyblogApplication {
    //    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)    {
    //        return application.sources(MyblogApplication.class);
    //    }
    public static void main(String[] args) {
        SpringApplication.run(MyblogApplication.class, args);
    }

}
