package seaweed.aeproject.AlphaEngine.TouchActionPackage;

import seaweed.aeproject.AlphaEngine.ETC.Type;
import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.TouchManager;

abstract public class TouchActionWithCamera extends TouchManager.TouchAction {

    public TouchActionWithCamera(String key, int correctTouchCount, boolean isLongTouch, boolean releaseTouch)
    {
        TouchManager.getInstance().super(correctTouchCount, isLongTouch, Type.TouchType.CAMERA, releaseTouch);
        TouchManager.getInstance().AddTouchAction(key, this);
    }
}