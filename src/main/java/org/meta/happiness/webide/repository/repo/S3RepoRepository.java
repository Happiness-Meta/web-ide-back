package org.meta.happiness.webide.repository.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class S3RepoRepository {

    @Value("${aws.s3.bucket}")
    private String bucketName;
    private final S3Client s3Client;

    public Optional<String> uploadRepo(String repoName) {
        String accessUrl = null;
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(repoName)
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromString(repoName)
            );

            GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(repoName)
                    .build();

            accessUrl = s3Client.utilities().getUrl(getUrlRequest).toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.of(accessUrl);
    }
}
