import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Elevator implements Runnable {
    private int floor;
    private ArrayList<PersonRequest> personRequests = new ArrayList<>();
    private int direction;
    private ArrayList<PersonRequest> queue;
    private final int id;
    private final char block;
    private boolean isEnd;
    private final Output output;

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Elevator(ArrayList<PersonRequest> queue, int id, char block, Output output) {
        this.queue = queue;
        this.direction = 1;
        this.floor = 1;
        this.id = id;
        this.block = block;
        isEnd = false;
        this.output = output;
    }

    public int strategy() { //remember not change queue and change direction
        List<Integer> list = new ArrayList<>();
        int target = -1;
        int flag = 0;
        for (PersonRequest i : this.queue) {
            if ((i.getFromFloor() - floor) * direction >= 0) {
                flag = 1;
                break;
            }
        }
        if (personRequests.size() == 0 && flag == 0) {
            direction = -1 * direction;
        }
        synchronized (this.queue) {
            if (personRequests.size() != 0 && personRequests.size() != 6) {
                for (PersonRequest i : this.queue) {
                    if ((i.getToFloor() - i.getFromFloor()) * direction > 0) {
                        if ((this.direction * (i.getFromFloor() - floor)) >= 0) {
                            list.add(i.getFromFloor());
                        }
                    }
                }
            }
            else if (personRequests.size() != 6) {
                for (PersonRequest i : queue) {
                    if ((i.getFromFloor() - floor) * direction >= 0) {
                        if (direction * (i.getToFloor() - i.getFromFloor()) > 0) {
                            list.add(i.getFromFloor());
                        }
                    }
                }
                if (list.size() == 0) {
                    for (PersonRequest i : queue) {
                        if ((i.getFromFloor() - floor) * direction >= 0) {
                            list.add(i.getFromFloor());
                        }
                    }
                    Collections.sort(list);
                    if (direction > 0) {
                        return list.get(list.size() - 1);
                    }
                    else {
                        return list.get(0);
                    }
                }
            }
            for (PersonRequest i : personRequests) {
                list.add(i.getToFloor());
            }
            Collections.sort(list);
            if (this.direction > 0) {
                target = list.get(0);
            }
            if (this.direction < 0) {
                target = list.get(list.size() - 1);
            }
        }
        return target;
    }

    public void out() throws Exception {
        output.println("OPEN-" + block + "-" + floor + "-" + id);
        ArrayList<PersonRequest> temp = new ArrayList<>();
        for (PersonRequest i: personRequests) {
            if (i.getToFloor() == floor) {
                temp.add(i);
                output.println("OUT-" + i.getPersonId() +
                        "-" + block + "-" + floor + "-" + id);
            }
        }
        personRequests.removeAll(temp);
    }

    public void in() {
        ArrayList<PersonRequest> temp2 = new ArrayList<>();
        for (PersonRequest i: queue) {
            if (i.getFromFloor() == floor && (i.getToFloor() - i.getFromFloor()) * direction > 0) {
                temp2.add(i);
                output.println("IN-" + i.getPersonId() +
                        "-" + block + "-" + floor + "-" + id);
            }
            if (temp2.size() + personRequests.size() == 6) {
                break;
            }
        }
        int flag = 0;
        for (PersonRequest i : this.queue) {
            if ((i.getFromFloor() - floor) * direction > 0) {
                if ((this.direction * (i.getFromFloor() - floor)) > 0) {
                    flag = 1;
                    break;
                }
            }
        }
        if (temp2.size() == 0 && personRequests.size() == 0 && flag == 0) {
            direction = -1 * direction;
            for (PersonRequest i: queue) {
                if (i.getFromFloor() == floor &&
                        (i.getToFloor() - i.getFromFloor()) * direction > 0) {
                    temp2.add(i);
                    output.println("IN-" + i.getPersonId() + "-"
                            + block + "-" + floor + "-" + id);
                }
                if (temp2.size() + personRequests.size() == 6) {
                    break;
                }
            }
        }
        queue.removeAll(temp2);
        personRequests.addAll(temp2);
        output.println("CLOSE-" + block + "-" + floor + "-" + id);
    }

    @Override
    public void run() {
        try {
            int target;
            while (true) {
                synchronized (this.queue) {
                    if (personRequests.size() == 0 && isEnd && queue.size() == 0) {
                        break;
                    }
                    if (this.queue.size() == 0 && personRequests.size() == 0) {
                        queue.wait();
                    }
                    if (personRequests.size() == 0 && isEnd && queue.size() == 0) {
                        break;
                    }
                    target = strategy();
                }
                if (this.floor != target) {
                    this.floor = this.floor + direction;
                    Thread.sleep(400);
                    output.println("ARRIVE-" + block + "-" + floor + "-" + id);
                } else {
                    synchronized (this.queue) {
                        out();
                    }
                    Thread.sleep(400);
                    synchronized (this.queue) {
                        in();
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println("Exception Found");
        }
    }
}