package controller;

import java.util.ArrayList;

import model.Match;
import model.Player;

public interface IPersistor 
{
	public void write(ArrayList<Player> dataModel);

	public ArrayList<Player> read();
	
	public void addMatchForPlayer(Match newMatch, String playerName);

}
