package com.daishuai.hadoop.controller;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Daishuai
 * @date 2020/5/11 21:40
 */
@RestController
@RequestMapping(value = "/hdfs")
public class HdfsController {

    @Autowired
    private FileSystem fileSystem;

    @GetMapping(value = "/upload")
    public ResponseEntity uploadFile() throws IOException {
        fileSystem.copyFromLocalFile(new Path("d:/a.txt"), new Path("/a2.txt"));
        return ResponseEntity.ok("success");
    }
}
