package kr.ac.sungkyul.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TimeClient {
	private static final String SERVER_IP = "220.67.115.224";
	private static final int SERVER_PORT = 1000;

	public static void main(String[] args) {
		DatagramSocket socket = null;
		Scanner scanner = new Scanner(System.in);
		try {
			socket = new DatagramSocket();
			System.out.print("[Client] 서버 명령어 입력 : ");
			String message = scanner.nextLine();

			byte[] sendData = message.getBytes(StandardCharsets.UTF_8);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
					new InetSocketAddress("220.67.115.224", SERVER_PORT));

			socket.send(sendPacket);

			DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
			socket.receive(receivePacket);// block

			String data = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);
			System.out.println("[Server] : " + data);

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