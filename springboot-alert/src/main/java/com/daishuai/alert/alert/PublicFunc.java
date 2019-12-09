package com.daishuai.alert.alert;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Daishuai
 * @description
 * @date 2019/9/5 13:42
 */
public class PublicFunc {
    private static String[] hexs = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    private static String[] bins = new String[]{"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
    
    public PublicFunc() {
    }
    
    public static String toHex(byte[] bs) {
        String hexString = "0123456789ABCDEF";
        StringBuilder sb = new StringBuilder(bs.length * 2);
        
        for(int i = 0; i < bs.length; ++i) {
            sb.append(hexString.charAt((bs[i] & 240) >> 4));
            sb.append(hexString.charAt((bs[i] & 15) >> 0));
        }
        
        return sb.toString();
    }
    
    public static String toHex(byte b) {
        String hexString = "0123456789ABCDEF";
        StringBuilder sb = new StringBuilder(2);
        sb.append(hexString.charAt((b & 240) >> 4));
        sb.append(hexString.charAt((b & 15) >> 0));
        return sb.toString();
    }
    
    public static String toHex_db(byte[] bs) {
        String hexString = "0123456789ABCDEF";
        StringBuilder sb = new StringBuilder(bs.length * 2);
        
        for(int i = 0; i < bs.length; ++i) {
            sb.append(hexString.charAt((bs[i] & 240) >> 4));
            sb.append(hexString.charAt((bs[i] & 15) >> 0));
            if (i < bs.length - 1) {
                sb.append(",");
            }
        }
        
        return sb.toString();
    }
    
    public static String formatHourMinute(int minutes) {
        if (minutes <= 0) {
            return "00:00";
        } else {
            String rs = "00:00";
            int h = minutes / 60;
            int m = minutes - h * 60;
            String hh = String.valueOf(h);
            if (hh.length() < 2) {
                hh = "0" + hh;
            }
            
            String mm = String.valueOf(m);
            if (mm.length() < 2) {
                mm = "0" + mm;
            }
            
            rs = hh + ":" + mm;
            return rs;
        }
    }
    
    public static String formatMinuteSeconds(int seconds) {
        String rs = "0:00";
        int m = seconds / 60;
        int s = seconds - m * 60;
        String mm = String.valueOf(m);
        if (mm.length() < 2) {
            mm = "0" + mm;
        }
        
        String ss = String.valueOf(s);
        if (ss.length() < 2) {
            ss = "0" + ss;
        }
        
        rs = mm + ":" + ss;
        return rs;
    }
    
    public static int byte2int(byte[] res) {
        int rs = 0;
        switch(res.length) {
            case 1:
                rs = res[0] & 255;
                break;
            case 2:
                rs = res[0] & 255 | res[1] << 8 & '\uff00';
                break;
            case 3:
                rs = res[0] & 255 | res[1] << 8 & '\uff00' | res[2] << 24 >>> 8;
                break;
            case 4:
                rs = res[0] & 255 | res[1] << 8 & '\uff00' | res[2] << 24 >>> 8 | res[3] << 24;
        }
        
        return rs;
    }
    
    private static String getBin(String hex) {
        int i;
        for(i = 0; i < hexs.length && !hex.toLowerCase().equals(hexs[i]); ++i) {
        }
        
        return bins[i];
    }
    
    public static String HexToBin(String hex) {
        StringBuffer buff = new StringBuffer();
        
        for(int i = 0; i < hex.length(); ++i) {
            buff.append(getBin(hex.substring(i, i + 1)));
        }
        
        return buff.toString();
    }
    
    private static byte charToByte(char c) {
        return (byte)"0123456789ABCDEF".indexOf(c);
    }
    
    public static byte HexToByte(String hex) {
        if (hex != null && !"".equals(hex)) {
            char[] hexChars = hex.toUpperCase().toCharArray();
            return (byte)(charToByte(hexChars[0]) << 4 | charToByte(hexChars[1]));
        } else {
            return 0;
        }
    }
    
    public static byte[] HexsToBytes(String hexs) {
        if (hexs != null && !"".equals(hexs)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            
            try {
                int i = 0;
                
                for(char[] hexChars = hexs.toUpperCase().toCharArray(); i < hexChars.length; i += 2) {
                    byte b = (byte)(charToByte(hexChars[i]) << 4 | charToByte(hexChars[i + 1]));
                    baos.write(b);
                }
                
                return baos.toByteArray();
            } catch (Exception var5) {
                return null;
            }
        } else {
            return null;
        }
    }
    
    public static String formatDateTime(String format, Date date) {
        String rs = "";
        if (date == null) {
            return rs;
        } else {
            try {
                SimpleDateFormat fmt = new SimpleDateFormat(format);
                rs = fmt.format(date);
            } catch (Exception var4) {
            }
            
            return rs;
        }
    }
    
    public static String booleanToStr(boolean b) {
        return !b ? "0" : "1";
    }
    
    public static boolean strToBoolean(String s) {
        boolean rs = true;
        if (s == null || "0".equals(s) || "".equals(s)) {
            rs = false;
        }
        
        return rs;
    }
    
    public static String fillRight(String s, String c, int l) {
        String rs = s;
        
        for(int i = 1; i <= l; ++i) {
            rs = rs + c;
        }
        
        return rs;
    }
    
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString != null && !hexString.equals("")) {
            int length = hexString.length() / 2;
            char[] hexChars = hexString.toCharArray();
            byte[] d = new byte[length];
            
            for(int i = 0; i < length; ++i) {
                int pos = i * 2;
                d[i] = (byte)(charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            }
            
            return d;
        } else {
            return null;
        }
    }
    
    public static String getProjectPath() {
        URL url = PublicFunc.class.getProtectionDomain().getCodeSource().getLocation();
        String filePath = null;
        
        try {
            filePath = URLDecoder.decode(url.getPath(), "utf-8");
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        
        if (filePath.endsWith(".jar")) {
            filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
        }
        
        File file = new File(filePath);
        filePath = file.getAbsolutePath();
        return filePath;
    }
    
    public static String fillLeftFmt(String source, int len) {
        int length = 0;
        String str = "";
        
        int i;
        int ascii;
        for(i = 0; i < source.length(); ++i) {
            ascii = Character.codePointAt(source, i);
            if (ascii >= 0 && ascii <= 255) {
                ++length;
            } else {
                length += 2;
            }
            
            if (length > len) {
                if (ascii >= 0 && ascii <= 255) {
                    --length;
                } else {
                    length -= 2;
                }
                
                for(int j = 1; j <= len - length; ++j) {
                    str = " " + str;
                }
                
                return str;
            }
            
            str = str + source.charAt(i);
        }
        
        i = len - length;
        
        for(ascii = 1; ascii <= i; ++ascii) {
            str = " " + str;
        }
        
        return str;
    }
    
    public static String fillRightFmt(String source, int len) {
        int length = 0;
        String str = "";
        
        int i;
        int ascii;
        for(i = 0; i < source.length(); ++i) {
            ascii = Character.codePointAt(source, i);
            if (ascii >= 0 && ascii <= 255) {
                ++length;
            } else {
                length += 2;
            }
            
            if (length > len) {
                if (ascii >= 0 && ascii <= 255) {
                    --length;
                } else {
                    length -= 2;
                }
                
                for(int j = 1; j <= len - length; ++j) {
                    str = str + " ";
                }
                
                return str;
            }
            
            str = str + source.charAt(i);
        }
        
        i = len - length;
        
        for(ascii = 1; ascii <= i; ++ascii) {
            str = str + " ";
        }
        
        return str;
    }
    
    public static String fillSideFmt(String source, int len) {
        int length = 0;
        String str = "";
        
        int i;
        int j;
        for(i = 0; i < source.length(); ++i) {
            int ascii = Character.codePointAt(source, i);
            if (ascii >= 0 && ascii <= 255) {
                ++length;
            } else {
                length += 2;
            }
            
            if (length > len) {
                if (ascii >= 0 && ascii <= 255) {
                    --length;
                } else {
                    length -= 2;
                }
                
                for(j = 1; j <= len - length; ++j) {
                    str = str + " ";
                }
                
                return str;
            }
            
            str = str + source.charAt(i);
        }
        
        i = (len - length) / 2;
        String fl = "";
        
        for(j = 1; j <= i; ++j) {
            fl = fl + " ";
        }
        
        String fr = fl;
        if ((fl + fl).length() < len - length) {
            fr = fl + " ";
        }
        
        return fl + str + fr;
    }
    
    public static void main(String[] args) {
    }
}
