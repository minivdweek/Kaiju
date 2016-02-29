package com.example.jorisvandijk.kanjiapp;

import kanji.server.game.Board;

public class HumanPlayer extends Player {
	
	public HumanPlayer(String name) {
		super(name);
	}

	@Override
	public int determineMove(Board board) {	
		return 0;
	}
	

}
