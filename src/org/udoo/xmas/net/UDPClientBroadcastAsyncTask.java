/**
*  Copyright (C) 2014 Ekironji <ekironjisolutions@gmail.com>
*
*  This file is part of UdooLights
*
*  UdooLights is free software: you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation, either version 3 of the License, or
*  (at your option) any later version.
*
*  UdooLights is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
*/
package org.udoo.xmas.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

public class UDPClientBroadcastAsyncTask extends AsyncTask<Void, Void, String> {
	private final String TAG = "UdpClientBroadcastAsyncTask";
	
	Context appContext;
	
	// UDP variables
	int port = 9002;
	byte[] outgoingBytes = new byte[4];
	
	DatagramSocket datagramSocket;
	InetAddress serverAddr;
	private static final int TIMEOUT_MS = 4000;
	
	private static final String GIONJIHOME_LABEL = "gionjiHome";
	private static final String MAC_SEPARATOR = "#";
	private static final String IP_SEPARATOR  = "@";
	
	ProgressDialog dialog;
	String progressDialogMessage = "Wait until server is found...";
	private boolean running = true;
	IPAddressServerListener ipAddressServerListener;

	/*
	 * UdpClientBroadcastAsyncTask costructor params Context context: the
	 * context of the Activity
	 */
	public UDPClientBroadcastAsyncTask(Context context) {
		this.appContext = context;
		dialog = new ProgressDialog(appContext);
		try {
			serverAddr = getBroadcastAddress(appContext);
			datagramSocket = new DatagramSocket();
			datagramSocket.setBroadcast(true);
			datagramSocket.setSoTimeout(TIMEOUT_MS);
			// TODO: prende messaggio gi� formattato da inviare in broadcast da
			// libreria
			outgoingBytes = "ciao".getBytes();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void onPreExecute() {
		showProgressDialog();
	}

	protected String doInBackground(Void... params) {
		running = true;
		int count = 0;
		try {
			while (running && count < 4) {
				Log.i(TAG, "datagramsocket: " + datagramSocket.toString());
				
				datagramSocket.setSoTimeout(TIMEOUT_MS);
				DatagramPacket datagramSendPacket = new DatagramPacket(
						outgoingBytes, outgoingBytes.length, serverAddr, port);
				
				datagramSocket.send(datagramSendPacket);
				Thread.sleep(30);
				datagramSocket.send(datagramSendPacket);
				Thread.sleep(30);
				datagramSocket.send(datagramSendPacket);
				Thread.sleep(30);
				try {
					while (running) {
						byte[] buf = new byte[64];
						DatagramPacket datagramReceivePacket = new DatagramPacket(
								buf, buf.length);
						
						datagramSocket.receive(datagramReceivePacket);
						
						String s = new String(datagramReceivePacket.getData(),
								0, datagramReceivePacket.getLength());
						
						Log.i(TAG, "Received response " + s);
						
						if (validateIPstring(s)) {
							running = false;
							return s;
						}
					}
				} catch (SocketTimeoutException e) {
					Log.w(TAG, "Receive timed out");
				}
				count++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (datagramSocket != null) {
				datagramSocket.close();
			}
		}
		Log.i(TAG, "stop Thread");
		datagramSocket.close();
		return null;
	}

	protected void onPostExecute(String address) {
		hideProgressDialog();
		if (datagramSocket != null) {
			datagramSocket.close();
		}
		if (address != null) {
			ipAddressServerListener.IPAddressServerFounded(address);
		} else {
			ipAddressServerListener.IPAddressServerFailed();
		}
	}

	
	private InetAddress getBroadcastAddress(Context context) throws IOException {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		// handle null somehow
		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	}

	private boolean validateIPstring(final String response) {
		
//		if(!response.contains(GIONJIHOME_LABEL))
//			return false;
		
		Log.i("UDPClientBroad", "Risposta al ciao: " + response);
		
		String ip = response;
//		try {
//			ip = response.split("@")[1];
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
		
		Pattern pattern = Pattern
				.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
						+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
						+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
						+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
		Matcher matcher = pattern.matcher(ip);
				
		return matcher.matches();
	}

	
	/*
	 * setProgressDialogMessage params: String message: the message of the
	 * progress dialog
	 */
	public void setProgressDialogMessage(String message) {
		this.progressDialogMessage = message;
	}

	public void showProgressDialog() {
		dialog.setMessage(progressDialogMessage);
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.show();
	}

	public void hideProgressDialog() {
		dialog.dismiss();
	}

	public static interface IPAddressServerListener {		
		void IPAddressServerFounded(String address);
		void IPAddressServerFailed();
	}
	
	public void setIPAddressServerListener(
			IPAddressServerListener ipAddressServerListener) {
		this.ipAddressServerListener = ipAddressServerListener;
	}

	
}