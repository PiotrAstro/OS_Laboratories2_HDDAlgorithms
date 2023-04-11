package HDDSimulator.Strategy;

import HDDSimulator.HDD;
import HDDSimulator.Request;

public class SSTF implements Strategy{
    private HDD hdd;
    private Request currentRequest;


    @Override
    public void setHDD(HDD hdd) {
        this.hdd = hdd;
        currentRequest = null;
    }

    @Override
    public void doOneStep() {
        if(currentRequest == null){
            currentRequest = hdd.getRequestsInHDD().getClosestToHDDNumber(hdd.getCurrentPosition());
        }

        if(currentRequest.getHDDNumber() > hdd.getCurrentPosition()) {
            hdd.moveRight();
        }
        else if(currentRequest.getHDDNumber() < hdd.getCurrentPosition()) {
            hdd.moveLeft();
        }
        else {
            hdd.endRequest(currentRequest);
            currentRequest = null;
        }
    }

    public String toString() {
        return "SSTF";
    }
}
