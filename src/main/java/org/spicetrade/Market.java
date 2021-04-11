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

import java.util.Enumeration;
import java.util.Vector;

import org.spicetrade.tools.Collection;
import org.spicetrade.tools.Item;

public class Market extends Collection {

    public static final String xml = "/data/items.xml"; 
    
    public Market() {
        super(xml);
    }
    
    public void refresh() {
        this.refresh(xml);
    }
    
    public Item getItem(String id) {
        Item i = null;
        try {
            i = (Item)getObject(id);
            return i;
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return i;
    }

    public int getPrice(String id) {
        try {
            Item i = getItem(id);
            return i.price;
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }
    
    public Vector<Item> getHelp(String firstLetter) {
        Vector<Item> v = new Vector<>();
        
        try {
            Enumeration<String> items = collection.keys();
            while (items.hasMoreElements()) {
                Item item = this.getItem(items.nextElement());
                if (item.id.length()==4)
                    if (item.name.substring(0,1).toLowerCase().equals(firstLetter.toLowerCase()))
                    v.add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return v;
    }
}
