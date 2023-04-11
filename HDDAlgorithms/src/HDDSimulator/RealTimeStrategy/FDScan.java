package HDDSimulator.RealTimeStrategy;

import HDDSimulator.HDD;
import HDDSimulator.RealTimeRequest;
import HDDSimulator.RequestList;
import HDDSimulator.Strategy.Scan;

import java.util.Comparator;
import java.util.Iterator;

public class FDScan extends Scan implements RealTimeStrategy{
    RealTimeRequest currentRealTimeRequest;
    Comparator<? super RealTimeRequest> comparatorIfBothAchieveable;

    public FDScan(Comparator<RealTimeRequest> comparatorIfBothAchieveable) {
        super();
        currentRealTimeRequest = null;
        this.comparatorIfBothAchieveable = comparatorIfBothAchieveable;
    }

    @Override
    public void doOneStep() {
        if(hdd.getRequestsInHDD().getRecentlyChanged()) {
            hdd.getRequestsInHDD().sortRequests(new RequestList.ComparatorByHDDNumber());
        }

        if(currentRealTimeRequest == null) {
            currentRealTimeRequest = findFirstAchieveableRealTimeRequest();
        }

        if(currentRealTimeRequest != null) {
            if(currentRealTimeRequest.getHDDNumber() > hdd.getCurrentPosition()) {
                currentRequestIndexInList = 0;
                hdd.moveRight();
                scanRight();
            }
            else if(currentRealTimeRequest.getHDDNumber() < hdd.getCurrentPosition()) {
                currentRequestIndexInList = hdd.getRequestsInHDD().getNumberOfRequests() - 1;
                hdd.moveLeft();
                scanLeft();
            }
            else {
                currentRequestIndexInList = 0;
                scanRight();
            }

            if(hdd.getCurrentPosition() == currentRealTimeRequest.getHDDNumber()) {
                currentRealTimeRequest = null;
            }
        }
        else {
            hdd.getStrategy().doOneStep();
        }
    }

    private RealTimeRequest findFirstAchieveableRealTimeRequest() {
        RealTimeRequest realTimeRequest = null;

        for(int i = 0; i < hdd.getRequestsInHDD().getNumberOfRequests(); i++) {
            if(hdd.getRequestsInHDD().getRequest(i) instanceof RealTimeRequest) {
                RealTimeRequest tempRealTime = (RealTimeRequest) hdd.getRequestsInHDD().getRequest(i);

                if(tempRealTime.isAchieveable(hdd.getCurrentTime() + tempRealTime.distanceTo(hdd.getCurrentPosition()))) {
                    if(realTimeRequest == null) {
                        realTimeRequest = tempRealTime;
                    }
                    else if(comparatorIfBothAchieveable.compare(tempRealTime, realTimeRequest) < 0){
                        realTimeRequest = tempRealTime;
                    }
                }
                else {
                    removeMissedDeadline(tempRealTime);
                    i--;
                }
            }
        }

        return realTimeRequest;
    }

    private void removeMissedDeadline(RealTimeRequest realTimeRequest) {
        hdd.endRequest(realTimeRequest);
        realTimeRequest.setDeadlineMissed(hdd.getCurrentTime());
    }

    public String toString() {
        return "FDScan";
    }
}
