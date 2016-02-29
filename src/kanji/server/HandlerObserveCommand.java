package kanji.server;

public class HandlerObserveCommand implements HandlerCommand {
	private ClientHandler handler;
	
	public HandlerObserveCommand(ClientHandler ch, String setupInfo) {
		handler = ch;
	}

	@Override
	public void execute() {
		handler.sendCommands(FAILURE + DELIMITER + NOTSUPPORTEDCOMMAND);
	}

}
