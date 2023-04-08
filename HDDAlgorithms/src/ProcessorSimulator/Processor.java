package ProcessorSimulator;

import MathForProject.Average;
import ProcessorSimulator.Strategy.Strategy;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Processor {
    private int selfTimer;
    private int howManySwithes;
    private ProcessList waitingProcesses, doingProcesses, doneProcesses;
    private Strategy strategy;

    public Processor(ProcessList processListCopyFrom, Strategy strategy) {
        this.strategy = strategy;
        selfTimer = 0;
        howManySwithes = 0;

        waitingProcesses = new ProcessList(processListCopyFrom);
        doingProcesses = new ProcessList();
        doneProcesses = new ProcessList();

        strategy.setProcessor(this);

        waitingProcesses.sortProcesses(new ProcessList.ComparatorByArrivalTime());
        moveProcessesFromWaitingToDoing();
    }

    public void runAllProcesses() {
        while(doingProcesses.getNumberOfProcesses() != 0 || waitingProcesses.getNumberOfProcesses() != 0) {
            if(doingProcesses.getNumberOfProcesses() != 0) {
                strategy.calculateCurrentProcess();
                strategy.getCurrentProcess().doOneStep(selfTimer);

                doingProcesses.swithOfChanged();
                moveProcessesFromDoingToDone();
            }

            selfTimer++;
            moveProcessesFromWaitingToDoing();
        }
    }

    public ProcessList getDoingList() {
        return doingProcesses;
    }

    public void moveProcessesFromWaitingToDoing() {
        for(int i = 0; i < waitingProcesses.getNumberOfProcesses(); i++) {
            if(waitingProcesses.getProcess(i).getArrivalTime() <= selfTimer) {
                Process process = waitingProcesses.getProcess(i);
                doingProcesses.addProcess(process);
                waitingProcesses.removeProcess(process);
                i--;
            }
            else {
                break;
            }
        }
    }

    public void moveProcessesFromDoingToDone() {
        Process process = strategy.getCurrentProcess();

        if(process != null && process.getTimeLeft() == 0) {
            doneProcesses.addProcess(process);
            doingProcesses.removeProcess(process);
        }
    }

    public void newSwitch() {
        howManySwithes ++;
    }

    public int getSelfTimer() {
        return selfTimer;
    }

    public void showStatistics() {
        System.out.print("\n\n");
        System.out.println(strategy.toString());
        showValue("Self timer: ", selfTimer);
        showValue("How many switches: ", howManySwithes);
        showValue("How many processes: ", doneProcesses.getNumberOfProcesses());

        Process process;
        Average averageWaitingTime = new Average();
        Average averageDoingTime = new Average();
        Average averageWaitingTimeBeforeFirstTry = new Average();
        int maximumWaitingTime = 0;
        int maximumDoingTime = 0;
        int maximumWaitingTimeBeforeFirstTry = 0;
        int waitingTimeTMP, doingTimeTMP, waitingTimeBeforeFirstTryTMP;

        for(int i = 0; i < doneProcesses.getNumberOfProcesses(); i++) {
            process = doneProcesses.getProcess(i);
            waitingTimeTMP = process.getProcessFinishTime() - process.getArrivalTime() - process.getInitialTimeLeft() + 1;
            doingTimeTMP = process.getProcessFinishTime() - process.getProcessStartTime() + 1;
            waitingTimeBeforeFirstTryTMP = process.getProcessStartTime() - process.getArrivalTime();

            averageWaitingTime.addValue(waitingTimeTMP);
            averageDoingTime.addValue(doingTimeTMP);
            averageWaitingTimeBeforeFirstTry.addValue(waitingTimeBeforeFirstTryTMP);

            if(waitingTimeTMP > maximumWaitingTime) {
                maximumWaitingTime = waitingTimeTMP;
            }
            if(doingTimeTMP > maximumDoingTime) {
                maximumDoingTime = doingTimeTMP;
            }
            if(waitingTimeBeforeFirstTryTMP > maximumWaitingTimeBeforeFirstTry) {
                maximumWaitingTimeBeforeFirstTry = waitingTimeBeforeFirstTryTMP;
            }
        }

        showValue("Average waiting time: ", averageWaitingTime.getValue());
        showValue("Average waiting time before first try: ", averageWaitingTimeBeforeFirstTry.getValue());
        showValue("Average doing time: ", averageDoingTime.getValue());
        showValue("Maximum waiting time: ", maximumWaitingTime);
        showValue("Maximum waiting time before first try: ", maximumWaitingTimeBeforeFirstTry);
        showValue("Maximum doing time: ", maximumDoingTime);
    }

    public void showValue(String text, int value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(' ');

        DecimalFormat noDecimalsFormat = new DecimalFormat();
        noDecimalsFormat.setDecimalFormatSymbols(symbols);
        noDecimalsFormat.setGroupingSize(3);
        noDecimalsFormat.setMaximumFractionDigits(0);

        System.out.println(text + noDecimalsFormat.format(value));
    }

    public void showValue(String text, double value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(' ');

        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalFormatSymbols(symbols);
        decimalFormat.setGroupingSize(3);
        decimalFormat.setMaximumFractionDigits(2);

        System.out.println(text + decimalFormat.format(value));
    }
}
