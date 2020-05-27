package com.daishuai.hadoop.comparator;

import com.daishuai.hadoop.entity.OrderEntity;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * @author Daishuai
 * @date 2020/5/27 16:38
 */
public class OrderGroupingComparator extends WritableComparator {

    public OrderGroupingComparator() {
        super(OrderEntity.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        OrderEntity aOrder = (OrderEntity) a;
        OrderEntity bOrder = (OrderEntity) b;
        return (int) (aOrder.getOrderId() - bOrder.getOrderId());
    }
}
