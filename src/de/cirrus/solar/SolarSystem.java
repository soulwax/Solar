package de.cirrus.solar;

import java.util.ArrayList;
import java.util.List;

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

public class SolarSystem {

    public Celestial sun;
    public static boolean leaveTrails = true;
    public List<Celestial> objects = new ArrayList<>();

    public SolarSystem() {

    }

    public void setSun(Celestial c) {
        this.sun = c;
    }

    public void addCelestial(Celestial c) {
        objects.add(c);
    }

    public void tick() {
        sun.tick();
        objects.stream().filter(c -> !c.removed).forEach(c -> c.tick(sun));
    }

    public void render(Screen screen) {
        sun.render(screen);
        objects.stream().filter(c -> !c.removed).forEach(c -> c.render(screen));
    }
}
