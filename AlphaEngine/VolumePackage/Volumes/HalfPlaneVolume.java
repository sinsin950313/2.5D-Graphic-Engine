package seaweed.aeproject.AlphaEngine.VolumePackage.Volumes;

import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.VolumePackage.Volume;

import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomDataType.Coordinate3D.*;
import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.*;

public class HalfPlaneVolume extends Volume {
//make small pieces
    private int width, deep;

    public HalfPlaneVolume(GameObject upperGameObject) {
        super(upperGameObject, true);
        this.width = 2;
        this.deep = 2;

        setVertex();
    }
    public HalfPlaneVolume(GameObject upperGameObject, int width, int deep) {
        super(upperGameObject, true);
        this.width = width;
        this.deep = deep;

        setVertex();
    }
    public void setScale(int width, int deep)
    {
        this.width = width;
        this.deep = deep;

        setVertex();
    }

    @Override
    protected void setVertex() {
        vertexes = new Vector1X4[4];
        //LeftUp, LeftDown, RightUp, RightDown
        vertexes[0] = new Vector1X4(-width/2, 0, deep/2, 1);
        vertexes[1] = new Vector1X4(-width/2, 0, -deep/2, 1);
        vertexes[2] = new Vector1X4(width/2, 0, deep/2, 1);
        vertexes[3] = new Vector1X4(width/2, 0, -deep/2, 1);

        /*int i = 0;
        int bigRadius = 0;
        int distance;
        Vector1X4 origin = new Vector1X4(0, 0, 0, 1);

        while (i < vertexes.length) {
            distance = (int) CustomCalculate.Distance(origin, vertexes[i]);
            if (bigRadius < distance)
                bigRadius = distance;
            i++;
        }
        AddPhysicsSpace(bigRadius);*/
    }

    @Override
    protected void setPlaneIndexes() {
        planeIndexes.add(new int[] {0, 2, 3, 1});
    }

    @Override
    protected boolean isPointInVolume(Vector1X4 localCheckPoint) {
        return CustomCalculate.getPlaneConstant(planeVectors[0], localCheckPoint) < 0;
    }
}
