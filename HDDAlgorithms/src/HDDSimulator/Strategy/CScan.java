package HDDSimulator.Strategy;

import HDDSimulator.RequestList;

public class CScan extends Scan{

    @Override
    public void doOneStep() {
        if(hdd.getRequestsInHDD().getRecentlyChanged()) {
            hdd.getRequestsInHDD().sortRequests(new RequestList.ComparatorByHDDNumber());
            setCurrentRequestIndexInList();
        }


        if(hdd.getCurrentPosition() == hdd.getHDDRange() - 1) {
            hdd.setCurrentPosition(0);
            hdd.setDirectionRight(true);
            setCurrentRequestIndexInList();
            hdd.decreaseDirectionChangesCounter();
        }
        else {
            hdd.moveRight();
        }

        scanRight();
    }


    public String toString() {
        return "CScan";
    }
}
