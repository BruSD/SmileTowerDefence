package net.badlogic.td.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.badlogic.td.game.Assets;

/**
 * Created by BruSD on 24.03.2014.
 */
public class Road extends AbstractGameObject {

    private TextureRegion regPlate;
    public boolean isEmpty;


    public Road () {
        init();
    }
    private void init () {

        dimension.set(1f, 1f);
        regPlate = Assets.instance.plateRoad.plateRoad;
// Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);

        isEmpty = true;
    }
    @Override
    public void render(SpriteBatch batch) {

        TextureRegion reg = null;
        reg = regPlate;
        batch.draw(reg.getTexture(),
                position.x, position.y,
                origin.x, origin.y,
                dimension.x, dimension.y,
                scale.x, scale.y,
                rotation,
                reg.getRegionX(), reg.getRegionY(),
                reg.getRegionWidth(), reg.getRegionHeight(),
                false, false);
    }
}
