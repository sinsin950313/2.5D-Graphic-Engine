package seaweed.aeproject.AlphaEngine.EngineCorePackage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import seaweed.aeproject.AlphaEngine.ETC.Type;
import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.DeviceDisplayData;
import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.MainLogicThread;
import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.ScreenManager;
import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.TimerThread;
import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.TouchManager;

public class EngineActivity extends AppCompatActivity {

    public static EngineActivity engineActivity;

    private TouchManager touchManager;
    private ScreenManager screenManager;
    private MainLogicThread mainLogicThread;
    private static DeviceDisplayData deviceDisplayData;

    public static TimerThread timer;
    public static Type.GameObjectComponent vGameObjectComponent;
    public static Type.GameObjectComponent mGameObjectComponent;
    public static Type.GameObjectComponent gGameObjectComponent;

    //public static User user;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        engineActivity = this;

        timer = new TimerThread();
        timer.start();

        vGameObjectComponent = Type.GameObjectComponent.VOLUME;
        mGameObjectComponent = Type.GameObjectComponent.MATERIAL;
        gGameObjectComponent = Type.GameObjectComponent.GRAPHIC;

        deviceDisplayData = DeviceDisplayData.getInstance();
        touchManager = TouchManager.getInstance();
        screenManager = ScreenManager.getInstance();
        mainLogicThread = MainLogicThread.getInstance();

        mainLogicThread.ThreadStart();
        touchManager.start();

        setContentView(screenManager.getScreenTransmitter());
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }
}
