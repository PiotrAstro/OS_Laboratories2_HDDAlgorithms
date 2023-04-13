import HDDSimulator.*;
import HDDSimulator.Strategy.*;
import HDDSimulator.RealTimeStrategy.*;

import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        // variables
        int numberOfNormalRequests = 1000;
        int numberOfRequestsAtStart = 0;
        int numberOfRealTimeRequests = 100;
        int HDDRange = 5000;
        int arrivalTimeRange = 8000000;
        int minimumDeadline = 5000;
        int maximumDeadline =50000;
        RequestComparator<? super RealTimeRequest> comparatorForFDScan = new RequestList.ComparatorByArrivalTime();



        // create RequestList
        RequestList requestList = new RequestList(
                HDDRange,
                numberOfNormalRequests,
                numberOfRequestsAtStart,
                arrivalTimeRange,
                numberOfRealTimeRequests,
                minimumDeadline,
                maximumDeadline);
//        requestList.addRequest(new Request(0,4999));


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



        // CScan with EDF
        hddSimulator = new HDD(HDDRange, requestList, new CScan(), new EDF());
        hddSimulator.process();
        hddSimulator.showStatistics();

        // CScan with FDScan
        hddSimulator = new HDD(HDDRange, requestList, new CScan(), new FDScan(comparatorForFDScan));
        hddSimulator.process();
        hddSimulator.showStatistics();
    }
}