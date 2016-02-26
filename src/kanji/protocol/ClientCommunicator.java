package kanji.protocol;

import kanji.client.Client;
import kanji.protocol.command.ClientAvailablePlayersCommand;
import kanji.protocol.command.ClientAvailableStrategiesCommand;
import kanji.protocol.command.ClientBoardCommand;
import kanji.protocol.command.ClientChatCommand;
import kanji.protocol.command.ClientCurrentGamesCommand;
import kanji.protocol.command.ClientExtensionsCommand;
import kanji.protocol.command.ClientGameOverCommand;
import kanji.protocol.command.ClientGameStartCommand;
import kanji.protocol.command.ClientHintCommand;
import kanji.protocol.command.ClientMoveCommand;
import kanji.protocol.command.ClientOptionsCommand;
import kanji.protocol.command.ClientYoureChallengedCommand;
import kanji.protocol.command.ClientYouveChallengedCommand;
import kanji.protocol.command.failure.ClientFailure;

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
		handleCommand(incommand);
	}

	private void handleCommand(String incommand) {
		switch (incommand.split(DELIMITER)[0].trim()) {
			case NEWPLAYERACCEPTED:
				sendCommand("O");
				break;				
			case GETEXTENSIONS:
				(new ClientExtensionsCommand(client, incommand)).execute();
				break;
			case OPTIONS:
				(new ClientOptionsCommand(client, incommand)).execute();
				break;
			case WAITFOROPPONENT:
				System.out.println("No opponent ready for a game, please wait a bit.");
				break;
			case GAMESTART:
				(new ClientGameStartCommand(client, incommand)).execute();
				break;
			case MOVE:
				(new ClientMoveCommand(client, incommand)).execute();
				break;
			case HINT :
				(new ClientHintCommand(client, incommand)).execute();
				break;
			case BOARD:
				(new ClientBoardCommand(client, incommand)).execute();
				break;
			case GAMEOVER:
				(new ClientGameOverCommand(client, incommand)).execute();
				break;			
			case CHAT:
				(new ClientChatCommand(client, incommand)).execute();
				break;
			case AVAILABLEPLAYERS:
				(new ClientAvailablePlayersCommand(client, incommand)).execute();
				break;
			case YOUVECHALLENGED:
				(new ClientYouveChallengedCommand(client, incommand)).execute();
				break;
			case YOURECHALLENGED:
				(new ClientYoureChallengedCommand(client, incommand)).execute();
				break;
			case AVAILABLESTRATEGIES:
				(new ClientAvailableStrategiesCommand(client, incommand)).execute();
				break;
			case CANCELLED:
				System.out.println("Succesfully cancelled your request.");
				break;
			case NOGAMESPLAYING:
				System.out.println("There are no games playing right now.");
				break;
			case CURRENTGAMES:
				(new ClientCurrentGamesCommand(client, incommand)).execute();
				break;
			case OBSERVEDGAME:
				break;
			case FAILURE:
				(new ClientFailure(client, incommand)).execute();
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

}
