import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MyUmlTransition {
    private final UmlTransition umlTransition;
    private final HashMap<String, UmlEvent> eventMap;
    private final HashSet<String> nameOfEvent;

    public MyUmlTransition(UmlTransition umlTransition) {
        this.umlTransition = umlTransition;
        eventMap = new HashMap<>();
        nameOfEvent = new HashSet<>();
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
        nameOfEvent.add(event.getName());
    }

    public List<String> getEventName() {
        List<String> eventName = new ArrayList<>();
        for (UmlEvent event : eventMap.values()) {
            eventName.add(event.getName());
        }
        return eventName;
    }

    public HashSet<String> getNameOfEvent() {
        return nameOfEvent;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MyUmlTransition)) {
            return false;
        }
        MyUmlTransition other = (MyUmlTransition) obj;
        boolean flag = true;
        for (String name: nameOfEvent) {
            if (other.getNameOfEvent().contains(name)) {
                flag = false;
                break;
            }
        }
        if (flag) {
            return false;
        }
        if (this.umlTransition.getGuard() == null ||
                this.umlTransition.getGuard().trim().length() == 0) {
            return true;
        }
        if (other.umlTransition.getGuard() == null ||
                other.umlTransition.getGuard().trim().length() == 0) {
            return true;
        }
        return this.umlTransition.getGuard().equals(other.umlTransition.getGuard());
    }
}
