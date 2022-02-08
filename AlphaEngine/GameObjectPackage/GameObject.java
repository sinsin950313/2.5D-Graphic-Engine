package seaweed.aeproject.AlphaEngine.GameObjectPackage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Iterator;
import java.util.Vector;

import seaweed.aeproject.AlphaEngine.ETC.Type;

import static seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineActivity.vGameObjectComponent;
import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.*;
import seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomDataType.Coordinate3D.*;
import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.ScreenManager;
import seaweed.aeproject.AlphaEngine.PhysicsPackage.Material;
import seaweed.aeproject.AlphaEngine.VolumePackage.Volume;

public class GameObject {

    protected GameObject parentGameObject;//if parentGameObject is null, this GameObject is parent GameObject.
    protected Matrix4X4 objectMatrix;
    private final String name;

    private GameObjectComponentManager componentManager;

    public GameObject(String name)
    {
        objectMatrix = new Matrix4X4();
        this.name = name;

        componentManager = new GameObjectComponentManager();
    }
    public GameObject(Vector1X4 position, String name)
    {
        objectMatrix = new Matrix4X4();
        setPosition(position, true);
        this.name = name;

        componentManager = new GameObjectComponentManager();
    }
    public Matrix4X4 getObjectMatrix()
    {
        if(parentGameObject != null) {
            Matrix4X4 matrix4X4 = new Matrix4X4();
            CustomCalculate.Multiply(objectMatrix, parentGameObject.getObjectMatrix(), matrix4X4);
            return matrix4X4;
        }
        return objectMatrix;
    }
    public void Rotate(Vector1X4 axis, float degree)
    {
        objectMatrix.Rotate(axis, degree);
    }
    public void Rotate(Vector1X4 point, Vector1X4 axis, int degree)
    {
        objectMatrix.Rotate(point, axis, degree);
    }
    public void setPosition(Vector1X4 position, boolean global)
    {
        if (global && hasParent())
        {
            Matrix4X4 matrix4X4 = new Matrix4X4();
            CustomCalculate.Inverse(parentGameObject.getObjectMatrix(), matrix4X4);
            CustomCalculate.Multiply(position, matrix4X4, position);
        }
        objectMatrix.setQVector(position);
    }
    public void setParentGameObject(GameObject gameObject)//consider independence
    {
        if (parentGameObject != null) {
            try {
                throw new Exception();
            } catch (Exception e) {
                Log.v("Exception", "GameObject : " + String.valueOf(name) + "already has parent");
            }
        }
        else {
            parentGameObject = gameObject;
            Matrix4X4 matrix4X4 = new Matrix4X4();
            CustomCalculate.Inverse(parentGameObject.getObjectMatrix(), matrix4X4);
            CustomCalculate.Multiply(objectMatrix, matrix4X4, objectMatrix);

            if (getComponent(vGameObjectComponent, null) != null && parentGameObject.getComponent(vGameObjectComponent, null) != null)
                PhysicsSpace.getInstance().JointBoundingVolume(gameObject, this);
        }
    }
    public void AddComponent(GameObjectComponentInterface gameObjectComponent, Object key)
    {
        componentManager.Add(gameObjectComponent, key);
    }
    public GameObjectComponentInterface getComponent(Type.GameObjectComponent componentType, @Nullable Object key)
    {
        return componentManager.getComponent(componentType, key);
    }
    public boolean hasParent()
    {
        return parentGameObject != null;
    }

    public interface GameObjectComponentInterface
    {
        boolean CheckComponent(Type.GameObjectComponent componentType);
    }

    private class GameObjectComponentManager
    {
        private GameObjectComponentInterface volume;
        private GameObjectComponentInterface material;
        private GameObjectComponentInterface texture;
        private Vector<GameObjectComponent> etc;
        private Iterator<GameObjectComponent> componentIterator;

        GameObjectComponentManager()
        {
            etc = new Vector<>();
        }
        void Add(GameObjectComponentInterface component, @Nullable Object key)
        {
            String errorLog = null;
            try {
                if (component instanceof Volume) {
                    if (volume == null)
                        volume = component;
                    else {
                        errorLog = "Volume already exist";
                        throw new Exception();
                    }
                }
                else if (component instanceof Material) {
                    if (material == null)
                        material = component;
                    else {
                        errorLog = "Material already exist";
                        throw new Exception();
                    }
                }
                else if (component instanceof ScreenManager.Texture) {
                    if (texture == null)
                        texture = component;
                    else {
                        errorLog = "Texture already exist";
                        throw new Exception();
                    }
                }
                else
                {
                    if (key != null)
                        etc.add(new GameObjectComponent(component, key));
                    else {
                        errorLog = "Component can't register without key";
                        throw new Exception();
                    }
                }
            } catch (Exception e)
            {
                Log.v("Exception", String.valueOf(errorLog));
            }
        }
        GameObjectComponentInterface getComponent(Type.GameObjectComponent componentType, @Nullable Object key)
        {
            GameObjectComponentInterface component = null;

            try {
                if (componentType == Type.GameObjectComponent.VOLUME)
                    component = volume;
                else if (componentType == Type.GameObjectComponent.MATERIAL)
                    component = material;
                else if (componentType == Type.GameObjectComponent.GRAPHIC)
                    component = texture;
                else {
                    GameObjectComponent tempComponent;
                    componentIterator = etc.iterator();
                    while (componentIterator.hasNext()) {
                        tempComponent = componentIterator.next();
                        if (tempComponent.Check(componentType) && tempComponent.Check(key)) {
                            component = tempComponent.getComponent();
                            break;
                        }
                    }
                }

                if (component == null)
                    throw new Exception();
            } catch (Exception e) {
                Log.v("Exception", "Component : " + String.valueOf(componentType) + ", key : " + String.valueOf(key) + "does not exist");
            }

            componentIterator = null;
            return component;
        }

        private class GameObjectComponent {

            private GameObjectComponentInterface component;
            private Object key;

            GameObjectComponent(GameObjectComponentInterface component, @NonNull Object key)
            {
                this.component = component;
                this.key = key;
            }
            boolean Check(Type.GameObjectComponent componentType)
            {
                return component.CheckComponent(componentType);
            }
            boolean Check(Object key)
            {
                return this.key.equals(key);
            }
            GameObjectComponentInterface getComponent()
            {
                return component;
            }
        }
    }
}
