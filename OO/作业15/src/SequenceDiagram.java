import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.models.elements.UmlEndpoint;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;
import com.oocourse.uml3.models.elements.UmlAttribute;

import java.util.HashMap;

public class SequenceDiagram {
    private final HashMap<String, MyUmlInteraction> interactionMap;
    private final HashMap<String, MyUmlInteraction> nameToInteractionMap;
    private final HashMap<String, Integer> countOfName;
    private final HashMap<String, MyUmlAttribute> attributeMap;
    private boolean r006 = false;
    private final HashMap<String, MyUmlLifeline> lifelineMap;

    public SequenceDiagram() {
        interactionMap = new HashMap<>();
        nameToInteractionMap = new HashMap<>();
        countOfName = new HashMap<>();
        attributeMap = new HashMap<>();
        lifelineMap = new HashMap<>();
    }

    public void dealInteraction(UmlInteraction umlInteraction) {
        MyUmlInteraction myUmlInteraction = new MyUmlInteraction(umlInteraction);
        interactionMap.put(myUmlInteraction.getId(), myUmlInteraction);
        if (umlInteraction.getName() != null) {
            String name = umlInteraction.getName();
            if (!nameToInteractionMap.containsKey(name)) {
                nameToInteractionMap.put(name, myUmlInteraction);
                countOfName.put(name, 1);
            }
            else {
                countOfName.put(name, countOfName.get(name) + 1);
            }
        }
    }

    public void dealAttribute(UmlAttribute umlAttribute) {
        attributeMap.put(umlAttribute.getId(), new MyUmlAttribute(umlAttribute));
    }

    public void dealLifeline(UmlLifeline lifeline) {
        MyUmlLifeline myUmlLifeline = new MyUmlLifeline(lifeline);
        interactionMap.get(lifeline.getParentId()).addLifeline(myUmlLifeline);
        lifelineMap.put(lifeline.getId(), myUmlLifeline);
        if (!attributeMap.containsKey(lifeline.getRepresent())) {
            r006 = true;
        }
        else {
            MyUmlAttribute myUmlAttribute = attributeMap.get(lifeline.getRepresent());
            MyUmlInteraction myUmlInteraction = interactionMap.get(lifeline.getParentId());
            if (!myUmlInteraction.getParentId().equals(myUmlAttribute.getParentId())) {
                r006 = true;
            }
        }
    }

    public void dealEndpoint(UmlEndpoint endpoint) {
        interactionMap.get(endpoint.getParentId()).addEndpoint(endpoint);
    }

    public void dealMessage(UmlMessage message) {
        MyUmlInteraction myUmlInteraction = interactionMap.get(message.getParentId());
        myUmlInteraction.addMessage(message);
    }

    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        if (!nameToInteractionMap.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        }
        else if (countOfName.get(interactionName) > 1) {
            throw new InteractionDuplicatedException(interactionName);
        }
        return nameToInteractionMap.get(interactionName).getLifelineCount();
    }

    public UmlLifeline getParticipantCreator(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        if (!nameToInteractionMap.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        }
        else if (countOfName.get(interactionName) > 1) {
            throw new InteractionDuplicatedException(interactionName);
        }
        return nameToInteractionMap.get(interactionName).getLifelineCreator(lifelineName);
    }

    public Pair<Integer, Integer> getParticipantLostAndFound(String interaction,
                                                             String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        if (!nameToInteractionMap.containsKey(interaction)) {
            throw new InteractionNotFoundException(interaction);
        }
        else if (countOfName.get(interaction) > 1) {
            throw new InteractionDuplicatedException(interaction);
        }
        return nameToInteractionMap.get(interaction).getLifelineLostAndFound(lifelineName);
    }

    public void checkR006() throws UmlRule006Exception {
        if (r006) {
            throw new UmlRule006Exception();
        }
    }

    public void checkR007() throws UmlRule007Exception {
        for (MyUmlLifeline i: lifelineMap.values()) {
            if (i.checkR007()) {
                throw new UmlRule007Exception();
            }
        }
    }
}
