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

public class Screen {
    public final int w, h;
    public int[] pixels;

    public Screen(int w, int h, int[] pixels) {
        this.w = w;
        this.h = h;
        this.pixels = pixels;
    }


    public void fill(int col) {
        for (int i = 0, len = pixels.length; i < len; i++){
            if(SolarSystem.leaveTrails && pixels[i] != 0xffffffff) pixels[i] = col;
        }

    }

    public void drawPlanet(int x, int y, int r, int col) {
        int x0 = x - r;
        int y0 = y - r;
        int x1 = x + r;
        int y1 = y + r;

        if(x0 < 0 || y0 < 0 || x1 > w - 1 || y1 > h - 1) return;

        for(int yy = y0; yy <= y1; yy++) {
            for(int xx = x0; xx <= x1; xx++) {
                double xr = xx - x;
                double yr = yy - y;
                double dd = Math.sqrt(xr*xr+yr*yr);
                if(dd < r && pixels[xx+yy*w] != 0xffffffff) pixels[xx+yy*w] = col;
            }
        }
    }

    public void setPixel(int x, int y, int col) {
        if(x < 0 || y < 0 || x >= w || y >= h) return;
        pixels[x+y*w] = col;
    }
}
