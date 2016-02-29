package kanji.protocol;

import kanji.server.ClientHandler;
import kanji.server.HandlerCancelCommand;
import kanji.server.HandlerChallengeAcceptedCommand;
import kanji.server.HandlerChallengeCommand;
import kanji.server.HandlerChallengeDeniedCommand;
import kanji.server.HandlerChatCommand;
import kanji.server.HandlerComputerPlayerCommand;
import kanji.server.HandlerExtensionsCommand;
import kanji.server.HandlerGetBoardCommand;
import kanji.server.HandlerGetExtensionsCommand;
import kanji.server.HandlerGetHintCommand;
import kanji.server.HandlerGetOptionsCommand;
import kanji.server.HandlerMoveCommand;
import kanji.server.HandlerNewPlayerCommand;
import kanji.server.HandlerObserveCommand;
import kanji.server.HandlerPlayCommand;
import kanji.server.HandlerPracticeCommand;
import kanji.server.HandlerQuitCommand;
import kanji.server.HandlerStopGameCommand;
import kanji.server.HandlerUnknownCommand;

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
		Command command = handleCommand(input);
		command.execute();
	}
	
	private Command handleCommand(String input) {
		switch (input.split(DELIMITER)[0].trim()) {
			case EXTENSIONS:
				return new HandlerExtensionsCommand(handler, input);
			case GETOPTIONS:
				return new HandlerGetOptionsCommand(handler, input);				
			case GETEXTENSIONS:
				return new HandlerGetExtensionsCommand(handler, input);
			case NEWPLAYER:
				return new HandlerNewPlayerCommand(handler, input);
			case PLAY:
				return new HandlerPlayCommand(handler, input);
			case OBSERVE:
				return new HandlerObserveCommand(handler, input);	
			case QUIT:
				return new HandlerQuitCommand(handler, input);
			case CHALLENGE:
				return new HandlerChallengeCommand(handler, input);
			case CHALLENGEACCEPTED:
				return new HandlerChallengeAcceptedCommand(handler, input);
			case CHALLENGEDENIED:
				return new HandlerChallengeDeniedCommand(handler, input);				
			case CANCEL:
				return new HandlerCancelCommand(handler, input);				
			case COMPUTERPLAYER:
				return new HandlerComputerPlayerCommand(handler, input);						
			case PRACTICE:
				return new HandlerPracticeCommand(handler, input);
			case MOVE:
				return new HandlerMoveCommand(handler, input);				
			case GETHINT:
				return new HandlerGetHintCommand(handler, input);				
			case GETBOARD:
				return new HandlerGetBoardCommand(handler, input);
			case STOPGAME:
				return new HandlerStopGameCommand(handler, input);
			case CHAT:
				return new HandlerChatCommand(handler, input);			
			default: 
				return new HandlerUnknownCommand(handler, input);			
		}
	}	
}