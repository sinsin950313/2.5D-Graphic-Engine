package seaweed.aeproject.AlphaEngine.PhysicsPackage.ForceGenerator;

import seaweed.aeproject.AlphaEngine.ETC.CustomMath;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.PhysicsPackage.Material;
import seaweed.aeproject.AlphaEngine.PhysicsPackage.ForceGenerator;

import static seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineActivity.mGameObjectComponent;

public class TemporaryForceGenerator extends ForceGenerator {

    protected boolean general;
    private CustomMath.Vector1X4 force;
    private CustomMath.Vector1X4 point;

    public TemporaryForceGenerator(GameObject gameObject, CustomMath.Vector1X4 point, CustomMath.Vector1X4 force, boolean general) {
        super(gameObject, false);
        this.point = point;
        this.force = force;
        this.general = general;
    }
    protected TemporaryForceGenerator(GameObject gameObject, boolean general) {
        super(gameObject, false);
        this.general = general;
    }

    @Override
    public void Apply() {
        Material material = (Material) gameObject.getComponent(mGameObjectComponent);
        material.AddForce(general, point, force);
    }
}
