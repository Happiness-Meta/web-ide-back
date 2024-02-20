package org.meta.happiness.webide.repository.repo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.dto.S3ObjectAndContent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class S3RepoRepository {

    @Value("${aws.s3.bucket}")
    private String bucketName;
    private final S3Client s3Client;

    public static final String DELIMITER = "/";

    public static final String EMPTY = "";

    public Optional<String> uploadRepo(String repoName) {
        String accessUrl = null;
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(repoName)
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromString(EMPTY)
            );

            GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(repoName)
                    .build();

            accessUrl = s3Client.utilities().getUrl(getUrlRequest).toString();

        } catch (Exception e) {
            log.error("error message={}", e.getMessage());
        }

        return Optional.of(accessUrl);
    }

    public void deleteRepoWithRepoPath(String repositoryPath) {
        try {
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(repositoryPath)
                    .build();

            s3Client.listObjectsV2(request).contents().stream()
                    .map((content) -> DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(content.key()).build()).forEach(s3Client::deleteObject);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public String getFileContent(String repoId, String id) {
        String key = "repo" + DELIMITER + repoId + DELIMITER + id + ".txt";
        String content = "";
        try {
            log.info(key);
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            content = s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes()).asUtf8String();
            log.info(content);
        } catch (Exception e) {
            log.error("error message={}", e.getMessage());
        }
        return content;
    }

    public List<S3ObjectAndContent> getTemplateFiles() {
        String repositoryPath = "todo" + DELIMITER;
        List<S3ObjectAndContent> result = new ArrayList<>();

        try {
            // 투두리스트 템플릿 위치에서 가져오기
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(repositoryPath)
                    .build();

            ListObjectsV2Response response = s3Client.listObjectsV2(request);
            List<S3Object> objects = new ArrayList<>(response.contents());

            for (S3Object o : objects) {

                // 파일 안의 내용 확인
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(o.key())
                        .build();

                String content = s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes()).asUtf8String();
                S3ObjectAndContent s3ObjectAndContent = S3ObjectAndContent.builder()
                        .s3Object(o)
                        .content(content)
                        .build();
                log.info("s3 >> {}", s3ObjectAndContent.getS3Object().toString());
                log.info("content >> {}", s3ObjectAndContent.getContent());

                result.add(s3ObjectAndContent);
            }

        } catch (Exception e) {
            log.error("Error while listing objects: {}", e.getMessage());
        }

        return result;
    }
}
