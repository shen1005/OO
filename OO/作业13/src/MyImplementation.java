import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml1.interact.format.UserApi;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyImplementation implements UserApi {
    private final HashMap<String, MyUmlClass> myUmlClassHashMap;
    private final HashMap<String, MyUmlInterface> myUmlInterfaceHashMap;
    private final HashMap<String, MyUmlObject> myUmlObjectHashMap;
    private final HashMap<String, MyUmlClass> nameToClass;
    private final HashMap<String, Integer> classCount;
    private final HashMap<String, MyUmlOperation> myUmlOperationHashMap;
    private final HashMap<String, UmlAssociationEnd> umlAssociationEndHashMap;

    public MyImplementation(UmlElement[] elements) {
        myUmlClassHashMap = new HashMap<>();
        myUmlInterfaceHashMap = new HashMap<>();
        myUmlObjectHashMap = new HashMap<>();
        nameToClass = new HashMap<>();
        classCount = new HashMap<>();
        myUmlOperationHashMap = new HashMap<>();
        umlAssociationEndHashMap = new HashMap<>();
        for (UmlElement i: elements) {
            if (i instanceof UmlClass) {
                dealClass((UmlClass) i);
            }
            if (i instanceof UmlInterface) {
                dealInterface((UmlInterface) i);
            }
            if (i instanceof UmlAssociationEnd) {
                umlAssociationEndHashMap.put(i.getId(), (UmlAssociationEnd) i);
            }
        }
        for (UmlElement i: elements) {
            if (i instanceof UmlGeneralization) {
                dealGeneralization((UmlGeneralization) i);
            }
            if (i instanceof UmlOperation) {
                dealOperation((UmlOperation) i);
            }
            if (i instanceof UmlAttribute) {
                dealAttribute((UmlAttribute) i);
            }
            if (i instanceof UmlInterfaceRealization) {
                dealInterfaceRealization((UmlInterfaceRealization) i);
            }
            if (i instanceof UmlAssociation) {
                dealAssociation((UmlAssociation) i);
            }
        }
        for (UmlElement i: elements) {
            if (i instanceof UmlParameter) {
                dealParameter((UmlParameter) i);
            }
        }
    }

    private void dealClass(UmlClass i) {
        MyUmlClass myUmlClass = new MyUmlClass(i);
        myUmlClassHashMap.put(i.getId(), myUmlClass);
        myUmlObjectHashMap.put(i.getId(), myUmlClass);
        if (myUmlClass.getName() != null) {
            if (nameToClass.containsKey(myUmlClass.getName())) {
                classCount.put(myUmlClass.getName(), classCount.get(myUmlClass.getName()) + 1);
            }
            else {
                classCount.put(myUmlClass.getName(), 1);
                nameToClass.put(myUmlClass.getName(), myUmlClass);
            }
        }
    }

    private void dealInterface(UmlInterface i) {
        MyUmlInterface myUmlInterface = new MyUmlInterface(i);
        myUmlInterfaceHashMap.put(i.getId(), myUmlInterface);
        myUmlObjectHashMap.put(i.getId(), myUmlInterface);
    }

    private void dealGeneralization(UmlGeneralization i) {
        MyUmlObject temp1 = myUmlObjectHashMap.get(((UmlGeneralization) i).getSource());
        MyUmlObject temp2 = myUmlObjectHashMap.get(((UmlGeneralization) i).getTarget());
        if (temp1 instanceof MyUmlClass) {
            ((MyUmlClass) temp1).setFather((MyUmlClass) temp2);
            ((MyUmlClass) temp2).addSon((MyUmlClass) temp1);
        }
        if (temp1 instanceof MyUmlInterface) {
            ((MyUmlInterface) temp1).setFather((MyUmlInterface) temp2);
            ((MyUmlInterface) temp2).addSon((MyUmlInterface) temp1);
        }
    }

    public void dealOperation(UmlOperation i) {
        MyUmlOperation temp = new MyUmlOperation((UmlOperation) i);
        myUmlOperationHashMap.put(temp.getId(), temp);
        MyUmlObject tempClass = myUmlObjectHashMap.get(temp.getParentId());
        tempClass.addOperation(temp);
    }

    public void dealParameter(UmlParameter i) {
        MyUmlParameter temp = new MyUmlParameter((UmlParameter) i);
        MyUmlOperation tempOperation = myUmlOperationHashMap.get(temp.getParentId());
        tempOperation.addParameter(temp);
    }

    public void dealAttribute(UmlAttribute i) {
        MyUmlAttribute myUmlAttribute = new MyUmlAttribute((UmlAttribute) i);
        MyUmlObject myUmlObject = myUmlObjectHashMap.get(myUmlAttribute.getParentId());
        myUmlObject.addAttribute(myUmlAttribute);
    }

    public void dealInterfaceRealization(UmlInterfaceRealization i) {
        MyUmlClass myUmlClass = myUmlClassHashMap.get(
                ((UmlInterfaceRealization) i).getSource());
        MyUmlInterface myUmlInterface = myUmlInterfaceHashMap.get(
                ((UmlInterfaceRealization) i).getTarget());
        myUmlClass.addInterface(myUmlInterface);
    }

    public void dealAssociation(UmlAssociation i) {
        UmlAssociationEnd associationEnd1 = umlAssociationEndHashMap.get(i.getEnd1());
        UmlAssociationEnd associationEnd2 = umlAssociationEndHashMap.get(i.getEnd2());
        MyUmlObject myUmlObject1 = myUmlObjectHashMap.get(associationEnd1.getReference());
        MyUmlObject myUmlObject2 = myUmlObjectHashMap.get(associationEnd2.getReference());
        myUmlObject1.addAssociation(myUmlObject2);
        myUmlObject2.addAssociation(myUmlObject1);
    }

    @Override
    public int getClassCount() {
        return myUmlClassHashMap.size();
    }

    @Override
    public int getClassSubClassCount(String className) throws
            ClassNotFoundException, ClassDuplicatedException {
        if (!nameToClass.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (classCount.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        }
        return nameToClass.get(className).getSon().size();
    }

    @Override
    public int getClassOperationCount(String className)
        throws ClassNotFoundException, ClassDuplicatedException {
        if (!nameToClass.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (classCount.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        }
        return nameToClass.get(className).getOperation().size();
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String className, String methodName)
        throws ClassNotFoundException, ClassDuplicatedException {
        if (!nameToClass.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (classCount.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        }
        return nameToClass.get(className).getOperationVisibility(methodName);
    }

    @Override
    public List<Integer> getClassOperationCouplingDegree(String className, String methodName)
        throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        if (!nameToClass.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (classCount.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        }
        List<Integer> list = nameToClass.get(className).getOperationDegree(methodName);
        return list;
    }

    @Override
    public int getClassAttributeCouplingDegree(String className)
        throws ClassNotFoundException, ClassDuplicatedException {
        if (!nameToClass.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (classCount.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        }
        return nameToClass.get(className).getAttributeDegree();
    }

    @Override
    public List<String> getClassImplementInterfaceList(String className)
        throws ClassNotFoundException, ClassDuplicatedException {
        if (!nameToClass.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (classCount.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        }
        return new ArrayList<>(nameToClass.get(className).getAllInterface().keySet());
    }

    @Override
    public int getClassDepthOfInheritance(String className)
        throws ClassNotFoundException, ClassDuplicatedException {
        if (!nameToClass.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (classCount.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        }
        return nameToClass.get(className).getDepth();
    }
}

