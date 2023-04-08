package MathForProject;

public class Average {
    private int numberOfValues;
    private double value;

    public Average() {
        value = 0;
        numberOfValues = 0;
    }

    public void addValue(double newValue) {
        numberOfValues++;
        value = value * ((double) (numberOfValues - 1) / numberOfValues) + newValue / numberOfValues;
    }

    public double getValue() {
        return value;
    }
}
