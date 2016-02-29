package kanji.server;

/**
 * close down everything. Stop a game, if any. Let 
 * challengers or challenged players know you've quit. 
 */
public class HandlerQuitCommand implements HandlerCommand {
	private ClientHandler handler;
	private String input;

	public HandlerQuitCommand(ClientHandler ch, String setupInfo) {
		handler = ch;
		input = setupInfo;
	}

	@Override
	public void execute() {
		if (this.handler.getGame() != null) {
			(new HandlerStopGameCommand(handler, input)).execute();
		} else if (this.handler.isChallengePending()) {
			if (this.handler.getChallenger() != null) {
				this.handler.getChallenger().setChallengePending(false);
			} else {
				for (ClientHandler ch : this.handler.getServer().handlersInLobby()) {
					if (ch.getChallenger() == this.handler) {
						ch.setChallengePending(false);
						ch.sendCommands(CHAT + " Server: Retracted Challenge, player quit");
						break;
					}
				}
			}
		}
		this.handler.quit();
	}

}
