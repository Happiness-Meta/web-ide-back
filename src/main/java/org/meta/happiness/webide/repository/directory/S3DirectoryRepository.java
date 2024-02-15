package org.meta.happiness.webide.repository.directory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class S3DirectoryRepository {
    public static final String EMPTY = "";

    @Value("${aws.s3.bucket}")
    private String bucketName;
    private final S3Client s3Client;

    public boolean isDirectoryExist(String s3Path) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Path)
                    .build();

            s3Client.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    public void putDirectoryPath(String s3Path) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Path)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromString(EMPTY));

        } catch (Exception e) {
            log.error("error >>> {}", e.getMessage());
        }
    }

    public void deleteDirectory(S3Object s3Object) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Object.key())
                    .build();
            s3Client.deleteObject(deleteRequest);
        } catch (Exception e) {
            log.error("error message={}",e.getMessage());
        }
    }

    public void deleteDirectoryPath(String s3Path) {
        try {
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(s3Path)
                    .build();

            s3Client.listObjectsV2(request).contents().stream()
                    .map((content) -> DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(content.key()).build()).forEach(s3Client::deleteObject);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
