package kr.ac.sungkyul.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class EchoServer {
	private static final int PORT = 1000;
	private static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {
		DatagramSocket socket = null;

		try {
			// 1. 소켓 생성
			socket = new DatagramSocket(1000);
			while (true) { // 2. 수신 대기
				System.out.println("수신 대기");
				DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);

				socket.receive(receivePacket);

				// 3. 데이터 수신
				String message = new String(receivePacket.getData(), 0, receivePacket.getLength(),
						StandardCharsets.UTF_8);
				System.out.println("데이터 수신 : " + message);

				// 4. 데이터 송신
				byte[] sendData = message.getBytes(StandardCharsets.UTF_8);
				// String.length는 글자의 수기 때문에 byte를 담는 변수를 생성해서 제대로 길이를 카운트 하도록 함
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
						new InetSocketAddress(receivePacket.getAddress(), receivePacket.getPort()));
				socket.send(sendPacket);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null && socket.isClosed() == false) {
				socket.close();
			}
		}
	}
}