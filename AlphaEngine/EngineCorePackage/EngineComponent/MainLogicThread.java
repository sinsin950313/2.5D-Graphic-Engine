package seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent;

import android.util.Log;

import seaweed.aeproject.Test.TestStageThread;

public class MainLogicThread extends Thread {

    private static MainLogicThread mainLogicThread = new MainLogicThread();

    private PhysicsSystem physicsSystem;

    private int mainStage;
    private int lastStage;
    private boolean stageChange;
    private boolean loading;

    private MainLogicThread()
    {
        mainStage = 0;
        stageChange = false;
    }
    public static MainLogicThread getInstance()
    {
        if(mainLogicThread == null)
            mainLogicThread = new MainLogicThread();
        return mainLogicThread;
    }
    public void ThreadStart()
    {
        ReadLastStage();
        start();
    }
    private StageThread getStage(int key)
    {
        //write manual
        StageThread stageThread;
        switch (key)
        {
            case 0: {
                stageThread = new TestStageThread();
                break;
            }
            default: {
                //Toast.makeText(engineActivity.getApplicationContext(), "There is nothing stage anymore", Toast.LENGTH_SHORT).show();
                Log.v("temp", "There is nothing stage anymore");
                stageThread = null;//change mainStage
            }
        }

        return stageThread;
    }
    public void setLastStage(int lastStage)
    {
        //save last stage at parsing data
        this.lastStage = lastStage;
    }
    private void ReadLastStage()
    {
        //read last stage by parsing and set lastStage variable
    }
    public void setStageChange(boolean stageChange)
    {
        this.stageChange = stageChange;
    }
    public void Stop()
    {

    }
    public void Start()
    {

    }

    @Override
    public void run()
    {
        PhysicsSystem.getInstance().ThreadStart();
        TriggerChecker.getInstance().ThreadStart();
        getStage(mainStage).start();

        while (true)
        {
            if (stageChange)
            {
                ScreenTransmitter.getInstance().Clear();
                PhysicsSystem.getInstance().Clear();
                TriggerChecker.getInstance().Clear();
                TouchManager.getInstance().Clear();

                getStage((lastStage + 1)).start();
                stageChange = false;
                try {
                    wait();//??
                } catch (Exception e) { }
            }
        }
    }

    public boolean isLoading() {
        return loading;
    }
}
