package com.daishuai.hadoop.partitioner;

import com.daishuai.hadoop.entity.OrderEntity;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * @author Daishuai
 * @date 2020/5/27 16:33
 */
public class OrderPartitioner extends Partitioner<OrderEntity, NullWritable> {
    @Override
    public int getPartition(OrderEntity orderEntity, NullWritable nullWritable, int numReduceTasks) {
        return (int) ((orderEntity.getOrderId() & Integer.MAX_VALUE) % numReduceTasks);
    }
}
