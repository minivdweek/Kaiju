package kanji.server;

public class HandlerMoveCommand implements HandlerCommand {
	private ClientHandler handler;
	private String[] commands;
	private String input;
	
	public HandlerMoveCommand(ClientHandler ch, String setupInfo) {
		handler = ch;
		input = setupInfo;
		commands = setupInfo.split(DELIMITER);
	}

	@Override
	public void execute() {
		if (this.handler.getGame() == null) {
			this.handler.sendCommands(FAILURE + DELIMITER + GAMENOTPLAYING);
		} else {
			int move;
			if (commands.length < 2) {
				this.handler.sendCommands(FAILURE + DELIMITER + ARGUMENTSMISSING);
				return;
			} else if (commands[1].trim().equals(PASS)) {
				pass();
				return;
			} else if (commands.length < 3) {
				if (isPositiveInteger(commands[1].trim())) {
					move = Integer.parseInt(commands[1].trim());
				} else {
					this.handler.sendCommands(FAILURE + DELIMITER + ILLEGALARGUMENT);
					return;
				} 
			} else {
				if (isPositiveInteger(commands[1].trim()) && 
								isPositiveInteger(commands[2].trim())) {
					int row = Integer.parseInt(commands[1].trim());
					int col = Integer.parseInt(commands[2].trim());
					move = this.handler.getGame().getBoard().getIndex(row, col);
				} else {
					this.handler.sendCommands(FAILURE + DELIMITER + ILLEGALARGUMENT);
					return;
				}
			}
			if (this.handler.getPlayer() != this.handler.getGame().getCurrentPlayer()) {
				this.handler.sendCommands(FAILURE + DELIMITER + NOTYOURTURN);
			} else {
				if (this.handler.getGame().getBoard().moveIsLegal(move, 
								this.handler.getPlayer().getStone())) {
					this.handler.getPlayer().makeMove(this.handler.getGame().getBoard(), move);
					int[] coord;
					if (commands.length < 3) {
						coord = this.handler.getGame().getBoard().getCoordinates(move);
						this.handler.sendCommands(MOVE + DELIMITER + 
										this.handler.getPlayer().getStone() + DELIMITER 
										+ coord[0] + DELIMITER + coord[1]);
						if (this.handler.getOpponent() != null) {
							this.handler.getOpponent().sendCommands(MOVE + DELIMITER 
											+ this.handler.getPlayer().getStone() + DELIMITER 
											+ coord[0] + DELIMITER + coord[1]);
						}
					} else {
						this.handler.sendCommands(MOVE + DELIMITER + 
										this.handler.getPlayer().getStone() 
										+ input.substring(commands[0].length()));
						if (this.handler.getOpponent() != null) {
							this.handler.getOpponent().sendCommands(MOVE + DELIMITER 
											+ this.handler.getPlayer().getStone() 
											+ input.substring(commands[0].length()));
						}
					}							
					this.handler.getGame().resetPass();
					this.handler.getGame().nextPlayer();
				} else {
					this.handler.sendCommands(FAILURE + DELIMITER + INVALIDMOVE); 
				}
			}
		}
	}
	
	/**.
	 * Check if a given string represents an integer
	 * @param str the string to check
	 * @return true if and only if the string can be parsed into an integer
	 */
	public boolean isPositiveInteger(String str) {
	    if (str == null) {
	        return false;
	    }
	    int length = str.length();
	    if (length == 0) {
	        return false;
	    }
	    int i = 0;
	    
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') {
	            return false;
	        }
	    }
	    return true;
	}
	
	private void pass() {
		if (this.handler.getGame() == null) {
			this.handler.sendCommands(FAILURE + DELIMITER + GAMENOTPLAYING);
		} else {
			if (this.handler.getPlayer() != this.handler.getGame().getCurrentPlayer()) {
				this.handler.sendCommands(FAILURE + DELIMITER + NOTYOURTURN);
			} else {

				this.handler.getGame().pass();
				if (this.handler.getGame().getPassCount() >= 2) {
					if (this.handler.getGame().getWinner() != null) {
						if (this.handler.getPlayer() == this.handler.getGame().getWinner()) {
							this.handler.sendCommands(GAMEOVER + DELIMITER + VICTORY);
							if (this.handler.getOpponent() != null) {
								this.handler.getOpponent().sendCommands(GAMEOVER 
												+ DELIMITER + DEFEAT);
							}
						} else {
							this.handler.sendCommands(GAMEOVER + DELIMITER + DEFEAT);
							if (this.handler.getOpponent() != null) {
								this.handler.getOpponent().sendCommands(GAMEOVER 
												+ DELIMITER + VICTORY);
							}
						}
					} else {
						this.handler.sendCommands(GAMEOVER + DELIMITER + DRAW);
						if (this.handler.getOpponent() != null) {
							this.handler.getOpponent().sendCommands(GAMEOVER 
											+ DELIMITER + DRAW);
						}
					}
					closeGame();
				} else {
					this.handler.sendCommands(MOVE + DELIMITER + this.handler.getPlayer().getStone()
									+ input.substring(commands[0].length()));
					if (this.handler.getOpponent() != null) {
						this.handler.getOpponent().sendCommands(MOVE + DELIMITER 
										+ this.handler.getPlayer().getStone() 
										+ input.substring(commands[0].length()));
					}
					this.handler.getGame().nextPlayer();
				}
			}	
		}
	}
	
	/**.
	 * Closes the game, returns its players to the lobby, and cleans up the ClientHandlers
	 */
	private void closeGame() {
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
