package kanji.client;

public class ClientExtensionsCommand implements ClientCommand {
	private String options;
	private Client client;
	
	public ClientExtensionsCommand(Client c, String setupInfo) {
		client = c;
	}

	@Override
	public void execute() {
		options = EXTENSIONS + DELIMITER;
		if (client.getClientName() == null) {
			options = options + NEWPLAYER + DELIMITER;
		} 
		if (client.getGame() == null) {
			options = options + CHALLENGE + DELIMITER + CHAT + 
							DELIMITER + PRACTICE + DELIMITER + QUIT;
		} else {
			options = options + CHAT + DELIMITER + QUIT;
		}

		client.sendCommand(options);		
	}

}
