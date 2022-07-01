import com.oocourse.elevator3.ElevatorRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class WaitingQueue {
    private final ArrayList<People> queue1 = new ArrayList<>();
    private final ArrayList<People> queue2 = new ArrayList<>();
    private final ArrayList<People> queue3 = new ArrayList<>();
    private final ArrayList<People> queue4 = new ArrayList<>();
    private final ArrayList<People> queue5 = new ArrayList<>();
    private final ArrayList<Elevator> elevators = new ArrayList<>();
    private final ArrayList<TranElv> tranElvs = new ArrayList<>();
    private final Output output;
    private final HashMap<String, ArrayList<People>> map = new HashMap<>();
    private final ArrayList<ArrayList<People>> queues = new ArrayList<>();
    private static WaitingQueue waitingQueue;
    private static int count;
    private static final Object OBJECT = new Object();

    public static int getCount() {
        synchronized (OBJECT) {
            return count;
        }
    }

    public WaitingQueue() {
        count = 0;
        Output output = new Output();
        this.output = output;
        map.put("A", queue1);
        map.put("B", queue2);
        map.put("C", queue3);
        map.put("D", queue4);
        map.put("E", queue5);
        Elevator elv1 = new Elevator(queue1, 1, 'A', output, 0.6, 8, this);
        Thread ele1 = new Thread(elv1);
        ele1.start();
        Elevator elv2 = new Elevator(queue2, 2, 'B', output, 0.6, 8, this);
        Thread ele2 = new Thread(elv2);
        ele2.start();
        Elevator elv3 = new Elevator(queue3, 3, 'C', output, 0.6, 8, this);
        Thread ele3 = new Thread(elv3);
        Elevator elv4 = new Elevator(queue4, 4, 'D', output, 0.6, 8, this);
        Thread ele4 = new Thread(elv4);
        Elevator elv5 = new Elevator(queue5, 5, 'E', output, 0.6, 8, this);
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
            ArrayList<People> temp = new ArrayList<>();
            queues.add(temp);
        }
        TranElv elv6 = new TranElv(6, 1, queues.get(0), output, 31, 0.6, 8, this);
        Thread ele6 = new Thread(elv6);
        ele6.start();
        tranElvs.add(elv6);
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
                    tempEle.getElevatorId(), tempEle.getBuilding(),
                    output, tempEle.getSpeed(), tempEle.getCapacity(), this);
            elevators.add(elevator);
            Thread thread = new Thread(elevator);
            thread.start();
        }
        else {
            TranElv tranElv = new TranElv(tempEle.getElevatorId(), tempEle.getFloor(),
                    queues.get(tempEle.getFloor() - 1), output, tempEle.getSwitchInfo(),
                    tempEle.getSpeed(), tempEle.getCapacity(), this);
            tranElvs.add(tranElv);
            Thread thread = new Thread(tranElv);
            thread.start();
        }

    }

    public void setRequest(People people) {
        if (people.getBeforeBuilding() == people.getPersonRequest().getToBuilding()) {
            people.setTargetBuilding(people.getBeforeBuilding());
            people.setTargetFloor(people.getPersonRequest().getToFloor());
            synchronized (OBJECT) {
                count++;
            }
            set(people);
            return;
        }
        int m = 1;
        for (TranElv i: tranElvs) {
            if (((i.getMask() >> (people.getBeforeBuilding() - 'A')) & 1) +
                    ((i.getMask() >> (people.getPersonRequest().getToBuilding() - 'A')) & 1) == 2) {
                if (abs(people.getBeforeFloor() - i.getFloor()) +
                        abs(people.getPersonRequest().getToFloor() - i.getFloor()) <
                        abs(people.getBeforeFloor() - m) +
                                abs(people.getPersonRequest().getToFloor() - m)) {
                    m = i.getFloor();
                }
            }
        }
        people.setTargetFloor(m);
        people.setTargetBuilding(people.getPersonRequest().getToBuilding());
        synchronized (OBJECT) {
            if (m == people.getBeforeFloor() && m == people.getPersonRequest().getToFloor()) {
                count += 1;
            }
            else if (m == people.getBeforeFloor() || m == people.getPersonRequest().getToFloor()) {
                count += 2;
            }
            else {
                count += 3;
            }
        }
        set(people);
    }

    public int abs(int a) {
        if (a < 0) {
            return -a;
        }
        return a;
    }

    public void set(People people) {
        if (people.getBeforeFloor() == people.getTargetFloor()) {
            synchronized (queues.get(people.getBeforeFloor() - 1)) {
                queues.get(people.getBeforeFloor() - 1).add(people);
                synchronized (OBJECT) {
                    count--;
                }
                queues.get(people.getBeforeFloor() - 1).notifyAll();
            }
            return;
        }
        switch (people.getBeforeBuilding()) {
            case 'A':
                synchronized (queue1) {
                    queue1.add(people);
                    synchronized (OBJECT) {
                        count--;
                    }
                    queue1.notifyAll();
                    break;
                }
            case 'B':
                synchronized (queue2) {
                    queue2.add(people);
                    synchronized (OBJECT) {
                        count--;
                    }
                    queue2.notifyAll();
                    break;
                }
            case 'C':
                synchronized (queue3) {
                    queue3.add(people);
                    synchronized (OBJECT) {
                        count--;
                    }
                    queue3.notifyAll();
                    break;
                }
            case 'D':
                synchronized (queue4) {
                    queue4.add(people);
                    synchronized (OBJECT) {
                        count--;
                    }
                    queue4.notifyAll();
                    break;
                }
            case 'E':
                synchronized (queue5) {
                    queue5.add(people);
                    synchronized (OBJECT) {
                        count--;
                    }
                    queue5.notifyAll();
                    break;
                }
            default: System.out.println("not found");
        }
    }
}
