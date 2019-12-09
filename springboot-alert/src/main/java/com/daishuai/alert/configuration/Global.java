package com.daishuai.alert.configuration;

import com.daishuai.alert.alert.Alert;
import com.daishuai.alert.alert.GV;
import com.daishuai.alert.service.impl.ConnectThread;
import org.springframework.beans.factory.annotation.Value;


public class Global {
//	public static String AlertbaseAddr = "127.0.0.1";
//	
//	public static int AlertbasePort = 12345;
//	
//	public static int Comm_Type = 1;
//	
//	public static int cycle_interval = 3250;
	
	@Value("${alert.base.ip:127.0.0.1}")
	public void setAlertBaseDev(String alertBaseDev) {
		GV.alertBaseDev = alertBaseDev;
	}

	@Value("${alert.base.port:12345}")
	public void setAlertBaseDev_Port(int alertBaseDev_Port) {
		GV.alertBaseDev_Port = alertBaseDev_Port;
	}

	@Value("${alert.base.type:1}")
	public void setComm_Type(int comm_Type) {
		GV.Comm_Type = comm_Type;
	}

	@Value("${alert.base.interval:3250}")
	public void setCycle_interval(int cycle_interval) {
		GV.cycle_interval = cycle_interval;
	}
	
	public static final Alert alert = new Alert();
	public static String ALERT_CFG_FILE = "/alert.cfg.xml";

	public static boolean baseConnected = false;
	private static ConnectThread connThread;

	static {
		System.out.println("===beginxxxx===");
	}

//	public static void initConfig() {
//		try {
//			InputStream stream = ConfigHelper.getResourceAsStream(ALERT_CFG_FILE);
//			SAXReader reader = new SAXReader();
//			Document doc = reader.read(stream);
//			Element ele = null;
//			ele = doc.getRootElement().element("alertbase-addr");
//			if (ele != null)
//				AlertbaseAddr = ele.getData().toString();
//			ele = doc.getRootElement().element("alertbase-port");
//			if (ele != null) {
//				AlertbasePort = Integer.valueOf(ele.getData().toString()).intValue();
//			}
//			ele = doc.getRootElement().element("comm-type");
//			if (ele != null) {
//				Comm_Type = Integer.valueOf(ele.getData().toString()).intValue();
//			}
//			ele = doc.getRootElement().element("cycle-interval");
//			if (ele != null) {
//				cycle_interval = Integer.valueOf(ele.getData().toString()).intValue();
//
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public static void init() {
		try {
//			GV.readConfig();
//			AlertbaseAddr = GV.alertBaseDev;
//			AlertbasePort = GV.alertBaseDev_Port;
//			Comm_Type = GV.Comm_Type;
//			cycle_interval = GV.cycle_interval;
			
			connThread = new ConnectThread();
			connThread.start();
			System.out.println("===init end (call connect thread)===");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void destory() {
	}
}
