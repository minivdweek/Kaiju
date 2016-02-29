package kanji.server;

public class HandlerComputerPlayerCommand implements HandlerCommand {
	private ClientHandler handler;
	
	public HandlerComputerPlayerCommand(ClientHandler ch, String setupInfo) {
		handler = ch;
	}

	@Override
	public void execute() {
		this.handler.sendCommands(FAILURE + DELIMITER + NOTSUPPORTEDCOMMAND);
	}

}
