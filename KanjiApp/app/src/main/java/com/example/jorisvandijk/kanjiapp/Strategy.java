package com.example.jorisvandijk.kanjiapp;

import kanji.server.game.Board;
import kanji.server.game.Stone;

public interface Strategy {

	public String getName();
	public int determineMove(Board b, Stone m);

}
