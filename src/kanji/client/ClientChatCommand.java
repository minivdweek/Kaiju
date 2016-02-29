package kanji.client;

public class ClientChatCommand implements ClientCommand {
	private Client client;
	private String command;
	private String[] subcommands;
	
	public ClientChatCommand(Client c, String setupInfo) {
		client = c;
		command = setupInfo;
		subcommands = setupInfo.split(DELIMITER);
	}

	@Override
	public void execute() {
		if (subcommands[0].trim().equals(CHAT)) {
			if (command.trim().length() > subcommands[0].trim().length()) {
				System.out.println(command.substring(subcommands[0].length()).trim());
			} else { 
				client.sendCommand(FAILURE + DELIMITER + ARGUMENTSMISSING);
			}
		} else {
			System.out.println(command);
		}
	}

}
