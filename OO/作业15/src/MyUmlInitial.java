import com.oocourse.uml3.models.elements.UmlPseudostate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
