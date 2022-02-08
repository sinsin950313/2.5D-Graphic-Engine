package seaweed.aeproject.AlphaEngine.TriggerPackage;

import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.TriggerChecker;
import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.TriggerChecker.TriggerOrigin;

public abstract class Trigger extends TriggerOrigin {

    public Trigger()
    {
        TriggerChecker.getInstance().super();
    }
}
