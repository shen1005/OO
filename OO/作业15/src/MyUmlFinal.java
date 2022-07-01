import com.oocourse.uml3.models.elements.UmlFinalState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyUmlFinal implements MyUmlState {
    private UmlFinalState umlFinalState;
    private HashMap<String, MyUmlTransition> umlTransitionMap;
    private HashMap<String, MyUmlState> nextStateMap;

    public MyUmlFinal(UmlFinalState umlFinalState) {
        this.umlFinalState = umlFinalState;
        umlTransitionMap = new HashMap<>();
        nextStateMap = new HashMap<>();
    }

    @Override
    public void addTransition(MyUmlTransition umlTransition, MyUmlState state) {
        umlTransitionMap.put(umlTransition.getId(), umlTransition);
        nextStateMap.put(umlTransition.getId(), state);
    }

    @Override
    public String getId() {
        return umlFinalState.getId();
    }

    @Override
    public HashMap<String, MyUmlState> getNextStateMap() {
        return nextStateMap;
    }

    @Override
    public HashMap<String, MyUmlTransition> getUmlTransitionMap() {
        return umlTransitionMap;
    }

    public boolean checkR008() {
        return umlTransitionMap.size() != 0;
    }

    @Override
    public boolean checkR009() {
        List<MyUmlTransition> list = new ArrayList<>();
        for (MyUmlTransition i: umlTransitionMap.values()) {
            if (!list.contains(i)) {
                list.add(i);
            }
            else {
                return true;
            }
        }
        return false;
    }
}
