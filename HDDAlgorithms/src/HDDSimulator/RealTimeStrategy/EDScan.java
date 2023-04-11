package HDDSimulator.RealTimeStrategy;

import HDDSimulator.HDD;
import HDDSimulator.RealTimeRequest;
import HDDSimulator.Request;
import HDDSimulator.RequestList;

public class EDScan implements RealTimeStrategy{
    HDD hdd;
    @Override
    public void setHDD(HDD hdd) {
        this.hdd = hdd;
    }

    @Override
    public void doOneStep() {
        if(hdd.getRequestsInHDD().getRecentlyChanged()) {
            hdd.getRequestsInHDD().sortRequests(new RequestList.ComparatorByRealTime(new RequestList.ComparatorByArrivalTime(), new RequestList.ComparatorByDeadline()));
        }

        RealTimeRequest currentRealTimeRequest = (RealTimeRequest) hdd.getRequestsInHDD().getRequest(0);

        if(currentRealTimeRequest.getHDDNumber() > hdd.getCurrentPosition()) {
            hdd.moveRight();
        }
        else if(currentRealTimeRequest.getHDDNumber() < hdd.getCurrentPosition()) {
            hdd.moveLeft();
        }
        else {
            hdd.endRequest(currentRealTimeRequest);
            currentRealTimeRequest = null;
        }

        if(currentRealTimeRequest != null && hdd.getCurrentTime() + 1 >= currentRealTimeRequest.getDeadline()) {
            currentRealTimeRequest.setDeadlineMissed(hdd.getCurrentTime());
            hdd.endRequest(currentRealTimeRequest);
        }
    }
}
