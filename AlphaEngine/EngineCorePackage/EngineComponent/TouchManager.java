package seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent;

import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Iterator;
import java.util.Vector;

import seaweed.aeproject.AlphaEngine.ETC.Type;
import seaweed.aeproject.AlphaEngine.TouchActionPackage.TouchActionWithCamera;
import seaweed.aeproject.AlphaEngine.TouchActionPackage.UITouchAction;

import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomCalculate.Distance;
import static seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineActivity.timer;

public class TouchManager extends Thread {

    private static TouchManager touchManager = new TouchManager();

    private final TouchSensor touchSensor;
    private final TouchPointChecker touchPointChecker;
    private final TouchActionFinder touchActionFinder;

    private TouchActionSet[] touchActionSets;
    private TouchPointData[] touchPointDatas;
    private final TouchOrderQueue touchOrderQueue;

    private int maxTouchPoint;

    private String touchActionSetKey;

    private boolean touch = false;
    private boolean ready = false;
    private boolean check = false;

    private TouchManager()
    {
        touchSensor = new TouchSensor();
        touchOrderQueue = new TouchOrderQueue();
        touchActionFinder = new TouchActionFinder();
        touchPointChecker = new TouchPointChecker();

        changeTouchActionSetCount(1);
        setMaxTouchPoint(2);
    }
    public static TouchManager getInstance()
    {
        if (touchManager == null)
            touchManager = new TouchManager();
        return touchManager;
    }
    public void AddTouchActionSet(String setName)
    {
        check = false;

        int i = 0;
        while (i < touchActionSets.length)
        {
            if (touchActionSets[i] == null)
            {
                touchActionSets[i] = new TouchActionSet(setName);
                break;
            }
            else {
                try {
                    if (touchActionSets[i].CheckKey(setName))
                        throw new Exception();
                } catch (Exception e) {
                    Log.v("Exception", "TouchActionSet Key already exist" + String.valueOf(setName));
                    break;
                }
            }
            i++;
        }
        if (i == touchActionSets.length)
        {
            try
            {
                throw new Exception();
            } catch (Exception e) {
                Log.v("Exception", "TouchActionSet is full");
            }
        }
    }
    public void AddTouchAction(String key, TouchAction touchAction)
    {
        check = false;

        try {
            int i = 0;
            while (i < touchActionSets.length)
            {
                if(touchActionSets[i].CheckKey(key))
                {
                    touchActionSets[i].Add(touchAction);
                    break;
                }
                i++;
            }

            if (i == touchActionSets.length)
                throw new Exception();
        } catch (Exception e) {
            Log.v("Exception", String.valueOf(key) + " doesn't exist");
        }
    }
    private void Add(final MotionEvent motionEvent, final int id)
    {
        TouchActionLinkedList touchActionLinkedList = new TouchActionLinkedList();
        touchActionLinkedList.setTouchIOInterface(new TouchIOInterface() {
            @Override
            public boolean Action() {
                int i = 0;
                while (i < maxTouchPoint)
                {
                    if(touchPointDatas[i].getId() == -1)
                    {
                        //Log.v("Touch", "AddTouchAction : " + String.valueOf(touchPointData.getId()));
                        touchPointDatas[i].setTouchID(motionEvent, id);
                        touchActionFinder.Add(touchPointDatas[i]);
                        break;
                    }
                    try {
                        if (touchPointDatas[i].getId() == id)
                            throw new Exception();
                    } catch (Exception e) {
                        Log.v("Exception", "Same Touch was input");
                    }
                    i++;
                }

                return true;
            }
        });
        touchOrderQueue.Push(touchActionLinkedList);
    }
    private void Subtract(final int id)
    {
        //Log.v("Touch", "Subtract");
        TouchActionLinkedList touchActionLinkedList = new TouchActionLinkedList();
        touchActionLinkedList.setTouchIOInterface(new TouchIOInterface() {
            @Override
            public boolean Action() {
                boolean pass = false;
                boolean subtract = false;

                int i = 0;
                while (i < touchPointDatas.length)
                {
                    if(touchPointDatas[i].getId() == id)
                    {
                        //Log.v("Touch", "subtract : " + String.valueOf(id));
                        if (!touchPointDatas[i].isPick() && timer.getTime() - touchPointDatas[i].getEventTime() < 100)
                        {
                            //Log.v("Touch", "pass");
                            pass = true;
                            break;
                        }
                        else {
                            //Log.v("Touch", "not Pass : " + String.valueOf(SystemClock.currentThreadTimeMillis() - touchPointData.getClickTime()));
                            touchActionFinder.Subtract(touchPointDatas[i]);
                            touchPointDatas[i].Clear();
                            subtract = true;
                            break;
                        }
                    }
                    i++;
                }
////////////////////////////////////////////////////////////////////////////////////////////////////////////
                if (subtract)
                {
                    int j = i + 1;
                    while (j < touchPointDatas.length)
                    {
                        touchPointDatas[i].Copy(touchPointDatas[j]);
                        touchPointDatas[j].Clear();
                        i++;
                        j = i + 1;

                        throw new Exception();
                        //synchronize process, exception process doesn't finish.
                    }
                }
////////////////////////////////////////////////////////////////////////////////////////////////////////////pointing error will occur
                return !pass;
            }
        });
        touchOrderQueue.Push(touchActionLinkedList);
    }
    public void changeTouchActionSetCount(int count)
    {
        check = false;
        synchronized (touchActionFinder) {
            touchActionFinder.Clear();
        }
        touchActionSets = new TouchActionSet[count];
    }
    public void setTouchActionSet(String key)
    {
        try {
            int i = 0;
            while (i < touchActionSets.length) {
                if (touchActionSets[i].CheckKey(key)) {
                    synchronized (touchActionFinder) {
                        check = false;
                        this.touchActionSetKey = key;
                        synchronized (touchActionFinder) {
                            touchActionFinder.setTouchActionSet(touchActionSets[i]);
                        }
                        break;
                    }
                }
                i++;
            }
            if (i == touchActionSets.length)
                throw new Exception();
        } catch (Exception e) {
            Log.v("Exception", "TouchActionSet doesn't exist");
        }
    }
    public String getTouchActionSetKey() {
        return touchActionSetKey;
    }
    public void setMaxTouchPoint(int maxTouchPoint)
    {
        ready = false;

        check = false;
        synchronized (touchActionFinder)
        {
            this.maxTouchPoint = maxTouchPoint;
            touchPointDatas = new TouchPointData[maxTouchPoint];
            int i = 0;
            while (i < maxTouchPoint)
            {
                touchPointDatas[i] = new TouchPointData();
                i++;
            }
            touchActionFinder.setTouchCounts(maxTouchPoint);
        }

        ready = true;
    }
    private void CheckTouchPointDatas()
    {
        int i = 0;
        while (i < touchPointDatas.length)
        {
            if(touchPointDatas[i].getId() != -1)
                touchPointDatas[i].CheckTouchState();
            else
                break;
            i++;
        }
    }
    public void Clear()
    {
        ready = false;

        check = false;
        synchronized (touchActionFinder)
        {
            touchActionFinder.Clear();
            touchOrderQueue.Clear();

            int i = 0;
            while (i < touchActionSets.length && touchActionSets[i] != null)
            {
                touchActionSets[i] = null;
                i++;
            }

            i = 0;
            while (i < touchPointDatas.length)
            {
                if (touchPointDatas[i].getId() != -1)
                    touchPointDatas[i].Clear();
                i++;
            }
            touchActionSetKey = null;
            touch = false;
        }

        ready = true;
    }
    public boolean isTouch() {
        return touch;
    }
    boolean isReady() {
        if (!check) {
            if (touchActionFinder.isReady() && ready) {
                check = true;
                return true;
            }

            return false;
        }
        else
            return true;
    }

    @Override
    public void start()
    {
        touchActionFinder.start();
        touchPointChecker.start();

        while (!touchActionFinder.isReady())
            Log.v("Loading", "TouchManager loading");
        ready = true;
    }

    @Override
    public void run()
    {
        while (true)
        {
            if (!isReady())
                Log.v("Exception", "TouchManager is not ready");
            else
                touchOrderQueue.Update();
        }
    }

    private class TouchSensor
    {
        private TouchSensor() {
            if (TouchManager.getInstance().isReady())
            {
                ScreenManager.getInstance().getScreenTransmitter().setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                        int id = event.getPointerId(pointerIndex);

                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN: {
                                //Log.v("Touch", "Calculate Down - " + String.valueOf(id));
                                Add(event, id);
                                touch = true;
                                break;
                            }
                            case MotionEvent.ACTION_POINTER_DOWN: {
                                //Log.v("Touch", "Calculate Pointer Down - " + String.valueOf(id));
                                Add(event, id);
                                break;
                            }
                            case MotionEvent.ACTION_UP: {
                                //Log.v("Touch", "Calculate Up");
                                Subtract(id);
                                touch = false;
                                break;
                            }
                            case MotionEvent.ACTION_POINTER_UP:
                            {
                                //Log.v("Touch", "Calculate Pointer Up");
                                Subtract(id);
                                break;
                            }
                        }
                        return true;
                    }
                });
            }
        }
    }

    private interface TouchIOInterface
    {
        boolean Action();
    }

    private class TouchActionLinkedList
    {
        private TouchIOInterface touchIOInterface;
        private TouchActionLinkedList nextTouchActionLinkedList;

        private void setTouchIOInterface(TouchIOInterface touchIOInterface)
        {
            this.touchIOInterface = touchIOInterface;
        }
        private TouchIOInterface getTouchIOInterface()
        {
            return touchIOInterface;
        }
        private void setNextTouchIOInterface(TouchActionLinkedList nextTouchActionLinkedList)
        {
            this.nextTouchActionLinkedList = nextTouchActionLinkedList;
        }
        private TouchActionLinkedList getNextTouchActionLinkedList()
        {
            return nextTouchActionLinkedList;
        }
    }

    private class TouchOrderQueue
    {
        private TouchActionLinkedList baseLinkedList;

        private void Push(TouchActionLinkedList touchActionLinkedList)
        {
            if(baseLinkedList == null) {
                //Log.v("Touch", "Base");
                baseLinkedList = touchActionLinkedList;
            }
            else
            {
                TouchActionLinkedList pointerLinkedList = baseLinkedList;
                while (pointerLinkedList.getNextTouchActionLinkedList() != null)
                {
                    pointerLinkedList = pointerLinkedList.getNextTouchActionLinkedList();
                }
                pointerLinkedList.setNextTouchIOInterface(touchActionLinkedList);
            }
        }
        private void Update()
        {
            if(baseLinkedList != null) {
                if (!baseLinkedList.getTouchIOInterface().Action())
                {
                    TouchActionLinkedList touchActionLinkedList = baseLinkedList;
                    baseLinkedList = baseLinkedList.getNextTouchActionLinkedList();
                    touchActionLinkedList.setNextTouchIOInterface(null);
                    Push(touchActionLinkedList);
                }
                else
                    baseLinkedList = baseLinkedList.getNextTouchActionLinkedList();
            }
        }
        private void Clear()
        {
            baseLinkedList = null;
        }
    }

    private class TouchActionFinder extends Thread
    {
        private TouchPointData[] touchPointDatas;
        private int maxTouchPoint;

        private TouchActionSet touchActionSet;
        private Vector<UITouchAction> uiTouchActions;
        private Vector<TouchActionWithCamera> touchActionWithCameras;

        private TouchAction currentTouchActionWithCamera;

        private boolean add;
        private boolean ready = false;
        private boolean run = false;

        TouchActionFinder() {
            maxTouchPoint = -1;
        }
        void setTouchCounts(int maxTouchPoint)
        {
            ready = false;

            this.maxTouchPoint = maxTouchPoint;
            synchronized (touchPointDatas) {
                touchPointDatas = new TouchPointData[maxTouchPoint];
            }

            ready = true;
        }
        void setTouchPointDatas(TouchPointData[] touchPointDatas)
        {
            ready = false;

            int i = 0;
            int j = 0;
            while (i < touchPointDatas.length && touchPointDatas[i] != null) {
                while (j < maxTouchPoint) {
                    if(this.touchPointDatas[j] == null) {
                        this.touchPointDatas[j] = touchPointDatas[i];
                        break;
                    }
                    j++;
                }
                i++;
            }

            ready = true;
        }
        void setTouchActionSet(TouchActionSet touchActionSet) {
            ready = false;

            this.touchActionSet = touchActionSet;
            uiTouchActions = touchActionSet.getUiTouchActions();
            touchActionWithCameras = touchActionSet.getTouchActionWithCameras();

            ready = true;
        }
        private void ClearTouchPointDatas()
        {
            ready = false;

            int i = 0;
            while (i < maxTouchPoint)
            {
                touchPointDatas[i] = null;
                i++;
            }

            ready = true;
        }
        void Add(TouchPointData touchPointData)
        {
            //Log.v("Touch", "FindAction First");
            while (!ready) { }

            TouchPointData[] tempTouchPointDatas = new TouchPointData[] { touchPointData };
            boolean OK = false;

            Iterator<UITouchAction> touchActionIterator = uiTouchActions.iterator();
            TouchAction uiTouchAction;
            while (touchActionIterator.hasNext())
            {
                uiTouchAction = touchActionIterator.next();
                if(uiTouchAction.Check(tempTouchPointDatas))
                {
                    touchPointData.setPick(true);
                    uiTouchAction.setTouchPointDatas(tempTouchPointDatas);

                    Thread thread = new Thread(uiTouchAction);
                    thread.start();

                    OK = true;
                    //Log.v("Touch", "OK");
                    break;
                }
            }

            if(!OK)
            {
                add = true;
                //Log.v("Touch", "Not OK");

                if(currentTouchActionWithCamera != null)
                {
                    if(currentTouchActionWithCamera.isFlag())
                    {
                        if (currentTouchActionWithCamera.isReleaseTouch())
                        {
                            setTouchPointDatas(currentTouchActionWithCamera.getTouchPointDatas());
                            currentTouchActionWithCamera.Stop(Type.TouchActionType.INPUT);
                            currentTouchActionWithCamera = null;
                        }
                        else
                        {
                            if (currentTouchActionWithCamera.isWait())
                                setTouchPointDatas(currentTouchActionWithCamera.getTouchPointDatas());
                            else
                                currentTouchActionWithCamera = null;
                        }
                    }
                    else
                        currentTouchActionWithCamera = null;
                }

                int i = 0;
                while (i < maxTouchPoint)
                {
                    if(touchPointDatas[i] == null)
                    {
                        touchPointDatas[i] = touchPointData;
                        break;
                    }
                    i++;
                }
                add = false;
            }
        }
        void Subtract(TouchPointData touchPointData)//synchronize error??
        {
            //Log.v("Touch", "Finder Subtract");
            while (!ready) { }

            int i = 0;
            while (i < maxTouchPoint)
            {
                if (touchPointDatas[i] != null && touchPointDatas[i].getId() == touchPointData.getId()) {
                    touchPointDatas[i] = null;
                    break;
                }
                i++;
            }

            int j;
            while (i < maxTouchPoint)
            {
                if(touchPointDatas[i] == null)
                {
                    j = i;
                    while (j < maxTouchPoint)
                    {
                        if(touchPointDatas[j] != null) {
                            touchPointDatas[i] = touchPointDatas[j];
                            touchPointDatas[j] = null;
                            break;
                        }
                        j++;
                    }
                    if(!(j < maxTouchPoint))
                        break;
                }
                i++;
            }
        }
        private void FindAction()
        {
            //Log.v("Touch", "FindAction");

            if(touchPointDatas[0] != null)
            {
                if(currentTouchActionWithCamera == null)
                {
                    TouchAction touchActionWithCamera;
                    Iterator<TouchActionWithCamera> touchActionWithCameraIterator = touchActionWithCameras.iterator();
                    while (touchActionWithCameraIterator.hasNext())
                    {
                        touchActionWithCamera = touchActionWithCameraIterator.next();
                        if(touchActionWithCamera.Check(touchPointDatas))
                        {
                            int i = 0;
                            while (i < touchPointDatas.length && touchPointDatas[i] != null)
                            {
                                touchPointDatas[i].setPick(true);
                                i++;
                            }

                            touchActionWithCamera.setTouchPointDatas(touchPointDatas);
                            this.currentTouchActionWithCamera = touchActionWithCamera;
                            ClearTouchPointDatas();

                            Thread thread = new Thread(touchActionWithCamera);
                            thread.start();

                            break;
                        }
                    }
                }
                else
                {
                    if(currentTouchActionWithCamera.Check(touchPointDatas))
                    {
                        int i = 0;
                        while (i < touchPointDatas.length && touchPointDatas[i] != null)
                        {
                            touchPointDatas[i].setPick(true);
                            i++;
                        }

                        currentTouchActionWithCamera.setTouchPointDatas(touchPointDatas);
                        ClearTouchPointDatas();

                        Thread thread = new Thread(currentTouchActionWithCamera);
                        thread.start();
                    }
                }
            }
        }
        private void CheckTouchPoint()
        {
            int i = 0;
            while (i < maxTouchPoint)
            {
                if(touchPointDatas[i] != null)
                {
                    if (touchPointDatas[i].getId() == -1)
                    {
                        touchPointDatas[i] = null;
                    }
                }
                i++;
            }

            int j;
            i = 0;
            while (i < maxTouchPoint)
            {
                if(touchPointDatas[i] == null)
                {
                    j = i;
                    while (j < maxTouchPoint)
                    {
                        if(touchPointDatas[j] != null) {
                            touchPointDatas[i] = touchPointDatas[j];
                            touchPointDatas[j] = null;
                            break;
                        }
                        j++;
                    }
                    if(!(j < maxTouchPoint))
                        break;
                }
                i++;
            }
        }
        void ClearTouchAction()
        {
            currentTouchActionWithCamera = null;
        }
        void Clear()
        {
            ready = false;

            touchActionSet = null;
            uiTouchActions = null;
            touchActionWithCameras = null;

            ready = true;
        }
        boolean isReady() {
            if (maxTouchPoint == -1) {
                try {
                    throw new Exception();
                } catch (Exception e) {
                    Log.v("Exception", "TouchActionFinder's maxTouchPoint is not set");
                }
                return false;
            }
            else if (touchActionSet == null)
            {
                try {
                    throw new Exception();
                } catch (Exception e) {
                    Log.v("Exception", "TouchActionFinder's TouchActionSet is not set");
                }
                return false;
            }
            return ready;
        }
        boolean checkTouchActionSet(TouchActionSet touchActionSet)
        {
            return this.touchActionSet.CheckKey(touchActionSet.getKey());
        }

        @Override
        public void run()
        {
            while (true)
            {
                if (!isReady())
                {
                    if (maxTouchPoint != -1 && touchActionSet != null)
                        Log.v("Exception", "TouchActionFinder is not ready");
                }
                else {
                    SystemClock.sleep(100);
                    if (touchPointDatas[0] != null && !add) {
                        CheckTouchPoint();
                        try {
                            FindAction();
                        } catch (Exception e) {
                            Log.v("Exception", "Find Calculate Exception", e);
                        }
                    }
                }
            }
        }
    }

    private class TouchPointChecker extends Thread {

        @Override
        public void run()
        {
            while (true)
            {
                CheckTouchPointDatas();
                SystemClock.sleep(100);
            }
        }
    }

    public class TouchPointData
    {
        private MotionEvent motionEvent;
        private int id;
        private long eventTime;
        private Type.TouchState touchState;
        private int[] beforeTouchPoint;
        private int[] movement;

        private final int maxMove = 3;
        private final long maxTime = 500;

        private boolean pick;

        TouchPointData()
        {
            motionEvent = null;
            id = -1;
            eventTime = -1;
            touchState = null;
            beforeTouchPoint = new int[2];
            beforeTouchPoint[0] = -1;
            beforeTouchPoint[1] = -1;
            movement = new int[2];
            movement[0] = -1;
            movement[1] = -1;
            pick = false;
        }
        void Copy(TouchPointData touchPointData)
        {
            motionEvent = touchPointData.motionEvent;
            id = touchPointData.id;
            eventTime = touchPointData.eventTime;
            touchState = touchPointData.touchState;
            beforeTouchPoint[0] = touchPointData.beforeTouchPoint[0];
            beforeTouchPoint[1] = touchPointData.beforeTouchPoint[1];
            movement[0] = touchPointData.movement[0];
            movement[1] = touchPointData.movement[1];
            pick = touchPointData.pick;
        }
        void setTouchID(MotionEvent motionEvent, int id)
        {
            this.motionEvent = motionEvent;
            this.id = id;
            eventTime = timer.getTime();
            beforeTouchPoint[0] = (int)motionEvent.getX(motionEvent.findPointerIndex(id));
            beforeTouchPoint[1] = (int)motionEvent.getY(motionEvent.findPointerIndex(id));
        }
        void CheckTouchState()
        {
        /*if((motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP || (motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP)
        {
            touchState = Type.TouchState.UP;
        } else */
            try {
                int[] tmpTouch = new int[] { (int)motionEvent.getX(motionEvent.findPointerIndex(id)), (int)motionEvent.getY(motionEvent.findPointerIndex(id))};//cause null pointer exception
                int distance = Distance(tmpTouch, beforeTouchPoint);

                if(distance < maxMove && touchState != Type.TouchState.MOVE) {
                    if(timer.getTime() - eventTime > maxTime) {
                        touchState = Type.TouchState.LONGTOUCH;
                    }
                    else
                        touchState = Type.TouchState.DOWN;
                }
                else {
                    touchState = Type.TouchState.MOVE;
                    eventTime = timer.getTime();
                    movement[0] = tmpTouch[0] - beforeTouchPoint[0];
                    movement[1] = tmpTouch[1] - beforeTouchPoint[1];
                    beforeTouchPoint[0] = tmpTouch[0];
                    beforeTouchPoint[1] = tmpTouch[1];
                }
            } catch (Exception e)
            {
                Log.v("Exception", "Touch Point Data Exception");
            }
        }
        void Clear()
        {
            id = -1;
            motionEvent = null;
            eventTime = -1;
            touchState = null;
            beforeTouchPoint[0] = -1;
            beforeTouchPoint[1] = -1;
            movement[0] = -1;
            movement[1] = -1;
            pick = false;
        }
        public Type.TouchState getTouchState() {
            return touchState;
        }
        int getId()
        {
            return id;
        }
        public int[] getTouchPoint()
        {
            return beforeTouchPoint;
        }
        public int[] getMovement()
        {
            return movement;
        }
        boolean isPick() {
            return pick;
        }
        void setPick(boolean pick) {
            this.pick = pick;
        }
        public long getEventTime()
        {
            return eventTime;
        }
    }

    private interface TouchCheckInterFace
    {
        boolean DetailCondition();
    }

    class TouchActionSet {

        private final String key;//generic?
        private final Vector<TouchActionWithCamera> touchActionWithCameras;
        private final Vector<UITouchAction> uiTouchActions;
        private boolean reference;

        TouchActionSet(String key)
        {
            this.key = key;
            touchActionWithCameras = new Vector<>();
            uiTouchActions = new Vector<>();
            reference = false;
        }
        void Add(TouchAction touchAction)
        {
            if (reference) {
                if (touchActionFinder.checkTouchActionSet(this)) {
                    try {
                        throw new Exception();
                    }
                    catch(Exception e){
                        Log.v("Exception", String.valueOf(key) + " is already referenced", e);
                    }
                }
                else {
                    reference = false;

                    if(touchAction.getTouchType() == Type.TouchType.CAMERA) {
                        touchActionWithCameras.add((TouchActionWithCamera) touchAction);
                    }
                    else {
                        uiTouchActions.add((UITouchAction) touchAction);
                    }
                }
            }
            else
            {
                if(touchAction.getTouchType() == Type.TouchType.CAMERA) {
                    touchActionWithCameras.add((TouchActionWithCamera) touchAction);
                }
                else {
                    uiTouchActions.add((UITouchAction) touchAction);
                }
            }
        }
        Vector<TouchActionWithCamera> getTouchActionWithCameras()
        {
            reference = true;
            return touchActionWithCameras;
        }
        Vector<UITouchAction> getUiTouchActions()
        {
            reference = true;
            return uiTouchActions;
        }
        boolean CheckKey(String key)
        {
            return this.key.equals(key);
        }
        String getKey()
        {
            return key;
        }
    }

    public abstract class TouchAction implements Runnable, TouchManager.TouchCheckInterFace {

        private final int activateTouchCounts;
        //private boolean persuadeLongTouch;
        private final Type.TouchType touchType;
        private final boolean releaseTouch;

        protected TouchManager.TouchPointData[] touchPointDatas;

        private int beforeCount;

        private boolean flag;
        private boolean wait;

        protected TouchAction(int activateTouchCounts, boolean persuadeLongTouch, Type.TouchType touchType, boolean releaseTouch)
        {
            this.activateTouchCounts = activateTouchCounts;

            touchPointDatas = new TouchManager.TouchPointData[activateTouchCounts];

            //this.persuadeLongTouch = persuadeLongTouch;
            this.touchType = touchType;
            this.releaseTouch = releaseTouch;
            wait = true;
        }
        Type.TouchType getTouchType() {
            return touchType;
        }
        protected void Stop(Type.TouchActionType touchActionType)
        {
            switch (touchActionType)
            {
                case INPUT:
                {
                    //Log.v("TouchAction", "Input");
                    ActionChange();
                    Clear(touchActionType);
                    flag = false;
                    break;
                }
                case OUTPUT:
                {
                    //Log.v("TouchAction", "Output");
                    if(releaseTouch || beforeCount == 0) {
                        ActionUp();
                        flag = false;
                    }
                    Clear(touchActionType);
                    break;
                }
                case CHANGE:
                {
                    //Log.v("TouchAction", "Change");
                    ActionChange();
                    touchActionFinder.setTouchPointDatas(touchPointDatas);
                    touchActionFinder.ClearTouchAction();
                    Clear(touchActionType);
                    flag = false;
                    break;
                }
            }
        }
        private void Clear(Type.TouchActionType touchActionType)
        {
            switch (touchActionType)
            {
                case OUTPUT:
                {
                    if(releaseTouch)
                    {
                        int i = 0;
                        while (i < activateTouchCounts) {
                            touchPointDatas[i].setPick(false);
                            touchPointDatas[i] = null;
                            i++;
                        }
                    }
                    else
                    {
                        int i = 0;
                        while (i < activateTouchCounts)
                        {
                            if(touchPointDatas[i] != null && touchPointDatas[i].getId() == -1)
                            {
                                touchPointDatas[i].setPick(false);
                                touchPointDatas[i] = null;
                                break;
                            }
                            i++;
                        }
                        while (i < activateTouchCounts)
                        {
                            int j = i;
                            while (j < activateTouchCounts)
                            {
                                if(touchPointDatas[j] != null)
                                {
                                    touchPointDatas[i] = touchPointDatas[j];
                                    touchPointDatas[j] = null;
                                }
                                j++;
                            }
                            i++;
                        }
                    }
                    break;
                }
                default:
                {
                    int i = 0;
                    while (i < activateTouchCounts) {
                        touchPointDatas[i].setPick(false);
                        touchPointDatas[i] = null;
                        i++;
                    }
                    break;
                }
            }
        }
        boolean Check(TouchManager.TouchPointData[] touchPointDatas) {
            //boolean longTouch = false;
            beforeCount = 0;
            int i = 0;
            while (i < touchPointDatas.length) {
                if (touchPointDatas[i] != null && touchPointDatas[i].getId() != -1) {
                    //touchPointDatas[i].CheckTouchState();
                /*if(touchPointDatas[i].getTouchState() == Type.TouchState.LONGTOUCH)
                    longTouch = true;*/
                    beforeCount++;
                }
                i++;
            }

            //return beforeCount == activateTouchCounts/* && !(!persuadeLongTouch && longTouch)*/;
            return beforeCount == activateTouchCounts && DetailCondition();
        }
        TouchManager.TouchPointData[] getTouchPointDatas() {
            return touchPointDatas;
        }
        void setTouchPointDatas(TouchManager.TouchPointData[] touchPointDatas) {
            int i = 0;
            while (i < activateTouchCounts) {
                this.touchPointDatas[i] = touchPointDatas[i];
                i++;
            }
        }
        boolean isReleaseTouch()
        {
            return releaseTouch;
        }
        boolean isFlag()
        {
            return flag;
        }
        boolean isWait()
        {
            return wait;
        }

        @Override
        public void run()
        {
            flag = true;
            wait = false;
            while (flag) {
                if (Check(touchPointDatas))
                    ActionDown();
                else {
                    if (beforeCount < activateTouchCounts) {
                        Stop(Type.TouchActionType.OUTPUT);
                        wait = beforeCount != 0;
                    } else
                        Stop(Type.TouchActionType.CHANGE);
                }
                SystemClock.sleep(100);
            }
        }

        abstract protected void ActionDown();
        abstract protected void ActionUp();
        abstract protected void ActionChange();
    }
}
