import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private HashMap<Integer, Person> acquaintance = new HashMap<>();
    private HashMap<Integer, Integer> value = new HashMap<>();
    private HashSet<Integer> group = new HashSet<>();
    private int money;
    private int socialValue;
    private LinkedList<Message> message = new LinkedList<>();

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.money = 0;
        this.socialValue = 0;
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

    @Override
    public void addSocialValue(int num) {
        socialValue += num;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public List<Message> getMessages() {
        return message;
    }

    @Override
    public List<Message> getReceivedMessages() { //would be changed
        if (message.size() < 4) {
            return message;
        }
        return message.subList(0, 4);
    }

    @Override
    public void addMoney(int num) {
        money += num;
    }

    @Override
    public int getMoney() {
        return money;
    }

    public void addMessage(Message m) {
        message.addFirst(m);
    }
}

