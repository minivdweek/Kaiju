package kanji.protocol.command;

import kanji.client.Client;

public class ClientYoureChallengedCommand implements Command {
	private String opponent;

	public ClientYoureChallengedCommand(Client c, String setupInfo) {
		opponent = setupInfo.split(DELIMITER)[1];
	}
	
	@Override
	public void execute() {
		System.out.printf("You've just been challenged by %s, please respond with:\n"
						+ "CHALLENGEACCEPTED or CHALLENGEDENIED\n", opponent);		
	}

}
