package com.daishuai.hadoop.reducer;

import com.daishuai.hadoop.entity.OrderEntity;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author Daishuai
 * @date 2020/5/27 16:23
 */
public class OrderReducer extends Reducer<OrderEntity, NullWritable, OrderEntity, NullWritable> {

    @Override
    protected void reduce(OrderEntity key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        context.write(key, NullWritable.get());
    }
}
