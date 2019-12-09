package com.daishuai.alert.alert;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * @author Daishuai
 * @description
 * @date 2019/9/5 13:41
 */
public class GV {
    public static String VERSION = "COPYRIGHT CTEC VER 3.3.35";
    public static Properties config = new Properties();
    public static int TemperatureAlarmVal = 50;
    public static int GasCylinderPressureFullValue = 300;
    public static int MaxMemberCount = 24;
    public static int MaxTeamCount = 6;
    public static int PressureReductionPerMinute = 10;
    public static String alertBaseDev = "/dev/ttyUSB0";
    public static int signal_timeout = 15;
    public static int signal_timeout_count = 2;
    public static String height_info_show = "1";
    public static String RootPwd = "123456";
    public static String LoadUsbDriver = "0";
    public static String ShellType = "/bin/bash";
    public static String OutPrintType = "1";
    public static String LoadUsbDriver_Command_Base = "modprobe ftdi_sio vendor=0x0403 product=0xdb79";
    public static String LoadUsbDriver_Command_Tag = "modprobe ftdi_sio vendor=0x0403 product=0xdb7a";
    public static String UninstallUsbDriver_Command_Base = "rmmod ftdi_sio;rmmod usbserial";
    public static String UninstallUsbDriver_Command_Tag = "rmmod ftdi_sio;rmmod usbserial";
    public static int WindowWidth = 0;
    public static int WindowHeight = 0;
    public static GV.EPlatform OSName = OSInfo.getOSname();
    public static final String DEVICE_NOT_EXISTS = "device is not exists";
    public static int Comm_Type = 1;
    public static int alertBaseDev_Port = 38400;
    public static Long ab_height = 0L;
    public static String ab_height_hex = "000000";
    public static long startTime;
    public static boolean connectServer = false;
    public static String serverAddress = "localhost";
    public static int serverPort = 1205;
    public static final String scharset = "utf-8";
    public static final Charset ccharset = Charset.forName("utf-8");
    public static boolean disableServerCfg = false;
    public static int cycle_interval = 3250;
    
    public GV() {
    }
    
    public static int OxStringtoInt(String ox) throws Exception {
        ox = ox.toLowerCase();
        if (ox.startsWith("0x")) {
            ox = ox.substring(2, ox.length());
        }
        
        int ri = 0;
        int oxlen = ox.length();
        if (oxlen > 8) {
            throw new Exception("too lang");
        } else {
            for(int i = 0; i < oxlen; ++i) {
                char c = ox.charAt(i);
                int h;
                if ('0' <= c && c <= '9') {
                    h = c - 48;
                } else if ('a' <= c && c <= 'f') {
                    h = c - 87;
                } else {
                    if ('A' > c || c > 'F') {
                        throw new Exception("not a integer ");
                    }
                    
                    h = c - 55;
                }
                
                byte left = (byte)((oxlen - i - 1) * 4);
                ri |= h << left;
            }
            
            return ri;
        }
    }
    
    public static void readConfig() {
        try {
            String path = GV.class.getProtectionDomain().getCodeSource().getLocation().getFile();
            path = URLDecoder.decode(path, "UTF-8");
            System.out.println("===jar path: " + path);
            int findex;
            if (OSName == GV.EPlatform.Windows) {
                findex = path.indexOf("/") + 1;
                int lindex = path.lastIndexOf("/");
                path = path.substring(findex, lindex);
            } else {
                findex = path.lastIndexOf("/");
                path = path.substring(1, findex);
            }
            
            System.out.println("===resource file path: " + path);
            config.load(new FileInputStream(path + "/alert.properties"));
            System.out.println("=====xxxx: " + config.getProperty("alertBaseDev"));
            alertBaseDev_Port = Integer.valueOf(config.getProperty("alertBaseDev_Port", "38400"));
            alertBaseDev = config.getProperty("alertBaseDev");
            Comm_Type = Integer.valueOf(config.getProperty("Comm_Type", "1"));
            cycle_interval = Integer.valueOf(config.getProperty("cycle_interval", "3250"));
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        
    }
    
    public static void main(String[] args) throws URISyntaxException, UnsupportedEncodingException {
    }
    
    public static enum EPlatform {
        Any("any"),
        Linux("Linux"),
        Mac_OS("Mac OS"),
        Mac_OS_X("Mac OS X"),
        Windows("Windows"),
        OS2("OS/2"),
        Solaris("Solaris"),
        SunOS("SunOS"),
        MPEiX("MPE/iX"),
        HP_UX("HP-UX"),
        AIX("AIX"),
        OS390("OS/390"),
        FreeBSD("FreeBSD"),
        Irix("Irix"),
        Digital_Unix("Digital Unix"),
        NetWare_411("NetWare"),
        OSF1("OSF1"),
        OpenVMS("OpenVMS"),
        Others("Others");
        
        private String description;
        
        private EPlatform(String desc) {
            this.description = desc;
        }
        
        @Override
        public String toString() {
            return this.description;
        }
    }
    
    public static enum OpenDeviceType {
        AlertBase,
        AlertTag,
        AlertTagSetting;
        
        private OpenDeviceType() {
        }
    }
}
