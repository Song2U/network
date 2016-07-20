package kr.ac.sungkyul.network.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChatClient {

	public static void main(String[] args) throws IOException {
		// 1. 키보드 연결
		Scanner scanner = new Scanner(System.in);
		// 2. socket 생성
		Socket socket = null;
		ServerSocket serverSocket = new ServerSocket();
		// 3. 연결

		// 4. reader/writer 생성
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
				true);
		// 5. join 프로토콜
		System.out.println("닉네임 >> ");
		String nickname = scanner.nextLine();
		pw.println("join:" + nickname);
		pw.flush();

		// 6. ChatClientReceiveThread 시작
		while (true) {
			socket = serverSocket.accept();
			new ChatClientReceiveThread(socket).start();

			// 7. 키보드 입력 처리

			System.out.println(">>");
			String input = scanner.nextLine();

			// 8. quit 프로토콜 처리
			if ("quit".equals(input) == true) {
				break;
			} else {
				// 9. 메시지 처리
				System.out.println(nickname + ":" + input);
			}
		}
	}
}