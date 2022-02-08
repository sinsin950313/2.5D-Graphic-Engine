package seaweed.aeproject.Test.TouchWithCamera;

import seaweed.aeproject.AlphaEngine.ETC.Type;
import seaweed.aeproject.AlphaEngine.TouchActionPackage.TouchActionWithCamera;

public class OneTouchDrag extends TouchActionWithCamera {

    protected int[] interval;

    public OneTouchDrag(String key) {
        super(key, 1, true, true);
    }

    @Override
    public void ActionDown() {
        //Log.v("Touch", "Drag Down");
        /*CustomMath.Vector1X4 tmpMatrix = camera.getFocus();

        CustomMath.Vector1X4 frontVector = new CustomMath.Vector1X4(camera.getObjectMatrix().zVector().getVector1X3().getX(), 0, camera.getObjectMatrix().zVector().getVector1X3().getZ(), 0);
        frontVector.Normalize();
        frontVector = frontVector.Scaled(interval[1]);
        tmpMatrix = tmpMatrix.AddTouchAction(frontVector);

        CustomMath.Vector1X4 sideVector = new CustomMath.Vector1X4(camera.getObjectMatrix().xVector().getVector1X3().getX(), 0, camera.getObjectMatrix().xVector().getVector1X3().getZ(), 0);
        sideVector.Normalize();
        sideVector = sideVector.Scaled(interval[0]);
        tmpMatrix = tmpMatrix.Subtract(sideVector);

        ViewThread.getInstance().setFocus(tmpMatrix);*/
        camera.RotateFromFocus(interval[0], interval[1]);
    }

    @Override
    public void ActionUp() {
        //Log.v("Touch", "Drag Up");
        interval = null;
    }

    @Override
    public void ActionChange() {

    }

    @Override
    public boolean DetailCondition() {
        if (touchPointDatas[0].getTouchState() == Type.TouchState.MOVE) {
            interval = touchPointDatas[0].getMovement();
            return true;
        }
        return false;
    }
}
