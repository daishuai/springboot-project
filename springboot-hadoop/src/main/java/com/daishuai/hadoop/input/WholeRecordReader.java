package com.daishuai.hadoop.input;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

/**
 * @author Daishuai
 * @date 2020/5/28 11:23
 */
public class WholeRecordReader extends RecordReader<NullWritable, BytesWritable> {
    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {

    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        return false;
    }

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return null;
    }

    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        return null;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
}
