package HDDSimulator;

import HDDSimulator.RealTimeStrategy.RealTimeStrategy;
import HDDSimulator.Strategy.Strategy;
import MathForProject.Average;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class HDD {
    private RequestList waitingRequests, finishedRequests, requestsInHDD;
    private int HDDRange, currentPosition, currentTime, directionChangesCounter;
    private boolean isDirectionRight;
    Strategy strategy;
    RealTimeStrategy realTimeStrategy;


    public HDD(int HDDRange, int initialPosition, RequestList requests, Strategy strategy) {
        if(initialPosition < 0 || initialPosition > HDDRange)
            initialPosition = 0;
        currentPosition = initialPosition;
        directionChangesCounter = 0;
        isDirectionRight = true;
        waitingRequests = new RequestList(requests);
        finishedRequests = new RequestList();
        requestsInHDD = new RequestList();
        this.HDDRange = HDDRange;
        this.strategy = strategy;
        this.strategy.setHDD(this);
        if(realTimeStrategy != null)
            this.realTimeStrategy.setHDD(this);
        currentTime = 0;

        waitingRequests.sortRequests(new RequestList.ComparatorByArrivalTime());
    }

    public HDD(int HDDRange, int initialPosition, RequestList requests, Strategy strategy, RealTimeStrategy realTimeStrategy) {
        this(HDDRange, requests, strategy);
        this.realTimeStrategy = realTimeStrategy;
    }

    public HDD(int HDDRange, RequestList requests, Strategy strategy) {
        this(HDDRange, 0, requests, strategy);
    }

    // write Processing method that will call strategy.doOneStep() and realTimeStrategy.doOneStep() if it is not null, it will process requestsInHDD and waitingRequests until it is not clear
    public void process() {
        while(!waitingRequests.isEmpty() || !requestsInHDD.isEmpty()) {
            if(!requestsInHDD.isEmpty()) {
                if(realTimeStrategy != null && requestsInHDD.getRecentlyChanged() && requestsInHDD.hasRealTimeRequests()) {
                    realTimeStrategy.doOneStep();
                }
                else {
                    strategy.doOneStep();
                }
                requestsInHDD.swithOfChanged();
            }
            currentTime++;
            moveRequestsFromWaitingToDoing();
        }
    }

    public void moveRequestsFromWaitingToDoing() {
        for(int i = 0; i < waitingRequests.getNumberOfRequests(); i++) {
            if(waitingRequests.getRequest(i).getArrivalTime() <= currentTime) {
                Request request = waitingRequests.getRequest(i);
                requestsInHDD.addRequest(request);
                waitingRequests.removeRequest(request);
                i--;
            }
            else {
                break;
            }
        }
    }


    public void endRequest(Request request) {
        request.setFinishTime(currentTime);
        finishedRequests.addRequest(request);
        requestsInHDD.removeRequest(request);
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
    public int getCurrentPosition() {
        return currentPosition;
    }
    public int getDirectionChangesCounter() {
        return directionChangesCounter;
    }
    public void setCurrentPosition(int currentPosition) {
        if(currentPosition < 0)
            currentPosition = 0;
        else if(currentPosition >= HDDRange)
            currentPosition = HDDRange - 1;

        if(!isDirectionRight() && this.currentPosition < currentPosition) {
            setDirectionRight(true);
        }
        else if(isDirectionRight() && this.currentPosition > currentPosition) {
            setDirectionRight(false);
        }
        this.currentPosition = currentPosition;
    }
    public void moveRight() {
        setCurrentPosition(currentPosition + 1);
    }
    public void moveLeft() {
        setCurrentPosition(currentPosition - 1);
    }


    public boolean isDirectionRight() {
        return isDirectionRight;
    }
    public void setDirectionRight(boolean isDirectionRight) {
        if(this.isDirectionRight != isDirectionRight)
            directionChangesCounter++;

        this.isDirectionRight = isDirectionRight;
    }


    public void showStatistics() {
        System.out.println("\n\n");
        System.out.println("HDD statistics:");
        System.out.println("Strategy: " + strategy.toString());
        if(realTimeStrategy != null)
            System.out.println("Real time strategy: " + realTimeStrategy.toString());
        showValue("Self Time: ", currentTime);

        int maximumWaitingTime = 0;
        Average averageWaitingTime = new Average();
        int numberOfDeadRealTimeRequests = 0;
        int numberOfServedRealTimeRequests = 0;

        for (Request request : finishedRequests) {
            int waitingTime = request.getFinishTime() - request.getArrivalTime();
            averageWaitingTime.addValue(waitingTime);

            if(waitingTime > maximumWaitingTime)
                maximumWaitingTime = waitingTime;

            if(realTimeStrategy != null && request instanceof RealTimeRequest) {
                RealTimeRequest realTimeRequest = (RealTimeRequest) request;
                if(realTimeRequest.isDeadlineMissed())
                    numberOfDeadRealTimeRequests++;
                else
                    numberOfServedRealTimeRequests++;
            }
        }

        showValue("Maximum waiting time: ", maximumWaitingTime);
        showValue("Average waiting time: ", averageWaitingTime.getValue());

        if(realTimeStrategy != null) {
            showValue("Number of dead real time requests: ", numberOfDeadRealTimeRequests);
            showValue("Number of served real time requests: ", numberOfServedRealTimeRequests);
        }
        showValue("Number of direction changes: ", directionChangesCounter);
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
