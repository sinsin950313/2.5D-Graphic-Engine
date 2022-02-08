package seaweed.aeproject.AlphaEngine.GraphicPackage;

import android.graphics.Canvas;

import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.ScreenManager;
import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.ScreenManager.Poster;

public class PainterPoster extends Poster {

    protected ScreenManager.PainterComponent drawComponent;

    public PainterPoster(int startX, int startY, int finishX, int finishY, int color)
    {
        ScreenManager.getInstance().super(ScreenManager.getInstance().new PainterComponent(), startX, startY, finishX, finishY);
        drawComponent = (ScreenManager.PainterComponent) super.drawComponent;
        drawComponent.setColor(color);
    }
    @Override
    public void Draw(Canvas canvas) {

    }
}
