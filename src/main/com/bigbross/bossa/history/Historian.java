/*
 * Bossa Workflow System
 *
 * $Id$
 *
 * Copyright (C) 2003 OpenBR Sistemas S/C Ltda.
 *
 * This file is part of Bossa.
 *
 * Bossa is free software; you can redistribute it and/or modify it
 * under the terms of version 2 of the GNU General Public License as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package com.bigbross.bossa.history;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.bigbross.bossa.notify.Event;

/**
 * This class keeps the historical records of a Bossa engine. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Historian implements Serializable {
    
    private ArrayList history;

    /**
     * Creates a new empty historian. <p>
     */
    public Historian() {
        history = new ArrayList();
    }

    /**
     * Finds the place in history where an event happening in the provided time
     * would appear. <p>
     * 
     * @param time the time.
     * @return the position the event would appear.
     */
    private int findPlaceInHistory(Date time) {
        Event proxy = new Event(null, -1, null, time);
        int place = Collections.binarySearch(history, proxy);
        if (place < 0) {
            place = - (place + 1);
        }
        return place;
    }
    
    /**
     * Return all events that took place util now. <p>
     * 
     * @return all events that took place util now.
     */
    public List getHistory() {
        return getHistory(null, null); 
    }
    
    /**
     * Returns the events that took place after the start date, inclusive,
     * until now. <p>
     * 
     * @param start the start date.
     * @return the events that took place after the start date until now.
     */
    public List getHistory(Date start) {
        return getHistory(start, null);
    }
    
    /**
     * Returns the events that took place between the start date and
     * the end date, including the start date and excluding the end date. <p>
     * 
     * @param start the start date.
     * @param end the end date.
     * @return the events that took place between the two dates.
     */
    public List getHistory(Date start, Date end) {
        int startPosition, endPosition;
        startPosition = start == null ? 0 : findPlaceInHistory(start);
        endPosition = end == null ? history.size() : findPlaceInHistory(end);
        List events = new ArrayList(Math.abs(endPosition - startPosition));
        for (int i = startPosition; i < endPosition; i++) {
            events.add(history.get(i));
        }
        return events;
    }
    
    /**
     * Adds a new event to the history. <p>
     * 
     * @param event the new event.
     */
    void newEvent(Event event) {
        /*
         * Let's see if the new event happened before the last event.
         * I'm not sure this can happen, but it won't hurt to deal with it.
         */
        int last = history.size() - 1;
        while (last >= 0 &&
               event.compareTo(history.get(last)) < 0) {
            last--;
        }
        history.add(last + 1, event);
    }
}
