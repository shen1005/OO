import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.models.common.Direction;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlParameter;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlInterface;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlInterfaceRealization;
import com.oocourse.uml3.models.elements.UmlAssociation;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class ClassDiagram {
    private final HashMap<String, MyUmlClass> myUmlClassHashMap;
    private final HashMap<String, MyUmlInterface> myUmlInterfaceHashMap;
    private final HashMap<String, MyUmlObject> myUmlObjectHashMap;
    private final HashMap<String, MyUmlClass> nameToClass;
    private final HashMap<String, Integer> classCount;
    private final HashMap<String, MyUmlOperation> myUmlOperationHashMap;
    private final HashMap<String, UmlAssociationEnd> umlAssociationEndHashMap;
    private boolean r001;

    public ClassDiagram() {
        myUmlClassHashMap = new HashMap<>();
        myUmlInterfaceHashMap = new HashMap<>();
        myUmlObjectHashMap = new HashMap<>();
        nameToClass = new HashMap<>();
        classCount = new HashMap<>();
        myUmlOperationHashMap = new HashMap<>();
        umlAssociationEndHashMap = new HashMap<>();
        r001 = false;
    }

    public void dealClass(UmlClass i) {
        MyUmlClass myUmlClass = new MyUmlClass(i);
        myUmlClassHashMap.put(i.getId(), myUmlClass);
        myUmlObjectHashMap.put(i.getId(), myUmlClass);
        if (i.getName() == null || i.getName().trim().length() == 0) {
            r001 = true;
        }
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
        if (i.getName() == null || i.getName().trim().length() == 0) {
            r001 = true;
        }
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
        if (i.getName() == null || i.getName().trim().length() == 0) {
            r001 = true;
        }
    }

    public void dealParameter(UmlParameter i) {
        MyUmlParameter temp = new MyUmlParameter(i);
        MyUmlOperation tempOperation = myUmlOperationHashMap.get(temp.getParentId());
        tempOperation.addParameter(temp);
        if (i.getDirection() != Direction.RETURN) {
            if (i.getName() == null || i.getName().trim().length() == 0) {
                r001 = true;
            }
        }
    }

    public boolean dealAttribute(UmlAttribute i) {
        if (!myUmlObjectHashMap.containsKey(i.getParentId())) {
            return false;
        }
        MyUmlAttribute myUmlAttribute = new MyUmlAttribute(i);
        MyUmlObject myUmlObject = myUmlObjectHashMap.get(myUmlAttribute.getParentId());
        myUmlObject.addAttribute(myUmlAttribute);
        if (i.getName() == null || i.getName().trim().length() == 0) {
            r001 = true;
        }
        return true;
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
        myUmlObject1.addAssociationEnd(associationEnd2);
        myUmlObject2.addAssociationEnd(associationEnd1);
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

    public boolean getR001() {
        return r001;
    }

    public void checkR002() throws UmlRule002Exception {
        Set<AttributeClassInformation> set = new HashSet<>();
        for (MyUmlClass myUmlClass: myUmlClassHashMap.values()) {
            set.addAll(myUmlClass.checkR002());
        }
        if (set.size() > 0) {
            throw new UmlRule002Exception(set);
        }
    }

    public void checkR003() throws UmlRule003Exception {
        Set<UmlClassOrInterface> set = new HashSet<>();
        for (MyUmlClass myUmlClass: myUmlClassHashMap.values()) {
            set.addAll(myUmlClass.checkR003());
        }
        for (MyUmlInterface i: myUmlInterfaceHashMap.values()) {
            set.addAll(i.checkR003());
        }
        if (set.size() > 0) {
            throw new UmlRule003Exception(set);
        }
    }

    public void checkR004() throws UmlRule004Exception {
        Set<UmlClassOrInterface> set = new HashSet<>();
        for (MyUmlInterface i : myUmlInterfaceHashMap.values()) {
            if (i.checkR004()) {
                set.add(i.getUmlInterface());
            }
        }
        if (set.size() > 0) {
            throw new UmlRule004Exception(set);
        }
    }

    public void checkR005() throws UmlRule005Exception {
        for (MyUmlInterface i: myUmlInterfaceHashMap.values()) {
            if (i.checkR005()) {
                throw new UmlRule005Exception();
            }
        }
    }
}
