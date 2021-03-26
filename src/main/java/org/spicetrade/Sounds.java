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

import java.util.Enumeration;
import java.util.Hashtable;

import org.spicetrade.tools.Sound;

public class Sounds {

    Hashtable sounds = new Hashtable();

    public boolean musicOn = true;
    
    public String lastMusic = "";
    public String currentMusic = "";
    
    public Sounds() {
        super();
    }

    public void playSound(String file, boolean loop) {
        if (Mainframe.DEBUG == 1) System.out.println("starting to play: " + file + ", loop: " + loop);
        if (!musicOn) return;
        try {
            if (isPlaying(file))
                return;
            else if (has(file))
                sounds.remove(file);
            Sound sound = new Sound();
            sound.start(file, loop);
            sounds.put(file, sound);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void playSound(String file) {
        playSound(file, false);
    }
    
    public void loopSound(String file) {
        playSound(file, true);
    }

    public void playMusic(String file) {
        if (!musicOn) return;
        try {
            if (isPlaying(file))
                return;            
            stopAll();
            playSound(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loopMusic(String file) {
        if (!musicOn) return;
        try {
            if (isPlaying(file))
                return;
            lastMusic = currentMusic;
            currentMusic = file;
            stopAll();
            loopSound(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void stopSound(String file) {
        try {
            if (!isPlaying(file))
                return;
            Sound sound = (Sound) sounds.get(file);
            sound.stop();
            sounds.remove(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stopAll() {
        Sound sound = null;
        Enumeration enum = sounds.elements();
        while (enum.hasMoreElements()) {
            sound = (Sound) enum.nextElement();
            sound.stop();
            sounds.remove(sound.name);
        }
        sounds = new Hashtable();
    }

    public boolean has(String file) {
        return sounds.containsKey(file);
    }
    
    public boolean isPlaying(String file) {
        if(has(file)) {
            Sound sound = (Sound)sounds.get(file);
            return sound.playing;
        } else return false;
    }
}
