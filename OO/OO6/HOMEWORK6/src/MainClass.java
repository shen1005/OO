import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.TimableOutput;
import com.oocourse.elevator2.Request;

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
                waitingQueue.set((PersonRequest)request);
            }
            else {
                waitingQueue.addElevator((ElevatorRequest) request);
            }

        }
        elevatorInput.close();
    }

}
