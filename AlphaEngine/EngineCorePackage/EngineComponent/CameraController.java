package seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent;

import java.util.Vector;

import seaweed.aeproject.AlphaEngine.ETC.CustomMath;
import seaweed.aeproject.AlphaEngine.ETC.Type;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObjectPackage.GameObjectComponentPackage.Volume;
import seaweed.aeproject.AlphaEngine.VolumePackage.Volume;
import seaweed.aeproject.AlphaEngine.VolumePackage.Volumes.RectangularVolume;

import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomDataType.Coordinate3D.*;
import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomCalculate.*;

public class CameraController {

    private static CameraController cameraController = new CameraController();
    private Vector<Camera> cameras;
    private Camera currentCamera;
    private float cameraToDisplayRatio;

    private CameraController()
    {
        cameras = new Vector<>();
        currentCamera = new Camera();
        cameras.add(currentCamera);
    }
    public static CameraController getInstance()
    {
        if (cameraController == null)
            cameraController = new CameraController();
        return cameraController;
    }
    public Camera getCamera()
    {
        return currentCamera;
    }
    public void setFocus(Vector1X4 point)
    {
        throw new Exception();//what about Update?
    }
    public void setPosition(Vector1X4 position)
    {
        throw new Exception();//what about converse with setFocus()?
    }
    public void CameraMove(int x, int y)
    {
        currentCamera.Move(x, y);
    }
    public void CameraRotate(int x, int y)
    {
        currentCamera.Rotate(x, y);
    }
    public Matrix4X4 getSightMatrix()
    {
        return currentCamera.getSightMatrix();
    }

    class Camera extends GameObject {

        private int maxSight = 1000;
        private int minSight = 100;

        private final int maxFieldOfView = 70;
        private final int minFieldOfView = 30;
        private int fieldOfView;//horizontal

        private GameObject focus;
        private float sight;

        private float angleY = 0;
        private float angleX = -10;

        private float cameraToDisplay;

        private Frustum frustum;

        private Camera()
        {
            super("camera");
            setSight();

            fieldOfView = 60;

            Rotate(this.objectMatrix.yVector(), angleY);
            Rotate(this.objectMatrix.xVector(), angleX);

            int screenWidth = (int) (2 * maxSight * Math.tan(Math.toRadians(maxFieldOfView)));
            cameraToDisplay = maxSight * screenWidth / DeviceDisplayData.getInstance().getDeviceDisplayWidth();

            frustum = new Frustum(this);
            AddComponent(frustum);
        }
        private void setSight()
        {
            int height = (int)objectMatrix.qVector().getVector1X3().getY();
            Vector1X4 temp = new Vector1X4(objectMatrix.zVector());
            temp.Normalize();

            float angle = (float) Math.acos(temp.Inner(new CustomMath.Vector1X4(0, -1, 0, 0)));
            if (angle == 0)
                sight = maxSight;
            else {
                sight = Math.abs((int) (height / Math.cos(angle)));
                if (sight < minSight)
                    sight = minSight;
                else if (sight > maxSight)
                    sight = maxSight;
            }

            if (focus == null)
                focus = new GameObject("focus");
            focus.setPosition(new Vector1X4(0, 0, sight, 1).Multiply(objectMatrix.Inverse()));
            if (focus.getParentGameObject() == null)
                focus.setParentGameObject(this);
        }
        void Rotate(int x, int y)//spherical coordinate?
        //angle's standard is focus
        {
            Vector1X4 vector1X4 = new Vector1X4(focus.getObjectMatrix().getQVector());

            angleY -= (x * 0.1);
            angleY %= 360;
            Rotate(new Vector1X4(0, 1, 0, 0), -(float) (x * 0.1));

            if((angleX - (y * 0.1)) > -90 && (angleX - (y * 0.1)) < -0) {
                angleX -= (y * 0.1);
                Rotate(objectMatrix.xVector(), -(float) (y * 0.1));
            }

            setFocus(vector1X4);

            objectMatrix.xVector().Normalize();
            objectMatrix.yVector().Normalize();
            objectMatrix.zVector().Normalize();
        }
        private void setFocus(Vector1X4 focus)
        {
            this.focus.setPositionByWorld(focus, true);
        }
        private void setDistance(float distance)
        {
            Vector1X4 tempPoint = new Vector1X4(focus.getObjectMatrix().qVector());
            if (distance < minSight)
                distance = minSight;
            else if (maxSight < distance)
                distance = maxSight;
            this.sight = distance;
            focus.setPosition(new Vector1X4(0, 0, distance, 1));
            setFocus(tempPoint);
        }
        private void Zoom(int x)
        {
            fieldOfView -= x * 0.01;

            if(fieldOfView < minFieldOfView || fieldOfView > maxFieldOfView) {
                if (fieldOfView > maxFieldOfView)
                    fieldOfView = maxFieldOfView;
                else if (fieldOfView < minFieldOfView)
                    fieldOfView = minFieldOfView;
            }
        }
        private float getAngleY() {
            return angleY;
        }
        private float getAngleX() {
            return angleX;
        }
        private int getFieldOfView()
        {
            return fieldOfView;
        }
        private boolean setFieldOfView(int FieldOfView) {
            if(minFieldOfView < fieldOfView && fieldOfView < maxFieldOfView) {
                this.fieldOfView = FieldOfView;
                return true;
            }
            return false;
        }
        private boolean setMaxSight(int maxSight)
        {
            if(maxSight > minSight) {
                this.maxSight = maxSight;
                frustum.setVertex();
                return true;
            }
            return false;
        }
        private boolean setMinSight(int minSight)
        {
            if(minSight < maxSight) {
                this.minSight = minSight;
                frustum.setVertex();
                return true;
            }
            return false;
        }
        private Matrix4X4 getSightMatrix()
        {
            return getObjectMatrix().Inverse();
        }
        public int getMaxSight()
        {
            return maxSight;
        }
        private float getSight()
        {
            return sight;
        }
        private Vector1X4 getFocus()
        {
            return focus.getObjectMatrix().qVector();
        }
        public float getCameraToDisplay(float distance) {
            return cameraToDisplay / distance;
        }

        @Override
        public void setPosition(Vector1X4 position)
        {
            super.setPosition(position);
            setSight();
            setFocus(this.focus.getObjectMatrix().qVector());
        }

        private class Frustum extends RectangularVolume {

            Frustum(GameObject upperGameObject) {
                super(upperGameObject, false);
            }

            @Override
            protected void setVertex() {
                //ShortLeftUp, ShortLeftDown, ShortRightUp, ShortRightDown, LongLeftUp, LongLeftDonw, LongRightUp, LongRightDown
                float displayRatio = DeviceDisplayData.getInstance().getDisplayRatio();
                vertexes = new Vector1X4[8];

                vertexes[0] = new Vector1X4((float) (-minSight * Math.tan(Math.toRadians(maxFieldOfView))), (float) ( minSight * Math.tan(Math.toRadians(maxFieldOfView * displayRatio))), minSight, 1);
                vertexes[1] = new Vector1X4((float) (-minSight * Math.tan(Math.toRadians(maxFieldOfView))), (float) (-minSight * Math.tan(Math.toRadians(maxFieldOfView * displayRatio))), minSight, 1);
                vertexes[2] = new Vector1X4((float) ( minSight * Math.tan(Math.toRadians(maxFieldOfView))), (float) ( minSight * Math.tan(Math.toRadians(maxFieldOfView * displayRatio))), minSight, 1);
                vertexes[3] = new Vector1X4((float) ( minSight * Math.tan(Math.toRadians(maxFieldOfView))), (float) (-minSight * Math.tan(Math.toRadians(maxFieldOfView * displayRatio))), minSight, 1);
                vertexes[4] = new Vector1X4((float) (-maxSight * Math.tan(Math.toRadians(maxFieldOfView))), (float) ( maxSight * Math.tan(Math.toRadians(maxFieldOfView * displayRatio))), maxSight, 1);
                vertexes[5] = new Vector1X4((float) (-maxSight * Math.tan(Math.toRadians(maxFieldOfView))), (float) (-maxSight * Math.tan(Math.toRadians(maxFieldOfView * displayRatio))), maxSight, 1);
                vertexes[6] = new Vector1X4((float) ( maxSight * Math.tan(Math.toRadians(maxFieldOfView))), (float) ( maxSight * Math.tan(Math.toRadians(maxFieldOfView * displayRatio))), maxSight, 1);
                vertexes[7] = new Vector1X4((float) ( maxSight * Math.tan(Math.toRadians(maxFieldOfView))), (float) (-maxSight * Math.tan(Math.toRadians(maxFieldOfView * displayRatio))), maxSight, 1);
                //add physicsSpace
            }
        }
    }
}
