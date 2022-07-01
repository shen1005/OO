import com.oocourse.uml2.models.elements.UmlState;
import java.util.HashMap;

public class MyState implements MyUmlState {
    private final UmlState umlState;
    private final HashMap<String, MyUmlTransition> umlTransitionMap;
    private final HashMap<String, MyUmlState> nextStateMap;

    public MyState(UmlState umlState) {
        this.umlState = umlState;
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
        return umlState.getId();
    }

    public String getName() {
        return umlState.getName();
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
