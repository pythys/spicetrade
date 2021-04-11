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

import java.util.Hashtable;

import org.spicetrade.Mainframe;

import bsh.Interpreter;

public class Task implements Runnable {

    String action = "";
    int times = 1;
    int counter = 1;
    int delay = 0;
    int delayBefore = 0;
    Interpreter bshint = new Interpreter();
    Thread task = null;
    boolean gotoPlace = false;
    String doAfter = "";
    boolean paused = false;
    Hashtable attr = new Hashtable();

    public Task(String action, int times, boolean gotoPlace, String doAfter, int delay) {
        this(action, times, gotoPlace, doAfter, delay, 0);
    }
    
    public Task(String action, int times, boolean gotoPlace, String doAfter, int delay, int delayBefore) {
        this.action = action;
        this.times = times;
        this.gotoPlace = gotoPlace;
        this.doAfter = doAfter;
        this.delay = delay;
        this.delayBefore = delayBefore;
    }

    public void add(String n) {
        add(n, "");
    }
    
    public void add(String n, String v) {
        attr.put(n, v);
    }
    
    public String get(String n) {
        if(has(n)) return (String)attr.get(n); else return null;
    }

    public int getInt(String n) {
        try {
            if(has(n)) return Integer.parseInt((String)attr.get(n));            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }
    
    public boolean has(String n) {
        return attr.containsKey(n);
    }        
    
    public boolean contains(String n, String v) {
        if (!has(n)) return false;else return (get(n).indexOf(v) != -1);
    }
    
    public void start() {
        this.paused = false;
        if (task == null) {
            task = new Thread(this, action);
            try {
                task.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void run() {
        try {
            Mainframe mf = Mainframe.me;

            Thread myThread = Thread.currentThread();
            
            Thread.sleep(delayBefore);

            if (Mainframe.DEBUG==1) System.out.println("Running thread " + this.task + " (" + myThread.hashCode() + ") - " + System.currentTimeMillis());
            
            while (task == myThread) {
                if (!paused) {
                    String a = action.replaceAll("!counter", String.valueOf(counter++));
                    times--;
                    if (times < 1) {
                        mf.player.chooseFace();
                        a = a.replaceAll("!donep", "true");
                        if (gotoPlace)
                            mf.gotoPlace(mf.player.place);
                        else {
                            bshint.set("mf", mf);
                            bshint.eval(mf.bshimport + a);
                        }

                        if (doAfter != null && !doAfter.equals("")) {
                            bshint.set("mf", mf);
                            bshint.eval(mf.bshimport + doAfter);
                        }
                        this.stop();
                    } else {
                        a = a.replaceAll("!donep", "false");
                        bshint.set("mf", mf);
                        bshint.eval(mf.bshimport + a);
                    }
                }
                Thread.sleep(delay);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void pause() {
        this.paused = true;
    }
    
    public void reset() {
        this.stop();
        this.start();
    }
    
    public void stop() {
        try {
            this.task = null;
            System.gc();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}