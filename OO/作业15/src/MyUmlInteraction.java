import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.models.common.MessageSort;
import com.oocourse.uml3.models.elements.UmlEndpoint;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;

import java.util.HashMap;

public class MyUmlInteraction {
    private final UmlInteraction umlInteraction;
    private final HashMap<String, MyUmlLifeline> lifelineMap;
    private final HashMap<String, UmlMessage> messageMap;
    private final HashMap<String, UmlEndpoint> endpointMap;
    private final HashMap<String, MyUmlLifeline> nameToLifelineMap;
    private final HashMap<String, Integer> countOfName;
    private final String name;

    public MyUmlInteraction(UmlInteraction umlInteraction) {
        this.umlInteraction = umlInteraction;
        lifelineMap = new HashMap<>();
        messageMap = new HashMap<>();
        endpointMap = new HashMap<>();
        nameToLifelineMap = new HashMap<>();
        countOfName = new HashMap<>();
        name = umlInteraction.getName();
    }

    public void addLifeline(MyUmlLifeline myUmlLifeline) {
        lifelineMap.put(myUmlLifeline.getId(), myUmlLifeline);
        if (myUmlLifeline.getName() != null) {
            String name = myUmlLifeline.getName();
            if (!nameToLifelineMap.containsKey(name)) {
                nameToLifelineMap.put(name, myUmlLifeline);
                countOfName.put(name, 1);
            }
            else {
                countOfName.put(name, countOfName.get(name) + 1);
            }
        }
    }

    public void addEndpoint(UmlEndpoint umlEndpoint) {
        endpointMap.put(umlEndpoint.getId(), umlEndpoint);
    }

    public void addMessage(UmlMessage umlMessage) {
        messageMap.put(umlMessage.getId(), umlMessage);
        String source = umlMessage.getSource();
        String target = umlMessage.getTarget();
        if (umlMessage.getMessageSort() == MessageSort.CREATE_MESSAGE) {
            if (lifelineMap.containsKey(target)) {
                lifelineMap.get(target).addCreateMessage(umlMessage);
            }
        }
        if (endpointMap.containsKey(source) && endpointMap.containsKey(target)) {
            return;
        }
        if (endpointMap.containsKey(source)) {
            MyUmlLifeline myUmlLifeline = lifelineMap.get(target);
            myUmlLifeline.addFoundMessage(umlMessage);
        }
        else if (endpointMap.containsKey(target)) {
            MyUmlLifeline myUmlLifeline = lifelineMap.get(source);
            myUmlLifeline.addLostMessage(umlMessage);
        }
        else {
            MyUmlLifeline myUmlLifeline = lifelineMap.get(target);
            myUmlLifeline.addMessage(umlMessage);
        }
    }

    public String getId() {
        return umlInteraction.getId();
    }

    public int getLifelineCount() {
        return lifelineMap.size();
    }

    public UmlLifeline getLifelineCreator(String lifelineName)
        throws LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        if (!nameToLifelineMap.containsKey(lifelineName)) {
            throw new LifelineNotFoundException(name,lifelineName);
        }
        else if (countOfName.get(lifelineName) > 1) {
            throw new LifelineDuplicatedException(name,lifelineName);
        }
        else {
            MyUmlLifeline myUmlLifeline = nameToLifelineMap.get(lifelineName);
            if (myUmlLifeline.getCreateMessageList().size() == 0) {
                throw new LifelineNeverCreatedException(name,lifelineName);
            }
            else if (myUmlLifeline.getCreateMessageList().size() > 1) {
                throw new LifelineCreatedRepeatedlyException(name,lifelineName);
            }
            else {
                String source = myUmlLifeline.getCreateMessageList().get(0).getSource();
                return lifelineMap.get(source).getUmlLifeline();
            }
        }
    }

    public Pair<Integer, Integer> getLifelineLostAndFound(String lifelineName)
            throws LifelineNotFoundException, LifelineDuplicatedException {
        if (!nameToLifelineMap.containsKey(lifelineName)) {
            throw new LifelineNotFoundException(name,lifelineName);
        }
        else if (countOfName.get(lifelineName) > 1) {
            throw new LifelineDuplicatedException(name,lifelineName);
        }
        else {
            MyUmlLifeline myUmlLifeline = nameToLifelineMap.get(lifelineName);
            int lost = myUmlLifeline.getLostNum();
            int found = myUmlLifeline.getFoundNum();
            return new Pair<>(found, lost);
        }
    }

    public String getParentId() {
        return umlInteraction.getParentId();
    }
}
