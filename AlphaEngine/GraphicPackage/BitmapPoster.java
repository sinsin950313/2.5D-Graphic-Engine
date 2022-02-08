package seaweed.aeproject.AlphaEngine.GraphicPackage;

import android.graphics.Canvas;

import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.ScreenManager;
import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.ScreenManager.Poster;

public class BitmapPoster extends Poster {

    protected ScreenManager.BitmapComponent drawComponent;

    public BitmapPoster(String bitmapFileName, int startX, int startY, int finishX, int finishY)
    {
        ScreenManager.getInstance().super(ScreenManager.getInstance().new BitmapComponent(bitmapFileName), startX, startY, finishX, finishY);
        drawComponent = (ScreenManager.BitmapComponent) super.drawComponent;
    }
    @Override
    public void Draw(Canvas canvas) {

    }
}
