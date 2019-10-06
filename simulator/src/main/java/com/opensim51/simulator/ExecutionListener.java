package com.opensim51.simulator;

import com.opensim51.simulator.memory.datatype.UInt16;

public interface ExecutionListener {

    void process(UInt16 programCounter);

    void reset();

    boolean isRunning();

}