package seaweed.aeproject.Test.TouchWithCamera;

import seaweed.aeproject.AlphaEngine.ETC.Type;
import seaweed.aeproject.AlphaEngine.TouchActionPackage.TouchActionWithCamera;

public class OneTouchDrag_OneTouchDrag extends TouchActionWithCamera {

    protected int[][] touchPoint;
    protected int[][] interval;

    public OneTouchDrag_OneTouchDrag(String key) {
        super(key, 2, true, true);
        touchPoint = new int[2][2];
        interval = new int[2][2];
    }

    @Override
    public void ActionDown() {
        //Log.v("Touch", "Drag - Drag Down");
        int delta;
        delta = (-interval[0][0] + interval[1][0]);
        camera.Zoom(delta);
    }

    @Override
    public void ActionUp() {
        //Log.v("Touch", "Drag - Drag Up");
        interval[0] = null;
        interval[1] = null;
    }

    @Override
    public void ActionChange() {

    }

    @Override
    public boolean DetailCondition() {
        touchPoint[0] = touchPointDatas[0].getTouchPoint();
        touchPoint[1] = touchPointDatas[1].getTouchPoint();

        if(touchPointDatas[0].getTouchState() == Type.TouchState.MOVE && touchPointDatas[1].getTouchState() == Type.TouchState.MOVE) {
            if(touchPointDatas[0].getTouchPoint()[0] < touchPointDatas[1].getTouchPoint()[0]) {
                interval[0] = touchPointDatas[0].getMovement();
                interval[1] = touchPointDatas[1].getMovement();
            }
            else
            {
                interval[0] = touchPointDatas[1].getMovement();
                interval[1] = touchPointDatas[0].getMovement();
            }
            return true;
        }
        return false;
    }
}
