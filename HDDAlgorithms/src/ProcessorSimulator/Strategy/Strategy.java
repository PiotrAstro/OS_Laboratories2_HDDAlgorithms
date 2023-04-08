package ProcessorSimulator.Strategy;

import ProcessorSimulator.ProcessList;
import ProcessorSimulator.Processor;
import ProcessorSimulator.Process;

public interface Strategy {
    public Process getCurrentProcess();
    public void setProcessor(Processor processor);
    public void calculateCurrentProcess();
}
