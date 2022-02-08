package seaweed.aeproject.AlphaEngine.ETC;

import android.graphics.Rect;
import android.util.Log;

import java.text.DecimalFormat;

import seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomDataType.*;

public class CustomLog {

    public static void Log(Coordinate3D.Vector1X4 vector1X4, String name)
    {
        if (vector1X4.isPoint())
            Log.v("Matrix1x4", name + " : " + String.valueOf(vector1X4.getVector1X3().getX()) + ", " + String.valueOf(vector1X4.getVector1X3().getY()) + ", " + String.valueOf(vector1X4.getVector1X3().getZ()) + ", 1");
        else
            Log.v("Matrix1x4", name + " : " + String.valueOf(vector1X4.getVector1X3().getX()) + ", " + String.valueOf(vector1X4.getVector1X3().getY()) + ", " + String.valueOf(vector1X4.getVector1X3().getZ()) + ", 0");
    }

    public static void Log(Coordinate3D.Matrix4X4 matrix4X4, String name)
    {
        Log(matrix4X4.getXVector(), name + " - xVector");
        Log(matrix4X4.getYVector(), name + " - yVector");
        Log(matrix4X4.getZVector(), name + " - zVector");
        Log(matrix4X4.getQVector(), name + " - qVector");
    }

    public static void Log(Rect rect, String name)
    {
        Log.v("Rect", name + " : " + String.valueOf(rect.left) + ", " + String.valueOf(rect.top) + ", " + String .valueOf(rect.right) + ", " + String .valueOf(rect.bottom));
    }

    public static void Log(Coordinate3D.Quaternion quaternion, String name)
    {
        Log.v("Quaternion", name + " : " + String.valueOf(quaternion.getUnrealVector().getX()) + ", " + String.valueOf(quaternion.getUnrealVector().getY()) + ", " + String.valueOf(quaternion.getUnrealVector().getZ()) + ", " + String.valueOf(quaternion.getRealValue()));
    }

    public static void Memory()
    {
        DecimalFormat format = new DecimalFormat("###,###,###.##");
        Runtime runtime = Runtime.getRuntime();
        Log.v("Memory", "Max : " + format.format(runtime.maxMemory()) + ", Total : " + format.format(runtime.totalMemory()) + ", Free : " + format.format(runtime.freeMemory()));
    }
}
