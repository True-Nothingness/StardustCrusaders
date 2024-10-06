package com.stardust.crusaders;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class PowerUp {

    public enum PowerUpType { SHIELD, FIRE_RATE }

    private PowerUpType type;
    private TextureRegion textureRegion;
    Vector2 position;
    float width, height;
    Rectangle boundingBox;


    public PowerUp(PowerUpType type, TextureRegion textureRegion, float x, float y, float width, float height) {
        this.type = type;
        this.textureRegion = textureRegion;
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.boundingBox = new Rectangle(position.x, position.y, width, height);
    }
    public void draw(Batch batch) {
        batch.draw(textureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }
    public PowerUpType getType() {
        return type;
    }

    public void update(float deltaTime) {
        position.y -= 40 * deltaTime;  // Falling down effect
    }
}
