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

import java.awt.*;
import java.util.Vector;

import org.spicetrade.*;

public class Drawable extends Rectangle {

    public static final int STATUS = 0;

    public static final int BUTTON = 1;

    public static final int LABEL = 2;

    public static final int IMAGE = 3;

    public static final int PANEL = 4;

    public static final int FLYOVER = 5;

    public static final int TIMAGE = 6;

    public static final int LINE = 7;

    public static final int CIRCLE = 8;

    public static final int RECTANGLE = 9;

    public static final int MPANEL = 10;

    public static final int BPANEL = 12;

    public int type;

    public int x;

    public int y;

    public int x2;

    public int y2;

    public int width;

    public int height;

    public int xarc;

    public int yarc;

    public float alpha;

    public Font font = null;

    public Color color = null;

    public Image image;

    public Image image2;

    public String text;

    public Vector textVector = null;

    public int textWidth = 0;

    public String action;

    public String niceText;

    public Drawable(int x, int y, int x2, int y2, int type) {
        // Lines and circles
        this(x, y, x2, y2, null, type);
    }

    public Drawable(int x, int y, int x2, int y2, Color color, int type, float alpha) {
        // Lines and circles with alpha
        this(x, y, x2, y2, color, type);
        this.alpha = alpha;
    }

    public Drawable(int x, int y, int x2, int y2, Color color, int type) {
        // Lines and circles with color
        super(0, 0, 0, 0);
        this.type = type;
        this.x = x;
        this.y = y;
        this.color = color;
        if (type == Drawable.LINE) {
            this.x2 = x2;
            this.y2 = y2;
        } else {
            this.width = x2;
            this.height = y2;
        }
    }

    public Drawable(int x, int y, int width, int height, int type, Color color) {
        // ??
        this(x, y, width, height, type, null, null, null, null, null, 0, null, color);
    }

    public Drawable(int x, int y, int width, int height, int type, Image image) {
        // Images
        this(x, y, width, height, type, image, null, null, null, null, 0, null, null);
    }

    public Drawable(int x, int y, int width, int height, int type, Image image, float alpha) {
        // Transparent images
        this(x, y, width, height, type, image, null, null, null, null, alpha, null, null);
    }

    public Drawable(int x, int y, int width, int height, int type, Image image, String text) {
        // Panels
        this(x, y, width, height, type, image, null, null, text, null, 0, null, null);
    }

    public Drawable(int x, int y, int width, int height, int type, Image image, String action, String text) {
        // MPanels and clickable images
        this(x, y, width, height, type, image, null, action, text, null, 0, null, null);
    }

    public Drawable(int x, int y, int width, int height, int type, Image image, Image image2, String text) {
        // Status dialog
        this(x, y, width, height, type, image, image2, null, text, null, 0, null, null);
    }

    public Drawable(int x, int y, int width, int height, int type, Image image, Image image2, String text, Color color) {
        // Status dialog
        this(x, y, width, height, type, image, image2, null, text, null, 0, null, color);
    }

    public Drawable(int x, int y, int width, int height, int type, Image image, Image image2, String action, String text) {
        // Status bar
        this(x, y, width, height, type, image, image2, action, text, null, 0, null, null);
    }

    public Drawable(int x, int y, int width, int height, int type, String text) {
        // Label
        this(x, y, width, height, type, null, null, null, text, null, 0, null, null);
    }

    public Drawable(int x, int y, int width, int height, int type, String text, Font font, Color color) {
        // Label with font and color
        this(x, y, width, height, type, null, null, null, text, null, 0, font, color);
    }

    public Drawable(int x, int y, int width, int height, int type, String text, String action, Font font, Color color) {
        // Clickable label with font and color
        this(x, y, width, height, type, null, null, action, text, null, 0, font, color);
    }

    public Drawable(int x, int y, int width, int height, int type, String text, String action, String niceText, Font font, Color color) {
        // Clickable label with font, color and niceText
        this(x, y, width, height, type, null, null, action, text, niceText, 0, font, color);
    }

    public Drawable(int x, int y, int width, int height, int type, String text, String action) {
        // Clickable label
        this(x, y, width, height, type, null, null, action, text, null, 0, null, null);
    }

    public Drawable(int x, int y, int width, int height, int type, String text, String action, String niceText) {
        // Clickable label
        this(x, y, width, height, type, null, null, action, text, niceText, 0, null, null);
    }

    public Drawable(int x, int y, int width, int height, int type, Image image, Image image2, String action, String text, String niceText, float alpha,
            Font font, Color color) {
        super(x, y, width, height);
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.image2 = image2;
        this.text = text;
        if (action == null || action.equals(""))
            this.action = null;
        else
            this.action = action;
        this.niceText = niceText;
        this.alpha = alpha;
        this.font = font;
        this.color = color;
        if (text != null) {
            Graphics2D g2 = (Graphics2D) Mainframe.me.getGraphics();
            if (font == null)
                g2.setFont(Mainframe.me.font);
            else
                g2.setFont(font);
            textVector = chop(text, width, g2);
            textWidth = ((Integer) textVector.elementAt(0)).intValue();
        }
    }

    public void draw(Graphics g) {
        try {
            Mainframe f = Mainframe.me;
            Graphics2D g2 = (Graphics2D) g;
            Stroke s = g2.getStroke();
            Composite old = g2.getComposite();
            FontMetrics fm = g2.getFontMetrics();
            int i = 0;
            int _y = 0;
            int increment = 0;
            g.setColor(Color.white);
            switch (type) {
            default:
                break;

            case 0: // status
                int h = f.getHeight();
                int ty = y + 149;
                int iy = ty - 12;
                int maxbar = f.getHeight() - y - 35;
                int bar = 0;
                g.drawImage(image2, 100, y + 137, f);
                g.drawImage(image, 20, y + 2, f);
                g.setColor(Color.BLACK);

                // age
                g.setColor(new Color(255, 0, 255));
                g.setFont(new Font("Arial", 1, 12));
                //g.drawString(f.player.day + "." + f.player.month + "." + f.player.age, 390, ty);
                g.setColor(new Color(0, 0, 0));
                g.drawString(f.player.nicecity, 1000 - fm.stringWidth(f.player.nicecity), ty);
                g.setFont(f.font);
                g2.setComposite(AlphaComposite.getInstance(3, 0.54F));
                g.setColor(Color.white);
                g2.fillRect(375, iy - f.player.age, 30, f.player.age);
                g.setColor(Color.white);

                // health
                bar = (int) f.player.health;
                if (bar > maxbar)
                    bar = maxbar;
                if (f.player.health > 0)
                    g2.fillRect(475, iy - bar, 30, bar);

                // money
                bar = f.player.money / 100;
                if (bar > maxbar)
                    bar = maxbar;
                if (f.player.money > 0)
                    g2.fillRect(585, iy - bar, 30, bar);

                // culture
                bar = f.player.culture;
                if (bar > maxbar)
                    bar = maxbar;
                if (f.player.culture > 0)
                    g2.fillRect(705, iy - bar, 30, bar);

                //                // wives
                //                bar = f.player.wifes * 10;
                //                if (bar > maxbar)
                //                    bar = maxbar;
                //                if (f.player.wifes > 0)
                //                    g2.fillRect(812, iy - bar, 30, bar);
                //
                //                // children
                //                bar = f.player.children * 5;
                //                if (bar > maxbar)
                //                    bar = maxbar;
                //                if (f.player.children > 0)
                //                    g2.fillRect(933, iy - bar, 30, bar);

                g2.setComposite(old);
                break;

            case 1: // buttons (not used anymore
                g2.setComposite(AlphaComposite.getInstance(3, 0.74F));
                g.setColor(Color.gray);
                g2.fillRoundRect(x + 4, y + 4, width, height, xarc, yarc);
                g.setColor(Color.white);
                if (action.indexOf("gotoPlace") == -1)
                    g.setColor(Color.red);
                g2.fillRoundRect(x, y, width, height, xarc, yarc);
                g.setColor(Color.BLACK);
                g2.drawRoundRect(x, y, width, height, 15, 15);
                g.drawString(text, x + 10, y + 20);
                g2.setComposite(old);
                break;

            case 2: // label
                increment = 18;
                if (font != null) {
                    switch (font.getSize()) {
                    case 10:
                        increment = 11;
                        break;
                    }
                }

                _y = y + increment;
                g.setColor(Color.BLACK);
                if (action == null || action.equals("")) {
                } else
                    g.setFont(f.fontBold);
                if (font != null) {
                    g.setFont(font);
                    g.setColor(color);
                }
                i = 1;
                for (int j = textVector.size(); i < j; i++) {
                    g.drawString((String) textVector.elementAt(i), x, _y);
                    _y += increment;
                }
                g.setFont(f.font);
                break;

            case 3: // image
                g.drawImage(image, x, y, f);
                break;

            case 4: // panel
                if (color == null)
                    g.setColor(Color.white);
                else
                    g.setColor(color);
                if (alpha == 0)
                    g2.setComposite(AlphaComposite.getInstance(3, 0.84F));
                else
                    g2.setComposite(AlphaComposite.getInstance(3, alpha));
                g2.fillRect(x, y, width, height);
                g.setColor(Color.BLACK);
                g2.setComposite(old);
                g.setColor(Color.white);
                if (image != null)
                    g.drawImage(image, x + 25, y + 25, f);
                if (image2 != null)
                    g.drawImage(image2, x + 260, y + 25, f);

                break;

            case 5: // flyover
                _y = y + 12;
                if (text != null) {
                    g.setColor(Color.white);
                    g2.setComposite(AlphaComposite.getInstance(3, 0.64F));
                    g2.fillRect(x, y, ((Integer) textVector.elementAt(0)).intValue() + 5, height * (textVector.size() - 1));
                    g.setColor(Color.BLACK);
                    g2.setComposite(old);
                    i = 1;
                    for (int j = textVector.size(); i < j; i++) {
                        g.drawString((String) textVector.elementAt(i), x + 2, _y + 2);
                        _y += 18;
                    }

                }
                g.setColor(Color.white);
                break;

            case 6: // transparent image
                g2.setComposite(AlphaComposite.getInstance(3, alpha));
                g.drawImage(image, x, y, f);
                g2.setComposite(old);
                break;

            case 7: // line
                if (color != null)
                    g.setColor(color);
                else
                    g.setColor(Color.RED);
                if (alpha == 0)
                    g2.setStroke(new BasicStroke(7.0F));
                else
                    g2.setStroke(new BasicStroke(alpha));
                g2.drawLine(x, y, x2, y2);
                g2.setStroke(s);
                break;

            case 8: // circle
                if (color != null)
                    g.setColor(color);
                else
                    g.setColor(Color.RED);
                g2.setStroke(new BasicStroke(5.0F));
                g2.drawOval(x, y, width, height);
                g2.setStroke(s);
                break;

            case 9: // rectangle
                if (color != null)
                    g.setColor(color);
                else
                    g.setColor(Color.RED);
                g.fillRect(x, y, width, height);
                break;

            case 10: // action panel
                if (text != null) {
                    g.setColor(Color.white);
                    g2.setComposite(AlphaComposite.getInstance(3, 0.84F));
                    g2.fillRect(x, y, width, height);
                    g.setColor(Color.BLACK);
                    g2.setComposite(old);
                    i = 1;
                    _y = y;
                    String t = "";
                    for (int j = textVector.size(); i < j; i++) {
                        t = (String) textVector.elementAt(i);
                        g.drawString(t, (x + width / 2) - (stringSize(t, g2) / 2), (_y + height) - ((textVector.size()) * 18));
                        _y += 18;
                    }
                    g.setColor(Color.white);
                }

                if (image2 != null) {
                    g2.setComposite(AlphaComposite.getInstance(3, ((float) 1 - alpha)));
                    g.drawImage(image, (x + width / 2) - 75, (y + height / 2) - image.getHeight(f) / 2 - 20, f);
                    g2.setComposite(AlphaComposite.getInstance(3, alpha));
                    g.drawImage(image2, (x + width / 2) - 75, (y + height / 2) - image2.getHeight(f) / 2 - 20, f);
                    g2.setComposite(old);
                } else
                    g.drawImage(image, (x + width / 2) - 75, (y + height / 2) - image.getHeight(f) / 2 - 20, f);

                break;
            case 11: // clickable panel TODO: why is this here?
                g.setColor(Color.white);
                g2.setComposite(AlphaComposite.getInstance(3, 0.84F));
                g2.fillRect(x, y, width, height);
                g.setColor(Color.BLACK);
                g2.setComposite(old);
                i = 1;
                _y = y;
                String t = "";
                for (int j = textVector.size(); i < j; i++) {
                    t = (String) textVector.elementAt(i);
                    g.drawString(t, (x + width / 2) - (stringSize(t, g2) / 2), _y + 35 + image.getHeight(f));
                    _y += 18;
                }
                g.setColor(Color.white);
                break;
            case 12: // battle and shop panel
                g.setColor(Color.white);
                g2.setComposite(AlphaComposite.getInstance(3, 0.84F));
                g2.fillRect(x, y, width, height);
                g.setColor(Color.BLACK);
                g2.setComposite(old);
                g.setColor(Color.BLACK);
                g.drawLine(x + (width / 2), y + 50, x + (width / 2), y + height - 80);
                g.drawImage(image, x + 206, y + 25, f);
                g.drawString(text, x + 240, y + height - 42);
                g.setColor(Color.WHITE);

                break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Vector chop(String text, int width, Graphics2D g2) {
        StringBuffer line = new StringBuffer();
        StringBuffer leftover = new StringBuffer();
        Vector res = new Vector();
        FontMetrics fontMetrics = g2.getFontMetrics();
        int size = 0;
        line.setLength(0);
        line.append(text);
        int a = 0;
        int b = line.length();

        while (a < b) {
            if (fontMetrics.stringWidth(leftover.toString()) < width) {
                if (line.charAt(a) != '\u00A7')
                    leftover.append(line.charAt(a));
                else {
                    if (fontMetrics.stringWidth(leftover.toString()) > size)
                        size = fontMetrics.stringWidth(leftover.toString());
                    res.add(leftover.toString());
                    leftover.setLength(0);
                }

            } else {
                for (int i = 0, j = leftover.length() - 1; i < j; j--) {
                    if (leftover.charAt(j) == ' ' || leftover.charAt(j) == '\n' || leftover.charAt(j) == ',' || leftover.charAt(j) == '.'
                            || leftover.charAt(i) == '/' || leftover.charAt(j) == '@')
                        break;
                    a--;
                    leftover.setLength(leftover.length() - 1);
                }
                a--;
                if (fontMetrics.stringWidth(leftover.toString()) > size)
                    size = fontMetrics.stringWidth(leftover.toString());
                res.add(leftover.toString());
                leftover.setLength(0);
            }
            a++;
        }
        if (fontMetrics.stringWidth(leftover.toString()) > size)
            size = fontMetrics.stringWidth(leftover.toString());
        res.add(leftover.toString());
        res.insertElementAt(Integer.valueOf(size), 0);
        return res;
    }

    private Vector chop2(String text, int width, Graphics2D g2) {
        StringBuffer line = new StringBuffer();
        StringBuffer leftover = new StringBuffer();
        Vector res = new Vector();
        FontMetrics fontMetrics = g2.getFontMetrics();
        line.setLength(0);
        line.append(text);
        int i = 0;
        for (; line.length() > 0; leftover.setLength(0)) {
            while (fontMetrics.stringWidth(line.toString()) > width)
                for (i = line.length() - 1; i > 0;) {
                    leftover.insert(0, line.charAt(i));

                    line.setLength(line.length() - 1);
                    i--;
                    if (line.charAt(i) == ' ' || line.charAt(i) == '\n' || line.charAt(i) == ',' || line.charAt(i) == '.' || line.charAt(i) == '/'
                            || line.charAt(i) == '@')
                        break;
                }

            res.add(line.toString());
            line.setLength(0);
            line.append(leftover.toString());
        }

        return res;
    }

    private int stringSize(String text, Graphics2D g2) {
        FontMetrics fontMetrics = g2.getFontMetrics();
        int size = fontMetrics.stringWidth(text);
        return size;
    }
}
