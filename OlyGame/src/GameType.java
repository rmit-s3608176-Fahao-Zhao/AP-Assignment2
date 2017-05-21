
public enum GameType {
    /**
     * the game's three types
     */
    SWIMMING(200), CYCLING(800), RUNNING(20);
    
    private int maxTime;
    
    private GameType(int maxTime) {
    	this.maxTime = maxTime;
    }
    
    public int maxTime() {
    	return maxTime;
    }
}
