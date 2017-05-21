import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JOptionPane;

public class Driver {
	public static final String PARTICIPANTS_PATH = "participants.txt";
	public static final String RESULT_PATH = "gameResults.txt";

	private Map<String, Athlete> athleteMap;
	private Map<String, Official> officialMap;
	private List<Game> gameList;
	private Game currentGame;

	/**
	 * constructor method
	 */
	public Driver() {
		athleteMap = new HashMap<String, Athlete>();
		officialMap = new HashMap<String, Official>();
		gameList = new ArrayList<Game>();
		currentGame = null;

		this.readParticipantsData();
		this.readGameResults();
	}

	/**
	 * read the data from config file
	 */
	private void readParticipantsData() {
		if(readDataFromDataBase()) {
			return;
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					PARTICIPANTS_PATH)));
			String line;
			while ((line = br.readLine()) != null) {
				String[] arr = line.split(",");
				String id = arr[0].trim();
				String type = arr[1].trim();
				String name = arr[2].trim();
				if (id.equals("") || type.equals("") || name.equals("")
						|| arr[3].trim().equals("") || arr[4].trim().equals("")) {
					continue;
				}
				int age = Integer.valueOf(arr[3].trim());
				String state = arr[4].trim();
				addAthlete(id, type, name, age, state);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * add athlete
	 * @param id
	 * @param type
	 * @param name
	 * @param age
	 * @param state
	 */
	private void addAthlete(String id, String type,
			String name, int age, String state) {
		switch (type) {
		case "swimmer":
			athleteMap.put(id, new Swimmer(id, name, age, state));
			break;
		case "cyclist":
			athleteMap.put(id, new Cyclist(id, name, age, state));
			break;
		case "sprinter":
			athleteMap.put(id, new Sprinter(id, name, age, state));
			break;
		case "officer":
			officialMap.put(id, new Official(id, name, age, state));
			break;
		case "super":
			athleteMap.put(id, new SuperAthlete(id, name, age, state));
			break;
		default:
			return;
		}
	}
	
	/**
	 * read data from database
	 * @return success:true  fail:false
	 */
	private boolean readDataFromDataBase() {
		String sql= "jdbc:sqlite:part.db";
	    try {
			Class.forName("org.sqlite.JDBC");
		    Connection conn = DriverManager.getConnection(sql);
		    Statement stat = conn.createStatement();
		    ResultSet rs = stat.executeQuery("select * from participant");
		    while(rs.next()) {
		    	String id = rs.getString("id");
		    	String name = rs.getString("name");
		    	String type = rs.getString("type");
		    	String state = rs.getString("state");
		    	int age = rs.getInt("age");
		    	addAthlete(id, type, name, age, state);		    	
		    }
		} catch (ClassNotFoundException | SQLException e) {
			return false;
		}
		return true;
	}

	/**
	 * read game results
	 */
	private void readGameResults() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					RESULT_PATH)));
			String line;
			GameResult result = new GameResult();
			while ((line = br.readLine()) != null) {
				if (line.trim().equals("")) {
					result = new GameResult();
					continue;
				}
				String[] gameinfo = line.split(",");
				result.setGameId(gameinfo[0].trim());
				result.setOfficialId(gameinfo[1].trim());
				result.setTime(gameinfo[2].trim());
				List<Participant> list = new ArrayList<Participant>();
				while ((line = br.readLine()) != null) {
					if (line.trim().equals("")) {
						break;
					}
					String[] playerinfo = line.split(",");
					Participant p = new Swimmer("", "", 1, "");
					p.setId(playerinfo[0].trim());
					p.setTime(Integer.parseInt(playerinfo[1].trim()));
					p.setPoint(Integer.parseInt(playerinfo[2].trim()));
					list.add(p);
				}
				result.setPlayerList(list);
				Game game = new Running(result.getGameId());
				game.setGameResult(result);
				gameList.add(game);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Map<String, Official> getOfficialMap() {
		return officialMap;
	}

	public Map<String, Athlete> getAthleteMap() {
		return athleteMap;
	}

	/**
	 * get the type of athlete
	 * @param ath
	 * @return
	 */
	public String getPlayerType(Athlete ath) {
		String type = "";
		if (ath instanceof Swimmer) {
			type = "swimmer";
		} else if (ath instanceof Cyclist) {
			type = "cyclist";
		} else if (ath instanceof Sprinter) {
			type = "sprinter";
		} else if (ath instanceof SuperAthlete) {
			type = "super";
		}
		return type;
	}

	/**
	 * run the game
	 */
	public boolean runGame() {
		if (currentGame == null) {
			showMsg("the current game is not set");
			return false;
		}
		if (currentGame.isRun()) {
			showMsg("the current game has been runned");
			return false;
		}
		Random random = new Random(System.currentTimeMillis());
		for (Athlete ath : currentGame.getAthletes()) {
			ath.compete(random);
		}
		currentGame.getOfficial().summerize(currentGame);
		currentGame.setRun(true);
		if (!currentGame.saveResult()) {
			showMsg("save game result failed");
		}
		gameList.add(currentGame);

		return true;
	}

	/**
	 * show message
	 * 
	 * @param msg
	 */
	public void showMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "info",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * show results of all the games
	 */
	public String allResults() {
		StringBuilder sb = new StringBuilder();
		for (Game game : gameList) {
			sb.append(game.getGameResult().toString() + "\n");
		}
		return sb.toString();
	}

	/**
	 * show the points of all players
	 */
	public String allPoints() {
		StringBuilder sb = new StringBuilder();
		sb.append("id               name  point\n");
		for (Athlete ath : athleteMap.values()) {
			Participant p = (Participant) ath;
			sb.append(p.getId() + "    " + p.getName() + "    "
					+ p.getTotalPoint() + "\n");
		}
		return sb.toString();
	}

	/**
	 * set the current game
	 * 
	 * @param game
	 */
	public void setCurrentGame(Game game) {
		this.currentGame = game;
	}

	/**
	 * get the current game
	 * 
	 * @return
	 */
	public Game getCurrentGame() {
		return currentGame;
	}
}
