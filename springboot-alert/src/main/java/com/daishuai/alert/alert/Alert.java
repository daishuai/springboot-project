package com.daishuai.alert.alert;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Daishuai
 * @description
 * @date 2019/9/5 13:39
 */
public class Alert {
    private Alertbase ab = new Alertbase();
    private ConcurrentMap<String, MemberBase> onlineMemberMap = new ConcurrentHashMap();
    private ConcurrentMap<String, MemberBase> offlineMemberMap = new ConcurrentHashMap();
    private ReadThread readThread;
    private boolean sendfirst = false;
    
    public Alert() {
    }
    
    public boolean isConnected() {
        return this.ab != null ? this.ab.getGetwayConnected() : false;
    }
    
    public boolean testConnect(String connectDev, int baudrate) {
        if (this.ab != null) {
            this.ab.setConnectDev(connectDev);
            this.ab.setBaudrate(baudrate);
            
            try {
                return this.ab.checkOpenDevice();
            } catch (Exception var4) {
                var4.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
    
    public boolean connect(String connectDev, int baudrate) {
        System.out.println("===start connecting===");
        this.ab.setConnectDev(connectDev);
        this.ab.setBaudrate(baudrate);
        
        try {
            this.ab.OpenPort();
            this.readThread = new ReadThread();
            this.readThread.start();
            System.out.println("===connect is succes!===");
            return true;
        } catch (Exception var4) {
            System.out.println("===connect is faild!===");
            var4.printStackTrace();
            return false;
        }
    }
    
    public void disconnect() {
        System.out.println("===start disconnecting===");
        if (this.readThread != null) {
            this.readThread.stopit();
            this.readThread.interrupt();
        }
        
        if (this.ab != null) {
            this.ab.ClosePort();
        }
        
        if (this.onlineMemberMap != null) {
            this.onlineMemberMap.clear();
        }
        
        System.out.println("===disconnect over!===");
    }
    
    private void addMap(Map<String, String> map, MemberBase mb) {
        try {
            map.clear();
            map.put("serialnumber", mb.getSerialNumber());
            map.put("onlineserial", mb.getOnlineSerial());
            String df = PublicFunc.toHex(mb.getDataFlag());
            System.out.println("===dataflag:" + df + " ===");
            if ("56".equalsIgnoreCase(df)) {
                map.put("packetflag", "1");
            } else if ("49".equalsIgnoreCase(df)) {
                map.put("packetflag", "2");
            } else if ("4C".equalsIgnoreCase(df)) {
                map.put("packetflag", "3");
            } else if ("4E".equalsIgnoreCase(df)) {
                map.put("packetflag", "4");
            } else if ("47".equalsIgnoreCase(df)) {
                map.put("packetflag", "5");
            } else if ("52".equalsIgnoreCase(df)) {
                map.put("packetflag", "6");
            } else if ("50".equalsIgnoreCase(df)) {
                map.put("packetflag", "7");
            } else {
                map.put("packetflag", "0");
            }
            
            String rf = PublicFunc.toHex(mb.getRecvFlag());
            System.out.println("===recvflag:" + rf + " ===");
            if ("23".equalsIgnoreCase(rf)) {
                map.put("hostflag", "1");
            } else if ("22".equalsIgnoreCase(rf)) {
                map.put("hostflag", "2");
            } else if ("24".equalsIgnoreCase(rf)) {
                map.put("hostflag", "3");
            } else if ("20".equalsIgnoreCase(rf)) {
                map.put("hostflag", "4");
            } else if ("2B".equalsIgnoreCase(rf)) {
                map.put("hostflag", "5");
            } else {
                map.put("hostflag", "0");
            }
            
            map.put("abdevid", String.valueOf(this.ab.alertBaseDevID));
            map.put("membername", mb.getMemberName());
            map.put("teamname", mb.getTeamName());
            map.put("pressurevalue", String.valueOf(mb.getPressureValue()));
            map.put("remainingtime", String.valueOf(mb.getRemainingTime()));
            map.put("evasignalval", String.valueOf(mb.getEvaSignalVal()));
            map.put("signaling", PublicFunc.booleanToStr(mb.isSignal()));
            map.put("relay", PublicFunc.booleanToStr(mb.isRelay()));
            map.put("temperature", mb.getTemperature());
            map.put("height", mb.getHeightVal());
            map.put("manualmode", PublicFunc.booleanToStr(mb.isManualMode()));
            map.put("activity", PublicFunc.booleanToStr(mb.isActivity()));
            map.put("offline", PublicFunc.booleanToStr(mb.isOffline()));
            map.put("loginover", PublicFunc.booleanToStr(mb.isLoginOver()));
            map.put("agptype", String.valueOf(mb.getAgpType()));
            map.put("repeatrsonlineserial", mb.getRepeatrsOnlineSerial());
            map.put("alertver", mb.getAlertVer());
            if (mb.getDataFlag() == 86) {
                map.put("abpowerlow", "1");
            } else {
                map.put("abpowerlow", "0");
            }
            
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            map.put("lasttime", fmt.format(mb.getLastTime()));
            if (mb.getPressureValue() < 150.0D) {
                map.put("alarmpressure", "1");
            } else {
                map.put("alarmpressure", "0");
            }
            
            map.put("alarmstatic", PublicFunc.booleanToStr(mb.isAlarmStatic()));
            map.put("alarmbatteryvoltage", PublicFunc.booleanToStr(mb.isAlarmBatteryVoltage()));
            map.put("alarmmanual", PublicFunc.booleanToStr(mb.isAlarmManual()));
            map.put("alarmtemperature", PublicFunc.booleanToStr(mb.isAlarmTemperature()));
            map.put("alarmpowersupply", PublicFunc.booleanToStr(mb.isAlarmPowerSupply()));
            map.put("alarmremaining", PublicFunc.booleanToStr(mb.isAlarmRemaining()));
            map.put("lastalarmname", "");
        } catch (Throwable var6) {
            var6.printStackTrace();
        }
        
    }
    
    public String readData() {
        String rs = "";
        List<Map<String, String>> list = new ArrayList();
        System.out.println("===start read onlinemember!===");
        Iterator it = this.onlineMemberMap.keySet().iterator();
        
        while(it.hasNext()) {
            MemberBase mb = (MemberBase)this.onlineMemberMap.get(it.next());
            System.out.println("===read onlinemember onlineserialnum is:" + mb.getOnlineSerial() + "===");
            
            try {
                Map<String, String> map = new HashMap();
                this.addMap(map, mb);
                list.add(map);
                mb.setModifyFlag(0);
            } catch (Throwable var10) {
                var10.printStackTrace();
            }
        }
        
        Iterator offit = this.offlineMemberMap.keySet().iterator();
        
        while(offit.hasNext()) {
            MemberBase offmb = (MemberBase)this.offlineMemberMap.get(offit.next());
            System.out.println("===read offlinemember offlineserialnum is:" + offmb.getOnlineSerial() + "===");
            if (offmb.getModifyFlag() == 1) {
                System.out.println("===offlineserialnum " + offmb.getOnlineSerial() + " is modifyed and start add to json data===");
                
                try {
                    Map<String, String> map = new HashMap();
                    this.addMap(map, offmb);
                    list.add(map);
                    offmb.setModifyFlag(0);
                } catch (Throwable var9) {
                    var9.printStackTrace();
                }
            }
        }
        
        try {
            if (!list.isEmpty()) {
                rs = JSON.toJSONString(list);
            }
        } catch (Throwable var8) {
            var8.printStackTrace();
        }
        
        return rs;
    }
    
    public int SendAGPEvacuationData(String onlineserial) {
        if (this.ab == null) {
            return -1;
        } else if ("".equals(onlineserial)) {
            return -2;
        } else {
            byte[] bs = PublicFunc.HexsToBytes(onlineserial);
            if (bs == null) {
                return -2;
            } else {
                for(int i = 0; i < bs.length; ++i) {
                    try {
                        if (this.onlineMemberMap == null) {
                            this.ab.SendEvacuation(bs[i]);
                            System.out.println("=== no map and send evacuation data===");
                        } else {
                            MemberBase mb = (MemberBase)this.onlineMemberMap.get(PublicFunc.toHex(bs[i]));
                            if (mb != null && mb.getEvaSignalVal() == 0) {
                                this.ab.SendEvacuation(bs[i]);
                                mb.setEvaSignalVal(1);
                                mb.setModifyFlag(1);
                                System.out.println("=== send " + PublicFunc.toHex(bs[i]) + "evacuation data===");
                            }
                        }
                    } catch (Exception var6) {
                    }
                }
                
                return 0;
            }
        }
    }
    
    public int SendResetAGPEvacuationData(String onlineserial) {
        if (this.ab == null) {
            return -1;
        } else if ("".equals(onlineserial)) {
            return -2;
        } else {
            byte[] bs = PublicFunc.HexsToBytes(onlineserial);
            if (bs == null) {
                return -2;
            } else {
                for(int i = 0; i < bs.length; ++i) {
                    try {
                        if (this.onlineMemberMap == null) {
                            this.ab.SendResetEvacuation(bs[i]);
                            System.out.println("=== no map and send restevacuation data===");
                        } else {
                            MemberBase mb = (MemberBase)this.onlineMemberMap.get(PublicFunc.toHex(bs[i]));
                            if (mb != null && mb.getEvaSignalVal() == 3) {
                                this.ab.SendResetEvacuation(bs[i]);
                                mb.setEvaSignalVal(4);
                                mb.setModifyFlag(1);
                                System.out.println("=== send " + PublicFunc.toHex(bs[i]) + " restevacuation data===");
                            }
                        }
                    } catch (Exception var6) {
                    }
                }
                
                return 0;
            }
        }
    }
    
    public int SendAGPOfflineData(String onlineserial) {
        if (this.ab == null) {
            return -1;
        } else if ("".equals(onlineserial)) {
            return -2;
        } else {
            byte[] bs = PublicFunc.HexsToBytes(onlineserial);
            if (bs == null) {
                return -2;
            } else {
                for(int i = 0; i < bs.length; ++i) {
                    try {
                        this.ab.SendOffline(bs[i]);
                    } catch (Exception var5) {
                    }
                }
                
                return 0;
            }
        }
    }
    
    public int SendCloseBaseData() {
        if (this.ab == null) {
            return -1;
        } else {
            try {
                this.ab.SendAlartBaseClose();
                return 0;
            } catch (Exception var2) {
                return -9;
            }
        }
    }
    
    public int SendDeleteAGPData(String onlineserial) {
        if (this.ab == null) {
            return -1;
        } else if ("".equals(onlineserial)) {
            return -2;
        } else {
            byte[] bs = PublicFunc.HexsToBytes(onlineserial);
            if (bs == null) {
                return -2;
            } else {
                for(int i = 0; i < bs.length; ++i) {
                    try {
                        this.ab.SendAGDelete(bs[i]);
                        if (this.onlineMemberMap != null) {
                            this.onlineMemberMap.remove(PublicFunc.toHex(bs[i]));
                        }
                    } catch (Exception var5) {
                    }
                }
                
                return 0;
            }
        }
    }
    
    private void refreshFromRecvData(byte dataFlag, byte recvFlag, MemberBase smSource, MemberBase smTarget) {
        smTarget.setRecvFlag(recvFlag);
        smTarget.setDataFlag(dataFlag);
        if (smSource.isSignal()) {
            switch(dataFlag) {
                case 71:
                case 72:
                case 73:
                case 74:
                case 75:
                case 76:
                case 77:
                case 79:
                case 81:
                case 83:
                case 84:
                case 85:
                case 86:
                default:
                    break;
                case 78:
                    smTarget.setOnlineSerial(smSource.getOnlineSerial());
                    break;
                case 80:
                case 82:
                    switch(recvFlag) {
                        case 32:
                            if (smTarget.getAgpType() == 0) {
                                smTarget.setPressureValue(smSource.getPressureValue());
                                smTarget.setRemainingTime(smSource.getRemainingTime());
                                smTarget.setTemperature(smSource.getTemperature());
                                smTarget.setHeightVal(smSource.getHeightVal());
                            }
                            
                            smTarget.setAlarmMessage(smSource.getAlarmMessage());
                            smTarget.setNewHeight(smSource.getNewHeight());
                            smTarget.setLoginOver(true);
                        case 33:
                        case 37:
                        case 38:
                        case 39:
                        case 40:
                        case 41:
                        case 42:
                        default:
                            break;
                        case 34:
                            smTarget.setMemberName(smSource.getMemberName());
                            break;
                        case 35:
                            smTarget.setSerialNumber(smSource.getSerialNumber());
                            smTarget.setAlertMITTER(smSource.getAlertMITTER());
                            smTarget.setAlertVer(smSource.getAlertVer());
                            smTarget.setAbonlineid(smSource.getAbonlineid());
                            smTarget.setAlertBase(smSource.getAlertBase());
                            break;
                        case 36:
                            smTarget.setTeamName(smSource.getTeamName());
                            if (smTarget.getAgpType() == 0) {
                                if (smSource.getAgpType() == 1) {
                                    smTarget.setAgpType(1);
                                    smTarget.setMemberName("中继器");
                                } else if (smSource.getAgpType() == 2) {
                                    smTarget.setAgpType(2);
                                    smTarget.setMemberName("呼救器");
                                }
                            } else {
                                smTarget.setAgpType(smSource.getAgpType());
                            }
                            break;
                        case 43:
                            smTarget.setOffline(smSource.isOffline());
                    }
            }
            
            if (!smTarget.isSignal()) {
                smTarget.setSignal(true);
            }
            
            smTarget.setRelay(smSource.isRelay());
        } else {
            smTarget.setSignal(false);
        }
        
        smTarget.setRepeatrsOnlineSerial(smSource.getRepeatrsOnlineSerial());
        smTarget.setLastTime(Calendar.getInstance().getTime());
        smTarget.setModifyFlag(1);
    }
    
    private MemberBase getMemberBySerialNum(String serialNum) {
        Iterator it = this.onlineMemberMap.keySet().iterator();
        
        while(it.hasNext()) {
            MemberBase mb = (MemberBase)this.onlineMemberMap.get(it.next());
            if (serialNum.equals(mb.getSerialNumber())) {
                return mb;
            }
        }
        
        return null;
    }
    
    private class ReadThread extends Thread {
        public boolean stop;
        public long lastSendCycle;
        private MemberBase recvmb;
        
        private ReadThread() {
            this.stop = false;
            this.lastSendCycle = 0L;
            this.recvmb = new MemberBase();
        }
        
        @Override
        public void run() {
            while(!this.stop) {
                if (!sendfirst) {
                    try {
                        ab.SendFirst_();
                        sendfirst = true;
                        System.out.println("---send first ok !---");
                    } catch (IOException var5) {
                        var5.printStackTrace();
                        System.out.println("---send first error !---");
                    }
                }
                
                this.recvmb.clear();
                
                try {
                    if (ab == null) {
                        this.stop = true;
                        break;
                    }
                    
                    ab.ReadAlertBaseInfo_(this.recvmb, this.lastSendCycle);
                    this.lastSendCycle = this.recvmb.getLastSendCycleTime();
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
                
                PublicFunc.toHex(this.recvmb.getRecvFlag()).equalsIgnoreCase("49");
                if (!"".equals(this.recvmb.getOnlineSerial())) {
                    MemberBase mbTmp;
                    MemberBase mbTmp2;
                    if (this.recvmb.getRecvFlag() == 35 && !"".equals(this.recvmb.getSerialNumber())) {
                        mbTmp = getMemberBySerialNum(this.recvmb.getSerialNumber());
                        if (mbTmp != null && !mbTmp.getOnlineSerial().equalsIgnoreCase(this.recvmb.getOnlineSerial())) {
                            mbTmp.setOffline(true);
                            mbTmp2 = new MemberBase();
                            mbTmp2.assign(mbTmp);
                            offlineMemberMap.put(mbTmp.getOnlineSerial(), mbTmp2);
                            onlineMemberMap.remove(mbTmp.getOnlineSerial());
                        }
                    }
                    
                    mbTmp = (MemberBase) onlineMemberMap.get(this.recvmb.getOnlineSerial());
                    if (mbTmp != null) {
                        refreshFromRecvData(this.recvmb.getDataFlag(), this.recvmb.getRecvFlag(), this.recvmb, mbTmp);
                        if (mbTmp.isOffline()) {
                            try {
                                ab.SendOffline(PublicFunc.HexToByte(this.recvmb.getOnlineSerial()));
                                mbTmp2 = new MemberBase();
                                mbTmp2.assign(mbTmp);
                                offlineMemberMap.put(mbTmp2.getOnlineSerial(), mbTmp2);
                                onlineMemberMap.remove(mbTmp.getOnlineSerial());
                                System.out.println("===send " + this.recvmb.getOnlineSerial() + " offline data===");
                            } catch (IOException var4) {
                                var4.printStackTrace();
                            }
                        }
                    } else if (this.recvmb.getDataFlag() == 76) {
                        mbTmp2 = (MemberBase) offlineMemberMap.get(this.recvmb.getOnlineSerial());
                        if (mbTmp2 != null) {
                            refreshFromRecvData(this.recvmb.getDataFlag(), this.recvmb.getRecvFlag(), this.recvmb, mbTmp2);
                        }
                    } else {
                        mbTmp = new MemberBase();
                        mbTmp.clear();
                        refreshFromRecvData(this.recvmb.getDataFlag(), this.recvmb.getRecvFlag(), this.recvmb, mbTmp);
                        onlineMemberMap.put(this.recvmb.getOnlineSerial(), mbTmp);
                        offlineMemberMap.remove(this.recvmb.getOnlineSerial());
                    }
                }
                
                ab.isAlarmAlertBasePower();
                
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException var3) {
                }
            }
            
        }
        
        public void stopit() {
            this.stop = true;
        }
    }
}
