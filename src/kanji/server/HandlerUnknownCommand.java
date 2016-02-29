package kanji.server;

public class HandlerUnknownCommand implements HandlerCommand {
	private ClientHandler handler;
	
	public HandlerUnknownCommand(ClientHandler ch, String setupInfo) {
		handler = ch;
	}

	@Override
	public void execute() {
		this.handler.sendCommands(FAILURE + DELIMITER + UNKNOWNCOMMAND);
	}

}
