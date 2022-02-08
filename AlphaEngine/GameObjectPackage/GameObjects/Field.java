package seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObjects;

import java.util.Vector;

import seaweed.aeproject.AlphaEngine.ETC.CustomMath;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.GraphicPackage.FieldBitmapGraphic;
import seaweed.aeproject.AlphaEngine.PhysicsPackage.Material;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObjectPackage.GameObjectComponentPackage.VolumePackage.HalfPlaneVolume;

//if want to make BitmapGraphic stage, use field class. else use custom volume
//what should I consider if use custom volume?
public class Field extends GameObject {

    private Vector<GameObject> structure;
    private String name;

    public Field(String name, int width, int deep)
    {
        super("F");
        this.name = name;
        setPosition(new CustomMath.Vector1X4(0, 0, 0, 1));
        structure = new Vector<>();
        AddComponent(new HalfPlaneVolume(this, width, deep));
        AddComponent(new FieldBitmapGraphic(this, name));
        AddComponent(new Material(this, Integer.MAX_VALUE, false));
    }

    public void setStructure(GameObject gameObject, CustomMath.Matrix4X4 position, boolean hold)
    {
        gameObject.setObjectMatrix(position);
        if(hold)
            gameObject.setParentGameObject(this);
        structure.add(gameObject);
    }
    public String getName() {
        return name;
    }
    //public abstract void Load_Map_Setting();
 }
