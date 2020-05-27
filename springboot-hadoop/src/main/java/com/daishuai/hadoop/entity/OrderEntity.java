package com.daishuai.hadoop.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author Daishuai
 * @date 2020/5/27 16:12
 */
@Data
@NoArgsConstructor
@ToString
public class OrderEntity implements WritableComparable<OrderEntity> {

    private Long orderId;

    private Double price;


    @Override
    public int compareTo(OrderEntity orderEntity) {
        int result;
        if (this.orderId > orderEntity.getOrderId()) {
            result = 1;
        } else if (this.orderId < orderEntity.getOrderId()) {
            result = -1;
        } else {
            result = this.price > orderEntity.getPrice() ? -1 : 1;
        }
        return result;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(orderId);
        dataOutput.writeDouble(price);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.orderId = dataInput.readLong();
        this.price = dataInput.readDouble();
    }
}
