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

package com.bigbross.bossa.notify;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.bigbross.bossa.Bossa;


/**
 * This class manages all event notifications inside Bossa. This class is
 * stateless and should be used by classes tightly integrated with the Bossa
 * core. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class NotificationBus {

    private Bossa engine;

    private Map listeners;

    /**
     * Creates a new empty notification bus. <p>
     * 
     * @param engine the bossa engine this notification bus is part.
     */
    public NotificationBus(Bossa engine) {
        this.engine = engine;
        this.listeners = new HashMap();
    }
    
    /**
     * Creates a new empty notification bus. <p>
     * 
     */
    public NotificationBus() {
        this(null);
    }

    /**
     * Registers a new listener of the notification bus. <p>
     * 
     * @param listener the object that will act as a listener of the
     *                 notification bus.
     * @return <code>true</code> if the listener is registered,
     *         <code>false</code> if there is already a listener registered
     *         with the same id.
     */    
    public boolean registerListener(Listener listener) {
        if (listeners.containsKey(listener.getId())) {
            return false;
        }
        listeners.put(listener.getId(), listener);
        return true;
    }
    
    /**
     * Removes the listener from the notification bus, if present. <p>
     * 
     * @param id the id of the listener.
     */
    public void removeListener(String id) {
        listeners.remove(id);
    }

    /**
     * Informs all registered listeners the occurrence of an event. An event
     * has an id and some attributes that are dependent on the event type. <p>
     * 
     * @param id the id of the event.
     * @param attributes a <code>Map</code> containing the attributes.
     */
    public void notifyEvent(String id, Map attributes) {
        Date now = new Date();
        Iterator i = listeners.values().iterator();
        while (i.hasNext()) {
            Listener l = (Listener) i.next();
            try {
                l.notifyEvent(id, now, attributes);
            } catch (Exception e) {
                // We ignore listeners exceptions.
            }
        }
    }
}
