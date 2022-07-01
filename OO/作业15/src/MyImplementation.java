import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.interact.format.UserApi;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlPseudostate;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlFinalState;
import com.oocourse.uml3.models.elements.UmlInterface;
import com.oocourse.uml3.models.elements.UmlEndpoint;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlStateMachine;
import com.oocourse.uml3.models.elements.UmlRegion;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlInterfaceRealization;
import com.oocourse.uml3.models.elements.UmlAssociation;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlState;
import com.oocourse.uml3.models.elements.UmlParameter;
import com.oocourse.uml3.models.elements.UmlMessage;
import com.oocourse.uml3.models.elements.UmlTransition;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule009Exception;

import java.util.List;
import java.util.Map;

public class MyImplementation implements UserApi {
    private final ClassDiagram classDiagram = new ClassDiagram();
    private final SequenceDiagram sequenceDiagram = new SequenceDiagram();
    private final StateDiagram stateDiagram = new StateDiagram();

    public MyImplementation(UmlElement[] elements) {
        for (UmlElement i: elements) {
            if (i instanceof UmlClass) {
                classDiagram.dealClass((UmlClass) i);
            } else if (i instanceof UmlInterface) {
                classDiagram.dealInterface((UmlInterface) i);
            } else if (i instanceof UmlAssociationEnd) {
                classDiagram.getUmlAssociationEndHashMap().put(i.getId(), (UmlAssociationEnd) i);
            } else if (i instanceof UmlInteraction) {
                sequenceDiagram.dealInteraction((UmlInteraction) i);
            } else if (i instanceof UmlStateMachine) {
                stateDiagram.dealStateMachine((UmlStateMachine) i);
            } else if (i instanceof UmlRegion) {
                stateDiagram.dealRegion((UmlRegion) i);
            }
        }
        for (UmlElement i: elements) {
            if (i instanceof UmlGeneralization) {
                classDiagram.dealGeneralization((UmlGeneralization) i);
            } else if (i instanceof UmlOperation) {
                classDiagram.dealOperation((UmlOperation) i);
            } else if (i instanceof UmlAttribute) {
                if (!classDiagram.dealAttribute((UmlAttribute) i)) {
                    sequenceDiagram.dealAttribute((UmlAttribute) i);
                }
            } else if (i instanceof UmlInterfaceRealization) {
                classDiagram.dealInterfaceRealization((UmlInterfaceRealization) i);
            } else if (i instanceof UmlAssociation) {
                classDiagram.dealAssociation((UmlAssociation) i);
            } else if (i instanceof UmlEndpoint) {
                sequenceDiagram.dealEndpoint((UmlEndpoint) i);
            } else if (i instanceof UmlState) {
                stateDiagram.dealState((UmlState) i);
            } else if (i instanceof UmlPseudostate) {
                stateDiagram.dealInitial((UmlPseudostate) i);
            } else if (i instanceof UmlFinalState) {
                stateDiagram.dealFinal((UmlFinalState) i);
            }
        }
        for (UmlElement i: elements) {
            if (i instanceof UmlParameter) {
                classDiagram.dealParameter((UmlParameter) i);
            } else if (i instanceof UmlLifeline) {
                sequenceDiagram.dealLifeline((UmlLifeline) i);
            } else if (i instanceof UmlTransition) {
                stateDiagram.dealTransition((UmlTransition) i);
            }
        }
        for (UmlElement i: elements) {
            if (i instanceof UmlEvent) {
                stateDiagram.dealEvent((UmlEvent) i);
            } else if (i instanceof UmlMessage) {
                sequenceDiagram.dealMessage((UmlMessage) i);
            }
        }
    }

    @Override
    public int getClassCount() {
        return classDiagram.getClassCount();
    }

    @Override
    public int getClassSubClassCount(String className) throws
            ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassSubClassCount(className);
    }

    @Override
    public int getClassOperationCount(String className)
        throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassOperationCount(className);
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String className, String methodName)
        throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassOperationVisibility(className, methodName);
    }

    @Override
    public List<Integer> getClassOperationCouplingDegree(String className, String methodName)
        throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        return classDiagram.getClassOperationCouplingDegree(className, methodName);
    }

    @Override
    public int getClassAttributeCouplingDegree(String className)
        throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassAttributeCouplingDegree(className);
    }

    @Override
    public List<String> getClassImplementInterfaceList(String className)
        throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassImplementInterfaceList(className);
    }

    @Override
    public int getClassDepthOfInheritance(String className)
        throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassDepthOfInheritance(className);
    }

    @Override
    public int getParticipantCount(String interactionName)
        throws InteractionNotFoundException, InteractionDuplicatedException {
        return sequenceDiagram.getParticipantCount(interactionName);
    }

    @Override
    public UmlLifeline getParticipantCreator(String interactionName, String lifelineName)
        throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        return sequenceDiagram.getParticipantCreator(interactionName, lifelineName);
    }

    @Override
    public Pair<Integer, Integer> getParticipantLostAndFound(String interaction,
                                                             String lifelineName)
        throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return sequenceDiagram.getParticipantLostAndFound(interaction, lifelineName);
    }

    @Override
    public int getStateCount(String stateMachineName)
        throws StateMachineNotFoundException, StateMachineDuplicatedException {
        return stateDiagram.getStateCount(stateMachineName);
    }

    @Override
    public boolean getStateIsCriticalPoint(String stateMachineName, String stateName)
        throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        return stateDiagram.getStateIsCriticalPoint(stateMachineName, stateName);
    }

    @Override
    public List<String> getTransitionTrigger(
            String stateMachineName, String sourceStateName, String targetStateName)
        throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException,
            TransitionNotFoundException {
        return stateDiagram.getTransitionTrigger(
                stateMachineName, sourceStateName, targetStateName);
    }

    @Override
    public void checkForUml001() throws UmlRule001Exception {
        if (classDiagram.getR001()) {
            throw new UmlRule001Exception();
        }
    }

    @Override
    public void checkForUml002() throws UmlRule002Exception {
        classDiagram.checkR002();
    }

    @Override
    public void checkForUml003() throws UmlRule003Exception {
        classDiagram.checkR003();
    }

    @Override
    public void checkForUml004() throws UmlRule004Exception {
        classDiagram.checkR004();
    }

    @Override
    public void checkForUml005() throws UmlRule005Exception {
        classDiagram.checkR005();
    }

    @Override
    public void checkForUml006() throws UmlRule006Exception {
        sequenceDiagram.checkR006();
    }

    @Override
    public void checkForUml007() throws UmlRule007Exception {
        sequenceDiagram.checkR007();
    }

    @Override
    public void checkForUml008() throws UmlRule008Exception {
        stateDiagram.checkR008();
    }

    @Override
    public void checkForUml009() throws UmlRule009Exception {
        stateDiagram.checkR009();
    }
}


