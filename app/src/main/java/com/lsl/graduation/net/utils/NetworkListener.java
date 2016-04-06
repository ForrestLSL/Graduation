package com.lsl.graduation.net.utils;

import android.net.NetworkInfo;

/**
 * 网络环境监听
 * @author jialg
 *
 */
public interface NetworkListener {

	/**
	 * 连接wifi时
	 *  Wifi网络连接的相关信息
	 */
	void onWifiConnected(NetworkInfo networkInfo);
	/**
	 * 连接手机数据网络时
	 * 手机网络连接的相关信息
	 */
	void onMobileConnected(NetworkInfo networkInfo);
	
	/**
	 * 断开网络连接时
	 *  断开网络的网络信息
	 */
	void onDisconnected(NetworkInfo networkInfo);
}
