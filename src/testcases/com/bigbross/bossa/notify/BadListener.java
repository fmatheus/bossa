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

import com.bigbross.bossa.resource.Resource;

/**
 * This class implements a broken listener. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class BadListener extends TestListener {

    private long wrongTime;

    public BadListener(String id, int type, Resource resource) {
        super(id, type, resource);
        wrongTime = -123456789;
    }

    /**
     * @see com.bigbross.bossa.notify.Listener#notifyEvent(
     *      com.bigbross.bossa.notify.Event)
     */
    public void notifyEvent(Event event) {
        super.notifyEvent(event);
        /* This should not affect the event. */
        try { event.getAttributes().put("bad", "run"); } catch (Exception e) {}
        event.getTime().setTime(wrongTime);
        /* This should not disturb the bus. */
        throw new RuntimeException("Muahahaha!");
    }
    
    public long wrongTime() {
        return wrongTime;
    }
}
