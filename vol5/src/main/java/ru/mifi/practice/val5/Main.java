package ru.mifi.practice.val5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public abstract class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod", "PMD.CloseResource"})
    @EventListener(ApplicationReadyEvent.class)
    private void postConstruct(ApplicationReadyEvent event) {
        ConfigurableApplicationContext context = event.getApplicationContext();
        String port = context.getEnvironment().getProperty("server.port");
        log.info("http://127.0.0.1:{}/swagger-ui/index.html", port);
    }
}
