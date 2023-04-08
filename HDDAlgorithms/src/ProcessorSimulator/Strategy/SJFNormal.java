package ProcessorSimulator.Strategy;

import ProcessorSimulator.Process;
import ProcessorSimulator.ProcessList;
import ProcessorSimulator.Processor;

import java.util.Comparator;

public class SJFNormal implements Strategy {
    private Processor processor;
    private Process currentProcess;


    @Override
    public void setProcessor(Processor processor) {
        this.processor = processor;
        currentProcess = null;
    }

    @Override
    public Process getCurrentProcess() {
        return currentProcess;
    }

    @Override
    public void calculateCurrentProcess() {
        if(currentProcess == null || currentProcess.getTimeLeft() == 0) {
            processor.getDoingList().sortProcesses(new ComparatorByTimeLeft());
            currentProcess = processor.getDoingList().getProcess(0);
            processor.newSwitch();
        }
    }

    public static class ComparatorByTimeLeft implements Comparator<Process> {
        @Override
        public int compare(Process Process1, Process Process2) {
            if(Process1.getTimeLeft() > Process2.getTimeLeft())
                return 1;
            else if (Process1.getTimeLeft() < Process2.getTimeLeft())
                return -1;
            else
                return 0;
        }
    }

    public String toString() {
        return "Strategy: SJFNormal";
    }
}
