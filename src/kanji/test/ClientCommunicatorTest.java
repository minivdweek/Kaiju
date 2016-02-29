package kanji.test;

import static org.junit.Assert.*;

import java.net.InetAddress;

import org.junit.Before;
import org.junit.Test;

import kanji.client.Client;
import kanji.client.ClientCommunicator;
import kanji.server.Server;

public class ClientCommunicatorTest {
	private Client client;
	private ClientCommunicator cc;
	private Server server;

	@Before
	public void setUp() throws Exception {
		server = new Server();
		(new Thread(server)).start();
		client = new Client(InetAddress.getLocalHost());
		(new Thread(client)).start();
		cc = new ClientCommunicator(client);
	}

	@Test
	public void testNewPLayer() {
		cc.sendCommand("NEWPLAYER Joris");
		assertEquals("Joris", client.getClientName());
		
	}
	
	@Test
	public void testGamestart() {
		cc.sendCommand("NEWPLAYER Joris");
		cc.execute("GAMESTART Henk 9 BLACK");
		assertEquals("Henk", client.getOpponent().getName());
		assertEquals(client.getPlayer(), client.getGame().getCurrentPlayer());
	}
	
//	@Test
//	public void testPractice() {
//		cc.sendCommand("NEWPLAYER Joris");
//		cc.sendCommand("PR");
//		assertEquals("Computer", client.getOpponent().getName());
//	}

}
