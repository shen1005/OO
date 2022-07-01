import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml3.models.elements.UmlRegion;
import com.oocourse.uml3.models.elements.UmlStateMachine;
import com.oocourse.uml3.models.elements.UmlState;
import com.oocourse.uml3.models.elements.UmlPseudostate;
import com.oocourse.uml3.models.elements.UmlFinalState;
import com.oocourse.uml3.models.elements.UmlTransition;
import com.oocourse.uml3.models.elements.UmlEvent;

import java.util.HashMap;
import java.util.List;

public class StateDiagram {
    private final HashMap<String, MyStateMachine> stateMachineMap;
    private final HashMap<String, MyStateMachine> nameToStateMachineMap;
    private final HashMap<String, Integer> countOfStateMachine;
    private final HashMap<String, UmlRegion> regionMap;
    private final HashMap<String, MyUmlTransition> transitionMap;

    public StateDiagram() {
        stateMachineMap = new HashMap<>();
        nameToStateMachineMap = new HashMap<>();
        countOfStateMachine = new HashMap<>();
        regionMap = new HashMap<>();
        transitionMap = new HashMap<>();
    }

    public void dealStateMachine(UmlStateMachine stateMachine) {
        MyStateMachine myStateMachine = new MyStateMachine(stateMachine);
        stateMachineMap.put(stateMachine.getId(), myStateMachine);
        if (myStateMachine.getName() != null) {
            String name = myStateMachine.getName();
            if (!nameToStateMachineMap.containsKey(name)) {
                nameToStateMachineMap.put(name, myStateMachine);
                countOfStateMachine.put(name, 1);
            }
            else {
                countOfStateMachine.put(name, countOfStateMachine.get(name) + 1);
            }
        }
    }

    public void dealRegion(UmlRegion region) {
        regionMap.put(region.getId(), region);
    }

    public void dealState(UmlState state) {
        UmlRegion region = regionMap.get(state.getParentId());
        MyStateMachine myStateMachine = stateMachineMap.get(region.getParentId());
        myStateMachine.dealState(state);
    }

    public void dealTransition(UmlTransition transition) {
        MyUmlTransition myTransition = new MyUmlTransition(transition);
        UmlRegion region = regionMap.get(transition.getParentId());
        MyStateMachine myStateMachine = stateMachineMap.get(region.getParentId());
        myStateMachine.dealTransition(myTransition);
        transitionMap.put(transition.getId(), myTransition);
    }

    public void dealInitial(UmlPseudostate umlPseudostate) {
        UmlRegion region = regionMap.get(umlPseudostate.getParentId());
        MyStateMachine myStateMachine = stateMachineMap.get(region.getParentId());
        myStateMachine.setUmlInitial(umlPseudostate);
    }

    public void dealFinal(UmlFinalState umlFinalState) {
        UmlRegion region = regionMap.get(umlFinalState.getParentId());
        MyStateMachine myStateMachine = stateMachineMap.get(region.getParentId());
        myStateMachine.dealUmlFinal(umlFinalState);
    }

    public void dealEvent(UmlEvent event) {
        MyUmlTransition myTransition = transitionMap.get(event.getParentId());
        myTransition.addEvent(event);
    }

    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        if (!nameToStateMachineMap.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        }
        else if (countOfStateMachine.get(stateMachineName) > 1) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        return nameToStateMachineMap.get(stateMachineName).getStateCount();
    }

    public boolean getStateIsCriticalPoint(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        if (!nameToStateMachineMap.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        }
        else if (countOfStateMachine.get(stateMachineName) > 1) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        return nameToStateMachineMap.get(stateMachineName).getStateIsCriticalPoint(stateName);
    }

    public List<String> getTransitionTrigger(
            String stateMachineName, String sourceStateName, String targetStateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException,
            TransitionNotFoundException {
        if (!nameToStateMachineMap.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        }
        else if (countOfStateMachine.get(stateMachineName) > 1) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        return nameToStateMachineMap.get(stateMachineName)
                .getTransitionTrigger(sourceStateName, targetStateName);
    }

    public void checkR008() throws UmlRule008Exception {
        for (MyStateMachine myStateMachine : stateMachineMap.values()) {
            if (myStateMachine.checkR008()) {
                throw new UmlRule008Exception();
            }
        }
    }

    public void checkR009() throws UmlRule009Exception {
        for (MyStateMachine myStateMachine : stateMachineMap.values()) {
            if (myStateMachine.checkR009()) {
                throw new UmlRule009Exception();
            }
        }
    }
}
