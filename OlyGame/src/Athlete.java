import java.util.Random;

public interface Athlete{
    /**
     * get the game type the athlete participants in
     * @return
     */
    public GameType getType();
    
    /**
     * set the game type
     * @param type
     */
    public void setType(GameType type);
    
    /**
     * randomly generate the time
     * @param ran
     * @return time
     */
    public int compete(Random ran);
}
