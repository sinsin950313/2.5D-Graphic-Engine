package seaweed.aeproject.Test.TouchWithCamera;

import seaweed.aeproject.AlphaEngine.ETC.Type;
import seaweed.aeproject.AlphaEngine.TouchActionPackage.TouchActionWithCamera;

//one point fix, one point move
public class OneTouchDown_OneTouchDrag extends TouchActionWithCamera {

    protected int[] interval;
    private boolean left;
    private boolean up;

    public OneTouchDown_OneTouchDrag(String key) {
        super(key, 2, true, false);
    }

    @Override
    public void ActionDown() {
        //Log.v("Touch", "Down - Drag Down");
        if(!up)
            camera.RotateFromFocus(interval[0], interval[1]);
        else
            camera.RotateFromFocus(-interval[0], -interval[1]);
    }

    @Override
    public void ActionUp() {
        //Log.v("Touch", "Down - Drag up");
    }

    @Override
    public void ActionChange() {

    }

    @Override
    public boolean DetailCondition() {
        if((touchPointDatas[0].getTouchState() == Type.TouchState.DOWN && touchPointDatas[1].getTouchState() == Type.TouchState.MOVE) ||
                (touchPointDatas[1].getTouchState() == Type.TouchState.DOWN && touchPointDatas[0].getTouchState() == Type.TouchState.MOVE)) {
            if(touchPointDatas[0].getTouchState() == Type.TouchState.MOVE) {
                interval = touchPointDatas[0].getMovement();
                left = touchPointDatas[0].getTouchPoint()[0] < touchPointDatas[1].getTouchPoint()[0];
                up = touchPointDatas[0].getTouchPoint()[1] < touchPointDatas[1].getTouchPoint()[1];
            }
            else {
                interval = touchPointDatas[1].getMovement();
                left = touchPointDatas[1].getTouchPoint()[0] < touchPointDatas[0].getTouchPoint()[0];
                up = touchPointDatas[1].getTouchPoint()[1] < touchPointDatas[0].getTouchPoint()[1];
            }
            return true;
        }
        return false;
    }
}
