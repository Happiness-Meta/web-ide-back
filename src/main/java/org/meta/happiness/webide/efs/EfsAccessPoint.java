package org.meta.happiness.webide.efs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.efs.EfsClient;
import software.amazon.awssdk.services.efs.model.CreateAccessPointResponse;

@Component
@RequiredArgsConstructor
public class EfsAccessPoint{

    @Value("${aws.efs.file-system-id}")
    private String fileSystemId;

    private final EfsClient efsClient;

    public String generateAccessPoint(String projectId) {
        CreateAccessPointResponse response = efsClient.createAccessPoint(req -> req
                .fileSystemId(fileSystemId)
                .rootDirectory(req2 -> req2.path("/efs/" + projectId))
                .build()
        );

        return response.accessPointId();
    }

    public void deleteAccessPoint(String accessPointId) {
        efsClient.deleteAccessPoint(req -> req.accessPointId(accessPointId));
    }
}
