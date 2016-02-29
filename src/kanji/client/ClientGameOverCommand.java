package kanji.client;

public class ClientGameOverCommand implements ClientCommand {
	private Client client;
	private String[] subcommands;
	
	public ClientGameOverCommand(Client c, String setupInfo) {
		client = c;
		subcommands = setupInfo.split(DELIMITER);
	}

	@Override
	public void execute() {
		if (subcommands.length > 1) {
			this.client.removeGame();
			if (subcommands[1].equals(VICTORY)) {
				System.out.println("You've won! :D");
			} else if (subcommands[1].equals(DEFEAT)) {
				System.out.println("You've lost :(");
			} else if (subcommands[1].equals(DRAW)) {
				System.out.println("It's a tie! :|");
			} else {
				client.sendCommand(FAILURE + DELIMITER + ILLEGALARGUMENT);
			}
		} else {
			client.sendCommand(FAILURE + DELIMITER + ARGUMENTSMISSING);
		}
	}

}
