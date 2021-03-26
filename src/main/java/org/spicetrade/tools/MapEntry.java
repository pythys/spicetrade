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

import bsh.Interpreter;


/**
 * @author holjuh
 */
public class MapEntry {

    public String name = "";

    public String nice = "";

    public String description = "";

    public String condition = "";

    public boolean european = false;

    public int x = 0;

    public int y = 0;

    public int width = 20;

    public int height = 20;
    
    public int days1 = 0; // feet
    public String action1 = "";

    public int days2 = 0; // horse

    public int days3 = 0; // caravan

    public int days4 = 0; // flying
    public String action2 = "";

    public int days5 = 0; // boat
    public String action3 = "";

    public static final int TRANSPORT1 = 0;

    public static final int TRANSPORT2 = 1;

    public static final int TRANSPORT3 = 2;

    public static final int TRANSPORT4 = 3;

    public static final int TRANSPORT5 = 4;

    public static final int TRANSPORT6 = 5;

    public static final int TRANSPORT7 = 6;
    
    public static final String[] TRANSPORTS = { "walk", "horse", "caravan", "suleiman", "boat", "borak", "dog" };

    public MapEntry() {
    }

    public String getDescription(int type) {
        return "Travel to " + nice + "\u00A7" + "This will take you " + getLength(type) + " days.";
    }
    
    public int getLength(int type) {
        int length = 0;
        switch (type) {
        case TRANSPORT1: // feet
            length = this.days1;
            break;
        case TRANSPORT2: // horse
            length = this.days2;
            break;
        case TRANSPORT3: // caravan
            length = this.days3;
            break;
        case TRANSPORT4: // suleiman
            length = this.days4;
            break;
        case TRANSPORT5: // boat
            length = this.days5;
            break;
        case TRANSPORT6: // borak
            length = this.days4;
            break;
        case TRANSPORT7: // dog
            length = this.days4;
            break;
        }
        return length;
    }
    
    public String getAction(int type) {
        String action = "";

        int days = getLength(type);
        switch (type) {
        case TRANSPORT1: // feet
            action = action1;
            break;
        case TRANSPORT2: // horse
            action = action1;
            break;
        case TRANSPORT3: // caravan
            action = action1;
            break;
        case TRANSPORT4: // suleiman
            action = action2;
            break;
        case TRANSPORT5: // boat
            action = action3;
            break;
        case TRANSPORT6: // borak
            action = action2;
            break;
        case TRANSPORT7: // dog
            action = action2;
            break;
        }

        action = action.replaceAll("!transport", TRANSPORTS[type]);
        action = action.replaceAll("!days", String.valueOf(days*2));

        return action;
    }

    public boolean isActive(Interpreter bsh) {
        try {
            bsh.eval(Mainframe.me.bshimport + condition);
            return getBoolean((Boolean) bsh.get("res"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean getBoolean(Boolean b) {
        return b.booleanValue();
    }
    
    public String toString() {
        return nice + " (" + this.x + ", " + this.y + ") - feet: " + days1 + ", horse: " + days2 + ", caravan: " + days3 + ", flying: " + days4 + ", boat: " + days5; 
    }
}
