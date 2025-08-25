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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

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
    private static Thread mixerThread;

    public static synchronized void init() throws LineUnavailableException {
        if (line != null) {
            return;
        }
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, FORMAT);
        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open();
        line.start();
        mixerThread = new Thread(SoundManager::mixLoop, "SoundMixer");
        mixerThread.start();
    }

    public static void playSound(String file, boolean loop) {
        boolean alreadyPlaying = sounds.stream()
            .anyMatch(s -> !s.finished && s.path.equals(file));
        if (alreadyPlaying) { return; }
        try {
            byte[] pcm = decodeToPCM(file);
            sounds.add(new Sound(file, pcm, loop));
        } catch (Exception e) {
            System.out.println("Unable to decode audio file " + file);
            System.out.println(e.getMessage());
        }
    }

    public static void stopAll() {
        sounds.clear();
    }

    public static synchronized boolean isPlaying(String file) {
        return sounds.stream()
                     .anyMatch(s -> !s.finished && s.path.equals(file));
    }

    private static void mixLoop() {
        byte[] mixBuf = new byte[4096];
        byte[] temp = new byte[4096];
        while (true) {
            Arrays.fill(mixBuf, (byte)0);
            int bytesReadAny = 0;
            for (Sound sound: sounds) {
                int read = sound.read(temp, 0, temp.length);
                if (read > 0) {
                    mixBytes(mixBuf, temp, read);
                    bytesReadAny = Math.max(bytesReadAny, read);
                } else if (sound.finished) {
                    sounds.remove(sound);
                }
            }
            if (bytesReadAny > 0) {
                line.write(mixBuf, 0, bytesReadAny);
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {}
            }
        }
    }

    private static void mixBytes(byte[] mixBuf, byte[] src, int len) {
        for (int i = 0; i < len; i+=2) {
           short acc = (short)(((mixBuf[i+1] << 8) | (mixBuf[i] & 0xFF))
                                + ((src[i+1] << 8) | (src[i] & 0xFF)));
           if (acc > Short.MAX_VALUE) acc = Short.MAX_VALUE;
           if (acc < Short.MIN_VALUE) acc = Short.MIN_VALUE;
           mixBuf[i]   = (byte)(acc & 0xFF);
           mixBuf[i+1] = (byte)((acc >> 8) & 0xFF);
        }
    }

    private static byte[] decodeToPCM(String resourcePath)
            throws IOException, UnsupportedAudioFileException {

        URL url = SoundManager.class.getResource(resourcePath);
        if (url == null) throw new IOException("Resource not found: " + resourcePath);
        AudioInputStream sourceIn = AudioSystem.getAudioInputStream(url);

        AudioFormat baseFormat = sourceIn.getFormat();
        AudioFormat intermediateFmt = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            FORMAT.getSampleRate(),
            FORMAT.getSampleSizeInBits(),
            baseFormat.getChannels(),
            baseFormat.getChannels() * 2,
            FORMAT.getSampleRate(),
            false
        );
        AudioInputStream pcmIn = AudioSystem.getAudioInputStream(intermediateFmt, sourceIn);
        ByteArrayOutputStream monoBaos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int read;
        while ((read = pcmIn.read(buf)) != -1) {
            monoBaos.write(buf, 0, read);
        }
        pcmIn.close();
        sourceIn.close();
        byte[] monoData = monoBaos.toByteArray();
        if (intermediateFmt.getChannels() == 2) {
            return monoData;
        }
        ByteArrayOutputStream stereoBaos = new ByteArrayOutputStream();
        for (int i = 0; i < monoData.length; i += 2) {
            short sample = (short) ((monoData[i] & 0xFF) | (monoData[i+1] << 8));
            stereoBaos.write(monoData[i]);
            stereoBaos.write(monoData[i+1]);
            stereoBaos.write(monoData[i]);
            stereoBaos.write(monoData[i+1]);
        }
        return stereoBaos.toByteArray();
    }

}
