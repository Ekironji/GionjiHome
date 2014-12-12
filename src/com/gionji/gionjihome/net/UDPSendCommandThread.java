package com.gionji.gionjihome.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import android.util.Log;

public class UDPSendCommandThread extends Thread {
	private final String TAG = "UDPSendCommandThread";
	// UDP variables
	int port = 9002;
	
	//byte[] outgoingBytes = new byte[4];
	byte[] outgoingBytes = null;
	
	DatagramSocket datagramSocket;
	DatagramPacket datagramPacket;
	InetAddress udooAddr;
	
	private boolean running = true;


	public UDPSendCommandThread(String ipIn, byte msg) {
		try {
			udooAddr = InetAddress.getByName(ipIn);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		try {
			datagramSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		outgoingBytes = new byte[1];
		outgoingBytes[0] = msg;
		
		Log.i("UDP Constructor: ", "UDP thread got this IP: " + ipIn);
	}
	
	public UDPSendCommandThread(String ipIn, int msg) {
		try {
			udooAddr = InetAddress.getByName(ipIn);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		try {
			datagramSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		outgoingBytes = new byte[4];
		outgoingBytes[0] = (byte)(msg& 0x000000ff);
		outgoingBytes[1] = (byte)((msg >> 8) & 0x000000ff);
		outgoingBytes[2] = (byte)((msg >> 16)& 0x000000ff);
		outgoingBytes[3] = (byte)((msg >> 24)& 0x000000ff);
		
		Log.i("UDP Constructor: ", "UDP thread got this IP: " + ipIn);
	}

	public void startRunning() {
		running = true;
	}

	public void stopRunning() {
		running = false;
	}

	@Override
	public void run() {
		Log.i(TAG, "start Thread");
		try {		
			for(int i=0; i<1; i++){			
				datagramPacket = new DatagramPacket(outgoingBytes, outgoingBytes.length, udooAddr, port);
				datagramSocket.send(datagramPacket);
				Thread.sleep(25);
			}
			
			datagramSocket.close();
			Log.i(TAG, "stop Thread");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (datagramSocket != null) {
				datagramSocket.close();
			}
		}
	}

}