package HDDSimulator.Strategy;

import HDDSimulator.RequestList;

public class CScan extends Scan{
    public CScan() {
        super();
    }

    @Override
    public void doOneStep() {
        if(hdd.getRequestsInHDD().getRecentlyChanged()) {
            hdd.getRequestsInHDD().sortRequests(new RequestList.ComparatorByHDDNumber());
            setCurrentRequestIndexInList();
        }

        moveRight();
        if(hdd.getCurrentPosition() == hdd.getHDDRange() - 1) {
            hdd.setCurrentPosition(0);
            hdd.setDirectionRight(true);
            setCurrentRequestIndexInList();
        }
    }


    public String toString() {
        return "CScan";
    }
}
