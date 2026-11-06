package com.ashenfall.game.entities.player;

import com.ashenfall.game.world.Tile;
import com.ashenfall.game.world.TileMap;

import java.awt.Rectangle;

public class PlayerCollision {
    private Player player;

    public PlayerCollision(Player player) {
        this.player = player;
    }

    public boolean resolveTileCollisions(TileMap tileMap) {
        boolean wasOnGround = player.onGround;
        player.onGround = false;
        Rectangle playerBounds = player.getBounds();
        boolean hitHazard = false;

        Rectangle groundCheckBounds = new Rectangle((int) player.x, (int) player.y + 1, player.width, player.height);

        for (Tile tile : tileMap.tiles) {
            if (!tile.solid && !tile.isHazard) continue;

            Rectangle tileBounds = tile.getBounds();
            if (playerBounds.intersects(tileBounds)) {
                if (tile.isHazard) {
                    hitHazard = true;
                    continue;
                }

                // Vertical collision
                if (player.vy >= 0 && player.y + player.height - Math.max(player.vy, 1.0f) <= tile.y + 12) {
                    player.y = tile.y - player.height;
                    player.vy = 0;
                    player.onGround = true;
                    player.getMovement().jumpCount = 0;
                } else if (player.vy < 0 && player.y - player.vy >= tile.y + tile.height - 12) {
                    player.y = tile.y + tile.height;
                    player.vy = 0;
                } else {
                    // Horizontal collision
                    if (player.vx > 0) {
                        player.x = tile.x - player.width;
                    } else if (player.vx < 0) {
                        player.x = tile.x + tile.width;
                    }
                }
            } else if (wasOnGround && player.vy >= 0 && groundCheckBounds.intersects(tileBounds) && !tile.isHazard) {
                if (player.y + player.height <= tile.y + 4) {
                    player.y = tile.y - player.height;
                    player.vy = 0;
                    player.onGround = true;
                }
            }
        }
        return hitHazard;
    }
}
