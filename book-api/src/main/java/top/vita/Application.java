package top.vita;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author vita
 * @Date 2023/5/23 23:36
 */
@SpringBootApplication
@MapperScan(basePackages = "top.vita.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
