package HDDSimulator;

public class Request {
    private int arrivalTime, HDDNumber, finishTime;
    private boolean isFinished;

    public Request(int arrivalTime, int HDDNumber) {
        this.arrivalTime = arrivalTime;
        this.HDDNumber = HDDNumber;
        isFinished = false;
    }

    public Request(Request copyFrom) {
        arrivalTime = copyFrom.getArrivalTime();
        HDDNumber = copyFrom.getHDDNumber();
        finishTime = copyFrom.getFinishTime();
        isFinished = copyFrom.isFinished();
    }

    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getHDDNumber() {
        return HDDNumber;
    }
    public int getFinishTime() {
        return finishTime;
    }
    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
        isFinished = true;
    }
    public boolean isFinished() {
        return isFinished;
    }

    public int getWaitingTime() {
        return finishTime - arrivalTime;
    }

    public int distanceTo(int HDDNumber) {
        return Math.abs(this.HDDNumber - HDDNumber);
    }
}
