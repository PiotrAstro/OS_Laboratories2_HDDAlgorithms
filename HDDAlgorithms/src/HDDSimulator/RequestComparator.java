package HDDSimulator;

import java.util.Comparator;

public interface RequestComparator<T extends Request> extends Comparator<T> {
    public void setHDD(HDD hdd);
}
