import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.models.elements.UmlRegion;
import com.oocourse.uml3.models.elements.UmlStateMachine;
import com.oocourse.uml3.models.elements.UmlState;
import com.oocourse.uml3.models.elements.UmlPseudostate;
import com.oocourse.uml3.models.elements.UmlFinalState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MyStateMachine {
    private UmlRegion umlRegion;
    private final HashMap<String, MyState> stateMap;
    private final HashMap<String, MyState> nameToStateMap;
    private final HashMap<String, Integer> countOfState;
    private final UmlStateMachine umlStateMachine;
    private String name;
    private MyUmlInitial myUmlInitial;
    private final HashMap<String, MyUmlFinal> umlFinalMap;
    private final HashMap<String, MyUmlState> umlStateMap;
    private int haveDfs;

    public MyStateMachine(UmlStateMachine umlStateMachine) {
        stateMap = new HashMap<>();
        nameToStateMap = new HashMap<>();
        countOfState = new HashMap<>();
        this.umlStateMachine = umlStateMachine;
        name = umlStateMachine.getName();
        umlFinalMap = new HashMap<>();
        umlStateMap = new HashMap<>();
        haveDfs = 0;
    }

    public void dealRegion(UmlRegion umlRegion) {
        this.umlRegion = umlRegion;
    }

    public UmlRegion getRegion() {
        return umlRegion;
    }

    public void dealState(UmlState state) {
        MyState myState = new MyState(state);
        stateMap.put(state.getId(), myState);
        umlStateMap.put(state.getId(), myState);
        if (myState.getName() != null) {
            if (!nameToStateMap.containsKey(myState.getName())) {
                nameToStateMap.put(myState.getName(), myState);
                countOfState.put(myState.getName(), 1);
            }
            else {
                countOfState.put(myState.getName(), countOfState.get(myState.getName()) + 1);
            }
        }
    }

    public void dealTransition(MyUmlTransition transition) {
        MyUmlState fromState = umlStateMap.get(transition.getSource());
        MyUmlState toState = umlStateMap.get(transition.getTarget());
        fromState.addTransition(transition, toState);
    }

    public String getName() {
        return name;
    }

    public void setUmlInitial(UmlPseudostate umlInitial) {
        MyUmlInitial temp = new MyUmlInitial(umlInitial);
        this.myUmlInitial = temp;
        umlStateMap.put(umlInitial.getId(), temp);
    }

    public void dealUmlFinal(UmlFinalState umlFinal) {
        MyUmlFinal myUmlFinal = new MyUmlFinal(umlFinal);
        umlFinalMap.put(umlFinal.getId(), myUmlFinal);
        umlStateMap.put(umlFinal.getId(), myUmlFinal);
    }

    public int getStateCount() {
        return umlStateMap.size();
    }

    public void dfs(MyUmlState umlState, HashSet<MyUmlState> visited) {
        visited.add(umlState);
        for (MyUmlState nextState : umlState.getNextStateMap().values()) {
            if (visited.contains(nextState)) {
                continue;
            }
            dfs(nextState, visited);
        }
    }

    public void check() {
        HashSet<MyUmlState> visited = new HashSet<>();
        dfs(myUmlInitial, visited);
        boolean flag = false;
        for (MyUmlFinal myUmlFinal : umlFinalMap.values()) {
            if (visited.contains(myUmlFinal)) {
                flag = true;
                break;
            }
        }
        if (flag) {
            haveDfs = 1;
        }
        else {
            haveDfs = -1;
        }
    }

    public boolean getStateIsCriticalPoint(String stateName)
        throws StateNotFoundException, StateDuplicatedException {
        if (!nameToStateMap.containsKey(stateName)) {
            throw new StateNotFoundException(name, stateName);
        }
        else if (countOfState.get(stateName) > 1) {
            throw new StateDuplicatedException(name, stateName);
        }
        if (haveDfs == -1) {
            return false;
        }
        else if (haveDfs == 0) {
            check();
            if (haveDfs == -1) {
                return false;
            }
        }
        HashSet<MyUmlState> visited = new HashSet<>();
        visited.add(nameToStateMap.get(stateName));
        dfs(myUmlInitial, visited);
        for (MyUmlFinal myUmlFinal : umlFinalMap.values()) {
            if (visited.contains(myUmlFinal)) {
                return false;
            }
        }
        return true;
    }

    public List<String> getTransitionTrigger(String sourceStateName, String targetStateName)
        throws StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        if (!nameToStateMap.containsKey(sourceStateName)) {
            throw new StateNotFoundException(name, sourceStateName);
        }
        else if (countOfState.get(sourceStateName) > 1) {
            throw new StateDuplicatedException(name, sourceStateName);
        }
        else if (!nameToStateMap.containsKey(targetStateName)) {
            throw new StateNotFoundException(name, targetStateName);
        }
        else if (countOfState.get(targetStateName) > 1) {
            throw new StateDuplicatedException(name, targetStateName);
        }
        MyUmlState sourceState = nameToStateMap.get(sourceStateName);
        MyUmlState targetState = nameToStateMap.get(targetStateName);
        if (sourceState.getUmlTransitionMap().size() == 0) {
            throw new TransitionNotFoundException(name, sourceStateName, targetStateName);
        }
        List<String> triggerList = new ArrayList<>();
        boolean flag = false;
        for (MyUmlTransition i: sourceState.getUmlTransitionMap().values()) {
            if (i.getTarget().equals(targetState.getId())) {
                flag = true;
                triggerList.addAll(i.getEventName());
            }
        }
        if (!flag) {
            throw new TransitionNotFoundException(name, sourceStateName, targetStateName);
        }
        return triggerList;
    }

    public String getId() {
        return umlStateMachine.getId();
    }

    public boolean checkR008() {
        for (MyUmlFinal i: umlFinalMap.values()) {
            if (i.checkR008()) {
                return true;
            }
        }
        return false;
    }

    public boolean checkR009() {
        for (MyUmlState i: umlStateMap.values()) {
            if (i.checkR009()) {
                return true;
            }
        }
        return false;
    }
}
