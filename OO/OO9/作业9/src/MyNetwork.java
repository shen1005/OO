import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.GroupIdNotFoundException;
import com.oocourse.spec1.exceptions.EqualGroupIdException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> personMap;
    private final HashMap<Integer, Group> groupMap;
    private final HashMap<Integer, Integer> fatherMap;
    private int blockNum;

    public MyNetwork() {
        personMap = new HashMap<>();
        groupMap = new HashMap<>();
        fatherMap = new HashMap<>();
        blockNum = 0;
    }

    @Override
    public boolean contains(int id) {
        return personMap.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        if (!personMap.containsKey(id)) {
            return null;
        }
        return personMap.get(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (personMap.containsKey(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        }
        personMap.put(person.getId(), person);
        fatherMap.put(person.getId(), person.getId());
        blockNum++;
    }

    @Override
    public void addRelation(int id1, int id2, int value) throws PersonIdNotFoundException,
            EqualRelationException {
        if (!personMap.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (!personMap.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        else if (personMap.get(id1).isLinked(personMap.get(id2))) {
            throw new MyEqualRelationException(id1, id2);
        }
        ((MyPerson) personMap.get(id1)).addAcquaintance(personMap.get(id2), value);
        ((MyPerson) personMap.get(id2)).addAcquaintance(personMap.get(id1), value);
        int temp1 = find(id1);
        int temp2 = find(id2);
        if (temp1 != temp2) {
            fatherMap.put(temp1, temp2);
            blockNum--;
        }
        for (Integer i: ((MyPerson)personMap.get(id1)).getGroup()) {
            if (groupMap.get(i).hasPerson(personMap.get(id2))) {
                ((MyGroup)groupMap.get(i)).addValueSum(value * 2);
            }
        }
    }

    public int find(int id) {
        if (fatherMap.get(id) == id) {
            return id;
        }
        else {
            fatherMap.put(id, find(fatherMap.get(id)));
            return fatherMap.get(id);
        }
    }

    @Override
    public int queryValue(int id1, int id2) throws PersonIdNotFoundException,
            RelationNotFoundException {
        if (!personMap.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (!personMap.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        else if (!personMap.get(id1).isLinked(personMap.get(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return personMap.get(id1).queryValue(personMap.get(id2));
    }

    @Override
    public int queryPeopleSum() {
        return personMap.size();
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!personMap.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (!personMap.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        return find(id1) == find(id2);
    }

    @Override
    public int queryBlockSum() {
        return blockNum;
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (groupMap.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        }
        groupMap.put(group.getId(), group);
    }

    @Override
    public Group getGroup(int id) {
        if (!groupMap.containsKey(id)) {
            return null;
        }
        return groupMap.get(id);
    }

    @Override
    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        if (!groupMap.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        }
        else if (!personMap.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (groupMap.get(id2).hasPerson(personMap.get(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        if (groupMap.get(id2).getSize() < 1111) {
            groupMap.get(id2).addPerson(personMap.get(id1));
            ((MyPerson)personMap.get(id1)).addGroupId(id2);
        }
    }

    @Override
    public void delFromGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        if (!groupMap.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        }
        else if (!personMap.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (!groupMap.get(id2).hasPerson(personMap.get(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        groupMap.get(id2).delPerson(personMap.get(id1));
        ((MyPerson)personMap.get(id1)).removeGroupId(id2);
    }
}
