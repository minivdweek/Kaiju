package kanji.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import kanji.protocol.ServerCommunicator;
import kanji.server.game.Game;
import kanji.server.player.ComputerPlayer;
import kanji.server.player.HumanPlayer;
import kanji.server.player.Player;

public class ClientHandler implements Runnable {
	
	private Game game;
	private Player player;
	private ClientHandler opponent;
	private ClientHandler challenger;
	private ServerCommunicator communicator;
	private Socket socket;
	private Server server;
	private Integer idnumber;
	private BufferedReader in;
	private BufferedWriter out;
	private String clientName;
	private boolean challengeable;
	private boolean canChat;
	private boolean waitingForGame;
	private boolean challengePending;
	
	public ClientHandler(Server server, Integer clientID, Socket sock) {
		this.server = server;
		this.idnumber = clientID;
		this.socket = sock;
		this.communicator = new ServerCommunicator(this);
		try {
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("connected");
	}

	
	@Override
	public void run() {
		(new Thread(new Listener())).start();
		sendCommands("GETEXTENSIONS");
	}
	
	

	public void sendCommands(String msg) {
		try {
			out.write(msg + "\n");
			out.flush();
			System.out.println("sent command:" + msg);
		} catch (IOException e) {
			server.removeHandler(this.idnumber, this);
			e.printStackTrace();
		}
	}
	
	public int[] hint() {
		ComputerPlayer cp = new ComputerPlayer("cp");
		cp.setStone(player.getStone().other().toString());
		return game.getBoard().getCoordinates(cp.determineMove(game.getBoard()));
	}
	
	
	
	public void quit() {
		if (!this.getServer().handlersInLobby().isEmpty()) {
			for (ClientHandler ch : this.getServer().handlersInLobby()) {
				ch.sendCommands("CHAT " + this.getName() + " has quit.");
			}
		}
		try {
			if (this.getGame() != null && this.getOpponent() != null) {
				opponent.sendCommands("CHAT Server: " + clientName + " forfeited the game.");
				opponent.sendCommands("GAMEOVER VICTORY");
				opponent.setGame(null);
				server.addToLobby(opponent);
				opponent.setChallengePending(false);
				opponent.setWaitingForGame(false);
				opponent.sendCommands("GETEXTENSIONS");
				opponent.setOpponent(null);
			}
			server.removeFromLobby(this);
			server.removeHandler(this.idnumber, this);
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private class Listener implements Runnable {

		public Listener() {

		}

		@Override
		public void run() {
			System.out.println("waiting for command...");
			try {
				String input = in.readLine();
				while (input != null) {
					System.out.println("got: " + input);

					communicator.execute(input);
					if (socket.isConnected()) {
						input = in.readLine();
					}
				}
				quit();
			} catch (IOException e) {
				quit();
				return;
			}
		}
	}
	
	public String getName() {
		return clientName;
	}
	
	public void setName(String name) {
		this.clientName = name;
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public Game getGame() {
		return this.game;
	}

	public void startGame(int size) {
		this.server.startGame(this, opponent, size);
	}
	
	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(String name) {
		this.player = new HumanPlayer(name);
	}
	
	public void setOpponent(ClientHandler opp) {
		this.opponent = opp;
	}
	
	public ClientHandler getOpponent() {
		return opponent;
	}
	
	public Server getServer() {
		return this.server;
	}

	/**
	 * @return the challenger
	 */
	public ClientHandler getChallenger() {
		return challenger;
	}

	/**
	 * @param challenger the challenger to set
	 */
	public void setChallenger(ClientHandler challenger) {
		this.challenger = challenger;
	}


	/**
	 * @return the challengeable
	 */
	public boolean isChallengeable() {
		return challengeable;
	}


	/**
	 * @param challengeable the challengeable to set
	 */
	public void setChallengeable(boolean challengeable) {
		this.challengeable = challengeable;
	}


	/**
	 * @return the canChat
	 */
	public boolean isCanChat() {
		return canChat;
	}


	/**
	 * @param canChat the canChat to set
	 */
	public void setCanChat(boolean canChat) {
		this.canChat = canChat;
	}


	/**
	 * @return the waitingForGame
	 */
	public boolean isWaitingForGame() {
		return waitingForGame;
	}


	/**
	 * @param waitingForGame the waitingForGame to set
	 */
	public void setWaitingForGame(boolean waitingForGame) {
		this.waitingForGame = waitingForGame;
	}


	/**
	 * @return the challengePending
	 */
	public boolean isChallengePending() {
		return challengePending;
	}


	/**
	 * @param challengePending the challengePending to set
	 */
	public void setChallengePending(boolean challengePending) {
		this.challengePending = challengePending;
	}

}
