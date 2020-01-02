package ca.brocku.cosc.geneticprogramming.services.regression;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {
    public Resource loadTrainingdataAsResource() {

        return new FileSystemResource("trainingdata.txt");
    }
    public Resource loadExampledataAsResource() {
        return new ClassPathResource("regression/exampledata.txt");
    }

    public void store(MultipartFile file) {
        try {
            file.transferTo(new File("trainingdata.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public Resource getTreeFile(long paramsId, int run){
        return new FileSystemResource("results/paramId" + paramsId + ".run" + run + ".stat");
    }
}
