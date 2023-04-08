package HDDSimulator;

import HDDSimulator.RealTimeStrategy.RealTimeStrategy;
import HDDSimulator.Strategy.Strategy;

public class HDD {
    private RequestList waitingRequests, finishedRequests, requestsInHDD;
    private int HDDRange, currentTime;
    Strategy strategy;
    RealTimeStrategy realTimeStrategy;


    public HDD(int HDDRange, RequestList requests, Strategy strategy) {
        waitingRequests = new RequestList(requests);
        finishedRequests = new RequestList();
        requestsInHDD = new RequestList();
        this.HDDRange = HDDRange;
        this.strategy = strategy;
        currentTime = 0;

        waitingRequests.sortRequests(new RequestList.ComparatorByArrivalTime());
    }

    public HDD(int HDDRange, RequestList requests, Strategy strategy, RealTimeStrategy realTimeStrategy) {
        this(HDDRange, requests, strategy);
        this.realTimeStrategy = realTimeStrategy;
    }

    // write Processing method that will call strategy.doOneStep() and realTimeStrategy.doOneStep() if it is not null, it will process requestsInHDD and waitingRequests until it is not clear
    public void process() {
        while(!waitingRequests.isEmpty() || !requestsInHDD.isEmpty()) {
            if(!requestsInHDD.isEmpty()) {
                strategy.doOneStep();
                realTimeStrategy.doOneStep();
                requestsInHDD.switchOfChanged();
                moveRequestsFromHDDToFinished();
            }
            currentTime++;
            moveRequestsFromWaitingToHDD();
        }
    }



    public RequestList getWaitingRequests() {
        return waitingRequests;
    }
    public RequestList getFinishedRequests() {
        return finishedRequests;
    }
    public RequestList getRequestsInHDD() {
        return requestsInHDD;
    }
    public int getHDDRange() {
        return HDDRange;
    }
    public int getCurrentTime() {
        return currentTime;
    }
}
