package com.ashenfall.game.engine;

public class Camera {
    private float x;
    private float targetX;
    private float shakeIntensity = 0;
    private int shakeDuration = 0;
    private float shakeOffsetX = 0;
    private float shakeOffsetY = 0;

    public Camera() {
        this.x = 0;
        this.targetX = 0;
    }

    public void update(float playerX, int screenWidth, int worldWidth) {
        targetX = playerX - screenWidth / 3.0f;
        
        // Smooth linear interpolation
        x += (targetX - x) * 0.1f;

        // Clamp camera to world bounds
        if (x < 0) x = 0;
        if (x > worldWidth - screenWidth) x = worldWidth - screenWidth;

        // Update screen shake
        if (shakeDuration > 0) {
            shakeDuration--;
            shakeOffsetX = (float) ((Math.random() - 0.5) * 2 * shakeIntensity);
            shakeOffsetY = (float) ((Math.random() - 0.5) * 2 * shakeIntensity);
        } else {
            shakeOffsetX = 0;
            shakeOffsetY = 0;
        }
    }

    public void shake(float intensity, int durationFrames) {
        this.shakeIntensity = intensity;
        this.shakeDuration = durationFrames;
    }

    public float getX() {
        return x + shakeOffsetX;
    }

    public float getOffsetY() {
        return shakeOffsetY;
    }
}
