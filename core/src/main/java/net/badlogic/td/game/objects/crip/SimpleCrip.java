package net.badlogic.td.game.objects.crip;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import net.badlogic.td.game.Assets;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by BruSD on 26.03.2014.
 */
public class SimpleCrip extends AbstractCrip {

    private TextureRegion regSimpleCrip;





    public SimpleCrip (ArrayList<HashMap<String, Float>> _roadList, float _startTime) {
        roadList = _roadList;
        startTime = _startTime;
        init();
    }

    private void init() {
        dimension.set(1.0f, 1.0f);
        regSimpleCrip = Assets.instance.assetCrip.simpleCrip;

        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        origin.set(dimension.x / 2, dimension.y / 2);

        cripHealth = 100;
        cripProfit = 5;

        velocity =  new Vector2(0.4f, 0.4f);


        pointStart = new Vector2(roadList.get(0).get("x"), roadList.get(0).get("y"));
        pointFinish= new Vector2(roadList.get(1).get("x"), roadList.get(1).get("y"));
        position.x = pointStart.x;
        position.y = pointStart.y;

    }

    @Override
    public void render(SpriteBatch batch) {

        TextureRegion reg = null;
        reg = regSimpleCrip;
        if (stateTime >= startTime) {
            batch.draw(reg.getTexture(), position.x,
                    position.y, origin.x, origin.y,
                    dimension.x,
                    dimension.y,
                    scale.x, scale.y,
                    rotation,
                    reg.getRegionX(), reg.getRegionY(),
                    reg.getRegionWidth(), reg.getRegionHeight(),
                    false, false);
        }
    }

    public void update(float deltaTime){
        super.update(deltaTime);

    }





}
