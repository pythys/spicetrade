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

import bsh.Interpreter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.spicetrade.tools.Drawable;
import org.spicetrade.tools.Item;
import org.spicetrade.tools.LogItem;
import org.spicetrade.tools.MapEntry;
import org.spicetrade.tools.Settings;
import org.spicetrade.tools.Spicebean;
import org.spicetrade.tools.Task;
import org.spicetrade.tools.Tools;

/**
 * Spice Trade The frame for the application, essentially this runs the whole
 * show.
 * 
 * There are groups of functions, the "goto"-functions serve as the entry points
 * for various elements in the game, ie. gotoPlace renders a place for the
 * player, gotoCity starts moving the player towards a designated city etc.
 * 
 * The "goto"-functions use the "paint"-functions, which contain the UI/layout
 * logic.
 * 
 * Then there are the "doAction"-functions that trigger a new thread that starts
 * to do something in the game world, ie. grow a plant, show the movement of the
 * player on the map while going to a new city and so on.
 * 
 * Then of course there are a bunch of helper functions, like getImage, load,
 * save. The rB() function is used in a lot of scripts, since ' <' and '>'
 * characters are _not_ fun to write when they are stored in XML files :-/
 * 
 * @author Juha Holopainen
 * @version 1.0
 */

public class Mainframe extends Frame {

    public Interpreter bsh;
    public XStream xstream;
    public Player player;
    public Dialogs dialogs;
    public Places places;
    public Market market;
    public Sounds sounds;
    public Tools tools;
    public Actions actions;
    public Armies armies;
    public Traders traders;
    public Task timer2;
    public Map map;
    public Hashtable images;
    public Vector objects;
    public Vector panel;
    public Vector glass;
    public Font font;
    public Font fontBold;
    public boolean isModal;
    public int converter;
    public boolean isStatus;
    public boolean showMapHomeCity;
    public boolean showMapGlobe;
    public boolean showStatus;
    public boolean idle;
    public Drawable flyover;
    public String dialog;
    public String bshimport;
    public Random random;
    public int nextInt;
    public String doActionOnEntering;
    public Timer timer = null;
    public Image offscreen;
    public Graphics bg;

    public static Mainframe me = null;
    public static final int DEBUG = 0; // DEBUG = 1; for verbose mode

    public Mainframe() {
        bsh = new Interpreter();
        bshimport = "import org.spicetrade.*;\nimport org.spicetrade.tools.*;\n";
        xstream = new XStream();
        XStream.setupDefaultSecurity(xstream);
        xstream.addPermission(AnyTypePermission.ANY);
        tools = new Tools();
        objects = new Vector();
        panel = new Vector();
        glass = new Vector();
        random = new Random();
        nextInt = random.nextInt(100);
        images = new Hashtable();
        isModal = false;
        converter = 0;
        isStatus = false;
        showStatus = false;
        showMapGlobe = false;
        idle = false;
        flyover = null;
        doActionOnEntering = "";
        dialog = "";
        initialize();
    }

    public void initialize() {
        try {
            // bootstrap the game world
            if (Mainframe.DEBUG == 1)
                System.out.println("Initializing Mainframe " + System.currentTimeMillis());
            me = this;
            dialogs = new Dialogs();
            places = new Places();
            market = new Market();
            sounds = new Sounds();
            sounds.musicOn = false;
            actions = new Actions();
            map = new Map();
            player = new Player();
            armies = new Armies();
            traders = new Traders();
            player.journal.open("field2");
            bsh.setOut(System.out);
            bsh.set("mf", this);
            bsh.set("res", false);
            setBounds(0, 0, 1024, 768);
            // caching a bunch of stock pictures that we use all the time
            images.put("notavailable", tools.loadImage(this, "/pics/notavailable.gif"));
            setIconImage(getImage("/pics/navigation/icons/icon.gif"));
            getImage("controls", "/pics/navigation/icons/controls.png", true);
            getImage("questionMark", "/pics/navigation/icons/question_mark.png", true);
            getImage("localMap", "/pics/navigation/icons/icon_map.png", true);
            getImage("globalMap", "/pics/navigation/icons/icon_globe.png", true);
            getImage("statusFace", player.statusFace, true);
            getImage("statusBar", "/pics/navigation/icons/statusbar.jpg", true);
            getImage("statusHashish", "/pics/navigation/icons/hashish_addiction.png", true);
            getImage("statusOpium", "/pics/navigation/icons/opium_addiction.png", true);
            getImage("statusPlague", "/pics/navigation/icons/health.png", true);
            getImage("playerIcon", "/pics/navigation/icons/abusmall.png", true);            
            getImage("logoIntro", "/pics/navigation/icons/logos/logo_intro.png", true);
            getImage("logoEvil", "/pics/navigation/icons/logos/logo_bad.png", true);
            getImage("logoAmulet", "/pics/navigation/icons/logos/logo_fatima.png", true);
            getImage("logoDeath", "/pics/navigation/icons/logos/logo_dead.png", true);
            getImage("logoDeath2", "/pics/navigation/icons/logos/logo_invert.png", true);
            getImage("logoFatima", "/pics/navigation/icons/logos/logo_fatima.png", true);
            getImage("logoHashish", "/pics/navigation/icons/logos/logo_hashish.png", true);
            getImage("logoOpium", "/pics/navigation/icons/logos/logo_opium.png", true);
            getImage("logoPlague", "/pics/navigation/icons/logos/logo_red_crescent.png", true);
            getImage("abu", "/pics/navigation/characters/abu.png", true);
            getImage("abuSoldier", "/pics/objects/abu_fighting_150.png", true);
            getImage("pixel", "/pics/pixel.gif", true);
            loadSettings();
            font = new Font("Arial", 0, 14);
            fontBold = new Font("Arial", 1, 14);
            addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
                    this_mouseClicked(e);
                }

            });
            addMouseMotionListener(new MouseMotionAdapter() {

                public void mouseMoved(MouseEvent e) {
                    this_mouseMoved(e);
                }

            });
            addWindowListener(new WindowAdapter() {

                public void windowClosed(WindowEvent e) {
                    this_windowClosed(e);
                }

            });
            sounds.musicOn = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (Mainframe.DEBUG == 1)
            System.out.println("Done initializing Mainframe " + System.currentTimeMillis());
    }

    public void paint(Graphics g) {
        // the paint routine is pretty simple, we just loop through the drawable
        // items
        // and draw them in the buffer. once we are done, the buffer is drawn to
        // the screen

        if (Mainframe.DEBUG == 2)
            System.out.println("Painting screen " + System.currentTimeMillis());
        try {
            if (offscreen == null) {
                // initialize buffer
                g.setFont(font);
                offscreen = createImage(getWidth(), getHeight());
                bg = offscreen.getGraphics();
            }

            // clear buffer
            bg.setColor(Color.darkGray);
            bg.fillRect(0, 0, 2000, 2000);

            // draw base objects
            for (int i = 0, j = objects.size(); i < j; i++)
                ((Drawable) objects.elementAt(i)).draw(bg);

            // draw modal "dialog"
            if (isModal)
                for (int i = 0, j = panel.size(); i < j; i++)
                    ((Drawable) panel.elementAt(i)).draw(bg);

            // draw status etc..
            for (int i = 0, j = glass.size(); i < j; i++)
                ((Drawable) glass.elementAt(i)).draw(bg);

            // draw flyover
            if (flyover != null)
                flyover.draw(bg);

            // draw buffer to screen
            g.drawImage(offscreen, 0, 0, this);
        } catch (Exception ex) {
            // once in a while the drawable vectors are out of sync and we don't
            // want to
            // show that error message to the player, since it does not matter,
            // everything
            // just gets drawn again, if there was a problem
            if (Mainframe.DEBUG == 1)
                ex.printStackTrace();
        }
        if (Mainframe.DEBUG == 2)
            System.out.println("Done painting screen " + System.currentTimeMillis());
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void doAction(String action) {
        doAction(action, this.getWidth() - 300, "");
    }

    public void doAction(String action, String doAfter) {
        doAction(action, this.getWidth() - 300, doAfter);
    }

    public void doAction(String action, int x, String doAfter) {
        doAction(action, x, 20, 20, 750, 20, doAfter);
    }

    public void doAction(String action, int x, int y, int times, int delay, String doAfter) {
        doAction(action, x, y, times, delay, 0, doAfter);
    }

    public void doAction(String action, int x, int y, int times, int delay, int after) {
        doAction(action, x, y, times, delay, after, "");
    }

    public void doAction(String action, int x, int y, int times, int delay, int after, String doAfter) {
        doAction(action, x, y, times, delay, after, doAfter, false);
    }

    public void doAction(String action, int x, int y, int times, int delay, int after, String doAfter, boolean fade) {
        // this ia a monstrosity that should be cleaned up, but too much script
        // already depends on it..
        if (Mainframe.DEBUG == 1)
            System.out.println("doAction: " + action + ", doAfter: " + doAfter);
        isModal = true;
        String timerAction = "mf.actions.perform(\"" + action + "\", !counter, " + x + ", " + y + ", !donep, " + after + ", " + fade + ");";
        //createTimer(timerAction, 1000, delay, times, doAfter); // this was
        // the old timer-functionality that is no longer used
        createTask(timerAction, 1000, delay, times, doAfter);
    }

    public void gotoMap() {
        // entering the world map
        if (Mainframe.DEBUG == 1)
            System.out.println("gotoMap " + System.currentTimeMillis());
        System.gc();
        sounds.loopMusic("/music/16_world_horizon.ogg");
        player.place = "map";
        player.nicecity = "Map of the world";
        isModal = false;
        flyover = null;
        panel.clear();
        objects.clear();
        glass.clear();
        paintMap(true);
        paintGlass();
        doLayout();
        repaint();

        // again, these kinds of "game rules" should be refactored out of the
        // game engine core
        player.journal.done("permit1");
        player.journal.open("permit5");

        // if we have agricultural produce, don't let the player get out of
        // Baghdad
        if ((player.hasItem("10100") || player.hasItem("10110") || player.hasItem("10120") || player.hasItem("10130") || player.hasItem("10200")
                || player.hasItem("10210") || player.hasItem("10220") || player.hasItem("10230"))
                && !player.hasItem("10640"))
            gotoDialog("1680");

        if (Mainframe.DEBUG == 1)
            System.out.println("gotoMap done " + System.currentTimeMillis());
    }

    public void gotoPlace(String to) {
        if (Mainframe.DEBUG == 1)
            System.out.println("gotoPlace " + System.currentTimeMillis());
        if (!doActionOnEntering.equals("")) {
            try {
                // if there was something to run once entering a place, do it
                // now
                String action = doActionOnEntering;
                doActionOnEntering = "";
                player.place = to;
                bsh.eval(bshimport + action);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else
            gotoPlace(to, false);
        if (Mainframe.DEBUG == 1)
            System.out.println("gotoPlace done " + System.currentTimeMillis());
    }

    public void gotoPlace(String to, boolean modal) {
        // entering a place
        if (Mainframe.DEBUG == 1)
            System.out.println("gotoPlace(to, modal) " + System.currentTimeMillis());
        if (!to.equals(player.place) && !to.equals("menu") && !to.equals("settings") && !to.equals("intro") && !to.equals("intro2") && !to.equals("credits")) {
            player.lastPlace = player.place;
            nextInt = random.nextInt(100);
        }

        // CHANGE 26.4.2005 support for full screen mode
        if (player.fullScreen && GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getFullScreenWindow() == null) GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
        if (!player.fullScreen && GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getFullScreenWindow() != null) GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);        
        
        isModal = modal;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        if (to.equals("map")) {
            player.place = to;
            gotoMap();
        } else {
            player.nicecity = places.getTitle(to);
            String old = player.place;
            player.place = to;
            flyover = null;
            panel.clear();
            objects.clear();
            glass.clear();
            paintPlace(to);
            paintGlass();
            doLayout();
            repaint();

            // the "events" in a place are triggered "before" entering the
            // place, but after
            // drawing. so it's sort of like pre-enter place event, but
            // post-draw.. if we would
            // have a true event mechanism, that is
            player.place = old;
            Vector events = places.getEvents(to);
            if (events != null && events.size() > 0) {
                for (int i = 0, j = events.size(); i < j; i++) {
                    Spicebean sb = (Spicebean) events.elementAt(i);
                    // I love BeanShell ;-)
                    sb.doAction();
                }
            }
            player.place = to;
        }
        if (Mainframe.DEBUG == 1)
            System.out.println("gotoPlace(to, modal) " + System.currentTimeMillis());
    }

    public void gotoCity(String to, String action, int xcity, int ycity, int days) {
        // travelling to a new city
        if (Mainframe.DEBUG == 1)
            System.out.println("gotoCity " + to + " (" + action + "): " + System.currentTimeMillis());
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        if (to.equals(player.city)) {
            player.nicecity = to;
            gotoPlace(to);
        } else {
            objects.clear();
            paintMap(false);
            player.to(xcity, ycity);
            //createTimer(action, 1000, 200, days);
            // evevntually this is done using the Actions class, which is run by
            // the Task threading thingy
            createTask(action, 1000, 200, days);
        }
        if (Mainframe.DEBUG == 1)
            System.out.println("gotoCity " + to + " (" + action + ") done: " + System.currentTimeMillis());
    }

    public void gotoDialog(String to) {
        gotoDialog(to, 0, 0);
    }

    public void gotoDialog(String to, int x, int y) {
        // entering a dialog
        player.addLog("!" + to, "mf.gotoDialog(\"" + to + "\", " + x + ", " + y + ");", player.day + player.month * 30 + player.year * 360);
        if (Mainframe.DEBUG == 1)
            System.out.println("gotoDialog " + to + ": " + System.currentTimeMillis());
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        dialog = to;
        isModal = true;
        flyover = null;
        panel.clear();
        objects.clear();
        if (player.place.equals("map"))
            paintMap(true);
        else
            paintPlace();
        paintDialog(x, y);
        doLayout();
        repaint();
        nextInt = random.nextInt(100);
        if (Mainframe.DEBUG == 1)
            System.out.println("gotoDialog " + to + " done: " + System.currentTimeMillis());
    }

    public void gotoStatus(int state, String id, int page, int from, int helpMore) {
        // entering the status dialog
        if (Mainframe.DEBUG == 1)
            System.out.println("gotoStatus " + state + ": " + System.currentTimeMillis());
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        isModal = true;
        flyover = null;
        panel.clear();
        objects.clear();

        if (player.hasItem("11500") && player.hasItem("11510") && player.hasItem("11520") && player.hasItem("11530"))
            player.chooseFace("14");

        if (player.place.equals("map"))
            paintMap(true);
        else
            paintPlace();
        paintStatus(state, id, page, from, helpMore);
        doLayout();
        repaint();

        String[] cities = { "!Baghdad", "!Najaf", "!Anjudan", "!Konya", "!Al-ladhiqiyah", "!Baku", "!Constantinople" };
        if (player.hasItem("11500") && player.hasItem("11510") && player.hasItem("11520") && player.hasItem("11530")) {
            // if we have four map pieces then open a new "find the grave with
            // treasures" quest
            // this should not really be in the game engine
            int which = 0;
            if (rB(0, 14))
                which = 0;
            if (rB(14, 28))
                which = 1;
            if (rB(28, 42))
                which = 2;
            if (rB(42, 56))
                which = 3;
            if (rB(56, 70))
                which = 4;
            if (rB(75, 90))
                which = 5;
            if (rB(90, 100))
                which = 6;

            if (!player.journal.isDone("grave1"))
                player.journal.open("grave1", cities[which]);
            else if (!player.journal.isDone("grave2"))
                player.journal.open("grave2", cities[which]);
            else if (!player.journal.isDone("grave3"))
                player.journal.open("grave3", cities[which]);
            else if (!player.journal.isDone("grave4"))
                player.journal.open("grave4", cities[which]);
            else if (!player.journal.isDone("grave5"))
                player.journal.open("grave5", cities[which]);
            else if (!player.journal.isDone("grave6"))
                player.journal.open("grave6", cities[which]);
            else if (!player.journal.isDone("grave7"))
                player.journal.open("grave7", cities[which]);

            player.removeItem("11500");
            player.removeItem("11510");
            player.removeItem("11520");
            player.removeItem("11530");
        }

        if (Mainframe.DEBUG == 1)
            System.out.println("gotoStatus " + state + " done: " + System.currentTimeMillis());
    }

    public void gotoBattle(String who) {
        gotoBattle(who, 1, 0, armies.getHealth(who), "");
    }

    public void gotoBattle(String who, int round, int weapon, int enemyMight, String endTask) {
        // entering the battle dialog
        sounds.loopMusic("/music/10_its_time.ogg");
        if (Mainframe.DEBUG == 1)
            System.out.println("gotoBattle " + who + ": " + System.currentTimeMillis());
        nextInt = random.nextInt(100);
        isModal = true;
        player.inBattle = true;
        panel.clear();
//        objects.clear();					// FIX 29.4.2005 No need to repaint the background
        player.chooseFace("16");
/*
 * FIX 29.4.2005 No need to repaint the background
 *      if (player.place.equals("map"))
 *          paintMap(true);
 *      else
 *          paintPlace();
 */
        paintBattle(who, round, weapon, enemyMight, endTask);
        doLayout();
        repaint();
        if (Mainframe.DEBUG == 1)
            System.out.println("gotoBattle " + who + " done: " + System.currentTimeMillis());
    }

    public void gotoDiary() {
        gotoDiary(0);
    }

    public void gotoDiary(int page) {
        // this is the end of the game "book of life"
        isModal = true;
        panel.clear();
        objects.clear();
        paintPlace();
        paintDiary(page);
        doLayout();
        repaint();
    }

    public void gotoShop(String who) {
        gotoShop(who, 0, "", "", 0, 0, 0);
    }

    public void gotoShop(String who, int state, String id, String name, int price, int pageLeft, int pageRight) {
        // entering a shop dialog
        if (Mainframe.DEBUG == 1)
            System.out.println("gotoShop " + who + ": " + System.currentTimeMillis());
        nextInt = random.nextInt(100);
        isModal = true;
        panel.clear();
//        objects.clear();				// FIX 29.4.2005 No need to repaint the background
        player.chooseFace("02");
/*
 * FIX 29.4.2005 No need to repaint the background
 *      if (player.place.equals("map"))
 *          paintMap(true);
 *      else
 *          paintPlace();
 */
        paintShop(who, state, id, name, price, pageLeft, pageRight);
        doLayout();
        repaint();
        if (Mainframe.DEBUG == 1)
            System.out.println("gotoShop " + who + " done: " + System.currentTimeMillis());
    }

    public void gotoDeath(String famousLastWords, int how) {
        // game over, you died
        // how == 0: regular death, of age etc
        // how == 1: died gloriously in battle
        // how == 2: died shamefully in battle
        // how == 3: died of disease or poisoning
        player.inGameOver = true;
        isModal = true;
        player.deathType = how;
        player.lastWords = famousLastWords;
        panel.clear();
        objects.clear();
        if (player.place.equals("map"))
            paintMap(true);
        else
            paintPlace();
        player.logo = "logoDeath";
        paintStatus(5, "", 0, 0, 0);
        doLayout();
        repaint();
    }

    public void paintShop(String who, int state, String id, String name, int price, int pageLeft, int pageRight) {
        int x = 150;
        int y = 100;
        int width = 760;
        int height = 500;
        boolean buy = false;
        boolean sell = false;
        String transaction = "";
        int per = 9;

        Font statusFontBold = new Font("Arial", 1, 12);
        Font statusFont = new Font("Arial", 0, 10);

        panel.add(new Drawable(x, y, width, height, Drawable.BPANEL, getImage("logoIntro"), "Trading with " + traders.getDisplay(who)));

        panel.add(new Drawable(x + 10, y + 10, 0, 0, Drawable.IMAGE, getImage(traders.getString(who, "Picture"))));
        panel.add(new Drawable(x + width - 110, y + 10, 0, 0, Drawable.IMAGE, getImage("abu")));

        panel.add(new Drawable(745, 541, 120, 20, Drawable.LABEL, "Money:", statusFontBold, Color.BLACK));
        panel.add(new Drawable(793, 548, 200, 20, Drawable.LABEL, player.money + " silver dirhams", statusFont, Color.BLACK));

        // first we need to know what items we can buy and sell
        Vector buyItems = traders.getBuyItems(who);
        Vector sellItems = traders.getSellItems(who);

        if (Mainframe.DEBUG == 1)
            System.out.println("Inventory: " + player.items.toString());
        if (Mainframe.DEBUG == 1)
            System.out.println("Buy items: " + buyItems.toString());
        if (Mainframe.DEBUG == 1)
            System.out.println("Sell items: " + sellItems.toString());

        int p = 0;

        x = 200;
        y = 250;
        int counter = 0;
        Image image = null;

        // first we paint the items we can buy..
        if (buyItems.size() > per) {
            if (pageLeft > 0)
                panel.add(new Drawable(160, 219, 80, 20, Drawable.LABEL, "<< Previous", "mf.gotoShop(\"" + who + "\", " + state + ", \"" + id + "\", \"" + name
                        + "\", " + price + ", " + (pageLeft - 1) + ", " + pageRight + ");", "Goto previous page"));

            String strPage = "";
            int i = 0;
            for (int j = (int) ((buyItems.size() - 1) / per); i <= j; i++) {
                if (i == j)
                    strPage = String.valueOf(i + 1);
                else
                    strPage = String.valueOf(i + 1) + ",";
                if (pageLeft == i)
                    panel.add(new Drawable(260 + (i * 20), 219, 18, 20, Drawable.LABEL, strPage, "mf.gotoShop(\"" + who + "\", " + state + ", \"" + id
                            + "\", \"" + name + "\", " + price + ", " + String.valueOf(i) + ", " + pageRight + ");", "Goto page " + String.valueOf(i + 1),
                            fontBold, Color.RED));
                else
                    panel.add(new Drawable(260 + (i * 20), 219, 18, 20, Drawable.LABEL, strPage, "mf.gotoShop(\"" + who + "\", " + state + ", \"" + id
                            + "\", \"" + name + "\", " + price + ", " + String.valueOf(i) + ", " + pageRight + ");", "Goto page " + String.valueOf(i + 1)));
            }

            if (pageLeft < (int) ((buyItems.size() - 1) / per))
                panel.add(new Drawable(260 + (i * 20), 219, 80, 20, Drawable.LABEL, "Next >>", "mf.gotoShop(\"" + who + "\", " + state + ", \"" + id + "\", \""
                        + name + "\", " + price + ", " + (pageLeft + 1) + ", " + pageRight + ");", "Goto next page"));
        }

        for (Iterator iter = buyItems.iterator(); iter.hasNext();) {
            counter++;
            Item item = (Item) iter.next();
            if (counter > (pageLeft * per) && (counter <= (pageLeft * per + per) && (counter < buyItems.size() + 1))) {
                p = traders.getBuyPrice(who, item.id);
                image = getImage(item.picture);
                panel.add(new Drawable(x + (47 - (image.getWidth(this) / 2)), y + (50 - (image.getHeight(this) / 2)), 70, 70, Drawable.IMAGE, image,
                        "mf.gotoShop(\"" + who + "\", 1, \"" + item.id + "\", \"" + item.name + "\",  " + p + ", " + pageLeft + ", " + pageRight + ");", "Buy "
                                + item.name + "\u00A7" + item.description + "\u00A7" + "Price: " + p + " silver dr"));
                y += 100;

                if (y > 510) {
                    x += 94;
                    y = 250;
                }
            }
        }

        x = 550;
        y = 250;
        counter = 0;

        // .. and then the items we can sell
        if (sellItems.size() > per) {
            String strPage = "";
            int i = 0;
            int j = 0;
            for (j = (int) ((sellItems.size() - 1) / per); i <= j; i++) {
                if (i == j)
                    strPage = String.valueOf(i + 1);
                else
                    strPage = String.valueOf(i + 1) + ",";
                if (pageRight == i)
                    panel.add(new Drawable(910 - (j * 20 + 80) + (i * 20), 219, 18, 20, Drawable.LABEL, strPage, "mf.gotoShop(\"" + who + "\", " + state
                            + ", \"" + id + "\", \"" + name + "\", " + price + ", " + pageLeft + ", " + String.valueOf(i) + ");", "Goto page "
                            + String.valueOf(i + 1), fontBold, Color.RED));
                else
                    panel.add(new Drawable(910 - (j * 20 + 80) + (i * 20), 219, 18, 20, Drawable.LABEL, strPage, "mf.gotoShop(\"" + who + "\", " + state
                            + ", \"" + id + "\", \"" + name + "\", " + price + ", " + pageLeft + ", " + String.valueOf(i) + ");", "Goto page "
                            + String.valueOf(i + 1)));
            }

            if (pageRight > 0)
                panel.add(new Drawable(910 - (j * 20 + 180), 219, 80, 20, Drawable.LABEL, "<< Previous", "mf.gotoShop(\"" + who + "\", " + state + ", \"" + id
                        + "\", \"" + name + "\", " + price + ", " + pageLeft + ", " + (pageRight - 1) + ");", "Goto previous page"));

            if (pageRight < (int) ((sellItems.size() - 1) / per))
                panel.add(new Drawable(910 - (j * 20 + 80) + (i * 20), 219, 80, 20, Drawable.LABEL, "Next >>", "mf.gotoShop(\"" + who + "\", " + state + ", \""
                        + id + "\", \"" + name + "\", " + price + ", " + pageLeft + ", " + (pageRight + 1) + ");", "Goto next page"));
        }

        for (Iterator iter = sellItems.iterator(); iter.hasNext();) {
            counter++;
            Item item = (Item) iter.next();
            if (counter > (pageRight * per) && (counter <= (pageRight * per + per) && counter < sellItems.size() + 1)) {
                p = traders.getSellPrice(who, item.id);
                image = getImage(item.picture);
                panel.add(new Drawable(x + (47 - (image.getWidth(this) / 2)), y + (50 - (image.getHeight(this) / 2)), 70, 70, Drawable.IMAGE, image,
                        "mf.gotoShop(\"" + who + "\", 2, \"" + item.id + "\", \"" + item.name + "\",  " + p + ", " + pageLeft + ", " + pageRight + ");",
                        "Sell " + item.name + "\u00A7" + item.description + "\u00A7" + "Price: " + p + " silver dr"));
                y += 100;

                if (y > 510) {
                    x += 94;
                    y = 250;
                }
            }
        }

        // Buy, sell, close labels
        if (state == 1) {
            buy = true;
            if (player.hasSold(id))
                transaction = "mf.player.buyItem(\"" + id + "\", " + price + ", \"" + who + "\");mf.gotoShop(\"" + who + "\", 0, \"\", \"\", 0, " + pageLeft
                        + ", " + pageRight + ");";
            else
                transaction = "mf.player.buyItem(\"" + id + "\", " + price + ");mf.gotoShop(\"" + who + "\", 0, \"\", \"\", 0, " + pageLeft + ", " + pageRight
                        + ");";

            // movement items
            if (id.equals("10310") || id.equals("10320") || id.equals("10330") || id.equals("10340") || id.equals("10350") || id.equals("10360"))
                transaction = "mf.player.buyItem(\"" + id.substring(0, 4) + "5\", 0);" + transaction;

            // agricultural items
            if (id.equals("10000") || id.equals("10050") || id.equals("10720"))
                transaction = "mf.player.removeItem(\"10000\");mf.player.removeItem(\"10050\");mf.player.removeItem(\"10720\");" + transaction;
        } else if (state == 2) {
            sell = true;
            if (id.equals("10100") || id.equals("10110") || id.equals("10120") || id.equals("10130") || id.equals("10200") || id.equals("10210")
                    || id.equals("10220") || id.equals("10230") || id.equals("10310") || id.equals("10320") || id.equals("10330") || id.equals("10340")
                    || id.equals("10350") || id.equals("10360"))
                transaction = "mf.player.sellItem(\"" + id + "\", " + price + ");mf.gotoShop(\"" + who + "\", 0, \"\", \"\", 0, " + pageLeft + ", " + pageRight
                        + ");";
            else
                transaction = "mf.player.sellItem(\"" + id + "\", " + price + ", \"" + who + "\");mf.gotoShop(\"" + who + "\", 0, \"\", \"\", 0, " + pageLeft
                        + ", " + pageRight + ");";

            // movement
            if (id.equals("10310") || id.equals("10320") || id.equals("10330") || id.equals("10340") || id.equals("10350") || id.equals("10360"))
                transaction = "mf.player.removeItem(\"" + id.substring(0, 4) + "5\");" + transaction;

            // food
            if (id.equals("10050") || id.equals("10720"))
                transaction = "mf.player.buyItem(\"10000\");" + transaction;
        }

        Drawable d = null;
        if (buy && player.money >= price)
            d = new Drawable(390, 563, 300, 20, Drawable.LABEL, "Buy " + name + " (Price: " + price + ")", transaction);
        else if (buy)
            d = new Drawable(390, 563, 300, 20, Drawable.LABEL, "You cannot afford " + name, "res=false;");
        if (sell)
            d = new Drawable(390, 563, 300, 20, Drawable.LABEL, "Sell " + name + " (Price: " + price + ")", transaction);
        if (d != null) {
            d.x = (140 + width / 2) - (d.textWidth / 2);
            panel.add(d);
        }
        panel.add(new Drawable(850, 570, 80, 20, Drawable.LABEL, "Close", "mf.player.chooseFace();mf.gotoPlace(mf.player.place);"));
    }

    public void paintBattle(String who, int turn, int weapon, int enemyMight, String endTask) {
        try {
            int x = 150;
            int y = 100;
            int width = 760;
            int height = 500;
            boolean end = false;

            String endDo = "mf.gotoPlace(mf.player.place);";

            String abuLog = "Abu's actions:\u00A7";
            String enemyLog = "Actions for " + armies.getName(who) + ":\u00A7";

            panel.add(new Drawable(x, y, width, height, Drawable.BPANEL, getImage("logoIntro"), "Fighting against " + armies.getName(who) + " starting turn: "
                    + turn));

            panel.add(new Drawable(x + 10, y + 10, 0, 0, Drawable.IMAGE, getImage(armies.getString(who, "Picture"))));
            panel.add(new Drawable(x + width - 110, y + 10, 0, 0, Drawable.IMAGE, getImage("abu")));
            panel.add(new Drawable(x + 50, y + 150, 0, 0, Drawable.IMAGE, getImage(armies.getString(who, "Soldier"))));
            panel.add(new Drawable(x + width - 150, y + 150, 0, 0, Drawable.IMAGE, getImage("abuSoldier")));

            int abu = (int) player.health;
            int enemy = enemyMight;

            Item item = new Item();

            if (player.hasItem("13000")) {
                item = market.getItem("13000");
                enemy -= item.force - (nextInt / 10);
                abuLog += "\u00A7- Your sword swings into action";
            } else if (player.hasItem("13010")){
                item = market.getItem("13010");
                enemy -= item.force - (nextInt / 10);
                abuLog += "\u00A7- You struck with the dagger";
            }

            switch (weapon) {
            case 1: // dagger
                item = market.getItem("13010");
                enemy -= item.force - (nextInt / 10);
                abuLog += "\u00A7- You struck with the dagger";
                break;
            case 2: // sword
                item = market.getItem("13000");
                enemy -= item.force - (nextInt / 10);
                abuLog += "\u00A7- Your sword swings into action";
                break;
            case 3: // djinnies
                enemy -= 50 - (nextInt / 10);
                abuLog += "\u00A7- The djinnies appear and attack the enemy";
                break;
            }

            if (Mainframe.DEBUG == 1)
                System.out.println("abu health: " + abu + "\nabu force: " + player.force + "\nenemy health: " + enemy + "\nenemy force: "
                        + armies.getForce(who) + "\n(nextInt: + " + nextInt + ")");

            if (rB(99, 100)) {
                enemy -= 20 - (nextInt / 10);
                abuLog += "\u00A7- You get a critical strike!";
            } else if (rB(1, 2)) {
                abu -= 20 - (nextInt / 10);
                enemyLog += "\u00A7- The enemy forces get strike a mighty blow!";

            }

            if (player.addictedHashish && rB(0, 25)) {
                abu -= 1;
                abuLog += "\u00A7- You are hampered by your addiction to hashish";
            } else if (player.addictedOpium && rB(0, 50)) {
                abu -= 3;
                abuLog += "\u00A7- Your opium addiction causes you to lose your will to fight";
            } else if (player.sickPlague && rB(0, 75)) {
                abu -= 6;
                abuLog += "\u00A7- The plague you are carrying hinders your battle greatly";
            }

            if (player.moral < -75 && rB(0, 75)) {
                abu += 10 - (nextInt / 10);
                enemyLog += "\u00A7- Your vile deeds on the battlefield cause your foes to tremble";
            } else if (player.moral > 75 && rB(0, 75)) {
                abu += 10 - (nextInt / 10);
                enemyLog += "\u00A7- Your good reputation preceeds you, some of the enemies soldiers abandon their foul cause";
            }

            Random r = new Random();
            int enemyHit = (armies.getForce(who) + r.nextInt(10));
            int abuHit = (player.force + r.nextInt(10));
            abu -= enemyHit;
            enemy -= abuHit;

            if (abuHit < 5)
                abuLog += "\u00A7- You barely scratch the enemy";
            else if (abuHit >= 5 && abuHit < 15)
                abuLog += "\u00A7- You feebly hit the enemy";
            else if (abuHit >= 15 && abuHit < 25)
                abuLog += "\u00A7- You hit the enemy";
            else if (abuHit >= 25 && abuHit < 50)
                abuLog += "\u00A7- You hit the enemy hard";
            else if (abuHit >= 50)
                abuLog += "\u00A7- You score a devastating hit";

            if (enemyHit < 5)
                enemyLog += "\u00A7- The enemy slaps you";
            else if (enemyHit >= 5 && enemyHit < 15)
                enemyLog += "\u00A7- You are slightly wounded by the enemy";
            else if (enemyHit >= 15 && enemyHit < 25)
                enemyLog += "\u00A7- The enemy hits you";
            else if (enemyHit >= 25 && enemyHit < 50)
                enemyLog += "\u00A7- You get a bad wound from the enemy";
            else if (enemyHit >= 50)
                enemyLog += "\u00A7- The enemy strikes you with enourmous power";

            player.health = abu;

            if (Mainframe.DEBUG == 1)
                System.out.println("abu health: " + abu + "\nabu force: " + player.force + "\nenemy health: " + enemy + "\nenemy force: "
                        + armies.getForce(who) + "\n(nextInt: + " + nextInt + ")");

            if (enemy < 1) {
                enemyLog += "\u00A7- The enemy forces have been annihilated";
                abuLog += "\u00A7- You won!";
                end = true;

                player.add(who + "won");
            }

            if (abu < 1) {
                if (enemy > 0) {
                    abuLog += "\u00A7- You were defeated!";
                    endDo = "mf.gotoDeath(\"Abu was defeated in a battle that occurred the surroundings of " + player.nicecity + " against the forces of "
                            + armies.getName(who) + ".\", 1);";
                } else {
                    abuLog += "\u00A7- You were defeated!";
                    endDo = "mf.gotoDeath(\"Abu died in a glorious battle on the surroundings of " + player.nicecity
                            + " while destroying the remnants of the forces of " + armies.getName(who) + ".\", 2);";
                }
                end = true;
            }

            Font statusFont = new Font("Arial", 0, 10);

            panel.add(new Drawable(x + 200, y + 150, 150, 200, Drawable.LABEL, enemyLog, statusFont, Color.BLACK));
            panel.add(new Drawable(x + width - 350, y + 150, 150, 200, Drawable.LABEL, abuLog, statusFont, Color.BLACK));

            // FIX 26.4.2005 Included endTask in the next turn script
            String nextTurn = "mf.player.nextDay();mf.gotoBattle(\"" + who + "\", " + (turn + 1) + ", !weapon, " + enemy + ", \"" + endTask.replaceAll("\"", "\\\\\"") + "\");";

            if (!end) {
                player.inBattle = false;
                //panel.add(new Drawable(170, 570, 100, 20, Drawable.LABEL, "Invoke djinnis", nextTurn.replaceAll("!weapon", "3")));
                //if (player.hasItem("13000"))
                //    panel.add(new Drawable(290, 570, 85, 20, Drawable.LABEL, "Use sword", nextTurn.replaceAll("!weapon", "2")));
                //else if (player.hasItem("13010"))
                //    panel.add(new Drawable(290, 570, 85, 20, Drawable.LABEL, "Use dagger", nextTurn.replaceAll("!weapon", "1")));
                panel.add(new Drawable(810, 570, 80, 20, Drawable.LABEL, "Next round", nextTurn.replaceAll("!weapon", "0")));
            } else {
                player.inBattle = false;
                
                if (!endTask.equals(""))
                    panel.add(new Drawable(810, 570, 80, 20, Drawable.LABEL, "End battle", "mf.sounds.loopMusic(mf.sounds.lastMusic);mf.player.chooseFace();" + endTask));
                else if (!endDo.equals(""))
                    panel.add(new Drawable(810, 570, 80, 20, Drawable.LABEL, "End battle", "mf.sounds.loopMusic(mf.sounds.lastMusic);mf.player.chooseFace();" + endDo));
                else
                    panel.add(new Drawable(810, 570, 80, 20, Drawable.LABEL, "End battle", "mf.sounds.loopMusic(mf.sounds.lastMusic);mf.player.chooseFace();mf.gotoPlace(mf.player.place);"));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void paintMap(boolean showIcon) {
        try {
            // first we get the active map entries, which depend on the
            // transport type and also if we have a travel
            // permit to europe
            objects.add(new Drawable(0, 0, getHeight(), getWidth(), Drawable.IMAGE, getImage(map.getBackground())));
            MapEntry entry = new MapEntry();
            Vector entries = map.getEntries(player.city, player.transport, (player.hasItem("10630") && player.transport != MapEntry.TRANSPORT3));
            for (int i = 0, j = entries.size(); i < j; i++) {
                entry = (MapEntry) entries.elementAt(i);
                objects.add(new Drawable(entry.x - 10, entry.y - 10, entry.width, entry.height, Drawable.IMAGE, getImage("pixel", true), null, entry
                        .getAction(player.transport), "Go to " + entry.nice, entry.getDescription(player.transport), 0, null, null));
            }

            Vector items = player.items;
            Item item = null;

            // right side of the window
            //int y = getHeight() - 465;
            //int x = 980;
            // left side of the window
            int y = getHeight() - 565;
            int x = 50; 

            for (int i = 0, j = items.size(); i < j; i++) {
                item = (Item) items.elementAt(i);
                if (item.isActive()) {
                    Image image = getImage(item.picture);
                    if (item.name.toLowerCase().equals(MapEntry.TRANSPORTS[player.transport]))
                        image = getImage(item.picture.replaceAll(".png", "_active.png"));
                    objects.add(new Drawable(x - (image.getWidth(this) / 2), y + (65 - (image.getHeight(this) / 2)), 75, 75, Drawable.IMAGE, image,
                            item.action, item.name + "\u00A7" + item.description));
                    y = y + 65;
                }
            }

            if (showIcon)
                objects.add(new Drawable(player.xHomeCountry - 14, player.yHomeCountry - 14, 0, 0, Drawable.IMAGE, getImage("playerIcon", true)));

            if (showStatus) {
                objects.add(new Drawable(this.getWidth() - 40, 10, 0, 0, Drawable.IMAGE, getImage("controls", true)));
                objects.add(new Drawable(this.getWidth() - 40, 88, 100, 19, Drawable.IMAGE, getImage("pixel", true), "mf.exit();", "Close"));
                objects.add(new Drawable(this.getWidth() - 40, 65, 100, 19, Drawable.IMAGE, getImage("pixel", true), "mf.save();", "Save"));
                objects.add(new Drawable(this.getWidth() - 40, 40, 100, 19, Drawable.IMAGE, getImage("pixel", true), "mf.load();", "Load"));
                objects.add(new Drawable(this.getWidth() - 40, 15, 100, 19, Drawable.IMAGE, getImage("pixel", true), "mf.setExtendedState(Frame.ICONIFIED);",
                        "Minimize"));
                objects.add(new Drawable(this.getWidth() - 40, 111, 24, 19, Drawable.IMAGE, getImage("pixel", true),
                        "mf.showStatus=false;mf.player.lastPlace=\"" + player.place + "\";mf.gotoPlace(\"menu\");", "Back to menu"));
                String helpid = "9239";
                if (helpid != null && !helpid.equals(""))
                    objects.add(new Drawable(this.getWidth() - 39, 136, 100, 19, Drawable.IMAGE, getImage("questionMark", true), "mf.gotoStatus(3, \"" + helpid
                            + "\", 0, 0, 0);", "Help about world map"));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void paintGlass() {
        try {
            // status bar and the city/world icons

            if (showStatus) {
                getImage("statusFace", player.statusFace, true, true);
                glass.add(new Drawable(20, getHeight() - 169, 155, 155, Drawable.STATUS, getImage("statusFace", true), getImage("statusBar", true),
                        "mf.gotoStatus(0, \"\", 0, 0, 0);", "Open status", "Open status", 0, null, null));

                glass.add(new Drawable(386, getHeight() - 38, 100, 19, Drawable.LABEL, player.age + " years", "res=false;",
                        "Abu's age is " + player.day + " days " + player.month + " months " + player.age + " years", new Font("Arial", 1, 12), new Color(255, 0, 255)));

                if (!player.place.equals("highwaymen") && !player.place.equals("pirates")) {
                    glass.add(new Drawable(195, getHeight() - 100, 65, 60, Drawable.IMAGE, getImage("localMap", true),
                            "mf.gotoPlace(\"" + player.city + "\");", "Go to map of " + places.getTitle(player.city)));
                    if (showMapGlobe)
                        glass.add(new Drawable(265, getHeight() - 100, 60, 60, Drawable.IMAGE, getImage("globalMap", true), null, "mf.gotoMap();",
                                "Go to map of the world"));
                }
                int y = 698;
                if (player.sickGeneral) {
                    y += 10;
                    glass.add(new Drawable(480, y, 20, 20, Drawable.IMAGE, getImage("statusPlague", true), "mf.gotoStatus(3, \"5430\", 0, 0, 0);",
                            "You are poisoned"));
                    if (!player.sickPlague && !player.sickPoisoned)
                        y -= 40;
                }

                if (player.sickPoisoned) {
                    if (!player.sickGeneral)
                        y += 10;
                    glass.add(new Drawable(480, y, 20, 20, Drawable.IMAGE, getImage("statusPlague", true), "mf.gotoStatus(3, \"5430\", 0, 0, 0);",
                            "You are poisoned"));
                    if (!player.sickPlague)
                        y -= 40;
                }

                if (player.sickPlague) {
                    if (!player.sickGeneral && !player.sickPoisoned)
                        y += 10;
                    glass.add(new Drawable(480, y, 20, 20, Drawable.IMAGE, getImage("statusPlague", true), "mf.gotoStatus(3, \"5430\", 0, 0, 0);",
                            "You have the plague"));
                    y -= 40;
                }

                if (player.addictedHashish) {
                    glass.add(new Drawable(473, y, 27, 27, Drawable.IMAGE, getImage("statusHashish", true), "mf.gotoStatus(3, \"3360\", 0, 0, 0);",
                            "You are addicted to hashish"));
                    y -= 43;
                }

                if (player.addictedOpium)
                    glass.add(new Drawable(479, y, 27, 27, Drawable.IMAGE, getImage("statusOpium", true), "mf.gotoStatus(3, \"3360\", 0, 0, 0);",
                            "You are addicted to opium"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void paintPlace() {
        paintPlace(player.place);
    }

    public void paintPlace(String to) {
        try {
            objects.add(new Drawable(0, 0, getHeight(), getWidth(), Drawable.IMAGE, getImage(places.getBackground(to))));
            Spicebean sb = new Spicebean();
            Vector pictures = places.getPictures(to);
            for (int i = 0, j = pictures.size(); i < j; i++) {
                sb = (Spicebean) pictures.elementAt(i);
                if (Mainframe.DEBUG == 2)
                    System.out.println("Adding picture: " + sb.picture);
                if (sb.isDoable())
                    objects.add(new Drawable(sb.x, sb.y, sb.width, sb.height, Drawable.IMAGE, getImage(sb.picture), sb.action, sb.name));
                else
                    objects.add(new Drawable(sb.x, sb.y, 0, 0, 3, getImage(sb.picture)));
            }

            Vector items = player.items;
            Item item = null;

            int x = 25;
            int y = 25;
            int width = 0;
            int height = 0;
            int counter = 0;
            String picture = "";

            // if there are any items that want to be shown on the screen, draw
            // them
            for (int i = 0, j = items.size(); i < j; i++) {
                item = (Item) items.elementAt(i);
                if (item.isActive()) {
                    if (item.pictureBig == null || item.pictureBig.equals("")) {
                        picture = item.picture;
                        width = 55;
                        height = 55;
                    } else {
                        picture = item.pictureBig;
                        width = 100;
                        height = 100;
                    }

                    objects.add(new Drawable(x, y, width, height, Drawable.IMAGE, getImage(picture), item.action, item.name + "\u00A7" + item.description));
                    x = x + 75;
                    if (counter == 11) {
                        y = y + 90;
                        x = 25;
                    } else if (counter == 23) {
                        y = y + 90;
                        x = 25;
                    } else if (counter == 35) {
                        y = y + 90;
                        x = 25;
                    } else if (counter == 47) {
                        y = y + 90;
                        x = 25;
                    }
                    counter++;
                }
            }

            x = 210;

            // random drawables that get thrown on the screen
            Vector drawables = places.getDrawables(to);
            for (int i = 0, j = drawables.size(); i < j; i++)
                objects.add(drawables.elementAt(i));

            if (showStatus) {
                objects.add(new Drawable(this.getWidth() - 40, 10, 0, 0, Drawable.IMAGE, getImage("controls", true)));
                objects.add(new Drawable(this.getWidth() - 40, 88, 100, 19, Drawable.IMAGE, getImage("pixel", true), "if(mf.tools.askYesNo(mf, \"You are leaving the game, you will lose all unsaved information. Do you really want to exit?\"))mf.exit();", "Close"));
                objects.add(new Drawable(this.getWidth() - 40, 65, 100, 19, Drawable.IMAGE, getImage("pixel", true), "mf.save();", "Save"));
                objects.add(new Drawable(this.getWidth() - 40, 40, 100, 19, Drawable.IMAGE, getImage("pixel", true), "mf.load();", "Load"));
                objects.add(new Drawable(this.getWidth() - 40, 15, 100, 19, Drawable.IMAGE, getImage("pixel", true), "mf.setExtendedState(Frame.ICONIFIED);",
                        "Minimize"));
                objects.add(new Drawable(this.getWidth() - 40, 111, 100, 19, Drawable.IMAGE, getImage("pixel", true),
                        "mf.showStatus=false;mf.player.lastPlace=\"" + player.place + "\";mf.gotoPlace(\"menu\");", "Back to menu"));
                String helpid = places.getString(to, "Help");
                if (helpid != null && !helpid.equals(""))
                    objects.add(new Drawable(this.getWidth() - 39, 136, 100, 19, Drawable.IMAGE, getImage("questionMark", true), "mf.gotoStatus(3, \"" + helpid
                            + "\", 0, 0, 0);", "Help about " + player.nicecity));
                if (Mainframe.DEBUG > 0) {
                    // some cheats to help with the debugging ;-)
                    objects.add(new Drawable(this.getWidth() - 39, 160, 100, 19, Drawable.IMAGE, getImage("pixel", true), "mf.refresh();mf.player.lastPlace=\""
                            + player.place + "\"", "Refresh all"));
                    objects.add(new Drawable(this.getWidth() - 39, 190, 100, 19, Drawable.IMAGE, getImage("pixel", true), "mf.player.nextDay(30);",
                            "Next month"));
                    objects.add(new Drawable(this.getWidth() - 39, 210, 100, 19, Drawable.IMAGE, getImage("pixel", true), "mf.player.addCulture(\""
                            + player.city + "\", 10);", "Add culture"));
                    objects.add(new Drawable(this.getWidth() - 39, 240, 100, 19, Drawable.IMAGE, getImage("pixel", true), "mf.player.removeCulture(\""
                            + player.city + "\", 10);", "Remove culture"));
                    objects.add(new Drawable(this.getWidth() - 39, 270, 100, 19, Drawable.IMAGE, getImage("pixel", true),
                            "mf.gotoDeath(\"Abu's famous last words\", 3);", "Kill"));
                    objects.add(new Drawable(this.getWidth() - 39, 300, 100, 19, Drawable.IMAGE, getImage("pixel", true), "mf.player.money+=10000;",
                            "Add lots of money"));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void paintDialog() {
        paintDialog(0, 0);
    }

    public void paintDialog(int x, int y) {
        try {
            Vector actions = dialogs.getChoices(dialog);
            String idialog = "/pics/navigation/characters/" + dialogs.getTitle(dialog).toLowerCase().replaceAll(" ", "_") + ".png";
            // draw the panel and the text
            Drawable d = new Drawable(x + 200, y + 56, getWidth() / 2 - 160, 150, Drawable.LABEL, dialogs.getText(dialog));
            if (d.textVector.size() > 6)
                panel.add(new Drawable(x + 20, y + 18, getWidth() - 70, 60 + (d.textVector.size()) * 18, Drawable.PANEL, getImage(idialog), dialogs
                        .getTitle(dialog)));
            else if (actions.size() > 3)
                panel.add(new Drawable(x + 20, y + 18, getWidth() - 70, 100 + (actions.size() + 1) * 20, Drawable.PANEL, getImage(idialog), dialogs
                        .getTitle(dialog)));
            else
                panel.add(new Drawable(x + 20, y + 18, getWidth() - 70, 155, Drawable.PANEL, getImage(idialog), dialogs.getTitle(dialog)));

            panel.add(d);

            // draw all the dialog choices
            int _y = y + 56;
            Spicebean sb = new Spicebean();
            int _x = 0;
            for (int i = actions.size(); _x < i; _x++) {
                sb = (Spicebean) actions.elementAt(_x);
                d = new Drawable(x + getWidth() / 2 + 70, y + _y, 340, 30, 2, sb.name, sb.action);
                panel.add(d);
                _y += 16 * d.textVector.size() - 1;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void paintDiary(int page) {
        try {
            int y = 100;
            int x = 100;
            int counter = 0;
            panel.add(new Drawable(150, 100, 760, 500, Drawable.PANEL, getImage("/pics/navigation/abus/abu_dead2.png"), getImage(player.logo, true),
                    "Your diary", Color.BLACK));

            Font statusFontBold = new Font("Arial", 1, 14);
            Font statusFont = new Font("Arial", 0, 14);
            Color color = Color.WHITE;

            // ADD 10.04.2005 -- Want to see the book of life events in the order they happen in
            Vector tasks = player.journal.tasks;
            String task = "";

            if (page == 0) {
                Image image = null;

                x = 327;
                y = 360;
                switch (player.deathType) {
                case 1:
                    image = getImage("/pics/bagdad/characters/dead001.png");
                    x = 420;
                    y = 280;
                    break;
                case 2:
                    image = getImage("/pics/bagdad/characters/dead003a.png");
                    x = 528;
                    y = 177;
                    break;
                case 3:
                    image = getImage("/pics/bagdad/characters/dead004.png");
                    x = 420;
                    y = 280;
                    break;
                case 4:
                    image = getImage("/pics/bagdad/characters/dead005.png");
                    x = 400;
                    y = 280;
                    break;
                default:
                    image = getImage("/pics/bagdad/characters/dead002.png");
                    x = 400;
                    y = 280;
                    break;
                }

                panel.add(new Drawable(x, y, 0, 0, Drawable.IMAGE, image));
                Drawable d1 = new Drawable((410 + 175), 495, 460, 20, Drawable.LABEL, player.lastWords, statusFontBold, color);
                d1.x -= (d1.textWidth / 2);
                panel.add(d1);
            } else {
                task = (String)tasks.elementAt(page-1);
                int when = Integer.parseInt((String) player.journal.timestamp.get(task));
                int year = (int) (when / 360) + 17 - 500;
                String time = ", age of " + year;
                Image image = getImage(player.journal.getPicture(task));
                String taskText = player.journal.getString(task, "Endtext");
                String taskTasks = player.journal.get(task);
                panel.add(new Drawable((507 + 75) - (int) (image.getWidth(this) / 2), (350) - (int) (image.getHeight(this) / 2), 0, 0, Drawable.IMAGE, image));
                Drawable d1 = new Drawable((410 + 175), 515, 460, 20, Drawable.LABEL, player.journal.getString(task, "Display") + time, statusFontBold, color);
                d1.x -= (d1.textWidth / 2);
                panel.add(d1);
                Drawable d2 = new Drawable((410 + 175), 540, 460, 380, Drawable.LABEL, taskText, statusFont, color);
                d2.x -= (d2.textWidth / 2);
                panel.add(d2);
            }

            if (page > 0)
                panel.add(new Drawable(170, 570, 120, 20, Drawable.LABEL, "<< PREVIOUS", "mf.gotoDiary(" + (page - 1) + ");", statusFontBold, color));
            // FIX 10.5.2005 Clicking END GAME caused music to be turned back on
            panel.add(new Drawable(540, 570, 120, 20, Drawable.LABEL, "END GAME",
                    "boolean p = mf.sounds.musicOn;mf.sounds.musicOn=false;mf.player = new Player();mf.sounds.musicOn=p;mf.sounds.loopMusic(\"/music/16_world_horizon.ogg\");mf.gotoPlace(\"menu\");", statusFontBold, color));
            if (page < player.journal.tasksClosed.size())
                panel.add(new Drawable(840, 570, 120, 20, Drawable.LABEL, "NEXT >>", "mf.gotoDiary(" + (page + 1) + ");", statusFontBold, color));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void paintConverter(String back) {
        paintConverter(back, -1, 0, 0);
    }

    public void paintConverter(String back, int number, int action, int total) {
        // this is the Hijra <-> Gregorian calendar calculator :-)
        // it was pretty fun to make this, even though we were pressed for time
        int x = 165;
        int y = 280;
        int width = 220;
        int height = 290;
        int newTotal = total;
        Font statusFontBold = new Font("Arial", 1, 11);
        Font statusFont = new Font("Arial", 0, 10);
        if (number > -1)
            newTotal = Integer.parseInt(String.valueOf(total) + String.valueOf(number));
        if (action == 1)
            newTotal = -newTotal;
        panel.add(new Drawable(x, y, width, height, Drawable.PANEL, null, null, null, "Hijra converter", null, 1, null, Color.WHITE));
        panel.add(new Drawable(x + width - 20, y + 10, 20, 20, Drawable.LABEL, "X", "mf.converter=0;" + back, "Close converter", statusFontBold, Color.BLACK));
        panel.add(new Drawable(x + 40, y + 18, 140, 20, Drawable.LABEL, "CALENDAR CONVERTER", statusFontBold, Color.BLACK));
        panel.add(new Drawable(x + 43, y + 45, 120, 108, Drawable.IMAGE, getImage("/pics/objects/converter.png")));

        panel.add(new Drawable(x + width - 70, y + 190, 40, 20, Drawable.LABEL, "CLEAR", "mf.paintConverter(\"" + back.replaceAll("\"", "\\\\\"")
                + "\", -1, 0, 0);", "Clear converter", statusFont, Color.BLACK));

        panel.add(new Drawable(x + 26, y + 183, 50, 20, Drawable.LABEL, "+/-", "mf.paintConverter(\"" + back.replaceAll("\"", "\\\\\"") + "\", -1, 1, "
                + String.valueOf(newTotal) + ");", "Change year to positive/negative", statusFontBold, Color.BLACK));

        int _x = x - 25 + 85;
        int _y = y + 138;
        Color color = Color.BLACK;
        int counter = 0;
        for (int i = 1; i < 11; i++) {
            if (i == number)
                color = Color.RED;
            else
                color = Color.BLACK;

            if (i < 10) {
                _x += 20;
                panel.add(new Drawable(_x, _y, 13, 15, Drawable.LABEL, String.valueOf(i), "mf.paintConverter(\"" + back.replaceAll("\"", "\\\\\"") + "\", "
                        + String.valueOf(i) + ", 0, " + String.valueOf(newTotal) + ");", String.valueOf(i), statusFontBold, color));
            } else {
                _x += 40;
                panel.add(new Drawable(_x, _y, 13, 15, Drawable.LABEL, "0", "mf.paintConverter(\"" + back.replaceAll("\"", "\\\\\"") + "\", 0, 0, "
                        + String.valueOf(newTotal) + ");", "0", statusFontBold, color));
            }

            if (counter++ > 1) {
                _x = x - 25 + 85;
                _y += 15;
                counter = 0;
            }
        }
        int ah = (int) (newTotal - Math.round((newTotal / 33F)) + 622);
        int gre = (int) ((newTotal - 622) + Math.round((newTotal - 622) / 32F));
        String greStr = "AC";
        String ahStr = "AH";
        String greStr2 = "AC";
        String ahStr2 = "AH";
        if (gre < 0)
            ahStr = "BH";
        if (ah < 0)
            greStr = "BC";
        if (newTotal < 0)
            ahStr2 = "BH";
        if (newTotal < 0)
            greStr2 = "BC";
        panel.add(new Drawable(x + 40, y + 220, 300, 100, Drawable.LABEL, "Hijra years to gregorian years", statusFont, Color.BLACK));
        panel.add(new Drawable(x + 70, y + 235, 300, 100, Drawable.LABEL, newTotal + " " + ahStr2 + " = " + ah + " " + greStr, statusFont, Color.BLACK));
        panel.add(new Drawable(x + 40, y + 255, 300, 100, Drawable.LABEL, "Gregorian years to Hijra years", statusFont, Color.BLACK));
        panel.add(new Drawable(x + 70, y + 270, 300, 100, Drawable.LABEL, newTotal + " " + greStr2 + " = " + gre + " " + ahStr, statusFont, Color.BLACK));

        doLayout();
        repaint();
    }

    public void paintStatus(int state, String id, int page, int from, int more) {
        try {
            // this is a terrible mess, because there is so much of it and this
            // takes
            // the longest to calculate and draw, so if any optimization is
            // needed, this
            // is one place to start.
            // [0] Status: the main status screen shows an overview of the
            // player status
            // [1] Inventory: all the items that the player has (well, not _all_
            // of them, just the ones we want to show
            // [2] Diary: open and done quests
            // [3] Help: help screen, index or individual help
            // [4] Log: the log of "all" the things that have happened, dialogs
            // etc
            // [5] Death: the death screen, when the game ends
            // [6] Culture: statistics for the culture points of the player

            int _y = 0;
            String[] title = { "Status", "Inventory", "Diary", "Help", "Log", "DEATH!", "Culture" };
            panel.add(new Drawable(150, 100, 760, 500, Drawable.PANEL, getImage("statusFace", true), getImage(player.logo, true), title[state]));

            Font statusFontBold = new Font("Arial", 1, 12);
            Font statusFont = new Font("Arial", 0, 10);

            if (state != 3) {
                panel.add(new Drawable(410, 210, 300, 20, Drawable.LABEL, player.get("Name"), statusFontBold, Color.BLACK));

                panel.add(new Drawable(410, 240, 300, 20, Drawable.LABEL, "date and place of born", statusFont, Color.BLACK));
                panel.add(new Drawable(594, 240, 300, 20, Drawable.LABEL, player.get("dateAndPlace"), statusFont, Color.BLACK));

                Drawable wd = new Drawable(594, 255, 300, 20, Drawable.LABEL, player.get("wifes"), statusFont, Color.BLACK);
                int wifes = wd.textVector.size();
                if (wifes > 1)
                    panel.add(new Drawable(410, 255, 300, 20, Drawable.LABEL, "wives", statusFont, Color.BLACK));
                else
                    panel.add(new Drawable(410, 255, 300, 20, Drawable.LABEL, "wife", statusFont, Color.BLACK));
                if (!player.get("wifes").equals(""))
                    panel.add(wd);
                else
                    panel.add(new Drawable(594, 255, 300, 20, Drawable.LABEL, "none", statusFont, Color.BLACK));

                panel.add(new Drawable(410, 257 + (wifes * 9), 300, 20, Drawable.LABEL, "children", statusFont, Color.BLACK));
                if (!player.get("children").equals(""))
                    panel.add(new Drawable(594, 257 + (wifes * 9), 300, 20, Drawable.LABEL, player.get("children"), statusFont, Color.BLACK));
                else
                    panel.add(new Drawable(594, 257 + (wifes * 9), 300, 20, Drawable.LABEL, "none", statusFont, Color.BLACK));
                panel.add(new Drawable(177, 280, 200, 20, Drawable.LABEL, "MONEY", statusFontBold, Color.BLACK));
                panel.add(new Drawable(177, 300, 200, 20, Drawable.LABEL, String.valueOf(player.money) + " silver dirhams", statusFont, Color.BLACK));
            }

            switch (state) {
            default:
                break;

            case 0: // status
                int increment = 20;
                int y = 370;
                boolean foundp = false;
                // Property
                panel.add(new Drawable(177, y - 30, 300, 20, Drawable.LABEL, "PROPERTY", statusFontBold, Color.BLACK));

                panel.add(new Drawable(177, y, 300, 20, Drawable.LABEL, "- lands", statusFont, Color.BLACK));
                int x = 253;
                String[] lands = { "12050", "14000", "14005", "14010", "14015", "14020", "14025", "14030", "14035", "14040", "14045" };
                for (int i = 0, j = lands.length; i < j; i++)
                    if (player.hasItem(lands[i], 1, false)) {
                        Item item = (Item) market.getItem(lands[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y - 5, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                    }

                String[] spices = { "10110", "10100", "10120", "10130", "10210", "10200", "10220", "10230" };
                x = 253;
                for (int i = 0, j = spices.length; i < j; i++)
                    if (player.hasItem(spices[i], 1, false)) {
                        Item item = (Item) market.getItem(spices[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 22, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                    }

                if (foundp)
                    panel.add(new Drawable(177, y + 30, 300, 20, Drawable.LABEL, "- spices", statusFont, Color.BLACK));
                foundp = false;

                String[] art1 = { "10800", "10801", "10802", "10803", "10804", "10805" };
                String[] art2 = { "10810", "10811", "10812" };
                String[] art3 = { "10820" };
                String[] art4 = { "10830", "10831" };
                x = 253;
                for (int i = 0, j = art1.length; i < j; i++)
                    if (player.hasItem(art1[i], 1, false)) {
                        Item item = (Item) market.getItem(art1[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 53, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = art2.length; i < j; i++)
                    if (player.hasItem(art2[i], 1, false)) {
                        Item item = (Item) market.getItem(art2[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 53, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = art3.length; i < j; i++)
                    if (player.hasItem(art3[i], 1, false)) {
                        Item item = (Item) market.getItem(art3[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 53, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = art4.length; i < j; i++)
                    if (player.hasItem(art4[i], 1, false)) {
                        Item item = (Item) market.getItem(art4[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 53, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                if (foundp)
                    panel.add(new Drawable(177, y + 60, 300, 20, Drawable.LABEL, "- works of art", statusFont, Color.BLACK));
                foundp = false;

                String[] treasures1 = { "11600", "11601", "11602", "11603", "11604" };
                String[] treasures2 = { "11610", "11611" };
                String[] treasures3 = { "11620" };
                String[] treasures4 = { "11630" };
                String[] treasures5 = { "11640" };
                String[] treasures6 = { "11650", "11651", "11652", "11660", "11661" };
                String[] treasures7 = { "11670", "11671", "11672", "11673" };
                String[] treasures8 = { "11680", "11680" };
                x = 253;
                for (int i = 0, j = treasures1.length; i < j; i++)
                    if (player.hasItem(treasures1[i], 1, false)) {
                        Item item = (Item) market.getItem(treasures1[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 82, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = treasures2.length; i < j; i++)
                    if (player.hasItem(treasures2[i], 1, false)) {
                        Item item = (Item) market.getItem(treasures2[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 82, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = treasures3.length; i < j; i++)
                    if (player.hasItem(treasures3[i], 1, false)) {
                        Item item = (Item) market.getItem(treasures3[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 82, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = treasures4.length; i < j; i++)
                    if (player.hasItem(treasures4[i], 1, false)) {
                        Item item = (Item) market.getItem(treasures4[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 82, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = treasures5.length; i < j; i++)
                    if (player.hasItem(treasures5[i], 1, false)) {
                        Item item = (Item) market.getItem(treasures5[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 82, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = treasures6.length; i < j; i++)
                    if (player.hasItem(treasures6[i], 1, false)) {
                        Item item = (Item) market.getItem(treasures6[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 82, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = treasures7.length; i < j; i++)
                    if (player.hasItem(treasures7[i], 1, false)) {
                        Item item = (Item) market.getItem(treasures7[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 82, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = treasures8.length; i < j; i++)
                    if (player.hasItem(treasures8[i], 1, false)) {
                        Item item = (Item) market.getItem(treasures8[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 82, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                if (foundp)
                    panel.add(new Drawable(177, y + 90, 300, 20, Drawable.LABEL, "- treasures", statusFont, Color.BLACK));
                foundp = false;

                String[] luxury1 = { "10720" };
                String[] luxury2 = { "10750", "10751" };
                String[] luxury3 = { "10730", "10731", "10732", "10733" };
                String[] luxury4 = { "10760" };
                String[] luxury5 = { "10770" };
                String[] luxury6 = { "10710" };
                String[] luxury7 = { "10740", "10741", "10742", "10743", "10744", "10745" };
                x = 253;
                for (int i = 0, j = luxury1.length; i < j; i++)
                    if (player.hasItem(luxury1[i], 1, false)) {
                        Item item = (Item) market.getItem(luxury1[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 112, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = luxury2.length; i < j; i++)
                    if (player.hasItem(luxury2[i], 1, false)) {
                        Item item = (Item) market.getItem(luxury2[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 112, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = luxury3.length; i < j; i++)
                    if (player.hasItem(luxury3[i], 1, false)) {
                        Item item = (Item) market.getItem(luxury3[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 112, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = luxury4.length; i < j; i++)
                    if (player.hasItem(luxury4[i], 1, false)) {
                        Item item = (Item) market.getItem(luxury4[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 112, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = luxury5.length; i < j; i++)
                    if (player.hasItem(luxury5[i], 1, false)) {
                        Item item = (Item) market.getItem(luxury5[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 112, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = luxury6.length; i < j; i++)
                    if (player.hasItem(luxury6[i], 1, false)) {
                        Item item = (Item) market.getItem(luxury6[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 112, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = luxury7.length; i < j; i++)
                    if (player.hasItem(luxury7[i], 1, false)) {
                        Item item = (Item) market.getItem(luxury7[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 112, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                if (foundp)
                    panel.add(new Drawable(177, y + 120, 300, 20, Drawable.LABEL, "- luxury", statusFont, Color.BLACK));
                foundp = false;

                String[] buildings1 = { "12000", "12001", "12002", "12003", "12004" };
                String[] buildings2 = { "12060", "12061", "12062", "12063", "12064", "12065", "12066", "12067", "12068", "12069", "12070" };
                String[] buildings3 = { "12080", "12081", "12082", "12083", "12084", "12085", "12086", "12087", "12088", "12089", "12090" };
                String[] buildings4 = { "12030" };
                String[] buildings5 = { "12010" };
                String[] buildings6 = { "12020" };
                String[] buildings7 = { "12040" };
                x = 253;
                for (int i = 0, j = buildings1.length; i < j; i++)
                    if (player.hasItem(buildings1[i], 1, false)) {
                        Item item = (Item) market.getItem(buildings1[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 143, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = buildings2.length; i < j; i++)
                    if (player.hasItem(buildings2[i], 1, false)) {
                        Item item = (Item) market.getItem(buildings2[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 143, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = buildings3.length; i < j; i++)
                    if (player.hasItem(buildings3[i], 1, false)) {
                        Item item = (Item) market.getItem(buildings3[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 143, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = buildings4.length; i < j; i++)
                    if (player.hasItem(buildings4[i], 1, false)) {
                        Item item = (Item) market.getItem(buildings4[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 143, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = buildings5.length; i < j; i++)
                    if (player.hasItem(buildings5[i], 1, false)) {
                        Item item = (Item) market.getItem(buildings5[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 143, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = buildings6.length; i < j; i++)
                    if (player.hasItem(buildings6[i], 1, false)) {
                        Item item = (Item) market.getItem(buildings6[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 143, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = buildings7.length; i < j; i++)
                    if (player.hasItem(buildings7[i], 1, false)) {
                        Item item = (Item) market.getItem(buildings7[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 143, 20, 25, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                if (foundp)
                    panel.add(new Drawable(177, y + 150, 300, 20, Drawable.LABEL, "- buildings", statusFont, Color.BLACK));
                foundp = false;

                // Licenses
                String[] licenses1 = { "10600", "10610" };
                String[] licenses2 = { "10620" };
                String[] licenses3 = { "10630" };
                String[] licenses4 = { "10640" };
                String[] licenses5 = { "10650", "10651", "10652", "10653", "10654", "10655", "10656", "10657", "10658", "10659", "10660" };
                increment = 40;
                x = 450;
                for (int i = 0, j = licenses1.length; i < j; i++)
                    if (player.hasItem(licenses1[i], 1, false)) {
                        Item item = (Item) market.getItem(licenses1[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y - 10, 40, 40, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = licenses2.length; i < j; i++)
                    if (player.hasItem(licenses2[i], 1, false)) {
                        Item item = (Item) market.getItem(licenses2[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y - 10, 40, 40, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = licenses3.length; i < j; i++)
                    if (player.hasItem(licenses3[i], 1, false)) {
                        Item item = (Item) market.getItem(licenses3[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y - 10, 40, 40, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = licenses4.length; i < j; i++)
                    if (player.hasItem(licenses4[i], 1, false)) {
                        Item item = (Item) market.getItem(licenses4[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y - 10, 40, 40, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                for (int i = 0, j = licenses5.length; i < j; i++)
                    if (player.hasItem(licenses5[i], 1, false)) {
                        Item item = (Item) market.getItem(licenses5[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y - 10, 40, 40, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                        break;
                    }
                if (foundp)
                    panel.add(new Drawable(460, y - 30, 300, 20, Drawable.LABEL, "LICENSES", statusFontBold, Color.BLACK));
                foundp = false;

                // Maps
                String[] maps = { "11500", "11510", "11520", "11530" };
                x = 450;
                for (int i = 0, j = maps.length; i < j; i++)
                    if (player.hasItem(maps[i], 1, false)) {
                        Item item = (Item) market.getItem(maps[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 60, 40, 40, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                    }
                if (foundp)
                    panel.add(new Drawable(460, y + 40, 300, 20, Drawable.LABEL, "MAPS", statusFontBold, Color.BLACK));
                foundp = false;

                // Workers
                String[] workers = { "16000", "16010", "16020", "16030" };
                x = 450;
                for (int i = 0, j = workers.length; i < j; i++)
                    if (player.hasItem(workers[i], 1, false)) {
                        Item item = (Item) market.getItem(workers[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 130, 40, 40, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundp = true;
                    }
                if (foundp)
                    panel.add(new Drawable(460, y + 110, 300, 20, Drawable.LABEL, "WORKERS", statusFontBold, Color.BLACK));
                foundp = false;

                boolean foundsuperp = false;
                // Supernatural
                String[] amulets = { "11000", "11001", "11002", "11003", "11004", "11005", "11010", "11011", "11012", "11013", "11014", "11015" };
                x = 723;
                increment = 13;

                for (int i = 0, j = amulets.length; i < j; i++)
                    if (player.hasItem(amulets[i], 1, false)) {
                        Item item = (Item) market.getItem(amulets[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y - 6, 10, 20, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundsuperp = true;
                        foundp = true;
                    }
                if (foundp)
                    panel.add(new Drawable(652, y, 300, 20, Drawable.LABEL, "- amulets", statusFont, Color.BLACK));
                foundp = false;

                String[] drinks = { "11030", "11031", "11040" };
                x = 723;
                for (int i = 0, j = drinks.length; i < j; i++)
                    if (player.hasItem(drinks[i], 1, false)) {
                        Item item = (Item) market.getItem(drinks[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 24, 10, 20, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundsuperp = true;
                        foundp = true;
                    }
                if (foundp)
                    panel.add(new Drawable(652, y + 30, 300, 20, Drawable.LABEL, "- drinks", statusFont, Color.BLACK));
                foundp = false;

                String[] buckles = { "11050", "11051", "11052", "11053", "11054", "11055", "11056" };
                x = 723;
                increment = 20;
                for (int i = 0, j = buckles.length; i < j; i++)
                    if (player.hasItem(buckles[i], 1, false)) {
                        Item item = (Item) market.getItem(buckles[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 54, 13, 22, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundsuperp = true;
                        foundp = true;
                    }
                if (foundp)
                    panel.add(new Drawable(652, y + 60, 300, 20, Drawable.LABEL, "- buckles", statusFont, Color.BLACK));
                foundp = false;

                String[] rosary = { "11020", "11025" };
                x = 723;
                increment = 50;
                for (int i = 0, j = rosary.length; i < j; i++)
                    if (player.hasItem(rosary[i], 1, false)) {
                        Item item = (Item) market.getItem(rosary[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 87, 60, 50, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundsuperp = true;
                        foundp = true;
                    }
                if (foundp)
                    if (player.hasItem("11020"))
                        panel.add(new Drawable(652, y + 90, 300, 20, Drawable.LABEL, "- rosary", statusFont, Color.BLACK));
                    else
                        panel.add(new Drawable(652, y + 90, 300, 20, Drawable.LABEL, "- talisman", statusFont, Color.BLACK));
                foundp = false;

                String[] flying = { "10335", "10345", "10355" };
                x = 723;
                increment = 50;
                for (int i = 0, j = flying.length; i < j; i++)
                    if (player.hasItem(flying[i], 1, false)) {
                        Item item = (Item) market.getItem(flying[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 105, 60, 50, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundsuperp = true;
                        foundp = true;
                    }
                if (foundp)
                    panel.add(new Drawable(652, y + 120, 300, 20, Drawable.LABEL, "- flying", statusFont, Color.BLACK));
                foundp = false;

                String[] others = { "11700", "13000" };
                x = 723;
                increment = 40;
                for (int i = 0, j = others.length; i < j; i++)
                    if (player.hasItem(others[i], 1, false)) {
                        Item item = (Item) market.getItem(others[i]);
                        Image image = getImage(item.pictureStatus);
                        panel.add(new Drawable(x, y + 150, 60, 50, Drawable.IMAGE, image, "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state
                                + ", 0);", item.name + "\u00A7" + item.description));
                        x = x + increment;
                        foundsuperp = true;
                        foundp = true;
                    }
                if (foundp)
                    panel.add(new Drawable(652, y + 150, 300, 20, Drawable.LABEL, "- others", statusFont, Color.BLACK));
                foundp = false;

                if (foundsuperp)
                    panel.add(new Drawable(652, y - 30, 300, 20, Drawable.LABEL, "SUPERNATURAL", statusFontBold, Color.BLACK));

                break;
            case 1: // inventory
                int _x = 150;
                int per = 16;
                Item item = null;
                Vector inventory = player.getInventory();
                int i = page * per;
                int j = inventory.size() - 1;
                if (j >= per) {
                    if (page > 0)
                        panel.add(new Drawable(190, 340, 80, 20, Drawable.LABEL, "<< Previous", "mf.gotoStatus(1, \"\", " + String.valueOf(page - 1) + ", "
                                + state + ", 0);", "Goto previous page"));
                    j = ((page + 1) * per) - 1;
                    if (j > inventory.size())
                        j = inventory.size();
                    int k = 0;
                    String strPage = "";
                    _y = 312;
                    for (int l = (int) ((inventory.size() - 1) / per); k <= l; k++) {
                        if (k == l)
                            strPage = String.valueOf(k + 1);
                        else
                            strPage = String.valueOf(k + 1) + ",";
                        if (page == k)
                            panel.add(new Drawable(290 + (k * 20), 340, 18, 20, Drawable.LABEL, strPage, "mf.gotoStatus(1, \"\", " + String.valueOf(k) + ", "
                                    + state + ", 0);", "Goto page " + String.valueOf(k + 1), fontBold, Color.RED));
                        else
                            panel.add(new Drawable(290 + (k * 20), 340, 18, 20, Drawable.LABEL, strPage, "mf.gotoStatus(1, \"\", " + String.valueOf(k) + ", "
                                    + state + ", 0);", "Goto page " + String.valueOf(k + 1)));

                    }
                    if (page < (int) ((inventory.size() - 1) / per))
                        panel.add(new Drawable(290 + (k * 20), 340, 80, 20, Drawable.LABEL, "Next >>", "mf.gotoStatus(1, \"\", " + String.valueOf(page + 1)
                                + ", " + state + ", 0);", "Goto next page"));

                }

                _y = 360;
                if ((inventory.size() >= per && page != (int) (inventory.size() / per)) || (page == 0 && inventory.size() != per))
                    j++;

                for (int l = i; l < j; l++) {
                    if (_y > 500) {
                        _x += 94;
                        _y = 360;
                    }
                    item = (Item) inventory.get(l);
                    Image image = getImage(item.picture);
                    i++;
                    panel.add(new Drawable(_x + (47 - (image.getWidth(this) / 2)), _y + (50 - (image.getHeight(this) / 2)), 70, 70, Drawable.IMAGE,
                            getImage(item.picture), "mf.gotoStatus(3,\"" + item.helpId + "\", " + page + ", " + state + ", 0);", item.name + "\u00A7"
                                    + item.description));
                    _y = _y + 100;
                }
                break;
            case 2: // journal
                increment = 20;
                y = 370;
                panel.add(new Drawable(177, y - 30, 300, 20, Drawable.LABEL, "OPEN QUESTS", statusFontBold, Color.BLACK));

                Enumeration tasks = player.journal.tasksOpen.keys();
                String task = "";
                String additional = "";
                int counter = 0;
                x = 177;
                while (tasks.hasMoreElements()) {
                    task = (String) tasks.nextElement();
                    // FIX 29.4.2005 Do not display null values
                    if (task == null) continue;
                    additional = player.journal.get(task);
                    if (!additional.equals("") && additional.charAt(0) == '!')
                        if (additional.indexOf("\u00A7") == -1)
                            additional = "";
                        else
                            additional = additional.substring(additional.indexOf("\u00A7"), additional.length());
                    // FIX 29.4.2005 Do not display null values
                    if (player.journal.getString(task, "Display") != null && player.journal.getString(task, "Text") != null)
                    panel.add(new Drawable(x, y, 180, 20, Drawable.LABEL, player.journal.getString(task, "Display"), "res=false;", player.journal.getString(
                            task, "Text")
                            + additional, statusFont, Color.BLACK));
                    y += increment;
                    counter++;
                    if (counter == 9) {
                        y = 370;
                        x += 180;
                    }
                }

                increment = 20;
                y = 370;
                counter = 0;
                x = 450;
                panel.add(new Drawable(450, y - 30, 300, 20, Drawable.LABEL, "QUESTS DONE", statusFontBold, Color.BLACK));

                if (player.journal.tasksClosed.size() > (18 + more)) {
                    panel.add(new Drawable(750, y - 30, 300, 20, Drawable.LABEL, "MORE >>", "mf.gotoStatus(" + state + ", \"\", " + page + ", " + from + ", "
                            + (more + 18) + ");", statusFontBold, Color.BLACK));
                }
                if (more > 0) {
                    panel.add(new Drawable(650, y - 30, 300, 20, Drawable.LABEL, "<< LESS", "mf.gotoStatus(" + state + ", \"\", " + page + ", " + from + ", "
                            + (more - 18) + ");", statusFontBold, Color.BLACK));
                }

                tasks = player.journal.tasksClosed.keys();
                task = "";
                int looper = 0;
                while (tasks.hasMoreElements()) {
                    
                    task = (String) tasks.nextElement();
                    if (looper >= more) {
                        panel.add(new Drawable(x, y, 180, 20, Drawable.LABEL, player.journal.getString(task, "Display"), "res=false;", player.journal
                                .getString(task, "Text")
                                + player.journal.get(task), statusFont, Color.BLACK));
                        y += increment;

                        counter++;
                        if (counter == 9) {
                            y = 370;
                            x += 180;
                        } else if (counter == 18)
                            break;
                    }
                    looper++;
                }

                int amount = Integer.parseInt("12");

                break;
            case 3: // help
                if (id.equals("")) {
                    String[] alphabet = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
                            "X", "Y", "Z" };

                    Vector helps = market.getHelp(alphabet[more]);

                    Color hcolor = Color.BLACK;
                    for (int k = 0, l = alphabet.length; k < l; k++) {
                        if (k == more)
                            hcolor = Color.RED;
                        else
                            hcolor = Color.BLACK;
                        panel.add(new Drawable(190 + (k * 27), 310, 15, 15, Drawable.LABEL, alphabet[k], "mf.gotoStatus(" + state + ", \"\", " + page + ", "
                                + from + ", " + String.valueOf(k) + ");", "Show help for letter " + alphabet[k], statusFontBold, hcolor));
                    }

                    _y = 340;
                    _x = 177;
                    for (Iterator iter = helps.iterator(); iter.hasNext();) {
                        Item help = (Item) iter.next();
                        panel.add(new Drawable(_x, _y, 230, 20, Drawable.LABEL, help.name, "mf.gotoStatus(" + state + ", \"" + help.id + "\", " + page + ", "
                                + state + ", 0);", statusFontBold, Color.BLACK));
                        _y += 30;

                        if (_y > 530) {
                            _y = 340;
                            _x += 190;
                        }

                        if (_x > 750)
                            break;
                    }
                } else {
                    item = (Item) market.getItem(id);
                    Image image = getImage(item.pictureBig);
                    panel.add(new Drawable((200 + 75) - (int) (image.getWidth(this) / 2), (300 + 119) - (int) (image.getHeight(this) / 2), 0, 0,
                            Drawable.IMAGE, image));
                    panel.add(new Drawable(410, 210, 460, 20, Drawable.LABEL, item.name, fontBold, Color.BLACK));
                    Drawable d = new Drawable(410, 240, 460, 380, Drawable.LABEL, item.description);
                    String str = "mf.converter=mf.panel.size();mf.paintConverter(\"mf.gotoStatus(" + state + ", \\\"" + id + "\\\", " + page + ", " + from
                            + ", " + more + ");\");";
                    panel.add(new Drawable(750, 529, 120, 20, Drawable.LABEL, "Calendar converter", str));
                    if (d.textVector.size() > 15 && more == 0) {
                        d.textVector.setSize(16);
                        panel.add(new Drawable(570, 529, 40, 20, Drawable.LABEL, "More", "mf.gotoStatus(" + state + ", \"" + id + "\", " + page + ", " + from
                                + ", " + (more + 1) + ");"));
                    } else if (more == 1) {
                        for (int l = 14; l >= 0; l--)
                            d.textVector.remove(l);
                        if (d.textVector.size() > 15) {
                            d.textVector.setSize(16);
                            panel.add(new Drawable(550, 529, 40, 20, Drawable.LABEL, "Less", "mf.gotoStatus(" + state + ", \"" + id + "\", " + page + ", "
                                    + from + ", " + (more - 1) + ");"));
                            panel.add(new Drawable(590, 529, 40, 20, Drawable.LABEL, "More", "mf.gotoStatus(" + state + ", \"" + id + "\", " + page + ", "
                                    + from + ", " + (more + 1) + ");"));
                        } else {
                            panel.add(new Drawable(570, 529, 40, 20, Drawable.LABEL, "Less", "mf.gotoStatus(" + state + ", \"" + id + "\", " + page + ", "
                                    + from + ", " + (more - 1) + ");"));
                        }
                    } else if (more == 2) {
                        for (int l = 29; l >= 0; l--)
                            d.textVector.remove(l);
                        if (d.textVector.size() > 15) {
                            d.textVector.setSize(16);
                            panel.add(new Drawable(550, 529, 40, 20, Drawable.LABEL, "Less", "mf.gotoStatus(" + state + ", \"" + id + "\", " + page + ", "
                                    + from + ", " + (more - 1) + ");"));
                            panel.add(new Drawable(590, 529, 40, 20, Drawable.LABEL, "More", "mf.gotoStatus(" + state + ", \"" + id + "\", " + page + ", "
                                    + from + ", " + (more + 1) + ");"));
                        } else {
                            panel.add(new Drawable(570, 529, 40, 20, Drawable.LABEL, "Less", "mf.gotoStatus(" + state + ", \"" + id + "\", " + page + ", "
                                    + from + ", " + (more - 1) + ");"));
                        }
                    } else if (more == 3) {
                        for (int l = 44; l >= 0; l--)
                            d.textVector.remove(l);
                        panel.add(new Drawable(570, 529, 40, 20, Drawable.LABEL, "Less", "mf.gotoStatus(" + state + ", \"" + id + "\", " + page + ", " + from
                                + ", " + (more - 1) + ");"));
                    }
                    panel.add(d);
                    panel.add(new Drawable(780, 570, 65, 20, Drawable.LABEL, "<< Back", "mf.gotoStatus(" + from + ", \"\", " + page + ", 0, 0);"));
                }
                break;
            case 4: // Log
                _y = 370;
                _x = 177;
                counter = player.log.size() - 1;

                panel.add(new Drawable(_x, _y - 30, 100, 200, Drawable.LABEL, "LOG", statusFontBold, Color.BLACK));

                Drawable d = null;
                while (_y < 500 && counter > -1) {
                    if (counter <= more) {
                        LogItem logItem = (LogItem) player.log.elementAt(counter);
                        d = new Drawable(_x, _y, 700, 200, Drawable.LABEL, logItem.toString(), statusFont, Color.BLACK);
                        panel.add(d);
                        _y += d.textVector.size() * 11 + 3;
                    }
                    counter--;
                }
                if (more > 0) {
                    int left = more - 2;
                    if (left < 0)
                        left = 0;
                    panel.add(new Drawable(590, 545, 40, 20, Drawable.LABEL, "More", "mf.gotoStatus(" + state + ", \"" + id + "\", " + page + ", " + from
                            + ", " + (left) + ");"));
                    if (more < player.log.size() - 1)
                        panel.add(new Drawable(550, 545, 40, 20, Drawable.LABEL, "Less", "mf.gotoStatus(" + state + ", \"" + id + "\", " + page + ", " + from
                                + ", " + (more + 2) + ");"));
                } else if (more < player.log.size() - 1) {
                    panel.add(new Drawable(550, 545, 40, 20, Drawable.LABEL, "Less", "mf.gotoStatus(" + state + ", \"" + id + "\", " + page + ", " + from
                            + ", " + (more + 2) + ");"));
                }
                break;
            case 5: // end of game
                String death = null;
                x = 327;
                y = 360;
                int width = 270;
                int height = 200;
                switch (player.deathType) {
                case 1:
                    death = "/pics/bagdad/characters/dead001.png";
                    x = 420;
                    y = 350;
                    break;
                case 2:
                    death = "/pics/bagdad/characters/dead003.png";
                    x = 528;
                    y = 177;
                    height = 400;
                    break;
                case 3:
                    death = "/pics/bagdad/characters/dead004.png";
                    x = 420;
                    y = 350;
                    break;
                case 4:
                    death = "/pics/bagdad/characters/dead005.png";
                    x = 400;
                    y = 280;
                    break;
                default:
                    death = "/pics/bagdad/characters/dead002.png";
                    x = 400;
                    y = 350;
                    break;
                }

                String action = "mf.showStatus=false;mf.player.logo=\"logoDeath2\";mf.gotoPlace(\"gameoverparadise\");";
                if (player.moral < 0)
                    action = "mf.showStatus=false;mf.player.logo=\"logoDeath2\";mf.gotoPlace(\"gameoverhell\");";

                panel.add(new Drawable(x, y, width, height, Drawable.IMAGE, getImage(death), action, "Game over"));
                break;
            case 6: // Culture
                _y = 370;
                _x = 177;

                panel.add(new Drawable(510, 310, 510, 570, Color.BLUE, Drawable.LINE, 1));

                Font cfont = null;
                Color left = Color.RED;
                Color right = Color.BLUE;
                Color pink = new Color(220, 0, 255);
                for (int k = 0, l = Player.cities.length; k < l; k++) {
                    if (Player.cities[k].equals(player.city))
                        cfont = new Font("Arial", 2, 10);
                    else
                        cfont = statusFont;
                    int culture = player.getCulture(Player.cities[k]);

                    if (culture < 0) {
                        if (culture > -16)
                            left = pink;
                        else
                            left = Color.RED;
                        panel.add(new Drawable(510 + (culture * 2), 315 + (k * 15), -(culture * 2), 10, Drawable.RECTANGLE, "", null, left));
                        panel.add(new Drawable(515, 312 + (k * 15), 100, 10, Drawable.LABEL, Player.niceCities[k], cfont, Color.BLACK));
                    } else {
                        if (culture < 16)
                            right = pink;
                        else
                            right = Color.BLUE;
                        panel.add(new Drawable(510, 315 + (k * 15), (culture * 2), 10, Drawable.RECTANGLE, "", null, right));
                        Drawable dc = new Drawable(505, 312 + (k * 15), 100, 10, Drawable.LABEL, Player.niceCities[k], cfont, Color.BLACK);
                        dc.x -= dc.textWidth;
                        panel.add(dc);
                    }
                }

                // meaning of red
                panel.add(new Drawable(760, 475, 10, 10, Drawable.RECTANGLE, "", null, Color.RED));
                panel.add(new Drawable(773, 472, 140, 10, Drawable.LABEL, "European influence strong", cfont, Color.BLACK));

                // ..blue
                panel.add(new Drawable(760, 500, 10, 10, Drawable.RECTANGLE, "", null, Color.BLUE));
                panel.add(new Drawable(773, 497, 140, 10, Drawable.LABEL, "My influence strong", cfont, Color.BLACK));

                // ..and the meaning of pink :-)
                panel.add(new Drawable(760, 525, 10, 10, Drawable.RECTANGLE, "", null, pink));
                panel.add(new Drawable(773, 522, 140, 10, Drawable.LABEL, "Close to neutral", cfont, Color.BLACK));

                break;
            }

            Color color = Color.BLACK;
            if (state == 0)
                color = Color.RED;
            else
                color = Color.BLACK;
            panel.add(new Drawable(170, 570, 80, 20, Drawable.LABEL, "Status", "mf.isStatus=false;mf.gotoStatus(0, \"\", " + page + ", " + state + ", 0);",
                    fontBold, color));
            if (state == 1)
                color = Color.RED;
            else
                color = Color.BLACK;
            panel.add(new Drawable(270, 570, 80, 20, Drawable.LABEL, "Inventory", "mf.player.chooseFace();mf.isStatus=false;mf.gotoStatus(1, \"\", " + page
                    + ", " + state + ", 0);", fontBold, color));
            if (state == 2)
                color = Color.RED;
            else
                color = Color.BLACK;
            panel.add(new Drawable(370, 570, 80, 20, Drawable.LABEL, "Diary", "mf.player.chooseFace();mf.isStatus=false;mf.gotoStatus(2, \"\", " + page + ", "
                    + state + ", 0);", fontBold, color));
            if (state == 3)
                color = Color.RED;
            else
                color = Color.BLACK;
            panel.add(new Drawable(470, 570, 80, 20, Drawable.LABEL, "Help", "mf.player.chooseFace();mf.isStatus=false;mf.gotoStatus(3, \"\", " + page
                    + ", 0 , 0);", fontBold, color));
            if (state == 4)
                color = Color.RED;
            else
                color = Color.BLACK;
            panel.add(new Drawable(570, 570, 80, 20, Drawable.LABEL, "Log", "mf.player.chooseFace();mf.isStatus=true;mf.gotoStatus(4, \"\", " + page + ", "
                    + state + ", (mf.player.log.size()-1));", fontBold, color));
            if (state == 6)
                color = Color.RED;
            else
                color = Color.BLACK;
            panel.add(new Drawable(670, 570, 80, 20, Drawable.LABEL, "Culture", "mf.player.chooseFace();mf.isStatus=false;mf.gotoStatus(6, \"\", " + page
                    + ", " + state + ", 0);", fontBold, color));
            if (state == 5)
                color = Color.RED;
            else
                color = Color.BLACK;
            String go = "gameoverparadise";
            if (player.moral < 0) go = "gameoverhell";
            if (player.inGameOver)
                panel.add(new Drawable(850, 570, 60, 20, Drawable.LABEL, "DEATH!", "mf.showStatus=false;mf.player.logo=\"logoDeath2\";mf.gotoPlace(\"" + go + "\");",
                        fontBold, color));
            else
                panel.add(new Drawable(850, 570, 60, 20, Drawable.LABEL, "Close", "mf.player.chooseFace();mf.isStatus=false;mf.gotoPlace(mf.player.place);",
                        fontBold, color));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        try {
            // XStream.. gotta love it! I mean, how much less code can you
            // write?
            String save = xstream.toXML(player);
            FileDialog fd = new FileDialog(this, "Save game", FileDialog.SAVE);
            fd.setDirectory(".");
            fd.setFile("spicetrade.sav");
            fd.setVisible(true);
            // FIX: 12.4.2005 Changed the loading and save routine so that the directory information is used also
            String file = fd.getDirectory() + fd.getFile();
            if (file == null) return;
            tools.writeFile(file, save);
            sounds.playSound("/music/fx_signal_bell_hitlink.ogg");
            //tools.showMessage(this, "Game saved.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveSettings() {
        try {
            Settings settings = new Settings();
            settings.difficulty = player.difficulty;
            settings.musicOn = sounds.musicOn;
            settings.fullScreen = player.fullScreen;
            String save = xstream.toXML(settings);
            tools.writeFile("spicetrade.properties", save);
        } catch (Exception ex) {
            if (Mainframe.DEBUG == 1) ex.printStackTrace();
        }
    }

    public void loadSettings() {
        try {
            String load = tools.readFile("spicetrade.properties", false);
            Settings settings = new Settings();
            if (load != null && !load.equals(""))
                settings = (Settings) xstream.fromXML(load);
            player.difficulty = settings.difficulty;
            sounds.musicOn = settings.musicOn;
            player.fullScreen = settings.fullScreen;
        } catch (Exception ex) {
            if (Mainframe.DEBUG == 1) ex.printStackTrace();
        }
    }

    public void load() {
        try {
            sounds.stopAll();
            FileDialog fd = new FileDialog(this, "Load game", FileDialog.LOAD);
            fd.setDirectory(".");
            fd.setFile("spicetrade.sav");
            fd.setVisible(true);
            // FIX: 12.4.2005 Changed the loading and save routine so that the directory information is used also
            String file = fd.getDirectory() + fd.getFile();            
            if (fd.getFile() == null) return;
            String load = tools.readFile(file, false);
            sounds.playSound("/music/fx_signal_bell_hitlink.ogg");
            this.player = (Player) xstream.fromXML(load);
            this.showStatus=true;
            //tools.showMessage(this, "Game loaded.");
            // check if the player has a travel permit
            if (player.hasItem("10620") || player.hasItem("10630"))
                this.showMapGlobe = true;
            player.chooseFace();
            refresh();
            Thread.sleep(50);
            gotoPlace(player.place);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Image getImage(String image) {
        return getImage("", image, false);
    }

    public Image getImage(String name, boolean cache) {
        return getImage(name, "", cache);
    }

    public Image getImage(String name, String image, boolean cache) {
        return getImage(name, image, cache, false);
    }

    public Image getImage(String name, String image, boolean cache, boolean removeCache) {
        // I'm not all together happy with this implementation, but will have to
        // do for a while
        if (Mainframe.DEBUG == 2)
            if (!name.equals(""))
                System.out.println("Getting image from cache: " + name);
            else
                System.out.println("Getting image: " + image);
        Image i = null;
        try {
            if (name == null || name.equals(""))
                name = image;
            if (images.containsKey(name) && !removeCache)
                return (Image) images.get(name);
            else {
                i = tools.loadImage(this, image);
                if (i == null) {
                    i = (Image) images.get("notavailable");
                    System.out.println("Image " + image + " not available.");
                }
                images.remove(name);

                if (cache)
                    images.put(name, i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            i = (Image) images.get("notavailable");
        }
        return i;
    }

    public boolean rB(int lower, int upper) {
        return (nextInt >= lower && nextInt < upper);

    }

    public boolean rMore(int limit) {
        return (nextInt > limit);
    }

    public boolean rLess(int limit) {
        return (nextInt < limit);
    }

    public void exit() {
        System.exit(0);
    }

    public void this_windowClosed(WindowEvent e) {
        System.exit(0);
    }

    public void this_mouseClicked(MouseEvent e) {
        try {
            // to prevent the user from double clicking on icons that generate
            // an animated action
            if (player.place.equals("fields") || player.place.equals("witchmountain") || player.place.equals("hermitcave"))
                Thread.sleep(1000);
            if (Mainframe.DEBUG == 1)
                System.out.print(e.getPoint().x + "," + e.getPoint().y + ";");
            Vector v = null;
            boolean foundp = false;
            if (isModal)
                v = panel;
            else
                v = objects;

            Drawable d = null;
            String text = null;
            for (int i = v.size() - 1; i >= this.converter; i--) {
                d = (Drawable) v.elementAt(i);
                if (!d.contains(e.getPoint()) || d.action == null)
                    continue;
                if (Mainframe.DEBUG == 1)
                    System.out.println("Doing action: " + d.action);

                if (player.place.equals("map"))
                    text = d.text;
                else if (d.niceText != null)
                    text = d.niceText;
                else if (d.text != null)
                    text = d.text;
                else
                    text = d.action;

                if (!isStatus && !"Minimize".equals(text) && !"Load".equals(text) && !"Save".equals(text) && !"Close".equals(text))
                    player.addLog(text, d.action, (player.day + player.month * 30 + player.year * 360));
                bsh.eval(bshimport + d.action);
                foundp = true;
                break;
            }

            if (!foundp && !isModal)
                for (int i = glass.size() - 1; i >= 0; i--) {
                    d = (Drawable) glass.elementAt(i);
                    if (!d.contains(e.getPoint()) || d.action == null)
                        continue;
                    if (Mainframe.DEBUG == 1)
                        System.out.println("Doing action: " + d.action);

                    if (player.place.equals("map"))
                        text = d.text;
                    else if (d.niceText != null)
                        text = d.niceText;
                    else if (d.text != null)
                        text = d.text;
                    else
                        text = d.action;
                    if (!isStatus)
                        player.addLog(text, d.action, (player.day + player.month * 30 + player.year * 360));
                    bsh.eval(bshimport + d.action);
                    break;
                }
        } catch (Exception ex) {
            if (Mainframe.DEBUG==1) ex.printStackTrace();
        }
    }

    public void this_mouseMoved(MouseEvent e) {
        try {
            // this is for the flyover
            if (idle) {
                player.chooseFace();
                paintGlass();
                idle = false;
            }

            Vector v = null;
            if (isModal)
                v = panel;
            else
                v = objects;

            Drawable d = null;
            int x = 0;
            int y = 0;
            int w = getWidth();
            int h = getHeight();
            String t = "";
            String old = "";
            boolean repaintp = false;
            boolean foundp = false;
            if (flyover != null)
                repaintp = true;

            flyover = null;

            for (int i = v.size() - 1; i >= this.converter; i--) {
                d = (Drawable) v.elementAt(i);
                if (!d.contains(e.getPoint()) || d.action == null) {
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    continue;
                }
                this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                x = e.getX();
                y = e.getY();

                if (d.niceText != null)
                    t = d.niceText;
                else if (d.text != null)
                    t = d.text;
                else
                    t = d.action;

                flyover = new Drawable(x + 10, y + 10, 300, 20, Drawable.FLYOVER, t);
                repaintp = true;
                foundp = true;
                break;
            }

            if (!foundp && !isModal)
                for (int i = glass.size() - 1; i >= 0; i--) {
                    d = (Drawable) glass.elementAt(i);
                    if (!d.contains(e.getPoint()) || d.action == null) {
                        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        continue;
                    }
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    x = e.getX();
                    y = e.getY();

                    if (d.niceText != null)
                        t = d.niceText;
                    else if (d.text != null)
                        t = d.text;
                    else
                        t = d.action;

                    flyover = new Drawable(x + 10, y + 10, 300, 20, 5, t);
                    repaintp = true;
                    break;
                }

            if (repaintp) {
                if (flyover != null) {
                    int tw = flyover.textWidth;

                    if (x + (tw * 2) > w) {
                        x = x - (tw + 30);
                    }
                    if (y + 40 > h)
                        y = y - 40;
                    flyover.x = x + 10;
                    flyover.y = y + 10;
                }
                repaint();
            }
        } catch (Exception ex) {
            if (Mainframe.DEBUG==1) ex.printStackTrace();
        }
    }

    public void refresh() {
        // FIX 10.04.2005 - the journal.refresh() is required when loading game 
        player.journal.refresh();
        // this is not really required during non debugging situation
        if (Mainframe.DEBUG > 0) {
            places.refresh();
            map.refresh();
            dialogs.refresh();
            market.refresh();
            traders.refresh();
            armies.refresh();
            actions.refresh();
            gotoPlace(player.place);
        }
    }

    // not used anymore
    public void createTimer(String action, int delay, int speed, int times) {
        createTimer(action, delay, speed, times, false, "");
    }

    // not used anymore
    public void createTimer(String action, int delay, int speed, int times, String doAfter) {
        createTimer(action, delay, speed, times, false, doAfter);
    }

    // not used anymore
    public void createTimer(String action, int delay, int speed, int times, boolean gotoPlace) {
        createTimer(action, delay, speed, times, gotoPlace, "");
    }

    // This is the real deal
    public void createTask(String action, int delay, int speed, int times) {
        createTask(action, delay, speed, times, "");
    }

    // why use a selfmade timer? because pausing and all that stuff just got too
    // hairy with the
    // stock Java timer..
    public void createTask(String action, int delay, int speed, int times, String doAfter) {
        timer2 = new Task(action, times, false, doAfter, speed);
        timer2.start();
    }

    // .. so why didn't I write one my self in the beginning? I just felt like
    // trying the regular one, since
    // I had not done that before
    public void createTimer(String action, int delay, int speed, int times, boolean gotoPlace, String doAfter) {
        if (Mainframe.DEBUG == 1)
            System.out.println("Creating timer: " + action);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimedTask(action, times, gotoPlace, doAfter), delay, speed);
    }

    class TimedTask extends TimerTask {
        String action = "";

        int times = 1;

        int counter = 1;

        Interpreter bshint = new Interpreter();

        boolean gotoPlace = false;

        String doAfter = "";

        public TimedTask(String action, int times, boolean gotoPlace, String doAfter) {
            this.action = action;
            this.times = times;
            this.gotoPlace = gotoPlace;
            this.doAfter = doAfter;
        }

        public void run() {
            try {
                String a = action.replaceAll("!counter", String.valueOf(counter++));
                times--;
                if (times < 1) {
                    timer.cancel();
                    a = a.replaceAll("!donep", "true");
                    if (gotoPlace)
                        gotoPlace(player.place);
                    else {
                        bshint.set("mf", me);
                        bshint.eval(bshimport + a);
                    }

                    if (doAfter != null && !doAfter.equals("")) {
                        bshint.set("mf", me);
                        bshint.eval(bshimport + doAfter);
                    }
                } else {
                    a = a.replaceAll("!donep", "false");
                    bshint.set("mf", me);
                    bshint.eval(bshimport + a);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
