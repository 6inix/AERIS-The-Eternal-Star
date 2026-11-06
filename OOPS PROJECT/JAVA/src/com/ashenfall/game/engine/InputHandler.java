package com.ashenfall.game.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Point;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {
    public boolean left = false;
    public boolean right = false;
    public boolean up = false;
    public boolean down = false;
    public boolean crouch = false;
    
    public boolean jumpPressed = false;
    public boolean attackPressed = false;
    public boolean pausePressed = false;
    public boolean upPressed = false;
    public boolean downPressed = false;
    public boolean enterPressed = false;
    
    public Point mousePoint = new Point(0, 0);
    public boolean mouseClicked = false;

    private boolean jumpHandled = false;
    private boolean attackHandled = false;
    private boolean pauseHandled = false;
    private boolean upHandled = false;
    private boolean downHandled = false;
    private boolean enterHandled = false;

    public void update() {
        // Reset single-frame triggers if released
    }

    public boolean consumeUp() {
        if (upPressed && !upHandled) {
            upHandled = true;
            return true;
        }
        return false;
    }

    public boolean consumeDown() {
        if (downPressed && !downHandled) {
            downHandled = true;
            return true;
        }
        return false;
    }

    public boolean consumeEnter() {
        if (enterPressed && !enterHandled) {
            enterHandled = true;
            return true;
        }
        return false;
    }

    public boolean consumeJump() {
        if (jumpPressed && !jumpHandled) {
            jumpHandled = true;
            return true;
        }
        return false;
    }

    public boolean consumeAttack() {
        if (attackPressed && !attackHandled) {
            attackHandled = true;
            return true;
        }
        return false;
    }

    public boolean consumePause() {
        if (pausePressed && !pauseHandled) {
            pauseHandled = true;
            return true;
        }
        return false;
    }

    public boolean consumeClick() {
        if (mouseClicked) {
            mouseClicked = false;
            return true;
        }
        return false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            left = true;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            right = true;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            down = true;
            crouch = true;
            if (!downHandled) {
                downPressed = true;
            }
        }
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            up = true;
            if (!upHandled) {
                upPressed = true;
            }
            if (!jumpHandled) {
                jumpPressed = true;
            }
        }
        if (code == KeyEvent.VK_SPACE) {
            up = true;
            if (!jumpHandled) {
                jumpPressed = true;
            }
        }
        if (code == KeyEvent.VK_ENTER) {
            if (!enterHandled) {
                enterPressed = true;
            }
        }
        if (code == KeyEvent.VK_J || code == KeyEvent.VK_Z) {
            if (!attackHandled) {
                attackPressed = true;
            }
        }
        if (code == KeyEvent.VK_P || code == KeyEvent.VK_ESCAPE) {
            if (!pauseHandled) {
                pausePressed = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            left = false;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            right = false;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            down = false;
            crouch = false;
            downPressed = false;
            downHandled = false;
        }
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            up = false;
            upPressed = false;
            upHandled = false;
            jumpPressed = false;
            jumpHandled = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            up = false;
            jumpPressed = false;
            jumpHandled = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
            enterHandled = false;
        }
        if (code == KeyEvent.VK_J || code == KeyEvent.VK_Z) {
            attackPressed = false;
            attackHandled = false;
        }
        if (code == KeyEvent.VK_P || code == KeyEvent.VK_ESCAPE) {
            pausePressed = false;
            pauseHandled = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (!attackHandled) {
                attackPressed = true;
            }
            mouseClicked = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            attackPressed = false;
            attackHandled = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePoint = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mousePoint = e.getPoint();
    }
}
