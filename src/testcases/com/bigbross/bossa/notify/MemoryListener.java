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

import java.util.ArrayList;
import java.util.List;

import com.bigbross.bossa.resource.Resource;

/**
 * This class implements a listener that remembers all notifications. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class MemoryListener extends TestListener {

    private List notifications;

    public MemoryListener(String id, int type, Resource resource) {
        super(id, type, resource);
        notifications = new ArrayList();
    }

    public List getNotifications() {
        return notifications;
    }

    /**
     * @see com.bigbross.bossa.notify.Listener#notifyEvent(
     *      com.bigbross.bossa.notify.Event)
     */
    public void notifyEvent(Event event) {
        notifications.add(event);
    }
}
