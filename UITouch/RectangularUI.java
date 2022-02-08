package seaweed.aeproject.Test.UITouch;

import android.graphics.Rect;

import seaweed.aeproject.AlphaEngine.TouchActionPackage.UITouchAction;

abstract public class RectangularUI extends UITouchAction {

    private int[] position;
    private int[] range;

    protected RectangularUI(String key, String name, int[] position, int[] range, boolean keepTouch) {
        super(key, name, new Rect(position[0], position[1], position[0] + range[0], position[1] + range[1]), 1, true, keepTouch);
        this.position = position;
        this.range = range;
    }

    @Override
    public boolean DetailCondition() {

        int x = touchPointDatas[0].getTouchPoint()[0] - position[0];
        int y = touchPointDatas[0].getTouchPoint()[1] - position[1];

        return 0 < x && x < range[0] && 0 < y && y < range[1];
    }
}
