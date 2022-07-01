import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Elevator implements Runnable {
    private int floor;
    private final ArrayList<People> peoples = new ArrayList<>();
    private int direction;
    private final ArrayList<People> queue;
    private final int id;
    private final char block;
    private boolean isEnd;
    private final Output output;
    private final double speed;
    private final int capacity;
    private final WaitingQueue waitingQueue;

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public Elevator(ArrayList<People> queue, int id,
                    char block, Output output, double speed, int capacity, WaitingQueue i) {
        this.queue = queue;
        this.direction = 1;
        this.floor = 1;
        this.id = id;
        this.block = block;
        isEnd = false;
        this.output = output;
        this.speed = speed;
        this.capacity = capacity;
        waitingQueue = i;
    }

    public int strategy() { //remember not change queue and change direction
        List<Integer> list = new ArrayList<>();
        int target = -1;
        int flag = 0;
        for (People i : this.queue) {
            if ((i.getBeforeFloor() - floor) * direction >= 0) {
                flag = 1;
                break;
            }
        }
        if (peoples.size() == 0 && flag == 0) {
            direction = -1 * direction;
        }
        synchronized (this.queue) {
            if (peoples.size() != 0 && peoples.size() != capacity) {
                for (People i : this.queue) {
                    if ((i.getTargetFloor() - i.getBeforeFloor()) * direction > 0) {
                        if ((this.direction * (i.getBeforeFloor() - floor)) >= 0) {
                            list.add(i.getBeforeFloor());
                        }
                    }
                }
            }
            else if (peoples.size() != capacity) {
                for (People i : queue) {
                    if ((i.getBeforeFloor() - floor) * direction >= 0) {
                        if (direction * (i.getTargetFloor() - i.getBeforeFloor()) > 0) {
                            list.add(i.getBeforeFloor());
                        }
                    }
                }
                if (list.size() == 0) {
                    for (People i : queue) {
                        if ((i.getBeforeFloor() - floor) * direction >= 0) {
                            list.add(i.getBeforeFloor());
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
            for (People i : peoples) {
                list.add(i.getTargetFloor());
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
        ArrayList<People> temp = new ArrayList<>();
        synchronized (this.queue) {
            output.println("OPEN-" + block + "-" + floor + "-" + id);
            for (People i : peoples) {
                if (i.getTargetFloor() == floor) {
                    temp.add(i);
                    output.println("OUT-" + i.getPersonId() +
                            "-" + block + "-" + floor + "-" + id);
                }
            }
        }
        peoples.removeAll(temp);
        for (People i : temp) {
            if (i.getPersonRequest().getToBuilding() != this.block ||
                    i.getPersonRequest().getToFloor() != floor) {
                i.setBeforeBuilding(block);
                i.setBeforeFloor(floor);
                waitingQueue.set(i);
            }
        }
    }

    public void in() {
        ArrayList<People> temp2 = new ArrayList<>();
        for (People i: queue) {
            if (i.getBeforeFloor() == floor &&
                    (i.getTargetFloor() - i.getBeforeFloor()) * direction > 0) {
                temp2.add(i);
                output.println("IN-" + i.getPersonId() +
                        "-" + block + "-" + floor + "-" + id);
            }
            if (temp2.size() + peoples.size() == capacity) {
                break;
            }
        }
        int flag = 0;
        for (People i : this.queue) {
            if ((i.getBeforeFloor() - floor) * direction > 0) {
                if ((this.direction * (i.getBeforeFloor() - floor)) > 0) {
                    flag = 1;
                    break;
                }
            }
        }
        if (temp2.size() == 0 && peoples.size() == 0 && flag == 0) {
            direction = -1 * direction;
            for (People i: queue) {
                if (i.getBeforeFloor() == floor &&
                        (i.getTargetFloor() - i.getBeforeFloor()) * direction > 0) {
                    temp2.add(i);
                    output.println("IN-" + i.getPersonId() + "-"
                            + block + "-" + floor + "-" + id);
                }
                if (temp2.size() + peoples.size() == capacity) {
                    break;
                }
            }
        }
        queue.removeAll(temp2);
        peoples.addAll(temp2);
        output.println("CLOSE-" + block + "-" + floor + "-" + id);
    }

    @Override
    public void run() {
        try {
            int target;
            while (true) {
                synchronized (this.queue) {
                    if (peoples.size() == 0 && isEnd && queue.size() == 0 &&
                            WaitingQueue.getCount() == 0) {
                        break;
                    }
                    if (this.queue.size() == 0 && peoples.size() == 0) {
                        queue.wait();
                    }
                    if (peoples.size() == 0 && isEnd && queue.size() == 0 &&
                            WaitingQueue.getCount() == 0) {
                        break;
                    }
                    if (peoples.size() == 0 && queue.size() == 0) {
                        continue;
                    }
                    target = strategy();
                }
                if (this.floor != target) {
                    this.floor = this.floor + direction;
                    Thread.sleep((int)(speed * 1000));
                    output.println("ARRIVE-" + block + "-" + floor + "-" + id);
                } else {
                    out();
                    Thread.sleep(400);
                    synchronized (this.queue) {
                        in();
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        if (isEnd) {
            waitingQueue.quit();
        }
    }
}