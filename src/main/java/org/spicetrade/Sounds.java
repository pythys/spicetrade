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

import org.spicetrade.tools.SoundManager;

public class Sounds {

    public boolean musicOn = true;
    public String lastMusic = "";
    public String currentMusic = "";

    public Sounds() {
        try {
            SoundManager.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void playSound(String file) {
        SoundManager.playSound(file, false);
    }

    public void loopSound(String file) {
        SoundManager.playSound(file, true);
    }

    public void playMusic(String file) {
        if (!musicOn) { return; }
        if (currentMusic != file) {
            playSound(file);
            currentMusic = file;
        }
    }

    public void loopMusic(String file) {
        if (!musicOn) { return; }
        if (currentMusic != file) {
            lastMusic = currentMusic;
            currentMusic = file;
            loopSound(file);
        }
    }

    public void stopAll() {
        SoundManager.stopAll();
    }
}
