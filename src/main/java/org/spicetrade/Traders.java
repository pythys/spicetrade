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

import java.util.Vector;

import org.spicetrade.tools.Collection;
import org.spicetrade.tools.Item;

public class Traders extends Collection {

    public static final String xml = "/data/traders.xml";

    public Traders() {
        super(xml);
    }

    public void refresh() {
        this.refresh(xml);
    }

    public String getName(String s) {
        return getString(s, "Name");
    }

    public String getDisplay(String s) {
        return getString(s, "Display");
    }

    public int getDescription(String s) {
        return getInt(s, "Description");
    }

    public double getBuyFactor(String s) {
        return getDouble(s, "Buy factor");
    }

    public double getSellFactor(String s) {
        return getDouble(s, "Sell factor");
    }

    public int getSellPrice(String who, String id) {
        Mainframe mf = Mainframe.me;
        Item item = mf.market.getItem(id);
        int ret = (int) (item.price * getSellFactor(who));
        if (mf.player.difficulty==0) ret = (int) (ret*1.15);
        return ret;
    }

    public int getBuyPrice(String who, String id) {
        Mainframe mf = Mainframe.me;
        Item item = mf.market.getItem(id);
        int ret = (int) (item.price * getBuyFactor(who));
        if (mf.player.difficulty==0) ret = (int) (ret*0.85);
        return ret;
    }

    public Vector<Item> getBuyItems(String who) {
        return getItems("Buy items", who, false);
    }

    public Vector<Item> getSellItems(String who) {
        return getItems("Sell items", who, true);
    }

    public Vector<Item> getItems(String what, String who, boolean selling) {
        Mainframe mf = Mainframe.me;
        Vector<Item> res = new Vector<>();
        try {
            Vector<?> v = getVector(who, what);
            Item item = new Item();
            for (Object o : v) {
                String s = (String) o;
                item = mf.market.getItem(s);
                if (!selling && (item.id.equals("10100") || item.id.equals("10110") || item.id.equals("10120") || item.id.equals("10130") || item.id.equals("10200") || item.id.equals("10210") || item.id.equals("10220") || item.id.equals("10230")) && !mf.player.has(item.id))
                    res.add(item);
                else if (mf.player.hasItem(item.id) && selling && !mf.player.hasSold(item.id))
                    res.add(item);
                else if (!mf.player.hasSold(item.id, who) && !mf.player.hasItem(item.id) && !selling)
                    res.add(item);
            }
            if (!selling) {
                Vector<?> soldItems = mf.player.getSoldItems(who);
                for (Object o : soldItems) {
                    Item soldItem = (Item) o;
                    res.add(soldItem);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }
}