package model;

import java.util.Date;

public class Match {
	private String opponent;
	private Date dateOfMatch;
	
	public Match(String opponent, Date dateOfMatch)
	{
		this.opponent = opponent;
		this.dateOfMatch = dateOfMatch;
	}

	public String getOpponent() {
		return opponent;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}

	public Date getDateOfMatch() {
		return dateOfMatch;
	}

	public void setDateOfMatch(Date dateOfMatch) {
		this.dateOfMatch = dateOfMatch;
	}
		
	public String toString()
	{
		return this.opponent;
	}
	
}
