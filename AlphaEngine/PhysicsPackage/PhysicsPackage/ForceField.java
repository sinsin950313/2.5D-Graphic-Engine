package seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObjectPackage.GameObjectComponentPackage.PhysicsPackage;

import seaweed.aeproject.AlphaEngine.ETC.CustomMath;
import seaweed.aeproject.AlphaEngine.ETC.Type;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObjectPackage.GameObjectComponentPackage.Volume;

public class ForceField extends Volume {

    public ForceField(CustomMath.Vector1X4 position, String name, Type.VolumeType volumeType) {
        super(new GameObject(position, name), false, volumeType);
    }

    @Override
    protected void setVertex() {

    }

    @Override
    protected boolean isPointInVolume(CustomMath.Vector1X4 checkPoint) {
        return false;
    }
}
