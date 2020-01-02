package ca.brocku.cosc.geneticprogramming.controller;

import ca.brocku.cosc.geneticprogramming.services.regression.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }


    @GetMapping("/trainingdata")
    @ResponseBody
    public ResponseEntity<Resource> serveFile() {

        Resource file = fileService.loadTrainingdataAsResource();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/exampledata")
    @ResponseBody
    public ResponseEntity<Resource> getExampledata() {

        Resource file = fileService.loadExampledataAsResource();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/results/{paramsid}/{run}")
    @ResponseBody
    public ResponseEntity<Resource> getTree(@PathVariable long paramsid, @PathVariable int run) {

        Resource file = fileService.getTreeFile(paramsid,run);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }



    @PostMapping("/trainingdata")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String message;
        try {
            fileService.store(file);
            message = "Successfully uploaded!";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Failed to upload!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }



}
