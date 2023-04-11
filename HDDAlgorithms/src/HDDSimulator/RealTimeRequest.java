package HDDSimulator;

public class RealTimeRequest extends Request {
    private int deadline;
    private boolean isDeadlineMissed;

    public RealTimeRequest(int arrivalTime, int HDDNumber, int deadline) {
        super(arrivalTime, HDDNumber);
        this.deadline = deadline;
        isDeadlineMissed = false;
    }

    public RealTimeRequest(RealTimeRequest copyFrom) {
        super(copyFrom);
        deadline = copyFrom.getDeadline();
        isDeadlineMissed = copyFrom.isDeadlineMissed();
    }

    public int getDeadline() {
        return deadline;
    }

    public boolean isDeadlineMissed() {
        return isDeadlineMissed;
    }

    public void setFinishTime(int finishTime) {
        super.setFinishTime(finishTime);
        isDeadlineMissed = finishTime > (deadline + getArrivalTime());
    }

    public void setDeadlineMissed(int finishTime) {
        super.setFinishTime(finishTime);
        isDeadlineMissed = true;
    }

    public boolean isAchieveable(int time) {
        return time <= deadline + getArrivalTime();
    }
}
