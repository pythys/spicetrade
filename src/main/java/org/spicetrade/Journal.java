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

import java.util.Hashtable;
import java.util.Vector;

import org.spicetrade.tools.Collection;

public class Journal extends Collection {

    Hashtable tasksOpen;

    Hashtable tasksClosed;

    Hashtable timestamp;
    
    // ADD 10.04.2005 -- Want to see the book of life events in the order they happen in
    Vector<String> tasks = new Vector<>();

    public static final String xml = "/data/journal.xml";

    public Journal() {
        super(xml);
        tasksOpen = new Hashtable();
        tasksClosed = new Hashtable();
        timestamp = new Hashtable();
    }

    public void refresh() {
        this.refresh(xml);
    }

    public void open(String task) {
        open(task, "");
    }

    public void open(String task, String value) {
        Mainframe mf = Mainframe.me;

        if (!isDone(task) && !has(task)) {
            mf.sounds.playSound("/music/fx_hit.ogg");
            tasksOpen.put(task, value);
            stamp(task);
        }
    }

    public boolean contains(String task, String value) {
        String compare = get(task);
        if (Mainframe.DEBUG == 1) System.out.println("Checking if task " + task + " contains " + value + " == " + !(compare.indexOf(value) == -1));
        if (compare.indexOf(value) == -1)
            return false;
        else
            return true;
    }

    public String get(String task) {
        if (tasksOpen.containsKey(task))
            return (String) tasksOpen.get(task);
        else if (tasksClosed.containsKey(task))
            return (String) tasksClosed.get(task);
        else
            return "";
    }

    public boolean has(String task) {
        return tasksOpen.containsKey(task);
    }

    public void done(String task) {
        done(task, "");
    }

    public void done(String task, String value) {
        done(task, value, false);
    }

    public void done(String task, String value, boolean replace) {
        Mainframe mf = Mainframe.me;
        if (!isDone(task)) {
            String newValue = value;
            if (!replace)
                newValue = get(task) + value;
            mf.sounds.playSound("/music/fx_famous_ending.ogg");
            // ADD 10.04.2005 -- Want the book of life happenings should be in the order they happen in
            tasks.add(task);
            tasksClosed.put(task, newValue);
            tasksOpen.remove(task);
            stamp(task);
        }
    }

    public boolean isDone(String task) {
        if (Mainframe.DEBUG == 1) System.out.println("Is task " + task + " done? == " + tasksClosed.containsKey(task));
        return tasksClosed.containsKey(task);
    }

    public void remove(String task) {
        if (Mainframe.DEBUG == 1) System.out.println("Removing task: " + task);
        if (has(task)) tasksOpen.remove(task);
    }
    
    public void add(String task, String value) {
        put(task, get(task) + value);
    }

    public void put(String task, String value) {
        if (Mainframe.DEBUG == 1) System.out.println("Putting " + value + " to task " + task);
        // FIX 10.04.2005 -- redundant check for exact value in task
        if (tasksOpen.containsKey(task)) {
            tasksOpen.put(task, value);
            stamp(task);
        } else if (tasksClosed.containsKey(task)) {
            tasksClosed.put(task, value);
            stamp(task);
        }
    }
    
    public String getPicture(String task) {
        return this.getString(task, "Picture");
    }
    
    public void stamp(String task) {
        Mainframe mf = Mainframe.me;
        timestamp.put(task, String.valueOf(mf.player.day + (mf.player.month*30) + (mf.player.year * 360)));
    }
}