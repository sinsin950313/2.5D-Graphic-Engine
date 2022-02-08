package seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent;

import android.graphics.Rect;

import java.util.HashMap;
import java.util.Vector;

import seaweed.aeproject.AlphaEngine.ETC.CustomMath;
import seaweed.aeproject.AlphaEngine.ETC.Type;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObjects.Ray;
import seaweed.aeproject.AlphaEngine.PhysicsPackage.ContactData;
import seaweed.aeproject.AlphaEngine.VolumePackage.Volume;

import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomCalculate.*;
import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomDataType.Coordinate3D.*;
import static seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineActivity.timer;

public class PhysicsSystem {

    private static PhysicsSystem physicsSystem = new PhysicsSystem();
    private SpaceProcessor spaceProcessor;
    private MovementProcessor movementProcessor;

    private PhysicsSystem()
    {
        spaceProcessor = new SpaceProcessor();
        movementProcessor = new MovementProcessor();

        movementProcessor.start();
    }
    public static PhysicsSystem getInstance()
    {
        if (physicsSystem == null)
            physicsSystem = new PhysicsSystem();
        return physicsSystem;
    }
    void getBillboard(Volume volume, Rect billboard) {
        spaceProcessor.setBillboard(volume, billboard);
    }
    boolean isBehind(Volume origin, Volume compare) {
        return spaceProcessor.isBehind(origin, compare);
    }
    public Vector<ContactData> CheckCollide(GameObject gameObject, boolean cheap) {
        return spaceProcessor.CheckCollide(gameObject, cheap);
    }
    public void Clear()
    {

    }

    class SpaceProcessor
    {
        private HashMap<GameObject, BoundingVolume> gameObjects;
        private int registerList = 0;
        private CollisionChecker collisionChecker;

        SpaceProcessor()
        {
            gameObjects = new HashMap<>();
            collisionChecker = new CollisionChecker();
        }
        void Register(GameObject ownerGameObject, VolumeOrigin volume) {
            new Thread(new VolumeRegister(ownerGameObject, volume)).start();
            registerList++;
        }
        boolean isBehind(VolumeOrigin origin, VolumeOrigin compare) {
            return origin.isBehind(compare);
        }
        void setBillboard(VolumeOrigin volume, Rect billboard)
        {
            volume.setBillboard(billboard);
        }
        private void setEdges(VolumeOrigin volume) {
            int i, j, k;
            int[] edge;
            boolean notExist;

            edge = new int[2];
            i = 0;
            while (i < volume.planeIndexes.size()) {

                j = 0;
                while (j < volume.planeIndexes.get(i).length)
                {
                    edge[0] = volume.planeIndexes.get(i)[j];

                    if (i == volume.planeIndexes.size())
                        edge[1] = volume.planeIndexes.get(i)[0];
                    else
                        edge[1] = volume.planeIndexes.get(i)[j + 1];

                    notExist = true;
                    k = 0;
                    while (k < volume.edges.size()) {
                        if ((volume.edges.get(k)[0] == edge[0] && volume.edges.get(k)[1] == edge[1]) || (volume.edges.get(k)[0] == edge[1] && volume.edges.get(k)[1] == edge[0]))
                        {
                            notExist = false;
                            break;
                        }
                        k++;
                    }

                    if (notExist) {
                        volume.edges.add(edge);
                        edge = new int[2];
                    }

                    j++;
                }

                i++;
            }
        }//??
        private void setPlaneVector(VolumeOrigin volume) {
            if (volume.planeIndexes.size() != 0)
                volume.planeVectors = new Vector1X4[volume.planeIndexes.size()];

            Vector1X4 tmp1 = new Vector1X4();
            Vector1X4 tmp2 = new Vector1X4();
            int i = 0;
            int j = 0;
            while(i < volume.planeIndexes.size())
            {
                Subtract(volume.vertexes[volume.planeIndexes.get(i)[0]], volume.vertexes[volume.planeIndexes.get(i)[1]], tmp1);
                Subtract(volume.vertexes[volume.planeIndexes.get(i)[2]], volume.vertexes[volume.planeIndexes.get(i)[1]], tmp2);

                tmp1.Normalize();
                tmp2.Normalize();

                volume.planeVectors[j] = new Vector1X4();
                Outer(tmp1, tmp2, volume.planeVectors[j]);
                volume.planeVectors[j].Normalize();
                i++;
                j++;
            }
        }//??
        Vector<ContactData> CheckCollide(GameObject gameObject, boolean cheap)
        {
            collisionChecker.CheckCollide();
        }
        boolean isReady() {
            return registerList == 0;
        }

        private class BoundingVolume {
            private GameObject ownerGameObject;
            private VolumeOrigin volume;
            //private Vector<BoundingVolume> boundingVolumes;
            private float radius;

            BoundingVolume(GameObject ownerGameObject, VolumeOrigin volume, float radius)
            {
                this.ownerGameObject = ownerGameObject;
                this.volume = volume;
                this.radius = radius;
                //boundingVolumes = new Vector<>();
            }
            VolumeOrigin getVolume() {
                return volume;
            }
            boolean CheckCollide(BoundingVolume boundingVolume) {
                return Distance(ownerGameObject.getObjectMatrix().getQVector(), boundingVolume.ownerGameObject.getObjectMatrix().getQVector()) < radius + boundingVolume.radius;
            }
            /*public void Enlarge(BoundingVolume boundingVolume)
            {
                BoundingVolume parentBoundingVolume = new BoundingVolume(volume, radius);
                boundingVolumes.add(parentBoundingVolume);

                float distance = CustomMath.Distance(volume.getUpperGameObject().getObjectMatrix().qVector(), boundingVolume.getPosition());
                float downerRadius = boundingVolume.getRadius();

                if (distance + downerRadius > radius)
                    radius = distance + downerRadius;

                boundingVolumes.add(boundingVolume);
            }*/
            /*public Vector<BoundingVolume> getBoundingVolumes()
            {
                return boundingVolumes;
            }
            public Vector1X4 getPosition() {
                return gameObject.getObjectMatrix().qVector();
            }*/
        }

        private class VolumeRegister implements Runnable {
            private GameObject ownerGameObject;
            private VolumeOrigin volume;

            VolumeRegister(GameObject ownerGameObject, VolumeOrigin volume)
            {
                this.ownerGameObject = ownerGameObject;
                this.volume = volume;
            }

            @Override
            public void run() {
                while (volume == null) { }

                volume.setVertex();
                volume.setPlaneIndexes();
                volume.setRadius();

                setEdges(volume);
                setPlaneVector(volume);
                gameObjects.put(ownerGameObject, new BoundingVolume(ownerGameObject, volume, volume.getRadius()));
                registerList--;
            }
        }

        private class CollisionChecker
        {
            ContactData CheckCollide(GameObject mainGameObject, GameObject subGameObject)
            {
                ContactData contactData = null;
                BoundingVolume mainBoundingVolume = gameObjects.get(mainGameObject);
                BoundingVolume subBoundVolume = gameObjects.get(subGameObject);

                if (mainBoundingVolume.CheckCollide(subBoundVolume))
                {
                    contactData = new ContactData(mainGameObject, subGameObject, );
                    mainBoundingVolume.getVolume().CheckCollide(subBoundVolume.getVolume(), contactData);
                }

                return contactData;//??
            }
        }
    }

    public abstract class VolumeOrigin implements GameObject.GameObjectComponentInterface
    {
        protected GameObject upperGameObject;

        protected Vector1X4[] vertexes;
        protected Vector<int[]> edges;
        protected Vector<int[]> planeIndexes;
        protected Vector1X4[] planeVectors;
        private Data data;

        private boolean rigidBody = false;

        protected VolumeOrigin(GameObject upperGameObject, boolean rigidBody)
        {
            this.upperGameObject = upperGameObject;
            this.rigidBody = rigidBody;
            this.data = new Data();

            edges = new Vector<>();
            planeIndexes = new Vector<>();

            spaceProcessor.Register(upperGameObject, this);
        }
        public boolean isRigidBody() {
            return rigidBody;
        }
        protected void AddPhysicsSpace(int bigRadius)/////////////////////////////////////////////////////////////////
        {
            if (upperGameObject.hasParent())
                PhysicsSpace.getInstance().Add(upperGameObject, this, bigRadius);
            else
                PhysicsSpace.getInstance().JointBoundingVolume(upperGameObject.getParentGameObject(), upperGameObject);
        }
        void CheckCollide(VolumeOrigin volume, ContactData contactData)
        {
            CheckCollide(new VolumeCoordinateConverter(this, volume), contactData);
        }
        protected void CheckCollide(VolumeCoordinateConverter volumeCoordinateConverter, ContactData contactData)
        {
            //calculate by local, save by world
            Vector1X4[] edge = new Vector1X4[2];
            edge[0] = new Vector1X4();
            edge[1] = new Vector1X4();
            Vector1X4[] planeRange;
            Vector1X4[] penetratedPoint = new Vector1X4[1];
            Vector1X4[] vertexArray = new Vector1X4[3];
            int i, j, k;

            i = 0;
            while (i < volumeCoordinateConverter.getEdgeSize())
            {
                volumeCoordinateConverter.getLEdge(i, edge);
                j = 0;
                planeRange = new Vector1X4[planeIndexes.get(i).length];
                while (j < planeVectors.length)
                {
                    k = 0;
                    while (k < planeRange.length)
                    {
                        planeRange[k] = vertexes[planeIndexes.get(j)[k]];
                        k++;
                    }

                    if (isPenetrate(edge, planeVectors[j], planeRange[0], penetratedPoint) && isPointInPlane(penetratedPoint[0], planeVectors[j], planeRange))
                    {
                        if (vertexArray[0] == null) {
                            vertexArray[0] = new Vector1X4();
                            Multiply(penetratedPoint[0], upperGameObject.getObjectMatrix(), vertexArray[0]);
                        }
                        else
                        {
                            vertexArray[1] = new Vector1X4();
                            Multiply(penetratedPoint[0], upperGameObject.getObjectMatrix(), vertexArray[1]);
                            break;
                        }
                    }
                    j++;
                }

                if (vertexArray[0] != null)///////////////////////////////////////////////////////////////////after contactData
                {
                    if (vertexArray[1] == null)
                    {
                        if (isPointInVolume(edge[0]))
                        {
                            vertexArray[1] = new Vector1X4();
                            Multiply(edge[0], upperGameObject.getObjectMatrix(), vertexArray[1]);
                        }
                        else
                        {
                            vertexArray[1] = new Vector1X4();
                            Multiply(edge[1], upperGameObject.getObjectMatrix(), vertexArray[1]);
                        }
                    }
                    vertexArray[2] = new Vector1X4();
                    Add(vertexArray[0], vertexArray[1], vertexArray[2]);
                    Scaled(vertexArray[2], 0.5f, vertexArray[2]);
                    contactData.Add(vertexArray);
                }//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            }
        }
        protected boolean isPointInVolume(Vector1X4 localCheckPoint)
        {
            int i = 0;
            while (i < planeVectors.length)
            {
                double planeConstant = getPlaneConstant(planeVectors[i], vertexes[planeIndexes.get(i)[0]]);
                double pointConstant = getPlaneConstant(planeVectors[i], localCheckPoint);

                if (pointConstant > planeConstant)
                    return false;
                i++;
            }

            return true;
        }
        void setRadius()
        {
            return a;
        }
        float getRadius()
        {

        }
        void setBillboard(Rect returnBillboard)
        {
            if (!data.CheckScreenTime())
                data.Update();

            returnBillboard.left = (int) data.left;
            returnBillboard.top = (int) data.top;
            returnBillboard.right = (int) data.right;
            returnBillboard.bottom = (int) data.bottom;
        }
        boolean isBehind(final VolumeOrigin volume)
        {
            //if registerList behind this, return true
            if (!data.CheckScreenTime())
                data.Update();

            if (data.deep < volume.data.close)
                return true;
            else
            {
                if (volume.data.deep < data.close)
                    return false;
                else
                {
                    if (volume.data.right < data.left || data.right < volume.data.left
                            || volume.data.bottom < data.top || data.top < volume.data.bottom)
                        return true;
                    else
                    {
                        Ray ray;
                        boolean cross = false;
                        GameObject returnGameObject;

                        int i = 0;
                        Vector1X4 cameraPoint = CameraController.getInstance().getCamera().getObjectMatrix().getQVector();
                        float distance;

                        Vector1X4 vertex = new Vector1X4();
                        Vector1X4 vector = new Vector1X4();
                        while (i < volume.vertexes.length)
                        {
                            Multiply(volume.vertexes[i], volume.upperGameObject.getObjectMatrix(), vertex);
                            Subtract(vertex, cameraPoint, vector);
                            distance = vector.Magnitude();
                            vector.Normalize();

                            ray = new Ray(cameraPoint, vector, distance, "before_after") {
                                @Override
                                protected boolean RayCondition() {
                                    return collideGameObject == upperGameObject || collideGameObject == volume.upperGameObject;
                                }
                            };

                            ray.ShootARay();
                            returnGameObject = ray.getCollideGameObject();

                            //what happen if penetration?
                            if (returnGameObject == volume.upperGameObject) {
                                cross = true;
                                break;
                            }
                            i++;
                        }

                        return !cross;
                    }
                }
            }
        }

        abstract protected void setVertex();
        abstract protected void setPlaneIndexes();

        @Override
        public boolean CheckComponent(Type.GameObjectComponent componentType) {
            return componentType == Type.GameObjectComponent.VOLUME;
        }

        private class Data
        {
            private long screenTime;
            private float left, top, right, bottom, deep, close;

            Data()
            {
                screenTime = -1;
                left = 0;
                top = 0;
                right = 0;
                bottom = 0;
                deep = 0;
                close = 0;
            }
            void Update()
            {
                screenTime = timer.getScreenTime();

                Matrix4X4 sightMatrix = CameraController.getInstance().getSightMatrix();

                double left, top, right, bottom, deep, close;
                Matrix4X4 gameObjectInSightMatrix = new Matrix4X4();
                Multiply(upperGameObject.getObjectMatrix(), sightMatrix, gameObjectInSightMatrix);

                Vector1X4 point = gameObjectInSightMatrix.getQVector();
                float distance = Distance(CustomMath.getCenter(), point);

                float cameraToDisplay = CameraController.getInstance().getCamera().getCameraToDisplay(distance);

                double centerX = point.getVector1X3().getX();
                double centerY = point.getVector1X3().getY();
                double centerZ = point.getVector1X3().getZ();

                left = right = centerX;
                top = bottom = centerY;
                deep = close = centerZ;

                Vector1X4 vertex = new Vector1X4();
                int i = 0;
                while (i < vertexes.length)
                {
                    Multiply(vertexes[i], gameObjectInSightMatrix, vertex);

                    double value;
                    value = vertex.getVector1X3().getX();
                    if (value < left)
                        left = value;
                    else if (right < value)
                        right = value;

                    value = vertex.getVector1X3().getY();
                    if (value < bottom)
                        bottom = value;
                    else if (top < value)
                        top = value;

                    value = vertex.getVector1X3().getZ();
                    if (value < close)
                        close = value;
                    else if (deep < value)
                        deep = value;

                    i++;
                }

                this.left = (float) left * cameraToDisplay;
                this.top = (float) top * cameraToDisplay;
                this.right = (float) right * cameraToDisplay;
                this.bottom = (float) bottom * cameraToDisplay;
                this.deep = (float) deep * cameraToDisplay;
                this.close = (float) close * cameraToDisplay;
            }
            void Clear()
            {
                screenTime = -1;
                left = 0;
                top = 0;
                right = 0;
                bottom = 0;
                deep = 0;
                close = 0;
            }
            boolean CheckScreenTime()
            {
                return timer.CheckScreenTime(screenTime);
            }
        }

        protected class VolumeCoordinateConverter
        {
            private Matrix4X4 toWorldMatrix;
            private Matrix4X4 toLocalMatrix;
            private Vector1X4[] vertexes;
            private Vector<int[]> edges;
            private Vector1X4[] planeVectors;
            private Vector<int[]> planeIndexes;

            public VolumeCoordinateConverter(VolumeOrigin origin, VolumeOrigin compare)
            {
                toWorldMatrix = new Matrix4X4(compare.upperGameObject.getObjectMatrix());
                toLocalMatrix = new Matrix4X4();
                Inverse(origin.upperGameObject.getObjectMatrix(), toLocalMatrix);

                this.vertexes = compare.vertexes;
                this.planeVectors = compare.planeVectors;
                this.edges = compare.edges;
                this.planeIndexes = compare.planeIndexes;
            }
            public void getWVertex(int index, Vector1X4 returnVertex)
            {
                Multiply(vertexes[index], toWorldMatrix, returnVertex);
            }
            public void getLVertex(int index, Vector1X4 returnVertex)
            {
                getWVertex(index, returnVertex);
                Multiply(returnVertex, toLocalMatrix, returnVertex);
            }
            public int getVertexSize()
            {
                return vertexes.length;
            }
            public void getWEdge(int index, Vector1X4[] edge)
            {
                getWVertex(edges.get(index)[0], edge[0]);
                getWVertex(edges.get(index)[1], edge[1]);
            }
            public void getLEdge(int index, Vector1X4[] edge)
            {
                getLVertex(edges.get(index)[0], edge[0]);
                getLVertex(edges.get(index)[1], edge[1]);
            }
            public int getEdgeSize()
            {
                return edges.size();
            }
            public void getWPlaneVector(int index, Vector1X4 returnVector)
            {
                Multiply(planeVectors[index], toWorldMatrix, returnVector);
            }
            public void getLPlaneVector(int index, Vector1X4 returnVector)
            {
                getWVertex(index, returnVector);
                Multiply(returnVector, toLocalMatrix, returnVector);
            }
            public int getPlaneVectorSize()
            {
                return planeVectors.length;
            }
            public int[] getPlaneIndexes(int index) {
                return planeIndexes.get(index);
            }
        }
    }

    private class MovementProcessor extends Thread
    {
        private class CollisionProcessor
        {

        }

        @Override
        public void run()
        {

        }
    }

    public abstract class MaterialOrigin implements GameObject.GameObjectComponentInterface
    {

    }
}
