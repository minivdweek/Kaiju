package kanji.server;

public class HandlerPracticeCommand implements HandlerCommand {
	private ClientHandler handler;

	public HandlerPracticeCommand(ClientHandler ch, String setupInfo) {
		handler = ch;
	}

	@Override
	public void execute() {
		if (this.handler.getName() == null || this.handler.isChallengePending() 
						|| this.handler.isWaitingForGame()) {
			this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
		} else {
			this.handler.setPlayer(this.handler.getName());
			this.handler.getServer().practice(handler, 9);
			this.handler.sendCommands(GAMESTART + DELIMITER + COMPUTER + DELIMITER 
							+ "9" + DELIMITER + BLACK);
			System.out.printf("Game Started, %s and %s \n", this.handler.getName(), COMPUTER);
		}
	}

}
