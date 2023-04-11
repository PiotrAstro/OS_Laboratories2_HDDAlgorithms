import HDDSimulator.HDD;
import HDDSimulator.RealTimeRequest;
import HDDSimulator.RequestList;
import HDDSimulator.Strategy.*;
import HDDSimulator.RealTimeStrategy.*;

import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        // variables
        int numberOfNormalRequests = 10000;
        int numberOfRequestsAtStart = 10;
        int numberOfRealTimeRequests = 1000;
        int HDDRange = 1000;
        int arrivalTimeRange = 20000;
        int minimumDeadline = 500;
        int maximumDeadline =1500;
        Comparator<RealTimeRequest> comparatorForFDScan = new RequestList.ComparatorByDeadline();

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