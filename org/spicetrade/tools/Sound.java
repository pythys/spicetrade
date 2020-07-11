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

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.spicetrade.Mainframe;

/**
 * @author holjuh
 *  
 */
public class Sound implements Runnable {

    private Thread thread = null;

    AudioInputStream audioInputStream = null;

    AudioFormat sourceFormat = null;

    AudioFormat audioFormat = null;

    SourceDataLine line = null;

    private static final int EXTERNAL_BUFFER_SIZE = 128000;

    byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];

    byte[] bytes;

    boolean loop = false;
    
    public String name = "";

    public boolean playing = false;
    
    public void start(String afile) {        
        start(afile, false);
    }

    public void start(String afile, boolean loop) {
        this.name = afile;
        if (thread == null) {
            thread = new Thread(this, (afile + String.valueOf(System.currentTimeMillis())));

            this.loop = loop;

            try {
                URL url = getClass().getResource(afile);
                audioInputStream = AudioSystem.getAudioInputStream(url.openStream());

                if (audioInputStream == null) {
                    System.out.println("Cannot play sound: " + afile);
                }
                sourceFormat = audioInputStream.getFormat();
                AudioFormat.Encoding targetEncoding = AudioFormat.Encoding.PCM_SIGNED;
                audioInputStream = AudioSystem.getAudioInputStream(targetEncoding, audioInputStream);
                audioFormat = audioInputStream.getFormat();

                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

                line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(audioFormat);
            } catch (LineUnavailableException e) {
                Mainframe mf = Mainframe.me;
                mf.sounds.musicOn = false;               
            } catch (Exception e) {
                e.printStackTrace();
            }
            line.start();

            thread.start();
        }
    }

    public void run() {
        Thread myThread = Thread.currentThread();
        while (thread == myThread)
            try {
                do {
                    playing = true;
                    int nBytesRead = 0;
                    if (Mainframe.DEBUG == 1) System.out.println("Playing: " + name + " and looping is " + loop + " - " + new java.util.Date(System.currentTimeMillis()));
                    while (nBytesRead != -1) {
                        try {
                            nBytesRead = audioInputStream.read(abData, 0, abData.length);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (nBytesRead >= 0) {
                            int nBytesWritten = line.write(abData, 0, nBytesRead);
                        }

                        Thread.sleep(400);
                    }
                    URL url = getClass().getResource(name);
                    audioInputStream = AudioSystem.getAudioInputStream(url.openStream());
                    sourceFormat = audioInputStream.getFormat();
                    AudioFormat.Encoding targetEncoding = AudioFormat.Encoding.PCM_SIGNED;
                    audioInputStream = AudioSystem.getAudioInputStream(targetEncoding, audioInputStream);
                    audioFormat = audioInputStream.getFormat();

                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

                    line = (SourceDataLine) AudioSystem.getLine(info);
                    line.open(audioFormat);
                    line.start();
                } while (loop);
                stop();
            } catch (InterruptedException e) {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }

    public void stop() {
        try {
            loop = false;
            thread = null;
            line.close();
            playing = false;
            System.gc();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}