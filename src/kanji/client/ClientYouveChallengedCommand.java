package kanji.client;

public class ClientYouveChallengedCommand implements ClientCommand {
	private String opponent;
	
	public ClientYouveChallengedCommand(Client c, String setupInfo) {
		opponent = setupInfo.split(DELIMITER)[1];
	}

	@Override
	public void execute() {
		System.out.printf("You've just challenged %s, "
						+ "please wait for a response.\n", opponent);		
	}

}
