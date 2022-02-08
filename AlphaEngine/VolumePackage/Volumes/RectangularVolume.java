package seaweed.aeproject.AlphaEngine.VolumePackage.Volumes;

import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.VolumePackage.Volume;

import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomDataType.Coordinate3D.*;
import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomCalculate.*;

public class RectangularVolume extends Volume {

    private int width, height, deep;

    public RectangularVolume(GameObject gameObject, boolean rigidBody)
    {
        super(gameObject, rigidBody);
        width = 2;
        height = 2;
        deep = 2;

        setVertex();
    }
    public RectangularVolume(GameObject gameObject, boolean rigidBody, int width, int height, int deep)
    {
        super(gameObject, rigidBody);

        this.width = width;
        this.height = height;
        this.deep = deep;

        setVertex();
    }
    public void setScale(int width, int height, int deep)
    {
        this.width = width;
        this.height = height;
        this.deep = deep;

        setVertex();
    }

    @Override
    protected void setVertex() {
        //look at y axis from head to tail, local coordinate
        //TopLeftUp, TopLeftDown, TopRightUp, TopRightDown, BottomLeftUp, BottomLeftDown, BottomRightUp, BottomRightDown
        vertexes = new Vector1X4[8];
        vertexes[0] = new Vector1X4(-width/2,  height/2,  deep/2, 1);
        vertexes[1] = new Vector1X4(-width/2,  height/2, -deep/2, 1);
        vertexes[2] = new Vector1X4( width/2,  height/2,  deep/2, 1);
        vertexes[3] = new Vector1X4( width/2,  height/2, -deep/2, 1);
        vertexes[4] = new Vector1X4(-width/2, -height/2,  deep/2, 1);
        vertexes[5] = new Vector1X4(-width/2, -height/2, -deep/2, 1);
        vertexes[6] = new Vector1X4( width/2, -height/2,  deep/2, 1);
        vertexes[7] = new Vector1X4( width/2, -height/2, -deep/2, 1);

        /*int i = 0;
        int bigRadius = 0;
        int distance;
        Vector1X4 origin = new Vector1X4(0, 0, 0, 1);

        while (i < vertexes.length) {
            distance = (int) Distance(origin, vertexes[i]);
            if (bigRadius < distance)
                bigRadius = distance;
            i++;
        }
        AddPhysicsSpace(bigRadius);*/
    }

    @Override
    protected void setPlaneIndexes() {
        planeIndexes.add(new int[] {0, 2, 3, 1});
        planeIndexes.add(new int[] {0, 1, 5, 4});
        planeIndexes.add(new int[] {1, 3, 7, 5});
        planeIndexes.add(new int[] {3, 2, 6, 7});
        planeIndexes.add(new int[] {2, 0, 4, 6});
        planeIndexes.add(new int[] {5, 7, 6, 4});
    }
}
