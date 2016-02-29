package kanji.client;

public class ClientNewPlayerAcceptedCommand implements ClientCommand {
	private Client client;
	
	public ClientNewPlayerAcceptedCommand(Client c, String setupInfo) {
		client = c;
	}

	@Override
	public void execute() {
		client.sendCommand(GETOPTIONS);
		
	}

}
