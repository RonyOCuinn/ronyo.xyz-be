package org.ronyo.ronyo.xyz_be.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class Aws {

    @Bean
    S3Client s3Client() {
        return S3Client.builder().region(Region.EU_WEST_1).build();
    }

}
