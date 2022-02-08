package seaweed.aeproject.AlphaEngine.VolumePackage;

import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.PhysicsSystem;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObject;

public abstract class Volume extends PhysicsSystem.VolumeOrigin {

    public Volume(GameObject upperGameObject, boolean rigidBody) {
        PhysicsSystem.getInstance().super(upperGameObject, rigidBody);
    }
}
