package com.daishuai.alert.service.impl;

import com.daishuai.alert.alert.GV;
import com.daishuai.alert.configuration.Global;
import com.daishuai.alert.service.IAlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlertServiceImpl implements IAlertService {
	@Override
	public String receiveAGPData() {
		return Global.alert.readData();
	}

	@Override
	public int sendAGPEvacuationData(String onlineserial) {
		int rs = -10;
		if (Global.baseConnected) {
			rs = Global.alert.SendAGPEvacuationData(onlineserial);
		} else {
			rs = -10;
		}
		return rs;
	}

	@Override
	public int sendResetAGPEvacuationData(String onlineserial) {
		int rs = -10;
		if (Global.baseConnected) {
			rs = Global.alert.SendResetAGPEvacuationData(onlineserial);
		} else {
			rs = -10;
		}
		return rs;
	}

	@Override
	public int sendAGPOfflineData(String onlineserial) {
		int rs = -10;
		if (Global.baseConnected) {
			rs = Global.alert.SendAGPOfflineData(onlineserial);
		} else {
			rs = -10;
		}
		return rs;
	}

	@Override
	public int sendDeleteAGPData(String onlineserial) {
		int rs = -10;
		if (Global.baseConnected) {
			rs = Global.alert.SendDeleteAGPData(onlineserial);
		} else {
			rs = -10;
		}
		return rs;
	}

	@Override
	public int sendCloseBaseData() {
		int rs = -10;
		if (Global.baseConnected) {
			rs = Global.alert.SendCloseBaseData();
		} else {
			rs = -10;
		}
		return rs;
	}

	@Override
	public int closeAlertBase() {
		Global.alert.disconnect();
		Global.baseConnected = false;
		return 0;
	}

	@Override
	public int connectAlertBase() {
		log.info("=======connect: " + GV.alertBaseDev + " " + GV.alertBaseDev_Port + "========");
		boolean b = false;
		try {
			b = Global.alert.connect(GV.alertBaseDev, GV.alertBaseDev_Port);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		Global.baseConnected = b;
		if (b) {
			return 0;
		}
		return -1;
	}
}
