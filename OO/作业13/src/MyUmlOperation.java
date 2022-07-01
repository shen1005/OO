import com.oocourse.uml1.models.common.Direction;
import com.oocourse.uml1.models.common.NameableType;
import com.oocourse.uml1.models.common.NamedType;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.common.ReferenceType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class MyUmlOperation {
    private final UmlOperation umlOperation;
    private final String name;
    private final HashMap<NameableType, Integer> parameterTypeMap;
    private final HashSet<NameableType> allSet;
    private boolean isRight;
    private final HashSet<String> set;
    private String fatherId;
    private int degree;

    public MyUmlOperation(UmlOperation umlOperation) {
        this.umlOperation = umlOperation;
        fatherId = umlOperation.getParentId();
        name = umlOperation.getName();
        parameterTypeMap = new HashMap<>();
        allSet = new HashSet<>();
        isRight = true;
        set = new HashSet<>();
        set.add("String");
        set.add("int");
        set.add("double");
        set.add("float");
        set.add("char");
        set.add("boolean");
        set.add("long");
        set.add("short");
        set.add("byte");
        degree = 0;
    }

    public UmlOperation getUmlOperation() {
        return umlOperation;
    }

    public String getId() {
        return umlOperation.getId();
    }

    public int getDegree() {
        return degree;
    }

    public String getParentId() {
        return umlOperation.getParentId();
    }

    public String getName() {
        return name;
    }

    public Visibility getVisibility() {
        return umlOperation.getVisibility();
    }

    public boolean isRight() {
        return isRight;
    }

    public void addParameter(MyUmlParameter myUmlParameter) {
        NameableType type = myUmlParameter.getType();
        if (type instanceof NamedType) {
            if (myUmlParameter.getDirection() == Direction.RETURN) {
                isRight = isRight && (set.contains(((NamedType) type).getName())
                        || ((NamedType) type).getName().equals("void"));
            }
            else {
                isRight = isRight && (set.contains(((NamedType) type).getName()));
            }
        }
        if (myUmlParameter.getDirection() == Direction.RETURN) {
            NameableType type2 = myUmlParameter.getType();
            if (type2 instanceof ReferenceType &&
                    !Objects.equals(((ReferenceType) type2).getReferenceId(), fatherId) &&
                    !allSet.contains(type2)) {
                degree++;
                allSet.add(type2);
            }
            return;
        }
        if (parameterTypeMap.containsKey(type)) {
            parameterTypeMap.put(type, parameterTypeMap.get(type) + 1);
        }
        else {
            parameterTypeMap.put(type, 1);
        }
        if (type instanceof ReferenceType &&
                !Objects.equals(((ReferenceType) type).getReferenceId(), fatherId)
                && !allSet.contains(type)) {
            degree++;
            allSet.add(type);
        }
    }

    public HashMap<NameableType, Integer> getParameterTypeMap() {
        return parameterTypeMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyUmlOperation that = (MyUmlOperation) o;
        boolean flag = true;
        if (this.parameterTypeMap.size() == that.parameterTypeMap.size()) {
            for (NameableType i : parameterTypeMap.keySet()) {
                if (!that.parameterTypeMap.containsKey(i) ||
                        !Objects.equals(that.parameterTypeMap.get(i), parameterTypeMap.get(i))) {
                    flag = false;
                    break;
                }
            }
        }
        if (this.parameterTypeMap.size() != that.parameterTypeMap.size()) {
            flag = false;
        }
        return flag && Objects.equals(name, that.name);
    }
}
