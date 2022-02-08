package seaweed.aeproject.AlphaEngine.GameObjectPackage;

import seaweed.aeproject.AlphaEngine.ETC.Type;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObject.GameObjectComponentInterface;

public class CustomComponentInterface implements GameObjectComponentInterface {
    @Override
    public boolean CheckComponent(Type.GameObjectComponent componentType) {
        return componentType == Type.GameObjectComponent.CUSTOM;
    }
}
