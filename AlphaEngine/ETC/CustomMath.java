package seaweed.aeproject.AlphaEngine.ETC;

import android.util.Log;

import seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomDataType.*;

import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomCalculate.*;

public class CustomMath {

    private static final Coordinate3D.Vector1X4 center = new Coordinate3D.Vector1X4(0, 0, 0, 1);

    public static Coordinate3D.Vector1X4 getCenter()
    {
        return center;
    }

    public static class CustomCalculate
    {
        public static int Distance(int[] origin, int[] target) { return (int)Math.sqrt(Math.pow(target[0] - origin[0], 2) + ((int)Math.pow(target[1] - origin[1], 2))); }
        public static float Distance(Coordinate3D.Vector1X4 point, Coordinate3D.Vector1X4 point_)
        {
            return (float) Math.sqrt(Math.pow(point.vector1X3.x - point_.vector1X3.x, 2) + Math.pow(point.vector1X3.y - point_.vector1X3.y, 2) + Math.pow(point.vector1X3.z - point_.vector1X3.z, 2));
        }
        public static float Distance(CustomDataType.Coordinate3D.Vector1X4[] segment, Coordinate3D.Vector1X4[] segment_)
        {
            Coordinate3D.Vector1X4[] tempSegment = ShortestSegment(segment, segment_);
            if (tempSegment[1] == null)
                return 0f;
            return Distance(tempSegment[0], tempSegment[1]);
        }
        public static float Distance (Coordinate3D.Vector1X4 point, Coordinate3D.Vector1X4 planeVector, Coordinate3D.Vector1X4[] planeRange)
        {
            Coordinate3D.Vector1X4[] tempSegment = ShortestSegment(point, planeVector, planeRange);
            if (tempSegment[1] == null)
                return 0f;
            return Distance(tempSegment[0], tempSegment[1]);
        }
    /*public static double Distance(Vector1X4 point, Vector1X4 planeVector, Vector1X4 planeVertex)
    {
        double planeConstant = getPlaneConstant(planeVector, planeVertex);
        double pointConstant = getPlaneConstant(planeVector, point);

        return Math.abs(planeConstant + pointConstant) / Math.sqrt(Math.pow(planeVector.getVector1X3().getX(), 2) + Math.pow(planeVector.getVector1X3().getY(), 2) + Math.pow(planeVector.getVector1X3().getZ(), 2));
    }*/

        public static Type.Direction_Vector Direction(float angle)
        {
            if(-2.7 < angle && angle < -2)
            {
                return Type.Direction_Vector.LU;
            }
            else if(-2 < angle && angle < -1.2)
            {
                return Type.Direction_Vector.U;
            }
            else if(-1.2 < angle && angle < -0.4)
            {
                return Type.Direction_Vector.RU;
            }
            else if(-0.4 < angle && angle < 0.4)
            {
                return Type.Direction_Vector.R;
            }
            else if(0.4 < angle && angle < 1.2)
            {
                return Type.Direction_Vector.RD;
            }
            else if(1.2 < angle && angle < 2)
            {
                return Type.Direction_Vector.D;
            }
            else if(2 < angle && angle < 2.7)
            {
                return Type.Direction_Vector.LD;
            }
            else
                return Type.Direction_Vector.L;
        }

        public static void Inverse(Coordinate3D.Vector1X3 vector1X3, Coordinate3D.Vector1X3 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                    result.Copy(-vector1X3.x, -vector1X3.y, -vector1X3.z);
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }
        public static void Inverse(Coordinate3D.Vector1X4 vector1X4, Coordinate3D.Vector1X4 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    Coordinate3D.Vector1X3 tempVector = new Coordinate3D.Vector1X3();
                    Inverse(vector1X4.vector1X3, tempVector);
                    result.Copy(tempVector, vector1X4.q);
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }
        public static void Inverse(Coordinate3D.Matrix4X4 matrix4X4, Coordinate3D.Matrix4X4 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else {
                    if (matrix4X4 == result)
                    {
                        Coordinate3D.Matrix4X4 temp = new Coordinate3D.Matrix4X4(matrix4X4);

                        result.xVector.Copy(temp.xVector.vector1X3.x, temp.yVector.vector1X3.x, temp.zVector.vector1X3.x, 0);
                        result.yVector.Copy(temp.xVector.vector1X3.y, temp.yVector.vector1X3.y, temp.zVector.vector1X3.y, 0);
                        result.zVector.Copy(temp.xVector.vector1X3.z, temp.yVector.vector1X3.z, temp.zVector.vector1X3.z, 0);
                        result.qVector.Copy(-Inner(temp.qVector, temp.xVector), -Inner(temp.qVector, temp.yVector), -Inner(temp.qVector, temp.zVector), 1);
                    }
                    else
                    {
                        result.xVector.Copy(matrix4X4.xVector.vector1X3.x, matrix4X4.yVector.vector1X3.x, matrix4X4.zVector.vector1X3.x, 0);
                        result.yVector.Copy(matrix4X4.xVector.vector1X3.y, matrix4X4.yVector.vector1X3.y, matrix4X4.zVector.vector1X3.y, 0);
                        result.zVector.Copy(matrix4X4.xVector.vector1X3.z, matrix4X4.yVector.vector1X3.z, matrix4X4.zVector.vector1X3.z, 0);
                        result.qVector.Copy(-Inner(matrix4X4.qVector, matrix4X4.xVector), -Inner(matrix4X4.qVector, matrix4X4.yVector), -Inner(matrix4X4.qVector, matrix4X4.zVector), 1);
                    }
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }

        public static void Add(Coordinate3D.Vector1X3 before, Coordinate3D.Vector1X3 after, Coordinate3D.Vector1X3 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    result.x = (before.x + after.x);
                    result.y = (before.y + after.y);
                    result.z = (before.z + after.z);
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }
        public static void Add(Coordinate3D.Vector1X4 before, Coordinate3D.Vector1X4 after, Coordinate3D.Vector1X4 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    Add(before.vector1X3, after.vector1X3, result.vector1X3);
                    if (before.q == 1 || after.q == 1)
                        result.q = 1;
                    else
                        result.q = 0;
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }
        public static void Add(Coordinate3D.Matrix3X3 before, Coordinate3D.Matrix3X3 after, Coordinate3D.Matrix3X3 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    Add(before.xVector, after.xVector, result.xVector);
                    Add(before.yVector, after.yVector, result.yVector);
                    Add(before.zVector, after.zVector, result.zVector);
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }
        public static void Add(Coordinate3D.Matrix4X4 before, Coordinate3D.Matrix4X4 after, Coordinate3D.Matrix4X4 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    Add(before.xVector, after.xVector, result.xVector);
                    Add(before.yVector, after.yVector, result.yVector);
                    Add(before.zVector, after.zVector, result.zVector);
                    Add(before.qVector, after.qVector, result.qVector);
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }
        void Add(Coordinate3D.Quaternion before, Coordinate3D.Quaternion after, Coordinate3D.Quaternion result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    Add(before.unrealVector, after.unrealVector, result.unrealVector);
                    result.realValue = before.realValue + after.realValue;
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }

        public static void Subtract(Coordinate3D.Vector1X3 before, Coordinate3D.Vector1X3 after, Coordinate3D.Vector1X3 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    result.x = before.x - after.x;
                    result.y = before.y - after.y;
                    result.z = before.z - after.z;
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }
        public static void Subtract(Coordinate3D.Vector1X4 before, Coordinate3D.Vector1X4 after, Coordinate3D.Vector1X4 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    Subtract(before.vector1X3, after.vector1X3, result.vector1X3);
                    try {
                        if (before.q == 1)
                        {
                            if (after.q == 1)
                                result.q = 0;
                            else
                                result.q = 1;
                        }
                        else
                        {
                            if (after.q == 0)
                                result.q = 0;
                            else
                                throw new Exception();
                        }
                    } catch (Exception e) {
                        Log.v("Exception", "It can't be Subtracted");
                    }
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }
        public static void Subtract(Coordinate3D.Matrix3X3 before, Coordinate3D.Matrix3X3 after, Coordinate3D.Matrix3X3 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    Subtract(before.xVector, after.xVector, result.xVector);
                    Subtract(before.yVector, after.yVector, result.yVector);
                    Subtract(before.zVector, after.zVector, result.zVector);
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }
        public static void Subtract(Coordinate3D.Matrix4X4 before, Coordinate3D.Matrix4X4 after, Coordinate3D.Matrix4X4 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    Subtract(before.xVector, after.xVector, result.xVector);
                    Subtract(before.yVector, after.yVector, result.yVector);
                    Subtract(before.zVector, after.zVector, result.zVector);
                    Subtract(before.qVector, after.qVector, result.qVector);
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }
        void Subtract(Coordinate3D.Quaternion before, Coordinate3D.Quaternion after, Coordinate3D.Quaternion result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    Subtract(before.unrealVector, after.unrealVector, result.unrealVector);
                    result.realValue = before.realValue - after.realValue;
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }

        public static void Multiply(Coordinate3D.Vector1X3 before, Coordinate3D.Matrix3X3 after, Coordinate3D.Vector1X3 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    if (before == result)
                    {
                        Coordinate3D.Vector1X3 tempBefore = new Coordinate3D.Vector1X3(before);

                        result.x = tempBefore.x * after.xVector.x + tempBefore.y * after.xVector.y + tempBefore.z * after.xVector.z;
                        result.y = tempBefore.x * after.yVector.x + tempBefore.y * after.yVector.y + tempBefore.z * after.yVector.z;
                        result.z = tempBefore.x * after.zVector.x + tempBefore.y * after.zVector.y + tempBefore.z * after.zVector.z;
                    }
                    else
                    {
                        result.x = before.x * after.xVector.x + before.y * after.xVector.y + before.z * after.xVector.z;
                        result.y = before.x * after.yVector.x + before.y * after.yVector.y + before.z * after.yVector.z;
                        result.z = before.x * after.zVector.x + before.y * after.zVector.y + before.z * after.zVector.z;
                    }
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }
        public static void Multiply(Coordinate3D.Vector1X4 before, Coordinate3D.Matrix4X4 after, Coordinate3D.Vector1X4 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    if (before == result)
                    {
                        Coordinate3D.Vector1X4 tempBefore = new Coordinate3D.Vector1X4(before);

                        result.vector1X3.x = tempBefore.vector1X3.x * after.xVector.vector1X3.x + tempBefore.vector1X3.y * after.yVector.vector1X3.x + tempBefore.vector1X3.z * after.zVector.vector1X3.x + tempBefore.q * after.qVector.vector1X3.x;
                        result.vector1X3.y = tempBefore.vector1X3.x * after.xVector.vector1X3.y + tempBefore.vector1X3.y * after.yVector.vector1X3.y + tempBefore.vector1X3.z * after.zVector.vector1X3.y + tempBefore.q * after.qVector.vector1X3.y;
                        result.vector1X3.z = tempBefore.vector1X3.x * after.xVector.vector1X3.z + tempBefore.vector1X3.y * after.yVector.vector1X3.z + tempBefore.vector1X3.z * after.zVector.vector1X3.z + tempBefore.q * after.qVector.vector1X3.z;
                        result.q           = tempBefore.vector1X3.x * after.xVector.q           + tempBefore.vector1X3.y * after.yVector.q           + tempBefore.vector1X3.z * after.zVector.q           + tempBefore.q * after.qVector.q;
                    }
                    else
                    {
                        result.vector1X3.x = before.vector1X3.x * after.xVector.vector1X3.x + before.vector1X3.y * after.yVector.vector1X3.x + before.vector1X3.z * after.zVector.vector1X3.x + before.q * after.qVector.vector1X3.x;
                        result.vector1X3.y = before.vector1X3.x * after.xVector.vector1X3.y + before.vector1X3.y * after.yVector.vector1X3.y + before.vector1X3.z * after.zVector.vector1X3.y + before.q * after.qVector.vector1X3.y;
                        result.vector1X3.z = before.vector1X3.x * after.xVector.vector1X3.z + before.vector1X3.y * after.yVector.vector1X3.z + before.vector1X3.z * after.zVector.vector1X3.z + before.q * after.qVector.vector1X3.z;
                        result.q           = before.vector1X3.x * after.xVector.q           + before.vector1X3.y * after.yVector.q           + before.vector1X3.z * after.zVector.q           + before.q * after.qVector.q;

                    }
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }
        public static void Multiply(Coordinate3D.Matrix3X3 before, Coordinate3D.Matrix3X3 after, Coordinate3D.Matrix3X3 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    float x, y, z;

                    if (before == result)
                    {
                        Coordinate3D.Matrix3X3 tempBefore = new Coordinate3D.Matrix3X3(before);

                        x = tempBefore.xVector.x * after.xVector.x + tempBefore.xVector.y * after.xVector.y + tempBefore.xVector.z * after.xVector.z;
                        y = tempBefore.xVector.x * after.yVector.x + tempBefore.xVector.y * after.yVector.y + tempBefore.xVector.z * after.yVector.z;
                        z = tempBefore.xVector.x * after.zVector.x + tempBefore.xVector.y * after.zVector.y + tempBefore.xVector.z * after.zVector.z;
                        result.xVector.Copy(x, y, z);

                        x = tempBefore.yVector.x * after.xVector.x + tempBefore.yVector.y * after.xVector.y + tempBefore.yVector.z * after.xVector.z;
                        y = tempBefore.yVector.x * after.yVector.x + tempBefore.yVector.y * after.yVector.y + tempBefore.yVector.z * after.yVector.z;
                        z = tempBefore.yVector.x * after.zVector.x + tempBefore.yVector.y * after.zVector.z + tempBefore.yVector.z * after.zVector.z;
                        result.yVector.Copy(x, y, z);

                        x = tempBefore.zVector.x * after.xVector.x + tempBefore.zVector.y * after.xVector.y + tempBefore.zVector.z * after.xVector.z;
                        y = tempBefore.zVector.x * after.yVector.x + tempBefore.zVector.y * after.yVector.y + tempBefore.zVector.z * after.yVector.z;
                        z = tempBefore.zVector.x * after.zVector.x + tempBefore.zVector.y * after.zVector.y + tempBefore.zVector.z * after.zVector.z;
                        result.zVector.Copy(x, y, z);
                    }
                    else if (after == result)
                    {
                        Coordinate3D.Matrix3X3 tempAfter = new Coordinate3D.Matrix3X3(after);

                        x = before.xVector.x * tempAfter.xVector.x + before.xVector.y * tempAfter.xVector.y + before.xVector.z * tempAfter.xVector.z;
                        y = before.xVector.x * tempAfter.yVector.x + before.xVector.y * tempAfter.yVector.y + before.xVector.z * tempAfter.yVector.z;
                        z = before.xVector.x * tempAfter.zVector.x + before.xVector.y * tempAfter.zVector.y + before.xVector.z * tempAfter.zVector.z;
                        result.xVector.Copy(x, y, z);

                        x = before.yVector.x * tempAfter.xVector.x + before.yVector.y * tempAfter.xVector.y + before.yVector.z * tempAfter.xVector.z;
                        y = before.yVector.x * tempAfter.yVector.x + before.yVector.y * tempAfter.yVector.y + before.yVector.z * tempAfter.yVector.z;
                        z = before.yVector.x * tempAfter.zVector.x + before.yVector.y * tempAfter.zVector.z + before.yVector.z * tempAfter.zVector.z;
                        result.yVector.Copy(x, y, z);

                        x = before.zVector.x * tempAfter.xVector.x + before.zVector.y * tempAfter.xVector.y + before.zVector.z * tempAfter.xVector.z;
                        y = before.zVector.x * tempAfter.yVector.x + before.zVector.y * tempAfter.yVector.y + before.zVector.z * tempAfter.yVector.z;
                        z = before.zVector.x * tempAfter.zVector.x + before.zVector.y * tempAfter.zVector.y + before.zVector.z * tempAfter.zVector.z;
                        result.zVector.Copy(x, y, z);
                    }
                    else
                    {
                        x = before.xVector.x * after.xVector.x + before.xVector.y * after.xVector.y + before.xVector.z * after.xVector.z;
                        y = before.xVector.x * after.yVector.x + before.xVector.y * after.yVector.y + before.xVector.z * after.yVector.z;
                        z = before.xVector.x * after.zVector.x + before.xVector.y * after.zVector.y + before.xVector.z * after.zVector.z;
                        result.xVector.Copy(x, y, z);

                        x = before.yVector.x * after.xVector.x + before.yVector.y * after.xVector.y + before.yVector.z * after.xVector.z;
                        y = before.yVector.x * after.yVector.x + before.yVector.y * after.yVector.y + before.yVector.z * after.yVector.z;
                        z = before.yVector.x * after.zVector.x + before.yVector.y * after.zVector.z + before.yVector.z * after.zVector.z;
                        result.yVector.Copy(x, y, z);

                        x = before.zVector.x * after.xVector.x + before.zVector.y * after.xVector.y + before.zVector.z * after.xVector.z;
                        y = before.zVector.x * after.yVector.x + before.zVector.y * after.yVector.y + before.zVector.z * after.yVector.z;
                        z = before.zVector.x * after.zVector.x + before.zVector.y * after.zVector.y + before.zVector.z * after.zVector.z;
                        result.zVector.Copy(x, y, z);
                    }
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }
        public static void Multiply(Coordinate3D.Matrix4X4 before, Coordinate3D.Matrix4X4 after, Coordinate3D.Matrix4X4 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    float x, y, z, q;

                    if (before == result)
                    {
                        Coordinate3D.Matrix4X4 tempBefore = new Coordinate3D.Matrix4X4(before);

                        x = tempBefore.xVector.vector1X3.x * after.xVector.vector1X3.x + tempBefore.xVector.vector1X3.y * after.yVector.vector1X3.x + tempBefore.xVector.vector1X3.z * after.zVector.vector1X3.x + tempBefore.xVector.q * after.qVector.vector1X3.x;
                        y = tempBefore.xVector.vector1X3.x * after.xVector.vector1X3.y + tempBefore.xVector.vector1X3.y * after.yVector.vector1X3.y + tempBefore.xVector.vector1X3.z * after.zVector.vector1X3.y + tempBefore.xVector.q * after.qVector.vector1X3.y;
                        z = tempBefore.xVector.vector1X3.x * after.xVector.vector1X3.z + tempBefore.xVector.vector1X3.y * after.yVector.vector1X3.z + tempBefore.xVector.vector1X3.z * after.zVector.vector1X3.z + tempBefore.xVector.q * after.qVector.vector1X3.z;
                        q = tempBefore.xVector.vector1X3.x * after.xVector.q + tempBefore.xVector.vector1X3.y * after.yVector.q + tempBefore.xVector.vector1X3.z * after.zVector.q + tempBefore.xVector.q * after.qVector.q;
                        result.xVector.Copy(x, y, z, q);

                        x = tempBefore.yVector.vector1X3.x * after.xVector.vector1X3.x + tempBefore.yVector.vector1X3.y * after.yVector.vector1X3.x + tempBefore.yVector.vector1X3.z * after.zVector.vector1X3.x + tempBefore.yVector.q * after.qVector.vector1X3.x;
                        y = tempBefore.yVector.vector1X3.x * after.xVector.vector1X3.y + tempBefore.yVector.vector1X3.y * after.yVector.vector1X3.y + tempBefore.yVector.vector1X3.z * after.zVector.vector1X3.y + tempBefore.yVector.q * after.qVector.vector1X3.y;
                        z = tempBefore.yVector.vector1X3.x * after.xVector.vector1X3.z + tempBefore.yVector.vector1X3.y * after.yVector.vector1X3.z + tempBefore.yVector.vector1X3.z * after.zVector.vector1X3.z + tempBefore.yVector.q * after.qVector.vector1X3.z;
                        q = tempBefore.yVector.vector1X3.x * after.xVector.q + tempBefore.yVector.vector1X3.y * after.yVector.q + tempBefore.yVector.vector1X3.z * after.zVector.q + tempBefore.yVector.q * after.qVector.q;
                        result.yVector.Copy(x, y, z, q);

                        x = tempBefore.zVector.vector1X3.x * after.xVector.vector1X3.x + tempBefore.zVector.vector1X3.y * after.yVector.vector1X3.x + tempBefore.zVector.vector1X3.z * after.zVector.vector1X3.x + tempBefore.zVector.q * after.qVector.vector1X3.x;
                        y = tempBefore.zVector.vector1X3.x * after.xVector.vector1X3.y + tempBefore.zVector.vector1X3.y * after.yVector.vector1X3.y + tempBefore.zVector.vector1X3.z * after.zVector.vector1X3.y + tempBefore.zVector.q * after.qVector.vector1X3.y;
                        z = tempBefore.zVector.vector1X3.x * after.xVector.vector1X3.z + tempBefore.zVector.vector1X3.y * after.yVector.vector1X3.z + tempBefore.zVector.vector1X3.z * after.zVector.vector1X3.z + tempBefore.zVector.q * after.qVector.vector1X3.z;
                        q = tempBefore.zVector.vector1X3.x * after.xVector.q + tempBefore.zVector.vector1X3.y * after.yVector.q + tempBefore.zVector.vector1X3.z * after.zVector.q + tempBefore.zVector.q * after.qVector.q;
                        result.zVector.Copy(x, y, z, q);

                        x = tempBefore.qVector.vector1X3.x * after.xVector.vector1X3.x + tempBefore.qVector.vector1X3.y * after.yVector.vector1X3.x + tempBefore.qVector.vector1X3.z * after.zVector.vector1X3.x + tempBefore.qVector.q * after.qVector.vector1X3.x;
                        y = tempBefore.qVector.vector1X3.x * after.xVector.vector1X3.y + tempBefore.qVector.vector1X3.y * after.yVector.vector1X3.y + tempBefore.qVector.vector1X3.z * after.zVector.vector1X3.y + tempBefore.qVector.q * after.qVector.vector1X3.y;
                        z = tempBefore.qVector.vector1X3.x * after.xVector.vector1X3.z + tempBefore.qVector.vector1X3.y * after.yVector.vector1X3.z + tempBefore.qVector.vector1X3.z * after.zVector.vector1X3.z + tempBefore.qVector.q * after.qVector.vector1X3.z;
                        q = tempBefore.qVector.vector1X3.x * after.xVector.q + tempBefore.qVector.vector1X3.y * after.yVector.q + tempBefore.qVector.vector1X3.z * after.zVector.q + tempBefore.qVector.q * after.qVector.q;
                        result.qVector.Copy(x, y, z, q);
                    }
                    else if (after == result)
                    {
                        Coordinate3D.Matrix4X4 tempAfter = new Coordinate3D.Matrix4X4(after);

                        x = before.xVector.vector1X3.x * tempAfter.xVector.vector1X3.x + before.xVector.vector1X3.y * tempAfter.yVector.vector1X3.x + before.xVector.vector1X3.z * tempAfter.zVector.vector1X3.x + before.xVector.q * tempAfter.qVector.vector1X3.x;
                        y = before.xVector.vector1X3.x * tempAfter.xVector.vector1X3.y + before.xVector.vector1X3.y * tempAfter.yVector.vector1X3.y + before.xVector.vector1X3.z * tempAfter.zVector.vector1X3.y + before.xVector.q * tempAfter.qVector.vector1X3.y;
                        z = before.xVector.vector1X3.x * tempAfter.xVector.vector1X3.z + before.xVector.vector1X3.y * tempAfter.yVector.vector1X3.z + before.xVector.vector1X3.z * tempAfter.zVector.vector1X3.z + before.xVector.q * tempAfter.qVector.vector1X3.z;
                        q = before.xVector.vector1X3.x * tempAfter.xVector.q + before.xVector.vector1X3.y * tempAfter.yVector.q + before.xVector.vector1X3.z * tempAfter.zVector.q + before.xVector.q * tempAfter.qVector.q;
                        result.xVector.Copy(x, y, z, q);

                        x = before.yVector.vector1X3.x * tempAfter.xVector.vector1X3.x + before.yVector.vector1X3.y * tempAfter.yVector.vector1X3.x + before.yVector.vector1X3.z * tempAfter.zVector.vector1X3.x + before.yVector.q * tempAfter.qVector.vector1X3.x;
                        y = before.yVector.vector1X3.x * tempAfter.xVector.vector1X3.y + before.yVector.vector1X3.y * tempAfter.yVector.vector1X3.y + before.yVector.vector1X3.z * tempAfter.zVector.vector1X3.y + before.yVector.q * tempAfter.qVector.vector1X3.y;
                        z = before.yVector.vector1X3.x * tempAfter.xVector.vector1X3.z + before.yVector.vector1X3.y * tempAfter.yVector.vector1X3.z + before.yVector.vector1X3.z * tempAfter.zVector.vector1X3.z + before.yVector.q * tempAfter.qVector.vector1X3.z;
                        q = before.yVector.vector1X3.x * tempAfter.xVector.q + before.yVector.vector1X3.y * tempAfter.yVector.q + before.yVector.vector1X3.z * tempAfter.zVector.q + before.yVector.q * tempAfter.qVector.q;
                        result.yVector.Copy(x, y, z, q);

                        x = before.zVector.vector1X3.x * tempAfter.xVector.vector1X3.x + before.zVector.vector1X3.y * tempAfter.yVector.vector1X3.x + before.zVector.vector1X3.z * tempAfter.zVector.vector1X3.x + before.zVector.q * tempAfter.qVector.vector1X3.x;
                        y = before.zVector.vector1X3.x * tempAfter.xVector.vector1X3.y + before.zVector.vector1X3.y * tempAfter.yVector.vector1X3.y + before.zVector.vector1X3.z * tempAfter.zVector.vector1X3.y + before.zVector.q * tempAfter.qVector.vector1X3.y;
                        z = before.zVector.vector1X3.x * tempAfter.xVector.vector1X3.z + before.zVector.vector1X3.y * tempAfter.yVector.vector1X3.z + before.zVector.vector1X3.z * tempAfter.zVector.vector1X3.z + before.zVector.q * tempAfter.qVector.vector1X3.z;
                        q = before.zVector.vector1X3.x * tempAfter.xVector.q + before.zVector.vector1X3.y * tempAfter.yVector.q + before.zVector.vector1X3.z * tempAfter.zVector.q + before.zVector.q * tempAfter.qVector.q;
                        result.zVector.Copy(x, y, z, q);

                        x = before.qVector.vector1X3.x * tempAfter.xVector.vector1X3.x + before.qVector.vector1X3.y * tempAfter.yVector.vector1X3.x + before.qVector.vector1X3.z * tempAfter.zVector.vector1X3.x + before.qVector.q * tempAfter.qVector.vector1X3.x;
                        y = before.qVector.vector1X3.x * tempAfter.xVector.vector1X3.y + before.qVector.vector1X3.y * tempAfter.yVector.vector1X3.y + before.qVector.vector1X3.z * tempAfter.zVector.vector1X3.y + before.qVector.q * tempAfter.qVector.vector1X3.y;
                        z = before.qVector.vector1X3.x * tempAfter.xVector.vector1X3.z + before.qVector.vector1X3.y * tempAfter.yVector.vector1X3.z + before.qVector.vector1X3.z * tempAfter.zVector.vector1X3.z + before.qVector.q * tempAfter.qVector.vector1X3.z;
                        q = before.qVector.vector1X3.x * tempAfter.xVector.q + before.qVector.vector1X3.y * tempAfter.yVector.q + before.qVector.vector1X3.z * tempAfter.zVector.q + before.qVector.q * tempAfter.qVector.q;
                        result.qVector.Copy(x, y, z, q);
                    }
                    else
                    {
                        x = before.xVector.vector1X3.x * after.xVector.vector1X3.x + before.xVector.vector1X3.y * after.yVector.vector1X3.x + before.xVector.vector1X3.z * after.zVector.vector1X3.x + before.xVector.q * after.qVector.vector1X3.x;
                        y = before.xVector.vector1X3.x * after.xVector.vector1X3.y + before.xVector.vector1X3.y * after.yVector.vector1X3.y + before.xVector.vector1X3.z * after.zVector.vector1X3.y + before.xVector.q * after.qVector.vector1X3.y;
                        z = before.xVector.vector1X3.x * after.xVector.vector1X3.z + before.xVector.vector1X3.y * after.yVector.vector1X3.z + before.xVector.vector1X3.z * after.zVector.vector1X3.z + before.xVector.q * after.qVector.vector1X3.z;
                        q = before.xVector.vector1X3.x * after.xVector.q + before.xVector.vector1X3.y * after.yVector.q + before.xVector.vector1X3.z * after.zVector.q + before.xVector.q * after.qVector.q;
                        result.xVector.Copy(x, y, z, q);

                        x = before.yVector.vector1X3.x * after.xVector.vector1X3.x + before.yVector.vector1X3.y * after.yVector.vector1X3.x + before.yVector.vector1X3.z * after.zVector.vector1X3.x + before.yVector.q * after.qVector.vector1X3.x;
                        y = before.yVector.vector1X3.x * after.xVector.vector1X3.y + before.yVector.vector1X3.y * after.yVector.vector1X3.y + before.yVector.vector1X3.z * after.zVector.vector1X3.y + before.yVector.q * after.qVector.vector1X3.y;
                        z = before.yVector.vector1X3.x * after.xVector.vector1X3.z + before.yVector.vector1X3.y * after.yVector.vector1X3.z + before.yVector.vector1X3.z * after.zVector.vector1X3.z + before.yVector.q * after.qVector.vector1X3.z;
                        q = before.yVector.vector1X3.x * after.xVector.q + before.yVector.vector1X3.y * after.yVector.q + before.yVector.vector1X3.z * after.zVector.q + before.yVector.q * after.qVector.q;
                        result.yVector.Copy(x, y, z, q);

                        x = before.zVector.vector1X3.x * after.xVector.vector1X3.x + before.zVector.vector1X3.y * after.yVector.vector1X3.x + before.zVector.vector1X3.z * after.zVector.vector1X3.x + before.zVector.q * after.qVector.vector1X3.x;
                        y = before.zVector.vector1X3.x * after.xVector.vector1X3.y + before.zVector.vector1X3.y * after.yVector.vector1X3.y + before.zVector.vector1X3.z * after.zVector.vector1X3.y + before.zVector.q * after.qVector.vector1X3.y;
                        z = before.zVector.vector1X3.x * after.xVector.vector1X3.z + before.zVector.vector1X3.y * after.yVector.vector1X3.z + before.zVector.vector1X3.z * after.zVector.vector1X3.z + before.zVector.q * after.qVector.vector1X3.z;
                        q = before.zVector.vector1X3.x * after.xVector.q + before.zVector.vector1X3.y * after.yVector.q + before.zVector.vector1X3.z * after.zVector.q + before.zVector.q * after.qVector.q;
                        result.zVector.Copy(x, y, z, q);

                        x = before.qVector.vector1X3.x * after.xVector.vector1X3.x + before.qVector.vector1X3.y * after.yVector.vector1X3.x + before.qVector.vector1X3.z * after.zVector.vector1X3.x + before.qVector.q * after.qVector.vector1X3.x;
                        y = before.qVector.vector1X3.x * after.xVector.vector1X3.y + before.qVector.vector1X3.y * after.yVector.vector1X3.y + before.qVector.vector1X3.z * after.zVector.vector1X3.y + before.qVector.q * after.qVector.vector1X3.y;
                        z = before.qVector.vector1X3.x * after.xVector.vector1X3.z + before.qVector.vector1X3.y * after.yVector.vector1X3.z + before.qVector.vector1X3.z * after.zVector.vector1X3.z + before.qVector.q * after.qVector.vector1X3.z;
                        q = before.qVector.vector1X3.x * after.xVector.q + before.qVector.vector1X3.y * after.yVector.q + before.qVector.vector1X3.z * after.zVector.q + before.qVector.q * after.qVector.q;
                        result.qVector.Copy(x, y, z, q);
                    }
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
            /*x = xVector.vector1X3.x * after.xVector.vector1X3.x + xVector.vector1X3.y * after.xVector.vector1X3.y + xVector.vector1X3.z * after.xVector.vector1X3.z + xVector.q * after.xVector.q;
            y = xVector.vector1X3.x * after.yVector.vector1X3.x + xVector.vector1X3.y * after.yVector.vector1X3.y + xVector.vector1X3.z * after.yVector.vector1X3.z + xVector.q * after.yVector.q;
            z = xVector.vector1X3.x * after.zVector.vector1X3.x + xVector.vector1X3.y * after.zVector.vector1X3.y + xVector.vector1X3.z * after.zVector.vector1X3.z + xVector.q * after.zVector.q;
            q = xVector.vector1X3.x * after.qVector.vector1X3.x + xVector.vector1X3.y * after.qVector.vector1X3.y + xVector.vector1X3.z * after.qVector.vector1X3.z + xVector.q * after.qVector.q;
            result.setxVector(new Vector1X4(x, y, z, q));

            x = yVector.vector1X3.x * after.xVector.vector1X3.x + yVector.vector1X3.y * after.xVector.vector1X3.y + yVector.vector1X3.z * after.xVector.vector1X3.z + yVector.q * after.xVector.q;
            y = yVector.vector1X3.x * after.yVector.vector1X3.x + yVector.vector1X3.y * after.yVector.vector1X3.y + yVector.vector1X3.z * after.yVector.vector1X3.z + yVector.q * after.yVector.q;
            z = yVector.vector1X3.x * after.zVector.vector1X3.x + yVector.vector1X3.y * after.zVector.vector1X3.y + yVector.vector1X3.z * after.zVector.vector1X3.z + yVector.q * after.zVector.q;
            q = yVector.vector1X3.x * after.qVector.vector1X3.x + yVector.vector1X3.y * after.qVector.vector1X3.y + yVector.vector1X3.z * after.qVector.vector1X3.z + yVector.q * after.qVector.q;
            result.setyVector(new Vector1X4(x, y, z, q));

            x = zVector.vector1X3.x * after.xVector.vector1X3.x + zVector.vector1X3.y * after.xVector.vector1X3.y + zVector.vector1X3.z * after.xVector.vector1X3.z + zVector.q * after.xVector.q;
            y = zVector.vector1X3.x * after.yVector.vector1X3.x + zVector.vector1X3.y * after.yVector.vector1X3.y + zVector.vector1X3.z * after.yVector.vector1X3.z + zVector.q * after.yVector.q;
            z = zVector.vector1X3.x * after.zVector.vector1X3.x + zVector.vector1X3.y * after.zVector.vector1X3.y + zVector.vector1X3.z * after.zVector.vector1X3.z + zVector.q * after.zVector.q;
            q = zVector.vector1X3.x * after.qVector.vector1X3.x + zVector.vector1X3.y * after.qVector.vector1X3.y + zVector.vector1X3.z * after.qVector.vector1X3.z + zVector.q * after.qVector.q;
            result.setzVector(new Vector1X4(x, y, z, q));

            x = qVector.vector1X3.x * after.xVector.vector1X3.x + qVector.vector1X3.y * after.xVector.vector1X3.y + qVector.vector1X3.z * after.xVector.vector1X3.z + qVector.q * after.xVector.q;
            y = qVector.vector1X3.x * after.yVector.vector1X3.x + qVector.vector1X3.y * after.yVector.vector1X3.y + qVector.vector1X3.z * after.yVector.vector1X3.z + qVector.q * after.yVector.q;
            z = qVector.vector1X3.x * after.zVector.vector1X3.x + qVector.vector1X3.y * after.zVector.vector1X3.y + qVector.vector1X3.z * after.zVector.vector1X3.z + qVector.q * after.zVector.q;
            q = qVector.vector1X3.x * after.qVector.vector1X3.x + qVector.vector1X3.y * after.qVector.vector1X3.y + qVector.vector1X3.z * after.qVector.vector1X3.z + qVector.q * after.qVector.q;
            result.setqVector(new Vector1X4(x, y, z, q));*/

        }
        static void Multiply(Coordinate3D.Quaternion before, Coordinate3D.Quaternion after, Coordinate3D.Quaternion result)
        {
            if (before == result)
            {
                Coordinate3D.Quaternion tempBefore = new Coordinate3D.Quaternion(before);

                result.unrealVector.x =   after.unrealVector.x * tempBefore.realValue - after.unrealVector.y * tempBefore.unrealVector.z + after.unrealVector.z * tempBefore.unrealVector.y + after.realValue * tempBefore.unrealVector.x;
                result.unrealVector.y =   after.unrealVector.x * tempBefore.unrealVector.z + after.unrealVector.y * tempBefore.realValue - after.unrealVector.z * tempBefore.unrealVector.x + after.realValue * tempBefore.unrealVector.y;
                result.unrealVector.z = - after.unrealVector.x * tempBefore.unrealVector.y + after.unrealVector.y * tempBefore.unrealVector.x + after.unrealVector.z * tempBefore.realValue + after.realValue * tempBefore.unrealVector.z;
                result.realValue = - after.unrealVector.x * tempBefore.unrealVector.x - after.unrealVector.y * tempBefore.unrealVector.y - after.unrealVector.z * tempBefore.unrealVector.z + after.realValue * tempBefore.realValue;
            }
            else if (after == result)
            {
                Coordinate3D.Quaternion tempAfter = new Coordinate3D.Quaternion();

                result.unrealVector.x =   tempAfter.unrealVector.x * before.realValue - tempAfter.unrealVector.y * before.unrealVector.z + tempAfter.unrealVector.z * before.unrealVector.y + tempAfter.realValue * before.unrealVector.x;
                result.unrealVector.y =   tempAfter.unrealVector.x * before.unrealVector.z + tempAfter.unrealVector.y * before.realValue - tempAfter.unrealVector.z * before.unrealVector.x + tempAfter.realValue * before.unrealVector.y;
                result.unrealVector.z = - tempAfter.unrealVector.x * before.unrealVector.y + tempAfter.unrealVector.y * before.unrealVector.x + tempAfter.unrealVector.z * before.realValue + tempAfter.realValue * before.unrealVector.z;
                result.realValue = - tempAfter.unrealVector.x * before.unrealVector.x - tempAfter.unrealVector.y * before.unrealVector.y - tempAfter.unrealVector.z * before.unrealVector.z + tempAfter.realValue * before.realValue;
            }
            else
            {
                result.unrealVector.x =   after.unrealVector.x * before.realValue - after.unrealVector.y * before.unrealVector.z + after.unrealVector.z * before.unrealVector.y + after.realValue * before.unrealVector.x;
                result.unrealVector.y =   after.unrealVector.x * before.unrealVector.z + after.unrealVector.y * before.realValue - after.unrealVector.z * before.unrealVector.x + after.realValue * before.unrealVector.y;
                result.unrealVector.z = - after.unrealVector.x * before.unrealVector.y + after.unrealVector.y * before.unrealVector.x + after.unrealVector.z * before.realValue + after.realValue * before.unrealVector.z;
                result.realValue = - after.unrealVector.x * before.unrealVector.x - after.unrealVector.y * before.unrealVector.y - after.unrealVector.z * before.unrealVector.z + after.realValue * before.realValue;
            }
        }

        public static void Scaled(Coordinate3D.Vector1X3 vector1X3, float s, Coordinate3D.Vector1X3 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    result.x = vector1X3.x * s;
                    result.y = vector1X3.y * s;
                    result.z = vector1X3.z * s;
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }
        public static void Scaled(Coordinate3D.Vector1X4 vector1X4, float s, Coordinate3D.Vector1X4 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    Scaled(vector1X4.vector1X3, s, result.vector1X3);
                    result.q = vector1X4.q;
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }
        public static void Scaled(Coordinate3D.Matrix3X3 matrix3X3, int s, Coordinate3D.Matrix3X3 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    Coordinate3D.Matrix3X3 S = new Coordinate3D.Matrix3X3(s, 0, 0,
                            0, s, 0,
                            0, 0, s);

                    Multiply(matrix3X3, S, result);
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }
        public static void Scaled(Coordinate3D.Matrix4X4 matrix4X4, int s, Coordinate3D.Matrix4X4 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    Coordinate3D.Matrix4X4 S = new Coordinate3D.Matrix4X4(s, 0, 0, 0,
                            0, s, 0, 0,
                            0, 0, s, 0,
                            0, 0, 0, 1);

                    Multiply(matrix4X4, S, result);
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }

        public static float Inner(Coordinate3D.Vector1X4 before, Coordinate3D.Vector1X4 after)
        {
            return ((before.vector1X3.x * after.vector1X3.x) + (before.vector1X3.y * after.vector1X3.y) + (before.vector1X3.z * after.vector1X3.z));
        }

        public static void Outer(Coordinate3D.Vector1X4 before, Coordinate3D.Vector1X4 after, Coordinate3D.Vector1X4 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    result.Copy(((before.vector1X3.z * after.vector1X3.y) - (before.vector1X3.y * after.vector1X3.z)),
                            ((before.vector1X3.x * after.vector1X3.z) - (before.vector1X3.z * after.vector1X3.x)),
                            ((before.vector1X3.y * after.vector1X3.x) - (before.vector1X3.x * after.vector1X3.y)),
                            0);
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }

        public static void Transpose(Coordinate3D.Matrix3X3 matrix3X3, Coordinate3D.Matrix3X3 result)
        {
            try {
                if (result == null)
                    throw new Exception();
                else
                {
                    if (matrix3X3 == result)
                    {
                        Coordinate3D.Matrix3X3 tempMatrix3X3 = new Coordinate3D.Matrix3X3(matrix3X3);

                        result.xVector.Copy(tempMatrix3X3.xVector.x, tempMatrix3X3.yVector.x, tempMatrix3X3.zVector.x);
                        result.yVector.Copy(tempMatrix3X3.xVector.y, tempMatrix3X3.yVector.y, tempMatrix3X3.zVector.y);
                        result.zVector.Copy(tempMatrix3X3.xVector.z, tempMatrix3X3.yVector.z, tempMatrix3X3.zVector.z);
                    }
                    else
                    {
                        result.xVector.Copy(matrix3X3.xVector.x, matrix3X3.yVector.x, matrix3X3.zVector.x);
                        result.yVector.Copy(matrix3X3.xVector.y, matrix3X3.yVector.y, matrix3X3.zVector.y);
                        result.zVector.Copy(matrix3X3.xVector.z, matrix3X3.yVector.z, matrix3X3.zVector.z);
                    }
                }
            } catch (Exception e) {
                Log.v("Exception", "Return Value is null");
            }
        }

        public static Coordinate3D.Vector1X4[] ShortestSegment(Coordinate3D.Vector1X4 point, Coordinate3D.Vector1X4[] segment)
        {
            Coordinate3D.Vector1X4[] shortestSegment = new Coordinate3D.Vector1X4[2];
            shortestSegment[0] = point;

            Coordinate3D.Vector1X4 vector = new Coordinate3D.Vector1X4();
            Subtract(point, segment[0], vector);
            float magnitude = vector.Magnitude();
            vector.Normalize();

            Coordinate3D.Vector1X4 vector_ = new Coordinate3D.Vector1X4();
            Subtract(segment[1], segment[0], vector_);
            vector_.Normalize();

            Coordinate3D.Vector1X4 scaledVector = new Coordinate3D.Vector1X4();
            Scaled(vector, magnitude, scaledVector);

            float t = Inner(scaledVector, vector_);
            if (Inner(vector, vector_) == 1)
            {
                if (t < 0)
                    t = 0;
                else if (t > 1)
                    t = 1;
                else
                    return shortestSegment;
            }
            else
            {
                if (t < 0)
                    t = 0;
                else if (t > 1)
                    t = 1;
            }
            Scaled(vector_, t, shortestSegment[1]);

            return shortestSegment;
        }
        public static Coordinate3D.Vector1X4[] ShortestSegment(Coordinate3D.Vector1X4 point, Coordinate3D.Vector1X4 planeVector, Coordinate3D.Vector1X4[] planeRange) {
            Coordinate3D.Vector1X4[] shortestSegment = new Coordinate3D.Vector1X4[2];
            shortestSegment[0] = point;

            if (isPointInPlane(point, planeVector, planeRange))
            {
                if (isPointOnPlane(point, planeVector, planeRange[0]))
                    return shortestSegment;
                else
                    shortestSegment[1] = FootOfPerpendicular(point, planeVector, planeRange[0]);
            }
            else {
                Coordinate3D.Vector1X4[] edge = new Coordinate3D.Vector1X4[2];
                float distance = Float.MAX_VALUE, distance_;
                int i = 0;
                while (i < planeRange.length) {
                    edge[0] = planeRange[i];
                    if (i + 1 == planeRange.length)
                        edge[1] = planeRange[0];
                    else
                        edge[1] = planeRange[i + 1];

                    Coordinate3D.Vector1X4[] tempSegment = ShortestSegment(point, edge);

                    if (tempSegment[1] == null)
                        return shortestSegment;
                    else
                    {
                        distance_ = Distance(point, tempSegment[1]);
                        if (distance_ < distance)
                        {
                            distance = Distance(point, tempSegment[1]);
                            shortestSegment[1] = tempSegment[1];
                        }
                    }

                    i++;
                }
            }
            return shortestSegment;
        }
        public static Coordinate3D.Vector1X4[] ShortestSegment(Coordinate3D.Vector1X4[] segment, Coordinate3D.Vector1X4[] segment_)
        {
            Coordinate3D.Vector1X4[] shortestSegment = new Coordinate3D.Vector1X4[2];
            Coordinate3D.Vector1X4 vector = new Coordinate3D.Vector1X4();
            Subtract(segment[1], segment[0], vector);
            vector.Normalize();
            Coordinate3D.Vector1X4 vector_ = new Coordinate3D.Vector1X4();
            Subtract(segment_[1], segment_[0], vector_);
            vector_.Normalize();

            if (Inner(vector, vector_) == 1)
            {
                Coordinate3D.Vector1X4[] tempVector = new Coordinate3D.Vector1X4[2];
                tempVector[0] = new Coordinate3D.Vector1X4();
                Subtract(segment_[0], segment[0], tempVector[0]);
                tempVector[1] = new Coordinate3D.Vector1X4();
                Subtract(segment_[1], segment[0], tempVector[1]);

                float[] t = new float[2];
                t[0] = Inner(tempVector[0], vector);
                t[1] = Inner(tempVector[1], vector);

                tempVector[0].Normalize();

                if (Inner(tempVector[0], vector) == 1)
                {
                    if (0 < t[0] && t[0] < 1)
                    {
                        shortestSegment[0] = new Coordinate3D.Vector1X4();
                        shortestSegment[0].Copy(segment_[0]);
                    }
                    else if (0 < t[1] && t[1] < 1)
                    {
                        shortestSegment[0] = new Coordinate3D.Vector1X4();
                        shortestSegment[0].Copy(segment_[1]);
                    }
                    else
                    {
                        shortestSegment[0] = new Coordinate3D.Vector1X4();
                        shortestSegment[1] = new Coordinate3D.Vector1X4();

                        float[] distance = new float[2];
                        distance[0] = Float.MAX_VALUE;
                        int i, j;
                        i = 0;
                        while (i < 2)
                        {
                            j = 0;
                            while (j < 2)
                            {
                                distance[1] = Distance(segment[i], segment_[j]);
                                if (distance[1] < distance[0])
                                {
                                    shortestSegment[0].Copy(segment[i]);
                                    shortestSegment[1].Copy(segment_[j]);
                                    distance[0] = distance[1];
                                }
                                j++;
                            }
                            i++;
                        }
                    }
                }
            }
            else
            {
                float a, b, c, d, e;
                Coordinate3D.Vector1X4 u = new Coordinate3D.Vector1X4();
                Subtract(segment[1], segment[0], u);
                u.Normalize();
                Coordinate3D.Vector1X4 v = new Coordinate3D.Vector1X4();
                Subtract(segment_[1], segment_[0], v);
                v.Normalize();
                Coordinate3D.Vector1X4 w0 = new Coordinate3D.Vector1X4();
                Subtract(segment[0], segment_[0], w0);

                a = Inner(u, u);
                b = Inner(u, v);
                c = Inner(v, v);
                d = Inner(u, w0);
                e = Inner(v, w0);

                float s = (b * e - c * d) / (a * c - b * b);
                float t = (a * e - b * d) / (a * c - b * b);

                if (s < 0)
                    s = 0;
                else if (1 < s)
                    s = 1;

                if (t < 0)
                    t = 0;
                else if (1 < t)
                    t = 1;

                Coordinate3D.Vector1X4 scaledVector = new Coordinate3D.Vector1X4();
                Scaled(u, s, scaledVector);

                shortestSegment[0] = new Coordinate3D.Vector1X4();
                Add(segment[0], scaledVector, shortestSegment[0]);
                Scaled(v, t, scaledVector);

                shortestSegment[1] = new Coordinate3D.Vector1X4();
                Add(segment_[0], scaledVector, shortestSegment[1]);

                if (Distance(shortestSegment[0], shortestSegment[1]) == 0)
                    shortestSegment[1] = null;
            }

            return shortestSegment;
        }
        public static Coordinate3D.Vector1X4[] ShortestSegment(Coordinate3D.Vector1X4[] segment, Coordinate3D.Vector1X4 planeVector, Coordinate3D.Vector1X4[] planeRange)
        {
            Coordinate3D.Vector1X4[] shortestSegment = new Coordinate3D.Vector1X4[2];
            Coordinate3D.Vector1X4 vector = new Coordinate3D.Vector1X4();
            Subtract(segment[1], segment[0], vector);
            vector.Normalize();

            boolean[] tempBool = new boolean[3];
            tempBool[0] = isPointInPlane(segment[0], planeVector, planeRange);
            tempBool[1] = isPointInPlane(segment[1], planeVector, planeRange);
            tempBool[2] = isPointOnPlane(segment[0], planeVector, planeRange[0]);

            if (Inner(vector, planeVector) == 0)
            {
                if (tempBool[2])
                {
                    if (tempBool[0])
                    {
                        shortestSegment[0] = new Coordinate3D.Vector1X4();
                        shortestSegment[0].Copy(segment[0]);
                    }
                    else if (tempBool[1])
                    {
                        shortestSegment[0] = new Coordinate3D.Vector1X4();
                        shortestSegment[0].Copy(segment[1]);
                    }
                    else
                    {
                        Coordinate3D.Vector1X4[] edge = new Coordinate3D.Vector1X4[2];
                        edge[0] = new Coordinate3D.Vector1X4();
                        edge[1] = new Coordinate3D.Vector1X4();

                        Coordinate3D.Vector1X4[] tempSegment;
                        float[] distance = new float[2];
                        distance[0] = Float.MAX_VALUE;

                        int i = 0;
                        while (i < planeRange.length)
                        {
                            if (i + 1 == planeRange.length)
                            {
                                edge[0].Copy(planeRange[0]);
                                edge[1].Copy(planeRange[i]);
                            }
                            else
                            {
                                edge[0].Copy(planeRange[i]);
                                edge[1].Copy(planeRange[i + 1]);
                            }

                            tempSegment = ShortestSegment(segment, edge);
                            if (tempSegment[1] == null) {
                                shortestSegment = tempSegment;
                                return shortestSegment;
                            }

                            distance[1] = Distance(tempSegment[0], tempSegment[1]);
                            if (distance[1] < distance[0])
                            {
                                shortestSegment = tempSegment;
                                distance[0] = distance[1];
                            }
                            i++;
                        }
                    }
                }
                else
                {
                    shortestSegment[0] = new Coordinate3D.Vector1X4();
                    shortestSegment[0].Copy(segment[0]);
                    shortestSegment[1] = FootOfPerpendicular(segment[0], planeVector, planeRange[0]);
                }
            }
            else
            {
                Coordinate3D.Vector1X4[] penetrationPoint = new Coordinate3D.Vector1X4[1];
                if (isPenetrate(segment, planeVector, planeRange[0], penetrationPoint))
                {
                    if (isPointInPlane(penetrationPoint[0], planeVector, planeRange))
                        shortestSegment[0] = penetrationPoint[0];
                    else
                        shortestSegment = ShortestSegment(penetrationPoint[0], planeVector, planeRange);
                }
                else
                {
                    if (tempBool[0] && tempBool[1])
                    {
                        Coordinate3D.Vector1X4[][] tempSegments = new Coordinate3D.Vector1X4[2][];
                        float[] distance = new float[2];
                        distance[0] = Float.MAX_VALUE;
                        int i = 0;
                        while (i < 2)
                        {
                            tempSegments[0] = ShortestSegment(segment[0], planeVector, planeRange);
                            if (tempSegments[0][1] == null)
                            {
                                shortestSegment = tempSegments[0];
                                return shortestSegment;
                            }
                            distance[0] = Distance(tempSegments[0][0], tempSegments[0][1]);

                            tempSegments[1] = ShortestSegment(segment[1], planeVector, planeRange);
                            if (tempSegments[1][1] == null)
                            {
                                shortestSegment = tempSegments[1];
                                return shortestSegment;
                            }
                            distance[1] = Distance(tempSegments[1][0], tempSegments[1][1]);

                            if (distance[0] < distance[1])
                                shortestSegment = tempSegments[0];
                            else
                                shortestSegment = tempSegments[1];
                            i++;
                        }
                    }
                    else if (!tempBool[0] && !tempBool[1])
                    {
                        Coordinate3D.Vector1X4[] edge = new Coordinate3D.Vector1X4[2];
                        edge[0] = new Coordinate3D.Vector1X4();
                        edge[1] = new Coordinate3D.Vector1X4();
                        Coordinate3D.Vector1X4[] tempSegment;
                        float[] distance = new float[2];
                        distance[0] = Float.MAX_VALUE;
                        int i = 0;
                        while (i < planeRange.length)
                        {
                            if (i + 1 == planeRange.length)
                            {
                                edge[0].Copy(planeRange[0]);
                                edge[1].Copy(planeRange[i]);
                            }
                            else
                            {
                                edge[0].Copy(planeRange[i]);
                                edge[1].Copy(planeRange[i + 1]);
                            }

                            tempSegment = ShortestSegment(segment, edge);
                            if (tempSegment[1] == null)
                            {
                                shortestSegment = tempSegment;
                                return shortestSegment;
                            }
                            distance[1] = Distance(tempSegment[0], tempSegment[1]);
                            if (distance[1] < distance[0])
                            {
                                shortestSegment = tempSegment;
                                distance[0] = distance[1];
                            }
                            i++;
                        }
                    }
                    else
                    {
                        Coordinate3D.Vector1X4[] tempSegment;
                        Coordinate3D.Vector1X4[] edge = new Coordinate3D.Vector1X4[2];
                        edge[0] = new Coordinate3D.Vector1X4();
                        edge[1] = new Coordinate3D.Vector1X4();
                        float[] distance = new float[2];
                        distance[0] = Float.MAX_VALUE;
                        int i = 0;
                        while (i < planeRange.length)
                        {
                            if (i + 1 == planeRange.length)
                            {
                                edge[0].Copy(planeRange[0]);
                                edge[1].Copy(planeRange[i]);
                            }
                            else
                            {
                                edge[0].Copy(planeRange[i]);
                                edge[1].Copy(planeRange[i + 1]);
                            }

                            tempSegment = ShortestSegment(segment, edge);
                            if (tempSegment[1] == null)
                            {
                                shortestSegment = tempSegment;
                                return shortestSegment;
                            }
                            distance[1] = Distance(tempSegment[0], tempSegment[1]);
                            if (distance[1] < distance[0])
                            {
                                shortestSegment = tempSegment;
                                distance[0] = distance[1];
                            }

                            i++;
                        }

                        if (tempBool[0])
                        {
                            tempSegment = ShortestSegment(segment[0], planeVector, planeRange);
                            if (tempSegment[1] == null)
                            {
                                shortestSegment = tempSegment;
                                return shortestSegment;
                            }
                            distance[1] = Distance(tempSegment[0], tempSegment[1]);
                            if (distance[1] < distance[0])
                            {
                                shortestSegment = tempSegment;
                                distance[0] = distance[1];
                            }
                        }
                        else
                        {
                            tempSegment = ShortestSegment(segment[1], planeVector, planeRange);
                            if (tempSegment[1] == null)
                            {
                                shortestSegment = tempSegment;
                                return shortestSegment;
                            }
                            distance[1] = Distance(tempSegment[0], tempSegment[1]);
                            if (distance[1] < distance[0])
                            {
                                shortestSegment = tempSegment;
                                distance[0] = distance[1];
                            }
                        }
                    }
                }
            }

            return shortestSegment;
        }

        public static Coordinate3D.Vector1X4 FootOfPerpendicular(Coordinate3D.Vector1X4 point, Coordinate3D.Vector1X4 planeVector, Coordinate3D.Vector1X4 planeVertex)
        {
            float planeConstant = getPlaneConstant(planeVector, planeVertex);
            float pointConstant = getPlaneConstant(planeVector, point);

            float distance = (float) (Math.abs(pointConstant - planeConstant) / Math.sqrt(Math.pow(planeVector.vector1X3.x, 2) + Math.pow(planeVector.vector1X3.y, 2) + Math.pow(planeVector.vector1X3.z, 2)));

            Coordinate3D.Vector1X4 tempVector = new Coordinate3D.Vector1X4();
            Inverse(planeVector, tempVector);
            Scaled(tempVector, distance, tempVector);
            Add(point, tempVector, tempVector);
            return tempVector;
        }

        public static boolean isPenetrate(Coordinate3D.Vector1X4[] segment, Coordinate3D.Vector1X4 planeVector, Coordinate3D.Vector1X4 planeVertex, Coordinate3D.Vector1X4[] penetrationPoint)
        {
            float vertexValue, vertexValue_, planeConstant;

            planeConstant = getPlaneConstant(planeVector, planeVertex);
            vertexValue = getPlaneConstant(planeVector, segment[0]);
            vertexValue_ = getPlaneConstant(planeVector, segment[1]);

            if (!((vertexValue - planeConstant) * (vertexValue_ - planeConstant) > 0))
            {
                Coordinate3D.Vector1X4 vector = new Coordinate3D.Vector1X4();
                Subtract(segment[1], segment[0], vector);
                float vectorValue = getPlaneConstant(planeVector, vector);
                float t = (planeConstant - vertexValue) / vectorValue;
                if (t < 1 && 0 < t) {
                    Coordinate3D.Vector1X4 tempVector = new Coordinate3D.Vector1X4();
                    Scaled(vector, t, tempVector);
                    Add(segment[0], tempVector, tempVector);
                    penetrationPoint[0].Copy(tempVector);
                /*if (!(vertexValue - planeConstant > 0))
                    penetrationPoint[1] = segment[0];
                else
                    penetrationPoint [1] = segment[1];*/

                    return true;
                }
            }
            return false;
        }

        public static boolean isPointOnPlane(Coordinate3D.Vector1X4 point, Coordinate3D.Vector1X4 planeVector, Coordinate3D.Vector1X4 planeVertex)
        {
            float planeConstant, pointConstant;
            planeConstant = getPlaneConstant(planeVector, planeVertex);
            pointConstant = getPlaneConstant(planeVector, point);

            return Math.abs(planeConstant - pointConstant) < 0.001f;
        }

        public static boolean isPointInPlane(Coordinate3D.Vector1X4 point, Coordinate3D.Vector1X4 planeVector, Coordinate3D.Vector1X4[] planeVertexes)
        {
            Coordinate3D.Vector1X4 pointOnPlane = FootOfPerpendicular(point, planeVector, planeVertexes[0]);

            int i = 0;
            Coordinate3D.Vector1X4 planeEdge, planeEdge_, pointVector, outer, outer_;
            planeEdge = new Coordinate3D.Vector1X4();
            planeEdge_ = new Coordinate3D.Vector1X4();
            pointVector = new Coordinate3D.Vector1X4();
            outer = new Coordinate3D.Vector1X4();
            outer_ = new Coordinate3D.Vector1X4();

            while (i < planeVertexes.length)
            {
                if (i == 0) {
                    Subtract(planeVertexes[planeVertexes.length - 1], planeVertexes[0], planeEdge);
                    Subtract(planeVertexes[i + 1], planeVertexes[0], planeEdge_);
                }
                else if (i + 1 == planeVertexes.length){
                    Subtract(planeVertexes[i - 1], planeVertexes[i], planeEdge);
                    Subtract(planeVertexes[0], planeVertexes[i], planeEdge_);
                }
                else {
                    Subtract(planeVertexes[i - 1], planeVertexes[i], planeEdge);
                    Subtract(planeVertexes[i + 1], planeVertexes[i], planeEdge_);
                }

                Subtract(pointOnPlane, planeVertexes[i], pointVector);

                planeEdge.Normalize();
                planeEdge_.Normalize();
                pointVector.Normalize();

                Outer(planeEdge, pointVector, outer);
                Outer(pointVector, planeEdge_, outer_);

                if (Inner(outer, outer_) < 0)
                    return false;
                i++;
            }
            return true;
        }

        public static float getPlaneConstant(Coordinate3D.Vector1X4 planeVector, Coordinate3D.Vector1X4 planeVertex)
        {
            return planeVector.vector1X3.x * planeVertex.vector1X3.x
                    + planeVector.vector1X3.y * planeVertex.vector1X3.y
                    + planeVector.vector1X3.z * planeVertex.vector1X3.z;
        }
    }

    public static class CustomDataType {

        public static class Coordinate3D {

            public static class Vector1X3
            {
                private float x, y, z;

                public Vector1X3() { x = y = z = 0; }
                public Vector1X3(float x, float y, float z)
                {
                    this.x = x;
                    this.y = y;
                    this.z = z;
                }
                public Vector1X3(Vector1X3 vector1X3)
                {
                    this.x = vector1X3.x;
                    this.y = vector1X3.y;
                    this.z = vector1X3.z;
                }
                public void Copy(float x, float y, float z)
                {
                    this.x = x;
                    this.y = y;
                    this.z = z;
                }
                public void Copy(Vector1X3 vector1X3)
                {
                    this.x = vector1X3.x;
                    this.y = vector1X3.y;
                    this.z = vector1X3.z;
                }
                public float Magnitude()
                {
                    return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
                }
                public void setX(float x)
                {
                    this.x = x;
                }
                public void setY(float y)
                {
                    this.y = y;
                }
                public void setZ(float z)
                {
                    this.z = z;
                }
                public float getX()
                {
                    return x;
                }
                public float getY()
                {
                    return y;
                }
                public float getZ()
                {
                    return z;
                }
            }
            public static class Vector1X4
            {
                private Vector1X3 vector1X3;
                private float q;

                public Vector1X4() {
                    vector1X3 = new Vector1X3();
                    q = 0;
                }
                public Vector1X4(float x, float y, float z, float q)
                {
                    vector1X3 = new Vector1X3(x, y, z);
                    try {
                        if (q != 0 || q != 1)
                            throw new Exception();
                        else
                            this.q = q;
                    } catch (Exception e) {
                        Log.v("Exception", "q Value is not right");
                    }
                }
                public Vector1X4(Vector1X3 vector1X3, float q)
                {
                    this.vector1X3 = new Vector1X3(vector1X3);
                    try {
                        if (q != 0 || q != 1)
                            throw new Exception();
                        else
                            this.q = q;
                    } catch (Exception e) {
                        Log.v("Exception", "q Value is not right");
                    }
                }
                public Vector1X4(Vector1X4 vector1X4)
                {
                    vector1X3 = new Vector1X3(vector1X4.vector1X3);
                    this.q = vector1X4.q;
                }
                public void Copy(float x, float y, float z, float q)
                {
                    vector1X3.Copy(x, y, z);
                    try {
                        if (q != 0 || q != 1)
                            throw new Exception();
                        else
                            this.q = q;
                    } catch (Exception e) {
                        Log.v("Exception", "q Value is not right");
                    }
                }
                public void Copy(Vector1X3 vector1X3, float q)
                {
                    this.vector1X3.Copy(vector1X3);
                    try {
                        if (q != 0 || q != 1)
                            throw new Exception();
                        else
                            this.q = q;
                    } catch (Exception e) {
                        Log.v("Exception", "q Value is not right");
                    }
                }
                public void Copy(Vector1X4 vector1X4)
                {
                    vector1X3.Copy(vector1X4.vector1X3);
                    this.q = vector1X4.q;
                }
                public float Magnitude()
                {
                    return vector1X3.Magnitude();
                }
                public void Normalize()
                {
                    float value = Magnitude();
                    try {
                        if (value == 0)
                            throw new Exception();
                        else
                        {
                            vector1X3.x = vector1X3.x / value;
                            vector1X3.y = vector1X3.y / value;
                            vector1X3.z = vector1X3.z / value;
                        }
                    } catch (Exception e) {
                        Log.v("Exception", "It can't be Normalize");
                    }
                }
                public void Rotate(Vector1X4 axis, float degree)
                {
                    Quaternion rotateQuaternion = new Quaternion(axis, degree);
                    rotateQuaternion.Normalize();

                    Quaternion thisQuaternion = new Quaternion(this);
                    //Quaternion tmpQuaternion = rotateQuaternion.Multiply(thisQuaternion.Multiply(rotateQuaternion.Reverse()));
                    Multiply(thisQuaternion, rotateQuaternion, thisQuaternion);
                    rotateQuaternion.Reverse();
                    Quaternion tmpQuaternion = new Quaternion();
                    Multiply(rotateQuaternion, thisQuaternion, tmpQuaternion);

                    vector1X3 = tmpQuaternion.unrealVector;

                    //new Vector1X4(tmpQuaternion.unrealVector, q);
                    //return rotateQuaternion.Transpose().Multiply(this.Multiply(rotateQuaternion));
                }
                public void setVector1X3(Vector1X3 vector1X3)
                {
                    this.vector1X3.Copy(vector1X3);
                }
                public void setQ(float q)
                {
                    this.q = q;
                }
                public Vector1X3 getVector1X3() { return vector1X3; }
                public float getQ()
                {
                    return q;
                }
                public boolean isPoint()
                {
                    return q == 1;
                }
            }
            public static class Matrix3X3
            {
                private Vector1X3 xVector, yVector, zVector;

                public Matrix3X3() {
                    xVector = new Vector1X3(1, 0, 0);
                    yVector = new Vector1X3(0, 1, 0);
                    zVector = new Vector1X3(0, 0, 1);
                }
                public Matrix3X3(float x1, float x2, float x3, float y1, float y2, float y3, float z1, float z2, float z3)
                {
                    xVector = new Vector1X3(x1, x2, x3);
                    yVector = new Vector1X3(y1, y2, y3);
                    zVector = new Vector1X3(z1, z2, z3);
                }
                public Matrix3X3(Vector1X3 xVector, Vector1X3 yVector, Vector1X3 zVector)
                {
                    this.xVector = new Vector1X3(xVector);
                    this.yVector = new Vector1X3(yVector);
                    this.zVector = new Vector1X3(zVector);
                }
                public Matrix3X3(Matrix3X3 matrix3X3)
                {
                    this.xVector = new Vector1X3(matrix3X3.xVector);
                    this.yVector = new Vector1X3(matrix3X3.yVector);
                    this.zVector = new Vector1X3(matrix3X3.zVector);
                }
                public void Copy(float x1, float x2, float x3, float y1, float y2, float y3, float z1, float z2, float z3)
                {
                    xVector.Copy(x1, x2, x3);
                    yVector.Copy(y1, y2, y3);
                    zVector.Copy(z1, z2, z3);
                }
                public void Copy(Vector1X3 xVector, Vector1X3 yVector, Vector1X3 zVector)
                {
                    this.xVector.Copy(xVector);
                    this.yVector.Copy(yVector);
                    this.zVector.Copy(zVector);
                }
                public void Copy(Matrix3X3 matrix3X3)
                {
                    this.xVector.Copy(matrix3X3.xVector);
                    this.yVector.Copy(matrix3X3.yVector);
                    this.zVector.Copy(matrix3X3.zVector);
                }
                public Vector1X3 getXVector()
                {
                    return xVector;
                }
                public Vector1X3 getYVector()
                {
                    return yVector;
                }
                public Vector1X3 getZVector()
                {
                    return zVector;
                }
                public void setXVector(Vector1X3 vector1X3)
                {
                    xVector.Copy(vector1X3);
                }
                public void setYVector(Vector1X3 vector1X3)
                {
                    yVector.Copy(vector1X3);
                }
                public void setZVector(Vector1X3 vector1X3)
                {
                    zVector.Copy(vector1X3);
                }
            }
            public static class Matrix4X4
            {
                private Vector1X4 xVector, yVector, zVector, qVector;

                public Matrix4X4() {
                    xVector = new Vector1X4(1, 0, 0, 0);
                    yVector = new Vector1X4(0, 1, 0, 0);
                    zVector = new Vector1X4(0, 0, 1, 0);
                    qVector = new Vector1X4(0, 0, 0, 1);
                }
                public Matrix4X4(float x1, float x2, float x3, float x4, float y1, float y2, float y3, float y4, float z1, float z2, float z3, float z4, float q1, float q2, float q3, float q4)
                {
                    xVector = new Vector1X4(x1, x2, x3, x4);
                    yVector = new Vector1X4(y1, y2, y3, y4);
                    zVector = new Vector1X4(z1, z2, z3, z4);
                    qVector = new Vector1X4(q1, q2, q3, q4);
                }
                public Matrix4X4(Vector1X4 xVector, Vector1X4 yVector, Vector1X4 zVector, Vector1X4 qVector)
                {
                    this.xVector = new Vector1X4(xVector);
                    this.yVector = new Vector1X4(yVector);
                    this.zVector = new Vector1X4(zVector);
                    this.qVector = new Vector1X4(qVector);
                }
                public Matrix4X4(Vector1X3 xVector, Vector1X3 yVector, Vector1X3 zVector)
                {
                    this.xVector = new Vector1X4(xVector, 0);
                    this.yVector = new Vector1X4(yVector, 0);
                    this.zVector = new Vector1X4(zVector, 0);
                    this.qVector = new Vector1X4(0, 0, 0, 1);
                }
                public Matrix4X4(Vector1X3 xVector, Vector1X3 yVector, Vector1X3 zVector, Vector1X3 qVector)
                {
                    this.xVector = new Vector1X4(xVector, 0);
                    this.yVector = new Vector1X4(yVector, 0);
                    this.zVector = new Vector1X4(zVector, 0);
                    this.qVector = new Vector1X4(qVector, 1);
                }
                public Matrix4X4(Matrix4X4 matrix4X4)
                {
                    this.xVector = new Vector1X4(matrix4X4.xVector);
                    this.yVector = new Vector1X4(matrix4X4.yVector);
                    this.zVector = new Vector1X4(matrix4X4.zVector);
                    this.qVector = new Vector1X4(matrix4X4.qVector);
                }
                public void Rotate(Vector1X4 axis, float degree)
                {
                    xVector.Rotate(axis, degree);
                    yVector.Rotate(axis, degree);
                    zVector.Rotate(axis, degree);

                    xVector.Normalize();
                    yVector.Normalize();
                    zVector.Normalize();
                }
                public void Rotate(Vector1X4 tQVector, Vector1X4 axis, int degree)
                {
                    Vector1X4 tXVector = axis;
                    tXVector.Normalize();
                    Vector1X4 direction = new Vector1X4();
                    Subtract(qVector, tQVector, direction);

                    Scaled(axis, Inner(axis, direction), axis);
                    Vector1X4 tempPoint = new Vector1X4();
                    Add(tQVector, axis, tempPoint);

                    Vector1X4 tZVector = new Vector1X4();
                    Subtract(qVector, tempPoint, tZVector);
                    tZVector.Normalize();
                    Vector1X4 tYVector = new Vector1X4();
                    Outer(tXVector, tZVector,tYVector);
                    tYVector.Normalize();

                    Matrix4X4 matrix4X4 = new Matrix4X4(tXVector, tYVector, tZVector, tQVector);
                    Matrix4X4 localMatrix = new Matrix4X4();
                    Inverse(matrix4X4, matrix4X4);
                    Multiply(this, matrix4X4, localMatrix);
                    matrix4X4.Rotate(axis, degree);
                    Matrix4X4 tempMatrix = new Matrix4X4();
                    Multiply(localMatrix, matrix4X4, tempMatrix);

                    xVector = tempMatrix.xVector;
                    yVector = tempMatrix.yVector;
                    zVector = tempMatrix.zVector;
                    qVector = tempMatrix.qVector;
                }
                public Vector1X4 getXVector()
                {
                    return xVector;
                }
                public Vector1X4 getYVector()
                {
                    return yVector;
                }
                public Vector1X4 getZVector()
                {
                    return zVector;
                }
                public Vector1X4 getQVector()
                {
                    return qVector;
                }
                public void setXVector(Vector1X4 vector1X4)
                {
                    xVector.Copy(vector1X4);
                }
                public void setYVector(Vector1X4 vector1X4)
                {
                    yVector.Copy(vector1X4);
                }
                public void setZVector(Vector1X4 vector1X4)
                {
                    zVector.Copy(vector1X4);
                }
                public void setQVector(Vector1X4 vector1X4)
                {
                    qVector.Copy(vector1X4);
                }
            }

            static class Quaternion
            {
                private Vector1X3 unrealVector;
                private float realValue;

                Quaternion()
                {
                    unrealVector = new Vector1X3();
                    realValue = 0;
                }
                Quaternion(float unRealX, float unRealY, float unRealZ, float realValue)
                {
                    unrealVector = new Vector1X3(unRealX, unRealY, unRealZ);
                    this.realValue = realValue;
                }
                Quaternion(Vector1X3 unrealVector)
                {
                    this.unrealVector = unrealVector;
                    realValue = 0;
                }
                Quaternion(Vector1X3 unrealVector, float realValue)
                {
                    this.unrealVector = unrealVector;
                    this.realValue = realValue;
                }
                Quaternion(Quaternion quaternion)
                {
                    unrealVector = quaternion.unrealVector;
                    realValue = quaternion.realValue;
                }
                Quaternion(Vector1X4 vector1X4)
                {
                    this.unrealVector = vector1X4.vector1X3;
                    realValue = 0;
                }
                Quaternion(Vector1X4 axis, float degree)
                {
                    float radian = (float) Math.toRadians(degree);
                    float x, y, z;

                    x = (float) (axis.vector1X3.x * Math.sin(radian / 2));
                    y = (float) (axis.vector1X3.y * Math.sin(radian / 2));
                    z = (float) (axis.vector1X3.z * Math.sin(radian / 2));

                    unrealVector = new Vector1X3(x, y, z);
                    realValue = (float) Math.cos(radian / 2);
                }
                void Conjugate()
                {
                    Inverse(unrealVector, unrealVector);
                }
                void Scale(float s)
                {
                    Scaled(unrealVector, s, unrealVector);
                    realValue = s * realValue;
                }
                float Magnitude()
                {
                    return (float) Math.sqrt(Math.pow(unrealVector.Magnitude(), 2) + Math.pow(realValue, 2));
                }
                void Reverse()
                {
                    float reverseScale = 1 / Magnitude();
                    Conjugate();
                    Scale((float) Math.pow(reverseScale, 2));
                }
                void Normalize()
                {
                    float magnitude = Magnitude();

                    Scaled(unrealVector, magnitude, unrealVector);
                    realValue /= magnitude;
                }
                void setUnrealVector(Vector1X3 unrealVector)
                {
                    this.unrealVector.Copy(unrealVector);
                }
                void setRealValue(float realValue)
                {
                    this.realValue = realValue;
                }
                Vector1X3 getUnrealVector()
                {
                    return unrealVector;
                }
                float getRealValue()
                {
                    return realValue;
                }
            }
        }
    }
}
