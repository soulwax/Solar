package de.cirrus.solar;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Solar
 * Copyright (C) 2014 by Cirrus
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * -
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * -
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * -
 * Contact: cirrus.contact@t-online.de
 */

public class SolarGame extends Canvas implements Runnable {
    public static final int WIDTH = 320;
    public static final int HEIGHT = 240;
    public static final int SCALE = 2;
    public static final String TITLE = "SOLAR-SYSTEM SIMULATOR";

    public static boolean running = false;
    public Thread mainThread;

    public BufferedImage image;
    public int[] pixels;

    public SolarSystem system;
    public Screen screen;

    public SolarGame() {
        this.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        this.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
    }

    public void init() {
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        screen = new Screen(WIDTH, HEIGHT, pixels);
        system = new SolarSystem();

        Celestial sun = new Celestial(WIDTH / 2 - 15, HEIGHT / 2 - 15, 100, 15, 0xffffff00, system);
        Celestial earth = new Celestial(WIDTH / 2 - 15-100, HEIGHT/2-15, 10, 5, 0xff0000ff, system);
        earth.xa = 0;
        earth.ya = 3.3;
        Celestial neptune = new Celestial(WIDTH / 2 - 15, HEIGHT/2-15-100, 10, 5, 0xff00ffff, system);
        neptune.xa = 3;
        neptune.ya = 0;
        system.setSun(sun);
        system.addCelestial(earth);
        system.addCelestial(neptune);
    }

    public synchronized void start() {
        running = true;
        mainThread = new Thread(this);
        mainThread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            mainThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        init();

        double nsPerTick = 1000000000D/60D;

        long lastTime = System.nanoTime();
        long lastFrameTime = System.currentTimeMillis();
        double unprocessedTime = 0;

        double maxSkipFrames = 10;

        int ticks = 0;
        int frames = 0;

        while(running) {
            long now = System.nanoTime();
            double passedTime = (System.nanoTime() - lastTime)/nsPerTick;
            lastTime = now;

            if(passedTime < -maxSkipFrames) passedTime = -maxSkipFrames;
            if(passedTime > maxSkipFrames) passedTime = maxSkipFrames;

            unprocessedTime+=passedTime;

            boolean shouldRender = false;

            while (unprocessedTime > 1D) {
                unprocessedTime-=1D;
                shouldRender = true;
                tick();
                ticks++;
            }

            if(shouldRender) {
                render(screen);
                frames++;
            }

            if(System.currentTimeMillis() - lastFrameTime > 1000) {
                lastFrameTime+=1000;
                System.out.println("ticks: "+ticks + ", fps: " + frames);
                ticks = 0;
                frames = 0;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            swap();
        }
    }


    public void tick() {
        system.tick();
    }

    public void swap() {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            requestFocus();
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);

        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

        g.dispose();
        bs.show();
    }

    public void render(Screen screen) {
        screen.fill(0xff000000);
        system.render(screen);
    }


    public static void main(String[] args) {
        SolarGame game = new SolarGame();
        JFrame frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(game, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();
    }
}
