import java.util.Random;

public class Swimmer extends Participant implements Athlete{

    public Swimmer(String id, String name, int age, String state) {
        super(id, name, age, state);
    }

    @Override
    public int compete(Random random) {
        time = 100 + random.nextInt(100);
        return time;
    }

    @Override
    public GameType getType() {
        return GameType.SWIMMING;
    }

    @Override
    public void setType(GameType type) {
        //do nothing
    }
    
    @Override
    public Participant copy() {
        Swimmer sp = new Swimmer(id, name, age, state);
        sp.time = this.time;
        sp.point = this.point;
        sp.totalPoint = this.totalPoint;
        return sp;
    }

}
