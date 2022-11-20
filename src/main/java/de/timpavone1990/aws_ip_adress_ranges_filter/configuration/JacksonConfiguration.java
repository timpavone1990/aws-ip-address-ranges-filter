package de.timpavone1990.aws_ip_adress_ranges_filter.configuration;

import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class JacksonConfiguration {

    @Bean
    public AfterburnerModule afterburnerModule() {
        return new AfterburnerModule();
    }
}
