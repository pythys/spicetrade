/* Spice Trade
 * Copyright (C) 2005 spicetrade.org
 *
 * Author: Juha Holopainen, juhah@spicetrade.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public License
 * as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.spicetrade;

import java.util.Vector;
import java.util.stream.Collectors;

import org.spicetrade.tools.Collection;
import org.spicetrade.tools.MapEntry;

public class Map extends Collection {

    public static final String background = "/pics/maps/world_maps/world.jpg";
    public static final String xml = "/data/map.xml";

    public Map() {
        super(xml);
    }

    public void refresh() {
        this.refresh(xml);
    }

    public String getBackground() {
        return background;
    }

    public Vector<MapEntry> getEntries(String place, int transport, boolean european) {
        Vector<MapEntry> entries = getVector(place);
        return entries.stream().filter(entry ->
                !(!european && entry.european) && entry.getLength(transport) > 0)
                .collect(Collectors.toCollection(Vector::new));
    }
}