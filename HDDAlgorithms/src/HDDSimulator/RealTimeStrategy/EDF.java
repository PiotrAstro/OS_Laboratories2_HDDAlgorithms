package HDDSimulator.RealTimeStrategy;

import HDDSimulator.*;

public class EDF implements RealTimeStrategy{
    private HDD hdd;
    private RealTimeRequest currentRealTimeRequest;
    private RequestComparator<RealTimeRequest> realTimeComparator;

    public EDF() {
        currentRealTimeRequest = null;
        realTimeComparator = new RequestList.ComparatorByLeftDeadline();
    }

    @Override
    public void setHDD(HDD hdd) {
        this.hdd = hdd;
        realTimeComparator.setHDD(hdd);
    }

    @Override
    public void doOneStep() {
        if(currentRealTimeRequest != null && !currentRealTimeRequest.isAchieveable(hdd.getCurrentTime())) {
            removeMissedDeadline(currentRealTimeRequest);
            currentRealTimeRequest = null;
        }

        if(hdd.getRequestsInHDD().getRecentlyChanged()) {
            hdd.getRequestsInHDD().sortRequests(new RequestList.ComparatorByRealTime(new RequestList.ComparatorByArrivalTime(), realTimeComparator));
        }

        if(currentRealTimeRequest == null) {
            int numberOfRealTimeRequests = hdd.getRequestsInHDD().getNumberOfRealTimeRequests();

            for(int i = 0; i < numberOfRealTimeRequests; i++) {
                RealTimeRequest realTimeRequest = (RealTimeRequest) hdd.getRequestsInHDD().getRequest(i);

                if(!realTimeRequest.isAchieveable(hdd.getCurrentTime())) {
                    removeMissedDeadline(realTimeRequest);
                    i--;
                    numberOfRealTimeRequests--;
                }
                else {
                    currentRealTimeRequest = realTimeRequest;
                    break;
                }
            }
        }

        if(currentRealTimeRequest == null) {
            hdd.getStrategy().doOneStep();
        }
        else {
            if (currentRealTimeRequest.getHDDNumber() > hdd.getCurrentPosition()) {
                hdd.moveRight();
            } else if (currentRealTimeRequest.getHDDNumber() < hdd.getCurrentPosition()) {
                hdd.moveLeft();
            } else {
                hdd.endRequest(currentRealTimeRequest);
                currentRealTimeRequest = null;
            }
        }
    }

    private void removeMissedDeadline(RealTimeRequest realTimeRequest) {
        hdd.endRequest(realTimeRequest);
        realTimeRequest.setDeadlineMissed(hdd.getCurrentTime());
    }

    public String toString() {
        return "EDF";
    }
}
