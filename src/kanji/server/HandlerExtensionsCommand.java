package kanji.server;

public class HandlerExtensionsCommand implements HandlerCommand {
	private String input;
	private String[] commands;
	private ClientHandler handler;
	
	public HandlerExtensionsCommand(ClientHandler ch, String setupInfo) {
		input = setupInfo;
		commands = setupInfo.split(DELIMITER);
		handler = ch;
	}

	@Override
	public void execute() {
		handleExtensions(input, commands);
	}
	
	private void handleExtensions(String input, String[] commands) {
		if (input.length() < 2) {
			handler.sendCommands(FAILURE + DELIMITER + ARGUMENTSMISSING);
		} else {
			for (String s : commands) {
				String sTrim = s.trim();
				switch (sTrim) {
					case NEWPLAYER:
						break;
					case CHALLENGE:
						handler.setChallengeable(true);
						break;
					case CHAT:
						handler.setCanChat(true);
						break;
					default:
						//doe lekker helemaal niets :)
				}
			}
		}
	}

}
