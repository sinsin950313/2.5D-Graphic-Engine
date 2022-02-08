package seaweed.aeproject.AlphaEngine.PhysicsPackage.ForceGenerator.SteadyForceGeneratorPackage;

import seaweed.aeproject.AlphaEngine.ETC.CustomMath;
import seaweed.aeproject.AlphaEngine.PhysicsPackage.ForceGenerator.SteadyForceGenerator;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.PhysicsPackage.Material;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObjectPackage.GameObjectComponentPackage.TriggersPackage.GravityTrigger;

import static seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineActivity.mGameObjectComponent;

public class GravityGenerator extends SteadyForceGenerator {

    private boolean contactWithGround;

    public GravityGenerator(GameObject gameObject, float mass) {
        super(gameObject, gameObject.getObjectMatrix().qVector(), new CustomMath.Vector1X4(0, -980 * mass, 0, 0), true, Integer.MAX_VALUE);
        contactWithGround = false;
        new GravityTrigger(this);
    }
    public boolean isContactWithGround() {
        return contactWithGround;
    }
    public void setContactWithGround(boolean contactWithGround) {
        this.contactWithGround = contactWithGround;
    }

    @Override
    public void Apply() {
        if (material == null)
            material = (Material) gameObject.getComponent(mGameObjectComponent);
        material.AddForce(true, point, force);
    }
}
