package seaweed.aeproject.AlphaEngine.PhysicsPackage.ForceGenerator.TemporaryForcePackage;

import java.util.Iterator;
import java.util.Vector;

import seaweed.aeproject.AlphaEngine.ETC.CustomMath;
import seaweed.aeproject.AlphaEngine.PhysicsPackage.ForceGenerator.TemporaryForceGenerator;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.PhysicsPackage.Material;
import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.PhysicsSystem;
import seaweed.aeproject.AlphaEngine.PhysicsPackage.ContactData;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObjectPackage.GameObjectComponentPackage.Volume;

import static seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineActivity.mGameObjectComponent;
import static seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineActivity.vGameObjectComponent;

public class CollisionForceGenerator extends TemporaryForceGenerator {

    private Vector<ContactData> contactData;
    private boolean cheap;

    public CollisionForceGenerator(Vector<ContactData> contactData, boolean cheap) {
        super(contactData.get(0).getMainGameObject(), false);
        this.contactData = contactData;
        this.cheap = cheap;
    }
    private void EnergyTransfer(ContactData contactData, Material mainMaterial, Material subMaterial)
    {
        if (cheap)
        {
            GameObject main = contactData.getMainGameObject();
            GameObject sub = contactData.getSubGameObject();

            CustomMath.Vector1X4 collidePlaneVector = contactData.getCollidePlaneVector();

            CustomMath.Vector1X4 mainImpulseVector = mainMaterial.getImpulse();
            CustomMath.Vector1X4 subImpulseVector = subMaterial.getImpulse();
            mainImpulseVector.Normalize();
            subImpulseVector.Normalize();
            float cosine = mainImpulseVector.Inner(subImpulseVector);

            CustomMath.Vector1X4 impulse;
            CustomMath.Vector1X4 reflect;
            CustomMath.Vector1X4 directionVector;

            if (mainImpulseVector.Magnitude() != 0 && subImpulseVector.Magnitude() != 0)
            {
                if (-1 < cosine && cosine < 0) {
                    directionVector = main.getObjectMatrix().qVector().Subtract(sub.getObjectMatrix().qVector());
                    directionVector.Normalize();
                    if (collidePlaneVector.Inner(directionVector) < 0)
                        collidePlaneVector.Copy(collidePlaneVector.Inverse());

                    impulse = mainMaterial.getImpulse();
                    reflect = collidePlaneVector.Scaled(collidePlaneVector.Inner(impulse)).Inverse();
                    mainMaterial.AddForce(true, main.getObjectMatrix().qVector(), reflect);
                    subMaterial.AddForce(true, sub.getObjectMatrix().qVector(), reflect.Inverse().Scaled(subMaterial.getRestitutionCoefficient()));

                    directionVector = sub.getObjectMatrix().qVector().Subtract(main.getObjectMatrix().qVector());
                    directionVector.Normalize();
                    if (collidePlaneVector.Inner(directionVector) < 0)
                        collidePlaneVector.Copy(collidePlaneVector.Inverse());

                    impulse = subMaterial.getImpulse();
                    reflect = collidePlaneVector.Scaled(collidePlaneVector.Inner(impulse)).Inverse();
                    mainMaterial.AddForce(true, main.getObjectMatrix().qVector(), reflect.Inverse().Scaled(mainMaterial.getRestitutionCoefficient()));
                    subMaterial.AddForce(true, sub.getObjectMatrix().qVector(), reflect);
                }
                else {
                    CustomMath.Vector1X4 tempVector = main.getObjectMatrix().qVector().Subtract(sub.getObjectMatrix().qVector());
                    tempVector.Normalize();

                    cosine = mainImpulseVector.Inner(tempVector);
                    if (0 < cosine) {
                        directionVector = sub.getObjectMatrix().qVector().Subtract(main.getObjectMatrix().qVector());
                        directionVector.Normalize();
                        if (collidePlaneVector.Inner(directionVector) < 0)
                            collidePlaneVector.Copy(collidePlaneVector.Inverse());

                        impulse = subMaterial.getImpulse();
                        reflect = collidePlaneVector.Scaled(collidePlaneVector.Inner(impulse)).Inverse();
                        mainMaterial.AddForce(true, main.getObjectMatrix().qVector(), reflect.Inverse().Scaled(mainMaterial.getRestitutionCoefficient()));
                        subMaterial.AddForce(true, sub.getObjectMatrix().qVector(), reflect);
                    } else {
                        directionVector = main.getObjectMatrix().qVector().Subtract(sub.getObjectMatrix().qVector());
                        directionVector.Normalize();
                        if (collidePlaneVector.Inner(directionVector) < 0)
                            collidePlaneVector.Copy(collidePlaneVector.Inverse());

                        impulse = mainMaterial.getImpulse();
                        reflect = collidePlaneVector.Scaled(collidePlaneVector.Inner(impulse)).Inverse();
                        mainMaterial.AddForce(true, main.getObjectMatrix().qVector(), reflect);
                        subMaterial.AddForce(true, sub.getObjectMatrix().qVector(), reflect.Inverse().Scaled(subMaterial.getRestitutionCoefficient()));
                    }
                }
            }
            else
            {
                if (mainImpulseVector.Magnitude() != 0)
                {
                    directionVector = main.getObjectMatrix().qVector().Subtract(sub.getObjectMatrix().qVector());
                    directionVector.Normalize();
                    if (collidePlaneVector.Inner(directionVector) < 0)
                        collidePlaneVector.Copy(collidePlaneVector.Inverse());

                    impulse = mainMaterial.getImpulse();
                    reflect = collidePlaneVector.Scaled(collidePlaneVector.Inner(impulse)).Inverse();
                    mainMaterial.AddForce(true, main.getObjectMatrix().qVector(), reflect);
                    subMaterial.AddForce(true, sub.getObjectMatrix().qVector(), reflect.Inverse().Scaled(subMaterial.getRestitutionCoefficient()));
                }
                else
                {
                    directionVector = sub.getObjectMatrix().qVector().Subtract(main.getObjectMatrix().qVector());
                    directionVector.Normalize();
                    if (collidePlaneVector.Inner(directionVector) < 0)
                        collidePlaneVector.Copy(collidePlaneVector.Inverse());

                    impulse = subMaterial.getImpulse();
                    reflect = collidePlaneVector.Scaled(collidePlaneVector.Inner(impulse)).Inverse();
                    mainMaterial.AddForce(true, main.getObjectMatrix().qVector(), reflect.Inverse().Scaled(mainMaterial.getRestitutionCoefficient()));
                    subMaterial.AddForce(true, sub.getObjectMatrix().qVector(), reflect);
                }
            }
        }
        else
        {
            int count = contactData.getVertexes().size();
            CustomMath.Vector1X4 collidePoint = new CustomMath.Vector1X4();
            CustomMath.Vector1X4[] collidePoints = new CustomMath.Vector1X4[contactData.getVertexes().size()];
            CustomMath.Vector1X4 collidePlaneVector = contactData.getCollidePlaneVector();

            CustomMath.Vector1X4 impulse;
            CustomMath.Vector1X4 reflect;
            CustomMath.Vector1X4 impulseVector = new CustomMath.Vector1X4();

            impulse = mainMaterial.getImpulse();
            impulseVector.Copy(impulse);
            impulseVector.Normalize();
            if (0 < impulseVector.Inner(collidePlaneVector))
                collidePlaneVector.Copy(collidePlaneVector.Inverse());
            reflect = collidePlaneVector.Scaled(collidePlaneVector.Inner(impulse)).Inverse();
            reflect.Copy(reflect.Scaled(1f / count));
            int i = 0;
            while (i < count) {
                collidePoint.Copy(contactData.getVertexes().get(i)[2]);
                collidePoints[i] = contactData.getVertexes().get(i)[2];
                subMaterial.AddForce(false, collidePoint, reflect.Inverse().Scaled(subMaterial.getRestitutionCoefficient()));
                i++;
            }

            reflect.Copy(reflect.Scaled(count));
            mainMaterial.ContactProcess(collidePoints, collidePlaneVector, reflect);

            impulse = subMaterial.getImpulse();
            impulseVector.Copy(impulse);
            impulseVector.Normalize();
            if (0 < impulseVector.Inner(collidePlaneVector))
                collidePlaneVector.Copy(collidePlaneVector.Inverse());
            reflect = collidePlaneVector.Scaled(collidePlaneVector.Inner(impulse)).Inverse();
            reflect.Copy(reflect.Scaled(1f / count));
            i = 0;
            while (i < count) {
                collidePoint.Copy(contactData.getVertexes().get(i)[2]);
                collidePoints[i] = contactData.getVertexes().get(i)[2];
                mainMaterial.AddForce(false, collidePoint, reflect.Inverse().Scaled(mainMaterial.getRestitutionCoefficient()));
                i++;
            }

            reflect.Copy(reflect.Scaled(count));
            subMaterial.ContactProcess(collidePoints, collidePlaneVector, reflect);
        }
    }
    private void ElasticityRevision()
    {

    }
    private void ContactRevision(ContactData contactData, Material mainMaterial, Material subMaterial)
    {
        GameObject main = contactData.getMainGameObject();
        GameObject sub = contactData.getSubGameObject();

        float massRatio;
        if (mainMaterial.getMass() == Integer.MAX_VALUE && subMaterial.getMass() == Integer.MAX_VALUE)
            massRatio = 0.5f;
        else if (subMaterial.getMass() == Integer.MAX_VALUE)
            massRatio = 1f / mainMaterial.getInertiaMoment();
        else if (mainMaterial.getMass() == Integer.MAX_VALUE)
            massRatio = 1f / subMaterial.getInertiaMoment();
        else
            massRatio = 1f / (mainMaterial.getInertiaMoment() + subMaterial.getInertiaMoment());

        int count = contactData.getVertexes().size();
        if (count > 2)
        {
            CustomMath.Vector1X4[] collidePoints = new CustomMath.Vector1X4[count];
            CustomMath.Vector1X4 collidePlaneVector = contactData.getCollidePlaneVector();
            int i, j;
            i = 0;
            while (i < count)
            {
                collidePoints[i] = contactData.getVertexes().get(i)[2];
                i++;
            }
            CustomMath.Vector1X4[] tCollidePoints = new CustomMath.Vector1X4[count];
            CustomMath.Vector1X4 tempVector, tempVector_, tempVector__;

            tCollidePoints[0] = collidePoints[0];

            i = 0;
            while (i < count)
            {
                if (i == 0) {
                    tempVector = collidePoints[1].Subtract(tCollidePoints[i]);
                    tempVector.Normalize();
                    tCollidePoints[i + 1] = collidePoints[1];
                }
                else
                {
                    if (i + 1 == count)
                        break;
                    else
                    {
                        tempVector = collidePoints[0].Subtract(tCollidePoints[i]);
                        tempVector.Normalize();
                        tCollidePoints[i + 1] = collidePoints[0];
                    }
                }

                j = 0;
                while (j < count)
                {
                    if (i == 0 && j == 0)
                        j = 2;
                    else if (j == 0)
                        j = 1;

                    tempVector_ = collidePoints[j].Subtract(tCollidePoints[i]);
                    tempVector_.Normalize();

                    tempVector__ = tempVector_.Outer(tempVector);
                    tempVector__.Normalize();

                    float value = collidePlaneVector.Inner(tempVector__);
                    if (0 < value) {
                        tCollidePoints[i + 1] = collidePoints[j];
                        tempVector = tempVector_;
                    }

                    j++;
                }
                i++;
            }

            if (!CustomMath.isPointInPlane(contactData.getMainGameObject().getObjectMatrix().qVector(), collidePlaneVector, tCollidePoints)) {
                CustomMath.Vector1X4 collidePoint;
                float collideDepth;

                CustomMath.Vector1X4 centerVector;
                CustomMath.Vector1X4 projectionVector;
                float tempDepth;
                float radius;

                CustomMath.Vector1X4 axis;
                float degree;

                i = 0;
                while (i < count)
                {
                    collidePoint = contactData.getVertexes().get(i)[2];
                    collideDepth = contactData.getPenetrationDepth().get(i);
                    collidePlaneVector.Copy(contactData.getCollidePlaneVector());

                    if (mainMaterial.getMass() != Integer.MAX_VALUE)
                    {
                        if (subMaterial.getMass() != Integer.MAX_VALUE)
                        {
                            tempDepth = collideDepth * subMaterial.getMass() * massRatio;
                            centerVector = main.getObjectMatrix().qVector().Subtract(collidePoint);
                            radius = centerVector.Magnitude();
                            centerVector.Normalize();
                            if (!(Math.abs(Math.acos(centerVector.Inner(collidePlaneVector))) < Math.PI / 2))
                                collidePlaneVector = collidePlaneVector.Inverse();
                            tempVector = collidePlaneVector.Scaled(collidePlaneVector.Inner(centerVector));
                            projectionVector = centerVector.Subtract(tempVector);
                            axis = collidePlaneVector.Outer(projectionVector);
                            degree = (float) Math.toDegrees(2 * Math.asin(tempDepth / (2 * radius)));

                            main.Rotate(axis, degree);
                        }
                        else
                        {
                            tempDepth = collideDepth;
                            centerVector = main.getObjectMatrix().qVector().Subtract(collidePoint);
                            radius = centerVector.Magnitude();
                            centerVector.Normalize();
                            if (!(Math.abs(Math.acos(centerVector.Inner(collidePlaneVector))) < Math.PI / 2))
                                collidePlaneVector = collidePlaneVector.Inverse();
                            tempVector = collidePlaneVector.Scaled(collidePlaneVector.Inner(centerVector));
                            projectionVector = centerVector.Subtract(tempVector);
                            axis = collidePlaneVector.Outer(projectionVector);
                            degree = (float) Math.toDegrees(2 * Math.asin(tempDepth / (2 * radius)));

                            main.Rotate(axis, degree);
                        }
                    }
                    i++;
                }
            }
            if (!CustomMath.isPointInPlane(contactData.getSubGameObject().getObjectMatrix().qVector(), collidePlaneVector, tCollidePoints)) {
                CustomMath.Vector1X4 collidePoint;
                float collideDepth;

                CustomMath.Vector1X4 centerVector;
                CustomMath.Vector1X4 projectionVector;
                float tempDepth;
                float radius;

                CustomMath.Vector1X4 axis;
                float degree;

                i = 0;
                while (i < count)
                {
                    collidePoint = contactData.getVertexes().get(i)[2];
                    collideDepth = contactData.getPenetrationDepth().get(i);
                    collidePlaneVector.Copy(contactData.getCollidePlaneVector());

                    if (subMaterial.getMass() != Integer.MAX_VALUE)
                    {
                        if (mainMaterial.getMass() != Integer.MAX_VALUE)
                        {
                            tempDepth = collideDepth * mainMaterial.getMass() * massRatio;
                            centerVector = sub.getObjectMatrix().qVector().Subtract(collidePoint);
                            radius = centerVector.Magnitude();
                            centerVector.Normalize();
                            if (!(Math.abs(Math.acos(centerVector.Inner(collidePlaneVector))) < Math.PI / 2))
                                collidePlaneVector = collidePlaneVector.Inverse();
                            tempVector = collidePlaneVector.Scaled(collidePlaneVector.Inner(centerVector));
                            projectionVector = centerVector.Subtract(tempVector);
                            axis = collidePlaneVector.Outer(projectionVector);
                            degree = (float) Math.toDegrees(2 * Math.asin(tempDepth / (2 * radius)));

                            sub.Rotate(axis, degree);
                        }
                        else
                        {
                            tempDepth = collideDepth;
                            centerVector = sub.getObjectMatrix().qVector().Subtract(collidePoint);
                            radius = centerVector.Magnitude();
                            centerVector.Normalize();
                            if (!(Math.abs(Math.acos(centerVector.Inner(collidePlaneVector))) < Math.PI / 2))
                                collidePlaneVector = collidePlaneVector.Inverse();
                            tempVector = collidePlaneVector.Scaled(collidePlaneVector.Inner(centerVector));
                            projectionVector = centerVector.Subtract(tempVector);
                            axis = collidePlaneVector.Outer(projectionVector);
                            degree = (float) Math.toDegrees(2 * Math.asin(tempDepth / (2 * radius)));

                            sub.Rotate(axis, degree);
                        }
                    }
                    i++;
                }
            }
        }
    }
    private void RigidBodyRevision(ContactData contactData, Material mainMaterial, Material subMaterial)
    {
        GameObject main = contactData.getMainGameObject();
        GameObject sub = contactData.getSubGameObject();

        float massRatio;
        if (mainMaterial.getMass() == Integer.MAX_VALUE && subMaterial.getMass() == Integer.MAX_VALUE)
            massRatio = 0.5f;
        else if (subMaterial.getMass() == Integer.MAX_VALUE)
            massRatio = 1f / mainMaterial.getMass();
        else if (mainMaterial.getMass() == Integer.MAX_VALUE)
            massRatio = 1f / subMaterial.getMass();
        else
            massRatio = 1f / (mainMaterial.getMass() + subMaterial.getMass());

        int count = contactData.getVertexes().size();
        CustomMath.Vector1X4 standardVector = contactData.getCollidePlaneVector();

        float collideLength = 0f;

        int i = 0;
        while (i < count)
        {
            if (collideLength < contactData.getPenetrationDepth().get(i))
                collideLength = contactData.getPenetrationDepth().get(i);
            i++;
        }

        if (mainMaterial.getMass() != Integer.MAX_VALUE) {
            if (subMaterial.getMass() != Integer.MAX_VALUE) {
                standardVector = standardVector.Scaled(standardVector.Inner(main.getObjectMatrix().qVector().Subtract(sub.getObjectMatrix().qVector())));
                standardVector.Normalize();
                main.setPosition(main.getObjectMatrix().qVector().Add(standardVector.Scaled(collideLength * subMaterial.getMass() * massRatio)));

                standardVector = standardVector.Scaled(standardVector.Inner(sub.getObjectMatrix().qVector().Subtract(main.getObjectMatrix().qVector())));
                standardVector.Normalize();
                sub.setPosition(sub.getObjectMatrix().qVector().Add(standardVector.Scaled(collideLength * mainMaterial.getMass() * massRatio)));
            } else {
                standardVector = standardVector.Scaled(standardVector.Inner(main.getObjectMatrix().qVector().Subtract(sub.getObjectMatrix().qVector())));
                standardVector.Normalize();
                main.setPosition(main.getObjectMatrix().qVector().Add(standardVector.Scaled(collideLength)));
            }
        } else {
            if (subMaterial.getMass() != Integer.MAX_VALUE) {
                standardVector = standardVector.Scaled(standardVector.Inner(sub.getObjectMatrix().qVector().Subtract(main.getObjectMatrix().qVector())));
                standardVector.Normalize();
                sub.setPosition(sub.getObjectMatrix().qVector().Add(standardVector.Scaled(collideLength)));
            }
        }
    }

    @Override
    public void Apply() {
        Material mainMaterial = (Material) gameObject.getComponent(mGameObjectComponent);

        Material subMaterial;
        Iterator<ContactData> collisionDataIterator = this.contactData.iterator();
        ContactData contactData;
        Volume collideVolume;

        while (collisionDataIterator.hasNext()) {
            contactData = collisionDataIterator.next();
            subMaterial = (Material) contactData.getSubGameObject().getComponent(mGameObjectComponent);
            collideVolume = (Volume) contactData.getSubGameObject().getComponent(vGameObjectComponent);
            int count = contactData.getVertexes().size();

            if (collideVolume.isRigidBody() && count != 0)
            {
                PhysicsSystem.getInstance().AddGameObjectToCalculator(contactData.getSubGameObject());

                cheap = true;

                EnergyTransfer(contactData, mainMaterial, subMaterial);
                if (!cheap) {
                    ContactRevision(contactData, mainMaterial, subMaterial);
                    //ElasticityRevision();
                }
                RigidBodyRevision(contactData, mainMaterial, subMaterial);
            }
        }
    }
}
