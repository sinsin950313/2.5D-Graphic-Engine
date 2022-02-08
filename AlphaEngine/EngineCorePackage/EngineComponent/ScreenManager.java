package seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import seaweed.aeproject.AlphaEngine.ETC.Type;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObject.GameObjectComponentInterface;
import seaweed.aeproject.AlphaEngine.PhysicsPackage.ContactData;
import seaweed.aeproject.AlphaEngine.VolumePackage.Volume;

import static seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineActivity.engineActivity;
import static seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineActivity.gGameObjectComponent;
import static seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineActivity.timer;
import static seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineActivity.vGameObjectComponent;

public class ScreenManager {

    private static ScreenManager screenManager = new ScreenManager();

    private final ScreenTransmitter screenTransmitter;
    private final BitmapStorage bitmapStorage;
    private final PosterStorage posterStorage;

    private int drawCounts = 20;

    private ScreenManager()
    {
        screenTransmitter = new ScreenTransmitter(drawCounts);
        bitmapStorage = new BitmapStorage();
        posterStorage = new PosterStorage();
    }
    public static ScreenManager getInstance()
    {
        if (screenManager == null)
            screenManager = new ScreenManager();
        return screenManager;
    }
    public void setDrawObjectCount(int count)
    {
        drawCounts = count;
        screenTransmitter.setDrawCount(drawCounts);
    }
    public View getScreenTransmitter()
    {
        return screenTransmitter;
    }

    private class ScreenTransmitter extends View {

        private DrawOrderNode[] drawOrderNodes;

        ScreenTransmitter(int count)
        {
            super(engineActivity.getApplicationContext());

            drawOrderNodes = new DrawOrderNode[count];
        }
        private DrawOrderNode Sort(Vector<ContactData> contactData)
        {
            int index;
            boolean connect;
            DrawOrderNode headNode, sortPointer, arrayPointer;
            Volume origin, compare;

            index = 0;
            headNode = drawOrderNodes[index];
            arrayPointer = drawOrderNodes[index];

            Iterator<ContactData> collisionDataIterator = contactData.iterator();
            while (collisionDataIterator.hasNext())
            {
                if (arrayPointer == null)
                    arrayPointer = new DrawOrderNode();

                arrayPointer.setData(collisionDataIterator.next().getSubGameObject());
                compare = (Volume) arrayPointer.getGameObject().getComponent(vGameObjectComponent, null);

                sortPointer = headNode;
                connect = false;
                while (sortPointer.getNextNode() != null)
                {
                    origin = (Volume) sortPointer.getGameObject().getComponent(vGameObjectComponent, null);
                    if (PhysicsSystem.getInstance().isBehind(origin, compare))
                    {
                        arrayPointer.Connect(sortPointer);
                        connect = true;
                        break;
                    }

                    sortPointer = sortPointer.getAfterPointer();
                }

                if (!connect) {
                    arrayPointer.Connect(sortPointer);
                }

                if (headNode.getBeforePointer() != null)
                    headNode = headNode.getBeforePointer();

                index++;
                arrayPointer = drawOrderNodes[index];
            }

            return headNode;
        }
        void setDrawCount(int count)
        {
            drawOrderNodes = new DrawOrderNode[count];
        }

        @Override
        public void onDraw(Canvas canvas) {
            timer.setScreenTime();
            CameraController.Camera camera = CameraController.getInstance().getCamera();
            canvas.drawColor(Color.BLACK);

            try {
                //draw sky
                //draw field

                DrawOrderNode headNode = Sort(PhysicsSystem.getInstance().CheckCollide(camera, true));
                while (headNode.getNextNode() == null)
                {
                    headNode.getTexture().setBillboard();
                    headNode.getTexture().Draw(canvas);
                    headNode = headNode.getNextNode();
                }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                Iterator<Poster> posterIterator = posterStorage.getPosters().iterator();
                while (posterIterator.hasNext())
                    posterIterator.next().Draw(canvas);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            } catch (Exception e) {
                Log.v("Exception", "ViewThread Exception", e);
            } finally {
                invalidate();
                SystemClock.sleep(100);
            }
        }

        @Override
        public boolean performClick()
        {
            return false;
        }

        private class DrawOrderNode
        {
            private GameObject gameObject;
            private Texture texture;
            private DrawOrderNode afterPointer;
            private DrawOrderNode beforePointer;

            DrawOrderNode()
            {
                gameObject = null;
                texture = null;
                afterPointer = null;
                beforePointer = null;
            }
            void setData(GameObject gameObject)
            {
                this.gameObject = gameObject;
                this.texture = (Texture) gameObject.getComponent(gGameObjectComponent, null);
            }
            void Connect(DrawOrderNode nextNode)
            {
                DrawOrderNode tempBefore;

                if (nextNode.beforePointer != null)
                {
                    tempBefore = nextNode.beforePointer;
                    tempBefore.Connect(this);
                }

                this.afterPointer = nextNode;
                nextNode.beforePointer = this;
            }
            Texture getTexture()
            {
                return texture;
            }
            GameObject getGameObject()
            {
                return gameObject;
            }
            DrawOrderNode getAfterPointer()
            {
                return afterPointer;
            }
            DrawOrderNode getBeforePointer()
            {
                return beforePointer;
            }
            DrawOrderNode getNextNode()
            {
                DrawOrderNode temp = afterPointer;
                Clear();
                return temp;
            }
            void Clear()
            {
                gameObject = null;
                texture = null;
                afterPointer = null;
                beforePointer = null;
            }
        }
    }

    private class BitmapStorage
    {
        private HashMap<String, Bitmap> bitmapHashMap;

        BitmapStorage()
        {
            bitmapHashMap = new HashMap<>();
        }
        Bitmap Register(String bitmapFileName)
        {
            if (!Check(bitmapFileName)) {
                int drawableResourceId = engineActivity.getApplicationContext().getResources().getIdentifier(bitmapFileName, "drawable", engineActivity.getApplicationContext().getPackageName());
                Bitmap bitmap = BitmapFactory.decodeResource(engineActivity.getApplicationContext().getResources(), drawableResourceId);

                try {
                    if (bitmap == null)
                        throw new Exception();
                } catch (Exception e) {
                    Log.v("Exception", String.valueOf(bitmapFileName) + " doesn't exist");
                    return null;
                }

                bitmapHashMap.put(bitmapFileName, bitmap);
                return bitmap;
            }

            return bitmapHashMap.get(bitmapFileName);
        }
        boolean Check(String name)
        {
            return bitmapHashMap.containsKey(name);
        }
        void Clear()
        {
            bitmapHashMap.clear();
        }
    }

    private static class PosterStorage
    {
        private Vector<Poster> posters;

        PosterStorage()
        {
            posters = new Vector<>();
        }
        void Register(Poster poster)
        {
            posters.add(poster);
        }
        Vector<Poster> getPosters()
        {
            return posters;
        }
        void Clear()
        {
            posters.clear();
        }
    }

    private interface DrawComponentInterface { }

    public class PainterComponent implements DrawComponentInterface
    {
        private final Paint paint;

        public PainterComponent()
        {
            paint = new Paint();
        }
        public void setColor(int color)
        {
            paint.setColor(color);
        }
        public Paint getPaint()
        {
            return paint;
        }
    }

    public class BitmapComponent implements DrawComponentInterface {

        private final Bitmap bitmap;
        private final Rect source;
        private int width, height;

        public BitmapComponent(String name)
        {
            bitmap = bitmapStorage.Register(name);
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            source = new Rect(0, 0, width, height);
        }
        public Bitmap getBitmap()
        {
            return bitmap;
        }
        public void setUnit(int width, int height)
        {
            this.width = width;
            this.height = height;
        }
        public void setSource(int widthNumber, int heightNumber)
        {
            if (width != bitmap.getWidth()) {
                source.left = width * (widthNumber - 1);
                source.right = width * widthNumber;
            }
            if (height != bitmap.getHeight()) {
                source.top = height * (heightNumber - 1);
                source.bottom = heightNumber * heightNumber;
            }
        }
        public Rect getSource()
        {
            return source;
        }
    }

    private abstract class Drawable
    {
        protected DrawComponentInterface drawComponent;

        protected Drawable(DrawComponentInterface drawComponent)
        {
            this.drawComponent = drawComponent;
        }

        abstract public void Draw(Canvas canvas);
    }

    public abstract class Poster extends Drawable
    {
        protected int startX, startY, finishX, finishY;

        public Poster(DrawComponentInterface drawComponent, int startX, int startY, int finishX, int finishY)
        {
            super(drawComponent);
            this.startX = startX;
            this.startY = startY;
            this.finishX = finishX;
            this.finishY = finishY;
            posterStorage.Register(this);
        }
        public void setDestination(int startX, int startY, int finishX, int finishY)
        {
            this.startX = startX;
            this.startY = startY;
            this.finishX = finishX;
            this.finishY = finishY;
        }
    }

    public abstract class Texture extends Drawable implements GameObjectComponentInterface {

        protected final GameObject ownerGameObject;
        protected final Volume volume;
        protected Rect destination;

        public Texture(GameObject ownerGameObject, DrawComponentInterface drawComponent)
        {
            super(drawComponent);
            this.ownerGameObject = ownerGameObject;
            this.volume = (Volume) ownerGameObject.getComponent(vGameObjectComponent, null);
        }
        void setBillboard()
        {
            PhysicsSystem.getInstance().getBillboard(volume, destination);
        }

        @Override
        public boolean CheckComponent(Type.GameObjectComponent componentType) {
            return componentType == Type.GameObjectComponent.GRAPHIC;
        }
    }
}
