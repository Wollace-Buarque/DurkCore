package dev.cromo29.durkcore.Updater;

public enum UpdateType {

    MIN_64(3840000L),
    MIN_32(1920000L),
    MIN_16(960000L),
    MIN_08(480000L),
    MIN_04(240000L),
    MIN_02(120000L),
    MIN_01(60000L),
    SLOWEST(32000L),
    SLOWER(16000L),
    SLOW(4000L),
    SEC_08(8000L),
    SEC_03(3000L),
    SEC(1000L),
    FAST(500L),
    FASTER(250L),
    FASTEST(125L),
    TICK(49L);

    private long time;
    private long last;
    private long timeSpent;
    private long timeCount;

    UpdateType(long time) {
        this.time = time;
        this.last = System.currentTimeMillis();
    }

    public boolean elapsed() {
        if (elapsed(last, time)) {
            last = System.currentTimeMillis();

            return true;
        } else return false;
    }

    public void startTime() {
        timeCount = System.currentTimeMillis();
    }

    public void stopTime() {
        timeSpent += System.currentTimeMillis() - timeCount;
    }

    public void resetTime(boolean print) {

        if (print) {
            System.out.println(name() + " in a second: " + timeSpent);
        }

        timeSpent = 0L;
    }

    private boolean elapsed(long t1, long t2) {
        return System.currentTimeMillis() - t1 > t2;
    }
}
