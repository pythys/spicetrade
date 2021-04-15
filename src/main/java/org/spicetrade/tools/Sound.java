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

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.*;

public class Sound implements Runnable {

    public String name = "";
    public volatile boolean playing;

    private boolean loop;
    private Thread thread = null;

    public void start(String afile) {
        start(afile, false);
    }

    public void start(String afile, boolean loop) {
        if (thread == null) {
            thread = new Thread(this, (afile + System.currentTimeMillis()));
            this.name = afile;
            this.loop = loop;
            thread.start();
        }
    }

    public void run() {
        do {
            try {
                URL url = getClass().getResource(this.name);
                AudioInputStream fileIn = AudioSystem.getAudioInputStream(url.openStream());
                if (fileIn == null) { stop(); return; }
                AudioFormat sourceFormat = fileIn.getFormat();
                AudioFormat targetFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        sourceFormat.getSampleRate(),
                        16,
                        sourceFormat.getChannels(),
                        sourceFormat.getChannels() * 2,
                        sourceFormat.getSampleRate(),
                        false);
                AudioInputStream dataIn = AudioSystem.getAudioInputStream(targetFormat, fileIn);
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, targetFormat);
                SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
                if (line == null) { stop(); return; }
                this.playing = true;
                line.open();
                line.start();
                byte[] buffer = new byte[4096];
                int nBytesRead = 0;
                while (nBytesRead != -1 && playing) {
                    nBytesRead = dataIn.read(buffer, 0, buffer.length);
                    if (nBytesRead != -1) {
                        line.write(buffer, 0, nBytesRead);
                    }
                }
                line.drain();
                line.stop();
                line.close();
                dataIn.close();
                fileIn.close();
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                System.out.println("Unable to play audio file: " + this.name);
                stop();
            }
        } while (this.loop);
        stop();
    }

    public void stop() {
        this.loop = false;
        this.playing = false;
    }
}
