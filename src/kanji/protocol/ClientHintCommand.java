package kanji.protocol;

import kanji.client.Client;

public class ClientHintCommand implements Command {
	private String[] subcommands;
	private Client client;

	public ClientHintCommand(Client c, String setupInfo) {
		subcommands = setupInfo.split(DELIMITER);
		client = c;
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		if (subcommands.length >= 3) {
			String row = subcommands[1].trim();
			String col = subcommands[2].trim();
			System.out.printf("There's an open spot on row: %s, column: %s\n", row, col);
		} else {
			client.sendCommand(FAILURE + DELIMITER + ARGUMENTSMISSING);
		}
	}

}
