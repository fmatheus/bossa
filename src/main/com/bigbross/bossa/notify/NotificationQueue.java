/*
 * Bossa Workflow System
 *
 * $Id$
 *
 * Copyright (C) 2004 OpenBR Sistemas S/C Ltda.
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bigbross.bossa.Bossa;

/**
 * This class implements a notification queue. It is intended to be used by
 * other Bossa packages to queue and send notifications in a atomic
 * way. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public abstract class NotificationQueue implements Serializable {
    
    private List queue;
    
    /**
     * Creates new empty notification queue. <p>
     */
    public NotificationQueue() {
        queue = new ArrayList();
    }

    /**
     * Adds an event to this notification queue. <p>
     * 
     * @param event the event to be added.
     */
    protected void addEvent(Event event) {
        queue.add(event);
    }
    
    /**
     * Notifies all events currently present in this queue and flushes it.
     * <p>
     * 
     * @param bossa the root of the bossa system.
     */
    public void notifyAll(Bossa bossa) {
        NotificationBus bus = bossa.getNotificationBus();
        for (Iterator i = queue.iterator(); i.hasNext(); ){
            Event e = (Event) i.next();
            bus.notifyEvent(e);
        }
    }
}
