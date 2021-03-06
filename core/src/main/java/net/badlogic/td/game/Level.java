package net.badlogic.td.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import net.badlogic.td.game.objects.AbstractGameObject;
import net.badlogic.td.game.objects.Road;
import net.badlogic.td.game.objects.crip.SimpleCrip;
import net.badlogic.td.util.ConstantID;
import net.badlogic.td.game.objects.Plate;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by BruSD on 16.03.14.
 */
public class Level {

    public static final String TAG = Level.class.getName();
    private TiledMap map;
    private TiledMapTile tiledMapTile;
    public MapRenderer mapRenderer;

    public  Array<Plate> plates;
    public Array<Road> roads;
    public  TiledMapTileLayer backgroundLayer;
    public  MapLayer roadLayer;


    private int objectID;


    float offSetY;
    float offSetX;

    public SimpleCrip simpleCrip;

    //vectors for move
    public ArrayList<HashMap<String, Float>> roadList;
    public Vector2 spawnPoint ;


    public Level (String filename ) {
        init(filename);
    }

    private void init (String filename) {
        map = new TmxMapLoader().load(filename);

        //Objects
        plates = new Array<Plate>();
        roads = new Array<Road>();

        roadList =  new ArrayList<HashMap<String, Float>>();
        spawnPoint =  new Vector2();
        //plate1 = new Plate();

        backgroundLayer = (TiledMapTileLayer)map.getLayers().get("plate");
        roadLayer = map.getLayers().get("road");

        offSetY = backgroundLayer.getHeight()/2;
        offSetX = backgroundLayer.getWidth()/2;
        initGameObjects();
        initRoadList();
        initSpawnPoints();
        initCrips();
        //create road


        Gdx.app.debug(TAG, "# layer name loaded: " + backgroundLayer.getName());

    }

    private void initCrips() {


        simpleCrip = new SimpleCrip(roadList, 0 );


    }

    public void render (SpriteBatch batch) {

        for (Plate plate : plates){
            if (plate.isEmpty){
                plate.render(batch);
            }else {
                plate.render(batch);
            }
        }


        for (Road road : roads)

            road.render(batch);

        simpleCrip.render(batch);
    }

    public void update (float deltaTime) {

        for (Plate plate : plates)
            plate.update(deltaTime);

        for (Road road : roads)
            road.update(deltaTime);

        simpleCrip.update(deltaTime);
    }

    private void initGameObjects(){
        float offSetY = backgroundLayer.getHeight()/2;
        float offSetX = backgroundLayer.getWidth()/2;

        //load map object
        for (int tileY = 0; tileY < backgroundLayer.getHeight() ; tileY++) {
            for (int tileX = 0; tileX < backgroundLayer.getWidth(); tileX++) {
                AbstractGameObject obj = null;
                try {

                    tiledMapTile = backgroundLayer.getCell(tileX, tileY).getTile();
                    objectID = Integer.valueOf(tiledMapTile.getProperties().get("tileID").toString());

                }catch (NullPointerException e){
                    objectID = 0;
                }
                switch (objectID){

                    case ConstantID.GAME_OBJECT_PLATE:

                        obj = new Plate();


                        obj.position.set(tileX - offSetX, tileY - offSetY );

                        plates.add((Plate)obj);
                        break;
                    case ConstantID.GAME_OBJECT_ROAD:
                        obj = new Road();


                        obj.position.set(tileX - offSetX, tileY- offSetY );

                        roads.add((Road)obj);
                        break;

                    case 0:
                        //do nothing
                        break;


                }
            }
        }
    }

    private void initRoadList(){
        int objectsCount = roadLayer.getObjects().getCount();

        for (int i = 0; i < objectsCount; i++){
            HashMap<String , Float >tempRoadPoint = new HashMap<String, Float>();
            float xPosition = Float.valueOf( roadLayer.getObjects().get(i).getProperties().get("x").toString()) - offSetX;
            float yPosition =  Float.valueOf(roadLayer.getObjects().get(i).getProperties().get("y").toString()) - offSetY ;

            tempRoadPoint.put("x",xPosition);
            tempRoadPoint.put("y", yPosition);
            roadList.add(tempRoadPoint);
        }
    }

    private void initSpawnPoints(){
        HashMap<String , Float > firstPoint =  roadList.get(0);
        spawnPoint = new Vector2(firstPoint.get("x"), firstPoint.get("y"));

    }
}
