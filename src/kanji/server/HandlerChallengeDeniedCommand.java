package kanji.server;

public class HandlerChallengeDeniedCommand implements HandlerCommand {
	private ClientHandler handler;
	
	public HandlerChallengeDeniedCommand(ClientHandler ch, String setupInfo) {
		handler = ch;
	}

	@Override
	public void execute() {
		if (this.handler.getName() == null || this.handler.getChallenger() != null) {
			this.handler.getChallenger().sendCommands("Your Challenge has been denied.");
			this.handler.getChallenger().setChallengePending(false);
			this.handler.setChallenger(null);
			this.handler.setChallengePending(false);
		} else {
			this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
		}
	}

}
