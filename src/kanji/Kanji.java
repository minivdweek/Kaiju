package kanji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import kanji.client.Client;
import kanji.server.Server;

public class Kanji {
	
	public static void main(String[] args) {
		String choice = null;
		try {
			do {
				System.out.println("Do you want to start a Server or a Client?");
				BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
				choice = input.readLine();
			} while (choice == null || (!choice.equalsIgnoreCase("Server") && 
							!choice.equalsIgnoreCase("Client")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (choice.equalsIgnoreCase("server")) {
			(new Kanji()).startServer();
		} else if (choice.equalsIgnoreCase("client")) {
			(new Kanji()).startClient();
		}
	}
	
	public void startServer() {
    	(new Thread(new Server())).start();
    }
	
	public void startClient() {
		InetAddress ia;
		try {
			System.out.println("please enter the ip address of the server you want to connect to:");
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			String address = input.readLine();
			if (address == "") {
				ia = InetAddress.getLocalHost();
			} else {
				ia = InetAddress.getByName(address);
			}
			(new Thread(new Client(ia))).start();
		} catch (UnknownHostException e) {
			System.out.println("Host unknown.");
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
	}

}
