package com.stardust.crusaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;

class GameScreen implements Screen {
    final SpaceShooterGame game;
    private Stage stage;
    private ImageButton pauseButton;
    private Texture pauseTexture;
    //screen
    private Camera camera;
    private Viewport viewport;
    private boolean sfxState, bgmState;
    //graphics
    private SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private Texture explosionTexture;

    private TextureRegion[] backgrounds;
    private float backgroundHeight; //height of background in World units

    private TextureRegion playerShipTextureRegion, playerShieldTextureRegion,
            enemyShipTextureRegion, enemyShieldTextureRegion,
            playerLaserTextureRegion, enemyLaserTextureRegion,
            shieldPowerupTextureRegion, firePowerupTextureRegion,
            enemyShipTextureRegion2, enemyShieldTextureRegion2,
            enemyLaserTextureRegion2;

    //sfx
    private Sound death, laser, power;
    private Music music;

    //timing
    private float[] backgroundOffsets = {0, 0, 0, 0};
    private float backgroundMaxScrollingSpeed;
    float timeBetweenEnemySpawns;
    private float enemySpawnTimer = 0;

    //control
    private Vector2 touchPoint = new Vector2();
    private Vector2 playerShipCentre = new Vector2();
    //world parameters
    private final float WORLD_WIDTH = 72;
    private final float WORLD_HEIGHT = 128;
    private final float TOUCH_MOVEMENT_THRESHOLD = 5f;
    int gachaRate;

    //game objects
    private PlayerShip playerShip;
    private LinkedList<EnemyShip> enemyShipList;
    private LinkedList<Laser> playerLaserList;
    private LinkedList<Laser> enemyLaserList;
    private LinkedList<Explosion> explosionList;
    private LinkedList<PowerUp> powerupList;

    private int score = 0;
    int multiplier;
    private boolean isPaused, gameOver = false;

    //Heads-Up Display
    BitmapFont font;
    float hudVerticalMargin, hudLeftX, hudRightX, hudCentreX, hudRow1Y, hudRow2Y, hudSectionWidth;

    GameScreen(final SpaceShooterGame game) {
        this.game = game;
        music = game.music;
        if(game.mode.equals(SpaceShooterGame.MODE.EASY)){
            timeBetweenEnemySpawns = 1.4f;
            gachaRate = 30;
            multiplier = 1;
        } else if(game.mode.equals(SpaceShooterGame.MODE.MEDIUM)){
            timeBetweenEnemySpawns = 1.2f;
            gachaRate = 20;
            multiplier = 2;
        } else {
            timeBetweenEnemySpawns = 1f;
            gachaRate = 10;
            multiplier = 3;
        }
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        //set up the texture atlas
        textureAtlas = new TextureAtlas("images.atlas");

        //setting up the background
        backgrounds = new TextureRegion[4];
        backgrounds[0] = textureAtlas.findRegion("Starscape00");
        backgrounds[1] = textureAtlas.findRegion("Starscape01");
        backgrounds[2] = textureAtlas.findRegion("Starscape02");
        backgrounds[3] = textureAtlas.findRegion("Starscape03");

        backgroundHeight = WORLD_HEIGHT * 2;
        backgroundMaxScrollingSpeed =  (WORLD_HEIGHT) / 4;

        //initialize texture regions
        playerShipTextureRegion = textureAtlas.findRegion("Main Ship");
        enemyShipTextureRegion = textureAtlas.findRegion("Enemy Ship - Fighter");
        enemyShipTextureRegion2 = textureAtlas.findRegion("Enemy Ship - Scout");

        playerShieldTextureRegion = textureAtlas.findRegion("Shield");
        enemyShieldTextureRegion = textureAtlas.findRegion("Fighter Shield");
        enemyShieldTextureRegion2 = textureAtlas.findRegion("Scout Shield");

        playerLaserTextureRegion = textureAtlas.findRegion("Cannon bullet");
        enemyLaserTextureRegion = textureAtlas.findRegion("Fast bullet");
        enemyLaserTextureRegion.flip(false, true);
        enemyLaserTextureRegion2 = textureAtlas.findRegion("Slow bullet");
        shieldPowerupTextureRegion = textureAtlas.findRegion("Shield Powerup");
        firePowerupTextureRegion = textureAtlas.findRegion("Fire Powerup");

        explosionTexture = new Texture("explosion.png");

        //set up sfx
        death = game.death;
        laser = game.laser;
        power = game.power;


        //set up game objects
        playerShip = new PlayerShip(WORLD_WIDTH / 2, WORLD_HEIGHT / 4,
                10, 10,
                60, 0,
                2f, 4.2f, 45, 0.5f,
                playerShipTextureRegion, playerShieldTextureRegion, playerLaserTextureRegion);

        enemyShipList = new LinkedList<>();

        playerLaserList = new LinkedList<>();
        enemyLaserList = new LinkedList<>();
        powerupList = new LinkedList<>();
        explosionList = new LinkedList<>();

        batch = new SpriteBatch();

        prepareHUD();
    }
    private void prepareHUD() {
        //Create a BitmapFont from our font file
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("EdgeOfTheGalaxyRegular-OVEa6.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 72;
        fontParameter.borderWidth = 3.6f;
        fontParameter.color = new Color(1.0f, 0.839f, 0.0f, 1.0f);
        fontParameter.borderColor = new Color(0, 0, 0, 0.3f);

        font = fontGenerator.generateFont(fontParameter);

        //scale the font to fit world
        font.getData().setScale(0.08f);

        //calculate hud margins, etc.
        hudVerticalMargin = font.getCapHeight() / 2;
        hudLeftX = hudVerticalMargin;
        hudRightX = WORLD_WIDTH * 2 / 3 - hudLeftX;
        hudCentreX = WORLD_WIDTH / 3;
        hudRow1Y = WORLD_HEIGHT - hudVerticalMargin;
        hudRow2Y = hudRow1Y - hudVerticalMargin - font.getCapHeight();
        hudSectionWidth = WORLD_WIDTH / 3;
    }

    @Override
    public void render(float deltaTime) {
        if(!isPaused&&!gameOver){
        batch.begin();
        //scrolling background
        renderBackground(deltaTime);
        detectInput(deltaTime);
        playerShip.update(deltaTime);

        spawnEnemyShips(deltaTime);

        for (EnemyShip enemyShip : enemyShipList) {
            moveEnemy(enemyShip, deltaTime);
            enemyShip.update(deltaTime);
            enemyShip.draw(batch);
        }
        //player ship
        playerShip.draw(batch);

        //lasers
        renderLasers(deltaTime);

        //detect collisions between lasers and ships
        detectCollisions();

        //explosions
        updateAndRenderExplosions(deltaTime);

        //powerups
            for (PowerUp powerUps : powerupList) {
                powerUps.update(deltaTime);
                powerUps.draw(batch);
            }
            checkPowerUpCollisions();
        //hud rendering
        updateAndRenderHUD();
        batch.end();
        }
        stage.act();
        stage.draw();
        if(isPaused){
            game.setScreen(game.pauseScreen);
        }
        if(gameOver){
            game.gameoverScreen.setScore(score);
            clearGameState();
            game.setScreen(game.gameoverScreen);
        }
    }

    private void updateAndRenderHUD() {
        //render top row labels
        font.draw(batch, "Score", hudLeftX, hudRow1Y, hudSectionWidth, Align.left, false);
        font.draw(batch, "Lives", hudRightX, hudRow1Y, hudSectionWidth, Align.right, false);
        //render second row values
        font.draw(batch, String.format(Locale.getDefault(), "%06d", score), hudLeftX, hudRow2Y, hudSectionWidth, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%02d", playerShip.lives), hudRightX, hudRow2Y, hudSectionWidth, Align.right, false);
    }

    private void spawnEnemyShips(float deltaTime) {
        enemySpawnTimer += deltaTime;
        int gacha = SpaceShooterGame.random.nextInt(100);
        if (enemySpawnTimer > timeBetweenEnemySpawns&&gacha>50) {
            enemyShipList.add(new EnemyShip(SpaceShooterGame.random.nextFloat() * (WORLD_WIDTH - 10) + 5,
                WORLD_HEIGHT - 5,
                10, 10,
                50, 1,
                1.3f, 1.3f, 20, 1f,
                enemyShipTextureRegion2, enemyShieldTextureRegion2, enemyLaserTextureRegion2));
            enemySpawnTimer -= timeBetweenEnemySpawns;
        } else if (enemySpawnTimer > timeBetweenEnemySpawns) {
            enemyShipList.add(new EnemyShip(SpaceShooterGame.random.nextFloat() * (WORLD_WIDTH - 10) + 5,
                    WORLD_HEIGHT - 5,
                    10, 10,
                    35, 2,
                    1f, 2, 50, 0.8f,
                    enemyShipTextureRegion, enemyShieldTextureRegion, enemyLaserTextureRegion));
            enemySpawnTimer -= timeBetweenEnemySpawns;
        }
    }

    private void detectInput(float deltaTime) {

        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -playerShip.boundingBox.x;
        downLimit = -playerShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - playerShip.boundingBox.x - playerShip.boundingBox.width;
        upLimit =  WORLD_HEIGHT / 2 - playerShip.boundingBox.y - playerShip.boundingBox.height;

        //touch input (also mouse)
        if (Gdx.input.isTouched()) {
            //get the screen position of the touch
            float xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();

            //convert to world position
            touchPoint.set(xTouchPixels, yTouchPixels);
            touchPoint = viewport.unproject(touchPoint);

            //calculate the x and y differences
            playerShipCentre.set(
                playerShip.boundingBox.x + playerShip.boundingBox.width / 2,
                playerShip.boundingBox.y + playerShip.boundingBox.height / 2);

            float touchDistance = touchPoint.dst(playerShipCentre);

            if (touchDistance > TOUCH_MOVEMENT_THRESHOLD) {
                float xTouchDifference = touchPoint.x - playerShipCentre.x;
                float yTouchDifference = touchPoint.y - playerShipCentre.y;

                //scale to the maximum speed of the ship
                float xMove = xTouchDifference / touchDistance * playerShip.movementSpeed * deltaTime;
                float yMove = yTouchDifference / touchDistance * playerShip.movementSpeed * deltaTime;

                if (xMove > 0) xMove = Math.min(xMove, rightLimit);
                else xMove = Math.max(xMove, leftLimit);

                if (yMove > 0) yMove = Math.min(yMove, upLimit);
                else yMove = Math.max(yMove, downLimit);

                playerShip.translate(xMove, yMove);
            }
        }
    }

    private void moveEnemy(EnemyShip enemyShip, float deltaTime) {
        //strategy: determine the max distance the ship can move

        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -enemyShip.boundingBox.x;
        downLimit =  WORLD_HEIGHT / 2 - enemyShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - enemyShip.boundingBox.x - enemyShip.boundingBox.width;
        upLimit = WORLD_HEIGHT - enemyShip.boundingBox.y - enemyShip.boundingBox.height;

        float xMove = enemyShip.getDirectionVector().x * enemyShip.movementSpeed * deltaTime;
        float yMove = enemyShip.getDirectionVector().y * enemyShip.movementSpeed * deltaTime;

        if (xMove > 0) xMove = Math.min(xMove, rightLimit);
        else xMove = Math.max(xMove, leftLimit);

        if (yMove > 0) yMove = Math.min(yMove, upLimit);
        else yMove = Math.max(yMove, downLimit);

        enemyShip.translate(xMove, yMove);
    }

    private void detectCollisions() {
        //for each player laser, check whether it intersects an enemy ship
        ListIterator<Laser> laserListIterator = playerLaserList.listIterator();
        while (laserListIterator.hasNext()) {
            Laser laser = laserListIterator.next();
            ListIterator<EnemyShip> enemyShipListIterator = enemyShipList.listIterator();
            while (enemyShipListIterator.hasNext()) {
                EnemyShip enemyShip = enemyShipListIterator.next();

                if (enemyShip.intersects(laser.boundingBox)) {
                    //contact with enemy ship
                    if (enemyShip.hitAndCheckDestroyed(laser)) {
                        enemyShipListIterator.remove();
                        explosionList.add(
                                new Explosion(explosionTexture,
                                        new Rectangle(enemyShip.boundingBox),
                                        0.7f));
                        if(sfxState){death.play(4f);}
                        score += 100*multiplier;
                        int gacha = SpaceShooterGame.random.nextInt(100);
                        if (gacha < gachaRate) {
                            spawnPowerUp(score, MathUtils.random(50, WORLD_WIDTH - 50), MathUtils.random(50, WORLD_HEIGHT / 3));

                        }
                    }
                    laserListIterator.remove();
                    break;
                }
            }
        }
        //for each enemy laser, check whether it intersects the player ship
        laserListIterator = enemyLaserList.listIterator();
        while (laserListIterator.hasNext()) {
            Laser laser = laserListIterator.next();
            if (playerShip.intersects(laser.boundingBox)) {
                //contact with player ship
                if (playerShip.hitAndCheckDestroyed(laser)) {
                    explosionList.add(
                            new Explosion(explosionTexture,
                                    new Rectangle(playerShip.boundingBox),
                                    1.6f));
                    playerShip.lives--;
                    if (playerShip.lives == 0){
                        if(sfxState){death.play(4f);}
                        gameOver = true;
                    }
                }
                laserListIterator.remove();
            }
        }
    }

    private void updateAndRenderExplosions(float deltaTime) {
        ListIterator<Explosion> explosionListIterator = explosionList.listIterator();
        while (explosionListIterator.hasNext()) {
            Explosion explosion = explosionListIterator.next();
            explosion.update(deltaTime);
            if (explosion.isFinished()) {
                explosionListIterator.remove();
            } else {
                explosion.draw(batch);
            }
        }
    }

    private void renderLasers(float deltaTime) {
        //create new lasers
        //player lasers
        if (playerShip.canFireLaser()) {
            Laser[] lasers = playerShip.fireLasers();
            if(sfxState){laser.play(4f);}
            playerLaserList.addAll(Arrays.asList(lasers));
        }
        //enemy lasers
        for (EnemyShip enemyShip : enemyShipList) {
            if (enemyShip.canFireLaser()) {
                Laser[] lasers = enemyShip.fireLasers();
                enemyLaserList.addAll(Arrays.asList(lasers));
            }
        }
        //draw lasers
        //remove old lasers
        ListIterator<Laser> iterator = playerLaserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y += laser.movementSpeed * deltaTime;
            if (laser.boundingBox.y > WORLD_HEIGHT) {
                iterator.remove();
            }
        }
        iterator = enemyLaserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y -= laser.movementSpeed * deltaTime;
            if (laser.boundingBox.y + laser.boundingBox.height < 0) {
                iterator.remove();
            }
        }
    }

    private void spawnPowerUp(int score, float x, float y) {
        if (powerupList.isEmpty()) {
            int randomType = SpaceShooterGame.random.nextInt(2);  // Assuming 3 types of power-ups
            if (randomType == 0) {
                powerupList.add(new PowerUp(PowerUp.PowerUpType.SHIELD, shieldPowerupTextureRegion, x, y, 5, 5));
            } else {
                powerupList.add(new PowerUp(PowerUp.PowerUpType.FIRE_RATE, firePowerupTextureRegion, x, y, 5, 5));
            }
        }
    }
    public void checkPowerUpCollisions() {
        ListIterator<PowerUp> powerListIterator = powerupList.listIterator();
        while (powerListIterator.hasNext()) {
            PowerUp powerup = powerListIterator.next();
            if (playerShip.intersects(powerup.boundingBox)) {
                //contact with player ship
                applyPowerUpEffect(powerup.getType());
                power.play(5f);
                score += 100;
                powerListIterator.remove();
            }
            if (powerup.height<0){
                powerListIterator.remove();
            }
        }
    }
    private  void applyPowerUpEffect(PowerUp.PowerUpType type){
        switch (type){
            case SHIELD:
                playerShip.shield += 1;
                break;
            case FIRE_RATE:
                playerShip.timeBetweenShots *= 0.9f;
                break;
            default:
                break;
        }
    }
    private void renderBackground(float deltaTime) {

        //update position of background images
        backgroundOffsets[0] += deltaTime * backgroundMaxScrollingSpeed / 8;
        backgroundOffsets[1] += deltaTime * backgroundMaxScrollingSpeed / 4;
        backgroundOffsets[2] += deltaTime * backgroundMaxScrollingSpeed / 2;
        backgroundOffsets[3] += deltaTime * backgroundMaxScrollingSpeed;

        //draw each background layer
        for (int layer = 0; layer < backgroundOffsets.length; layer++) {
            if (backgroundOffsets[layer] > WORLD_HEIGHT) {
                backgroundOffsets[layer] = 0;
            }
            batch.draw(backgrounds[layer], 0, -backgroundOffsets[layer],
                    WORLD_WIDTH, backgroundHeight);
        }
    }
    void clearGameState() {
        // Clear all entities and reset relevant game variables
        enemyShipList.clear();
        playerLaserList.clear();
        enemyLaserList.clear();
        explosionList.clear();
        powerupList.clear();
        resetPlayer();
        score = 0;
        music.stop();
        isPaused = false;
        gameOver = false;
    }
    private void resetPlayer(){
        playerShip = new PlayerShip(WORLD_WIDTH / 2, WORLD_HEIGHT / 4,
            10, 10,
            60, 0,
            2f, 4.2f, 45, 0.5f,
            playerShipTextureRegion, playerShieldTextureRegion, playerLaserTextureRegion);
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {
        isPaused = false;
    }

    @Override
    public void hide() {
    }

    @Override
    public void show() {
        stage = new Stage();

        // Load the texture for the pause button
        pauseTexture = new Texture(Gdx.files.internal("Pause.png"));

        // Create a drawable from the texture
        TextureRegionDrawable pauseDrawable = new TextureRegionDrawable(pauseTexture);

        // Create the ImageButton with the pause image
        pauseButton = new ImageButton(pauseDrawable);


        // Set the position of the button in the top center of the screen
        float buttonX = (float) (Gdx.graphics.getWidth() - pauseTexture.getWidth()) / 2;
        float buttonY = Gdx.graphics.getHeight() - pauseTexture.getHeight() - 50;
        pauseButton.setPosition(buttonX, buttonY);

        // Add the button to the stage
        stage.addActor(pauseButton);

        // Add a listener to the button to handle pause functionality
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Toggle pause state when button is clicked
                isPaused = true;
            }
        });
        sfxState = game.prefs.getBoolean("sfxState");
        bgmState = game.prefs.getBoolean("bgmState");
        if(bgmState){
            music.play();
        }
        // Set the stage to handle input
        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        laser.dispose();
        death.dispose();
        power.dispose();
    }
}
