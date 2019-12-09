package com.daishuai.alert.alert;

/**
 * @author Daishuai
 * @description
 * @date 2019/9/5 13:41
 */
public class MakeComplete {
    private int recvHead = 0;
    private byte[] recvBuf = new byte[50];
    private int bufIndex = 0;
    private int recvOver = 0;
    
    public MakeComplete() {
    }
    
    public int getRecvHead() {
        return this.recvHead;
    }
    
    public void setRecvHead(int recvHead) {
        this.recvHead = recvHead;
    }
    
    public byte[] getRecvBuf() {
        return this.recvBuf;
    }
    
    public void setRecvBuf(byte[] recvBuf) {
        this.recvBuf = recvBuf;
    }
    
    public int getBufIndex() {
        return this.bufIndex;
    }
    
    public void setBufIndex(int bufIndex) {
        this.bufIndex = bufIndex;
    }
    
    public int getRecvOver() {
        return this.recvOver;
    }
    
    public void setRecvOver(int recvOver) {
        this.recvOver = recvOver;
    }
    
    public void clearRecvBuf() {
        for(int i = 0; i < this.recvBuf.length; ++i) {
            this.recvBuf[i] = 0;
        }
        
    }
    
    public void init() {
        this.recvHead = 0;
        this.bufIndex = 0;
        this.recvOver = 0;
    }
    
    public boolean setRecvBufByte(int index, byte b) {
        if (index >= 0 && index <= this.recvBuf.length - 1) {
            this.recvBuf[index] = b;
            return true;
        } else {
            return false;
        }
    }
    
    public int addBufIndex() {
        ++this.bufIndex;
        return this.bufIndex;
    }
    
    public void makeCompleteRecvData(byte rb) {
        if (rb == 16 && this.getRecvHead() == 0) {
            this.setRecvHead(1);
            this.setBufIndex(0);
            this.setRecvBufByte(this.getBufIndex(), rb);
            this.addBufIndex();
        } else if (rb == 2 && this.getRecvHead() == 1) {
            this.setRecvHead(2);
            this.setRecvBufByte(this.getBufIndex(), rb);
            this.addBufIndex();
        } else if (this.getRecvHead() != 2) {
            this.setRecvHead(0);
            this.setBufIndex(0);
        } else if (this.getRecvHead() == 2) {
            if (rb == 16 && this.getRecvOver() == 0) {
                this.setRecvBufByte(this.getBufIndex(), rb);
                this.addBufIndex();
                this.setRecvOver(1);
            } else if (rb == 3 && this.getRecvOver() == 1) {
                this.setRecvBufByte(this.getBufIndex(), rb);
                this.setRecvOver(2);
                this.setRecvHead(0);
            } else if (this.getRecvOver() == 1) {
                if (rb != 16) {
                    this.setRecvBufByte(this.getBufIndex(), rb);
                    this.addBufIndex();
                    this.setRecvOver(0);
                } else {
                    this.setRecvOver(0);
                }
            } else if (this.setRecvBufByte(this.getBufIndex(), rb)) {
                this.addBufIndex();
            } else {
                this.setRecvOver(2);
                this.setRecvHead(0);
            }
        }
        
    }
    
    public void makeCompleteRecvData_tag(byte rb) {
        if (rb == 16 && this.getRecvHead() == 0) {
            this.setRecvHead(1);
            this.setBufIndex(0);
            this.setRecvBufByte(this.getBufIndex(), rb);
            this.addBufIndex();
        } else if (rb == 2 && this.getRecvHead() == 1) {
            this.setRecvHead(2);
            this.setRecvBufByte(this.getBufIndex(), rb);
            this.addBufIndex();
        } else if (this.getRecvHead() != 2) {
            this.setRecvHead(0);
            this.setBufIndex(0);
        } else if (this.getRecvHead() == 2) {
            if (rb == 16 && this.getRecvOver() == 0) {
                this.setRecvBufByte(this.getBufIndex(), rb);
                this.addBufIndex();
                this.setRecvOver(1);
            } else if (rb == 3 && this.getRecvOver() == 1) {
                this.setRecvBufByte(this.getBufIndex(), rb);
                this.setRecvOver(2);
                this.setRecvHead(0);
            } else if (this.getRecvOver() == 1) {
                if (rb != 16) {
                    this.setRecvBufByte(this.getBufIndex(), rb);
                    this.addBufIndex();
                    this.setRecvOver(0);
                } else {
                    this.setRecvBufByte(this.getBufIndex(), rb);
                    this.addBufIndex();
                    this.setRecvOver(1);
                }
            } else if (this.setRecvBufByte(this.getBufIndex(), rb)) {
                this.addBufIndex();
            } else {
                this.setRecvOver(2);
                this.setRecvHead(0);
            }
        }
        
    }
}
