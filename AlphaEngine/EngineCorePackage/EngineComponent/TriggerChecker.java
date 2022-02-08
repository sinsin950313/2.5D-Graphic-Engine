package seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent;

import java.util.Iterator;
import java.util.Vector;

import seaweed.aeproject.AlphaEngine.ETC.Type;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObject.GameObjectComponentInterface;

public class TriggerChecker extends Thread {

    private static TriggerChecker triggerChecker = new TriggerChecker();

    private Vector<TriggerOrigin> triggers;
    private Vector<TriggerOrigin> addTrigger;
    private Vector<TriggerOrigin> removeTrigger;

    private TriggerChecker()
    {
        triggers = new Vector<>();
        addTrigger = new Vector<>();
        removeTrigger = new Vector<>();
    }
    public static TriggerChecker getInstance()
    {
        if(triggerChecker == null)
            triggerChecker = new TriggerChecker();
        return triggerChecker;
    }
    private void Add(TriggerOrigin trigger)
    {
        addTrigger.add(trigger);
    }
    private void Remove(TriggerOrigin trigger)
    {
        removeTrigger.add(trigger);
    }
    public void Clear()
    {
        triggers.clear();
        addTrigger.clear();
        removeTrigger.clear();
    }

    @Override
    public void run()
    {
        Iterator<TriggerOrigin> triggerIterator;
        TriggerOrigin tmpTrigger;
        while (true)
        {
            triggers.addAll(addTrigger);
            addTrigger.clear();

            triggerIterator = triggers.iterator();
            while(triggerIterator.hasNext())
            {
                tmpTrigger = triggerIterator.next();
                if(tmpTrigger.isActivate())
                {
                    if (tmpTrigger.CheckTrigger()) {
                        tmpTrigger.Action();

                        if (tmpTrigger.CheckPause())
                            tmpTrigger.setActivate(false);

                        if (tmpTrigger.CheckRemove())
                            Remove(tmpTrigger);
                    }
                }
                else
                {
                    if (tmpTrigger.CheckRestart())
                        tmpTrigger.setActivate(true);
                }
            }

            triggerIterator = removeTrigger.iterator();
            while (triggerIterator.hasNext())
            {
                tmpTrigger = triggerIterator.next();
                triggers.remove(tmpTrigger);
            }
            removeTrigger.clear();
        }
    }

    public abstract class TriggerOrigin implements GameObjectComponentInterface {

        private boolean activate;

        protected TriggerOrigin()
        {
            this.activate = true;
            TriggerChecker.getInstance().Add(this);
        }
        boolean isActivate() {
            return activate;
        }
        public void setActivate(boolean activate)
        {
            this.activate = activate;
        }
        public void RemoveTrigger()
        {
            Remove(this);
        }//consider transaction

        //remove this if need when action active
        abstract protected boolean CheckTrigger();
        abstract protected void Action();
        abstract protected boolean CheckPause();
        abstract protected boolean CheckRestart();
        abstract protected boolean CheckRemove();

        @Override
        public boolean CheckComponent(Type.GameObjectComponent gameObjectComponentType) {
            return gameObjectComponentType == Type.GameObjectComponent.TRIGGER;
        }
    }
}
