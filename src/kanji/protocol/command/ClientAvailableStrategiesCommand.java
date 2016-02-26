package kanji.protocol.command;

import kanji.client.Client;

public class ClientAvailableStrategiesCommand implements Command {
	private String strategies;

	public ClientAvailableStrategiesCommand(Client c, String setupInfo) {
		strategies = setupInfo.substring(setupInfo.split(DELIMITER)[0].length());
	}
	
	@Override
	public void execute() {
		System.out.println("The available strategies are: \n" + strategies);		
	}

}
