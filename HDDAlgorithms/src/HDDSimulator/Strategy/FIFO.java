package HDDSimulator.Strategy;

import HDDSimulator.HDD;
import HDDSimulator.Request;
import HDDSimulator.RequestList;

public class FIFO implements Strategy{
    private HDD hdd;

    @Override
    public void setHDD(HDD hdd) {
        this.hdd = hdd;
    }

    @Override
    public void doOneStep() {
        if(hdd.getRequestsInHDD().getRecentlyChanged()) {
            hdd.getRequestsInHDD().sortRequests(new RequestList.ComparatorByArrivalTime());
        }
        Request currentRequest = hdd.getRequestsInHDD().getRequest(0);

        if(currentRequest.getHDDNumber() > hdd.getCurrentPosition()) {
            hdd.moveRight();
        }
        else if(currentRequest.getHDDNumber() < hdd.getCurrentPosition()) {
            hdd.moveLeft();
        }
        else {
            hdd.endRequest(currentRequest);
        }
    }

    public String toString() {
        return "FIFO";
    }
}
