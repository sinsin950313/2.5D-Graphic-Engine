package seaweed.aeproject.AlphaEngine.TouchActionPackage;

import android.graphics.Rect;

import seaweed.aeproject.AlphaEngine.ETC.Type;
import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.TouchManager;
import seaweed.aeproject.AlphaEngine.GraphicPackage.BitmapGraphic;

abstract public class UITouchAction extends TouchManager.TouchAction {

    protected BitmapGraphic bitmapGraphic;
    protected boolean keepTouch;

    public UITouchAction(String touchActionSetKey, String name, Rect dst, int correctTouchCount, boolean isLongTouch, boolean keepTouch) {
        TouchManager.getInstance().super(correctTouchCount, isLongTouch, Type.TouchType.UI, true);
        TouchManager.getInstance().AddTouchAction(touchActionSetKey, this);
        bitmapGraphic = new BitmapGraphic(name, Type.Draw_Type.UI);
        bitmapGraphic.setDestination(dst);
        this.keepTouch = keepTouch;
    }
}