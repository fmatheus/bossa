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
 * This interface should be implemented by a class that wants to be registered
 * in the notification bus. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public interface Listener {

    /**
     * Returns the id of this listener. This id must be unique with respect
     * to the desired notification bus. <p>
     *
     * @return the id of this listener.
     */
    String getId();

    /**
     * Indicates if this listener is interested in an event type. The
     * notification bus will only notify this listener of the event types it
     * is interested. <p>
     *
     * @param type the type of the event.
     * @return <code>true</code> if this listener is interested in the event,
     *         <code>false</code> otherwise.
     */
    boolean interested(int type);

    /**
     * Returns the resource used to further filter the work item or activity
     * related events passed to this listener. The notification bus will only
     * notify this listener of events associated with resources that contain
     * the resource returned by this method. If the resource returned is
     * <code>null</code>, no resource filtering will happen. <p>
     *
     * @return the resource to be used as filter.
     */
    Resource getResource();

    /**
     * Notifies this listener of an event. See the event class for the
     * information provided to the listener. <p>
     *
     * @param event the event.
     * @see com.bigbross.bossa.notify.Event
     */
    void notifyEvent(Event event);
}
