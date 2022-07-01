import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyUmlTransition {
    private final UmlTransition umlTransition;
    private final HashMap<String, UmlEvent> eventMap;

    public MyUmlTransition(UmlTransition umlTransition) {
        this.umlTransition = umlTransition;
        eventMap = new HashMap<>();
    }

    public String getId() {
        return umlTransition.getId();
    }

    public String getSource() {
        return umlTransition.getSource();
    }

    public String getTarget() {
        return umlTransition.getTarget();
    }

    public void addEvent(UmlEvent event) {
        eventMap.put(event.getId(), event);
    }

    public List<String> getEventName() {
        List<String> eventName = new ArrayList<>();
        for (UmlEvent event : eventMap.values()) {
            eventName.add(event.getName());
        }
        return eventName;
    }
}
