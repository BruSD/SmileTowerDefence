package net.badlogic.td.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import net.badlogic.td.game.objects.Plate;
import net.badlogic.td.game.objects.crip.AbstractCrip;
import net.badlogic.td.game.objects.crip.SimpleCrip;
import net.badlogic.td.screens.DirectedGame;
import net.badlogic.td.screens.MenuScreen;
import net.badlogic.td.screens.transitions.ScreenTransition;
import net.badlogic.td.screens.transitions.ScreenTransitionSlide;
import net.badlogic.td.util.CameraHelper;
import net.badlogic.td.util.TDGestureListener;


/**
 * Created by BruSD on 16.03.14.
 */
public class WorldController extends InputAdapter implements Disposable{


    private static final String TAG = WorldController.class.getName();
    public CameraHelper cameraHelper;
    public boolean isPlateOpenForPutTower = true;
    private DirectedGame game;

    public Level level;
    public int score;

    public float scoreVisual;

    private String levelID = "levels/level-1.tmx";

    public World b2world;

    /** a hit body **/
    protected Body hitBody = null;
    /** our mouse joint **/
    protected MouseJoint mouseJoint = null;

    /** ground body to connect the mouse joint to **/
    protected Body groundBody;



    private InputTDGestureListener tdGestureListener;

    public WorldController (DirectedGame game) {
        this.game = game;
        init();
    }

    //region LifeCycle
    private void init() {
        cameraHelper = new CameraHelper();
        initLevel();
    }

    private void initLevel () {
        score = 0;
        scoreVisual = score;

        level = new Level(levelID);


        initPhysics();
    }
    public void update (float deltaTime) {
        handleDebugInput(deltaTime);
        handleGesteInput();

        b2world.step(deltaTime, 6, 2);

        level.update(deltaTime);

    }

    @Override
    public void dispose() {
        mouseJoint = null;
    }

    //endregion

    public boolean isGameOver () {
        return false;
    }


    private void backToMenu () {
        // switch to menu screen
        ScreenTransition transition = ScreenTransitionSlide.init(0.75f,
                ScreenTransitionSlide.DOWN, false, Interpolation.bounceOut);
        game.setScreen(new MenuScreen(game), transition);
    }
    //region HKeyInput
    @Override
    public boolean keyUp (int keycode) {
        // Reset game world
        if (keycode == Input.Keys.R) {
            init();
            Gdx.app.debug(TAG, "Game world resetted");
        }
        // Toggle camera follow
        else if (keycode == Input.Keys.ENTER) {
//            cameraHelper.setTarget(cameraHelper.hasTarget() ? null: level.bunnyHead);
//            Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
        }
        // Back to Menu
        else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            backToMenu();
        }
        return false;
    }

    private void handleGesteInput () {
        tdGestureListener = new InputTDGestureListener();

        Gdx.input.setInputProcessor(new GestureDetector(tdGestureListener));

    }

    private void handleDebugInput (float deltaTime) {
        // Camera Controls (move)

        float camMoveSpeed = 1 ;
        float camMoveSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) camMoveSpeed *=
                camMoveSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveCamera(-camMoveSpeed,
                0);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveCamera(camMoveSpeed,
                0);
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) moveCamera(0, camMoveSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) moveCamera(0,
                -camMoveSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE))
            cameraHelper.setPosition(0, 0);

        // Camera Controls (zoom)
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) camZoomSpeed *=
                camZoomSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Input.Keys.COMMA))
            cameraHelper.addZoom(camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) cameraHelper.addZoom(
                -camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.SLASH)) cameraHelper.setZoom(1);


        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE )||Gdx.input.isKeyPressed(Input.Keys.BACK )){
            backToMenu();
        }


    }



    //endregion

    private void moveCamera (float x, float y) {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);

        Gdx.app.debug(TAG, "Camera position: " + x +" " + y);

    }

    private void initPhysics () {
        initWirld();


    }
    public void initWirld(){
        if (b2world != null) b2world.dispose();
        b2world = new World(new Vector2(0, 0), true);

        BodyDef bodyDef;
        // we also need an invisible zero size ground body
        // to which we can connect the mouse joint
        bodyDef = new BodyDef();
        groundBody = b2world.createBody(bodyDef);
        // Plate
        Vector2 origin = new Vector2();
        for (Plate plate : level.plates) {
            bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(plate.position);
            Body body = b2world.createBody(bodyDef);
            plate.body = body;

            PolygonShape polygonShape = new PolygonShape();
            origin.x = plate.bounds.width / 2.0f;
            origin.y = plate.bounds.height / 2.0f;
            polygonShape.setAsBox(plate.bounds.width / 2.0f,
                    plate.bounds.height / 2.0f, origin, 0);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
        }
    }

//region GDetector
    /** we instantiate this vector and the callback here so we don't irritate the GC **/
    Vector3 testPoint = new Vector3();

    QueryCallback callback = new QueryCallback() {
        @Override
        public boolean reportFixture (Fixture fixture) {
            // if the hit point is inside the fixture of the body
            // we report it
            if (fixture.testPoint(testPoint.x, testPoint.y)) {
                hitBody = fixture.getBody();
                return false;
            } else
                return true;
        }
    };


    private  class InputTDGestureListener extends TDGestureListener{

        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            if (!isPlateOpenForPutTower) {
                return false;
            }else {
                Camera camera = CameraHelper.getCurentCamera();
                // translate the mouse coordinates to world coordinates
                camera.unproject(testPoint.set(x, y, 0));
                // ask the world which bodies are within the given
                // bounding box around the mouse pointer
                hitBody = null;
                b2world.QueryAABB(callback, testPoint.x - 0.0001f, testPoint.y - 0.0001f, testPoint.x + 0.0001f, testPoint.y + 0.0001f);

    //        if (hitBody == groundBody) hitBody = null;
    //
    //        // ignore kinematic bodies, they don't work with the mouse joint
    //        if (hitBody != null && hitBody.getType() == BodyDef.BodyType.KinematicBody) return false;

                // if we hit something we create a new mouse joint
                // and attach it to the hit body.
                if (hitBody != null) {
                    MouseJointDef def = new MouseJointDef();
                    def.bodyA = groundBody;
                    def.bodyB = hitBody;
                    def.collideConnected = true;
                    def.target.set(testPoint.x, testPoint.y);

                    putTowerToPosition(level.spawnPoint);

                    mouseJoint = (MouseJoint)b2world.createJoint(def);
                    hitBody.setAwake(true);
                    scoreVisual = scoreVisual +1;

                }
            }
            return false;
        }



        @Override
        public boolean zoom(float initialDistance, float distance) {
            float camZoomSpeed = 5 ;

            if (initialDistance > distance){
                //cameraHelper.addZoom(camZoomSpeed);
            }else if(initialDistance < distance){
                //cameraHelper.addZoom(-camZoomSpeed);
            }

            Gdx.app.debug(TAG,"initialDistance:"+ String.valueOf(initialDistance) + "distance:"+String.valueOf(distance));
            return false;
        }

    }
    //endregion

    public void putTowerToPosition(Vector2 _towerPosition){

    }
}
