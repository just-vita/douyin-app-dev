package top.vita;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author vita
 * @Date 2023/5/23 23:36
 */
@SpringBootApplication
@MapperScan(basePackages = "top.vita.mapper")
@ComponentScan(basePackages = {"top.vita", "org.n3r.idworker"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
