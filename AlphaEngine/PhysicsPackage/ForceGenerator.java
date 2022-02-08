package seaweed.aeproject.AlphaEngine.PhysicsPackage;

import seaweed.aeproject.AlphaEngine.GameObjectPackage.*;

public abstract class ForceGenerator implements GameObjectComponentInterface//add force value
{
    protected GameObject gameObject;
    protected boolean steady;

    private ForceGenerator beforeForceGenerator;
    private ForceGenerator afterForceGenerator;

    private boolean generate;

    public ForceGenerator(GameObject gameObject, boolean steady)
    {
        this.gameObject = gameObject;
        this.steady = steady;

        beforeForceGenerator = null;
        afterForceGenerator = null;

        generate = true;
    }
    public GameObject getGameObject()
    {
        return gameObject;
    }
    public boolean isSteady()
    {
        return steady;
    }
    public ForceGenerator getBeforeForceGenerator() {
        return beforeForceGenerator;
    }
    public void setBeforeForceGenerator(ForceGenerator beforeForceGenerator) {
        this.beforeForceGenerator = beforeForceGenerator;
    }
    public ForceGenerator getAfterForceGenerator() {
        return afterForceGenerator;
    }
    public void setAfterForceGenerator(ForceGenerator afterForceGenerator) {
        this.afterForceGenerator = afterForceGenerator;
    }
    public boolean isGenerate() {
        return generate;
    }
    public void setGenerate(boolean generate) {
        this.generate = generate;
    }

    abstract public void Apply();
}