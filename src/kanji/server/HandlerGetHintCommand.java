package kanji.server;

public class HandlerGetHintCommand implements HandlerCommand {
	private ClientHandler handler;
	
	public HandlerGetHintCommand(ClientHandler ch, String setupInfo) {
		handler = ch;
	}

	@Override
	public void execute() {
		if (this.handler.getName() == null || this.handler.getGame() == null) {
			this.handler.sendCommands(FAILURE + DELIMITER + NOTAPPLICABLECOMMAND);
		} else {
			int[] hint = this.handler.hint();
			this.handler.sendCommands(HINT + DELIMITER + hint[0] + DELIMITER + hint[1]);
		}
	}

}
