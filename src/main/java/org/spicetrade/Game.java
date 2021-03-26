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

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.UIManager;

/**
 * Spice Trade
 * This is the main class that starts the whole application.
 * 
 * @author Juha Holopainen
 * @version 1.0
 */

public class Game
{

    public Game()
    {
        Frame frame = new Mainframe();
        // I heard that macs have problems with this undecorated setting, will look into that
        frame.setUndecorated(true);        
        frame.setLayout(null);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((d.width - frame.getWidth()) / 2, (d.height - frame.getHeight()) / 2);
        frame.setName("mainframe");
        frame.setResizable(false);
        frame.setTitle("Spice Trade");
        frame.setBackground(Color.lightGray);
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }

        });
        frame.setVisible(true);
        // start at the intro page
        Mainframe.me.gotoPlace("intro");
    }

    public static void main(String args[])
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        new Game();
    }
}
