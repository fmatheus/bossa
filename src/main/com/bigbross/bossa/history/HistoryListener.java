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

import com.bigbross.bossa.notify.Event;
import com.bigbross.bossa.notify.Listener;
import com.bigbross.bossa.resource.Resource;

/**
 * This class implements the listener for a historian in the notification
 * bus. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class HistoryListener implements Listener, Serializable {

    private String id;

    private Historian historian;

    /**
     * Creates a new listener. <p>
     *
     * @param historian the historian this listener is working to.
     */
    public HistoryListener(Historian historian) {
        this.id = "HistoryListener" + new Double(Math.random());
        this.historian = historian;
    }

    /**
     * @see com.bigbross.bossa.notify.Listener#getId()
     */
    public String getId() {
        return id;
    }

    /**
     * @see com.bigbross.bossa.notify.Listener#interested(int)
     */
    public boolean interested(int type) {
        return true;
    }

    /**
     * @see com.bigbross.bossa.notify.Listener#getResource()
     */
    public Resource getResource() {
        return null;
    }

    /**
     * @see com.bigbross.bossa.notify.Listener#notifyEvent(
     *                                        com.bigbross.bossa.notify.Event)
     */
    public void notifyEvent(Event event) {
        this.historian.newEvent(event);
    }
}
