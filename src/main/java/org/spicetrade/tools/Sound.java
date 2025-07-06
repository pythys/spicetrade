/* Spice Trade
 * Copyright (C) 2024 pythys
 *
 * Author: Taher Alkhateeb, taher@pythys.com
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

public class Sound {
    final String path;
    final byte[] pcmData;
    int readPos = 0;
    final boolean loop;
    boolean finished = false;

    Sound(String path, byte[] pcmData, boolean loop) {
        this.path = path;
        this.pcmData = pcmData;
        this.loop = loop;
    }

    int read(byte[] dst, int dstOff, int len) {
        if (finished) {
            return -1;
        }
        int available = pcmData.length - readPos;
        int toCopy = Math.min(available, len);
        System.arraycopy(pcmData, readPos, dst, dstOff, toCopy);
        readPos += toCopy;
        if (readPos >= pcmData.length) {
            if (loop) {
                readPos = 0;
            } else {
                finished = true;
            }
        }
        return toCopy;
    }
}
