package de.timpavone1990.aws_ip_adress_ranges_filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AwsIpAddressRangesFilterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwsIpAddressRangesFilterApplication.class, args);
    }
}
