package kanji.server;

public class HandlerStopGameCommand implements HandlerCommand {
	private ClientHandler handler;
	
	public HandlerStopGameCommand(ClientHandler ch, String setupInfo) {
		handler = ch;
	}
	
	@Override
	public void execute() {
		if (this.handler.getGame() != null) {
			this.handler.sendCommands(CHAT + " Server: You've forfeited the game.");
			this.handler.sendCommands(GAMEOVER + DELIMITER + DEFEAT);
			if (this.handler.getOpponent() != null) {
				this.handler.getOpponent().sendCommands(CHAT + " Server: " 
								+ this.handler.getName() + " forfeited the game.");
				this.handler.getOpponent().sendCommands(GAMEOVER + DELIMITER + VICTORY);
			}					
			closeGame();
		} else {
			this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
			this.handler.sendCommands(CHAT + " Server: You're not playing a game now.");
		}
	}
	
	/**.
	 * Closes the game, returns its players to the lobby, and cleans up the ClientHandlers
	 */
	public void closeGame() {
		if (this.handler.getOpponent() != null) {
			this.handler.getOpponent().setGame(null);
			this.handler.getServer().addToLobby(this.handler.getOpponent());
			this.handler.getOpponent().setChallengePending(false);
			this.handler.getOpponent().setWaitingForGame(false);
			this.handler.getOpponent().sendCommands(GETEXTENSIONS);
			this.handler.getOpponent().setOpponent(null);
			this.handler.setOpponent(null);
		}
		this.handler.getServer().removeGameFromList(this.handler.getGame());
		this.handler.setGame(null);
		this.handler.getServer().addToLobby(this.handler);
		this.handler.setChallengePending(false);
		this.handler.setWaitingForGame(false);
		this.handler.sendCommands(GETEXTENSIONS);
	}

}
