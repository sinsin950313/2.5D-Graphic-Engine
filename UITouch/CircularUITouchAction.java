package seaweed.aeproject.Test.UITouch;

import android.graphics.Rect;

import seaweed.aeproject.AlphaEngine.ETC.CustomMath;
import seaweed.aeproject.AlphaEngine.TouchActionPackage.UITouchAction;

abstract public class CircularUITouchAction extends UITouchAction {

    private int[] center;
    private int radius;

    protected CircularUITouchAction(String key, int[] center, int radius, String name, int currectTouchCount, boolean isLongTouch, boolean keepTouch) {
        super(key, name, new Rect(center[0] - radius, center[1] - radius, center[0] + radius, center[1] + radius), currectTouchCount, isLongTouch, keepTouch);

        this.center = new int[2];
        this.center = center;
        this.radius = radius;
    }

    @Override
    public boolean DetailCondition() {

        int[] tmp = touchPointDatas[0].getTouchPoint();

        return CustomMath.Distance(tmp, center) < radius;
    }
}
