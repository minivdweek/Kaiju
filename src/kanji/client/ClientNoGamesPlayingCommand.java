package kanji.client;

public class ClientNoGamesPlayingCommand implements ClientCommand {
	
	public ClientNoGamesPlayingCommand(Client c, String setupInfo) {
		
	}
	@Override
	public void execute() {
		System.out.println("There are no games playing right now.");
	}

}
