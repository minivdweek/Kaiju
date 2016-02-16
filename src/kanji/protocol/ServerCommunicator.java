package kanji.protocol;

import kanji.server.ClientHandler;

/**
 * @author joris.vandijk
 *The translator for the commands received from a client, 
 *connected to an associated ClientHandler.
 */
public class ServerCommunicator implements Constants {
	private ClientHandler handler;

	public  ServerCommunicator(ClientHandler handler) {
		this.handler = handler;
	}

	/**
	 * Execute the commands received from the client.
	 * @param input a string with the commands to be executed
	 */
	public void execute(String input) {
		String[] commands = input.split(DELIMITER); 
		String command = commands[0];
		//Command command = parseInput(input) 
		// command.execute();
		
		switch (command) {

			case EXTENSIONS:
				handleExtensions(input, commands);
				break;

			case GETOPTIONS:
				handleGetOptions();
				break;
				
			case GETEXTENSIONS:
				String extensions = EXTENSIONS + DELIMITER + PLAY + DELIMITER + PRACTICE;
				if (this.handler.isCanChat()) {
					for (ClientHandler ch : this.handler.getServer().handlersInLobby()) {
						if (ch.isCanChat()) {
							extensions = extensions + DELIMITER + CHAT;
							break;
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
				break;

			case NEWPLAYER:
				if (this.handler.getName() == null) {
					if (commands.length > 1) {
						if (commands.length > 2) {
							//check if it is only one argument, but don't fail if there are more.
							this.handler.sendCommands(CHAT + " Server: Hold on, mr FancyPants, first name only!");
						}
						// check if it is already taken
						if (!this.handler.getServer().namesTaken().contains(commands[1])) {
							//and if not, set the name of the handler to the first input after NEWPLAYER
							this.handler.setName(commands[1]);
							this.handler.getServer().addToLobby(handler);
							this.handler.sendCommands(NEWPLAYERACCEPTED);
							for (ClientHandler ch : this.handler.getServer().handlersInLobby()) {
								if (this.handler != ch) {
									ch.sendCommands(CHAT + " Server: " + this.handler.getName() + " has joined!");
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
				break;

			case PLAY:
				if (this.handler.getName() != null && !this.handler.isChallengePending() && !this.handler.isWaitingForGame()) {
					for (ClientHandler ch : this.handler.getServer().handlersInLobby()) {
						if (ch.isWaitingForGame()) {
							setUpGame(this.handler, ch);
							this.handler.startGame(9);
							System.out.printf("Game Started, %s and %s \n", this.handler.getName(), ch.getName());
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
				break;

			case OBSERVE:
				this.handler.sendCommands(FAILURE + DELIMITER + NOTSUPPORTEDCOMMAND);
				break;
				
			/**
			 * close down everything. Stop a game, if any. Let challengers or challenged players know you've quit. 
			 */
			case QUIT:
				if (this.handler.getGame() != null) {
					this.execute(STOPGAME);
				} else if (this.handler.isChallengePending()) {
					if (this.handler.getChallenger() != null) {
						this.handler.getChallenger().setChallengePending(false);
					} else {
						for (ClientHandler ch : this.handler.getServer().handlersInLobby()) {
							if (ch.getChallenger() == this.handler) {
								ch.setChallengePending(false);
								ch.sendCommands(CHAT + " Server: Retracted Challenge, player quit");
								break;
							}
						}
					}
				}
				this.handler.quit();
				break;
			
				//----------------Challenge -------------------------------
			case CHALLENGE:
				if (this.handler.isChallengeable()) {
					if (this.handler.getName() == null) {
						this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
					} else if (!this.handler.isWaitingForGame() && !this.handler.isChallengePending()) {
						if (commands.length < 2) {
							if (this.handler.getServer().handlersInLobby().size() >= 2) {
								int challengeable = 0;
								String playersToChallenge = DELIMITER;
								for (ClientHandler ch : this.handler.getServer().handlersInLobby()) {
									if (!this.handler.getName().equals(ch.getName()) && ch.isChallengeable() && !ch.isChallengePending()) {
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
								if (ch != this.handler && ch.getName().equals(input.substring(commands[0].length()).trim())) {
									if (ch.isChallengeable() && !ch.isChallengePending()) {
										ch.setChallenger(this.handler);
										ch.setChallengePending(true);
										this.handler.setChallengePending(true);
										this.handler.sendCommands(YOUVECHALLENGED + DELIMITER + ch.getName());
										ch.sendCommands(YOURECHALLENGED + DELIMITER + this.handler.getName());
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
						this.handler.sendCommands(CHAT + " Server: CANCEL current operation, and try again.");
					}
				} else {
					this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
				}
				break;

			case CHALLENGEACCEPTED:
				if (this.handler.getName() == null || this.handler.getChallenger() != null) {
					setUpGame(this.handler, this.handler.getChallenger());					
					this.handler.startGame(9);
					System.out.printf("Game Started, %s and %s \n", this.handler.getName(), this.handler.getChallenger().getName());
					
					this.handler.setChallenger(null);
				} else {
					this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
				}
				break;

			case CHALLENGEDENIED:
				if (this.handler.getName() == null || this.handler.getChallenger() != null) {
					this.handler.getChallenger().sendCommands("Your Challenge has been denied.");
					this.handler.getChallenger().setChallengePending(false);
					this.handler.setChallenger(null);
					this.handler.setChallengePending(false);
				} else {
					this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
				}
				break;
				
			/**
			 * Cancel the effects of PLAY or CHALLENGE
			 */
			case CANCEL:
				if (this.handler.getName() != null && this.handler.getGame() == null) {
					if (this.handler.isWaitingForGame()) {
						this.handler.setWaitingForGame(false);
					} else if (this.handler.isChallengePending() && this.handler.getChallenger() == null) {
						this.handler.setChallengePending(false);
						for (ClientHandler ch : this.handler.getServer().handlersInLobby()) {
							if (ch.getChallenger() == this.handler) {
								ch.setChallenger(null);
								ch.setChallengePending(false);
								ch.sendCommands(CHAT + " Server: " + this.handler.getName() + " retracted their Challenge");
								break;
							}
						}
					} else {
						this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
					}
				} else {
					this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
				}
				this.handler.sendCommands(CANCELLED);
				
				break;

			case COMPUTERPLAYER:
				this.handler.sendCommands(FAILURE + DELIMITER + NOTSUPPORTEDCOMMAND);
				break;
				
				/**
				 * enable a practice play against a random-setting computer
				 */
			case PRACTICE:
				if (this.handler.getName() == null || this.handler.isChallengePending() || this.handler.isWaitingForGame()) {
					this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
				} else {
					this.handler.setPlayer(this.handler.getName());
					this.handler.getServer().practice(handler, 9);
					this.handler.sendCommands(GAMESTART + DELIMITER + COMPUTER + DELIMITER + "9" + DELIMITER + BLACK);
					System.out.printf("Game Started, %s and %s \n", this.handler.getName(), COMPUTER);
				}
				break;
				
				
				
//-----------------in-game commands ----------------------------------------
			/**
			 * Make a move server-side and forward the command to both players
			 */
			case MOVE:
				if (this.handler.getGame() == null) {
					this.handler.sendCommands(FAILURE + DELIMITER + GAMENOTPLAYING);
				} else {
					int move;
					if (commands.length < 2) {
						this.handler.sendCommands(FAILURE + DELIMITER + ARGUMENTSMISSING);
						break;
					} else if (commands[1].trim().equals(PASS)) {
						this.execute(PASS + input.substring(command.length()));
						break;
					} else if (commands.length < 3) {
						if (isPositiveInteger(commands[1].trim())) {
							move = Integer.parseInt(commands[1].trim());
						} else {
							this.handler.sendCommands(FAILURE + DELIMITER + ILLEGALARGUMENT);
							break;
						} 
					} else {
						if (isPositiveInteger(commands[1].trim()) && isPositiveInteger(commands[2].trim())) {
							int row = Integer.parseInt(commands[1].trim());
							int col = Integer.parseInt(commands[2].trim());
							move = this.handler.getGame().getBoard().getIndex(row, col);
						} else {
							this.handler.sendCommands(FAILURE + DELIMITER + ILLEGALARGUMENT);
							break;
						}
					}
					if (this.handler.getPlayer() != this.handler.getGame().getCurrentPlayer()) {
						this.handler.sendCommands(FAILURE + DELIMITER + NOTYOURTURN);
					} else {
						if (this.handler.getGame().getBoard().moveIsLegal(move, this.handler.getPlayer().getStone())) {
							this.handler.getPlayer().makeMove(this.handler.getGame().getBoard(), move);
							int[] coord;
							if (commands.length < 3) {
								coord = this.handler.getGame().getBoard().getCoordinates(move);
								this.handler.sendCommands(MOVE + DELIMITER + this.handler.getPlayer().getStone() + DELIMITER + coord[0] + DELIMITER + coord[1]);
								if (this.handler.getOpponent() != null) {
									this.handler.getOpponent().sendCommands(MOVE + DELIMITER + this.handler.getPlayer().getStone() + DELIMITER + coord[0] + DELIMITER + coord[1]);
								}
							} else {
								this.handler.sendCommands(MOVE + DELIMITER + this.handler.getPlayer().getStone() + input.substring(commands[0].length()));
								if (this.handler.getOpponent() != null) {
									this.handler.getOpponent().sendCommands(MOVE + DELIMITER + this.handler.getPlayer().getStone() + input.substring(commands[0].length()));
								}
							}							
							this.handler.getGame().resetPass();
							this.handler.getGame().nextPlayer();
						} else {
							this.handler.sendCommands(FAILURE + DELIMITER + INVALIDMOVE); 
						}
					}
				}
				break;

			/**
			 * a special case of move; if two players pass consecutively, the game ends
			 */
			case PASS:
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
									this.handler.getOpponent().sendCommands(GAMEOVER +DELIMITER + DEFEAT);
									}
								} else {
									this.handler.sendCommands(GAMEOVER +DELIMITER + DEFEAT);
									if (this.handler.getOpponent() != null) {
									this.handler.getOpponent().sendCommands(GAMEOVER + DELIMITER + VICTORY);
									}
								}
							} else {
								this.handler.sendCommands(GAMEOVER +DELIMITER + DRAW);
								if (this.handler.getOpponent() != null) {
								this.handler.getOpponent().sendCommands(GAMEOVER +DELIMITER + DRAW);
								}
							}
							closeGame();
						} else {
							this.handler.sendCommands(MOVE + DELIMITER + this.handler.getPlayer().getStone() + input.substring(commands[0].length()));
							if (this.handler.getOpponent() != null) {
								this.handler.getOpponent().sendCommands(MOVE + DELIMITER + this.handler.getPlayer().getStone() + input.substring(commands[0].length()));
							}
							this.handler.getGame().nextPlayer();
						}
					}	
				}
				break;
				
			case GETHINT:
				if (this.handler.getName() == null || this.handler.getGame() == null) {
					this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
				} else {
					int[] hint = this.handler.hint();
					this.handler.sendCommands(HINT + DELIMITER + hint[0] + DELIMITER + hint[1]);
				}
				break;
			
			/**
			 * returns a string representation of the board
			 */
			case GETBOARD:
				if (this.handler.getGame() != null) {
					String board = this.handler.getGame().getBoard().getStringInclCaptives();
					this.handler.sendCommands(BOARD + DELIMITER + board);
				} else {
					this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
				}
				break;
			
			/**
			 * Stops the current game
			 */
			case STOPGAME:
				if (this.handler.getGame() != null) {
					this.handler.sendCommands(CHAT + " Server: You've forfeited the game.");
					this.handler.sendCommands(GAMEOVER + DELIMITER + DEFEAT);
					if (this.handler.getOpponent() != null) {
						this.handler.getOpponent().sendCommands(CHAT + " Server: " + this.handler.getName() + " forfeited the game.");
						this.handler.getOpponent().sendCommands(GAMEOVER + DELIMITER + VICTORY);
					}					
					closeGame();
				} else {
					this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
					this.handler.sendCommands(CHAT + " Server: You're not playing a game now.");
				}
				break;


				//-----------------Chatbox commands -----------------------------
			case CHAT:
				if (this.handler.isCanChat()) {
					if (this.handler.getOpponent() != null && this.handler.getOpponent().isCanChat()) {
						this.handler.getOpponent().sendCommands(CHAT + DELIMITER + this.handler.getName() + ": " + input.substring(commands[0].length()));
					} else if (this.handler.getOpponent() != null) {
						this.handler.sendCommands(FAILURE + DELIMITER + OTHERPLAYERCANNOTCHAT);
					} else if (this.handler.getServer().handlersInLobby().contains(this.handler) && this.handler.getServer().handlersInLobby().size() >= 2) {
						int canTalk = 0;
						for (ClientHandler ch : this.handler.getServer().handlersInLobby()) {
							if (this.handler != ch && ch.isCanChat()) {
								ch.sendCommands(CHAT + DELIMITER + this.handler.getName() + ": " + input.substring(commands[0].length()));
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
				break;
				
			default: 
				this.handler.sendCommands(FAILURE + DELIMITER + UNKNOWNCOMMAND);
		}

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
				options = options + DELIMITER + MOVE + DELIMITER + PASS + DELIMITER + GETHINT + DELIMITER + STOPGAME;	
			}
		}
		options = options + DELIMITER + QUIT;
		
		this.handler.sendCommands(options);
	}

	private void handleExtensions(String input, String[] commands) {
		if (input.length() < 2) {
			this.handler.sendCommands(FAILURE + DELIMITER + ARGUMENTSMISSING);
		} else {
			for (String s : commands) {
				String sTrim = s.trim();
				switch (sTrim) {
					case NEWPLAYER:
						break;
					case CHALLENGE:
						this.handler.setChallengeable(true);
						break;
					case CHAT:
						this.handler.setCanChat(true);
						break;
					default:
						//doe lekker helemaal niets :)
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
	
	/**
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
	
	/**
	 * Starts up a game, lets the players know they've started.
	 * @param ch1 the challenged or 2nd player to join the game
	 * @param ch2 the challenger or the player waiting in the waitlist
	 */
	public void setUpGame(ClientHandler ch1, ClientHandler ch2) {
		ch1.setOpponent(ch2);
		ch2.setOpponent(ch1);
		ch1.getServer().removeFromLobby(ch1);
		ch1.getServer().removeFromLobby(ch2);
		ch1.setPlayer(ch1.getName());
		ch2.setPlayer(ch2.getName());
		ch1.sendCommands(GAMESTART + DELIMITER + ch2.getName() + DELIMITER + "9" + DELIMITER + BLACK);
		ch2.sendCommands(GAMESTART + DELIMITER + ch1.getName() + DELIMITER + "9" + DELIMITER + WHITE);
	}
	
	private Command parseInput(String input) {
		return new ExtensionsCommand();
	}
	
	private interface Command {
		void execute();
	}
	
	private class ExtensionsCommand implements Command {
		public void execute() {
			
		}
	}
	
}