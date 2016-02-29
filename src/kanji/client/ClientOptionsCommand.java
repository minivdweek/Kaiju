package kanji.client;

public class ClientOptionsCommand implements ClientCommand {
	private Client client;
	private String opt;
	
	public ClientOptionsCommand(Client c, String setupInfo) {
		client = c;
		opt = setupInfo;
	}

	@Override
	public void execute() {
		tui(opt, "What do you want to do?");
	}
	

	/**.
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
	
	public void makeMenu(String str) {
		String[] options = str.split(DELIMITER);
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
					break;
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
			if (client.getGame().getCurrentPlayer() == client.getPlayer()) {
				System.out.println("\n It's Your turn!");
			} else {
				System.out.println("\n Wait for your opponent to make a move.");
			}
		}
	}
	
	public void tui(String options, String optional) {
		clearDown();
		displayCurrentBoard();
		makeMenu(options);
		System.out.println(optional);
	}
}
