package net.badlogic.td.game.objects.crip;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by BruSD on 26.03.2014.
 */
public abstract class AbstractCrip  {

    public Vector2 position;
    public Vector2 dimension;
    public Vector2 origin;
    public Vector2 scale;

    public Rectangle bounds;
    public float rotation;

    //crip params
    public int cripHealth;
    public int cripProfit;

    public Vector2 velocity;

    public Body body;
    public float stateTime;
    public Animation animation;

    // road
    public ArrayList<HashMap<String, Float>> roadList;
    public Vector2 pointStart;
    public Vector2 pointFinish;
    public int stepCount;
    public float startTime;


    public enum VIEW_DIRECTION {
        LEFT, RIGHT, UP, DOWN
    }

    public VIEW_DIRECTION viewDirection;

    public AbstractCrip() {
        position = new Vector2();
        dimension = new Vector2(1, 1);
        origin = new Vector2();
        scale = new Vector2(0.5f, 0.5f);
        rotation = 0;

        bounds = new Rectangle();

        cripHealth = 0;
        cripProfit = 0;

        pointStart =  new Vector2();
        pointFinish = new Vector2();
        stepCount = 0;
        startTime = 0;
    }

    public void update (float deltaTime) {
        stateTime += deltaTime;

        updateMoving();
        if (stateTime >= startTime) {
            moveToPoint(deltaTime);
        }




    }

    public abstract void render (SpriteBatch batch);


    public void setAnimation (Animation animation) {
        this.animation = animation;
        stateTime = 0;
    }

    protected void updateMoving () {
        if (pointStart.x == pointFinish.x){
            if (pointStart.y > pointFinish.y){
                viewDirection = VIEW_DIRECTION.DOWN; //Go Down
            }else if(pointStart.y < pointFinish.y) {
                viewDirection = VIEW_DIRECTION.UP; // Go Up
            }

        }else if(pointStart.y == pointFinish.y){
            if (pointStart.x > pointFinish.x){
                viewDirection = VIEW_DIRECTION.LEFT; //Go Left
            }else if(pointStart.y < pointFinish.y) {
                viewDirection = VIEW_DIRECTION.RIGHT; // Go Right
            }
        }

    }
    public void moveToPoint(float deltaTime){

        switch (viewDirection) {
            case UP:
                if (position.y < pointFinish.y) {
                    rotation = 0;
                    position.y += velocity.y * deltaTime;
                }else {
                    setNextMovePoints();
                }
                break;
            case DOWN:
                if (position.y > pointFinish.y) {
                    rotation = 180;
                    position.y -= velocity.y * deltaTime;
                }else {
                    setNextMovePoints();
                }
                break;
            case LEFT:
                if (position.x > pointFinish.x) {
                    rotation = 90;
                    position.x -= velocity.x * deltaTime;
                }else {
                    setNextMovePoints();
                }
                break;
            case RIGHT:
                if (position.x < pointFinish.x) {
                    rotation = 270;
                    position.x += velocity.x * deltaTime;
                }else {
                    setNextMovePoints();
                }
                break;
        }
    }
    public void setNextMovePoints(){
        if(stepCount < roadList.size()-2){
            stepCount += 1;
            pointStart = new Vector2(roadList.get(stepCount).get("x"), roadList.get(stepCount).get("y"));
            pointFinish= new Vector2(roadList.get(stepCount + 1).get("x"), roadList.get(stepCount + 1).get("y"));
        }

    }

}
