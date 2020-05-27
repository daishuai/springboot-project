package com.daishuai.hadoop.mapper;

import com.daishuai.hadoop.entity.OrderEntity;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author Daishuai
 * @date 2020/5/27 16:18
 */
public class OrderMapper extends Mapper<LongWritable, Text, OrderEntity, NullWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String order = value.toString();
        String[] fields = order.split("\t");
        long orderId = Long.parseLong(fields[0]);
        double price = Double.parseDouble(fields[2]);
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(orderId);
        orderEntity.setPrice(price);
        context.write(orderEntity, NullWritable.get());
    }
}
