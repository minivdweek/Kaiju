package kanji.client;

public class ClientAvailableStrategiesCommand implements ClientCommand {
	private String strategies;

	public ClientAvailableStrategiesCommand(Client c, String setupInfo) {
		strategies = setupInfo.substring(setupInfo.split(DELIMITER)[0].length());
	}
	
	@Override
	public void execute() {
		System.out.println("The available strategies are: \n" + strategies);		
	}

}
