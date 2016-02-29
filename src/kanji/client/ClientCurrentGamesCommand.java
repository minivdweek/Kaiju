package kanji.client;

public class ClientCurrentGamesCommand implements ClientCommand {
	private String command;
	
	public ClientCurrentGamesCommand(Client c, String setupInfo) {
		command = setupInfo;
	}

	@Override
	public void execute() {
		if (command.split(DELIMITER).length > 1) {
			System.out.println("Games in progress: \n" + 
							command.substring(command.split(DELIMITER).length));
		} else {
			System.out.println("There are no games playing right now.");
		}
	}

}
