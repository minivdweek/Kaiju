package kanji.server;

public class HandlerGetBoardCommand implements HandlerCommand {
	private ClientHandler handler;

	public HandlerGetBoardCommand(ClientHandler ch, String setupInfo) {
		handler = ch;
	}

	@Override
	public void execute() {
		if (this.handler.getGame() != null) {
			String board = this.handler.getGame().getBoard().getStringInclCaptives();
			this.handler.sendCommands(BOARD + DELIMITER + board);
		} else {
			this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
		}
	}

}
