package kanji.server;

public class HandlerGetExtensionsCommand implements HandlerCommand {
	private ClientHandler handler;
	
	public HandlerGetExtensionsCommand(ClientHandler ch, String setupInfo) {
		handler = ch;
	}

	@Override
	public void execute() {
		String extensions = EXTENSIONS + DELIMITER + PLAY + DELIMITER + PRACTICE;
		if (this.handler.isCanChat()) {
			if (handler.getOpponent() != null) {
				if (handler.getOpponent().isCanChat()) {
					extensions = extensions + DELIMITER + CHAT;
				}
			} else {
				for (ClientHandler ch : this.handler.getServer().handlersInLobby()) {
					if (ch.isCanChat()) {
						extensions = extensions + DELIMITER + CHAT;
						break;
					}
				}
			}
		}
		if (this.handler.isChallengeable()) {
			for (ClientHandler ch : this.handler.getServer().handlersInLobby()) {
				if (this.handler != ch && ch.isChallengeable()) {
					extensions = extensions + DELIMITER + CHALLENGE;
					break;
				}
			}
		}
		
		this.handler.sendCommands(extensions);
	}

}
