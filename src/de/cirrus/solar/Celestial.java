package de.cirrus.solar;

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

public class Celestial {

    public SolarSystem system;

    public double x, y;
    public double xa, ya;
    public int mass = 0;
    public int radius;
    public int col;
    public boolean removed = false;


    public Celestial(double x, double y, int mass, int radius, int col, SolarSystem system) {
        this.x = x;
        this.y = y;
        this.mass = mass;
        this.radius = radius;
        this.col = col;
        this.system = system;
        xa=0;
        ya=-1;
    }



    public void tick() {

    }

    public void tick(Celestial body) {
        double dx = body.x - x;
        double dy = body.y - y;

        double dist = Math.sqrt(dx*dx+dy*dy);

        if(dist < radius + body.radius) {
            removed = true;
        }
        //normalize dx and dy
        dx /= dist;
        dy /= dist;

        double force = (body.mass * mass)/(dist*dist);

        dx *= force;
        dy *= force;

        xa+=dx;
        ya+=dy;

        x+=xa;
        y+=ya;
    }

    public void render(Screen screen) {
        screen.drawPlanet((int)x, (int)y, radius, col);
        if(SolarSystem.leaveTrails) screen.setPixel((int) x, (int) y, 0xffffffff);
    }
}
