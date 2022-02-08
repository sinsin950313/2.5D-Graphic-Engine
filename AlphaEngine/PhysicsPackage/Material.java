package seaweed.aeproject.AlphaEngine.PhysicsPackage;

import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.PhysicsSystem;

public class Material extends PhysicsSystem.MaterialOrigin
{
    public Material(GameObject gameObject, int mass, boolean gravity) {
        PhysicsSystem.getInstance().super(gameObject, mass, gravity);
    }
}
