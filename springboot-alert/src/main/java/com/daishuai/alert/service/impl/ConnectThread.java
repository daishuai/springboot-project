package com.daishuai.alert.service.impl;

import com.daishuai.alert.alert.GV;
import com.daishuai.alert.configuration.Global;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectThread extends Thread {
	public boolean stop = false;

	@Override
	public void run() {
		log.info("====> begin run ConnectThread......");
		while (!this.stop) {

			if (!Global.baseConnected && Global.alert.testConnect(GV.alertBaseDev, GV.alertBaseDev_Port)) {
				log.info("====> begin connect......");
				Global.baseConnected = Global.alert.connect(GV.alertBaseDev, GV.alertBaseDev_Port);
				log.info("====> connect status: " + Global.baseConnected);
			}

			try {
				Thread.sleep(500L);
			} catch (InterruptedException interruptedException) {
			}
		}
	}

	public void stopit() {
		this.stop = true;
	}
}