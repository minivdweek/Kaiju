package kanji.server;

public class HandlerChallengeCommand implements HandlerCommand {
	private ClientHandler handler;
	private String[] commands;
	private String input;
	
	public HandlerChallengeCommand(ClientHandler ch, String setupInfo) {
		handler = ch;
		input = setupInfo;
		commands = setupInfo.split(DELIMITER);
	}

	@Override
	public void execute() {
		if (this.handler.isChallengeable()) {
			if (this.handler.getName() == null) {
				this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
			} else if (!this.handler.isWaitingForGame() && !this.handler.isChallengePending()) {
				if (commands.length < 2) {
					if (this.handler.getServer().handlersInLobby().size() >= 2) {
						int challengeable = 0;
						String playersToChallenge = DELIMITER;
						for (ClientHandler ch : this.handler.getServer().handlersInLobby()) {
							if (!this.handler.getName().equals(ch.getName()) && 
											ch.isChallengeable() && !ch.isChallengePending()) {
								playersToChallenge = playersToChallenge + DELIMITER + ch.getName();
								challengeable++;
							}
						}
						if (challengeable > 0) {
							this.handler.sendCommands(AVAILABLEPLAYERS + playersToChallenge);
						} else {
							this.handler.sendCommands(FAILURE + DELIMITER + PLAYERNOTAVAILABLE);
						}
					} else {
						this.handler.sendCommands(FAILURE + DELIMITER + PLAYERNOTAVAILABLE);
					}					
				} else {
					boolean exists = false;
					for (ClientHandler ch : this.handler.getServer().handlersInLobby()) {
						if (ch != this.handler && ch.getName()
										.equals(input.substring(commands[0].length()).trim())) {
							if (ch.isChallengeable() && !ch.isChallengePending()) {
								ch.setChallenger(this.handler);
								ch.setChallengePending(true);
								this.handler.setChallengePending(true);
								this.handler.sendCommands(YOUVECHALLENGED 
												+ DELIMITER + ch.getName());
								ch.sendCommands(YOURECHALLENGED 
												+ DELIMITER + this.handler.getName());
								exists = true;
								break;
							} else {
								this.handler.sendCommands(FAILURE + DELIMITER + PLAYERNOTAVAILABLE);
								exists = true;
								break;
							}
						}							
					}
					if (!exists) {
						this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
					}
				}
			} else {
				this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
				this.handler.sendCommands(CHAT 
								+ " Server: CANCEL current operation, and try again.");
			}
		} else {
			this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
		}
	}

}
