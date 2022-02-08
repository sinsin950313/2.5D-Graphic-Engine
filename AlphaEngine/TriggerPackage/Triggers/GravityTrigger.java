package seaweed.aeproject.AlphaEngine.TriggerPackage.Triggers;

import java.util.Iterator;
import java.util.Vector;

import seaweed.aeproject.AlphaEngine.PhysicsPackage.ContactData;
import seaweed.aeproject.AlphaEngine.PhysicsPackage.ForceGenerator.SteadyForceGeneratorPackage.GravityGenerator;
import seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineComponent.PhysicsSystem;
import seaweed.aeproject.AlphaEngine.TriggerPackage.Trigger;
import seaweed.aeproject.AlphaEngine.VolumePackage.Volume;

import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomDataType.Coordinate3D.*;
import static seaweed.aeproject.AlphaEngine.ETC.CustomMath.*;

import static seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineActivity.vGameObjectComponent;

public class GravityTrigger extends Trigger {

    private GravityGenerator gravityGenerator;
    private Vector1X4 collideVector;

    public GravityTrigger(GravityGenerator gravityGenerator) {
        super();
        this.gravityGenerator = gravityGenerator;
        collideVector = new Vector1X4();
    }

    @Override
    public boolean CheckTrigger() {
        Vector<ContactData> contactData = PhysicsSystem.getInstance().CheckCollide(gravityGenerator.getGameObject(), false);

        ContactData tempContactData;
        Volume volume;
        collideVector.Copy(0, 0, 0, 0);
        Iterator<ContactData> collisionDataIterator = contactData.iterator();
        while (collisionDataIterator.hasNext())
        {
            tempContactData = collisionDataIterator.next();
            volume = (Volume) tempContactData.getSubGameObject().getComponent(vGameObjectComponent, null);
            if (volume.isRigidBody())
                CustomCalculate.Add(collideVector, tempContactData.getCollidePlaneVector(), collideVector);
        }
        collideVector.Normalize();

        if (CustomCalculate.Inner(collideVector, new Vector1X4(0, 1, 0, 0)) == 1)
            gravityGenerator.setContactWithGround(true);
        else
            gravityGenerator.setContactWithGround(false);

        if (gravityGenerator.isGenerate())
        {
            if (gravityGenerator.isContactWithGround())
                return true;
        }
        else
        {
            if (!gravityGenerator.isContactWithGround())
                return true;
        }

        return false;
    }

    @Override
    public void Action() {
        if (gravityGenerator.isGenerate())
            gravityGenerator.setGenerate(false);
        else
            gravityGenerator.setGenerate(true);
    }

    @Override
    protected boolean CheckPause() {
        return false;
    }

    @Override
    protected boolean CheckRestart() {
        return false;
    }

    @Override
    protected boolean CheckRemove() {
        return false;
    }
}
