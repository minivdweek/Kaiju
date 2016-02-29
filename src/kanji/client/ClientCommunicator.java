package kanji.client;

import kanji.protocol.Command;
import kanji.protocol.Constants;

public class ClientCommunicator implements Constants {
	private Client client;

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
		Command command = handleCommand(incommand);
		command.execute();
	}

	private Command handleCommand(String incommand) {
		switch (incommand.split(DELIMITER)[0].trim()) {
			case NEWPLAYERACCEPTED:
				return new ClientNewPlayerAcceptedCommand(client, incommand);				
			case GETEXTENSIONS:
				return new ClientExtensionsCommand(client, incommand);
			case OPTIONS:
				return new ClientOptionsCommand(client, incommand);
			case WAITFOROPPONENT:
				return new ClientWaitForOpponentCommand(client, incommand);
			case GAMESTART:
				return new ClientGameStartCommand(client, incommand);
			case MOVE:
				return new ClientMoveCommand(client, incommand);
			case HINT :
				return new ClientHintCommand(client, incommand);
			case BOARD:
				return new ClientBoardCommand(client, incommand);
			case GAMEOVER:
				return new ClientGameOverCommand(client, incommand);			
			case CHAT:
				return new ClientChatCommand(client, incommand);
			case AVAILABLEPLAYERS:
				return new ClientAvailablePlayersCommand(client, incommand);
			case YOUVECHALLENGED:
				return new ClientYouveChallengedCommand(client, incommand);
			case YOURECHALLENGED:
				return new ClientYoureChallengedCommand(client, incommand);
			case AVAILABLESTRATEGIES:
				return new ClientAvailableStrategiesCommand(client, incommand);
			case CANCELLED:
				return new ClientCancelledCommand(client, incommand);
			case NOGAMESPLAYING:
				return new ClientNoGamesPlayingCommand(client, incommand);
			case CURRENTGAMES:
				return new ClientCurrentGamesCommand(client, incommand);
//			case OBSERVEDGAME:
				
			case FAILURE:
				return new ClientFailureCommand(client, incommand);
			default:
				return new ClientChatCommand(client, incommand);
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
			case "CH":
				sendCommand(CHALLENGE + DELIMITER + outcommand.substring(command.length()).trim());
				break;
			case "C":
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

}
