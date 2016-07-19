package kr.ac.sungkyul.network.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer2 {
	private final static int SERVER_PORT = 1000;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket();

			InetAddress inetAddress = InetAddress.getLocalHost();
			String serverAddress = inetAddress.getHostAddress();
			InetSocketAddress inetSocketAddress = new InetSocketAddress(serverAddress, SERVER_PORT);

			serverSocket.bind(inetSocketAddress);
			System.out.println("<EchoServer> bind : " + serverAddress + ":" + SERVER_PORT);

			Socket socket = serverSocket.accept(); // 데이터 통신하는 소켓

			InetSocketAddress remoteAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			String remoteHostAddress = remoteAddress.getAddress().getHostAddress();
			System.out.println("<EchoServer> 연결 성공, from " + remoteHostAddress + ":" + remoteAddress.getPort());
			try {
				// 5. IOStream
				/*
				 * InputStream is = socket.getInputStream(); OutputStream os =
				 * socket.getOutputStream();
				 */
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);

				// 6. 데이터 읽기
				while (true) {
					/*
					 * byte[] buffer = new byte[256]; // 메세지가 입력되는 바이트단위의 버퍼 준비
					 * int readBytes = is.read(buffer); // blocked (바이트 단위로 읽음)
					 */
					String data = br.readLine();
					if (data == null) { // 클라이언트가 연결을 끊음(정상 종료)
						System.out.println("<EchoServer> closed by client");
						return;
					}

					// 출력
					// String data = new String(buffer, 0, readBytes, "utf-8");
					System.out.println("<EchoServer> received : " + data);
					// 7. 데이터 쓰기
					// os.write(data.getBytes("utf-8")); // os.write(buffer);
					
					pw.println(data);
					
				}
			} catch (SocketException e) {
				System.out.println("<EchoServer> 비정상적으로 클라이언트가 연결을 끊었습니다.");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 8. 소켓 닫기(자원 정리)
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}