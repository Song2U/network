package kr.ac.sungkyul.network.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHost {

	public static void main(String[] args) {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();

			String hostname = inetAddress.getHostName();
			String hostAddress = inetAddress.getHostAddress();
			byte[] addresses = inetAddress.getAddress();

			System.out.println("Hostname	: " + hostname);
			System.out.println("HostAddress	: " + hostAddress);

			for (int i = 0; i < addresses.length; i++) {
				System.out.print(addresses[i] & 0x000000ff);
				if (i < addresses.length - 1) {
					System.out.print(".");
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}