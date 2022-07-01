import com.oocourse.uml2.models.elements.UmlPseudostate;

import java.util.HashMap;

public class MyUmlInitial implements MyUmlState {
    private final UmlPseudostate umlPseudostate;
    private final HashMap<String, MyUmlTransition> umlTransitionMap;
    private final HashMap<String, MyUmlState> nextStateMap;

    public MyUmlInitial(UmlPseudostate umlPseudostate) {
        this.umlPseudostate = umlPseudostate;
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
        return umlPseudostate.getId();
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
