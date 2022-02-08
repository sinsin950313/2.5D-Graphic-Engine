package seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent;

import android.util.DisplayMetrics;
import android.view.Display;

import static seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineActivity.engineActivity;

public class DeviceDisplayData
{
    private static DeviceDisplayData deviceDisplayData = new DeviceDisplayData();

    private final int deviceDisplayWidth;
    private final int deviceDisplayHeight;

    private final float displayRatio;//(height / width)

    private final int centerX;
    private final int centerY;

    private DeviceDisplayData()
    {
        Display display = engineActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        deviceDisplayWidth = metrics.widthPixels;
        deviceDisplayHeight = metrics.heightPixels;

        displayRatio = (float) deviceDisplayHeight / deviceDisplayWidth;

        centerX = deviceDisplayWidth / 2;
        centerY = deviceDisplayHeight / 2;
    }
    public static DeviceDisplayData getInstance()
    {
        if (deviceDisplayData == null)
            deviceDisplayData = new DeviceDisplayData();
        return deviceDisplayData;
    }
    public int getDeviceDisplayWidth()
    {
        return deviceDisplayWidth;
    }
    public int getDeviceDisplayHeight()
    {
        return deviceDisplayHeight;
    }
    public int getCenterX() {
        return centerX;
    }
    public int getCenterY() {
        return centerY;
    }
    float getDisplayRatio()
    {
        return displayRatio;
    }
}