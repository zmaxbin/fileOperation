package com.maxbin.fileoperation.service;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class HdfsService {

    private FileSystem fs = null;

    public FileSystem init() throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.defaultFS","hdfs://172.16.56.100:9000/");

        fs = FileSystem.get(new URI("hdfs://172.16.56.100:9000/"), conf, "hadoop");
        return fs;
    }
}
