package seaweed.aeproject.AlphaEngine.PhysicsPackage.ForceGenerator;

import seaweed.aeproject.AlphaEngine.ETC.CustomMath;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.PhysicsPackage.Material;
import seaweed.aeproject.AlphaEngine.PhysicsPackage.ForceGenerator;

import static seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineActivity.mGameObjectComponent;
import static seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.PhysicsSystem.INTERVAL_TIME;

public class SteadyForceGenerator extends ForceGenerator {

    protected Material material;
    protected CustomMath.Vector1X4 force;
    protected CustomMath.Vector1X4 point;
    private boolean general;
    protected float duration;//if duration is -1 it means forever

    public SteadyForceGenerator(GameObject gameObject, CustomMath.Vector1X4 point, CustomMath.Vector1X4 force, boolean general, float duration) {
        super(gameObject, true);
        this.point = point;
        this.force = force.Scaled(INTERVAL_TIME);
        this.general = general;
        this.duration = duration;
    }
    public void TimePass()
    {
        if (duration != Integer.MAX_VALUE)
            duration -= INTERVAL_TIME;
    }
    public float getDuration()
    {
        return duration;
    }

    @Override
    public void Apply() {
        if (material == null)
            material = (Material) gameObject.getComponent(mGameObjectComponent);
        material.AddForce(general, point, force);
    }
}
