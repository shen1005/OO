import com.oocourse.TimableOutput;

public class Output {
    public synchronized void println(String str) {
        TimableOutput.println(str);
    }

}
