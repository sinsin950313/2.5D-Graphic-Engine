package seaweed.aeproject.Test.TouchWithCamera;

import seaweed.aeproject.AlphaEngine.ETC.Type;
import seaweed.aeproject.AlphaEngine.TouchActionPackage.TouchActionWithCamera;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObjects.Ray;

public class OneTouchDown extends TouchActionWithCamera {

    private int[] points;
    private Ray ray;
    private boolean touch;

    public OneTouchDown(String key) {
        super(key, 1, false, true);
    }

    public GameObject getGamePoint()
    {
        //shoot BitmapGraphic ray, if collide some GameObject, return it.(except field)
        //else create BitmapGraphic empty GameObject return it.

        return null;
    }

    @Override
    public void ActionDown() {
        //Log.v("Touch", "Touch Down");
        touch = true;
    }

    @Override
    public void ActionUp() {
        //Log.v("Touch", "Touch Up");
        /*CustomMath.Vector1X4 screenPoint = new CustomMath.Vector1X4((points[0] - camera.getWidthPixels()/2) / camera.getwRatio(), -(points[1] - camera.getHeightPixels()/2) / camera.gethRatio(), camera.getMaxSight(), 1).Multiply(camera.getObjectMatrix());
        new DotGraphic(screenPoint);

        CustomMath.Vector1X4 rayVector = screenPoint.Subtract(camera.getObjectMatrix().qVector());
        rayVector.Normalize();
        ray = new Ray(rayVector, camera.getObjectMatrix().qVector(), camera.getMaxSight()) {
            @Override
            protected boolean RayCondition() {
                Volume volume = (Volume) collideGameObject.getComponent(vGameObjectComponent);
                return volume.isRigidBody();
            }
        };
        GameObject[] returnGameObject = new GameObject[1];
        CustomMath.Vector1X4[] returnPoint = new CustomMath.Vector1X4[1];
        ray.ShootARay(returnGameObject, returnPoint);

        points = null;*/
        touch = false;
    }

    @Override
    public void ActionChange() {
        touch = false;
    }

    @Override
    public boolean DetailCondition() {
        if(touchPointDatas[0].getTouchState() == Type.TouchState.DOWN) {
            points = new int[2];
            points[0] = touchPointDatas[0].getTouchPoint()[0];
            points[1] = touchPointDatas[0].getTouchPoint()[1];
            return true;
        }
        return false;
    }

    public boolean isTouch() {
        return touch;
    }
    public void setTouch(boolean touch) {
        this.touch = touch;
    }
}
