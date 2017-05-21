
public abstract class Participant implements Comparable<Participant>{
    protected String id;
    protected String name;
    protected int age;
    protected String state;
    
    protected int time;
    protected int point;
    protected int totalPoint;
    
    /**
     * constructor method
     * @param id
     * @param name
     * @param age
     * @param state
     */
    public Participant(String id, String name, int age, String state) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.state = state;
        
        this.point = 0;
        this.totalPoint = 0;
    }
    
    @Override
    public int compareTo(Participant p) {
        if(this.time > p.time) {
            return 1;
        } else if (this.time == p.time) {
            return 0;
        } else {
            return -1;
        }
    }
    
    
    
    @Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Participant)) {
			return false;
		}
		Participant p = (Participant) obj;
		return p.getId().equals(this.id) && p.getName().equals(this.name);
	}

	/**
     * make a copy of the participant
     * @return
     */
    public abstract Participant copy();

    public void addPoint(int p) {
        this.point = p;
        this.totalPoint += p;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
    
    public int getTotalPoint() {
    	return totalPoint;
    }
}
