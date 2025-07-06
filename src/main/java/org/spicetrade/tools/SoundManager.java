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

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

public class SoundManager {
    private static final int CHANNELS = 2;
    private static final AudioFormat FORMAT = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            44100,
            16,
            CHANNELS,
            CHANNELS * 2,
            44100,
            false);
    private static final List<Sound> sounds = new CopyOnWriteArrayList<>();
    private static SourceDataLine line;

    public static synchronized void init() throws LineUnavailableException {
        if (line != null) {
            return;
        }
        List<Mixer.Info> mixerInfos = Arrays.asList(AudioSystem.getMixerInfo());
        Mixer mixer = AudioSystem.getMixer(mixerInfos.get(mixerInfos.size() - 1));
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, FORMAT);
        line = (SourceDataLine) mixer.getLine(info);
        line.open();
        line.start();
    }

}
