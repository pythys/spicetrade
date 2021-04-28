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

import org.spicetrade.tools.Collection;
import org.spicetrade.tools.Drawable;
import org.spicetrade.tools.Spicebean;

import bsh.Interpreter;

public class Places extends Collection {

    public static final String xml = "/data/places.xml";

    public Places() {
        super(xml);
    }

    public void refresh() {
        this.refresh(xml);
    }

    public String getText(String place) {
        return getString(place, "Text");
    }

    public String getTitle(String place) {
        return getString(place, "Name");
    }

    public Vector<Spicebean> getActions(String place) {
        return getVector(place, "Actions");
    }

    public Vector<Spicebean> getPictures(String place) {
        return getVector(place, "Pictures");
    }

    public Vector<Spicebean> getEvents(String place) {
        return getVector(place, "Events");
    }

    public String getBackground(String place) {
        if (Mainframe.DEBUG == 1) {
            System.out.println("Getting background for: " + place);
        }
        try {
            Vector<Spicebean> v = getVector(place, "Backgrounds");
            if (v != null && v.size() > 0) {
                for (Spicebean sb : v) {
                    if (sb.isActive()) {
                        if (Mainframe.DEBUG == 1)
                            System.out.println("Found background: " + sb.picture);
                        return sb.picture;
                    }
                }
            } else {
                return "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public Vector<Drawable> getDrawables(String place) {
        if (Mainframe.DEBUG == 1)
            System.out.println("Getting drawables for place: " + place);
        Vector<Spicebean> drawables = getVector(place, "Drawables");
        Vector<Drawable> res = new Vector<>();
        if (drawables.isEmpty())
            return res;
        try {
            Interpreter bsh = new Interpreter();
            bsh.set("mf", Mainframe.me);

            Spicebean sb;
            Drawable d;

            for (int i = 0, j = drawables.size(); i < j; i++) {
                sb = drawables.elementAt(i);
                bsh.eval(Mainframe.me.bshimport + sb.action);
                d = (Drawable) bsh.get("drawable");
                if (d != null)
                    res.add(d);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }
}