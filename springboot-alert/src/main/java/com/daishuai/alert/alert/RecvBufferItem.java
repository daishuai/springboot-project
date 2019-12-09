package com.daishuai.alert.alert;

/**
 * @author Daishuai
 * @description
 * @date 2019/9/5 13:43
 */
public class RecvBufferItem {
    private int size = 0;
    private byte[] buf;
    
    public RecvBufferItem(int size, byte[] buf) {
        this.size = size;
        this.buf = buf;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public byte[] getBuf() {
        return this.buf;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public void setBuf(byte[] buf) {
        this.buf = buf;
    }
}
