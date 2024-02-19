package org.meta.happiness.webide.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meta.happiness.webide.dto.file.FileDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.meta.happiness.webide.dto.response.RepoTreeResponseSerializer.EXTENSION_SEPARATOR;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonSerialize(using = RepoTreeResponseSerializer.class)
public class RepoTreeResponse {
    public static final String DELIMITER = "/";
    private int id;
    private String key;
    private String name;
    private List<RepoTreeResponse> children;

    private String uuid;


    private String content;

    public RepoTreeResponse(int id, String key, String name) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.children = new ArrayList<>();
    }

    @Builder
    public RepoTreeResponse(String key, String name) {
        this.key = key;
        this.name = name;
        this.children = new ArrayList<>();
    }

    @Builder
    public RepoTreeResponse(int id, String key, String name, String content, String uuid) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.content = content;
        this.uuid = uuid;
        this.children = new ArrayList<>();
    }

    public RepoTreeResponse findOrCreateChild(int id, String fullKey, String name, String content, String uuid) {
        for (RepoTreeResponse child : children) {
            if (child.name.equals(name)) {
                return child;
            }
        }
        RepoTreeResponse newNode = new RepoTreeResponse(id, fullKey, name, content, uuid);
        children.add(newNode);
        return newNode;
    }

    public static RepoTreeResponse buildTreeFromKeys(List<FileDto> fileDtos) {
        int c = 0;
        RepoTreeResponse root = new RepoTreeResponse(++c,"", "root");

        for (FileDto file : fileDtos) {

            String fullKey = file.getFilePath();
            String[] parts = fullKey.split(DELIMITER);

            System.out.println(">>>>>>>>> " + Arrays.toString(parts));

            RepoTreeResponse currentNode = root;

            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                if (part.isEmpty()) continue;

                String accumulatedKey = DELIMITER + String.join(DELIMITER, java.util.Arrays.copyOfRange(parts, 0, i + 1));
                if (!accumulatedKey.contains(EXTENSION_SEPARATOR))
                    accumulatedKey += DELIMITER;

                c++;
                currentNode = currentNode.findOrCreateChild(c, accumulatedKey, part, file.getContent(), file.getUuid());
            }
        }

//        return root.children.get(0);
        return root;
    }
}
