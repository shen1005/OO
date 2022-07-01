import com.oocourse.spec3.main.Person;

public class PersonDis implements Comparable<PersonDis> {
    private Person person;
    private int dis;

    public PersonDis(Person person, int dis) {
        this.person = person;
        this.dis = dis;
    }

    public Person getPerson() {
        return person;
    }

    public int getDis() {
        return dis;
    }

    public int compareTo(PersonDis other) {
        if (this.dis < other.dis) {
            return -1;
        } else if (this.dis > other.dis) {
            return 1;
        } else {
            return 0;
        }
    }
}
