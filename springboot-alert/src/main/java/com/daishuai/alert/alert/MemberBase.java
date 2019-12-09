package com.daishuai.alert.alert;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Daishuai
 * @description
 * @date 2019/9/5 13:41
 */
public class MemberBase {
    private String memberName;
    private String serialNumber;
    private String alertMITTER;
    private String radioModul;
    private String radioModulVer;
    private String preferredBase;
    private String alertScoutVer;
    private String alertBase;
    private String area;
    private String teamName;
    private double pressureValue;
    private String onlineSerial;
    private Date lastTime;
    private byte recvFlag;
    private String alarmMessage;
    private int remainingTime;
    private int evaSignalVal;
    private Date taskBeginTime;
    private Date taskEndTime;
    private int taskStatus;
    private boolean signal;
    private boolean relay;
    private String temperature;
    private String heightVal;
    private Date noSignalTimeBegin;
    private int noSignalTime;
    private boolean alarmStatic;
    private String gasCylinderType;
    private boolean alarmBatteryVoltage;
    private boolean manualMode;
    private String tableID;
    private String workLocation;
    private boolean alarmManual;
    private boolean alarmTemperature;
    private boolean alarmPowerSupply;
    private boolean alarmRemaining;
    private boolean activity;
    private int noSignalCount = 0;
    private boolean offline = false;
    private boolean loginOver = false;
    private int agpType = 0;
    private long lastSendCycleTime = 0L;
    private String repeatrsOnlineSerial = "";
    private String alertVer = "";
    private String newHeight = "000000";
    private String abonlineid = "";
    private int modifyFlag = 0;
    private byte dataFlag;
    
    public MemberBase() {
    }
    
    public byte getDataFlag() {
        return this.dataFlag;
    }
    
    public void setDataFlag(byte dataFlag) {
        this.dataFlag = dataFlag;
    }
    
    public int getModifyFlag() {
        return this.modifyFlag;
    }
    
    public void setModifyFlag(int modifyFlag) {
        this.modifyFlag = modifyFlag;
    }
    
    public String getAbonlineid() {
        return this.abonlineid;
    }
    
    public void setAbonlineid(String abonlineid) {
        this.abonlineid = abonlineid;
    }
    
    public String getNewHeight() {
        return this.newHeight;
    }
    
    public void setNewHeight(String newHeight) {
        this.newHeight = newHeight;
    }
    
    public String getRepeatrsOnlineSerial() {
        return this.repeatrsOnlineSerial;
    }
    
    public void setRepeatrsOnlineSerial(String repeatrsOnlineSerial) {
        this.repeatrsOnlineSerial = repeatrsOnlineSerial;
    }
    
    public long getLastSendCycleTime() {
        return this.lastSendCycleTime;
    }
    
    public void setLastSendCycleTime(long lastSendCycleTime) {
        this.lastSendCycleTime = lastSendCycleTime;
    }
    
    public int getAgpType() {
        return this.agpType;
    }
    
    public void setAgpType(int agpType) {
        this.agpType = agpType;
    }
    
    public String getOnlineSerial() {
        return this.onlineSerial;
    }
    
    public void setOnlineSerial(String onlineSerial) {
        this.onlineSerial = onlineSerial;
    }
    
    public String getMemberName() {
        return this.memberName;
    }
    
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    
    public String getSerialNumber() {
        return this.serialNumber;
    }
    
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    public String getAlertMITTER() {
        return this.alertMITTER;
    }
    
    public void setAlertMITTER(String alertMITTER) {
        this.alertMITTER = alertMITTER;
    }
    
    public String getRadioModul() {
        return this.radioModul;
    }
    
    public void setRadioModul(String radioModul) {
        this.radioModul = radioModul;
    }
    
    public String getRadioModulVer() {
        return this.radioModulVer;
    }
    
    public void setRadioModulVer(String radioModulVer) {
        this.radioModulVer = radioModulVer;
    }
    
    public String getPreferredBase() {
        return this.preferredBase;
    }
    
    public void setPreferredBase(String preferredBase) {
        this.preferredBase = preferredBase;
    }
    
    public String getAlertScoutVer() {
        return this.alertScoutVer;
    }
    
    public void setAlertScoutVer(String alertScoutVer) {
        this.alertScoutVer = alertScoutVer;
    }
    
    public String getAlertBase() {
        return this.alertBase;
    }
    
    public void setAlertBase(String alertBase) {
        this.alertBase = alertBase;
    }
    
    public String getArea() {
        return this.area;
    }
    
    public void setArea(String area) {
        this.area = area;
    }
    
    public String getTeamName() {
        return this.teamName;
    }
    
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    
    public double getPressureValue() {
        return this.pressureValue;
    }
    
    public void setPressureValue(double pressureValue) {
        this.pressureValue = pressureValue;
    }
    
    public Date getLastTime() {
        return this.lastTime;
    }
    
    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }
    
    public byte getRecvFlag() {
        return this.recvFlag;
    }
    
    public void setRecvFlag(byte recvFlag) {
        this.recvFlag = recvFlag;
    }
    
    public String getAlarmMessage() {
        return this.alarmMessage;
    }
    
    public boolean isSignal() {
        return this.signal;
    }
    
    public void setSignal(boolean signal) {
        this.signal = signal;
    }
    
    public void setAlertVer(String alertVer) {
        this.alertVer = alertVer;
    }
    
    public String getAlertVer() {
        return this.alertVer;
    }
    
    public void setAlarmMessage(String alarmMessage) {
        this.setAlarmPowerSupply(false);
        this.alarmMessage = alarmMessage;
        String alarm = this.alarmMessage != null ? this.alarmMessage : "";
        boolean bResetEva = false;
        
        for(int i = alarm.length() - 1; i >= 0; --i) {
            char c = alarm.charAt(i);
            int j = alarm.length() - 1 - i;
            switch(j) {
                case 0:
                case 1:
                case 4:
                case 5:
                case 6:
                case 7:
                case 14:
                default:
                    break;
                case 2:
                    if (c == '1' && this.getTaskStatus() != 1) {
                        this.setTaskStatus(1);
                        this.setTaskBeginTime(Calendar.getInstance().getTime());
                    }
                    break;
                case 3:
                    if (c == '1') {
                        this.setTaskStatus(2);
                        this.setTaskEndTime(Calendar.getInstance().getTime());
                    }
                    break;
                case 8:
                    if (c == '1') {
                        this.setAlarmStatic(true);
                    } else {
                        this.setAlarmStatic(false);
                    }
                    break;
                case 9:
                    if (c == '1') {
                        this.setAlarmManual(true);
                    } else {
                        this.setAlarmManual(false);
                    }
                    break;
                case 10:
                    if (c == '1') {
                        this.setAlarmTemperature(true);
                    } else {
                        this.setAlarmTemperature(false);
                    }
                    break;
                case 11:
                    if (c == '1') {
                        this.setAlarmBatteryVoltage(true);
                    } else {
                        this.setAlarmBatteryVoltage(false);
                    }
                    break;
                case 12:
                    if (c == '1') {
                        if (this.getEvaSignalVal() < 2) {
                            this.setEvaSignalVal(2);
                            System.out.println("=== receive evacuation alarm and evasignalval is 2===");
                        }
                    } else {
                        bResetEva = true;
                    }
                    break;
                case 13:
                    if (c == '1') {
                        if (this.getEvaSignalVal() < 3) {
                            this.setEvaSignalVal(3);
                            System.out.println("=== receive evacuation ok and evasignalval is 3===");
                        }
                    } else if (bResetEva && (this.getEvaSignalVal() == 3 || this.getEvaSignalVal() == 4)) {
                        this.setEvaSignalVal(0);
                        System.out.println("=== evacuation stadus set 0===");
                    }
            }
        }
        
    }
    
    public int getRemainingTime() {
        return this.remainingTime;
    }
    
    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }
    
    public int getEvaSignalVal() {
        return this.evaSignalVal;
    }
    
    public void setEvaSignalVal(int evaSignalVal) {
        this.evaSignalVal = evaSignalVal;
    }
    
    public Date getTaskBeginTime() {
        return this.taskBeginTime;
    }
    
    public void setTaskBeginTime(Date taskBeginTime) {
        this.taskBeginTime = taskBeginTime;
    }
    
    public Date getTaskEndTime() {
        return this.taskEndTime;
    }
    
    public void setTaskEndTime(Date taskEndTime) {
        this.taskEndTime = taskEndTime;
    }
    
    public int getTaskStatus() {
        return this.taskStatus;
    }
    
    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }
    
    public boolean isRelay() {
        return this.relay;
    }
    
    public void setRelay(boolean relay) {
        this.relay = relay;
    }
    
    public String getTemperature() {
        return this.temperature;
    }
    
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    
    public String getHeightVal() {
        return this.heightVal;
    }
    
    public void setHeightVal(String heightVal) {
        this.heightVal = heightVal;
    }
    
    public Date getNoSignalTimeBegin() {
        return this.noSignalTimeBegin;
    }
    
    public void setNoSignalTimeBegin(Date noSignalTimeBegin) {
        this.noSignalTimeBegin = noSignalTimeBegin;
    }
    
    public int getNoSignalTime() {
        return this.noSignalTime;
    }
    
    public void setNoSignalTime(int noSignalTime) {
        this.noSignalTime = noSignalTime;
    }
    
    public boolean isAlarmStatic() {
        return this.alarmStatic;
    }
    
    public void setAlarmStatic(boolean alarmStatic) {
        this.alarmStatic = alarmStatic;
    }
    
    public String getGasCylinderType() {
        return this.gasCylinderType;
    }
    
    public void setGasCylinderType(String gasCylinderType) {
        this.gasCylinderType = gasCylinderType;
    }
    
    public boolean isAlarmBatteryVoltage() {
        return this.alarmBatteryVoltage;
    }
    
    public void setAlarmBatteryVoltage(boolean alarmBatteryVoltage) {
        this.alarmBatteryVoltage = alarmBatteryVoltage;
    }
    
    public boolean isManualMode() {
        return this.manualMode;
    }
    
    public void setManualMode(boolean manualMode) {
        this.manualMode = manualMode;
    }
    
    public String getTableID() {
        return this.tableID;
    }
    
    public void setTableID(String tableID) {
        this.tableID = tableID;
    }
    
    public String getWorkLocation() {
        return this.workLocation;
    }
    
    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }
    
    public boolean isAlarmManual() {
        return this.alarmManual;
    }
    
    public void setAlarmManual(boolean alarmManual) {
        this.alarmManual = alarmManual;
    }
    
    public boolean isAlarmTemperature() {
        return this.alarmTemperature;
    }
    
    public void setAlarmTemperature(boolean alarmTemperature) {
        this.alarmTemperature = alarmTemperature;
    }
    
    public boolean isAlarmPowerSupply() {
        return this.alarmPowerSupply;
    }
    
    public void setAlarmPowerSupply(boolean alarmPowerSupply) {
        this.alarmPowerSupply = alarmPowerSupply;
    }
    
    public boolean isAlarmRemaining() {
        return this.alarmRemaining;
    }
    
    public void setAlarmRemaining(boolean alarmRemaining) {
        this.alarmRemaining = alarmRemaining;
    }
    
    public boolean isActivity() {
        this.activity = false;
        if (this.isOffline()) {
            return false;
        } else {
            if (this.isManualMode()) {
                if (this.getTaskStatus() == 1) {
                    this.activity = true;
                }
            } else {
                this.activity = this.isSignal();
            }
            
            return this.activity;
        }
    }
    
    public void setActivity(boolean activity) {
        this.activity = activity;
    }
    
    public int getNoSignalCount() {
        return this.noSignalCount;
    }
    
    public void setNoSignalCount(int noSignalCount) {
        this.noSignalCount = noSignalCount;
    }
    
    public boolean isOffline() {
        return this.offline;
    }
    
    public void setOffline(boolean offline) {
        this.offline = offline;
    }
    
    public boolean isLoginOver() {
        return this.loginOver;
    }
    
    public void setLoginOver(boolean loginOver) {
        this.loginOver = loginOver;
    }
    
    public void assign(MemberBase mb) {
        if (mb != null) {
            this.memberName = mb.getMemberName();
            this.serialNumber = mb.getSerialNumber();
            this.alertMITTER = mb.getAlertMITTER();
            this.radioModul = mb.getRadioModul();
            this.radioModulVer = mb.getRadioModulVer();
            this.preferredBase = mb.getPreferredBase();
            this.alertScoutVer = mb.getAlertScoutVer();
            this.alertBase = mb.getAlertBase();
            this.area = mb.getArea();
            this.teamName = mb.getTeamName();
            this.pressureValue = mb.getPressureValue();
            this.onlineSerial = mb.getOnlineSerial();
            this.lastTime = mb.getLastTime();
            this.remainingTime = mb.getRemainingTime();
            this.evaSignalVal = mb.getEvaSignalVal();
            this.taskBeginTime = mb.getTaskBeginTime();
            this.taskEndTime = mb.getTaskEndTime();
            this.taskStatus = mb.getTaskStatus();
            this.signal = mb.isSignal();
            this.relay = mb.isRelay();
            this.temperature = mb.getTemperature();
            this.heightVal = mb.getHeightVal();
            this.noSignalTime = mb.getNoSignalTime();
            this.noSignalTimeBegin = mb.getNoSignalTimeBegin();
            this.gasCylinderType = mb.getGasCylinderType();
            this.alarmBatteryVoltage = mb.isAlarmBatteryVoltage();
            this.alarmStatic = mb.isAlarmStatic();
            this.manualMode = mb.isManualMode();
            this.tableID = mb.getTableID();
            this.workLocation = mb.getWorkLocation();
            this.alarmManual = mb.isAlarmManual();
            this.alarmTemperature = mb.isAlarmTemperature();
            this.alarmPowerSupply = mb.isAlarmPowerSupply();
            this.alarmRemaining = mb.isAlarmRemaining();
            this.alarmMessage = mb.getAlarmMessage();
            this.activity = mb.isActivity();
            this.noSignalCount = mb.getNoSignalCount();
            this.offline = mb.isOffline();
            this.loginOver = mb.isLoginOver();
            this.agpType = mb.getAgpType();
            this.lastSendCycleTime = mb.getLastSendCycleTime();
            this.repeatrsOnlineSerial = mb.getRepeatrsOnlineSerial();
            this.alertVer = mb.getAlertVer();
            this.newHeight = mb.getNewHeight();
            this.abonlineid = mb.getAbonlineid();
            this.modifyFlag = mb.getModifyFlag();
            this.dataFlag = mb.getDataFlag();
        }
    }
    
    public void assignSet(MemberBase mb) {
        if (mb != null) {
            this.setMemberName(mb.getMemberName());
            this.setSerialNumber(mb.getSerialNumber());
            this.setAlertMITTER(mb.getAlertMITTER());
            this.setRadioModul(mb.getRadioModul());
            this.setRadioModulVer(mb.getRadioModulVer());
            this.setPreferredBase(mb.getPreferredBase());
            this.setAlertScoutVer(mb.getAlertScoutVer());
            this.setAlertBase(mb.getAlertBase());
            this.setArea(mb.getArea());
            this.setTeamName(mb.getTeamName());
            this.setPressureValue(mb.getPressureValue());
            this.setOnlineSerial(mb.getOnlineSerial());
            this.setLastTime(mb.getLastTime());
            this.setRemainingTime(mb.getRemainingTime());
            this.setEvaSignalVal(mb.getEvaSignalVal());
            this.setTaskBeginTime(mb.getTaskBeginTime());
            this.setTaskEndTime(mb.getTaskEndTime());
            this.setTaskStatus(mb.getTaskStatus());
            this.setSignal(mb.isSignal());
            this.setRelay(mb.isRelay());
            this.setTemperature(mb.getTemperature());
            this.setHeightVal(mb.getHeightVal());
            this.setNoSignalTime(mb.getNoSignalTime());
            this.setNoSignalTimeBegin(mb.getNoSignalTimeBegin());
            this.setGasCylinderType(mb.getGasCylinderType());
            this.setAlarmBatteryVoltage(mb.isAlarmBatteryVoltage());
            this.setAlarmStatic(mb.isAlarmStatic());
            this.setManualMode(mb.isManualMode());
            this.setTableID(mb.getTableID());
            this.setWorkLocation(mb.getWorkLocation());
            this.setAlarmManual(mb.isAlarmManual());
            this.setAlarmTemperature(mb.isAlarmTemperature());
            this.setAlarmPowerSupply(mb.isAlarmPowerSupply());
            this.setAlarmRemaining(mb.isAlarmRemaining());
            this.setAlarmMessage(mb.getAlarmMessage());
            this.setActivity(mb.isActivity());
            this.setNoSignalCount(mb.getNoSignalCount());
            this.setOffline(mb.isOffline());
            this.setLoginOver(mb.isLoginOver());
            this.setAgpType(mb.getAgpType());
            this.setLastSendCycleTime(mb.getLastSendCycleTime());
            this.setRepeatrsOnlineSerial(mb.getRepeatrsOnlineSerial());
            this.setAlertVer(mb.getAlertVer());
            this.setNewHeight(mb.getNewHeight());
            this.setAbonlineid(mb.getAbonlineid());
            this.setModifyFlag(mb.getModifyFlag());
            this.setDataFlag(mb.getDataFlag());
        }
    }
    
    public void clear() {
        this.memberName = "";
        this.serialNumber = "";
        this.alertMITTER = "";
        this.radioModul = "";
        this.radioModulVer = "";
        this.preferredBase = "";
        this.alertScoutVer = "";
        this.alertBase = "";
        this.area = "";
        this.teamName = "";
        this.pressureValue = 0.0D;
        this.onlineSerial = "";
        this.lastTime = Calendar.getInstance().getTime();
        this.recvFlag = 0;
        this.alarmMessage = "";
        this.remainingTime = 0;
        this.evaSignalVal = 0;
        this.taskStatus = 0;
        this.taskBeginTime = null;
        this.taskEndTime = null;
        this.signal = false;
        this.relay = false;
        this.temperature = "";
        this.heightVal = "";
        this.noSignalTime = 0;
        this.noSignalTimeBegin = this.lastTime;
        this.gasCylinderType = "";
        this.alarmBatteryVoltage = false;
        this.alarmStatic = false;
        this.manualMode = false;
        this.tableID = "";
        this.workLocation = "";
        this.alarmManual = false;
        this.alarmTemperature = false;
        this.alarmPowerSupply = false;
        this.alarmRemaining = false;
        this.activity = false;
        this.noSignalCount = 0;
        this.offline = false;
        this.loginOver = false;
        this.agpType = 0;
        this.lastSendCycleTime = 0L;
        this.repeatrsOnlineSerial = "";
        this.alertVer = "";
        this.newHeight = "";
        this.abonlineid = "";
        this.modifyFlag = 0;
        this.dataFlag = 0;
    }
    
    public boolean isAlarming() {
        boolean rs = false;
        if (!this.isLoginOver()) {
            return false;
        } else {
            boolean b1 = this.getPressureValue() <= 150.0D;
            if (this.isAlarmStatic() || this.isAlarmBatteryVoltage() || this.isAlarmManual() || this.isAlarmTemperature() || this.isAlarmPowerSupply() || this.isAlarmRemaining() || b1) {
                rs = true;
            }
            
            return rs;
        }
    }
    
    public static void main(String[] args) {
    }
}
