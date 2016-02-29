package kanji.server;

public class HandlerCancelCommand implements HandlerCommand {
	private ClientHandler handler;
	
	public HandlerCancelCommand(ClientHandler ch, String setupInfo) {
		handler = ch;
	}

	@Override
	public void execute() {
		if (this.handler.getName() != null && this.handler.getGame() == null) {
			if (this.handler.isWaitingForGame()) {
				this.handler.setWaitingForGame(false);
				this.handler.sendCommands(CANCELLED);
			} else if (this.handler.isChallengePending() && this.handler.getChallenger() == null) {
				this.handler.setChallengePending(false);
				for (ClientHandler ch : this.handler.getServer().handlersInLobby()) {
					if (ch.getChallenger() == this.handler) {
						ch.setChallenger(null);
						ch.setChallengePending(false);
						ch.sendCommands(CHAT + " Server: " + this.handler.getName() 
										+ " retracted their Challenge");
						break;
					}
				}
				this.handler.sendCommands(CANCELLED);
			} else {
				this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
			}
		} else {
			this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
		}
	}

}
