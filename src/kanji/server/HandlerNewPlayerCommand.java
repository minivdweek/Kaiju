package kanji.server;

public class HandlerNewPlayerCommand implements HandlerCommand {
	private ClientHandler handler;
	private String[] commands;
	
	public HandlerNewPlayerCommand(ClientHandler ch, String setupInfo) {
		handler = ch;
		commands = setupInfo.split(DELIMITER);
	}
	
	@Override
	public void execute() {
		if (this.handler.getName() == null) {
			if (commands.length > 1) {
				if (commands.length > 2) {
					//check if it is only one argument, but don't fail if there are more.
					this.handler.sendCommands(CHAT + 
									" Server: Hold on, mr FancyPants, first name only!");
				}
				// check if it is already taken
				if (!this.handler.getServer().namesTaken().contains(commands[1])) {
					//and if not, set the name of the handler to the first input after NEWPLAYER
					this.handler.setName(commands[1]);
					this.handler.getServer().addToLobby(handler);
					this.handler.sendCommands(NEWPLAYERACCEPTED);
					for (ClientHandler ch : this.handler.getServer().handlersInLobby()) {
						if (this.handler != ch) {
							ch.sendCommands(CHAT + " Server: " 
											+ this.handler.getName() + " has joined!");
						}
					}
				} else {
					this.handler.sendCommands(FAILURE + DELIMITER + NAMETAKEN);
				}
			} else {
				this.handler.sendCommands(FAILURE + DELIMITER + ARGUMENTSMISSING);
			}
		} else {
			this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
		}
	}

}
