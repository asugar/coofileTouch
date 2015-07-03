package com.twinflag.coofiletouch.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;


public class NetSocket extends Socket {
	public OutputStream m_out;
	private InputStream m_in;
	private SocketAddress m_address;

	public NetSocket() {
	}

	public int connect(String host, int port) {
		this.m_address = new InetSocketAddress(host, port);
		try {
			connect(m_address);
			this.m_out = getOutputStream();
			this.m_in = getInputStream();
			return 1;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 读取服务器发送过来的数据
	 */
	public byte[] receiveData() {
		byte[] length = new byte[4];
		byte[] bits = new byte[1];
		try {
			int result = this.m_in.read(length);
			if (result == -1) {// 掉线了
				return null;
			}
			int size = byte2Int(length);
			if (size == 0) {// 没发消息
				return bits;
			}
			int readCount = 0;
			byte[] buffers = new byte[size];
			while (readCount < size) {
				readCount += this.m_in.read(buffers, readCount, size - readCount);
			}
			return buffers;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 向服务器发送数据
	 * 
	 * @param message
	 * @return
	 */
	public boolean SendData(String message) {
		boolean success = false;
		try {
			byte[] buffer = message.getBytes("UTF-8");
			int size = buffer.length;
			byte[] array = intToByte(size);
			this.m_out.write(array);
			this.m_out.flush();
			this.m_out.write(buffer);
			this.m_out.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	/**
	 * 把整型转化为一个四个字节的数组
	 * 
	 * @param length
	 * @return byte[]
	 * */

	public static byte[] intToByte(int length) {
		byte[] array = new byte[4];
		array[3] = (byte) (0xff & length);
		array[2] = (byte) ((0xff00 & length) >> 8);
		array[1] = (byte) ((0xff0000 & length) >> 16);
		array[0] = (byte) (0xff000000 & length >> 24);
		return array;
	}

	/**
	 * 把字节数组转化为整型
	 * 
	 * @param byte[] 字节数组
	 * @return int 长度
	 * */
	public int byte2Int(byte[] bytes) {
		byte[] temp = new byte[4];
		byte b;
		int size = 0;
		for (int i = 0; i < 4; i++) {
			temp[3 - i] = bytes[i];
		}
		for (int j = 0; j < 4; j++) {
			b = temp[j];
			size += (b & 0xFF) << (8 * j);
		}
		return size;
	}

	/**
	 * 关闭Socket
	 * */
	public void Close() {
		if (m_out != null) {
			try {
				m_out.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			if (isConnected()) {
				close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}
}
