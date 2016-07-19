package kr.ac.sungkyul.network.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class EchoClient2 {
	private static final String SERVER_IP = "220.67.115.224";
	private static final int SERVER_PORT = 1000;

	public static void main(String[] args) {
		Socket socket = null;
		Scanner scanner = new Scanner(System.in);
		try { // 1. 소켓 생성
			socket = new Socket();

			// 2. 서버 연결
			InetSocketAddress serverSocketAddress = new InetSocketAddress(SERVER_IP, SERVER_PORT);
			socket.connect(serverSocketAddress);

			// 3. IOStream 받아오기 (보조스트림으로 씌우기)
			// InputStream is = socket.getInputStream();
			// OutputStream os = socket.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);

			// 5. 데이터 읽기
			while (true) {
				System.out.print(">> ");

				String data = scanner.nextLine(); // 문자열 입력을 읽어냄
				if ("exit".equals(data)) {
					System.out.println("[Client] 서버와의 연결을 종료합니다.");// exit 문자열이 들어오면 프로그램 종료
					break;
				}
				// os.write(data.getBytes("utf-8"));
				// byte[] buffer = new byte[256];
				// int readBytes = is.read(buffer); // blocking, 몇 바이트를 읽었는지 체크
				// 메시지 보내기
				pw.println(data);
				// 메세지 다시 받기
				String dataEcho = br.readLine(); // TCP는 여기에 Block되어있어 다중처리를 할 수 없음
				if (dataEcho == null) { // 서버가 연결을 끊음
					System.out.println("[Client] closed by Server");
					break;
				}
				// data = new String(buffer, 0, readBytes, "utf-8");
				
				
				System.out.println("<< " + data);
				
			}
		} catch (SocketException e) {
			System.out.println("[Client] 비정상적으로 서버로부터 연결이 끊어졌습니다.");
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