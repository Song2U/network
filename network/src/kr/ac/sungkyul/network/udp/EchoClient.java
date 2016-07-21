package kr.ac.sungkyul.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class EchoClient {
	private static final String SERVER_IP = "220.67.115.224";
	private static final int SERVER_PORT = 1000;
	private static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {
		// 0. 키보드 연결
		Scanner scanner = new Scanner(System.in);

		DatagramSocket socket = null;

		try {
			// 1. 소켓 생성
			socket = new DatagramSocket();
			while (true) { // 2. 사용자 입력 값
				String message = scanner.nextLine();

				if ("quit".equals(message)) {
					break;
				}

				// 3. 데이터 송신
				byte[] sendData = message.getBytes(StandardCharsets.UTF_8);
				DatagramPacket sendpacket = new DatagramPacket(sendData, sendData.length,
						new InetSocketAddress(SERVER_IP, SERVER_PORT));

				socket.send(sendpacket);

				// 4. 데이터 수신
				DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				socket.receive(receivePacket); // block

				String data = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);
				System.out.println("<<" + data);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
			if (socket != null && socket.isClosed() == false) {
				socket.close();
			}
		}
	}
}