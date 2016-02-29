package kanji.server;

public class HandlerChatCommand implements HandlerCommand {
	private ClientHandler handler;
	private String input;
	private String[] commands;

	public HandlerChatCommand(ClientHandler ch, String setupInfo) {
		handler = ch;
		input = setupInfo;
		commands = setupInfo.split(DELIMITER);
	}

	@Override
	public void execute() {
		if (this.handler.isCanChat()) {
			if (this.handler.getOpponent() != null && this.handler.getOpponent().isCanChat()) {
				this.handler.getOpponent().sendCommands(CHAT + DELIMITER + this.handler.getName() 
								+ ": " + input.substring(commands[0].length()));
			} else if (this.handler.getOpponent() != null) {
				this.handler.sendCommands(FAILURE + DELIMITER + OTHERPLAYERCANNOTCHAT);
			} else if (this.handler.getServer().handlersInLobby().contains(this.handler) && 
							this.handler.getServer().handlersInLobby().size() >= 2) {
				int canTalk = 0;
				for (ClientHandler ch : this.handler.getServer().handlersInLobby()) {
					if (this.handler != ch && ch.isCanChat()) {
						ch.sendCommands(CHAT + DELIMITER + this.handler.getName() + ": " + 
										input.substring(commands[0].length()));
						canTalk++;
					} 
				}
				if (canTalk == 0) {
					this.handler.sendCommands(FAILURE + DELIMITER + OTHERPLAYERCANNOTCHAT);
				}
			} else {
				this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
			}
		} else {
			this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
		}
	}

}
