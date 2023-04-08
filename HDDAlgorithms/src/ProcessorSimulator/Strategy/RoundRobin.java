package ProcessorSimulator.Strategy;

import ProcessorSimulator.Process;
import ProcessorSimulator.Processor;

import java.util.Comparator;

public class RoundRobin implements Strategy {
    private Processor processor;
    private int timeQuant;
    private int currentTimeInQuant;
    private int currentIndexInList;
    private Process previousProcess;
    private Process currentProcess;

    public RoundRobin(int timeQuant) {
        if (timeQuant < 0) {
            timeQuant = 1;
        }

        this.timeQuant = timeQuant;
    }

    @Override
    public Process getCurrentProcess() {
        return currentProcess;
    }


    @Override
    public void setProcessor(Processor processor) {
        this.processor = processor;
        currentTimeInQuant = 0;
        currentIndexInList = 0;
        previousProcess = null;
    }

    @Override
    public void calculateCurrentProcess() {
        if(currentIndexInList >= processor.getDoingList().getNumberOfProcesses()) {
            currentIndexInList = 0;
            currentTimeInQuant = 0;
        }

        currentProcess = processor.getDoingList().getProcess(currentIndexInList);

        currentTimeInQuant ++;
        if(currentProcess.getTimeLeft() == 1) {
            currentTimeInQuant = 0;
            // index in list should not change in this case, because this place will have new process - current one will be removed
        }
        else if(currentTimeInQuant == timeQuant) {
            currentTimeInQuant = 0;
            currentIndexInList ++;
        }

        if(currentProcess != previousProcess) {
            processor.newSwitch();
            previousProcess = currentProcess;
        }
    }

    public String toString() {
        return "Strategy: RoundRobin";
    }
}
