package kanji.server;

public class HandlerPlayCommand implements HandlerCommand {
	private ClientHandler handler;
	
	public HandlerPlayCommand(ClientHandler ch, String setupInfo) {
		handler = ch;
	}
	
	@Override
	public void execute() {
		if (this.handler.getName() != null && !this.handler.isChallengePending() && 
							!this.handler.isWaitingForGame()) {
			for (ClientHandler ch : this.handler.getServer().handlersInLobby()) {
				if (ch.isWaitingForGame()) {
					setUpGame(this.handler, ch);
					this.handler.startGame(9);
					System.out.printf("Game Started, %s and %s \n", 
									this.handler.getName(), ch.getName());
					break;
				}
			}
			if (this.handler.getOpponent() == null) {
				this.handler.setWaitingForGame(true);
				this.handler.sendCommands(WAITFOROPPONENT);
			}
		} else {
			this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
		}
	}
	
	/**
	 * Starts up a game, lets the players know they've started.
	 * @param ch1 the challenged or 2nd player to join the game
	 * @param ch2 the challenger or the player waiting in the waitlist
	 */
	private void setUpGame(ClientHandler ch1, ClientHandler ch2) {
		ch1.setOpponent(ch2);
		ch2.setOpponent(ch1);
		ch1.getServer().removeFromLobby(ch1);
		ch1.getServer().removeFromLobby(ch2);
		ch1.setPlayer(ch1.getName());
		ch2.setPlayer(ch2.getName());
		ch1.sendCommands(GAMESTART + DELIMITER + ch2.getName() 
						+ DELIMITER + "9" + DELIMITER + BLACK);
		ch2.sendCommands(GAMESTART + DELIMITER + ch1.getName() 
						+ DELIMITER + "9" + DELIMITER + WHITE);
	}

}
