package seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent;

import android.os.SystemClock;

abstract public class StageThread extends Thread {

    protected boolean stageClear = false;
    protected int stageNumber;

    public StageThread(int stageNumber)
    {
        this.stageNumber = stageNumber;
        Setting();
        Load();
    }
    public boolean isStageClear()
    {
        return stageClear;
    }

    @Override
    public void run()
    {
        while(!stageClear)
        {
            UpDate();
            CheckStageClear();
            SystemClock.sleep(100);
        }
        MainLogicThread.getInstance().setLastStage(stageNumber);
        MainLogicThread.getInstance().setStageChange(true);
        MainLogicThread.getInstance().notify();
    }

    abstract protected void CheckStageClear();
    abstract protected void UpDate();
    abstract protected void Setting();//touch action, camera position, trigger etc...
    abstract protected void Load();//gameobject position, graphic data etc...
}
