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

public class Spicebean {

    public String condition;
    public String name;
    public String action;
    public String picture;
    public int x;
    public int y;
    public int width;
    public int height;

    public Spicebean() {
        condition = null;
        name = null;
        action = null;
        picture = null;
        x = 0;
        y = 0;
        width = 0;
        height = 0;
    }

    public void doAction() {
        if (isDoable())
            try {
                if (Mainframe.DEBUG == 1) System.out.println("Doing action: " + action);
                Interpreter bsh = new Interpreter();
                bsh.set("mf", Mainframe.me);
                bsh.eval(Mainframe.me.bshimport + action);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }

    public boolean isActive() {
        if (condition == null || condition.equals("") || condition.equals("res=false;"))
            return false;
        else if (condition.equals("res=true;"))
            return true;
        
        try {
            Interpreter bsh = new Interpreter();
            bsh.set("mf", Mainframe.me);
            bsh.set("res", false);
            bsh.eval(Mainframe.me.bshimport + condition);
            if (Mainframe.DEBUG == 2) System.out.println(condition + " ----- is " + bsh.get("res").toString());
            return getBoolean((Boolean) bsh.get("res"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean isDoable() {
        return (action != null && !action.equals(""));
    }

    private boolean getBoolean(Boolean b) {
        return b.booleanValue();
    }
}
