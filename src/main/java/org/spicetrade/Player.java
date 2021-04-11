/*
 * Spice Trade Copyright (C) 2005 spicetrade.org
 * 
 * Author: Juha Holopainen, juhah@spicetrade.org
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Library General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Library General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.spicetrade;

import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import org.spicetrade.tools.Item;
import org.spicetrade.tools.LogItem;
import org.spicetrade.tools.MapEntry;

public class Player {

    public Hashtable<String,String> attr;
    public Vector<Item> soldItems;
    public Vector<Item> items;
    public Vector<LogItem> log;
    public boolean fullScreen = false;
    public int x;
    public int y;
    public int xHomeCountry;
    public int yHomeCountry;
    public int year;
    public int month;
    public int day;
    public int age;
    public String ageType;
    public double health;
    public int money;
    public int happiness;
    public int moral;
    public int force;
    public int baseForce;
    public int culture;
    public int economy;
    public int wifes;
    public int children;
    public String place;
    public String city;
    public String nicecity;
    public String lastPlace;
    public String logo;
    public String lastWords;
    public boolean inGame;
    public boolean inBattle;
    public boolean inGameOver;
    public int transport;
    public Journal journal;
    public boolean addictedHashish;
    public boolean addictedOpium;
    public boolean sickPlague;
    public boolean sickGeneral;
    public boolean sickPoisoned;
    public int deathType = 0;
    public int difficulty = 1;
    public String statusFace = "/pics/navigation/abus/abu_young01.png";

    public final static String[] cities = {
            "baghdad",
            "anjoudan",
            "najaf",
            "latakia",
            "konya",
            "baku",
            "constantinopol",
            "venice",
            "madrid",
            "lisboa",
            "budapest",
            "vienna",
            "moscow",
            "hamburg",
            "amsterdam",
            "london",
            "paris"
    };

    public final static String[] niceCities = {
            "Baghdad",
            "Anjoudan",
            "Najaf",
            "al-Ladiqiyah",
            "Konya",
            "Baku",
            "Constantinople",
            "Venice",
            "Madrid",
            "Lisbon",
            "Budapest",
            "Vienna",
            "Moscow",
            "Hamburg",
            "Amsterdam",
            "London",
            "Paris"
    };

    public Player() {
        this(1);
    }

    public Player(int difficulty) {
        this.difficulty = difficulty;
        attr = new Hashtable<>();
        soldItems = new Vector<>();
        items = new Vector<>();
        journal = new Journal();
        log = new Vector<>();
        x = 0;
        y = 0;
        xHomeCountry = 0;
        yHomeCountry = 0;
        moral = 0;
        inGameOver = false;
        addictedHashish = false;
        addictedOpium = false;
        sickPlague = false;
        inBattle = false;
        lastWords = "";
        ageType = "young";
        attr.put("Name", "Abu Mansur 'abd ar-Rahman al-Qazzaz");
        attr.put("dateAndPlace", "C. 500 AH, Baghdad");
        attr.put("wifes", "");
        attr.put("children", "");

        Random r = new Random();
        int c = 0;
        c = r.nextInt(100);
        while (c < 60)
            c = r.nextInt(100);
        attr.put("culturebaghdad", String.valueOf(c));

        c = r.nextInt(100);
        while (c < 40)
            c = r.nextInt(100);
        attr.put("cultureanjoudan", String.valueOf(c));

        c = r.nextInt(100);
        while (c < 40)
            c = r.nextInt(100);
        attr.put("culturenajaf", String.valueOf(c));

        c = r.nextInt(100);
        while (c < 40)
            c = r.nextInt(100);
        attr.put("culturelatakia", String.valueOf(c));

        c = r.nextInt(100);
        while (c < 20)
            c = r.nextInt(100);
        attr.put("cultureconstantinopol", String.valueOf(c));

        c = r.nextInt(100);
        while (c < 30)
            c = r.nextInt(100);
        attr.put("culturekonya", String.valueOf(c));

        c = r.nextInt(100);
        while (c < 30)
            c = r.nextInt(100);
        attr.put("culturebaku", String.valueOf(c));

        c = r.nextInt(100);
        while (c < 30)
            c = r.nextInt(100);
        attr.put("culturevienna", String.valueOf(-c));

        c = r.nextInt(100);
        while (c < 30)
            c = r.nextInt(100);
        attr.put("culturebudapest", String.valueOf(-c));

        c = r.nextInt(100);
        while (c < 60)
            c = r.nextInt(100);
        attr.put("culturevenice", String.valueOf(-c));

        c = r.nextInt(100);
        while (c < 50)
            c = r.nextInt(100);
        attr.put("culturemadrid", String.valueOf(-c));

        c = r.nextInt(100);
        while (c < 40)
            c = r.nextInt(100);
        attr.put("culturelisboa", String.valueOf(-c));

        c = r.nextInt(100);
        while (c < 60)
            c = r.nextInt(100);
        attr.put("cultureparis", String.valueOf(-c));

        c = r.nextInt(100);
        while (c < 40)
            c = r.nextInt(100);
        attr.put("cultureamsterdam", String.valueOf(-c));

        c = r.nextInt(100);
        while (c < 50)
            c = r.nextInt(100);
        attr.put("culturehamburg", String.valueOf(-c));

        c = r.nextInt(100);
        while (c < 60)
            c = r.nextInt(100);
        attr.put("culturemoscow", String.valueOf(-c));

        c = r.nextInt(100);
        while (c < 60)
            c = r.nextInt(100);
        attr.put("culturelondon", String.valueOf(-c));

        attr.put("Moneyunit", "Dirham");
        attr.put("Timeunit", "AH");
        attr.put("Special", "Navigation");
        year = 500;
        month = 4;
        day = 7;
        age = 17;
        if (difficulty == 1)
            health = 50;
        else
            health = 80;
        happiness = 30;
        if (difficulty == 1)
            money = 25;
        else
            money = 100;
        if (difficulty == 1)
            baseForce = 2;
        else
            baseForce = 5;
        force = baseForce;
        economy = 0;
        culture = 0;
        wifes = 0;
        children = 0;
        place = "";
        lastPlace = "";
        logo = "logoIntro";
        to(141, 421);
        toHomeCountry(249, 135, "baghdad", "Baghdad");
        buyItem("12050", 0); // home
        buyItem("14000", 0); // fields
        buyItem("10000", 0); // basic food
        buyItem("10300", 0); // ability to walk

        if (Mainframe.DEBUG > 0) {
            buyItem("10050", 0);

            //buyItem("10600", 0);
            //buyItem("10610", 0);
            //buyItem("10620", 0);
            //buyItem("10630", 0);
            //buyItem("10640", 0);

            buyItem("10710", 0);
            buyItem("10720", 0);
            buyItem("10750", 0);
            buyItem("10751", 0);
            buyItem("10760", 0);
            buyItem("10770", 0);

            // poem books
            buyItem("10800", 0);
            buyItem("10801", 0);
            buyItem("10802", 0);
            buyItem("10803", 0);
            buyItem("10804", 0);
            buyItem("10805", 0);
            buyItem("10806", 0);
            buyItem("10807", 0);
            buyItem("10808", 0);
            buyItem("10809", 0);
            buyItem("10832", 0);
            buyItem("10833", 0);
            buyItem("10834", 0);
            buyItem("10835", 0);
            buyItem("10836", 0);
            buyItem("10837", 0);
            buyItem("10838", 0);
            buyItem("10839", 0);
            buyItem("10840", 0);
            buyItem("10841", 0);

            buyItem("10810", 0);
            buyItem("10811", 0);
            buyItem("10812", 0);
            buyItem("10820", 0);
            buyItem("10830", 0);
            buyItem("10831", 0);

            buyItem("10650", 0);

            buyItem("10320", 0);
            buyItem("10330", 0);
            buyItem("10340", 0);

            buyItem("10350", 0);
            buyItem("10325", 0);
            buyItem("10335", 0);
            buyItem("10345", 0);
            buyItem("10355", 0);
            //buyItem("10360", 0);
            //buyItem("10365", 0);

            buyItem("10720", 0);
            buyItem("10730", 0);
            buyItem("10731", 0);
            buyItem("10732", 0);
            buyItem("10733", 0);

            buyItem("10740", 0);
            buyItem("10741", 0);
            buyItem("10742", 0);
            buyItem("10743", 0);
            buyItem("10744", 0);
            buyItem("10745", 0);

            buyItem("11000", 0);
            buyItem("11001", 0);
            buyItem("11002", 0);
            buyItem("11003", 0);
            buyItem("11004", 0);
            buyItem("11005", 0);

            buyItem("11010", 0);
            buyItem("11011", 0);
            buyItem("11012", 0);
            buyItem("11013", 0);
            buyItem("11014", 0);
            buyItem("11015", 0);

            buyItem("11020", 0);

            buyItem("11030", 0);
            buyItem("11031", 0);
            buyItem("11040", 0);

            buyItem("11050", 0);
            buyItem("11051", 0);
            buyItem("11052", 0);
            buyItem("11053", 0);
            buyItem("11054", 0);
            buyItem("11055", 0);
            buyItem("11056", 0);

            buyItem("11500", 0);
            buyItem("11510", 0);
            buyItem("11520", 0);
            buyItem("11530", 0);

            buyItem("11600", 0);
            buyItem("11601", 0);
            buyItem("11602", 0);
            buyItem("11603", 0);
            buyItem("11610", 0);
            buyItem("11611", 0);
            buyItem("11620", 0);
            buyItem("11630", 0);
            buyItem("11640", 0);
            buyItem("11650", 0);
            buyItem("11651", 0);
            buyItem("11652", 0);
            buyItem("11660", 0);
            buyItem("11661", 0);
            buyItem("11670", 0);
            buyItem("11671", 0);
            buyItem("11672", 0);
            buyItem("11673", 0);
            buyItem("11680", 0);
            buyItem("11681", 0);

            buyItem("11700", 0);

            buyItem("12000", 0);
            buyItem("12001", 0);
            buyItem("12002", 0);
            buyItem("12003", 0);
            buyItem("12004", 0);

            buyItem("12010", 0);
            buyItem("12020", 0);
            buyItem("12030", 0);
            buyItem("12040", 0);

            buyItem("12060", 0);
            buyItem("12061", 0);
            buyItem("12062", 0);
            buyItem("12063", 0);
            buyItem("12064", 0);
            buyItem("12065", 0);
            buyItem("12066", 0);
            buyItem("12067", 0);
            buyItem("12068", 0);
            buyItem("12069", 0);
            buyItem("12070", 0);

            buyItem("12080", 0);
            buyItem("12081", 0);
            buyItem("12082", 0);
            buyItem("12083", 0);
            buyItem("12084", 0);
            buyItem("12085", 0);
            buyItem("12086", 0);
            buyItem("12087", 0);
            buyItem("12088", 0);
            buyItem("12089", 0);
            buyItem("12090", 0);

            buyItem("14010", 0);
            buyItem("14020", 0);
            buyItem("14030", 0);
            //buyItem("14040", 0);

            buyItem("13000", 0);
            buyItem("13010", 0);

            //buyItem("15000", 0);

            buyItem("16000", 0);
            buyItem("16010", 0);
            buyItem("16020", 0);
            buyItem("16030", 0);
        }

        Mainframe.me.showMapGlobe = false;
        inGame = false;
        transport = MapEntry.TRANSPORT1;
    }

    public String get(String s) {
        try {
            return (String) attr.get(s);
        } catch (Exception exception) {
            return null;
        }
    }

    public boolean has(String n) {
        return attr.containsKey(n);
    }

    public void add(String n) {
        attr.put(n, "");
    }

    public void add(String n, String v) {
        attr.put(n, v);
    }

    public void remove(String n) {
        attr.remove(n);
    }

    public boolean contains(String n, String v) {
        String c = "";
        if (has(n))
            c = get(n);
        return (c.contains(v));
    }

    public void addWife(String name) {
        if (contains("wifes", name))
            return;
        String all = (String) attr.get("wifes");
        if (all == null || all.equals(""))
            all = name;
        else
            all += ", " + name;
        attr.put("wifes", all);
        wifes++;
    }

    public boolean hasWife(String name) {
        return contains("wifes", name);
    }

    public void removeWife(String name) {
        if (!contains("wifes", name))
            return;
        String all = (String) attr.get("wifes");
        if (all.indexOf(name) == 0)
            if (!all.contains(","))
                all = "";
            else
                all = all.substring(all.indexOf(","), all.length());
        else
            all = all.substring(all.indexOf(", " + name) + name.length() + 2, all.length());
        attr.put("wifes", all);
        wifes--;
    }

    public void addChild(String name) {
        if (contains("children", name))
            return;
        String all = (String) attr.get("children");
        if (all == null || all.equals(""))
            all = name;
        else
            all += ", " + name;
        attr.put("children", all);
        children++;
    }

    public boolean hasChild(String name) {
        return contains("children", name);
    }

    public void removeChild(String name) {
        if (!contains("children", name))
            return;
        String all = (String) attr.get("children");
        if (all.indexOf(name) == 0)
            if (!all.contains(","))
                all = "";
            else
                all = all.substring(all.indexOf(","), all.length());
        else
            all = all.substring(all.indexOf(", " + name) + name.length() + 2, all.length());
        attr.put("children", all);
        children--;
    }

    public void addLog(String id, String action, int when) {
        if (this.inGame)
            log.add(new LogItem(id, action, when));
        if (log.size() > 100)
            log.remove(0);
    }

    public void addMoney(int amount) {
        this.money += amount;
    }

    public void removeMoney(int amount) {
        this.money -= amount;
    }

    public Vector<Item> getInventory() {
        Vector<Item> inventory = new Vector<>();
        Item item = null;

        for (int i = 0, j = items.size(); i < j; i++) {
            item = (Item) items.elementAt(i);
            if (item.inventory)
                inventory.add(item);
        }

        return inventory;
    }

    public void buyItem(String id) {
        try {
            Item item = Mainframe.me.market.getItem(id);
            buyItem(id, item.price);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void buyItem(String id, int price, boolean only) {
        try {
            if (only && !hasItem(id))
                buyItem(id, price);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void buyItem(String id, int price, String who) {
        try {
            for (int i = 0, j = soldItems.size(); i < j; i++) {
                Item item = (Item) soldItems.elementAt(i);
                if (item.who.equals(who) && item.id.equals(id)) {
                    buyItem(id, price);
                    soldItems.remove(i);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void buyItem(String id, int price) {
        try {
            Mainframe mf = Mainframe.me;
            Item item = mf.market.getItem(id);
            if (Mainframe.DEBUG == 1)
                System.out.println("Buying item: " + item.name + " for price " + price);
            money -= price;
            if (id.equals("16000") || id.equals("16010") || id.equals("16020") || id.equals("16030"))
                item.name += ", " + get("worker");
            mf.sounds.playSound("/music/fx_hit.ogg");
            items.add(item);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean hasSold(String id) {
        return hasSold(id, null);
    }

    public boolean hasSold(String id, String who) {
        boolean ret = false;

        for (Item item : soldItems) {
            if (item.id.equals(id)) {
                if (who == null || who.equals(item.who)) {
                    ret = true;
                    break;
                }
            }
        }

        return ret;
    }

    public void sellItem(String id) {
        try {
            Item item = Mainframe.me.market.getItem(id);
            sellItem(id, item.price);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sellItem(String id, int price) {
        try {
            Mainframe mf = Mainframe.me;
            if (journal.has("field2"))
                journal.done("field2", "\u00A7- I sold some spices in " + nicecity);
            money += price;
            mf.sounds.playSound("/music/fx_hit.ogg");
            removeItem(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Vector<Item> getSoldItems(String who) {
        Vector<Item> res = new Vector<>();

        try {
            for (Item item : soldItems) {
                if (who.equals(item.who))
                    res.add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return res;
    }

    public void sellItem(String id, int price, String who) {
        Mainframe mf = Mainframe.me;
        if (hasItem(id)) {
            Item item = mf.market.getItem(id);
            removeItem(id);
            item.who = who;
            money += (int) (mf.traders.getSellFactor(who) * price);
            soldItems.add(item);
        }
    }

    public boolean hasItem(String id) {
        return hasItem(id, 1);
    }

    public boolean hasItem(String id, int amount) {
        return hasItem(id, amount, true);
    }

    public boolean hasItem(String id, int amount, boolean givenItems) {
        Item item = null;
        int count = 0;
        boolean res = false;
        for (Item value : items) {
            item = (Item) value;
            if (item.id.equals(id))
                count++;
        }

        for (Item soldItem : soldItems) {
            item = (Item) soldItem;
            if (item.id.equals(id))
                count++;
        }

        if (attr.containsKey(id) && givenItems)
            count++;

        if (count >= amount)
            res = true;

        return res;
    }

    public boolean hasAnyItems(String[] ids) {
        Item item = null;
        boolean res = false;
        for (Item value : items) {
            item = (Item) value;
            for (String id : ids)
                if (item.id.equals(id)) {
                    res = true;
                    break;
                }
        }

        return res;
    }

    public boolean hasAllItems(String[] ids) {
        Item item = null;
        boolean res = false;
        for (Item value : items) {
            item = (Item) value;
            for (String id : ids)
                res = item.id.equals(id);
        }

        return res;
    }

    public boolean canBuy(String id) {
        try {
            return (this.money >= Mainframe.me.market.getPrice(id));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean hasMoney(int compare) {
        return money >= compare;
    }

    public boolean hasMoreMoney(int compare) {
        return money > compare;
    }

    public void removeItem(String id) {
        Item item = null;

        for (int i = 0, j = items.size(); i < j; i++) {
            item = (Item) items.get(i);
            if (!item.id.equals(id))
                continue;
            if (Mainframe.DEBUG == 1)
                System.out.println("Removing " + id + " from position " + i);
            items.remove(i);
            break;
        }
    }

    public void giveItem(String id) {
        if (Mainframe.DEBUG == 1)
            System.out.println("Giving item: " + id);
        removeItem(id);
        attr.put(id, "");
    }

    public boolean inPlace(String where) {
        return inPlace(where, false);
    }

    public boolean inPlace(String where, boolean exact) {
        boolean ret = false;
        if (exact)
            ret = (where.equals(place));
        else
            ret = (place.contains(where));

        if (Mainframe.DEBUG == 1)
            System.out.println("(" + place + ") inplace: " + where + ", exact: " + exact + " = " + ret);
        return ret;
    }

    public void to(int _x, int _y) {
        x = _x;
        y = _y;
    }

    public void toHomeCountry(int _x, int _y, String _city, String _nicecity) {
        xHomeCountry = _x;
        yHomeCountry = _y;
        city = _city;
        nicecity = _nicecity;
    }

    public void nextDay() {
        nextDay(false);
    }

    public void nextDay(int amount) {
        nextDay(amount, false);
    }

    public void nextDay(int amount, boolean travelling) {
        for (int i = 0; i < amount; i++)
            nextDay(travelling);
    }

    public void nextDay(boolean travelling) {
        Mainframe mf = Mainframe.me;

        if (day++ == 30)
            nextMonth(travelling);

        if (health > 300) health = 300; // have to have a max value
        
        if (travelling)
            switch (this.transport) {
            case MapEntry.TRANSPORT1: // feet
                if (this.difficulty == 1)
                    this.health -= 0.5;
                else
                    this.health -= 0.3;
                break;
            case MapEntry.TRANSPORT2: // horse
                case MapEntry.TRANSPORT3: // caravan
                case MapEntry.TRANSPORT5: // boat
                    if (this.difficulty == 1)
                    this.health -= 0.4;
                else
                    this.health -= 0.2;
                break;
                case MapEntry.TRANSPORT4: // suleiman
                case MapEntry.TRANSPORT6: // borak
                case MapEntry.TRANSPORT7: // dog
                    if (this.difficulty == 1)
                    this.health -= 0.2;
                else
                    this.health -= 0.1;
                break;
            }

        if (this.sickGeneral) {
            this.health -= 0.01;

        } else if (this.sickPlague) {
            this.health -= 0.1;
        } else if (this.sickPoisoned) {
            this.health -= 1;
        }

        if (sickPoisoned) {
            int value = Integer.parseInt((String) attr.get("poisoned"));
            value++;
            attr.put("poisoned", String.valueOf(value));
            if (value >= 100)
                mf.doActionOnEntering = "mf.gotoDeath(\"Abu died of poisoning.\", 3);";
        }

        if (this.health < 1) {
            // died, game over
            mf.doActionOnEntering = "mf.gotoDialog(\"9110\");";
        }
    }

    public void nextMonth() {
        nextMonth(false);
    }

    public void nextMonth(boolean travelling) {
        day = 1;
        month++;

        if (month > 12) {
            month = 1;
            nextYear();
        }

        int months = age * 12 + month;

        Mainframe mf = Mainframe.me;
        mf.nextInt = mf.random.nextInt(100);
        ageType = "young";

        int randomEffect = 0;
        force = baseForce;
        for (Item item : items) {
            if (item.id.length() == 5) {
                health = health + item.health;
                money = money + item.monthlyCost;
                culture = culture + item.culture;
                economy = economy + item.economy;
                force = force + item.force;
                happiness = happiness + item.happiness;
                randomEffect = randomEffect + item.random;
            }
        }

        if ((hasItem("11000") && hasItem("11001") && hasItem("11002") && hasItem("11003") && hasItem("11004") && hasItem("11005"))
                || (hasItem("11010") && hasItem("11011") && hasItem("11012") && hasItem("11013") && hasItem("11014") && hasItem("11015"))) {
            this.logo = "logoAmulet";
            happiness++;
            health += 5;
            baseForce++;
            randomEffect += 5;
        } else {
            // Yima
            if (journal.has("yima1") && !journal.contains("yima1", "cursed by Yima")) {
                if (Mainframe.DEBUG == 1)
                    System.out.println("yima random, nextInt: " + mf.nextInt + ", randomEffect: " + randomEffect);

                int value = Integer.parseInt((String) journal.get("yima1").substring(1, journal.get("yima1").length()));
                value--;
                journal.put("yima1", "!" + value);

                if (value < 1) {
                    if (mf.rB(0, 25 - randomEffect)) {
                        journal.put("yima1", "\u00A7- I was cursed by Yima");
                        journal.open("random2");
                        this.logo = "logoEvil";
                    } else if (mf.rB(25, 50 - randomEffect)) {
                        journal.put("yima1", "\u00A7- I was cursed by Yima");
                        journal.open("random3");
                        this.logo = "logoEvil";
                    } else if (mf.rB(50, 65 - randomEffect)) {
                        journal.put("yima1", "\u00A7- I was cursed by Yima");
                        journal.open("random1");
                        this.logo = "logoEvil";
                    } else if (mf.rB(65, 70 - randomEffect)) {
                        journal.put("yima1", "\u00A7- I was cursed by Yima");
                        addAddiction("plague", 10);
                        this.logo = "logoEvil";
                    }
                }
            }
        }

        // Iblis
        if (journal.has("iblis1") && journal.get("iblis1").charAt(0) == '!') {
            if (Mainframe.DEBUG == 1)
                System.out.println("iblis random, nextInt: " + mf.nextInt + ", randomEffect: " + randomEffect);
            int value = Integer.parseInt((String) journal.get("iblis1").substring(1, journal.get("iblis1").length()));
            value--;
            journal.put("iblis1", "!" + value);
            if (value <= 0 && mf.rB(0, 75 - randomEffect)) {
                mf.doActionOnEntering = "mf.gotoPlace(\"supernaturalbad2\");mf.gotoDialog(\"1571\");";
                this.logo = "logoEvil";
            }
        }

        // permits
        String permit = "permit1";
        if (journal.has(permit) && !journal.get(permit).equals("") && journal.get(permit).charAt(0) == '!') {
            // travel
            int value = Integer.parseInt((String) journal.get(permit).substring(1, journal.get(permit).length()));
            value--;
            journal.put(permit, "!" + value);
            if (value <= 0)
                journal.put(permit, "\u00A7- The permit can be retrieved from the sultan's official");
        }
        permit = "permit2";
        if (journal.has(permit) && !journal.get(permit).equals("") && journal.get(permit).charAt(0) == '!') {
            // museum
            int value = Integer.parseInt((String) journal.get(permit).substring(1, journal.get(permit).length()));
            value--;
            journal.put(permit, "!" + value);
            if (value <= 0)
                journal.put(permit, "\u00A7- The permit can be retrieved from the sultan's official");
        }
        permit = "permit3";
        if (journal.has(permit) && !journal.get(permit).equals("") && journal.get(permit).charAt(0) == '!') {
            // mosque
            int value = Integer.parseInt((String) journal.get(permit).substring(1, journal.get(permit).length()));
            value--;
            journal.put(permit, "!" + value);
            if (value <= 0)
                journal.put(permit, "\u00A7- The permit can be retrieved from the sultan's official");
        }
        permit = "permit4";
        if (journal.has(permit) && !journal.get(permit).equals("") && journal.get(permit).charAt(0) == '!') {
            // export
            int value = Integer.parseInt((String) journal.get(permit).substring(1, journal.get(permit).length()));
            value--;
            journal.put(permit, "!" + value);
            if (value <= 0)
                journal.put(permit, "\u00A7- The permit can be retrieved from the sultan's official");
        }
        permit = "permit10";
        if (journal.has(permit) && !journal.get(permit).equals("") && journal.get(permit).charAt(0) == '!') {
            // library
            int value = Integer.parseInt((String) journal.get(permit).substring(1, journal.get(permit).length()));
            value--;
            journal.put(permit, "!" + value);
            if (value <= 0)
                journal.put(permit, "\u00A7- The permit can be retrieved from the sultan's official");
        }
        permit = "permit11";
        if (journal.has(permit) && !journal.get(permit).equals("") && journal.get(permit).charAt(0) == '!') {
            // shrine
            int value = Integer.parseInt((String) journal.get(permit).substring(1, journal.get(permit).length()));
            value--;
            journal.put(permit, "!" + value);
            if (value <= 0)
                journal.put(permit, "\u00A7- The permit can be retrieved from the sultan's official");
        }
        permit = "permit12";
        if (journal.has(permit) && !journal.get(permit).equals("") && journal.get(permit).charAt(0) == '!') {
            // shrine
            int value = Integer.parseInt((String) journal.get(permit).substring(1, journal.get(permit).length()));
            value--;
            journal.put(permit, "!" + value);
            if (value <= 0)
                journal.put(permit, "\u00A7- The permit can be retrieved from the king's official");
        }

        // Umm
        if (journal.has("abdullah2") && journal.get("abdullah2").charAt(0) == '!') {
            // FIX 13.5.2005 Getting married to Umm needed some changes to make it foolproof(er)
            String parse = (String) journal.get("abdullah2").substring(1, 2);
            String other = (String) journal.get("abdullah2").substring(2, ((String) journal.get("abdullah2")).length());
            journal.put("abdullah2", other + "\u00A7- I waited one month to see Umm again");
        }

        // culture
        for (String s : cities) {
            Vector<Item> museumItems = null;
            if (has("museum" + s)) {
                if (s.equals("baghdad")) {
                    museumItems = itemsVector("museum");
                    for (Item item : museumItems) {
                        for (String city : cities) {
                            addCulture(city, item.culture / 2);
                        }
                    }
                } else {
                    museumItems = itemsVector(s + "museum");
                    for (Item item : museumItems) {
                        for (String city : cities) {
                            addCulture(city, item.culture / 4);
                        }
                        addCulture(s, item.culture);
                    }
                }
            }

            if (getCulture(s) < 0) {
                boolean destroy = false;
                // they don't appreciate you
                if (has("museum" + s) && !journal.contains("permit" + s, "destroyed")) {
                    if (mf.doActionOnEntering.equals("")) {
                        if (s.equals("amsterdam"))
                            mf.doActionOnEntering = "mf.gotoDialog(\"3340\");";
                        else if (s.equals("venice"))
                            mf.doActionOnEntering = "mf.gotoDialog(\"3341\");";
                        else if (s.equals("budapest"))
                            mf.doActionOnEntering = "mf.gotoDialog(\"3342\");";
                        else if (s.equals("vienna"))
                            mf.doActionOnEntering = "mf.gotoDialog(\"3343\");";
                        else if (s.equals("moscow"))
                            mf.doActionOnEntering = "mf.gotoDialog(\"3344\");";
                        else if (s.equals("madrid"))
                            mf.doActionOnEntering = "mf.gotoDialog(\"3345\");";
                        else if (s.equals("lisboa"))
                            mf.doActionOnEntering = "mf.gotoDialog(\"3346\");";
                        else if (s.equals("paris"))
                            mf.doActionOnEntering = "mf.gotoDialog(\"3347\");";
                        else if (s.equals("london"))
                            mf.doActionOnEntering = "mf.gotoDialog(\"3348\");";
                        else if (s.equals("hamburg"))
                            mf.doActionOnEntering = "mf.gotoDialog(\"3349\");";

                        // destroy museum and collections in there
                        destroy = true;
                        if (s.equals("baghdad")) {
                            museumItems = itemsVector("museum");
                            for (Item item : museumItems) {
                                giveItem(item.id);
                            }
                        } else {
                            museumItems = itemsVector(s + "museum");
                            for (Item item : museumItems) {
                                giveItem(item.id);
                            }
                        }

                        journal.add("permit" + s, "\u00A7- They destroyed my museum and my collections");
                        add("removemuseum" + s);
                    }
                }

                if (has("mosque" + s)) {
                    if ((destroy || mf.doActionOnEntering.equals("")) && !journal.contains("church" + s, "mosque is gone")) {
                        switch (s) {
                            case "amsterdam":
                                if (destroy)
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3330\");";
                                else
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3320\");";
                                break;
                            case "venice":
                                if (destroy)
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3331\");";
                                else
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3321\");";
                                break;
                            case "budapest":
                                if (destroy)
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3332\");";
                                else
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3322\");";
                                break;
                            case "vienna":
                                if (destroy)
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3333\");";
                                else
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3323\");";
                                break;
                            case "moscow":
                                if (destroy)
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3334\");";
                                else
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3324\");";
                                break;
                            case "madrid":
                                if (destroy)
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3335\");";
                                else
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3325\");";
                                break;
                            case "lisboa":
                                if (destroy)
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3336\");";
                                else
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3326\");";
                                break;
                            case "paris":
                                if (destroy)
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3337\");";
                                else
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3327\");";
                                break;
                            case "london":
                                if (destroy)
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3338\");";
                                else
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3328\");";
                                break;
                            case "hamburg":
                                if (destroy)
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3339\");";
                                else
                                    mf.doActionOnEntering = "mf.gotoDialog(\"3329\");";
                                break;
                        }

                        // destroy mosque
                        journal.add("church" + s, "\u00A7- The mosque is gone and a church stands in it's place");
                        add("removemosque" + s);
                    }
                }

            } else if (getCulture(s) > 49 && !has("museum" + s)) {
                // they really like you and want you to build a museum
                if (!has("museum" + s) && !journal.has("permit" + s) && !journal.isDone("permit" + s)
                        && mf.doActionOnEntering.equals(""))
                    if (s.equals("amsterdam"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3300\");";
                    else if (s.equals("venice"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3301\");";
                    else if (s.equals("budapest"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3302\");";
                    else if (s.equals("vienna"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3303\");";
                    else if (s.equals("moscow"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3304\");";
                    else if (s.equals("madrid"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3305\");";
                    else if (s.equals("lisboa"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3306\");";
                    else if (s.equals("paris"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3307\");";
                    else if (s.equals("london"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3308\");";
                    else if (s.equals("hamburg"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3309\");";
            } else if (getCulture(s) > 79) {
                // they worship you and will change their churches to mosques
                if (!has("mosque" + s) && !journal.has("church" + s) && !journal.isDone("church" + s)
                        && mf.doActionOnEntering.equals(""))
                    if (s.equals("amsterdam"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3310\");";
                    else if (s.equals("venice"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3311\");";
                    else if (s.equals("budapest"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3312\");";
                    else if (s.equals("vienna"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3313\");";
                    else if (s.equals("moscow"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3314\");";
                    else if (s.equals("madrid"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3315\");";
                    else if (s.equals("lisboa"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3316\");";
                    else if (s.equals("paris"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3317\");";
                    else if (s.equals("london"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3318\");";
                    else if (s.equals("hamburg"))
                        mf.doActionOnEntering = "mf.gotoDialog(\"3319\");";
            }
        }

        if (getCulture("baghdad") < 0 && !has("churchbaghdad")) {
            if (mf.doActionOnEntering.equals("")) {
                add("churchbaghdad");
                mf.doActionOnEntering = "mf.gotoDialog(\"5100\");";
            }
        } else if (getCulture("najaf") < 0 && !has("churchnajaf")) {
            if (mf.doActionOnEntering.equals("")) {
                add("churchnajaf");
                mf.doActionOnEntering = "mf.gotoDialog(\"5101\");";
            }
        } else if (getCulture("anjoudan") < 0 && !has("churchanjoudan")) {
            if (mf.doActionOnEntering.equals("")) {
                add("churchanjoudan");
                mf.doActionOnEntering = "mf.gotoDialog(\"5102\");";
            }
        } else if (getCulture("latakia") < 0 && !has("churchlatakia")) {
            if (mf.doActionOnEntering.equals("")) {
                add("churchlatakia");
                mf.doActionOnEntering = "mf.gotoDialog(\"5103\");";
            }
        } else if (getCulture("konya") < 0 && !has("churchkonya")) {
            if (mf.doActionOnEntering.equals("")) {
                add("churchkonya");
                mf.doActionOnEntering = "mf.gotoDialog(\"5104\");";
            }
        } else if (getCulture("baku") < 0 && !has("churchbaku")) {
            if (mf.doActionOnEntering.equals("")) {
                add("churchbaku");
                mf.doActionOnEntering = "mf.gotoDialog(\"5105\");";
            }
        } else if (getCulture("constantinopol") < 0 && !has("churchconstantinopol")) {
            if (mf.doActionOnEntering.equals("")) {
                add("churchconstantinopol");
                mf.doActionOnEntering = "mf.gotoDialog(\"5106\");";
            }
        }

        if (getCulture("baghdad") > 9 && has("churchbaghdad")) {
            if (mf.doActionOnEntering.equals("")) {
                add("mosquebaghdad");
                remove("churchbaghdad");
                mf.doActionOnEntering = "mf.gotoDialog(\"5110\");";
            }
        } else if (getCulture("najaf") > 9 && has("churchnajaf")) {
            if (mf.doActionOnEntering.equals("")) {
                add("mosquenajaf");
                remove("churchnajaf");
                mf.doActionOnEntering = "mf.gotoDialog(\"5111\");";
            }
        } else if (getCulture("anjoudan") > 9 && has("churchanjoudan")) {
            if (mf.doActionOnEntering.equals("")) {
                add("mosqueanjoudan");
                remove("churchanjoudan");
                mf.doActionOnEntering = "mf.gotoDialog(\"5112\");";
            }
        } else if (getCulture("latakia") > 9 && has("churchlatakia")) {
            if (mf.doActionOnEntering.equals("")) {
                add("mosquelatakia");
                remove("churchlatakia");
                mf.doActionOnEntering = "mf.gotoDialog(\"5113\");";
            }
        } else if (getCulture("konya") > 9 && has("churchkonya")) {
            if (mf.doActionOnEntering.equals("")) {
                add("mosquekonya");
                remove("churchkonya");
                mf.doActionOnEntering = "mf.gotoDialog(\"5114\");";
            }
        } else if (getCulture("baku") > 9 && has("churchbaku")) {
            if (mf.doActionOnEntering.equals("")) {
                add("mosquebaku");
                remove("churchbaku");
                mf.doActionOnEntering = "mf.gotoDialog(\"5115\");";
            }
        } else if (getCulture("constantinopol") > 9 && has("churchconstantinopol")) {
            if (mf.doActionOnEntering.equals("")) {
                add("mosqueconstantinopol");
                remove("churchconstantinopol");
                mf.doActionOnEntering = "mf.gotoDialog(\"5116\");";
            }
        }

        if (this.difficulty == 0) {
            if (this.age > 19 && (money > 10000 || journal.isDone("mahdi1") || journal.isDone("yima1") || journal.isDone("grave3")) && !journal.has("permit2")
                    && !journal.isDone("permit2") && mf.doActionOnEntering.equals("")) {
                journal.open("permit2");
                journal.open("");
                add("buildingpermit");
                mf.doActionOnEntering = "mf.gotoDialog(\"3110\");";
            } else if (this.age > 24 && (money > 10000 || journal.isDone("mahdi1") || journal.isDone("yima1") || journal.isDone("grave3"))
                    && !journal.has("permit10") && !journal.isDone("permit10") && mf.doActionOnEntering.equals("") && journal.isDone("permit2")) {
                journal.open("permit10");
                add("buildingpermit");
                mf.doActionOnEntering = "mf.gotoDialog(\"3080\");";
            }
        } else if (this.difficulty == 1) {
            if (this.age > 25 && (money > 20000 || journal.isDone("mahdi1") || journal.isDone("yima1") || journal.isDone("grave5")) && !journal.has("permit2")
                    && !journal.isDone("permit2") && mf.doActionOnEntering.equals("")) {
                journal.open("permit2");
                add("buildingpermit");
                mf.doActionOnEntering = "mf.gotoDialog(\"3110\");";
            } else if (this.age > 29 && (money > 20000 || journal.isDone("mahdi1") || journal.isDone("yima1") || journal.isDone("grave5"))
                    && !journal.has("permit10") && !journal.isDone("permit10") && mf.doActionOnEntering.equals("") && journal.isDone("permit2")) {
                journal.open("permit10");
                add("buildingpermit");
                mf.doActionOnEntering = "mf.gotoDialog(\"3080\");";
            }
        }

        // addictions
        if (this.addictedHashish) {
            if (hasItem("10220")) {
                removeItem("10220");
                this.addLog("You smoked hashish", "res=false;", day + month * 30 + year * 360);
            } else {
                health -= 5;
                this.addLog("You had no hashish, even though you are addicted to it. You feel weaker.", "res=false;", day + month * 30 + year * 360);
            }
        } else if (this.addictedOpium) {
            if (hasItem("10230")) {
                removeItem("10230");
                this.addLog("You smoked opium", "res=false;", day + month * 30 + year * 360);
            } else {
                health -= 20;
                this.addLog("You had no opium, even though you are addicted to it. You feel weaker.", "res=false;", day + month * 30 + year * 360);
            }
        }

        String[] armies = { "venice", "lisboa", "paris", "hamburg", "moscow", "budapest", "vienna", "madrid", "london", "amsterdam" };
        Random r = new Random();
        int army = r.nextInt(9);
        if (!journal.isDone("random1") && hasItem("16020") && age > 18) {
            add("foreignarmy", armies[0]);
            journal.open("random1", "\u00A7- The army of " + mf.armies.getName(armies[0]) + " is attacking your country!");
        } else if (!journal.isDone("random4") && journal.isDone("random1") && hasItem("16020") && age > 20) {
            add("foreignarmy", armies[1]);
            journal.open("random4", "\u00A7- The army of " + mf.armies.getName(armies[1]) + " is attacking your country!");
        } else if (!journal.isDone("random5") && journal.isDone("random4") && hasItem("16020") && age > 23) {
            add("foreignarmy", armies[2]);
            journal.open("random5", "\u00A7- The army of " + mf.armies.getName(armies[1]) + " is attacking your country!");
        } else if (!journal.isDone("random6") && journal.isDone("random5") && hasItem("16020") && age > 24) {
            add("foreignarmy", armies[3]);
            journal.open("random6", "\u00A7- The army of " + mf.armies.getName(armies[1]) + " is attacking your country!");
        } else if (!journal.isDone("random7") && journal.isDone("random6") && hasItem("16020") && age > 27) {
            add("foreignarmy", armies[4]);
            journal.open("random7", "\u00A7- The army of " + mf.armies.getName(armies[1]) + " is attacking your country!");
        } else if (!journal.isDone("random8") && journal.isDone("random7") && hasItem("16020") && age > 29) {
            add("foreignarmy", armies[5]);
            journal.open("random8", "\u00A7- The army of " + mf.armies.getName(armies[1]) + " is attacking your country!");
        } else if (!journal.isDone("random9") && journal.isDone("random8") && hasItem("16020") && age > 34) {
            add("foreignarmy", armies[6]);
            journal.open("random9", "\u00A7- The army of " + mf.armies.getName(armies[1]) + " is attacking your country!");
        } else if (!journal.isDone("random10") && journal.isDone("random9") && hasItem("16020") && age > 35) {
            add("foreignarmy", armies[7]);
            journal.open("random10", "\u00A7- The army of " + mf.armies.getName(armies[1]) + " is attacking your country!");
        } else if (!journal.isDone("random11") && journal.isDone("random10") && hasItem("16020") && age > 39) {
            add("foreignarmy", armies[8]);
            journal.open("random11", "\u00A7- The army of " + mf.armies.getName(armies[1]) + " is attacking your country!");
        } else if (!journal.isDone("random12") && journal.isDone("random11") && hasItem("16020") && age > 44) {
            add("foreignarmy", armies[9]);
            journal.open("random12", "\u00A7- The army of " + mf.armies.getName(armies[1]) + " is attacking your country!");
        }

        int allCulture = 0;
        for (String s : cities) allCulture += getCulture(s);
        this.culture = allCulture / cities.length;

        String[] shopCities = { "amsterdam", "budapest", "constantinopol", "hamburg", "lisboa", "london", "madrid", "moscow", "paris", "venice", "vienna" };
        String[] shops = { "12080", "12081", "12082", "12083", "12084", "12085", "12086", "12087", "12088", "12089", "12090" };
        for (int i = 0, j = shops.length; i < j; i++)
            if (hasItem(shops[i]))
                putShopMoney(shopCities[i], 150);

        chooseFace(true);
    }

    public void nextYear() {
        Mainframe mf = Mainframe.me;
        Random r = new Random();
        age++;
        year++;
        baseForce += 3;
        if (this.difficulty == 0) // easy level
            baseForce += 2;
        // reset map piece counters
        for (String s : cities) attr.remove(s + "mappiece");

        // culture
        String[] ccities = { "venice", "lisboa", "paris", "hamburg", "moscow", "budapest", "vienna", "madrid", "london", "amsterdam" };
        int[] cultures = { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };
        int whole = 0;
        for (int i = 0, j = ccities.length; i < j; i++) {
            whole += cultures[i];
            if (r.nextInt(10) > 7)
                removeCulture(ccities[i], (int) (cultures[i] * r.nextDouble()));
            else
                addCulture(ccities[i], (int) (cultures[i] * r.nextDouble()));
        }

        if (r.nextInt(10) > 7) {
            addCulture("baghdad", (int) (whole / 10));
            addCulture("najaf", (int) (whole / 8));
            addCulture("anjoudan", (int) (whole / 8));
            addCulture("latakia", (int) (whole / 6));
            addCulture("konya", (int) (whole / 6));
            addCulture("baku", (int) (whole / 6));
            addCulture("constantinopol", (int) (whole / 3));
        } else {
            removeCulture("baghdad", (int) (whole / 10));
            removeCulture("najaf", (int) (whole / 8));
            removeCulture("anjoudan", (int) (whole / 8));
            removeCulture("latakia", (int) (whole / 6));
            removeCulture("konya", (int) (whole / 6));
            removeCulture("baku", (int) (whole / 6));
            removeCulture("constantinopol", (int) (whole / 3));
        }
        
        // died of age
        if ((age > 50 && health < 60 && r.nextInt(100) < age) || (age > 70 && r.nextBoolean())) {
            mf.doActionOnEntering = "mf.gotoDialog(\"9120\");";
        }
    }

    public void chooseFace() {
        chooseFace(false, "");
    }

    public void chooseFace(String number) {
        chooseFace(false, number);
    }

    public void chooseFace(boolean nextMonth) {
        chooseFace(nextMonth, "");
    }

    public void chooseFace(boolean nextMonth, String number) {
        Mainframe mf = Mainframe.me;

        String beginning = "/pics/navigation/abus/abu_";

        if (age > 49)
            ageType = "old";
        else if (age > 29)
            ageType = "middleaged";
        else
            ageType = "young";

        if (number.equals("")) {
            if (health < 10)
                statusFace = beginning + ageType + "08.png";
            else if (health < 20)
                statusFace = beginning + ageType + "12.png";
            else if (health < 35) {
                if (moral < -75) {
                    statusFace = beginning + ageType + "07.png";
                } else if (moral > 75) {
                    statusFace = beginning + ageType + "17.png";
                } else
                    statusFace = beginning + ageType + "04.png";
            } else if (health < 60) {
                if (moral < -75) {
                    statusFace = beginning + ageType + "07.png";
                } else if (moral > 75) {
                    statusFace = beginning + ageType + "17.png";
                } else if (money < 200 && mf.rB(0, 2))
                    statusFace = beginning + ageType + "15.png";
                else
                    statusFace = beginning + ageType + "01.png";
            } else if (health < 75) {
                if (moral < -75) {
                    statusFace = beginning + ageType + "07.png";
                } else if (moral > 75) {
                    statusFace = beginning + ageType + "17.png";
                } else
                    statusFace = beginning + ageType + "06.png";
            } else if (health < 90) {
                if (moral < -75) {
                    statusFace = beginning + ageType + "07.png";
                } else if (moral > 75) {
                    statusFace = beginning + ageType + "17.png";
                } else
                    statusFace = beginning + ageType + "09.png";
            } else {
                if (moral < -75) {
                    statusFace = beginning + ageType + "07.png";
                } else if (moral > 75) {
                    statusFace = beginning + ageType + "17.png";
                } else
                    statusFace = beginning + ageType + "11.png";
            }
        } else
            statusFace = beginning + ageType + number + ".png";
    }

    public void addGood(int amount) {
        addBad(-amount);
    }

    public void addBad(int amount) {
        Mainframe mf = Mainframe.me;

        if (moral >= (-100 - amount) && moral <= (100 + amount)) {
            moral -= amount;
        }

        chooseFace();
    }

    public void addAddiction(String addiction, int amount) {
        Mainframe mf = Mainframe.me;
        int value = 0;
        if (attr.containsKey(addiction))
            value = Integer.parseInt((String) attr.get(addiction));
        value += amount;
        attr.put(addiction, String.valueOf(value));

        switch (addiction) {
            case "hashish":
                if (value > 20 && mf.rB(0, 60))
                    this.addictedHashish = true;
                else if (value < 20)
                    this.addictedHashish = false;
                break;
            case "opium":
                if (value > 20 && mf.rB(0, 80))
                    this.addictedOpium = true;
                else if (value < 20)
                    this.addictedOpium = false;
                break;
            case "sick":
                this.sickGeneral = value > 0;
                break;
            case "poisoned":
                this.sickPoisoned = value > 0;
                break;
            case "plague":
                this.sickPlague = value > 0;
                break;
        }

        this.logo = "logoIntro";

        if (this.addictedHashish)
            this.logo = "logoHashish";
        if (this.addictedOpium)
            this.logo = "logoOpium";
        if (this.sickPlague || this.sickGeneral || this.sickPoisoned)
            this.logo = "logoPlague";

        chooseFace();
    }

    public void removeAddiction(String addiction) {
        switch (addiction) {
            case "hashish":
                attr.remove(addiction);
                addictedHashish = false;
                break;
            case "opium":
                attr.remove(addiction);
                addictedOpium = false;
                break;
            case "sick":
                attr.remove(addiction);
                sickGeneral = false;
                break;
            case "poisoned":
                attr.remove(addiction);
                sickPoisoned = false;
                break;
            case "plague":
                attr.remove(addiction);
                sickPlague = false;
                break;
        }
        this.logo = "logoIntro";
        chooseFace();
    }

    public void removeAddictions() {
        removeAddiction("hashish");
        removeAddiction("opium");
        removeAddiction("sick");
        removeAddiction("poisoned");
        removeAddiction("plague");
    }

    public void switchItem(String id, String id2) {
        switchItem(id, id2, false);
    }

    public void switchItem(String id, String id2, boolean out) {
        try {
            if (Mainframe.DEBUG == 1)
                System.out.println("switching item: " + id + " to " + id2);
            if (!hasItem(id))
                return;
            Mainframe mf = Mainframe.me;
            removeItem(id);
            Item item = mf.market.getItem(id2);
            if (!out)
                item.where = place;
            items.add(item);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean itemHere(String id) {
        for (Item item : items) {
            if (item.id.equals(id) && item.here())
                return true;
        }
        return false;
    }

    public int itemsHere() {
        return itemsHere(place);
    }

    public int itemsHere(String place) {
        int counter = 0;
        for (Item item : items) {
            if (item.here(place))
                counter++;
        }
        if (Mainframe.DEBUG == 2)
            System.out.println("Items here: " + counter);
        return counter;
    }

    public Vector<Item> itemsVector(String place) {
        Vector<Item> ret = new Vector<>();
        int counter = 0;
        for (Item item : items) {
            if (item.here(place))
                ret.add(item);
        }
        if (Mainframe.DEBUG == 2)
            System.out.println("Items here: " + ret);
        return ret;
    }

    public void addCulture(String where, int amount) {
        removeCulture(where, -amount);
    }

    public void removeCulture(String where, int amount) {
        try {
            String parse = (String) attr.get("culture" + where);
            int value = Integer.parseInt(parse);
            amount = -amount;
            if (Mainframe.DEBUG == 1)
                System.out.println("Changing culture of " + where + " (" + value + "): " + amount);
            value += amount;
            attr.put("culture" + where, String.valueOf(value));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getCulture(String where) {
        try {
            String parse = (String) attr.get("culture" + where);
            int value = Integer.parseInt(parse);
            if (Mainframe.DEBUG == 1)
                System.out.println("Getting culture of " + where + ": " + value);
            return value;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public void getShopMoney(String where) {
        try {
            String[] shops = { "amsterdam", "budapest", "constantinopol", "hamburg", "lisboa", "london", "madrid", "moscow", "paris", "venice", "vienna" };

            if (where.equals("all")) {
                for (String string : shops) {
                    if (has(where + "shop")) {
                        String parse = (String) attr.get(where + "shop");
                        int value = Integer.parseInt(parse);
                        if (Mainframe.DEBUG == 1)
                            System.out.println("Getting money from shop in " + where + ": " + value);
                        money += value;
                        attr.put(where + "shop", "0");
                    }
                }
            } else if (has(where + "shop")) {
                String parse = (String) attr.get(where + "shop");
                int value = Integer.parseInt(parse);
                if (Mainframe.DEBUG == 1)
                    System.out.println("Getting money from shop in " + where + ": " + value);
                money += value;
                attr.put(where + "shop", "0");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void putShopMoney(String where, int amount) {
        try {
            if (has(where + "shop")) {
                String parse = (String) attr.get(where + "shop");
                int value = Integer.parseInt(parse);
                if (Mainframe.DEBUG == 1)
                    System.out.println("Putting money to shop in " + where + " (+" + amount + "): " + value);
                value += amount;
                attr.put(where + "shop", String.valueOf(value));
            } else
                attr.put(where + "shop", "0");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
