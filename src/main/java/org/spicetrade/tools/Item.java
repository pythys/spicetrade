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

package org.spicetrade.tools;

import org.spicetrade.Mainframe;

public class Item extends Spicebean {

    public String id;
    public String description;
    public String help;
    public String helpId;
    public String pictureBig;
    public String pictureStatus;
    public String who;
    public String where;
    public int price;
    public int happiness;
    public int monthlyCost;
    public int health;
    public int force;
    public int culture;
    public int economy;
    public int amount;
    public int random;
    public boolean inventory;

    public Item() {
        name = null;
        id = null;
        description = null;
        help = null;
        helpId = "3320";
        condition = null;
        action = null;
        who = "";
        where = "";
        picture = "/pics/objects/none_50.jpg";
        pictureBig = "/pics/objects/none_100.jpg";
        price = 0;
        happiness = 0;
        monthlyCost = 0;
        health = 0;
        force = 0;
        culture = 0;
        economy = 0;
        amount = 0;
        random = 0;
        inventory = true;
    }

    public boolean here() {
        return here(Mainframe.me.player.place);
    }

    public boolean here(String place) {
        if (where == null || where.equals(""))
            return false;
        else
            return (place.equals(where));
    }

    public String toString() {
        return ("|" + id + ": " + name + "|");
    }
}