package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import model.Match;
import model.Player;

public class CountryDatabasePersistor implements IPersistor {

	private Connection dbConnection;
	
	public CountryDatabasePersistor()
	{
		try
		{
			//Start the driver for MySQL
			Class.forName("com.mysql.jdbc.Driver");
			
			dbConnection = DriverManager.getConnection(
					"jdbc:mysql://db4free.net:3306/simondb?"+"user=simonlong&password=password");
			System.out.println("Database Connection : "+dbConnection);
		}
		catch(ClassNotFoundException cnf)
		{
			cnf.printStackTrace();
			System.out.println(cnf.getMessage());
		}
		catch(SQLException sqlEx)
		{
			sqlEx.printStackTrace();
			System.out.println(sqlEx.getMessage());
		}
	}
	
	@Override
	public void write(ArrayList<Player> players) 
	{
		for(Player currPlayer : players)
		{
			try
			{
				PreparedStatement prepStmt = 
						dbConnection.prepareStatement("INSERT into PLAYERS values (?, ?, ?)");
				
				prepStmt.setString(1, currPlayer.getName());
				prepStmt.setInt(2, currPlayer.getAge());
				prepStmt.setInt(3, currPlayer.getNoOfGoals());
				
				prepStmt.executeUpdate();
				prepStmt.close();
			}
			catch(SQLException sqlEx)
			{
				System.out.println(sqlEx.getMessage());
			}
		}

	}

	@Override
	public ArrayList<Player> read() {
		//Create an empty list
		ArrayList<Player> playersList = new ArrayList<Player>();
		try
		{
			Statement getAllPlayersStmt = dbConnection.createStatement();
			
			ResultSet rs = 
					getAllPlayersStmt.executeQuery("SELECT * from PLAYERS");
			
			while(rs.next())
			{
				//Give me the data in column 'name' at the row
				//at which the ResultSet is currently pointing at.
				String currentName = rs.getString("name");
				int currAge = rs.getInt("age");
				int goalsScored = rs.getInt("goalsScored");
				//Re-create a Player object and initialize it 
				//with the raw data we have just extracted from 
				//the row in the database.
				Player p = 
						new Player(currentName, currAge, 0, goalsScored);
				playersList.add(p);
				
				//Get the matches for a player
				ArrayList<Match> matchesForPlayer = 
						getMatchListForPlayer(currentName);
				
				//Add the Match objects into the Player
				for(Match currMatch : matchesForPlayer)
				{
					p.addMatch(currMatch);
				}
				
			}
			getAllPlayersStmt.close();
			rs.close();
			
		}
		catch(SQLException sqlEx)
		{
			System.out.println(sqlEx.getMessage());			
		}
		
		return playersList;
	}
	
	private ArrayList<Match> getMatchListForPlayer(String playerName)
	{
		ArrayList<Match> matches = new ArrayList<Match>();
		try
		{
			PreparedStatement getMatchesForPlayer = 
					dbConnection.prepareStatement("SELECT * FROM MATCHES WHERE name=?");
			getMatchesForPlayer.setString(1, playerName);
			ResultSet matchSet = getMatchesForPlayer.executeQuery();
			//Process the ResultSet
			while(matchSet.next())
			{
				String opponent = matchSet.getString("opponent");
				String date = matchSet.getString("dateOfMatch");
				Calendar cal = 	Calendar.getInstance(); 
				cal.setTimeInMillis(Long.parseLong(date));
				
				Match match = new Match(opponent, cal.getTime());
				matches.add(match);
			}
		}
		catch(SQLException sqlEx)
		{
			System.out.println(sqlEx.getMessage());
		}
		return matches;

	}
	
	public void addMatchForPlayer(Match newMatch, String playerName)
	{
		try
		{
			PreparedStatement insertMatchRow = 
					dbConnection.prepareStatement("INSERT into MATCHES values (?,?,?)");
			insertMatchRow.setString(1, newMatch.getOpponent());
			
			Date matchDate = newMatch.getDateOfMatch();
			long timeInMillis = matchDate.getTime();
			
			insertMatchRow.setString(2, Long.toString(timeInMillis));
			insertMatchRow.setString(3, playerName);
			
			insertMatchRow.executeUpdate();
			insertMatchRow.close();
		}
		catch(SQLException sqlEx)
		{
			System.out.println(sqlEx.getMessage());
		}
	}
	

}
