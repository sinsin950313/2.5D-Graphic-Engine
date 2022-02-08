package seaweed.aeproject.AlphaEngine.PhysicsPackage;

import java.util.Vector;

import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.VolumePackage.Volume;

import static seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineActivity.vGameObjectComponent;
import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomDataType.Coordinate3D.*;
import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.*;

public class ContactData
{
    private GameObject mainGameObject;
    private GameObject subGameObject;
    private Vector<Vector1X4> vertexes;
    private Vector<Float> penetrationDepths;
    private Vector1X4 collidePlaneVector;

    public ContactData(GameObject mainGameObject, GameObject subGameObject, Vector<Vector1X4> vertexes, Vector<Float> penetrationDepths, Vector1X4 collidePlaneVector)
    {
        this.mainGameObject = mainGameObject;
        this.subGameObject = subGameObject;

        this.vertexes = new Vector<>(vertexes);
        this.penetrationDepths = new Vector<>(penetrationDepths);
        this.collidePlaneVector = new Vector1X4(collidePlaneVector);
    }
    public GameObject getMainGameObject()
    {
        return mainGameObject;
    }
    public GameObject getSubGameObject() {
        return subGameObject;
    }
    public Vector1X4 getVertexes(int index) {
        return vertexes.get(index);
    }//keep this
    public int getVertexSize()
    {
        return vertexes.size();
    }
    public float getPenetrationDepth(int index) {
        return penetrationDepths.get(index);
    }
    public int getPenetrationDepthSize()
    {
        return penetrationDepths.size();
    }
    public Vector1X4 getCollidePlaneVector() {
    return collidePlaneVector;
}

    public void Clear()
    {
        mainGameObject = null;
        subGameObject = null;
        vertexes = null;
        collidePlaneVector = null;
        penetrationDepths = null;
    }
    public boolean isEmpty() {
        return vertexes == null || vertexes.size() == 0;
    }
}