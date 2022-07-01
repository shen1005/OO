import com.oocourse.elevator2.PersonRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TranElv implements Runnable {
    private char nowBlock;
    private int id;
    private final ArrayList<PersonRequest> queue;
    private int direction;
    private int floor;
    private boolean isEnd;
    private final ArrayList<PersonRequest> personRequests = new ArrayList<>();
    private final Output output;

    public TranElv(int id, int floor, ArrayList<PersonRequest> queue, Output output) {
        this.id = id;
        this.floor = floor;
        this.queue = queue;
        this.direction = 1;
        nowBlock = 'A';
        this.output = output;
    }

    public int towards(PersonRequest people) {
        int temp = (people.getToBuilding() + 5 - people.getFromBuilding()) % 5;
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

    public char strategy() { //remember not change queue and change direction
        List<Integer> list = new ArrayList<>();
        char target = 0;
        int flag = 0;
        for (PersonRequest i : this.queue) {
            if (subTowards(i.getFromBuilding(), nowBlock) * direction >= 0) {
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
                    if (towards(i) * direction > 0) {
                        if ((this.direction * subTowards(i.getFromBuilding(), nowBlock)) >= 0) {
                            list.add(distance(i.getFromBuilding(), nowBlock));
                        }
                    }
                }
            }
            else if (personRequests.size() != 6) {
                char min = queue.get(0).getFromBuilding();
                for (PersonRequest i: queue) {
                    if (abs(distance(min, nowBlock)) >
                            abs(distance(i.getFromBuilding(), nowBlock))) {
                        min = i.getFromBuilding();
                    }
                }
                if (subTowards(min, nowBlock) != 0) {
                    this.direction = subTowards(min, nowBlock);
                }
                return min;
            }
            for (PersonRequest i : personRequests) {
                list.add(distance(i.getToBuilding(), nowBlock));
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
        output.println("OPEN-" + nowBlock + "-" + floor + "-" + id);
        ArrayList<PersonRequest> temp = new ArrayList<>();
        for (PersonRequest i: personRequests) {
            if (i.getToBuilding() == nowBlock) {
                temp.add(i);
                output.println("OUT-" + i.getPersonId() +
                        "-" + nowBlock + "-" + floor + "-" + id);
            }
        }
        personRequests.removeAll(temp);
    }

    public void in() {
        ArrayList<PersonRequest> temp2 = new ArrayList<>();
        for (PersonRequest i: queue) {
            if (i.getFromBuilding() == nowBlock && towards(i) * direction > 0) {
                temp2.add(i);
                output.println("IN-" + i.getPersonId() +
                        "-" + nowBlock + "-" + floor + "-" + id);
            }
            if (temp2.size() + personRequests.size() == 6) {
                break;
            }
        }
        if (temp2.size() == 0 && personRequests.size() == 0) {
            direction = -1 * direction;
            for (PersonRequest i: queue) {
                if (i.getFromBuilding() == nowBlock &&
                        towards(i) * direction > 0) {
                    temp2.add(i);
                    output.println("IN-" + i.getPersonId() + "-"
                            + nowBlock + "-" + floor + "-" + id);
                }
                if (temp2.size() + personRequests.size() == 6) {
                    break;
                }
            }
        }
        queue.removeAll(temp2);
        personRequests.addAll(temp2);
        output.println("CLOSE-" + nowBlock + "-" + floor + "-" + id);
    }

    @Override
    public void run() {
        try {
            char target;
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
                    target = this.strategy();
                }
                if (this.nowBlock != target) {
                    this.nowBlock = (char) ((this.nowBlock  - 'A' + direction + 5) % 5 + 'A');
                    Thread.sleep(200);
                    output.println("ARRIVE-" + nowBlock + "-" + floor + "-" + id);
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
