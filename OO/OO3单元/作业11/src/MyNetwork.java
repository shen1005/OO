import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import com.oocourse.spec3.main.EmojiMessage;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.HashSet;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> personMap;
    private final HashMap<Integer, Group> groupMap;
    private final HashMap<Integer, Integer> fatherMap;
    private int blockNum;
    private HashMap<Integer, Message> messageMap;
    private HashMap<Integer, Graph> graphMap;
    private HashMap<Integer, Integer> emojiMessageMap;

    public MyNetwork() {
        personMap = new HashMap<>();
        groupMap = new HashMap<>();
        fatherMap = new HashMap<>();
        blockNum = 0;
        messageMap = new HashMap<>();
        graphMap = new HashMap<>();
        emojiMessageMap = new HashMap<>();
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
        Graph graph = new Graph(person);
        graphMap.put(person.getId(), graph);
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
        Side side1 = new Side(value, id1, id2);
        int temp1 = find(id1);
        int temp2 = find(id2);
        if (temp1 != temp2) {
            fatherMap.put(temp1, temp2);
            blockNum--;
            graphMap.get(temp2).getSides().addAll(graphMap.get(temp1).getSides());
            graphMap.get(temp2).getChildren().addAll(graphMap.get(temp1).getChildren());
            graphMap.get(temp2).addChild(id1);
            graphMap.get(temp2).addSide(side1);
            graphMap.remove(temp1);
        }
        else {
            graphMap.get(temp1).addSide(side1);
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

    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (!groupMap.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groupMap.get(id).getSize();
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groupMap.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groupMap.get(id).getValueSum();
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groupMap.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groupMap.get(id).getAgeVar();
    }

    @Override
    public boolean containsMessage(int id) {
        return messageMap.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException,
            EmojiIdNotFoundException, EqualPersonIdException {
        if (messageMap.containsKey(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        }
        else if (message instanceof MyEmojiMessage &&
                !emojiMessageMap.containsKey(((MyEmojiMessage)message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((MyEmojiMessage)message).getEmojiId());
        }
        else if (message.getType() == 0 && message.getPerson1() == message.getPerson2()) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        messageMap.put(message.getId(), message);
    }

    @Override
    public Message getMessage(int id) {
        if (!messageMap.containsKey(id)) {
            return null;
        }
        return messageMap.get(id);
    }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException {
        if (!messageMap.containsKey(id)) {
            throw new MyMessageIdNotFoundException(id);
        }
        else if (messageMap.get(id).getType() == 0 && !personMap.get(messageMap.
                get(id).getPerson1().getId()).isLinked(messageMap.get(id).getPerson2())) {
            throw new MyRelationNotFoundException(messageMap.get(id).getPerson1().getId(),
                    messageMap.get(id).getPerson2().getId());
        }
        else if (messageMap.get(id).getType() == 1 && !groupMap.get(messageMap.
                get(id).getGroup().getId()).hasPerson(messageMap.get(id).getPerson1())) {
            throw new MyPersonIdNotFoundException(messageMap.get(id).getPerson1().getId());
        }
        MyMessage message = (MyMessage)messageMap.get(id);
        if (messageMap.get(id).getType() == 0) {
            message.getPerson1().addSocialValue(message.getSocialValue());
            message.getPerson2().addSocialValue(message.getSocialValue());
            if (message instanceof MyEmojiMessage) {
                emojiMessageMap.put(((MyEmojiMessage)message).getEmojiId(),
                        emojiMessageMap.get(((MyEmojiMessage)message).getEmojiId()) + 1);
            }
            if (message instanceof MyRedEnvelopeMessage) {
                message.getPerson2().addMoney(((MyRedEnvelopeMessage)message).getMoney());
                message.getPerson1().addMoney(-1 * ((MyRedEnvelopeMessage)message).getMoney());
            }
            ((MyPerson)message.getPerson2()).addMessage(message);
            messageMap.remove(id);
        }
        else {
            ((MyGroup)message.getGroup()).addSocialValue(message.getSocialValue());
            if (message instanceof MyEmojiMessage) {
                emojiMessageMap.put(((MyEmojiMessage)message).getEmojiId(),
                        emojiMessageMap.get(((MyEmojiMessage)message).getEmojiId()) + 1);
            }
            if (message instanceof MyRedEnvelopeMessage) {
                int i = ((MyRedEnvelopeMessage) message).getMoney() / message.getGroup().getSize();
                ((MyGroup) message.getGroup()).addMoney(message.getPerson1().getId(), i);
                message.getPerson1().addMoney(-1 * i * (message.getGroup().getSize() - 1));
            }
            messageMap.remove(id);
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!personMap.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return personMap.get(id).getSocialValue();
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!personMap.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return personMap.get(id).getReceivedMessages();
    }

    public int tempFind(int id, HashMap<Integer, Integer> map) {
        if (!map.containsKey(id)) {
            return id;
        }
        if (map.get(id) == id) {
            return id;
        }
        else {
            map.put(id, tempFind(map.get(id), map));
            return map.get(id);
        }
    }

    @Override
    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        if (!personMap.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        PriorityQueue<Side> queue = new PriorityQueue<>();
        queue.addAll(graphMap.get(find(id)).getSides());
        HashMap<Integer, Integer> map = new HashMap<>();
        int sum = 0;
        int leastConnection = 0;
        int sideNum = graphMap.get(find(id)).getChildren().size();
        while (sum != sideNum - 1) {
            Side side = queue.poll();
            if (side == null) {
                break;
            }
            if (tempFind(side.getPerson1(), map) == tempFind(side.getPerson2(), map)) {
                continue;
            }
            leastConnection += side.getValue();
            sum++;
            map.put(tempFind(side.getPerson1(), map), tempFind(side.getPerson2(), map));
        }
        return leastConnection;
    }

    @Override
    public boolean containsEmojiId(int id) {
        return emojiMessageMap.containsKey(id);
    }

    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (emojiMessageMap.containsKey(id)) {
            throw new MyEqualEmojiIdException(id);
        }
        emojiMessageMap.put(id, 0);
    }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!personMap.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return personMap.get(id).getMoney();
    }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!emojiMessageMap.containsKey(id)) {
            throw new MyEmojiIdNotFoundException(id);
        }
        return emojiMessageMap.get(id);
    }

    @Override
    public int deleteColdEmoji(int limit) {
        emojiMessageMap.entrySet().removeIf(entry -> entry.getValue() < limit);
        messageMap.entrySet().removeIf(entry -> entry.getValue() instanceof EmojiMessage
                && !emojiMessageMap.containsKey(((EmojiMessage) entry.getValue()).getEmojiId()));
        return emojiMessageMap.size();
    }

    @Override
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!personMap.containsKey(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        personMap.get(personId).getMessages().
                removeIf(message -> message instanceof MyNoticeMessage);
    }

    @Override
    public int sendIndirectMessage(int id) throws MessageIdNotFoundException {
        if (!messageMap.containsKey(id) || messageMap.get(id).getType() == 1) {
            throw new MyMessageIdNotFoundException(id);
        }
        if (messageMap.containsKey(id) && messageMap.get(id).getType() == 0 &&
                find(messageMap.get(id).getPerson1().getId())
                        != find(messageMap.get(id).getPerson2().getId())) {
            return -1;
        }
        if (messageMap.get(id) instanceof RedEnvelopeMessage) {
            RedEnvelopeMessage redEnvelopeMessage = (RedEnvelopeMessage) messageMap.get(id);
            int money = redEnvelopeMessage.getMoney();
            redEnvelopeMessage.getPerson1().addMoney(-1 * money);
            redEnvelopeMessage.getPerson2().addMoney(money);
        }
        if (messageMap.get(id) instanceof EmojiMessage) {
            Message message = messageMap.get(id);
            emojiMessageMap.put(((MyEmojiMessage)message).getEmojiId(),
                    emojiMessageMap.get(((MyEmojiMessage)message).getEmojiId()) + 1);
        }
        Message message = messageMap.get(id);
        messageMap.remove(id);
        message.getPerson1().addSocialValue(message.getSocialValue());
        message.getPerson2().addSocialValue(message.getSocialValue());
        ((MyPerson) message.getPerson2()).addMessage(message);
        PriorityQueue<PersonDis> queue = new PriorityQueue<>();
        HashSet<Person> visited = new HashSet<>();
        queue.add(new PersonDis(message.getPerson1(), 0));
        int sum;
        while (!queue.isEmpty()) {
            PersonDis personDis = queue.poll();
            if (personDis == null) {
                break;
            }
            if (visited.contains(personDis.getPerson())) {
                continue;
            }
            if (personDis.getPerson().getId() == message.getPerson2().getId()) {
                return personDis.getDis();
            }
            for (Person person : ((MyPerson) personDis.getPerson()).getAcquaintance().values()) {
                if (!visited.contains(person)) {
                    sum = personDis.getDis() + personDis.getPerson().queryValue(person);
                    queue.add(new PersonDis(person, sum));
                }
            }
            visited.add(personDis.getPerson());
        }
        return 666;
    }
}
