package net.badlogic.td.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import net.badlogic.td.game.objects.AbstractGameObject;
import net.badlogic.td.game.objects.Road;
import net.badlogic.td.util.ConstantID;
import net.badlogic.td.game.objects.Plate;

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

    private int objectID;





    public Level (String filename ) {
        init(filename);
    }

    private void init (String filename) {
        map = new TmxMapLoader().load(filename);

        //Objects
        plates = new Array<Plate>();
        roads = new Array<Road>();
        //plate1 = new Plate();

        backgroundLayer = (TiledMapTileLayer)map.getLayers().get("plate");



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

        Gdx.app.debug(TAG, "# layer name loaded: " + backgroundLayer.getName());

    }

    public void render (SpriteBatch batch) {

        for (Plate plate : plates)
            plate.render(batch);

        for (Road road : roads)
            road.render(batch);
//        mapRenderer.setView(camera);
//
//        mapRenderer.render();

        //plate.render(batch);


    }

    public void update (float deltaTime) {

        for (Plate plate : plates)
            plate.update(deltaTime);

        for (Road road : roads)
            road.update(deltaTime);
    }


}
