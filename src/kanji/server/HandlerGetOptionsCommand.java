package kanji.server;

public class HandlerGetOptionsCommand implements HandlerCommand {
	private ClientHandler handler;
	
	public HandlerGetOptionsCommand(ClientHandler ch, String setupInfo) {
		handler = ch;
	}

	@Override
	public void execute() {
		handleGetOptions();
	}
	
	private void handleGetOptions() {
		String options = OPTIONS + DELIMITER + GETEXTENSIONS + DELIMITER + GETOPTIONS;
		if (this.handler.getName() == null) {
			options = options + DELIMITER + NEWPLAYER;
		} else {
			if (this.handler.isCanChat()) {
				for (ClientHandler ch : this.handler.getServer().handlersInLobby()) {
					if (ch != this.handler && ch.isCanChat()) {
						options = options + DELIMITER + CHAT;
						break;
					}
				}
			}
			if (this.handler.getGame() == null) {
				if (this.handler.isWaitingForGame() || this.handler.isChallengePending()) {
					options = options + DELIMITER + CANCEL;
				} else {
					if (this.handler.isChallengeable() && !this.handler.isChallengePending()) {
						for (ClientHandler ch : this.handler.getServer().handlersInLobby()) {
							if (ch.isChallengeable() && !ch.isChallengePending()) {
								options = options + DELIMITER + CHALLENGE;
								break;
							}
						}
					}
					options = options + DELIMITER + PLAY + DELIMITER + PRACTICE;
				}
			} else {
				options = options + DELIMITER + MOVE + DELIMITER + PASS + DELIMITER 
								+ GETHINT + DELIMITER + STOPGAME;	
			}
		}
		options = options + DELIMITER + QUIT;
		
		this.handler.sendCommands(options);
	}

}
