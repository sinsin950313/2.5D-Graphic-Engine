package seaweed.aeproject.AlphaEngine.GraphicPackage;

import android.graphics.Canvas;

import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.ScreenManager;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.ScreenManager.Texture;

public class PainterTexture extends Texture {

    protected ScreenManager.PainterComponent drawComponent;

    public PainterTexture(GameObject ownerGameObject, int color) {
        ScreenManager.getInstance().super(ownerGameObject, ScreenManager.getInstance().new PainterComponent());
        drawComponent = (ScreenManager.PainterComponent) super.drawComponent;
        drawComponent.setColor(color);
    }

    @Override
    public void Draw(Canvas canvas) {

    }
}
