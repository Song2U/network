package kr.ac.sungkyul.network.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class TCPClient {
	private static final String SERVER_IP = "220.67.115.224";
	private static final int SERVER_PORT = 1000;

	public static void main(String[] args) {
		Socket socket = null;
		try { // 1. 소켓 생성
			socket = new Socket();

			// 2. 서버 연결
			InetSocketAddress serverSocketAddress = new InetSocketAddress(SERVER_IP, SERVER_PORT);
			socket.connect(serverSocketAddress);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}