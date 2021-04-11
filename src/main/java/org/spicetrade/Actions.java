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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.util.Iterator;
import java.util.Vector;

import org.spicetrade.tools.Collection;
import org.spicetrade.tools.Drawable;
import org.spicetrade.tools.MapEntry;
import org.spicetrade.tools.Spicebean;

/**
 * @author holjuh
 */
public class Actions extends Collection {

    Mainframe f;

    public static final String xml = "/data/actions.xml";

    public Actions() {
        super(xml);
        f = Mainframe.me;
    }

    public void refresh() {
        this.refresh(xml);
    }

    public void perform(String action, Vector coords, int days, String to, String nicecity, int i, int x, int y, boolean done) {
        try {
            // I'm sure there is a wonderful algorithm that does this in a more clean and elegant way, but no time to fix
            // something that seems to work ok.
            // Anyway, this piece of ugliness calculates the line across the map by taking in the vector with the
            // coordinates, the number of steps (==days) in the line and the current step (==i) and then calculates
            // the lines required to be drawn
            
            int[] start = (int[]) coords.elementAt(0);
            int[] end = (int[]) coords.elementAt(coords.size() - 1);

            double div = days;
            int where = 1;
            int size = coords.size() - 1;

            if (size > 1) {
                // so we have more than one node in the line
                div = (double)days / (double)size;
                start = (int[]) coords.elementAt(0);
                end = (int[]) coords.elementAt(where);

                // I know.. casting to double like this is pretty bad, but before, when I did not do this, I got
                // an overrun in the line when I had more than 10 nodes or so and don't have time to fix and test the
                // incoming parameter types
                while (i > Math.round((double)days / (double)size * (double)where)) {
                    if (++where > size) {
                        where = size;
                        start = (int[]) coords.elementAt(where - 1);
                        end = (int[]) coords.elementAt(where);
                        break;
                    }
                    start = (int[]) coords.elementAt(where - 1);
                    end = (int[]) coords.elementAt(where);
                }
            }

            // then we calculate the length of one step in this particular line
            double stepX = 0;
            double stepY = 0;

            stepX = (double) ((end[0] - start[0]) / div);
            stepY = (double) ((end[1] - start[1]) / div);

            double lineX = (start[0] + (stepX * (i - (where - 1) * div)));
            double lineY = (start[1] + (stepY * (i - (where - 1) * div)));

            // calculating the position of the Abu-icon in front of the line
            int iconX = (int) lineX;
            int iconY = (int) lineY;
            double angle = Math.atan2(end[1] - start[1], end[0] - start[0]) + Math.PI;
            if (angle >= 5.1 && angle < 5.8) {
                if (Mainframe.DEBUG == 2)
                    System.out.println("Travelling: south west");
                iconX = iconX - 15;
                iconY = iconY - 13;
            } else if (angle > 0.24 && angle < 1.02) {
                if (Mainframe.DEBUG == 2)
                    System.out.println("Travelling: north west");
                iconX = iconX - 16;
                iconY = iconY - 14;
            } else if (angle >= 1.02 && angle < 2.03) {
                if (Mainframe.DEBUG == 2)
                    System.out.println("Travelling: north");
                iconX = iconX - 15;
                iconY = iconY - 15;
            } else if (angle >= 2.03 && angle < 2.84) {
                if (Mainframe.DEBUG == 2)
                    System.out.println("Travelling: north east");
                iconX = iconX - 10;
                iconY = iconY - 20;
            } else if (angle >= 2.84 && angle < 3.5) {
                if (Mainframe.DEBUG == 2)
                    System.out.println("Travelling: east");
                iconX = iconX - 10;
                iconY = iconY - 12;
            } else if (angle >= 3.5 && angle < 4.4) {
                if (Mainframe.DEBUG == 2)
                    System.out.println("Travelling: south east");
                iconX = iconX - 16;
                iconY = iconY - 14;
            } else if (angle >= 4.4 && angle < 5.1) {
                if (Mainframe.DEBUG == 2)
                    System.out.println("Travelling: south");
                iconX = iconX - 15;
                iconY = iconY - 15;
            } else {
                if (Mainframe.DEBUG == 2)
                    System.out.println("Travelling: west");
                iconX = iconX - 20;
                iconY = iconY - 10;
            }

            int startX = start[0];
            int startY = start[1];

            // just drawing the panel, no need to draw the glass or the map
            f.isModal = true;
            f.panel.clear();

            // red is for land travel, blue for ocean and white for flying
            Color color = Color.RED;
            if (action.equals(MapEntry.TRANSPORTS[MapEntry.TRANSPORT5]))
                color = new Color(56, 91, 192);
            else if (action.equals(MapEntry.TRANSPORTS[MapEntry.TRANSPORT4]) || action.equals(MapEntry.TRANSPORTS[MapEntry.TRANSPORT6])
                    || action.equals(MapEntry.TRANSPORTS[MapEntry.TRANSPORT7]))
                color = new Color(255, 255, 255);

            // I _really_ should use multilines, I'll see if I have time before the publishing..
            if (where > 1)
                for (int a = 0; a < where - 1; a++)
                    f.objects.add(new Drawable(((int[]) coords.elementAt(a))[0], ((int[]) coords.elementAt(a))[1], ((int[]) coords.elementAt(a + 1))[0],
                            ((int[]) coords.elementAt(a + 1))[1], color, Drawable.LINE));

            // after all the "old" lines (if there were any) were drawn, put in the current line
            f.panel.add(new Drawable(startX, startY, (int) lineX, (int) lineY, color, Drawable.LINE));

            // and the icon
            f.panel.add(new Drawable(iconX, iconY, 0, 0, Drawable.IMAGE, f.getImage("playerIcon")));

            // and of course the movement panel
            perform(action, this.getString(action, "Text") + " to " + nicecity, i, x, y, 200, 300);
            if (i%2 == 0) f.player.nextDay(true);

            f.doLayout();
            f.repaint();

            if (done) {
                // once we are done, move to the appropriate city etc..
                
                if (f.player.transport == MapEntry.TRANSPORT3) {
                    // Caravan is a one way ticket
                    // these kinds of checks should really be refactored straight into the items, and not used here                    
                    f.player.removeItem("10320");
                    f.player.removeItem("10325");
                    f.player.transport = MapEntry.TRANSPORT1;
                }
                Thread.sleep(500);
                f.player.toHomeCountry(end[0], end[1], to, nicecity);
                f.isModal = false;
                f.gotoPlace(to);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void perform(String action, int i, int x, int y, boolean done) {
        perform(action, i, x, y, done, 0);
    }

    public void perform(String action, int i, int x, int y, boolean done, int after) {
        perform(action, i, x, y, done, after, false);
    }

    public void perform(String action, int i, int x, int y, boolean done, int after, boolean fade) {
        try {
            // Regular actions (like drawing something etc...)
            f.isModal = true;
            f.panel.clear();
            perform(action, this.getString(action, "Text"), i, x, y, fade);
            f.doLayout();
            f.repaint();
            if (done) {
                Thread.sleep(1000 + after);
                f.isModal = false;
                f.gotoPlace(f.player.place);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void perform(String action, String text, int i, int x, int y) {
        perform(action, text, i, x, y, false);
    }

    public void perform(String action, String text, int i, int x, int y, boolean fade) {
        perform(action, text, i, x, y, 200, 400, fade);
    }

    public void perform(String action, String text, int i, int x, int y, int width, int height) {
        perform(action, text, i, x, y, width, height, false);
    }

    public void perform(String action, String text, int i, int x, int y, int width, int height, boolean fade) {
        // This is the action/movement panel logic
        f.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Vector actions = new Vector();
        Vector icons = this.getVector(action, "Icons");

        // fade means that we have two pictures and fade from the first to the second
        // this is used when the churches turn into mosques etc..
        if (fade) {
            actions.add((Image) f.tools.loadImage(f, (String) icons.elementAt(0)));
            actions.add((Image) f.tools.loadImage(f, (String) icons.elementAt(1)));
            f.panel.add(new Drawable(x, y, width, height, Drawable.MPANEL, (Image) actions.elementAt(0), (Image) actions.elementAt(1), "", text, null,
                    ((float) (i * 5) / 100), null, null));
        } else {
            // this is the other kind of action, where we loop through the pictures, one by one
            for (int a = 0, b = icons.size(); a < b; a++) {
                actions.add((Image) f.tools.loadImage(f, (String) icons.elementAt(a)));
            }

            int act = i % actions.size();
            if (act > actions.size() - 1)
                act = 0;
            if (text != null && !text.equals("")) {
                // regular panel
                f.panel.add(new Drawable(x, y, width, height, Drawable.MPANEL, (Image) actions.elementAt(act), "", text));
            } else {
                // just show the picture.. this is used for the growing "animations"
                f.panel.add(new Drawable(x, y, 0, 0, Drawable.IMAGE, (Image) actions.elementAt(act)));
            }
        }

        // get the events for the action and see if one of them would like to be triggered
        // again, this "event" mechanism is too simple.. will see if I have the energy to refactor these to a more logical
        // structure
        Vector events = this.getVector(action, "Events");
        Mainframe mf = Mainframe.me;
        mf.nextInt = mf.random.nextInt(100);
        for (Iterator iter = events.iterator(); iter.hasNext();) {
            Spicebean sb = (Spicebean) iter.next();
            if (Mainframe.DEBUG == 1)
                System.out.println("Doing event: " + sb.action + ", condition: " + sb.condition);
            sb.doAction();
            break;
        }
    }
}