package com.daishuai.hadoop;

import com.daishuai.hadoop.comparator.OrderGroupingComparator;
import com.daishuai.hadoop.entity.OrderEntity;
import com.daishuai.hadoop.mapper.OrderMapper;
import com.daishuai.hadoop.partitioner.OrderPartitioner;
import com.daishuai.hadoop.reducer.OrderReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @author Daishuai
 * @date 2020/5/27 16:24
 */
public class OrderDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //获取配置信息
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        //设置jar包加载路径
        job.setJarByClass(OrderDriver.class);

        //加载Mapper/Reducer类
        job.setMapperClass(OrderMapper.class);
        job.setReducerClass(OrderReducer.class);

        //设置Mapper输出数据的key和value类型
        job.setMapOutputKeyClass(OrderEntity.class);
        job.setMapOutputValueClass(NullWritable.class);

        //设置最终输出数据的key和value类型
        job.setOutputKeyClass(OrderEntity.class);
        job.setOutputValueClass(NullWritable.class);

        //设置分区
        job.setPartitionerClass(OrderPartitioner.class);

        //设置reduce个数
        job.setNumReduceTasks(3);

        //设置reduce端的分组
        job.setGroupingComparatorClass(OrderGroupingComparator.class);

        //设置输入数据和输出数据的路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        boolean result = job.waitForCompletion(true);
        System.exit(result ? 1 : 0);

    }
}
