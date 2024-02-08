package org.meta.happiness.webide.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.efs.EfsClient;

@Configuration
@RequiredArgsConstructor
public class AwsClientConfig {

    private final AwsCredentialsProvider awsCredentialsProvider;

    @Bean
    public EfsClient efsClient(){
        return EfsClient.builder()
                .credentialsProvider(awsCredentialsProvider)
                .region(Region.AP_NORTHEAST_2)
                .build();
    }
}
