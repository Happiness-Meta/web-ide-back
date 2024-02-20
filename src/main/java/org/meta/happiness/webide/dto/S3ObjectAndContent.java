package org.meta.happiness.webide.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.s3.model.S3Object;

@Data
@NoArgsConstructor
public class S3ObjectAndContent {
    private S3Object s3Object;
    private String content;

    @Builder
    public S3ObjectAndContent(S3Object s3Object, String content) {
        this.s3Object = s3Object;
        this.content = content;
    }
}
