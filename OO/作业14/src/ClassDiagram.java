import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlParameter;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassDiagram {
    private final HashMap<String, MyUmlClass> myUmlClassHashMap;
    private final HashMap<String, MyUmlInterface> myUmlInterfaceHashMap;
    private final HashMap<String, MyUmlObject> myUmlObjectHashMap;
    private final HashMap<String, MyUmlClass> nameToClass;
    private final HashMap<String, Integer> classCount;
    private final HashMap<String, MyUmlOperation> myUmlOperationHashMap;
    private final HashMap<String, UmlAssociationEnd> umlAssociationEndHashMap;

    public ClassDiagram() {
        myUmlClassHashMap = new HashMap<>();
        myUmlInterfaceHashMap = new HashMap<>();
        myUmlObjectHashMap = new HashMap<>();
        nameToClass = new HashMap<>();
        classCount = new HashMap<>();
        myUmlOperationHashMap = new HashMap<>();
        umlAssociationEndHashMap = new HashMap<>();
    }

    public void dealClass(UmlClass i) {
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

    public void dealInterface(UmlInterface i) {
        MyUmlInterface myUmlInterface = new MyUmlInterface(i);
        myUmlInterfaceHashMap.put(i.getId(), myUmlInterface);
        myUmlObjectHashMap.put(i.getId(), myUmlInterface);
    }

    public HashMap<String, UmlAssociationEnd> getUmlAssociationEndHashMap() {
        return umlAssociationEndHashMap;
    }

    public void dealGeneralization(UmlGeneralization i) {
        MyUmlObject temp1 = myUmlObjectHashMap.get((i).getSource());
        MyUmlObject temp2 = myUmlObjectHashMap.get((i).getTarget());
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
        MyUmlOperation temp = new MyUmlOperation(i);
        myUmlOperationHashMap.put(temp.getId(), temp);
        MyUmlObject tempClass = myUmlObjectHashMap.get(temp.getParentId());
        tempClass.addOperation(temp);
    }

    public void dealParameter(UmlParameter i) {
        MyUmlParameter temp = new MyUmlParameter(i);
        MyUmlOperation tempOperation = myUmlOperationHashMap.get(temp.getParentId());
        tempOperation.addParameter(temp);
    }

    public void dealAttribute(UmlAttribute i) {
        if (!myUmlObjectHashMap.containsKey(i.getParentId())) {
            return;
        }
        MyUmlAttribute myUmlAttribute = new MyUmlAttribute(i);
        MyUmlObject myUmlObject = myUmlObjectHashMap.get(myUmlAttribute.getParentId());
        myUmlObject.addAttribute(myUmlAttribute);
    }

    public void dealInterfaceRealization(UmlInterfaceRealization i) {
        MyUmlClass myUmlClass = myUmlClassHashMap.get(
                (i).getSource());
        MyUmlInterface myUmlInterface = myUmlInterfaceHashMap.get(
                (i).getTarget());
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

    public int getClassCount() {
        return myUmlClassHashMap.size();
    }

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
