import java.util.HashMap;

public interface MyUmlState {
    public void addTransition(MyUmlTransition umlTransition, MyUmlState state);

    public String getId();

    public HashMap<String, MyUmlState> getNextStateMap();

    public HashMap<String, MyUmlTransition> getUmlTransitionMap();
}
