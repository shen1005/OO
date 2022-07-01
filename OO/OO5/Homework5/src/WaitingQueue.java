import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class WaitingQueue {
    private ArrayList<PersonRequest> queue1 = new ArrayList<>();
    private ArrayList<PersonRequest> queue2 = new ArrayList<>();
    private ArrayList<PersonRequest> queue3 = new ArrayList<>();
    private ArrayList<PersonRequest> queue4 = new ArrayList<>();
    private ArrayList<PersonRequest> queue5 = new ArrayList<>();

    public void quit() {
        synchronized (queue1) {
            queue1.notify();
        }
        synchronized (queue2) {
            queue2.notify();
        }
        synchronized (queue3) {
            queue3.notify();
        }
        synchronized (queue4) {
            queue4.notify();
        }
        synchronized (queue5) {
            queue5.notify();
        }
    }

    public void set(PersonRequest personRequest) {
        char block = personRequest.getFromBuilding();
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

    public ArrayList<PersonRequest> getQueue1() {
        return queue1;
    }

    public ArrayList<PersonRequest> getQueue2() {
        return queue2;
    }

    public ArrayList<PersonRequest> getQueue3() {
        return queue3;
    }

    public ArrayList<PersonRequest> getQueue4() {
        return queue4;
    }

    public ArrayList<PersonRequest> getQueue5() {
        return queue5;
    }
}
