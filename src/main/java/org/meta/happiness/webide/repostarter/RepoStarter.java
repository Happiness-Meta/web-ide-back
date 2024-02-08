package org.meta.happiness.webide.repostarter;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RepoStarter {

    @Value("${app.efs-root}")
    private String ROOT;

    public void startRepository(){
        String path = ROOT + "/" + UUID.randomUUID().toString().substring(0,6);
        createDefaultFiles(path, "Python");
    }

    //
    private void createDefaultFiles(String path, String programmingLanguage) {

        String fileName = "main.py";
        String content = "print(\"Hello, World!\")";
        saveFile(path + "/" + fileName, content);
        saveFile(path + "/readme.md", ".");
    }


    public void saveFile(String filePath, String content)  {
        File file = new File(filePath);
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(content);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
