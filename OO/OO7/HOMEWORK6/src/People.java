import com.oocourse.elevator3.PersonRequest;

public class People {
    private int targetFloor;
    private char targetBuilding;
    private int beforeFloor;
    private char beforeBuilding;
    private int personId;
    private final PersonRequest personRequest;

    public People(PersonRequest personRequest) {
        this.personRequest = personRequest;
        targetFloor = 1;
        targetBuilding = 'A';
        beforeBuilding = personRequest.getFromBuilding();
        beforeFloor = personRequest.getFromFloor();
        personId = personRequest.getPersonId();
    }

    public int getPersonId() {
        return personId;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    public char getBeforeBuilding() {
        return beforeBuilding;
    }

    public void setBeforeBuilding(char beforeBuilding) {
        this.beforeBuilding = beforeBuilding;
    }

    public int getBeforeFloor() {
        return beforeFloor;
    }

    public void setBeforeFloor(int beforeFloor) {
        this.beforeFloor = beforeFloor;
    }

    public void setTargetFloor(int targetFloor) {
        this.targetFloor = targetFloor;
    }

    public char getTargetBuilding() {
        return targetBuilding;
    }

    public void setTargetBuilding(char targetBuilding) {
        this.targetBuilding = targetBuilding;
    }

    public PersonRequest getPersonRequest() {
        return personRequest;
    }
}
