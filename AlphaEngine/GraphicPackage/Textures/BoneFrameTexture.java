package seaweed.aeproject.AlphaEngine.GraphicPackage.Textures;

import seaweed.aeproject.AlphaEngine.ETC.CustomMath.CustomDataType.*;
import seaweed.aeproject.AlphaEngine.GameObjectPackage.GameObject;
import seaweed.aeproject.AlphaEngine.GraphicPackage.PainterTexture;

public class BoneFrameTexture extends PainterTexture {

    private Coordinate3D.Vector1X4[] vertexes;
    private int[][] edges;

    public BoneFrameTexture(GameObject ownerGameObject, int color) {
        super(ownerGameObject, color);
    }
}
