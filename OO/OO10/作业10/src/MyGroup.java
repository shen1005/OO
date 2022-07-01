import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;
import java.util.HashMap;

public class MyGroup implements Group {
    private int id;
    private final HashMap<Integer, Person> personMap = new HashMap<>();
    private int ageSum;
    private int ageSqrSum;
    private int valueSum;

    public MyGroup(int id) {
        this.id = id;
        ageSum = 0;
        ageSqrSum = 0;
        valueSum = 0;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof MyGroup) {
            MyGroup g = (MyGroup) obj;
            return (id == g.id);
        }
        return false;
    }

    @Override
    public void addPerson(Person p) {
        personMap.put(p.getId(), p);
        ageSum += p.getAge();
        ageSqrSum += p.getAge() * p.getAge();
        for (Person p2 : personMap.values()) {
            if (p.isLinked(p2)) {
                valueSum += p.queryValue(p2) * 2;
            }
        }
    }

    @Override
    public boolean hasPerson(Person p) {
        if (p == null) {
            return false;
        }
        return personMap.containsKey(p.getId());
    }

    @Override
    public int getValueSum() {
        return valueSum;
    }

    public void addValueSum(int valueSum) {
        this.valueSum += valueSum;
    }

    @Override
    public int getAgeMean() {
        if (personMap.size() == 0) {
            return 0;
        }
        return ageSum / personMap.size();
    }

    @Override
    public int getAgeVar() {
        if (personMap.size() == 0) {
            return 0;
        }
        return (ageSqrSum - 2 * getAgeMean() * ageSum + personMap.size() * getAgeMean() *
                getAgeMean()) / personMap.size();
    }

    @Override
    public void delPerson(Person p) {
        if (p == null) {
            return;
        }
        for (Person p2 : personMap.values()) {
            if (p.isLinked(p2)) {
                valueSum -= p.queryValue(p2) * 2;
            }
        }
        personMap.remove(p.getId());
        ageSum -= p.getAge();
        ageSqrSum -= p.getAge() * p.getAge();
    }

    @Override
    public int getSize() {
        return personMap.size();
    }

    public void addSocialValue(int value) {
        for (Person p : personMap.values()) {
            p.addSocialValue(value);
        }
    }
}
