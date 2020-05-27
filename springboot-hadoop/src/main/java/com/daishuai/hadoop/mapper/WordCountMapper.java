package com.daishuai.hadoop.mapper;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author Daishuai
 * @date 2020/5/26 10:51
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private Text outKey = new Text();

    private IntWritable outValue = new IntWritable(1);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String lineStr = value.toString();
        String[] words = lineStr.split(" ");
        for (String word : words) {
            outKey.set(word);
            context.write(outKey, outValue);
        }
    }
}
