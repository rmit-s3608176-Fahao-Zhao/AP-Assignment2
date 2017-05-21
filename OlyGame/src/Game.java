import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class Game {
    private final static int MAX_PLAYERS = 8;
    public static final String RESULT_PATH = "gameResults.txt";
    public static final int GAME_TIME = 10;
	
    private String id;
    private List<Athlete> athleteList;
    private Official official;
    protected GameType type;
    
    private GameResult gameResult;
    private List<Participant> recordList;
    
    private boolean isRun;
    
    /**
     * constructor method
     * @param id
     */
    public Game(String id) {
        this.id = id;
        athleteList = new ArrayList<>();
        official = null;
        this.isRun = false;
    }
    
    public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}

	/**
     * add one athlete to the game
     * @param ath
     */
    public void addAthlete(Athlete ath) {
        athleteList.add(ath);
    }
    
    /**
     * get the athletes
     * @return
     */
    public List<Athlete> getAthletes() {
        return athleteList;
    }
    
    /**
     * set official of the game
     * @param off
     */
    public void setOfficial(Official off) {
        this.official = off;
    }
    
    /**
     * get official of the game
     * @return
     */
    public Official getOfficial() {
        return official;
    }
    
    /**
     * record the player's state
     * @param pants
     */
    public void record(List<Participant> pants) {
        recordList = new ArrayList<Participant>();
        for(Participant p : pants) {
            recordList.add(p.copy());
        }
    }

    /**
     * whehter the game is full
     * @return true or false
     */
    public boolean isFull() {
    	return this.athleteList.size() == MAX_PLAYERS;
    			
    }
    
    /**
     * set the game's type
     * @param type
     */
    public void setType(GameType type) {
        this.type = type;
    }
    
    /**
     * get the game's type
     * @return type
     */
    public GameType getType() {
        return type;
    }
    
    /**
     * get the game id
     * @return id
     */
    public String getId() {
    	return id;
    }
    
    /**
     * save results
     */
    public boolean saveResult() {
    	try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(RESULT_PATH), true));
			bw.write(gameResult.toString() + "\r\n");
			bw.close();
		} catch (IOException e) {
			return false;
		}
    	//save to database
		String sql= "jdbc:sqlite:part.db";
	    try {
			Class.forName("org.sqlite.JDBC");
		    Connection conn = DriverManager.getConnection(sql);
		    Statement stat = conn.createStatement();
		    for(Participant p : gameResult.getPlayerList()) {
			    stat.execute("insert into result values ('"
			    			+ gameResult.getGameId() + 
			    			"','" + 
			    			gameResult.getOfficialId() + 
			    			"','" + 
			    			p.getId() + 
			    			"','" +
			    			p.getTime() + 
			    			"','" + 
			    			p.getPoint() + "')");
		    }
		} catch (ClassNotFoundException | SQLException e) {
			return true;
		}
    	return true;
    }

    /**
     * get the gameResult
     * @return
     */
	public GameResult getGameResult() {
		return gameResult;
	}

	/**
	 * set the gameResult
	 * @param gameResult
	 */
	public void setGameResult(GameResult gameResult) {
		this.gameResult = gameResult;
	}
    
	/**
	 * get record list
	 * @return
	 */
    public List<Participant> getRecordList() {
    	return recordList;
    }
    
    /**
     * get game type
     * @return
     */
    public GameType getGameType() {
    	return type;
    }
}
