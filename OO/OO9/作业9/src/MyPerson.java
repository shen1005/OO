import com.oocourse.spec1.main.Person;
import java.util.HashMap;
import java.util.HashSet;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private HashMap<Integer, Person> acquaintance = new HashMap<>();
    private HashMap<Integer, Integer> value = new HashMap<>();
    private HashSet<Integer> group = new HashSet<>();

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public int getId() {
        return id;
    }

    public HashSet<Integer> getGroup() {
        return group;
    }

    public void addGroupId(int gid) {
        group.add(gid);
    }

    public void removeGroupId(Integer gid) {
        group.remove(gid);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof MyPerson) {
            MyPerson p = (MyPerson) obj;
            return id == p.id;
        }
        return false;
    }

    @Override
    public boolean isLinked(Person p) {
        if (p.getId() == id) {
            return true;
        }
        return acquaintance.containsKey(p.getId());
    }

    @Override
    public int queryValue(Person p) {
        if (acquaintance.containsKey(p.getId())) {
            return value.get(p.getId());
        }
        return 0;
    }

    @Override
    public int compareTo(Person p) {
        return name.compareTo(p.getName());
    }

    public void addAcquaintance(Person p, int v) {
        acquaintance.put(p.getId(), p);
        value.put(p.getId(), v);
    }
}

