package com.didispace.scca.service.discovery.consul.test;

import com.didispace.scca.core.config.SccaBaseConfiguration;
import com.didispace.scca.service.discovery.consul.SccaConsulConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Created by 程序猿DD/翟永超 on 2018/4/24.
 * <p>
 * Blog: http://blog.didispace.com/
 * Github: https://github.com/dyc87112/
 */
@SpringBootApplication
@Import({SccaConsulConfiguration.class, SccaBaseConfiguration.class})
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class);
    }

}
