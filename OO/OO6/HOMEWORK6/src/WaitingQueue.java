import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class WaitingQueue {
    private final ArrayList<PersonRequest> queue1 = new ArrayList<>();
    private final ArrayList<PersonRequest> queue2 = new ArrayList<>();
    private final ArrayList<PersonRequest> queue3 = new ArrayList<>();
    private final ArrayList<PersonRequest> queue4 = new ArrayList<>();
    private final ArrayList<PersonRequest> queue5 = new ArrayList<>();
    private final ArrayList<Elevator> elevators = new ArrayList<>();
    private final ArrayList<TranElv> tranElvs = new ArrayList<>();
    private final Output output;
    private final HashMap<String, ArrayList<PersonRequest>> map = new HashMap<>();
    private final ArrayList<ArrayList<PersonRequest>> queues = new ArrayList<>();

    public WaitingQueue() {
        Output output = new Output();
        this.output = output;
        map.put("A", queue1);
        map.put("B", queue2);
        map.put("C", queue3);
        map.put("D", queue4);
        map.put("E", queue5);
        Elevator elv1 = new Elevator(queue1, 1, 'A', output);
        Thread ele1 = new Thread(elv1);
        ele1.start();
        Elevator elv2 = new Elevator(queue2, 2, 'B', output);
        Thread ele2 = new Thread(elv2);
        ele2.start();
        Elevator elv3 = new Elevator(queue3, 3, 'C', output);
        Thread ele3 = new Thread(elv3);
        Elevator elv4 = new Elevator(queue4, 4, 'D', output);
        Thread ele4 = new Thread(elv4);
        Elevator elv5 = new Elevator(queue5, 5, 'E', output);
        Thread ele5 = new Thread(elv5);
        ele3.start();
        ele4.start();
        ele5.start();
        elevators.add(elv1);
        elevators.add(elv2);
        elevators.add(elv3);
        elevators.add(elv4);
        elevators.add(elv5);
        int i;
        for (i = 0; i < 10; i++) {
            ArrayList<PersonRequest> temp = new ArrayList<>();
            queues.add(temp);
        }
    }

    public void quit() {
        for (Elevator i: elevators) {
            i.setEnd(true);
        }
        for (TranElv i: tranElvs) {
            i.setEnd(true);
        }
        int i;
        for (i = 0; i < 10; i++) {
            synchronized (queues.get(i)) {
                queues.get(i).notifyAll();
            }
        }
        synchronized (queue1) {
            queue1.notifyAll();
        }
        synchronized (queue2) {
            queue2.notifyAll();
        }
        synchronized (queue3) {
            queue3.notifyAll();
        }
        synchronized (queue4) {
            queue4.notifyAll();
        }
        synchronized (queue5) {
            queue5.notifyAll();
        }
    }

    public void addElevator(ElevatorRequest tempEle) {
        String type = tempEle.getType();
        if (Objects.equals(type, "building")) {
            Elevator elevator = new Elevator(map.get(tempEle.getBuilding() + ""),
                    tempEle.getElevatorId(), tempEle.getBuilding(), output);
            elevators.add(elevator);
            Thread thread = new Thread(elevator);
            thread.start();
        }
        else {
            TranElv tranElv = new TranElv(tempEle.getElevatorId(), tempEle.getFloor(),
                    queues.get(tempEle.getFloor() - 1), output);
            tranElvs.add(tranElv);
            Thread thread = new Thread(tranElv);
            thread.start();
        }

    }

    public void set(PersonRequest personRequest) {
        char block = personRequest.getFromBuilding();
        if (personRequest.getFromFloor() == personRequest.getToFloor()) {
            synchronized (queues.get(personRequest.getFromFloor() - 1)) {
                queues.get(personRequest.getFromFloor() - 1).add(personRequest);
                queues.get(personRequest.getFromFloor() - 1).notifyAll();
            }
            return;
        }
        switch (block) {
            case 'A':
                synchronized (queue1) {
                    queue1.add(personRequest);
                    queue1.notify();
                    break;
                }
            case 'B':
                synchronized (queue2) {
                    queue2.add(personRequest);
                    queue2.notify();
                    break;
                }
            case 'C':
                synchronized (queue3) {
                    queue3.add(personRequest);
                    queue3.notify();
                    break;
                }
            case 'D':
                synchronized (queue4) {
                    queue4.add(personRequest);
                    queue4.notify();
                    break;
                }
            case 'E':
                synchronized (queue5) {
                    queue5.add(personRequest);
                    queue5.notify();
                    break;
                }
            default: System.out.println("not found");
        }
    }
}
