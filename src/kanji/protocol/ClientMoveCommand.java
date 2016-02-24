package kanji.protocol;

import kanji.client.Client;
import kanji.server.game.Stone;

public class ClientMoveCommand implements Command {
	private String[] subcommands;
	private Client client;

	public ClientMoveCommand(Client c, String setupInfo) {
		subcommands = setupInfo.split(DELIMITER);
		client = c;
	}
	
	/**
	 * Executes this command
	 */
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		int row = -1;
		int col = -1;
		if (this.subcommands.length < 3 || (!subcommands[2].trim().equals(PASS) && subcommands.length == 3)) {
			client.sendCommand(FAILURE + DELIMITER + ARGUMENTSMISSING);
			return;
		} else if (this.subcommands[2].trim().equals(PASS)) {
			client.getGame().pass();
			if (client.getGame().getPassCount() >= 2) {
				System.out.println("The game should finish now...");
				return;
			}
			client.getGame().nextPlayer();
		} else {
			if (isPositiveInteger(subcommands[2]) && isPositiveInteger(subcommands[3])) {
				row = Integer.parseInt((subcommands[2].trim()));
				col = Integer.parseInt((subcommands[3].trim()));
			} else {
				client.sendCommand(FAILURE + DELIMITER + ILLEGALARGUMENT);
				return;
			}
		}
		if (row >= 0 && col >= 0) {
			client.getGame().getBoard().setIntersection(row, col, Stone.toStone(subcommands[1].trim()));
			client.getGame().nextPlayer();
			client.getGame().resetPass();
		}
		client.sendCommand(GETOPTIONS);
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
	
	public int getPositiveInteger(String str) {
		if (str == null) {
			return -1;
		}
		int length = str.length();
		if (length == 0) {
			return -1;
		}
		int result = 0;
		for (int i = 0; i < length; i++) {
			int c = str.charAt(i);
			result += c * 10 ^ (length - (i + 1));
		}
		return result;
	}

}
