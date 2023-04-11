import HDDSimulator.HDD;
import HDDSimulator.RequestList;
import HDDSimulator.Strategy.CScan;
import HDDSimulator.Strategy.FIFO;
import HDDSimulator.Strategy.SSTF;
import HDDSimulator.Strategy.Scan;

public class Main {
    public static void main(String[] args) {
        // variables
        int numberOfNormalRequests = 100;
        int numberOfRequestsAtStart = 10;
        int numberOfRealTimeRequests = 10;
        int HDDRange = 1000;
        int arrivalTimeRange = 1000;
        int minimumDeadline = 100;
        int maximumDeadline = 1000;

        // create RequestList
        RequestList requestList = new RequestList(
                HDDRange,
                numberOfNormalRequests,
                numberOfRequestsAtStart,
                arrivalTimeRange,
                numberOfRealTimeRequests,
                minimumDeadline,
                maximumDeadline);

        // HDDSimulator
        HDD hddSimulator;

        // FIFO no RealTime
        hddSimulator = new HDD(HDDRange, requestList, new FIFO());
        hddSimulator.process();
        hddSimulator.showStatistics();

        // SSTF no RealTime
        hddSimulator = new HDD(HDDRange, requestList, new SSTF());
        hddSimulator.process();
        hddSimulator.showStatistics();

        // Scan no RealTime
        hddSimulator = new HDD(HDDRange, requestList, new Scan());
        hddSimulator.process();
        hddSimulator.showStatistics();

        // CScan no RealTime
        hddSimulator = new HDD(HDDRange, requestList, new CScan());
        hddSimulator.process();
        hddSimulator.showStatistics();
    }
}