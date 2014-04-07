package net.badlogic.td.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.*;
import com.badlogic.gdx.utils.Disposable;
import net.badlogic.td.game.objects.crip.ConstantCrip;
import net.badlogic.td.util.Constants;

/**
 * Created by BruSD on 16.03.14.
 */
public class Assets  implements Disposable, AssetErrorListener {

    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();

    private AssetManager assetManager;

    public AssetFonts fonts;

    //static game
    public AssetBackground background;
    public AssetPlate plate;
    public AssetPlateRoad plateRoad;

    //Crips
    public AssetCrip assetCrip;

    // singleton: prevent instantiation from other classes
    private Assets () {}

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {

    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public void init (AssetManager assetManager) {
        this.assetManager = assetManager;

        assetManager.setErrorListener(this);
        assetManager.load(Constants.TEXTURE_ATLAS_STATIC_GAME_OBJECTS, TextureAtlas.class);
        assetManager.load(Constants.TEXTURE_ATLAS_CRIPS, TextureAtlas.class);

        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: "
                + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames())
            Gdx.app.debug(TAG, "asset: " + a);

        TextureAtlas atlasStaticGameElements = assetManager.get(Constants.TEXTURE_ATLAS_STATIC_GAME_OBJECTS);
        TextureAtlas cripsAtlas= assetManager.get(Constants.TEXTURE_ATLAS_CRIPS);
        // enable texture filtering for pixel smoothing
        for (Texture t : atlasStaticGameElements.getTextures())
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        for (Texture t : cripsAtlas.getTextures())
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        // create game resource objects
        fonts = new AssetFonts();

        //static object
        plate = new AssetPlate(atlasStaticGameElements);
        plateRoad = new AssetPlateRoad(atlasStaticGameElements);

        //crips
        assetCrip = new AssetCrip(cripsAtlas);
    }

    public class AssetCrip {

        public final AtlasRegion simpleCrip;

        public AssetCrip(TextureAtlas atlas){
            simpleCrip = atlas.findRegion(ConstantCrip.SIMPLE_CRIP);

        }
    }
    public class AssetBackground {

        public final AtlasRegion background;

        public AssetBackground(TextureAtlas atlas){
            background = atlas.findRegion("background");

        }
    }

    public class AssetPlate{
        public final AtlasRegion plate;

        public AssetPlate(TextureAtlas atlas){
            plate = atlas.findRegion("plate");

        }
    }
    public class AssetPlateRoad {
        public final AtlasRegion plateRoad;
        public AssetPlateRoad(TextureAtlas atlas){
            plateRoad = atlas.findRegion("plate_road");

        }
    }



    public class AssetFonts {

        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;
        public AssetFonts () {
            // create three fonts using Libgdx's 15px bitmap font
            defaultSmall = new BitmapFont( Gdx.files.internal("images/arial-15.fnt"), true);
            defaultNormal = new BitmapFont( Gdx.files.internal("images/arial-15.fnt"), true);
            defaultBig = new BitmapFont( Gdx.files.internal("images/arial-15.fnt"), true);
            // set font sizes
            defaultSmall.setScale(0.75f);
            defaultNormal.setScale(1.0f);
            defaultBig.setScale(2.0f);
            // enable linear texture filtering for smooth fonts

            defaultSmall.getRegion().getTexture().setFilter(
                    Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(
                    Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(
                    Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
    }



}
