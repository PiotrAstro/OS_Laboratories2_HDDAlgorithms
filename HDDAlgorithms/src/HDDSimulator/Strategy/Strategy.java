package HDDSimulator.Strategy;

import HDDSimulator.HDD;

public interface Strategy {
    public void setHDD(HDD hdd);
    public void doOneStep();
}
