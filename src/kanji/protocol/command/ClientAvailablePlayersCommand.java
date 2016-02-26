package kanji.protocol.command;

import kanji.client.Client;

public class ClientAvailablePlayersCommand implements Command {
	private String subcommand;
	
	public ClientAvailablePlayersCommand(Client c, String setupInfo) {
		subcommand = setupInfo.substring(setupInfo.split(DELIMITER)[0].trim().length());
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		System.out.println("The following players are available: \n" + subcommand);
	}

}