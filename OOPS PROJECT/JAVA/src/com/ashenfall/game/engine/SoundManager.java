package com.ashenfall.game.engine;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class SoundManager {
    private static boolean soundEnabled = true;

    public static boolean isSoundEnabled() {
        return soundEnabled;
    }

    public static void toggleSound() {
        soundEnabled = !soundEnabled;
    }

    public static void playSound(String type) {
        if (!soundEnabled) return;
        new Thread(() -> {
            try {
                switch (type) {
                    case "jump":
                        playSweep(300, 600, 100);
                        break;
                    case "double_jump":
                        playSweep(500, 900, 120);
                        break;
                    case "slash":
                        playNoise(80);
                        break;
                    case "hit":
                        playSweep(220, 100, 90);
                        break;
                    case "boss_spell":
                        playSweep(150, 450, 250);
                        break;
                    case "button":
                        playTone(700, 50);
                        break;
                    case "coin":
                        playSweep(900, 1600, 70);
                        break;
                    case "heal":
                        playTone(523, 80);
                        Thread.sleep(80);
                        playTone(784, 120);
                        break;
                    case "checkpoint":
                        playTone(440, 90);
                        Thread.sleep(90);
                        playTone(554, 90);
                        Thread.sleep(90);
                        playTone(659, 180);
                        break;
                    case "chest":
                        playSweep(300, 800, 150);
                        break;
                    case "victory":
                        playTone(523, 150);
                        Thread.sleep(150);
                        playTone(659, 150);
                        Thread.sleep(150);
                        playTone(783, 300);
                        break;
                    case "game_over":
                        playSweep(350, 120, 300);
                        break;
                }
            } catch (Exception ignored) {}
        }).start();
    }

    private static void playTone(int freq, int durationMs) throws Exception {
        byte[] buf = new byte[durationMs * 8];
        for (int i = 0; i < buf.length; i++) {
            double angle = i / (8000.0 / freq) * 2.0 * Math.PI;
            buf[i] = (byte) (Math.sin(angle) * 80);
        }
        playBuffer(buf);
    }

    private static void playSweep(int startFreq, int endFreq, int durationMs) throws Exception {
        byte[] buf = new byte[durationMs * 8];
        for (int i = 0; i < buf.length; i++) {
            double progress = (double) i / buf.length;
            double freq = startFreq + (endFreq - startFreq) * progress;
            double angle = i / (8000.0 / freq) * 2.0 * Math.PI;
            buf[i] = (byte) (Math.sin(angle) * 70 * (1.0 - progress));
        }
        playBuffer(buf);
    }

    private static void playNoise(int durationMs) throws Exception {
        byte[] buf = new byte[durationMs * 8];
        for (int i = 0; i < buf.length; i++) {
            double progress = (double) i / buf.length;
            buf[i] = (byte) ((Math.random() * 160 - 80) * (1.0 - progress));
        }
        playBuffer(buf);
    }

    private static void playBuffer(byte[] buf) throws Exception {
        AudioFormat af = new AudioFormat(8000, 8, 1, true, false);
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
        sdl.write(buf, 0, buf.length);
        sdl.drain();
        sdl.close();
    }
}
