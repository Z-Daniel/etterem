package hu.etterem;

import com.vaadin.annotations.Viewport;
import com.vaadin.annotations.Widgetset;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EnableAutoConfiguration
@EnableJpaRepositories("hu.etterem")
@ComponentScan({"hu.etterem"})
@Widgetset("com.vaadin.DefaultWidgetSet")
@Viewport("user-scalable=no,initial-scale=1.0")
@EnableTransactionManagement
@SpringBootApplication
public class EtteremApplication {
    public static void main(String[] args) {
        SpringApplication.run(EtteremApplication.class, args);
    }

}
