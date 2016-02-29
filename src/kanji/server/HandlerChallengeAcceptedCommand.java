package kanji.server;

public class HandlerChallengeAcceptedCommand implements HandlerCommand {
	private ClientHandler handler;
	
	public HandlerChallengeAcceptedCommand(ClientHandler ch, String setupInfo) {
		handler = ch;
	}
	

	@Override
	public void execute() {
		if (this.handler.getName() == null || this.handler.getChallenger() != null) {
			setUpGame(this.handler, this.handler.getChallenger());					
			this.handler.startGame(9);
			System.out.printf("Game Started, %s and %s \n", this.handler.getName(), 
							this.handler.getChallenger().getName());					
			this.handler.setChallenger(null);
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
