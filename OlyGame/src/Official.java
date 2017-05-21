import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Official extends Participant{

    public Official(String id, String name, int age, String state) {
        super(id, name, age, state);
    }
    
    /**
     * summarize the game
     * @param game
     * @return the winner's id
     */
    public String summerize(Game game) {
        List<Participant> pants = new ArrayList<Participant>();
        for(Athlete ath : game.getAthletes()) {
            pants.add((Participant)ath);
        }
        Collections.sort(pants);
        for(int i=0; i<pants.size(); i++) {
        	pants.get(i).setPoint(0);
        }
        for(int i=0; i<pants.size() && i<3; i++) {
            if(i == 0) {
                pants.get(i).addPoint(5);
            } else if (i == 1) {
                pants.get(i).addPoint(2);
            } else {
                pants.get(i).addPoint(1);
            }
        }
        game.record(pants);
        
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(System.currentTimeMillis());
        GameResult result = new GameResult();
        result.setGameId(game.getId());
        result.setOfficialId(this.id);
        result.setTime(sdf.format(cal.getTime()));
        result.setPlayerList(game.getRecordList());
        game.setGameResult(result);
        
        return pants.get(0).getId();
    }
    
    @Override
    public Participant copy() {
        return null;
    }

}
