package kr.ac.sungkyul.network.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChatServerThread extends Thread {
	private String nickname;
	private Socket socket;
	private List<Writer> listWriters;

	public ChatServerThread(Socket socket, List<Writer> listWriters) throws IOException {
		// 1. Remote Host Information
		this.socket = socket;
		this.listWriters = listWriters;

		// 2. 스트림 얻기
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
				true);

		// 3. 요청 처리
		while (true) {
			String request = br.readLine();
			if (request == null) {
				ChatServer.log("클라이언트로부터 연결 끊김");
				doQuit(pw);
				break;
			}

			// 4. 프로토콜 분석
			String[] tokens = request.split(":");
			if ("join".equals(tokens[0])) {
				doJoin(tokens[1], pw);
			} else if ("message".equals(tokens[0])) {
				doMessage(tokens[1]);
			} else if ("quit".equals(tokens[0])) {
				doQuit(pw);
			} else {
				ChatServer.log("에러 : 알 수 없는 요청 (" + tokens[0] + ")");
			}
		}

	}

	private void doJoin(String nickName, Writer writer) {
		this.nickname = nickName;
		String data = nickName + " 님이 참여하셨습니다.";
		broadCast(data);
		
		/* writer pool에 저장 */
		addWriter(writer);

		// ack
		PrintWriter printWriter = (PrintWriter) writer;
		printWriter.println("join:ok");
		printWriter.flush();
	}

	private void addWriter(Writer writer) {
		synchronized (listWriters) {
			listWriters.add(writer);
		}
	}

	private void broadCast(String data) {
		synchronized (listWriters) {
			for (Writer writer : listWriters) {
				PrintWriter printWriter = (PrintWriter) writer;
				printWriter.println(data);
				//printWriter.flush();
				return;
			}
		}
	}

	private void doMessage(String message) {
		/* 잘 구현 해 보기 */
		String data = nickname + ":" + message;
		broadCast(data);
	}

	private void doQuit(Writer writer) {
		removeWriter(writer);

		String data = nickname + "님이 퇴장하셨습니다.";
		broadCast(data);
	}

	private void removeWriter(Writer writer) {
		/* 잘 구현 해보기 */
		synchronized (listWriters) {
			listWriters.remove(nickname);			
		}
	}

	@Override
	public void run() {
		/* reader를 통해 읽은 데이터 콘솔에 출력하기 (message 처리) */
		InetSocketAddress remoteAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
		String remoteHostAddress = remoteAddress.getAddress().getHostAddress();
		int remoteHostPort = remoteAddress.getPort();
		System.out.println("연결 from " + getId());

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);

			while (true) {
				String data = br.readLine();
				if (data == null) {
					System.out.println("클라이언트에 의해 정상종료 되었습니다.");
					return;
				}
				System.out.println(" received : " + data);
				pw.println(data);
			}
		} catch (SocketException e) {
			System.out.println("클라이언트의 연결이 비정상적으로 끊겼습니다." + e);
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