package seaweed.aeproject.AlphaEngine.VolumePackage.Volumes;

import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.PhysicsPackage.ContactData;
import seaweed.aeproject.AlphaEngine.VolumePackage.Volume;

import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomDataType.Coordinate3D.*;
import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomCalculate.*;

public class CylinderVolume extends Volume {

    private int radius, height;

    public CylinderVolume(GameObject gameObject, boolean rigidBody)
    {
        super(gameObject, rigidBody);

        this.radius = 1;
        this.height = 2;

        setVertex();
    }
    public CylinderVolume(GameObject gameObject, boolean rigidBody, int radius, int height)
    {
        super(gameObject, rigidBody);

        this.radius = radius;
        this.height = height;

        setVertex();
    }
    public void setScale(int radius, int height)
    {
        this.radius = radius;
        this.height = height;

        setVertex();
    }

    @Override
    protected void CheckCollide(VolumeCoordinateConverter volumeCoordinateConverter, ContactData contactData) {
        Vector1X4[] vertexArray;
        Vector1X4[] axis = new Vector1X4[2];
        axis[0] = new Vector1X4(0, height, 0, 1);
        axis[1] = new Vector1X4(0, -height, 0, 1);
        Vector1X4 vertical = new Vector1X4(0, 1, 0, 0);
        Vector1X4 vector = new Vector1X4();

        Vector1X4 planeVector = new Vector1X4();
        Vector1X4[] planeRange = null;

        Vector1X4[] shortestSegment;
        int i, j;

        i = 0;
        while (i < volumeCoordinateConverter.getPlaneVectorSize())
        {
            volumeCoordinateConverter.getLPlaneVector(i, planeVector);

            if (planeRange == null || planeRange.length != volumeCoordinateConverter.getPlaneIndexes(i).length)
                planeRange = new Vector1X4[volumeCoordinateConverter.getPlaneIndexes(i).length];
            j = 0;
            while (j < volumeCoordinateConverter.getPlaneIndexes(i).length)
            {
                if (planeRange[j] == null)
                    planeRange[j] = new Vector1X4();

                volumeCoordinateConverter.getLVertex(volumeCoordinateConverter.getPlaneIndexes(i)[j], planeRange[j]);
                j++;
            }
            shortestSegment = ShortestSegment(axis, planeVector, planeRange);

            Subtract(shortestSegment[0], shortestSegment[1], vector);
            vector.Normalize();

            if (Inner(vertical, vector) == 0)
            {
                if (Distance(shortestSegment[0], shortestSegment[1]) < radius) {
                    //contactData Add
                }
            }
            i++;
        }
    }

    @Override
    protected void setVertex() {
        vertexes = new Vector1X4[12];
        Vector1X4 axisVector = new Vector1X4(0, height, 0, 0);
        Vector1X4 tempVector = new Vector1X4();
        Vector1X4 rotateVector = new Vector1X4(radius, 0, 0, 0);
        Vector1X4 basePoint = new Vector1X4(0, 0, 0, 1);

        int i = 0, j;
        while (i < 2)
        {
            j = 0;
            while(j < 6)
            {
                Add(axisVector, rotateVector, tempVector);
                Add(basePoint, tempVector, vertexes[j + (6 * i)]);
                rotateVector.Rotate(new Vector1X4(0, 1, 0, 0), 60);
                j++;
            }
            Inverse(axisVector, axisVector);
            i++;
        }

        /*int bigRadius = (int)Math.sqrt(Math.pow(radius, 2) + Math.pow(height/2, 2));
        AddPhysicsSpace(bigRadius);*/
    }

    @Override
    protected void setPlaneIndexes() {
        planeIndexes.add(new int[] {0, 5, 4, 3, 2, 1});
        planeIndexes.add(new int[] {0, 1, 7, 6});
        planeIndexes.add(new int[] {1, 2, 8, 7});
        planeIndexes.add(new int[] {2, 3, 9, 8});
        planeIndexes.add(new int[] {3, 4, 10, 9});
        planeIndexes.add(new int[] {4, 5, 11, 10});
        planeIndexes.add(new int[] {0, 6, 11, 5});
        planeIndexes.add(new int[] {6, 7, 8, 9, 10, 11});
    }

    @Override
    protected boolean isPointInVolume(Vector1X4 localCheckPoint) {
        Vector1X4[] axis = new Vector1X4[2];
        axis[0] = new Vector1X4(0, height, 0, 1);
        axis[1] = new Vector1X4(0, -height, 0, 1);

        float topPlaneConstant = getPlaneConstant(planeVectors[0], axis[0]);
        float bottomPlaneConstant = getPlaneConstant(planeVectors[7], axis[1]);

        if (!(topPlaneConstant > 0) && !(bottomPlaneConstant > 0))
        {
            Vector1X4[] segment = ShortestSegment(localCheckPoint, axis);
            return !(Distance(segment[0], segment[1]) > radius);
        }

        return false;
    }
}
