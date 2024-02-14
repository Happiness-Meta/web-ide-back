package org.meta.happiness.webide.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

@Configuration
public class AwsConfig {
    static class CustomAwsCredentialsProvider implements AwsCredentialsProvider {
        private final String accessKey;
        private final String secretKey;

        public CustomAwsCredentialsProvider(
                String accessKey,
                String secretKey
        ) {
            this.accessKey = accessKey;
            this.secretKey = secretKey;
        }

        @Override
        public AwsCredentials resolveCredentials() {
            return AwsBasicCredentials.create(accessKey, secretKey);
        }
    }


    @Bean
    public AwsCredentialsProvider awsCredentialsProvider(
            @Value("${aws.accessKey}") String accessKey,
            @Value("${aws.secretKey}") String secretKey
    ) {
        return new CustomAwsCredentialsProvider(accessKey, secretKey);
    }
}