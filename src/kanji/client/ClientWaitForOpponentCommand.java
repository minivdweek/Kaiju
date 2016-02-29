package kanji.client;

public class ClientWaitForOpponentCommand implements ClientCommand {

	public ClientWaitForOpponentCommand(Client c, String setupInfo) {

	}

	@Override
	public void execute() {
		System.out.println("No opponent ready for a game, please wait a bit.");

	}

}
