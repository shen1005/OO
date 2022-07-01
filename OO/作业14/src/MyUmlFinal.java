import com.oocourse.uml2.models.elements.UmlFinalState;

import java.util.HashMap;

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
}
