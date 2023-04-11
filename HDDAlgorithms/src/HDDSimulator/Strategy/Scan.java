package HDDSimulator.Strategy;

import HDDSimulator.HDD;
import HDDSimulator.Request;
import HDDSimulator.RequestList;

public class Scan implements Strategy{
    private int currentRequestIndexInList;
    protected HDD hdd;

    @Override
    public void setHDD(HDD hdd) {
        this.hdd = hdd;
        currentRequestIndexInList = 0;
    }

    @Override
    public void doOneStep() {
        if(hdd.getRequestsInHDD().getRecentlyChanged()) {
            hdd.getRequestsInHDD().sortRequests(new RequestList.ComparatorByHDDNumber());

            setCurrentRequestIndexInList();
        }

        if(hdd.isDirectionRight()) {
            moveRight();
            if(hdd.getCurrentPosition() == hdd.getHDDRange() - 1) {
                hdd.setDirectionRight(false);
                setCurrentRequestIndexInList();
            }
        }
        else {
            moveLeft();
            if(hdd.getCurrentPosition() == 0) {
                hdd.setDirectionRight(true);
                setCurrentRequestIndexInList();
            }
        }
    }

    protected void setCurrentRequestIndexInList() {
        if(hdd.isDirectionRight()) {
            currentRequestIndexInList = 0;
        }
        else {
            currentRequestIndexInList = hdd.getRequestsInHDD().getNumberOfRequests() - 1;
        }
    }

    protected void moveRight() {
        hdd.moveRight();
        for(int i = currentRequestIndexInList; i < hdd.getRequestsInHDD().getNumberOfRequests(); i++) {
            currentRequestIndexInList = i;
            if(hdd.getRequestsInHDD().getRequest(i).getHDDNumber() > hdd.getCurrentPosition()) {
                break;
            }
            else if (hdd.getCurrentPosition() == hdd.getRequestsInHDD().getRequest(i).getHDDNumber()) {
                hdd.endRequest(hdd.getRequestsInHDD().getRequest(i));
                i--;
            }
        }
    }

    protected void moveLeft() {
        hdd.moveLeft();
        for(int i = currentRequestIndexInList; i >= 0; i--) {
            currentRequestIndexInList = i;
            if(hdd.getRequestsInHDD().getRequest(i).getHDDNumber() < hdd.getCurrentPosition()) {
                break;
            }
            else if (hdd.getCurrentPosition() == hdd.getRequestsInHDD().getRequest(i).getHDDNumber()) {
                hdd.endRequest(hdd.getRequestsInHDD().getRequest(i));
            }
        }
    }

    public String toString() {
        return "Scan";
    }
}
