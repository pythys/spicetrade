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

public class LogItem {
    public String id;
    public String action;
    public int when;

    public LogItem(String id, String action, int when) {
        if (Mainframe.DEBUG==1) {
            System.out.println("Logging: " + when + " (" + action + "): " + id);
        }
        this.id = id;
        this.action = action;
        this.when = when;
    }

    public String getText() {
        if (id.charAt(0) == '!') {
            return Mainframe.me.dialogs.getText(id.substring(1, 5));
        } else {
            return id;
        }
    }

    public String getDate() {
        int year = (when/360);
        int month = ((when - (360*year)) / 30);
        int day = (when - (30*month) - (360*year));
        return (day + "." + month + "." + year);
    }

    public String toString() {
        return (getDate() + "\u00A7" + getText());
    }
}
