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

import java.awt.Frame;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.swing.JOptionPane;

import org.spicetrade.Mainframe;

public class Tools {

    public Tools() {
    }

    public boolean askYesNo(Frame f, String s) {
        try {
            int ret = JOptionPane.showConfirmDialog(Mainframe.me, s, "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ret == JOptionPane.YES_OPTION) return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public Image loadImage(Frame f, String s) {
        if (getClass().getResource(s) == null)
            return null;

        Image i = null;
        MediaTracker mt = new MediaTracker(f);
        try {
            i = Toolkit.getDefaultToolkit().getImage(getClass().getResource(s));
            mt.addImage(i, 0);
            mt.waitForID(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return i;
    }

    public String readFile(String f) {
        return readFile(f, true);
    }

    public String readFile(String f, boolean injar) {
        StringBuilder ret = new StringBuilder();
        try {
            int i = 0;
            String line = "";
            InputStreamReader r = null;
            if (injar)
                r = new InputStreamReader(getClass().getResourceAsStream(f), "ISO-8859-1");
            else
                r = new InputStreamReader(new FileInputStream(f), "ISO-8859-1");

            BufferedReader br = new BufferedReader(r);

            while ((line = br.readLine()) != null)
                ret.append(line);

            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret.toString();
    }

    public void writeFile(String f, String s) {
        FileOutputStream out;
        PrintStream p;
        try {
            out = new FileOutputStream(f);
            p = new PrintStream(out, false, "ISO-8859-1");
            p.print(s);
            p.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}