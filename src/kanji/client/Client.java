package kanji.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import kanji.protocol.ClientCommunicator;
import kanji.server.game.Game;
import kanji.server.player.HumanPlayer;
import kanji.server.player.Player;

public class Client implements Runnable {

	private String clientName;
	private Socket socket;
	private ClientCommunicator clientCommunicator;
	private BufferedReader in;
	private BufferedWriter out;
	private InetAddress inetAddress;
	private int port;
	private Game game;
	private Player player;
	private Player opponent;
	

	public Client(InetAddress inetAdd) {
		this.inetAddress = inetAdd;
		this.port = 1929;
		connect(this.inetAddress, this.port);
		this.clientCommunicator = new ClientCommunicator(this);
	}

	private void connect(InetAddress address, int toPort) {
		try {
			socket = new Socket(address, toPort);
			System.out.println("Connected to " + address);
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8")); 
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * disconnect gracefully
	 */
	public void disconnect() {
		try {
			System.out.println("Disconnected from server.");
			socket.close();
			in.close();
			out.close();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * start listening to the server, and the user
	 */
	@Override
	public void run() {	
		(new Thread(new Listener())).start();		
		(new Thread(new Commander())).start();
	}
	
	/**
	 * sends a command over the socket
	 * @param msg the command to be sent
	 */
	public void sendCommand(String msg) {
		try {
			out.write(msg + "\n");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	 /**.
	  * The inner class that listens to the socket
	  * @author joris.vandijk
	  * disconnects if the connection is broken
	  */
	private class Listener implements Runnable {

		@Override
		public void run() {
			try {
				String input = in.readLine();
				while (input != null) {
					//System.out.println("got: " + input);
					clientCommunicator.execute(input);
					if (socket.isConnected()) {
						input = in.readLine();
					}		
				}
				disconnect();
			} catch (IOException e) {
				disconnect();
				return;
			}
		}
	}
	
	/**.
	 * The inner class that listens to the user (terminal)
	 * @author joris.vandijk
	 */
	private class Commander implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					String input = readUserInput();
					clientCommunicator.sendCommand(input);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private String readUserInput() throws IOException {
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			String command = input.readLine();
			return command;
		}
		
	}
	
	
	/**
	 * @return the clientName
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * @param clientName the clientName to set
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public void startGame(int size) {
		this.game = new Game(player, opponent, size);
	}
	
	public void removeGame() {
		this.game = null;
	}
	
	public Game getGame() {
		return this.game;
	}
	
	public void setPlayer(String name) {
		this.player = new HumanPlayer(name);
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public void setOpponent(String name) {
		this.opponent = new HumanPlayer(name);
	}
	
	public Player getOpponent() {
		return this.opponent;
	}
	
	/**.
	 * displays the current board in a string format
	 */
	public void displayBoard(String board, String bcapt, String wcapt, int size) {
		String display = "\n";
		display += "   ";
		for (int h = 0; h < size; h++) {
			display += h + "  "; 
		}
		display += "\n";
		for (int i = 0; i < (size * size); i++) {
			if ((i + 1) % size == 0 && i != ((size * size) - 1)) {
				display = display + board.charAt(i) + "\n   ";
				for (int j = 0; j < (size - 1); j++) {
					display += "|  ";
				}
				display += "|\n";
			} else if (i % size == 0) {
				display = display + (i / size) + "  " + board.charAt(i) + "--";
			} else if (i != ((size * size) - 1)) {
				display = display + board.charAt(i) + "--";
			} else {
				display += board.charAt(i);
			}
		}
		display += "\n\n";
		display += "Black captives: " + bcapt + "\nWhite captives: " + wcapt + "\n";
		System.out.print(display);
	}

}
