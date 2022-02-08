package seaweed.aeproject.AlphaEngine.GraphicPackage;

import android.graphics.Canvas;

import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.ScreenManager;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.ScreenManager.Texture;

public class BitmapTexture extends Texture {

    protected ScreenManager.BitmapComponent drawComponent;

    public BitmapTexture(GameObject ownerGameObject, String bitmapFileName) {
        ScreenManager.getInstance().super(ownerGameObject, ScreenManager.getInstance().new BitmapComponent(bitmapFileName));
        drawComponent = (ScreenManager.BitmapComponent) super.drawComponent;
    }

    @Override
    public void Draw(Canvas canvas) {

    }
}
