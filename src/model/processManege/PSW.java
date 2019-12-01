package model.processManege;

public class PSW {
    private boolean processEnd;
    private boolean timeSliceUsedUp;
    private boolean IOInterrupt;

    private static PSW psw = new PSW();

    private PSW(){}

    public void initPSW() {
        this.IOInterrupt = false;
        this.timeSliceUsedUp = false;
        this.processEnd = false;
    }

    public static PSW getPSW() {
        return psw;
    }

    //getter && setter
    public boolean isProcessEnd() {
        return processEnd;
    }

    public void setProcessEnd(boolean processEnd) {
        this.processEnd = processEnd;
    }

    public boolean isTimeSliceUsedUp() {
        return timeSliceUsedUp;
    }

    public void setTimeSliceUsedUp(boolean timeSliceUsedUp) {
        this.timeSliceUsedUp = timeSliceUsedUp;
    }

    public boolean isIOInterrupt() {
        return IOInterrupt;
    }

    public void setIOInterrupt(boolean IOInterrupt) {
        this.IOInterrupt = IOInterrupt;
    }
}
