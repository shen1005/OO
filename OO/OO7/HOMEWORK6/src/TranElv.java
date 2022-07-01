import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TranElv implements Runnable {
    private char nowBlock;
    private int id;
    private final ArrayList<People> queue;
    private int direction;
    private int floor;
    private boolean isEnd;
    private final ArrayList<People> peoples = new ArrayList<>();
    private final Output output;
    private final int mask;
    private final double speed;
    private final int capacity;
    private final WaitingQueue waitingQueue;

    public TranElv(int id, int floor, ArrayList<People> queue, Output output,
                   int mask, double speed, int capacity, WaitingQueue waitingQueue) {
        this.id = id;
        this.floor = floor;
        this.queue = queue;
        this.direction = 1;
        nowBlock = 'A';
        this.output = output;
        this.mask = mask;
        this.speed = speed;
        this.capacity = capacity;
        this.waitingQueue = waitingQueue;
    }

    public int towards(People people) {
        int temp = (people.getTargetBuilding() + 5 - people.getBeforeBuilding()) % 5;
        if (temp > 2) {
            return -1;
        }
        else {
            return 1;
        }
    }

    public int subTowards(char target, char now) {
        int temp = (target + 5 - now) % 5;
        if (temp > 2) {
            return -1;
        }
        else if (temp != 0) {
            return 1;
        }
        else {
            return 0;
        }
    }

    public int distance(char target, char now) {
        int temp = (target + 5 - now) % 5;
        if (temp > 2) {
            return temp - 5;
        }
        else {
            return temp;
        }
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public int abs(int a) {
        if (a < 0) {
            return -a;
        }
        return a;
    }

    public int getMask() {
        return mask;
    }

    public int getFloor() {
        return floor;
    }

    public char noPeople() {
        char min = 'A';
        for (People i: queue) {
            if (((mask >> (i.getBeforeBuilding() - 'A')) & 1) +
                    ((mask >> (i.getTargetBuilding() - 'A')) & 1) == 2) {
                min = i.getBeforeBuilding();
                break;
            }
        }
        for (People i: queue) {
            if (abs(distance(min, nowBlock)) >
                    abs(distance(i.getBeforeBuilding(), nowBlock))) {
                if (((mask >> (i.getBeforeBuilding() - 'A')) & 1) +
                        ((mask >> (i.getTargetBuilding() - 'A')) & 1) == 2) {
                    min = i.getBeforeBuilding();
                }
            }
        }
        if (subTowards(min, nowBlock) != 0) {
            this.direction = subTowards(min, nowBlock);
        }
        return min;
    }

    public char strategy() { //remember not change queue and change direction
        List<Integer> list = new ArrayList<>();
        char target = 0;
        int flag = 0;
        for (People i : this.queue) {
            if (subTowards(i.getBeforeBuilding(), nowBlock) * direction >= 0) {
                if (((mask >> (i.getBeforeBuilding() - 'A')) & 1) +
                        ((mask >> (i.getTargetBuilding() - 'A')) & 1) == 2) {
                    flag++;
                    break;
                }
            }
        }
        if (peoples.size() == 0 && flag == 0) {
            direction = -1 * direction;
        }
        synchronized (this.queue) {
            if (peoples.size() != 0 && peoples.size() != capacity) {
                for (People i : this.queue) {
                    if (towards(i) * direction > 0) {
                        if ((this.direction * subTowards(i.getBeforeBuilding(), nowBlock)) >= 0) {
                            if (((mask >> (i.getBeforeBuilding() - 'A')) & 1) +
                                    ((mask >> (i.getTargetBuilding() - 'A')) & 1) == 2) {
                                list.add(distance(i.getBeforeBuilding(), nowBlock));
                            }
                        }
                    }
                }
            }
            else if (peoples.size() != capacity) {
                return noPeople();
            }
            for (People i : peoples) {
                list.add(distance(i.getTargetBuilding(), nowBlock));
            }
            Collections.sort(list);
            if (this.direction > 0) {
                target = (char) ((nowBlock - 'A' + list.get(0) + 5) % 5 + 'A');
            }
            if (this.direction < 0) {
                target = (char) ((nowBlock - 'A' + list.get(list.size() - 1) + 5) % 5 + 'A');
            }
        }
        return target;
    }

    public void out() {
        ArrayList<People> temp = new ArrayList<>();
        synchronized (this.queue) {
            output.println("OPEN-" + nowBlock + "-" + floor + "-" + id);
            for (People i : peoples) {
                if (i.getTargetBuilding() == nowBlock) {
                    temp.add(i);
                    output.println("OUT-" + i.getPersonId() +
                            "-" + nowBlock + "-" + floor + "-" + id);
                }
            }
        }
        peoples.removeAll(temp);
        for (People i: temp) {
            if (i.getPersonRequest().getToBuilding() != nowBlock ||
                    i.getPersonRequest().getToFloor() != floor) {
                i.setBeforeBuilding(nowBlock);
                i.setBeforeFloor(floor);
                i.setTargetFloor(i.getPersonRequest().getToFloor());
                waitingQueue.set(i);
            }
        }
    }

    public void in() {
        ArrayList<People> temp2 = new ArrayList<>();
        for (People i: queue) {
            if (i.getBeforeBuilding() == nowBlock && towards(i) * direction > 0) {
                if (((mask >> (i.getBeforeBuilding() - 'A')) & 1) +
                        ((mask >> (i.getTargetBuilding() - 'A')) & 1) == 2) {
                    temp2.add(i);
                    output.println("IN-" + i.getPersonId() +
                            "-" + nowBlock + "-" + floor + "-" + id);
                }
            }
            if (temp2.size() + peoples.size() == capacity) {
                break;
            }
        }
        if (temp2.size() == 0 && peoples.size() == 0) {
            direction = -1 * direction;
            for (People i: queue) {
                if (i.getBeforeBuilding() == nowBlock && towards(i) * direction > 0) {
                    if (((mask >> (i.getBeforeBuilding() - 'A')) & 1) +
                            ((mask >> (i.getTargetBuilding() - 'A')) & 1) == 2) {
                        temp2.add(i);
                        output.println("IN-" + i.getPersonId() + "-"
                                + nowBlock + "-" + floor + "-" + id);
                    }
                }
                if (temp2.size() + peoples.size() == capacity) {
                    break;
                }
            }
        }
        queue.removeAll(temp2);
        peoples.addAll(temp2);
        output.println("CLOSE-" + nowBlock + "-" + floor + "-" + id);
    }

    @Override
    public void run() {
        try {
            char target;
            while (true) {
                synchronized (this.queue) {
                    int temp = 0;
                    for (People i: queue) {
                        if (((mask >> (i.getBeforeBuilding() - 'A')) & 1) +
                                ((mask >> (i.getTargetBuilding() - 'A')) & 1) == 2) {
                            temp++;
                            break;
                        }
                    }
                    if (peoples.size() == 0 && isEnd && temp == 0 &&
                            WaitingQueue.getCount() == 0) {
                        break;
                    }
                    if (peoples.size() == 0 && temp == 0) {
                        queue.wait();
                    }
                    temp = 0;
                    for (People i: queue) {
                        if (((mask >> (i.getBeforeBuilding() - 'A')) & 1) +
                                ((mask >> (i.getTargetBuilding() - 'A')) & 1) == 2) {
                            temp++;
                            break;
                        }
                    }
                    if (peoples.size() == 0 && isEnd && temp == 0 &&
                            WaitingQueue.getCount() == 0) {
                        break;
                    }
                    if (peoples.size() == 0 && temp == 0) {
                        continue;
                    }
                    target = this.strategy();
                }
                if (this.nowBlock != target) {
                    this.nowBlock = (char) ((this.nowBlock  - 'A' + direction + 5) % 5 + 'A');
                    Thread.sleep((int)(speed * 1000));
                    output.println("ARRIVE-" + nowBlock + "-" + floor + "-" + id);
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
            System.out.println("Found");
        }
        if (isEnd) {
            waitingQueue.quit();
        }
    }

}
