import java.util.List;


/**
 * game result entity
 *
 */
public class GameResult {
	private String gameId;
	
	private String officialId;
	
	private String time;
	
	private List<Participant> playerList;

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getOfficialId() {
		return officialId;
	}

	public void setOfficialId(String officialId) {
		this.officialId = officialId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<Participant> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(List<Participant> playerList) {
		this.playerList = playerList;
	}
	
	public String toString() {
        String str = "";
        str += gameId + ", " + officialId + ", " + time + "\r\n";
        for(Participant p : playerList) {
            str += p.getId() + ", " +  p.getTime() + ", " + p.getPoint() + "\r\n";
        }
        
        return str;
	}
}
