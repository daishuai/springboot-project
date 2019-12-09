package com.daishuai.alert.service;

public interface IAlertService {
	/**
	 * 接收空呼数据包
	 *
	 * @return
	 */
	String receiveAGPData();
	
	/**
	 * 发送撤离指令
	 *
	 * @param paramString
	 * @return	0成功，负数为错误
	 */
	int sendAGPEvacuationData(String paramString);
	
	/**
	 * 重置撤离指令
	 *
	 * @param paramString
	 * @return	0成功，负数为错误
	 */
	int sendResetAGPEvacuationData(String paramString);
	
	/**
	 * 发送空呼终端下线指令
	 *
	 * @param paramString
	 * @return	0成功，负数为错误
	 */
	int sendAGPOfflineData(String paramString);
	
	/**
	 * 发送删除空呼终端指令
	 *
	 * @param paramString
	 * @return	0成功，负数为错误
	 */
	int sendDeleteAGPData(String paramString);
	
	/**
	 * 发送关闭基站指令
	 *
	 * @return	0成功，负数为错误
	 */
	int sendCloseBaseData();
	
	/**
	 * 服务端断开基站连接
	 *
	 * @return	0成功，负数为错误
	 */
	int closeAlertBase();
	
	/**
	 * 使服务端连接基站
	 *
	 * @return	0成功，负数为错误
	 */
	int connectAlertBase();
}
