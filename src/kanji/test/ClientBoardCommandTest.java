package kanji.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import kanji.client.Client;
import kanji.client.ClientAvailablePlayersCommand;
import kanji.client.ClientCommand;

public class ClientBoardCommandTest {
	private Client client;
	private ClientCommand testCommand;

	@Before
	public void setUp() throws Exception {
		client = new Client(null);
		client.startGame(2, true);
	}

	@Test
	public void testExecute() {
		assertEquals(client.getGame().getBoard().getStringInclCaptives(), "EEEE 0 0");
		String testBoard = "EEEB 0 0";
		testCommand = new ClientAvailablePlayersCommand(client, "BOARD " + testBoard);
		testCommand.execute();
		assertEquals(client.getGame().getBoard().getStringInclCaptives(), testBoard);
	}

}
