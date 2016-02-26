package kanji.protocol.command.failure;

import kanji.client.Client;
import kanji.protocol.command.Command;

public class ClientFailure implements Command {
	private String[] commands;
	
	public ClientFailure(Client c, String setupInfo) {
		commands = setupInfo.split(DELIMITER);
	}
	
	public void execute() {
		System.out.println(getFailureMessage());
	}
	
	public String getFailureMessage() {
		switch (commands[1].trim()) {
			case UNKNOWNCOMMAND:
				return "The server does not know this command.";
			case NOTAPPLICABLECOMMAND:
				return "This command is not valid at this time.";
			case ARGUMENTSMISSING:
				return "You're missing some arguments there, bud!";
			case NOTSUPPORTEDCOMMAND:
				return "This command is not supported.";
			case TIMEOUTEXCEEDED:
				return "You've waited too long.";
			case INVALIDNAME:
				return "You can't take this name, please choose another";
			case NAMETAKEN:
				return "This name is already taken, please choose another";
			case NAMENOTALLOWED:
				return "You can't take this name, please choose another";
			case INVALIDMOVE:
				return "This is an invalid move";
			case NOTYOURTURN:
				return "Please wait for your turn!";
			case ILLEGALARGUMENT:
				return "You can't use that argument.";
			case OTHERPLAYERCANNOTCHAT:
				return "The other player can't talk to you";
			case PLAYERNOTAVAILABLE:
				return "The other player(s) is/are not available";
			case GAMENOTPLAYING:
				return "You have no game ;)";
			default:
				return "Something went horribly wrong!";
		}
	}

}
