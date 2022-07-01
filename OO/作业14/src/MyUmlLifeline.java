import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class MyUmlLifeline {
    private final String id;
    private final UmlLifeline umlLifeline;
    private final String name;
    private final ArrayList<UmlMessage> createMessageList;
    private final HashMap<String, UmlMessage> lostMessageMap;
    private final HashMap<String, UmlMessage> foundMessageMap;

    public MyUmlLifeline(UmlLifeline umlLifeline) {
        this.umlLifeline = umlLifeline;
        this.id = umlLifeline.getId();
        this.name = umlLifeline.getName();
        createMessageList = new ArrayList<>();
        lostMessageMap = new HashMap<>();
        foundMessageMap = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addCreateMessage(UmlMessage umlMessage) {
        createMessageList.add(umlMessage);
    }

    public void addLostMessage(UmlMessage umlMessage) {
        lostMessageMap.put(umlMessage.getId(), umlMessage);
    }

    public void addFoundMessage(UmlMessage umlMessage) {
        foundMessageMap.put(umlMessage.getId(), umlMessage);
    }

    public ArrayList<UmlMessage> getCreateMessageList() {
        return createMessageList;
    }

    public UmlLifeline getUmlLifeline() {
        return umlLifeline;
    }

    public int getLostNum() {
        return lostMessageMap.size();
    }

    public int getFoundNum() {
        return foundMessageMap.size();
    }
}
