package ProcessorSimulator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class ProcessList {
    private List<Process> list;
    private boolean recentlyChanged;

    public ProcessList(int numberOfProcesses, int numberOfProcessesAtStart, int arrivalTimeRange, int doingTimeRange, int mostAtPoint, int whatRangeFor70Percent) {
        if(numberOfProcesses < 0) {
            numberOfProcesses = 0;
        }
        if(numberOfProcessesAtStart < 0) {
            numberOfProcessesAtStart = 0;
        }
        list = new ArrayList<>(numberOfProcesses);

        Random random = new Random();

        for(int i = 0; i < numberOfProcessesAtStart; i++) {
            addProcess(0, doingTimeRange, mostAtPoint, whatRangeFor70Percent, random);
        }

        for(int i = numberOfProcessesAtStart; i < numberOfProcesses; i++) {
            addProcess(arrivalTimeRange, doingTimeRange, mostAtPoint, whatRangeFor70Percent, random);
        }

        recentlyChanged = false;
    }

    public ProcessList(ProcessList copyFrom) {
        list = new ArrayList<>(copyFrom.getNumberOfProcesses());
        for(int i = 0; i < copyFrom.getNumberOfProcesses(); i++) {
            list.add(copyFrom.getProcess(i).deepCopy());
        }

        recentlyChanged = false;
    }

    public ProcessList() {
        list = new ArrayList<>(0);

        recentlyChanged = false;
    }

    public void removeProcess(Process process) {
        list.remove(process);

        recentlyChanged = true;
    }

    public Process getProcess(int index) {
        return list.get(index);
    }

    public int getNumberOfProcesses() {
        return list.size();
    }

    public void addProcess(Process process) {
        list.add(process);
        recentlyChanged = true;
    }
    public void addProcess(int arrivalTimeRange, int doingTimeRange, int mostAtPoint, int whatRangeFor70Percent, Random random) {
        int arrivalTime, doingTime;

        doingTime = (int) Math.round(MathForProject.getRandomWithStandardDistribution(1, mostAtPoint, doingTimeRange, whatRangeFor70Percent, random));
        arrivalTime = (int) Math.round(random.nextDouble() * arrivalTimeRange);

        Process process = new Process(arrivalTime, doingTime);
        addProcess(process);
    }

    public void swithOfChanged() {
        recentlyChanged = false;
    }

    public boolean getRecentlyChanged() {
        return recentlyChanged;
    }
    public void saveToFile(String fileName) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for(Process process : list) {
                writer.write(process.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sortProcesses(Comparator<Process> comparator) {
        list.sort(comparator);
    }

    public static class ComparatorByArrivalTime implements Comparator<Process> {
        @Override
        public int compare(Process Process1, Process Process2) {
            if (Process1.getArrivalTime() > Process2.getArrivalTime())
                return 1;
            else if (Process1.getArrivalTime() < Process2.getArrivalTime())
                return -1;
            else
                return 0;
        }
    }

}
