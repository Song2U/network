package kr.ac.sungkyul.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServer {
	private static final int PORT = 1000;

	public static void main(String[] args) {
		DatagramSocket socket = null;

		try {
			socket = new DatagramSocket(1000);
			while (true) {
				System.out.println("[Server] 서버가 켜졌습니다.");
				DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);

				socket.receive(receivePacket);

				String message = new String(receivePacket.getData(), 0, receivePacket.getLength(),
						StandardCharsets.UTF_8);
				System.out.println("클라이언트 요청 : " + message);

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
				String data = format.format(new Date());
				byte[] sendTime = data.getBytes(StandardCharsets.UTF_8);
				DatagramPacket sendPacket = new DatagramPacket(sendTime, sendTime.length,
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