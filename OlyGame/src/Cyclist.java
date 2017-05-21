import java.util.Random;

public class Cyclist extends Participant implements Athlete{
  
    public Cyclist(String id, String name, int age, String state) {
        super(id, name, age, state);
    }

    @Override
    public int compete(Random ran) {
        time = 500 + ran.nextInt(300);
        return time;
    }

    @Override
    public GameType getType() {
        return GameType.CYCLING;
    }

    @Override
    public void setType(GameType type) {
        //do nothing
    }

    @Override
    public Participant copy() {
        Cyclist cyc = new Cyclist(id, name, age, state);
        cyc.time = this.time;
        cyc.point = this.point;
        cyc.totalPoint = this.totalPoint;
        return cyc;
    }

}
