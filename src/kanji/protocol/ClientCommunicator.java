package kanji.protocol;

import kanji.client.Client;
import kanji.server.game.Stone;

public class ClientCommunicator implements Constants {
	private Client client;
	private String opponent;

	public ClientCommunicator(Client client) {
		this.client = client;
	}

	/**
	 * The all-crucial parser for the incoming commands.
	 * Uses the Constants interface to check the first substring,
	 * and reacts accordingly
	 * @param incommand a string containing a command and (optional) extra information
	 * formatted as <COMMAND> <COMMAND2> ..
	 */
	public void execute(String incommand) {
		String[] commands = incommand.split(DELIMITER); 
		String command = commands[0].trim();
		switch (command) {
			case NEWPLAYERACCEPTED:
				sendCommand("O");
				break;
				
			case GETEXTENSIONS:
				String options = EXTENSIONS + DELIMITER;
				if (client.getClientName() == null) {
					options = options + NEWPLAYER + DELIMITER;
				} 
				if (client.getGame() == null) {
					options = options + CHALLENGE + DELIMITER + CHAT + DELIMITER + QUIT;
				} else {
					options = options + CHAT + DELIMITER + QUIT;
				}

				sendCommand(options);
				break;
				
			case OPTIONS:
				tui(commands, "What do you want to do?");
				break;
				
			case WAITFOROPPONENT:
				System.out.println("No opponent ready for a game, please wait a bit.");
				break;
				
			case GAMESTART:
				if (commands.length >= 4) {
					this.opponent = commands[1].trim();
					int size = Integer.parseInt(commands[2].trim());
					String color = commands[3].trim();
					client.setPlayer(client.getClientName());
					client.setOpponent(opponent);
					System.out.println("Your opponent is: " + opponent);
					if (color.equals(WHITE)) {
						client.getPlayer().setStone(WHITE);
						client.getOpponent().setStone(BLACK);
						System.out.println("You're WHITE, so wait for your opponent.");
					} else if (color.equals(BLACK)) {
						client.getPlayer().setStone(BLACK);
						client.getOpponent().setStone(WHITE);
						System.out.println("You're BLACK, so you start.");
					} else {
						this.sendCommand(FAILURE + DELIMITER + ILLEGALARGUMENT);
						break;
					}
					client.startGame(size);
					
				} else {
					sendCommand(FAILURE + " " + ARGUMENTSMISSING);
				}
				break;
				
			case MOVE:
				//interpret the move
				int move = -1;
				if (commands.length < 3) {
					sendCommand(FAILURE + DELIMITER + ARGUMENTSMISSING);
					break;
				} else if (commands[2].trim().equals(PASS)) {
					this.client.getGame().pass();
					if (this.client.getGame().getPassCount() >= 2) {
						System.out.println("The game should finish now.");
						break;
					}
					this.client.getGame().nextPlayer();
				} else if (commands.length < 4) {
					if (isPositiveInteger(commands[2].trim())) {
						move = Integer.parseInt(commands[2].trim());
					} else {
						sendCommand(FAILURE + DELIMITER + ILLEGALARGUMENT);
						break;
					} 
				} else if (commands.length >= 4) {
					if (isPositiveInteger(commands[2].trim()) && isPositiveInteger(commands[3].trim())) {
						int row = Integer.parseInt(commands[2].trim());
						int col = Integer.parseInt(commands[3].trim());
						move = client.getGame().getBoard().getIndex(row, col);
					} else {
						sendCommand(FAILURE + DELIMITER + ILLEGALARGUMENT);
						break;
					}
				} 
				//HANDLE THE MOVE!!!!
				if (move >= 0) {
					client.getGame().getBoard().setIntersection(move, Stone.toStone(commands[1].trim()));
					client.getGame().nextPlayer();
					client.getGame().resetPass();
				}
				
				
				//return some result
				if (move >= 0 && !client.getPlayer().getStone().equals(Stone.toStone(commands[1].trim()))) {
					System.out.println("Your turn, use the command: MOVE <row> <col>");
				}
				sendCommand("O");
				break;
				

			case HINT :
				if (commands.length >= 3) {
					String row = commands[1].trim();
					String col = commands[2].trim();
					System.out.printf("There's an open spot on row: %s, column: %s\n", row, col);
				} else {
					sendCommand(FAILURE + DELIMITER + ARGUMENTSMISSING);
				}
				break;
				
			case BOARD:
				if (commands.length > 3) {
					if (this.client.getGame().getBoard().getStringInclCaptives().trim().equals(incommand.substring(command.length()).trim())) {
						sendCommand("O");
					} else {
						this.client.displayBoard(commands[1].trim(), commands[2].trim(), commands[3].trim(), 9);
					}
				} else {
					sendCommand(FAILURE + DELIMITER + ARGUMENTSMISSING);
				}
				break;
				
			case GAMEOVER:
				if (commands.length > 1) {
					this.client.removeGame();
					if (commands[1].equals(VICTORY)) {
					System.out.println("You've won! :D");
					} else if (commands[1].equals(DEFEAT)) {
						System.out.println("You've lost :(");
					} else if (commands[1].equals(DRAW)) {
						System.out.println("It's a tie! :|");
					} else {
						sendCommand(FAILURE + DELIMITER + ILLEGALARGUMENT);
					}
				} else {
					sendCommand(FAILURE + DELIMITER + ARGUMENTSMISSING);
				}
				break;
			
	
			case CHAT:
				if (incommand.length() > command.length()) {
					System.out.println(incommand.substring(command.length()).trim());
				} else { 
					sendCommand(FAILURE + DELIMITER + ARGUMENTSMISSING);
				}
				break;
	
			case AVAILABLEPLAYERS:
				System.out.println("The following players are available: \n" + incommand.substring(17));
				break;
				
			case YOUVECHALLENGED:
				if (commands.length > 1) {
					System.out.printf("You've just challenged %s, please wait for a response!\n", commands[1]);
				} else {
					System.out.println("This shouldn't happen.");
				}
				break;
				
			case YOURECHALLENGED:
				if (commands.length > 1) {
					System.out.printf("You've just been challenged by %s, please respond with:\nCHALLENGEACCEPTED or CHALLENGEDENIED\n", commands[1]);
				} else {
					System.out.println("You've been challenged, but you don't know by whom... respond with: \nCHALLENGEACCEPTED or CHALLENGEDENIED\n");
				}
				break;
				
			case AVAILABLESTRATEGIES:
				if (commands.length > 1) {
					String strat = incommand.substring(command.length());
					System.out.println("The available strategies are: " + strat);
				} else {
					System.out.println("there are no strategies available.");
				}
				break;
				
			case CANCELLED:
				System.out.println("Succesfully cancelled your request.");
				break;
	
			case NOGAMESPLAYING:
				System.out.println("There are no games playing right now.");
				break;
				
			case CURRENTGAMES:
				if (commands.length > 1) {
					System.out.println("Games in progress: " + incommand.substring(commands[0].length()));
				} else {
					this.execute(NOGAMESPLAYING);
				}
				break;
				
			case OBSERVEDGAME:
				break;
	
			case FAILURE:
				switch (commands[1].trim()) {
					case UNKNOWNCOMMAND:
						System.out.println("The server does not know this command.");
						break;
					case NOTAPPLICABLECOMMAND:
						System.out.println("This command is not valid at this time.");
						break;
					case ARGUMENTSMISSING:
						System.out.println("You're missing some arguments there, bud!");
						break;
					case NOTSUPPORTEDCOMMAND:
						System.out.println("This command is not supported.");
						break;
					case TIMEOUTEXCEEDED:
						System.out.println("You've waited too long.");
						break;
					case INVALIDNAME:
						System.out.println("You can't take this name, please choose another");
						break;
					case NAMETAKEN:
						System.out.println("This name is already taken, please choose another");
						break;
					case NAMENOTALLOWED:
						System.out.println("You can't take this name, please choose another");
						break;
					case INVALIDMOVE:
						System.out.println("This is an invalid move");
						break;
					case NOTYOURTURN:
						System.out.println("Please wait for your turn!");
						break;
					case ILLEGALARGUMENT:
						System.out.println("You can't use that argument.");
						break;
					case OTHERPLAYERCANNOTCHAT:
						System.out.println("The other player can't talk to you");
						break;
					case PLAYERNOTAVAILABLE:
						System.out.println("The other player(s) is/are not available");
						break;
					case GAMENOTPLAYING:
						System.out.println("You have no game ;)");
						break;
					default:
						System.out.println("Something went horribly wrong!");
	
	
				}
				break;

			default:
				this.execute(CHAT + DELIMITER + incommand);

		}

	}

	
	/**
	 * optionally parses commands the user is likely to use, for extra convenience.
	 * @param outcommand the string sent by Client's sendCommand() method.
	 */
	public void sendCommand(String outcommand) {
		String[] commands = outcommand.split(DELIMITER);
		String command = commands[0].trim();
		switch (command) {
			
			case "N":
				sendCommand(NEWPLAYER + DELIMITER + outcommand.substring(command.length()).trim());
				break;
			case NEWPLAYER:
				if (commands.length > 1) {
					client.setClientName(commands[1].trim());
					client.sendCommand(outcommand);
				} else {
					System.out.println(ARGUMENTSMISSING);
				}
				break;
				
			case "P":
				sendCommand(PASS);
				break;
			case PASS:
				sendCommand(MOVE + DELIMITER + PASS);
				break;
			case "M":
				sendCommand(MOVE + DELIMITER + outcommand.substring(command.length()).trim());
				break;
			case "H":
				sendCommand(GETHINT);
				break;
			case "Q":
				sendCommand(QUIT);
				break;
			case "E":
				sendCommand(GETEXTENSIONS);
				break;
			case "O":
				sendCommand(GETOPTIONS);
				break;
			case "PR":
				sendCommand(PRACTICE + DELIMITER + outcommand.substring(command.length()).trim());
				break;
			case "C":
				sendCommand(CHALLENGE + DELIMITER + outcommand.substring(command.length()).trim());
				break;
			case "CH":
				sendCommand(CHAT + DELIMITER + outcommand.substring(command.length()).trim());
				break;
			case "CA":
				sendCommand(CANCEL);
				break;
			case "S":
				sendCommand(STOPGAME);
				break;
			case "G":
				sendCommand(GETBOARD);
				break;
			case "PL":
				sendCommand(PLAY);
				break;
			case QUIT:
				client.sendCommand(outcommand);
				client.disconnect();
				break;
			default:
				this.client.sendCommand(outcommand);
		}
	}
	
	/**
	 * tests whether a string is a positive integer, so it can be used on the board
	 * @param str the string to test
	 * @return true if and only is the string contains only characters, such that
	 * they are >0 and <=9
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
	 * clears space to create a beautiful interface
	 * 
	 */
	public void clearDown() {
		String image = "\n";
		for (int i = 0; i < 60; i++) {
			image += "\n";
		}
		System.out.print(image);
	}
	
	public void makeMenu(String[] options) {
		String mM = "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n"
				+ "Kanji by J van Dijk \n"
				+ "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n";
		mM += "E: Get the extensions this server supplies.\n";
		mM += "O: Get the current options.\n";
		for (String s : options) {
			String sTrim = s.trim();
			switch (sTrim) {
				case NEWPLAYER:
					mM += "N <name>: Identify yourself!\n";
					break;
				case PLAY:
					mM += "PL: Play a game against a random opponent.\n";
					break;
				case CHALLENGE:
					mM += "CH: See the players you can challenge. \nCH <name>: Challenge <name>.\n";
					break;
				case CHAT:
					mM += "C <text>: Share your thoughts!\n";
					break;
				case GETHINT:
					mM += "H: Get a Hint.\n";
					break;
				case PRACTICE:
					mM += "PR: Practice against a computer.\n";
					break;
				case MOVE:
					mM += "M <row> <col>: Make a move.\n";
					break;
				case PASS:
					mM += "P: Pass this round.\n";
					break;
				case STOPGAME:
					mM += "S: Stop this game\n";
					break;
				case GETBOARD:
					mM += "G: Get the current board, as it exists on the server.\n";
				default:
					//doe lekker helemaal niets :)
			}
		}
		mM += "Q: Quit the client.\n";
		mM += "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n\n";
		System.out.println(mM);
	}

	public void displayCurrentBoard() {
		if (client.getGame() != null) {
			String[] cb = client.getGame().getBoard().getStringInclCaptives().split(" ");
			client.displayBoard(cb[0], cb[1], cb[2], client.getGame().getBoard().getDimension());
			if (client.getGame().getCurrentPlayer() != client.getPlayer()) {
				System.out.println("\n It's Your turn!");
			} else {
				System.out.println("\n Wait for your opponent to make a move.");
			}
		}
	}
	
	public void tui(String[] options, String optional) {
		clearDown();
		displayCurrentBoard();
		makeMenu(options);
		System.out.println(optional);
	}

}
