package seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObjects;

import java.util.Vector;

import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.PhysicsSystem;
import seaweed.aeproject.AlphaEngine.PhysicsPackage.ContactData;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObject;

import seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomDataType.Coordinate3D.*;
import seaweed.aeproject.AlphaEngine.VolumePackage.Volume;
import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomCalculate.*;
import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomCalculate.isPointInPlane;

abstract public class Ray extends GameObject {

    protected GameObject collideGameObject;
    protected Vector1X4 collidePoint;

    public Ray(Vector1X4 point, Vector1X4 vector, float distance, String key) {
        super(point, "Ray");
        Vector1X4 zVector = new Vector1X4(0, 0, 1, 0);
        Vector1X4 axis = new Vector1X4();
        Outer(zVector, vector, axis);
        axis.Normalize();

        float angle = (float) Math.toDegrees(Math.acos(Inner(vector, zVector)));
        Rotate(axis, angle);

        AddComponent(new RayVolume(this, distance), key);
    }

    public void ShootARay()
    {
        Vector<ContactData> contactData = PhysicsSystem.getInstance().CheckCollide(this, true);
        float distance = Float.MAX_VALUE;
        int i, j;

        i = 0;
        while (i < contactData.size())
        {
            if (RayCondition())
            {
                j = 0;
                while (j < contactData.get(i).getVertexes().size())
                {
                    if (Distance(getObjectMatrix().getQVector(), contactData.get(i).getVertexes().get(j)) == 0)
                    {
                        j++;
                        continue;
                    }
                    if (Distance(getObjectMatrix().getQVector(), contactData.get(i).getVertexes().get(j)) < distance)
                    {
                        collideGameObject = contactData.get(i).getSubGameObject();
                        collidePoint = contactData.get(i).get;
                        distance = Distance(getObjectMatrix().getQVector(), contactData.get(i).getVertexes().get(j));
                    }
                    j++;
                }
            }
            i++;
        }
    }
    public GameObject getCollideGameObject()
    {
        return collideGameObject;
    }
    public Vector1X4 getCollidePoint()
    {
        return collidePoint;
    }

    abstract protected boolean RayCondition();

    private class RayVolume extends Volume
    {
        private float length;
        RayVolume(GameObject upperGameObject, float length) {
            super(upperGameObject, false);
            this.length = length;
        }

        @Override
        public void CheckCollide(VolumeCoordinateConverter volumeCoordinateConverter, ContactData contactData)
        {
            Vector1X4 planeVector = new Vector1X4();
            Vector1X4[] planeRange = null;
            Vector1X4[] penetrationPoint = new Vector1X4[1];
            penetrationPoint[0] = new Vector1X4();
            int i, j;

            i = 0;
            while (i < volumeCoordinateConverter.getPlaneVectorSize())
            {
                if (planeRange == null || planeRange.length != volumeCoordinateConverter.getPlaneIndexes(i).length)
                    planeRange = new Vector1X4[volumeCoordinateConverter.getPlaneIndexes(i).length];

                j = 0;
                while (j < volumeCoordinateConverter.getVertexSize())
                {
                    if (planeRange[j] == null)
                        planeRange[j] = new Vector1X4();

                    volumeCoordinateConverter.getLVertex(volumeCoordinateConverter.getPlaneIndexes(i)[j], planeRange[j]);
                    j++;
                }

                volumeCoordinateConverter.getLPlaneVector(i, planeVector);
                if (isPenetrate(vertexes, planeVector, planeRange[0], penetrationPoint) && isPointInPlane(penetrationPoint[0], planeVector, planeRange))
                {
                    //contactData add
                    break;//break?
                }
            }
        }

        @Override
        protected void setVertex() {
            vertexes = new Vector1X4[2];
            vertexes[0] = new Vector1X4(0, 0, 0, 1);
            vertexes[1] = new Vector1X4(0, 0, length, 1);
        }

        @Override
        protected void setPlaneIndexes() {

        }

        @Override
        protected boolean isPointInVolume(Vector1X4 localCheckPoint) {
            return false;
        }
    }
}
