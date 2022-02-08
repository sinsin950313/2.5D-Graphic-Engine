package seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent;

import android.os.SystemClock;

public class TimerThread extends Thread {

    private long time;
    private long screenTimer;

    public long getTime()
    {
        return time;
    }
    boolean CheckScreenTime(long screenTimer)
    {
        return this.screenTimer == screenTimer;
    }
    long getScreenTime() {
        return screenTimer;
    }
    void setScreenTime() {
        this.screenTimer = time;
    }

    @Override
    public void run()
    {
        while (true) {
            time = SystemClock.currentThreadTimeMillis();
        }
    }
}
