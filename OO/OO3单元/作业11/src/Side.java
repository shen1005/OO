
public class Side implements Comparable<Side> {
    private final int value;
    private final int person1;
    private final int person2;

    public Side(int value, int person1, int person2) {
        this.value = value;
        this.person1 = person1;
        this.person2 = person2;
    }

    @Override
    public int compareTo(Side o) {
        return Integer.compare(this.value, o.value);
    }

    public int getValue() {
        return value;
    }

    public int getPerson1() {
        return person1;
    }

    public int getPerson2() {
        return person2;
    }
    
}
