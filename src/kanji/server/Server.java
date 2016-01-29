package kanji.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import kanji.server.game.Game;
import kanji.server.player.ComputerPlayer;

public class Server implements Runnable {
	
	private final Integer port = 1929;
    private Map<Integer, ClientHandler> threads;
    private List<ClientHandler> lobby;    
    private Map<Game, String> gamesInProgress;
    private ServerSocket servSock;
    private Socket socket;
    private static Integer clientNumber;
    private Lock lock;
    
    public Server() {
    	try {
    		String address = InetAddress.getLocalHost().getHostAddress();
    		System.out.println("Starting a socket. \n On adress: " 
    						+ address + "\nOn port: " + port);
    		servSock = new ServerSocket(this.port, 5);
    	} catch (IOException e) {
    		e.printStackTrace();
    		return;
    	}
    	threads = new HashMap<Integer, ClientHandler>();
        lobby = new ArrayList<ClientHandler>();
        gamesInProgress = new HashMap<Game, String>();
        lock = new ReentrantLock();
    }
    
    
    public void run() {
    	while (true) {
    		try {
    			socket = servSock.accept();
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		ClientHandler handler = new ClientHandler(this, clientNumber, socket);
    		threads.put(clientNumber, handler);
    		clientNumber = threads.size();
    		(new Thread(handler)).start();
    	}

    }
    
	public void removeHandler(Integer client, ClientHandler ch) {
		if (lobby.contains(ch)) {
			lobby.remove(ch);
		}
		this.threads.remove(client, ch);
		clientNumber = threads.size();
	}
	
	public void closeServer() {
		System.exit(0);
	}
	
	public void addToLobby(ClientHandler handler) {
		lock.lock();
		try {
			if (!this.lobby.contains(handler)) {
				this.lobby.add(handler);
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void removeFromLobby(ClientHandler handler) {
		lock.lock();
		try {
			this.lobby.remove(handler);
			List<ClientHandler> newlist = new ArrayList<ClientHandler>();
			for (ClientHandler ch : this.lobby) {
				if (ch != null) {
					newlist.add(ch);
				}
			}
			this.lobby = newlist;
		} finally {
			lock.unlock();
		}
	}
	
	public List<ClientHandler> handlersInLobby() {
		return this.lobby;
	}
	
	
	public void removeGameFromList(Game game) {
		lock.lock();
		try {
			this.gamesInProgress.remove(game, gamesInProgress.get(game));
		} finally {
			lock.unlock();
		}
	}
	
	public Map<Game, String> getGamesInProgress() {
		return this.gamesInProgress;
	}
	
	public void startGame(ClientHandler p1, ClientHandler p2, int size) {
		Game game = new Game(p1.getPlayer(), p2.getPlayer(), size);
		p1.setGame(game);
		p2.setGame(game);
		gamesInProgress.put(game, p1.getName() + " vs " + p2.getName());
	}
	
	public void practice(ClientHandler p1, int size) {
		ComputerPlayer cp = new ComputerPlayer("Computer");
		cp.setHandler(p1);
		Game game = new Game(p1.getPlayer(), cp, size);
		cp.setObservable(game);
		System.out.println("The amount of observers is: " + game.countObservers());
		p1.setGame(game);
		gamesInProgress.put(game, p1.getName() + " vs Computer");
	}
	
	public List<String> namesTaken() {
		List<String> result = new ArrayList<String>();
		for (ClientHandler ch : this.threads.values()) {
			result.add(ch.getName());
		}
		return result;	
	}
	
	public int getThreadsCount() {
		return threads.size();
	}
}
