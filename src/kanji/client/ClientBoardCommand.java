package kanji.client;

public class ClientBoardCommand implements ClientCommand {
	private String command;
	private String[] subcommands;
	private Client client;
	
	public ClientBoardCommand(Client c, String setupInfo) {
		client = c;
		command = setupInfo;
		subcommands = setupInfo.split(DELIMITER);
	}

	@Override
	public void execute() {
		if (subcommands.length > 3) {
			if (client.getGame().getBoard().getStringInclCaptives().trim()
							.equals(command.substring(subcommands[0].length()).trim())) {
				client.sendCommand(GETOPTIONS);
			} else {
				int size = (int) Math.sqrt(subcommands[1].trim().length());
				client.displayBoard(subcommands[1].trim(), 
								subcommands[2].trim(), subcommands[3].trim(), size);
				client.getGame().getBoard().recreateBoardFromString(
								command.substring(subcommands[0].length()).trim());
			}
		} else {
			client.sendCommand(FAILURE + DELIMITER + ARGUMENTSMISSING);
		}
	}

}