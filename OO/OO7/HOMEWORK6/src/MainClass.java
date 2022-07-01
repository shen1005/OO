import com.oocourse.TimableOutput;
import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.Request;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.ElevatorRequest;

public class MainClass {
    public static void main(String[] args) throws Exception {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        WaitingQueue waitingQueue = new WaitingQueue();
        TimableOutput.initStartTimestamp();
        while (true) {
            Request request = elevatorInput.nextRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (request == null) {
                waitingQueue.quit();
                break;
            }
            else if (request instanceof PersonRequest) {
                People people = new People((PersonRequest) request);
                waitingQueue.setRequest(people);
            }
            else {
                waitingQueue.addElevator((ElevatorRequest) request);
            }

        }
        elevatorInput.close();
    }

}
