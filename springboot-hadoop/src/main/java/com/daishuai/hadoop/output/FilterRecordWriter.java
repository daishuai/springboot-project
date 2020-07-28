package com.daishuai.hadoop.output;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

/**
 * @author Daishuai
 * @date 2020/5/28 11:09
 */
@Slf4j
public class FilterRecordWriter extends RecordWriter<Text, NullWritable> {

    private Configuration configuration;

    private FSDataOutputStream outputStream1;

    private FSDataOutputStream outputStream2;

    public FilterRecordWriter(TaskAttemptContext context) {
        this.configuration = context.getConfiguration();
        try {
            FileSystem fileSystem = FileSystem.get(configuration);
            outputStream1 = fileSystem.create(new Path("d:/tmp/out1/log.txt"));
            outputStream2 = fileSystem.create(new Path("d:/tmp/out2/log.txt"));
        } catch (IOException e) {
            log.error("获取文件系统出错:{}", e.getMessage(), e);
        }
    }

    @Override
    public void write(Text text, NullWritable nullWritable) throws IOException, InterruptedException {
        String key = text.toString();
        if (StringUtils.contains(key, "hello")) {
            outputStream1.write(key.getBytes());
        } else {
            outputStream2.write(key.getBytes());
        }
    }

    @Override
    public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        IOUtils.closeQuietly(outputStream1);
        IOUtils.closeQuietly(outputStream2);
    }
}
