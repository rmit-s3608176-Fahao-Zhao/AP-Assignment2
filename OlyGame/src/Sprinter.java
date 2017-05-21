import java.util.Random;

public class Sprinter extends Participant implements Athlete{
    
    private GameType type;

    public Sprinter(String id, String name, int age, String state) {
        super(id, name, age, state);
    }

    @Override
    public int compete(Random random) {
        switch(type) {
        case SWIMMING: time = 100 + random.nextInt(100);break;
        case CYCLING: time = 500 + random.nextInt(300);break;
        case RUNNING:time = 10 + random.nextInt(10);break;
        default: time = 0;
        }
        return time;
    }
    
    public void setType(GameType t) {
        this.type = t;
    }
    
    public GameType getType() {
        return type;
    }
    
    @Override
    public Participant copy() {
        Sprinter sp = new Sprinter(id, name, age, state);
        sp.time = this.time;
        sp.point = this.point;
        sp.totalPoint = this.totalPoint;
        return sp;
    }
}
