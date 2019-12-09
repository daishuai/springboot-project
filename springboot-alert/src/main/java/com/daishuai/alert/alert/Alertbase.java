package com.daishuai.alert.alert;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Daishuai
 * @description
 * @date 2019/9/5 13:40
 */
public class Alertbase {
    private FileDescriptor mFd = null;
    private FileInputStream mFileInputStream = null;
    private FileOutputStream mFileOutputStream = null;
    private SerialPort port = null;
    public int alertBaseDevID = 0;
    public String abonlineid = "";
    private Long cycle = 1L;
    private boolean alarmAlertBasePower = false;
    public int alarmAlertBasePowerShowCount = 0;
    private int readbufSize = 0;
    private String alertBaseVersion = "000";
    public int read_count = 0;
    private ConcurrentLinkedQueue<RecvBufferItem> ReadBufferQueue = null;
    private ConcurrentLinkedQueue<RecvBufferItem> WriteBufferQueue = null;
    private Alertbase.LinuxLocalReadCommThread linuxLocalReadCommThread = null;
    private Alertbase.GetwayClientSocketThread getwayClientSocketThread = null;
    private boolean getwayConnected = false;
    private String dev = "";
    private int baudrate = 0;
    
    static {
        if (GV.OSName != GV.EPlatform.Windows) {
            //System.loadLibrary("alertbase");
        }
        
    }
    
    private native FileDescriptor open(String var1, int var2, int var3);
    
    private native void close();
    
    public Alertbase() {
    }
    
    public void setConnectDev(String connectDev) {
        this.dev = connectDev;
    }
    
    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }
    
    private boolean execSudo(String command) {
        boolean rs = false;
        Process sudo = null;
        
        try {
            String[] cmd = new String[]{GV.ShellType, "-c", "echo " + GV.RootPwd + " | sudo -S " + command};
            sudo = Runtime.getRuntime().exec(cmd);
            rs = true;
        } catch (IOException var13) {
            System.out.println(var13);
            rs = false;
        } finally {
            if (sudo != null) {
                try {
                    sudo.waitFor();
                } catch (InterruptedException var12) {
                    System.out.println(var12);
                    rs = false;
                }
            }
            
            if (sudo != null) {
                sudo.destroy();
            }
            
        }
        
        return rs;
    }
    
    public boolean LoadUsbDriver_base() {
        if (OSInfo.getOSname() == GV.EPlatform.Windows) {
            return true;
        } else {
            boolean rs = false;
            String[] args = GV.LoadUsbDriver_Command_Base.split(";");
            
            for(int i = 0; i < args.length; ++i) {
                rs = this.execSudo(args[i]);
            }
            
            if (rs) {
                rs = this.execSudo("chmod 777 " + GV.alertBaseDev);
            }
            
            return rs;
        }
    }
    
    private void Reload_Reopen_Usb() throws Exception {
        boolean b = false;
        String dev = GV.alertBaseDev;
        int port = GV.alertBaseDev_Port;
        b = this.LoadUsbDriver_base();
        if (b) {
            System.out.println("---reload usb driver ok! ---");
            
            try {
                File device = new File(dev);
                if (device.exists()) {
                    this.mFd = this.open(device.getAbsolutePath(), port, 0);
                    if (this.mFd == null) {
                        System.out.println("native reopen returns null");
                        throw new IOException("native reopen returns null");
                    } else {
                        this.mFileInputStream = new FileInputStream(this.mFd);
                        this.mFileOutputStream = new FileOutputStream(this.mFd);
                    }
                } else {
                    throw new IOException(dev + " is not exists! ");
                }
            } catch (Exception var5) {
                System.out.println("---reopen serial-port error: " + var5.getMessage());
                throw new Exception(var5.getMessage());
            }
        } else {
            System.out.println("---reload usb driver failed !");
            throw new Exception("reload usb driver failed !");
        }
    }
    
    public void ClearReadQueue() {
        if (this.ReadBufferQueue != null) {
            this.ReadBufferQueue.clear();
        }
        
    }
    
    public boolean getGetwayConnected() {
        return this.getwayConnected;
    }
    
    public String getAlertBaseVersion() {
        return this.alertBaseVersion;
    }
    
    public void setAlertBaseVersion(String alertBaseVersion) {
        this.alertBaseVersion = alertBaseVersion;
    }
    
    public boolean isAlarmAlertBasePower() {
        return this.alarmAlertBasePower;
    }
    
    public void setAlarmAlertBasePower(boolean alarmAlertBasePower) {
        this.alarmAlertBasePower = alarmAlertBasePower;
    }
    
    public void OpenPort() throws Exception {
        if (GV.Comm_Type != 1) {
            if (GV.Comm_Type == 2) {
                this.WriteBufferQueue = new ConcurrentLinkedQueue();
                this.ReadBufferQueue = new ConcurrentLinkedQueue();
                this.getwayClientSocketThread = new Alertbase.GetwayClientSocketThread();
                if (!this.getwayClientSocketThread.connectGetway()) {
                    throw new Exception("connect getway failed!");
                }
                
                this.getwayClientSocketThread.start();
            }
        } else if (GV.OSName == GV.EPlatform.Windows) {
            this.port = new SerialPort(this.dev);
            
            try {
                if (!this.port.openPort()) {
                    System.out.println("===open serialport failed in windows!");
                    throw new Exception("aaa: open serialport failed!");
                }
                
                this.port.setParams(this.baudrate, 8, 1, 0);
                this.ReadBufferQueue = new ConcurrentLinkedQueue();
                this.port.addEventListener(new Alertbase.SerialPortEventReader());
            } catch (SerialPortException var4) {
                this.port.closePort();
                this.port = null;
                System.out.println("xxxx:" + var4.getMessage());
                throw new Exception(var4.getMessage());
            } catch (Exception var5) {
                this.port.closePort();
                this.port = null;
                System.out.println("yyyy:" + var5.getMessage());
                throw new Exception(var5.getMessage());
            }
        } else {
            File device = new File(this.dev);
            if (!device.exists()) {
                System.out.println(this.dev + " is not exists!");
                throw new Exception("device is not exists");
            }
            
            if (!device.canRead() || !device.canWrite()) {
                try {
                    Process su = Runtime.getRuntime().exec("/usr/bin/sudo");
                    String cmd = "chmod 777 " + device.getAbsolutePath() + "\n" + "exit\n";
                    su.getOutputStream().write(cmd.getBytes());
                    if (su.waitFor() != 0 || !device.canRead() || !device.canWrite()) {
                        throw new SecurityException();
                    }
                } catch (Exception var6) {
                    var6.printStackTrace();
                    throw new SecurityException();
                }
            }
            
            System.out.println("---path: " + device.getAbsolutePath());
            this.mFd = this.open(device.getAbsolutePath(), this.baudrate, 0);
            if (this.mFd == null) {
                System.out.println("native open returns null");
                throw new IOException();
            }
            
            this.mFileInputStream = new FileInputStream(this.mFd);
            this.mFileOutputStream = new FileOutputStream(this.mFd);
            this.ReadBufferQueue = new ConcurrentLinkedQueue();
            this.linuxLocalReadCommThread = new Alertbase.LinuxLocalReadCommThread();
            this.linuxLocalReadCommThread.start();
        }
        
    }
    
    public boolean checkOpenDevice() {
        boolean rs = false;
        if (GV.Comm_Type == 1) {
            if (GV.OSName == GV.EPlatform.Windows) {
                this.port = new SerialPort(this.dev);
                
                try {
                    if (this.port.openPort()) {
                        rs = true;
                        this.port.closePort();
                    } else {
                        System.out.println("===checkout serialport failed in windows!");
                    }
                } catch (SerialPortException var7) {
                    this.port = null;
                    System.out.println(var7.getMessage());
                    var7.printStackTrace();
                } catch (Exception var8) {
                    this.port = null;
                    System.out.println(var8.getMessage());
                    var8.printStackTrace();
                }
            } else {
                File device = new File(this.dev);
                if (device.exists()) {
                    this.mFd = this.open(device.getAbsolutePath(), this.baudrate, 0);
                    if (this.mFd != null) {
                        this.close();
                        rs = true;
                    }
                }
            }
        } else if (GV.Comm_Type == 2) {
            try {
                Socket socket = new Socket(this.dev, this.baudrate);
                rs = true;
                
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException var4) {
                }
                
                socket.close();
            } catch (UnknownHostException var5) {
            } catch (IOException var6) {
            }
        }
        
        return rs;
    }
    
    public int Read(byte[] buf) throws IOException {
        int rs = 0;
        RecvBufferItem it;
        int len;
        if (GV.Comm_Type == 1) {
            if (GV.OSName == GV.EPlatform.Windows) {
                if (this.port == null) {
                    return 0;
                }
                
                if (this.ReadBufferQueue != null && !this.ReadBufferQueue.isEmpty()) {
                    it = (RecvBufferItem)this.ReadBufferQueue.poll();
                    len = buf.length;
                    if (it.getSize() < len) {
                        len = it.getSize();
                    }
                    
                    System.arraycopy(it.getBuf(), 0, buf, 0, len);
                    rs = len;
                }
            } else {
                if (this.mFileInputStream == null) {
                    return 0;
                }
                
                if (this.ReadBufferQueue != null && !this.ReadBufferQueue.isEmpty()) {
                    it = (RecvBufferItem)this.ReadBufferQueue.poll();
                    len = buf.length;
                    if (it.getSize() < len) {
                        len = it.getSize();
                    }
                    
                    System.arraycopy(it.getBuf(), 0, buf, 0, len);
                    rs = len;
                }
            }
        } else if (GV.Comm_Type == 2 && this.ReadBufferQueue != null && !this.ReadBufferQueue.isEmpty()) {
            it = (RecvBufferItem)this.ReadBufferQueue.poll();
            len = buf.length;
            if (it.getSize() < len) {
                len = it.getSize();
            }
            
            System.arraycopy(it.getBuf(), 0, buf, 0, len);
            rs = len;
        }
        
        return rs;
    }
    
    public void Write(byte[] buf) throws IOException {
        if (GV.Comm_Type == 1) {
            if (GV.OSName == GV.EPlatform.Windows) {
                if (this.port == null) {
                    return;
                }
                
                try {
                    this.port.writeBytes(buf);
                    //System.out.println("===send data：" + PublicFunc.toHex_db(buf));
                } catch (SerialPortException var6) {
                    System.out.println(var6.getMessage());
                }
            } else {
                if (this.mFileOutputStream == null) {
                    return;
                }
                
                try {
                    this.mFileOutputStream.write(buf);
                    //System.out.println("===send data：" + PublicFunc.toHex_db(buf));
                } catch (IOException var5) {
                    try {
                        this.Reload_Reopen_Usb();
                        this.mFileOutputStream.write(buf);
                        //System.out.println("===send data：" + PublicFunc.toHex_db(buf));
                    } catch (Exception var4) {
                        throw new IOException("reload usb driver faild!");
                    }
                }
            }
        } else if (GV.Comm_Type == 2 && this.getwayClientSocketThread.isConnected() && this.WriteBufferQueue != null) {
            this.WriteBufferQueue.add(new RecvBufferItem(buf.length, buf));
        }
        
    }
    
    public InputStream getInputStream() {
        return this.mFileInputStream;
    }
    
    public OutputStream getOutputStream() {
        return this.mFileOutputStream;
    }
    
    public void ClosePort() {
        if (GV.Comm_Type == 1) {
            if (GV.OSName == GV.EPlatform.Windows) {
                if (this.port != null) {
                    try {
                        this.port.removeEventListener();
                        this.port.closePort();
                        if (this.ReadBufferQueue != null) {
                            this.ReadBufferQueue.clear();
                            this.ReadBufferQueue = null;
                        }
                    } catch (SerialPortException var3) {
                        System.out.println(var3.getMessage());
                    }
                }
            } else {
                try {
                    if (this.linuxLocalReadCommThread != null) {
                        this.linuxLocalReadCommThread.stop = true;
                        this.linuxLocalReadCommThread.interrupt();
                    }
                } catch (Exception var2) {
                    System.out.println(var2.getMessage());
                }
                
                if (this.ReadBufferQueue != null) {
                    this.ReadBufferQueue.clear();
                    this.ReadBufferQueue = null;
                }
                
                if (this.mFd != null) {
                    this.close();
                }
            }
        } else if (GV.Comm_Type == 2) {
            if (this.getwayClientSocketThread != null) {
                this.getwayClientSocketThread.stop = true;
                this.getwayClientSocketThread.interrupt();
                this.getwayClientSocketThread.closeClient();
            }
            
            if (this.ReadBufferQueue != null) {
                this.ReadBufferQueue.clear();
                this.ReadBufferQueue = null;
            }
            
            if (this.WriteBufferQueue != null) {
                this.WriteBufferQueue.clear();
                this.WriteBufferQueue = null;
            }
        }
        
    }
    
    public void SendEvacuation(byte b) throws IOException {
        byte[] send_eva = new byte[]{16, 2, 66, 82, b, 80, 0, -94, 9, -2, 16, 3};
        System.out.println("---send Evacuation: " + PublicFunc.toHex(send_eva));
        this.Write(send_eva);
    }
    
    public void SendResetEvacuation(byte b) throws IOException {
        byte[] send_eva = new byte[]{16, 2, 66, 82, b, 80, 0, -92, 9, -2, 16, 3};
        System.out.println("---send Reset Evacuation: " + PublicFunc.toHex(send_eva));
        this.Write(send_eva);
    }
    
    public void SendOffline(byte b) throws IOException {
        byte[] send_c = new byte[]{16, 2, 66, 82, b, 80, 0, -95, 9, -2, 16, 3};
        this.Write(send_c);
    }
    
    public void SendAlartBaseClose() {
        byte[] send_c = new byte[]{16, 2, 69, 66, 0, 83, 10, -2, 16, 3};
        
        try {
            this.Write(send_c);
        } catch (IOException var3) {
            var3.printStackTrace();
            System.out.println("---send alertbase close error !---");
        }
        
    }
    
    public void SendAGDelete(byte b) {
        byte[] send_c = new byte[]{16, 2, 66, 82, b, 82, -57, 47, 16, 3};
        
        try {
            this.Write(send_c);
        } catch (IOException var4) {
            var4.printStackTrace();
            System.out.println("---send alertbase close error !---");
        }
        
    }
    
    private void writeBaseDataToMember(byte[] recvBuf, MemberBase mb) {
        byte[] send_snum = new byte[]{16, 2, 66, 82, 6, 80, 0, -109, 9, -2, 16, 3};
        byte[] send_name = new byte[]{16, 2, 66, 82, 6, 80, 0, -110, -56, 62, 16, 3};
        byte[] send_team = new byte[]{16, 2, 66, 82, 6, 80, 0, -108, 72, 60, 16, 3};
        byte[] send_pandt = new byte[]{16, 2, 66, 82, 6, 80, 0, -112, 0, 0, 0, 0, 16, 3};
        byte[] var10000 = new byte[]{16, 2, 122, 0, 25, 51, 0, 0, 0, 0, 16, 3};
        String stmp = "";
        /*System.out.println("---recv: ");
        System.out.print("         ");
        
        for(int i = 0; i < recvBuf.length; ++i) {
            if (i == 5 || i == 10 || i == 15 || i == 20 || i == 25 || i == 30 || i == 35) {
                System.out.println("");
                System.out.print("         ");
            }
            
            System.out.print(PublicFunc.toHex(recvBuf[i]) + " ");
        }
        
        System.out.println("");
        System.out.println("---online ID: " + PublicFunc.toHex(recvBuf[4]));*/
        mb.setSignal(true);
        mb.setRecvFlag(recvBuf[7]);
        mb.setDataFlag(recvBuf[5]);
        byte[] barr;
        int i;
        byte[] barr3;
        int t;
        byte tem;
        byte hei;
        byte[] barr2;
        double itmp;
        switch(recvBuf[5]) {
            case 71:
                this.setAlarmAlertBasePower(false);
                if (recvBuf[4] != 0) {
                    mb.setOnlineSerial(PublicFunc.toHex(recvBuf[4]));
                }
                
                mb.setRecvFlag((byte)0);
                recvBuf[7] = 0;
                break;
            case 72:
            case 74:
            case 75:
            case 77:
            case 79:
            case 81:
            case 83:
            case 84:
            case 85:
            default:
                this.setAlarmAlertBasePower(false);
                mb.setDataFlag((byte)0);
                mb.setRecvFlag((byte)0);
                recvBuf[7] = 0;
                break;
            case 73:
                this.setAlarmAlertBasePower(false);
                mb.setRecvFlag(recvBuf[5]);
                stmp = PublicFunc.toHex(recvBuf[6]) + PublicFunc.toHex(recvBuf[7]) + PublicFunc.toHex(recvBuf[8]);
                this.alertBaseDevID = Integer.valueOf(stmp, 16);
                this.alertBaseVersion = Integer.toString(this.alertBaseDevID);
                SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                Date date = Calendar.getInstance().getTime();
                this.abonlineid = this.alertBaseDevID + fmt.format(date);
                stmp = PublicFunc.toHex(recvBuf[8]);
                this.alertBaseVersion = this.alertBaseVersion + "(" + stmp + ")";
                break;
            case 76:
                this.setAlarmAlertBasePower(false);
                if (recvBuf[4] != 0) {
                    mb.setOnlineSerial(PublicFunc.toHex(recvBuf[4]));
                }
                
                mb.setSignal(false);
                recvBuf[7] = 0;
                break;
            case 78:
                this.setAlarmAlertBasePower(false);
                if (recvBuf[4] != 0) {
                    mb.setOnlineSerial(PublicFunc.toHex(recvBuf[4]));
                }
                
                send_snum[4] = recvBuf[4];
                
                try {
                    this.Write(send_snum);
                } catch (IOException var25) {
                    var25.printStackTrace();
                }
                
                mb.setRecvFlag((byte)78);
                recvBuf[7] = 0;
                System.out.println("---get first back ok!");
                break;
            case 80:
                this.setAlarmAlertBasePower(false);
                switch(recvBuf[7]) {
                    case 2:
                    default:
                        return;
                    case 32:
                        if (recvBuf[4] != 0) {
                            mb.setOnlineSerial(PublicFunc.toHex(recvBuf[4]));
                        }
                        
                        send_pandt[4] = recvBuf[4];
                        send_pandt[7] = -112;
                        send_pandt[8] = recvBuf[8];
                        send_pandt[9] = PublicFunc.HexToByte(GV.ab_height_hex.substring(0, 2));
                        send_pandt[10] = PublicFunc.HexToByte(GV.ab_height_hex.substring(2, 4));
                        send_pandt[11] = PublicFunc.HexToByte(GV.ab_height_hex.substring(4, 6));
                        //System.out.println("---send pandt bytes: " + PublicFunc.toHex(send_pandt));
                        
                        try {
                            this.Write(send_pandt);
                        } catch (IOException var19) {
                            var19.printStackTrace();
                        }
                        
                        recvBuf[7] = 0;
                        itmp = (double)((((recvBuf[10] & 255) << 8) + (recvBuf[11] & 255)) * 69 / 1000);
                        mb.setPressureValue(itmp);
                        //System.out.println("---Get airpressure message OK: " + String.valueOf(itmp));
                        if (PublicFunc.toHex(recvBuf[12]).equalsIgnoreCase("FF")) {
                            t = -1;
                        } else {
                            t = recvBuf[12] & 255;
                        }
                        
                        mb.setRemainingTime(t);
                        //System.out.println("---Get remiantime message OK: " + t);
                        if (PublicFunc.toHex(recvBuf[15]).equalsIgnoreCase("FF")) {
                            recvBuf[15] = 0;
                        }
                        
                        stmp = PublicFunc.HexToBin(PublicFunc.toHex(recvBuf[15])) + PublicFunc.HexToBin(PublicFunc.toHex(recvBuf[16]));
                        mb.setAlarmMessage(stmp);
                        //System.out.println("---Get alarm message OK: " + stmp);
                        tem = recvBuf[13];
                        mb.setTemperature(String.valueOf(tem));
                        //System.out.println("---Get temp message OK: " + PublicFunc.toHex(recvBuf[13]) + " " + tem);
                        stmp = PublicFunc.toHex(recvBuf[14]);
                        hei = recvBuf[14];
                        mb.setHeightVal(String.valueOf(hei));
                        //System.out.println("---Get height message OK: " + stmp + " " + hei);
                        return;
                    case 34:
                        if (recvBuf[4] != 0) {
                            mb.setOnlineSerial(PublicFunc.toHex(recvBuf[4]));
                        }
                        
                        send_team[4] = recvBuf[4];
                        
                        try {
                            this.Write(send_team);
                        } catch (IOException var20) {
                            var20.printStackTrace();
                        }
                        
                        recvBuf[7] = 0;
                        stmp = "";
                        barr2 = new byte[12];
                        
                        for(i = 0; i < 12; ++i) {
                            barr2[i] = recvBuf[9 + i];
                        }
                        
                        stmp = new String(barr2, GV.ccharset);
                        mb.setMemberName(stmp.trim());
                        //System.out.println("---get Name message ok! is " + stmp);
                        return;
                    case 35:
                        if (recvBuf[4] != 0) {
                            mb.setOnlineSerial(PublicFunc.toHex(recvBuf[4]));
                        }
                        
                        send_name[4] = recvBuf[4];
                        
                        try {
                            this.Write(send_name);
                        } catch (IOException var21) {
                            var21.printStackTrace();
                        }
                        
                        recvBuf[7] = 0;
                        stmp = "";
                        barr = new byte[3];
                        
                        for(i = 0; i < 3; ++i) {
                            barr[i] = recvBuf[8 + i];
                        }
                        
                        stmp = String.valueOf(PublicFunc.byte2int(barr));
                        mb.setSerialNumber(stmp.trim());
                        mb.setAbonlineid(this.abonlineid);
                        mb.setAlertBase(String.valueOf(this.alertBaseDevID));
                        System.out.println("---get AG serial ok! is " + stmp);
                        stmp = "";
                        
                        for(i = 0; i < 3; ++i) {
                            barr[i] = recvBuf[11 + i];
                        }
                        
                        stmp = String.valueOf(PublicFunc.byte2int(barr));
                        mb.setAlertMITTER(stmp.trim());
                        System.out.println("---get AP serial ok! is " + stmp);
                        if (recvBuf.length > 21) {
                            mb.setAlertVer(PublicFunc.toHex(recvBuf[21]));
                        }
                        
                        return;
                    case 36:
                        if (recvBuf[4] != 0) {
                            mb.setOnlineSerial(PublicFunc.toHex(recvBuf[4]));
                        }
                        
                        recvBuf[7] = 0;
                        stmp = "";
                        barr3 = new byte[12];
                        
                        for(i = 0; i < 12; ++i) {
                            barr3[i] = recvBuf[9 + i];
                        }
                        
                        stmp = new String(barr3, GV.ccharset);
                        if ("Repeater".equals(stmp.trim())) {
                            mb.setTeamName("");
                            mb.setAgpType(1);
                        } else if ("Beeper".equals(stmp.trim())) {
                            mb.setTeamName("");
                            mb.setAgpType(2);
                        } else {
                            mb.setTeamName(stmp.trim());
                        }
                        
                        System.out.println("---get team message ok! is " + stmp);
                        return;
                    case 43:
                        if (recvBuf[4] != 0) {
                            mb.setOnlineSerial(PublicFunc.toHex(recvBuf[4]));
                        }
                        
                        mb.setOffline(true);
                        recvBuf[7] = 0;
                        return;
                }
            case 82:
                this.setAlarmAlertBasePower(false);
                if (recvBuf[4] != 0) {
                    mb.setOnlineSerial(PublicFunc.toHex(recvBuf[4]));
                }
                
                switch(recvBuf[7]) {
                    case 32:
                        if (recvBuf[6] != 0) {
                            mb.setOnlineSerial(PublicFunc.toHex(recvBuf[6]));
                        }
                        
                        send_pandt[4] = recvBuf[6];
                        send_pandt[7] = -112;
                        send_pandt[8] = recvBuf[8];
                        send_pandt[9] = PublicFunc.HexToByte(GV.ab_height_hex.substring(0, 2));
                        send_pandt[10] = PublicFunc.HexToByte(GV.ab_height_hex.substring(2, 4));
                        send_pandt[11] = PublicFunc.HexToByte(GV.ab_height_hex.substring(4, 6));
                        
                        try {
                            this.Write(send_pandt);
                        } catch (IOException var22) {
                            var22.printStackTrace();
                        }
                        
                        recvBuf[7] = 0;
                        itmp = (double)((((recvBuf[10] & 255) << 8) + (recvBuf[11] & 255)) * 69 / 1000);
                        mb.setPressureValue(itmp);
                        //System.out.println("---Get airpressure message OK: " + String.valueOf(itmp));
                        if (PublicFunc.toHex(recvBuf[12]).equalsIgnoreCase("FF")) {
                            t = -1;
                        } else {
                            t = recvBuf[12] & 255;
                        }
                        
                        mb.setRemainingTime(t);
                        //System.out.println("---Get remiantime message OK: " + t);
                        if (PublicFunc.toHex(recvBuf[15]).equalsIgnoreCase("FF")) {
                            recvBuf[15] = 0;
                        }
                        
                        stmp = PublicFunc.HexToBin(PublicFunc.toHex(recvBuf[15])) + PublicFunc.HexToBin(PublicFunc.toHex(recvBuf[16]));
                        mb.setAlarmMessage(stmp);
                        //System.out.println("---Get alarm message OK: " + stmp);
                        tem = recvBuf[13];
                        mb.setTemperature(String.valueOf(tem));
                        //System.out.println("---Get temp message OK: " + PublicFunc.toHex(recvBuf[13]) + " " + tem);
                        stmp = PublicFunc.toHex(recvBuf[14]);
                        hei = recvBuf[14];
                        mb.setHeightVal(String.valueOf(hei));
                        //System.out.println("---Get height message OK: " + stmp + " " + hei);
                        mb.setRelay(true);
                        return;
                    case 33:
                    case 37:
                    case 38:
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                    default:
                        return;
                    case 34:
                        if (recvBuf[6] != 0) {
                            mb.setOnlineSerial(PublicFunc.toHex(recvBuf[6]));
                        }
                        
                        send_team[4] = recvBuf[6];
                        
                        try {
                            this.Write(send_team);
                        } catch (IOException var23) {
                            var23.printStackTrace();
                        }
                        
                        recvBuf[7] = 0;
                        stmp = "";
                        barr2 = new byte[12];
                        
                        for(i = 0; i < 12; ++i) {
                            barr2[i] = recvBuf[9 + i];
                        }
                        
                        stmp = new String(barr2, GV.ccharset);
                        mb.setMemberName(stmp.trim());
                        mb.setRelay(true);
                        return;
                    case 35:
                        if (recvBuf[6] != 0) {
                            mb.setOnlineSerial(PublicFunc.toHex(recvBuf[6]));
                        }
                        
                        send_name[4] = recvBuf[6];
                        
                        try {
                            this.Write(send_name);
                        } catch (IOException var24) {
                            var24.printStackTrace();
                        }
                        
                        recvBuf[7] = 0;
                        stmp = "";
                        barr = new byte[3];
                        
                        for(i = 0; i < 3; ++i) {
                            barr[i] = recvBuf[8 + i];
                        }
                        
                        stmp = String.valueOf(PublicFunc.byte2int(barr));
                        mb.setSerialNumber(stmp.trim());
                        mb.setAbonlineid(this.abonlineid);
                        mb.setAlertBase(String.valueOf(this.alertBaseDevID));
                        stmp = "";
                        
                        for(i = 0; i < 3; ++i) {
                            barr[i] = recvBuf[11 + i];
                        }
                        
                        stmp = String.valueOf(PublicFunc.byte2int(barr));
                        mb.setAlertMITTER(stmp.trim());
                        mb.setRelay(true);
                        if (recvBuf.length > 21) {
                            mb.setAlertVer(PublicFunc.toHex(recvBuf[21]));
                        }
                        
                        return;
                    case 36:
                        if (recvBuf[6] != 0) {
                            mb.setOnlineSerial(PublicFunc.toHex(recvBuf[6]));
                        }
                        
                        recvBuf[7] = 0;
                        stmp = "";
                        barr3 = new byte[12];
                        
                        for(i = 0; i < 12; ++i) {
                            barr3[i] = recvBuf[9 + i];
                        }
                        
                        stmp = new String(barr3, GV.ccharset);
                        mb.setTeamName(stmp.trim());
                        mb.setRelay(true);
                        return;
                    case 43:
                        if (recvBuf[6] != 0) {
                            mb.setOnlineSerial(PublicFunc.toHex(recvBuf[6]));
                        }
                        
                        mb.setOffline(true);
                        recvBuf[7] = 0;
                        mb.setRelay(true);
                        return;
                }
            case 86:
                this.setAlarmAlertBasePower(true);
        }
        
    }
    
    public byte[] ReadAlertBaseInfo_(MemberBase mb, long lastSendCycleTime) throws IOException {
        byte[] result = (byte[])null;
        if (GV.Comm_Type == 1) {
            if (GV.OSName == GV.EPlatform.Windows) {
                if (this.port == null || this.ReadBufferQueue == null) {
                    return null;
                }
            } else if (this.mFd == null || this.mFileOutputStream == null || this.mFileInputStream == null) {
                return null;
            }
        } else {
            int var10000 = GV.Comm_Type;
        }
        
        byte[] send_cycle = new byte[]{16, 2, 69, 66, -1, 65, -53, 3, 16, 3};
        mb.setLastSendCycleTime(lastSendCycleTime);
        Date curDate = Calendar.getInstance().getTime();
        long curTime = curDate.getTime();
        if (curTime - lastSendCycleTime >= (long)GV.cycle_interval) {
            mb.setLastSendCycleTime(curTime);
            
            try {
                this.Write(send_cycle);
            } catch (IOException var11) {
                var11.printStackTrace();
                System.out.println("---send cycle error ! reload usb driver---");
            }
        }
        
        byte[] rb = new byte[30];
        int len = this.Read(rb);
        if (len > 0) {
            result = new byte[rb.length];
            System.arraycopy(rb, 0, result, 0, len);
            this.writeBaseDataToMember(rb, mb);
        }
        
        return result;
    }
    
    public void SendFirst_() throws IOException {
        byte[] send_first = new byte[]{16, 2, 69, 66, 0, 63, 10, -45, 16, 3};
        this.Write(send_first);
    }
    
    public void SendReadTag() {
        byte[] send_readtag = new byte[]{16, 2, 1, 1, 82, 0, 0, 16, 3};
        
        try {
            this.Write(send_readtag);
            System.out.println("---send read tag command ok---" + PublicFunc.toHex(send_readtag));
        } catch (IOException var3) {
            System.out.println("---send read tag command error---");
            var3.printStackTrace();
        }
        
    }
    
    private void writeTagDataToMember(int len, byte[] recvBuf, MemberBase mb, boolean hasWrite) {
        System.out.println("---recv size: " + len);
        //System.out.println("---recv: " + PublicFunc.toHex(recvBuf));
        String stmp = "";
        if (!hasWrite) {
            System.out.println("---读取标签数据---");
            if (len > 4) {
                if (recvBuf[3] == 33) {
                    mb.setOnlineSerial("9");
                } else {
                    mb.setOnlineSerial("2");
                }
            } else {
                mb.setOnlineSerial("1");
            }
        } else {
            System.out.println("---写入数据之后再读取一次---");
            if (this.read_count < 1) {
                if (len > 6) {
                    ++this.read_count;
                    this.SendReadTag();
                    System.out.println("---reread---");
                    mb.setOnlineSerial("0");
                } else {
                    mb.setOnlineSerial("1");
                }
            } else if (len > 4) {
                ++this.read_count;
                if (recvBuf[3] == 33) {
                    mb.setOnlineSerial("9");
                } else {
                    mb.setOnlineSerial("3");
                }
            } else {
                mb.setOnlineSerial("1");
            }
        }
        
        if ("9".equals(mb.getOnlineSerial())) {
            mb.setRecvFlag(recvBuf[7]);
            byte[] barr1;
            int i;
            switch(recvBuf[7]) {
                case 66:
                    mb.setAgpType(9);
                    stmp = PublicFunc.toHex(recvBuf[11]) + PublicFunc.toHex(recvBuf[12]) + PublicFunc.toHex(recvBuf[13]);
                    mb.setAlertBase(Integer.toString(Integer.parseInt(stmp, 16)));
                    System.out.println("---read base: " + mb.getAlertBase());
                    break;
                case 70:
                    switch(recvBuf[9]) {
                        case 65:
                            mb.setAgpType(2);
                            if (PublicFunc.toHex(recvBuf[10]).equalsIgnoreCase("AA")) {
                                mb.setAlarmStatic(true);
                                System.out.println("---read Help active alarm ---");
                            } else {
                                mb.setAlarmStatic(false);
                                System.out.println("---read Help no active alarm ---");
                            }
                            
                            return;
                        case 82:
                            mb.setAgpType(1);
                            System.out.println("---read Repeatrs ---");
                            return;
                        default:
                            return;
                    }
                case 78:
                    byte[] barr = new byte[4];
                    
                    for(int i1 = 0; i1 < 4; ++i1) {
                        barr[i1] = recvBuf[33 + i1];
                    }
                    
                    if ("FFFFFFFF".equalsIgnoreCase(PublicFunc.toHex(barr))) {
                        mb.setAlertVer("FFFFFFFF");
                        barr1 = new byte[12];
                        
                        for(i = 0; i < 12; ++i) {
                            barr1[i] = recvBuf[9 + i];
                        }
                        
                        System.out.println("---read new membername hexs: " + PublicFunc.toHex(barr1));
                        stmp = new String(barr1, GV.ccharset);
                        mb.setMemberName(stmp.trim());
                        System.out.println("---read new membername: " + mb.getMemberName());
                        byte[] barr2 = new byte[12];
                        
                        for(int i1 = 0; i1 < 12; ++i1) {
                            barr2[i1] = recvBuf[21 + i1];
                        }
                        
                        stmp = new String(barr2, GV.ccharset);
                        mb.setTeamName(stmp.trim());
                        System.out.println("---read new teamname: " + mb.getTeamName());
                    } else {
                        barr1 = new byte[28];
                        
                        for(i = 0; i < 26; ++i) {
                            barr1[i] = recvBuf[9 + i];
                        }
                        
                        stmp = new String(barr1, GV.ccharset);
                        mb.setMemberName(stmp.trim());
                        System.out.println("---read name: " + mb.getMemberName());
                    }
                    break;
                case 80:
                    barr1 = new byte[28];
                    
                    for(i = 0; i < 26; ++i) {
                        barr1[i] = recvBuf[9 + i];
                    }
                    
                    stmp = new String(barr1, GV.ccharset);
                    mb.setTeamName(stmp.trim());
                    System.out.println("---read team: " + mb.getTeamName());
            }
        } else {
            mb.setRecvFlag((byte)0);
        }
        
    }
    
    public void ReadAlertTagInfo(MemberBase mb, boolean hasWrite) throws IOException {
        mb.clear();
        mb.setOnlineSerial("0");
        if (GV.Comm_Type == 1) {
            if (GV.OSName == GV.EPlatform.Windows) {
                if (this.port == null || this.ReadBufferQueue == null) {
                    return;
                }
            } else if (this.mFd == null || this.mFileOutputStream == null || this.mFileInputStream == null) {
                return;
            }
        } else {
            int var10000 = GV.Comm_Type;
        }
        
        String stmp = "";
        byte[] rb = new byte[41];
        int len = this.Read(rb);
        if (len > 0) {
            this.writeTagDataToMember(len, rb, mb, hasWrite);
        }
        
    }
    
    public void WriteTagName(String memberName) throws IOException {
        byte[] send_name = new byte[39];
        byte[] b = memberName.getBytes(GV.ccharset);
        send_name[0] = 16;
        send_name[1] = 2;
        send_name[2] = 1;
        send_name[3] = 31;
        send_name[4] = 87;
        send_name[5] = 78;
        send_name[6] = 58;
        send_name[35] = 0;
        send_name[36] = 0;
        send_name[37] = 16;
        send_name[38] = 3;
        
        for(int i = 0; i < 28; ++i) {
            if (i < b.length) {
                send_name[7 + i] = b[i];
            } else {
                send_name[7 + i] = 32;
            }
        }
        
        System.out.println("---send tag name data: " + PublicFunc.toHex(send_name));
        this.Write(send_name);
    }
    
    public void WriteTagTeamName(String teamName) throws IOException {
        byte[] send_name = new byte[39];
        byte[] b = teamName.getBytes(GV.ccharset);
        send_name[0] = 16;
        send_name[1] = 2;
        send_name[2] = 1;
        send_name[3] = 31;
        send_name[4] = 87;
        send_name[5] = 80;
        send_name[6] = 58;
        send_name[35] = 0;
        send_name[36] = 0;
        send_name[37] = 16;
        send_name[38] = 3;
        
        for(int i = 0; i < 28; ++i) {
            if (i < b.length) {
                send_name[7 + i] = b[i];
            } else {
                send_name[7 + i] = 32;
            }
        }
        
        System.out.println("---send tag team name data: " + PublicFunc.toHex(send_name));
        this.Write(send_name);
    }
    
    public void WriteIdentityTag(String memberName, String teamName) throws IOException {
        byte[] send_name = new byte[39];
        byte[] b1 = memberName.getBytes(GV.ccharset);
        byte[] b2 = teamName.getBytes(GV.ccharset);
        send_name[0] = 16;
        send_name[1] = 2;
        send_name[2] = 1;
        send_name[3] = 31;
        send_name[4] = 87;
        send_name[5] = 78;
        send_name[6] = 58;
        send_name[35] = 0;
        send_name[36] = 0;
        send_name[37] = 16;
        send_name[38] = 3;
        
        int i;
        for(i = 0; i < 12; ++i) {
            if (i < b1.length) {
                send_name[7 + i] = b1[i];
            } else {
                send_name[7 + i] = 32;
            }
        }
        
        for(i = 0; i < 12; ++i) {
            if (i < b2.length) {
                send_name[19 + i] = b2[i];
            } else {
                send_name[19 + i] = 32;
            }
        }
        
        for(i = 0; i < 4; ++i) {
            send_name[31 + i] = -1;
        }
        
        System.out.println("---send Identity tag data: " + PublicFunc.toHex(send_name));
        this.Write(send_name);
    }
    
    public boolean WriteSpecialTagCenterID(String centerID) {
        byte[] send = new byte[39];
        
        for(int i = 0; i < send.length; ++i) {
            send[i] = 32;
        }
        
        String sid = "";
        String s = Integer.toHexString(Integer.valueOf(centerID)).toUpperCase();
        if (s.length() < 6) {
            String c = "";
            
            for(int j = 1; j <= 6 - s.length(); ++j) {
                c = c + "0";
            }
            
            sid = c + s;
        } else {
            sid = s.substring(0, 6);
        }
        
        send[0] = 16;
        send[1] = 2;
        send[2] = 1;
        send[3] = 31;
        send[4] = 87;
        send[5] = 66;
        send[6] = 58;
        send[8] = 32;
        send[9] = PublicFunc.HexToByte(sid.substring(0, 2));
        send[10] = PublicFunc.HexToByte(sid.substring(2, 4));
        send[11] = PublicFunc.HexToByte(sid.substring(4, 6));
        send[7] = send[11];
        send[35] = 0;
        send[36] = 0;
        send[37] = 16;
        send[38] = 3;
        
        try {
            System.out.println("---send special tag centerid data: " + PublicFunc.toHex(send));
            this.Write(send);
            return true;
        } catch (IOException var7) {
            var7.printStackTrace();
            return false;
        }
    }
    
    public boolean WriteSpecialTagRepeatrs() {
        byte[] send = new byte[39];
        
        for(int i = 0; i < send.length; ++i) {
            send[i] = 32;
        }
        
        send[0] = 16;
        send[1] = 2;
        send[2] = 1;
        send[3] = 31;
        send[4] = 87;
        send[5] = 70;
        send[6] = 58;
        send[7] = 82;
        send[35] = 0;
        send[36] = 0;
        send[37] = 16;
        send[38] = 3;
        
        try {
            System.out.println("---send special tag repeaters data: " + PublicFunc.toHex(send));
            this.Write(send);
            return true;
        } catch (IOException var3) {
            var3.printStackTrace();
            return false;
        }
    }
    
    public boolean WriteSpecialTagHelp_HasStaticAlarm() {
        byte[] send = new byte[39];
        
        for(int i = 0; i < send.length; ++i) {
            send[i] = 32;
        }
        
        send[0] = 16;
        send[1] = 2;
        send[2] = 1;
        send[3] = 31;
        send[4] = 87;
        send[5] = 70;
        send[6] = 58;
        send[7] = 65;
        send[8] = -86;
        send[35] = 0;
        send[36] = 0;
        send[37] = 16;
        send[38] = 3;
        
        try {
            System.out.println("---send special tag help has static alarm data: " + PublicFunc.toHex(send));
            this.Write(send);
            return true;
        } catch (IOException var3) {
            var3.printStackTrace();
            return false;
        }
    }
    
    public boolean WriteSpecialTagHelp_NoStaticAlarm() {
        byte[] send = new byte[39];
        
        for(int i = 0; i < send.length; ++i) {
            send[i] = 32;
        }
        
        send[0] = 16;
        send[1] = 2;
        send[2] = 1;
        send[3] = 31;
        send[4] = 87;
        send[5] = 70;
        send[6] = 58;
        send[7] = 65;
        send[8] = 32;
        send[35] = 0;
        send[36] = 0;
        send[37] = 16;
        send[38] = 3;
        
        try {
            System.out.println("---send special tag help has no static alarm data: " + PublicFunc.toHex(send));
            this.Write(send);
            return true;
        } catch (IOException var3) {
            var3.printStackTrace();
            return false;
        }
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
    }
    
    class GetwayClientSocketThread extends Thread {
        public boolean stop = false;
        Socket socket = null;
        DataInputStream input = null;
        DataOutputStream output = null;
        String dev = "";
        int baudrate = 0;
        private MakeComplete mc = new MakeComplete();
        
        public GetwayClientSocketThread() {
            this.dev = GV.alertBaseDev;
            this.baudrate = GV.alertBaseDev_Port;
        }
        
        public boolean testConnect() {
            boolean result = false;
            
            try {
                this.socket = new Socket(this.dev, this.baudrate);
                result = true;
                
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException var3) {
                    Logger.getLogger(Alertbase.class.getName()).log(Level.SEVERE, (String)null, var3);
                }
                
                this.socket.close();
            } catch (UnknownHostException var4) {
                System.out.println("===UnknownHostException: " + var4.getMessage());
            } catch (IOException var5) {
                //System.out.println("===connect getway ioexception: " + var5.getMessage());
            }
            
            return result;
        }
        
        public boolean connectGetway() {
            boolean result = false;
            Alertbase.this.getwayConnected = false;
            
            try {
                this.socket = new Socket(this.dev, this.baudrate);
                this.input = new DataInputStream(this.socket.getInputStream());
                this.output = new DataOutputStream(this.socket.getOutputStream());
                result = true;
                Alertbase.this.getwayConnected = true;
            } catch (UnknownHostException var3) {
                System.out.println("===UnknownHostException: " + var3.getMessage());
            } catch (IOException var4) {
                //System.out.println("===connect getway ioexception: " + var4.getMessage());
            }
            
            return result;
        }
        
        public void closeClient() {
            if (this.input != null) {
                try {
                    this.input.close();
                } catch (IOException var4) {
                    System.out.println("===datainputstream close exception: " + var4.getMessage());
                }
                
                this.input = null;
            }
            
            if (this.output != null) {
                try {
                    this.output.close();
                } catch (IOException var3) {
                    System.out.println("===dataoutputstream close exception: " + var3.getMessage());
                }
                
                this.output = null;
            }
            
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException var2) {
                    System.out.println("===socket close exception: " + var2.getMessage());
                }
                
                this.socket = null;
            }
            
            this.mc.clearRecvBuf();
            this.mc.init();
            Alertbase.this.getwayConnected = false;
        }
        
        private boolean reconnectGetway() {
            //System.out.println("===reconnect getway begin ===");
            this.closeClient();
            Alertbase.this.getwayConnected = this.connectGetway();
            //System.out.println("===reconnect getway end ===" + Alertbase.this.getwayConnected);
            return Alertbase.this.getwayConnected;
        }
        
        public boolean isConnected() {
            return this.socket != null && !this.socket.isClosed() && this.socket.isConnected();
        }
        
        @Override
        public void run() {
            this.mc.clearRecvBuf();
            this.mc.init();
            
            while(!this.stop) {
                if (this.socket == null || this.socket.isClosed() || !this.socket.isConnected()) {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException var8) {
                    }
                    
                    this.reconnectGetway();
                }
                
                try {
                    if (this.input != null && this.input.available() > 0) {
                        byte[] rb = new byte[256];
                        int len = this.input.read(rb);
                        if (len > 0) {
                            byte[] bb = new byte[len];
                            System.arraycopy(rb, 0, bb, 0, len);
                            //System.out.println("---接收到的原始数据: " + PublicFunc.toHex(bb));
                            
                            for(int i = 0; i < len; ++i) {
                                this.mc.makeCompleteRecvData(rb[i]);
                                if (this.mc.getRecvOver() == 2) {
                                    byte[] newbuf = new byte[this.mc.getBufIndex() + 1];
                                    System.arraycopy(this.mc.getRecvBuf(), 0, newbuf, 0, this.mc.getBufIndex() + 1);
                                    Alertbase.this.ReadBufferQueue.add(new RecvBufferItem(this.mc.getBufIndex() + 1, newbuf));
                                    //System.out.println("---buffer temp: " + PublicFunc.toHex(newbuf));
                                    this.mc.clearRecvBuf();
                                    this.mc.init();
                                }
                            }
                        }
                    }
                } catch (IOException var9) {
                    System.out.println("===socket read error: " + var9.getMessage());
                    this.reconnectGetway();
                }
                
                if (Alertbase.this.WriteBufferQueue != null && !Alertbase.this.WriteBufferQueue.isEmpty()) {
                    RecvBufferItem bi = (RecvBufferItem) Alertbase.this.WriteBufferQueue.poll();
                    //System.out.println("===send data：" + PublicFunc.toHex_db(bi.getBuf()));
                    if (this.output != null) {
                        try {
                            this.output.write(bi.getBuf());
                        } catch (IOException var7) {
                            System.out.println("===socket write error: " + var7.getMessage());
                            this.reconnectGetway();
                        }
                    }
                }
                
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var6) {
                }
            }
            
        }
    }
    
    class LinuxLocalReadCommThread extends Thread {
        public boolean stop = false;
        private MakeComplete mc = new MakeComplete();
        
        LinuxLocalReadCommThread() {
        }
        
        @Override
        public void run() {
            this.mc.clearRecvBuf();
            this.mc.init();
            
            while(true) {
                while(!this.stop) {
                    if (Alertbase.this.mFileInputStream == null) {
                        try {
                            Thread.sleep(50L);
                        } catch (InterruptedException var7) {
                        }
                    } else {
                        boolean var1 = false;
                        
                        int av;
                        try {
                            av = Alertbase.this.mFileInputStream.available();
                        } catch (IOException var14) {
                            try {
                                Alertbase.this.Reload_Reopen_Usb();
                                av = Alertbase.this.mFileInputStream.available();
                            } catch (Exception var13) {
                                try {
                                    Thread.sleep(20L);
                                } catch (InterruptedException var10) {
                                }
                                continue;
                            }
                        }
                        
                        if (av > 0) {
                            byte[] rb = new byte[256];
                            boolean var3 = false;
                            
                            int len;
                            try {
                                len = Alertbase.this.mFileInputStream.read(rb);
                            } catch (IOException var12) {
                                try {
                                    Alertbase.this.Reload_Reopen_Usb();
                                    len = Alertbase.this.mFileInputStream.read(rb);
                                } catch (Exception var11) {
                                    try {
                                        Thread.sleep(20L);
                                    } catch (InterruptedException var9) {
                                    }
                                    continue;
                                }
                            }
                            
                            if (len > 0) {
                                byte[] bb = new byte[len];
                                System.arraycopy(rb, 0, bb, 0, len);
                                //System.out.println("---接收到的原始数据: " + PublicFunc.toHex(bb));
                                
                                for(int i = 0; i < len; ++i) {
                                    this.mc.makeCompleteRecvData(rb[i]);
                                    if (this.mc.getRecvOver() == 2) {
                                        byte[] newbuf = new byte[this.mc.getBufIndex() + 1];
                                        System.arraycopy(this.mc.getRecvBuf(), 0, newbuf, 0, this.mc.getBufIndex() + 1);
                                        Alertbase.this.ReadBufferQueue.add(new RecvBufferItem(this.mc.getBufIndex() + 1, newbuf));
                                        //System.out.println("---buffer temp: " + PublicFunc.toHex(newbuf));
                                        this.mc.clearRecvBuf();
                                        this.mc.init();
                                    }
                                }
                            }
                        }
                        
                        try {
                            Thread.sleep(20L);
                        } catch (InterruptedException var8) {
                        }
                    }
                }
                
                return;
            }
        }
    }
    
    class SerialPortEventReader implements SerialPortEventListener {
        MakeComplete mc = new MakeComplete();
        
        public SerialPortEventReader() {
            this.mc.clearRecvBuf();
            this.mc.init();
        }
        
        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR()) {
                try {
                    byte[] rb = Alertbase.this.port.readBytes();
                    
                    for(int i = 0; i < rb.length; ++i) {
                        this.mc.makeCompleteRecvData(rb[i]);
                        if (this.mc.getRecvOver() == 2) {
                            byte[] newbuf = new byte[this.mc.getBufIndex() + 1];
                            System.arraycopy(this.mc.getRecvBuf(), 0, newbuf, 0, this.mc.getBufIndex() + 1);
                            Alertbase.this.ReadBufferQueue.add(new RecvBufferItem(this.mc.getBufIndex() + 1, newbuf));
                            //System.out.println("---buffer temp: " + PublicFunc.toHex(newbuf));
                            this.mc.clearRecvBuf();
                            this.mc.init();
                        }
                    }
                } catch (SerialPortException var5) {
                    System.out.println(var5);
                }
            }
            
        }
    }
}
