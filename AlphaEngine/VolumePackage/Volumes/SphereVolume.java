package seaweed.aeproject.AlphaEngine.VolumePackage.Volumes;

import seaweed.aeproject.AlphaEngine.ETC.CustomMath;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.PhysicsPackage.ContactData;
import seaweed.aeproject.AlphaEngine.VolumePackage.Volume;

import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomDataType.Coordinate3D.*;
import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomCalculate.*;

public class SphereVolume extends Volume {

    private int radius;

    public SphereVolume(GameObject gameObject, boolean rigidBody)
    {
        super(gameObject, rigidBody);

        this.radius = 1;

        setVertex();
    }
    public SphereVolume(GameObject gameObject, boolean rigidBody, int radius) {
        super(gameObject, rigidBody);

        this.radius = radius;

        setVertex();
    }
    public void setScale(int radius)
    {
        this.radius = radius;

        setVertex();
    }

    @Override
    protected void CheckCollide(VolumeCoordinateConverter volumeCoordinateConverter, ContactData contactData) {
        Vector1X4[] vertexArray;

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
            shortestSegment = ShortestSegment(CustomMath.getCenter(), planeVector, planeRange);

            Subtract(shortestSegment[0], shortestSegment[1], vector);
            vector.Normalize();

            if (Distance(shortestSegment[0], shortestSegment[1]) < radius) {
                //contactData Add
            }
            i++;
        }
    }

    @Override
    protected void setVertex() {
        vertexes = new Vector1X4[14];
        Vector1X4 baseVector = new Vector1X4(0, radius, 0, 0);
        Vector1X4 basePoint = new Vector1X4(0, 0, 0, 1);

        Vector1X4 verticalAxis = new Vector1X4(0, 1, 0, 0);
        Vector1X4 horizontalAxis = new Vector1X4(1, 0, 0, 0);

        vertexes[0] = new Vector1X4();
        Add(basePoint, baseVector, vertexes[0]);
        baseVector.Rotate(horizontalAxis, 60);
        int i = 0;
        while(i < 2)
        {
            int j = 0;
            while(j < 6)
            {
                vertexes[(j + 1) + (i * 6)] = new Vector1X4();
                Add(basePoint, baseVector, vertexes[(j + 1) + (i * 6)]);
                baseVector.Rotate(verticalAxis, 60);
                j++;
            }
            baseVector.Rotate(horizontalAxis, 60);
            i++;
        }
        vertexes[13] = new Vector1X4();
        Add(basePoint, baseVector, vertexes[13]);

        /*int bigRadius = radius;
        AddPhysicsSpace(bigRadius);*/
    }

    @Override
    protected void setPlaneIndexes() {
        planeIndexes.add(new int[] {0, 2, 1});
        planeIndexes.add(new int[] {0, 3, 2});
        planeIndexes.add(new int[] {0, 4, 3});
        planeIndexes.add(new int[] {0, 5, 4});
        planeIndexes.add(new int[] {0, 6, 5});
        planeIndexes.add(new int[] {0, 1, 6});
        planeIndexes.add(new int[] {1, 2, 8, 7});
        planeIndexes.add(new int[] {2, 3, 9, 8});
        planeIndexes.add(new int[] {3, 4, 10, 9});
        planeIndexes.add(new int[] {4, 5, 11, 10});
        planeIndexes.add(new int[] {5, 6, 12, 11});
        planeIndexes.add(new int[] {6, 1, 7, 12});
        planeIndexes.add(new int[] {13, 9, 10});
        planeIndexes.add(new int[] {13, 8, 9});
        planeIndexes.add(new int[] {13, 7, 8});
        planeIndexes.add(new int[] {13, 12, 7});
        planeIndexes.add(new int[] {13, 11, 12});
        planeIndexes.add(new int[] {13, 10, 11});
    }

    @Override
    protected boolean isPointInVolume(Vector1X4 localCheckPoint) {
        return Distance(localCheckPoint, CustomMath.getCenter()) < radius;
    }
}
