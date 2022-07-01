import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;
import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] args) throws Exception {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        WaitingQueue waitingQueue = new WaitingQueue();
        TimableOutput.initStartTimestamp();
        Output output = new Output();
        Elevator elv1 = new Elevator(waitingQueue.getQueue1(), 1, 'A', output);
        Thread ele1 = new Thread(elv1);
        ele1.start();
        Elevator elv2 = new Elevator(waitingQueue.getQueue2(), 2, 'B', output);
        Thread ele2 = new Thread(elv2);
        ele2.start();
        Elevator elv3 = new Elevator(waitingQueue.getQueue3(), 3, 'C', output);
        Thread ele3 = new Thread(elv3);
        Elevator elv4 = new Elevator(waitingQueue.getQueue4(), 4, 'D', output);
        Thread ele4 = new Thread(elv4);
        Elevator elv5 = new Elevator(waitingQueue.getQueue5(), 5, 'E', output);
        Thread ele5 = new Thread(elv5);
        ele3.start();
        ele4.start();
        ele5.start();
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (request == null) {
                elv1.setEnd(true);
                elv2.setEnd(true);
                elv3.setEnd(true);
                elv4.setEnd(true);
                elv5.setEnd(true);
                waitingQueue.quit();
                break;
            }
            else {
                waitingQueue.set(request);
            }
        }
        elevatorInput.close();
    }

}
