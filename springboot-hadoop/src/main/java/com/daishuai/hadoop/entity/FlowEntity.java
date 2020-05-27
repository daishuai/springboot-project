package com.daishuai.hadoop.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author Daishuai
 * @date 2020/5/27 9:47
 */
@Data
@NoArgsConstructor
@ToString
public class FlowEntity implements Writable {

    /**
     * 上行流量
     */
    private Long upFlow;

    /**
     * 下行流量
     */
    private Long downFlow;

    /**
     * 总流量
     */
    private Long sumFlow;


    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(upFlow);
        dataOutput.writeLong(downFlow);
        dataOutput.writeLong(sumFlow);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.upFlow = dataInput.readLong();
        this.downFlow = dataInput.readLong();
        this.sumFlow = dataInput.readLong();
    }
}
