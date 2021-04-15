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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;

import java.util.Hashtable;
import java.util.Vector;

import org.spicetrade.Mainframe;

public class Collection {

    protected transient Hashtable<String,Object> collection;

    public Collection(String s) {
        readXML(s);
    }

    public void refresh(String s) {
        readXML(s);
    }

    @SuppressWarnings("unchecked")
    public void readXML(String s) {
        collection = new Hashtable<>();
        String xml;
        XStream xstream = new XStream();
        XStream.setupDefaultSecurity(xstream);
        xstream.addPermission(AnyTypePermission.ANY);
        if (Mainframe.DEBUG == 1) System.out.println("Reading XML file: " + s);
        xml = Mainframe.me.tools.readFile(s);
        if (Mainframe.DEBUG == 1) System.out.println("Creating the collection hashtable from XML: " + s);
        collection = (Hashtable) xstream.fromXML(xml);
    }

    @SuppressWarnings("unchecked")
    public <T> Vector<T> getVector(String s1) {
        if (Mainframe.DEBUG == 2) System.out.println("Getting vector: " + s1);
        Vector<T> res = new Vector<>();
        try {
            Vector<T> v = (Vector<T>) collection.get(s1);
            for (int i = 0, j = v.size(); i < j; i++) {
                if (v.elementAt(i) instanceof MapEntry) {
                    if (((MapEntry) v.elementAt(i)).isActive(Mainframe.me.bsh))
                        res.add(v.elementAt(i));
                } else
                    res.add(v.elementAt(i));
            }

            return res;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> Vector<T> getVector(String s1, String s2) {
        if (Mainframe.DEBUG == 2) System.out.println("Getting vector: " + s2 + " from hashtable: " + s1);
        Hashtable<?,?> ht;
        Vector<T> res = new Vector<>();
        try {
            ht = (Hashtable<?,?>) collection.get(s1);
            Vector<T> v = (Vector<T>) ht.get(s2);
            for (int i = 0, j = v.size(); i < j; i++) {
                if (v.elementAt(i) instanceof Spicebean) {
                    if (((Spicebean) v.elementAt(i)).isActive())
                        res.add(v.elementAt(i));
                } else
                    res.add(v.elementAt(i));
            }

            return res;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getString(String s1, String s2) {
        if (Mainframe.DEBUG == 2) System.out.println("Getting string: " + s2 + " from hashtable: " + s1);
        try {
            Hashtable<?,?> h = (Hashtable<?,?>) collection.get(s1);
            return (String) h.get(s2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int getInt(String s1, String s2) {
        if (Mainframe.DEBUG == 2) System.out.println("Getting int: " + s2 + " from hashtable: " + s1);
        int i = 0;
        try {
            Hashtable<?,?> h = (Hashtable<?,?>) collection.get(s1);
            String s = (String) h.get(s2);
            i = Integer.parseInt(s);
            return i;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return i;
    }

    public double getDouble(String s1, String s2) {
        if (Mainframe.DEBUG == 2) System.out.println("Getting double: " + s2 + " from hashtable: " + s1);
        double d = 0;
        try {
            Hashtable<?,?> h = (Hashtable<?,?>) collection.get(s1);
            String s = (String) h.get(s2);
            d = Double.parseDouble(s);
            return d;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return d;
    }

    public Object getObject(String s1) {
        if (Mainframe.DEBUG == 2) System.out.println("Getting object: " + s1);
        try {
            return collection.get(s1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
