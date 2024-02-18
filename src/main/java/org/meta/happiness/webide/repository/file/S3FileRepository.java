package org.meta.happiness.webide.repository.file;


import jakarta.activation.MimetypesFileTypeMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URLConnection;

@Repository
@RequiredArgsConstructor
@Slf4j
public class S3FileRepository {

    public static final String EMPTY = "";

    @Value("${aws.s3.bucket}")
    private String bucketName;
    private final S3Client s3Client;

    public boolean isFileExist(String S3Key) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(S3Key)
                    .build();

            s3Client.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    public void deleteFile(String s3Path) {
        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Path)
                    .build();

            s3Client.deleteObject(request);
        } catch (Exception e) {
            log.error("error message={}", e.getMessage());
        }
    }

    public void putFilePath(String s3Path) {
        try {
            // 파일의 확장자가 무엇인가
            MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
            String mimeType = mimetypesFileTypeMap.getContentType(s3Path);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Path)
                    .contentType(mimeType)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromString(EMPTY));

        } catch (Exception e) {
            log.error("error message={}", e.getMessage());
        }
    }

    public void putFile(String s3Path, String content) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Path)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromString(content));
        } catch (Exception e) {
            log.error("error message={}", e.getMessage());
        }
    }
}
