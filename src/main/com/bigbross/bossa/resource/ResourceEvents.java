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

package com.bigbross.bossa.resource;

import java.util.HashMap;
import java.util.Map;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.notify.Event;
import com.bigbross.bossa.resource.Resource;

/**
 * This class holds as static constants all ids of resource events. It also
 * provides static methods used by the resource classes to create these
 * events. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public abstract class ResourceEvents {

    /**
     * Constant to indicate the event of a resource creation in the
     * resource manager. <p>
     * 
     * This event contains the following attributes: ATTRIB_RESOURCE_ID <p>
     * 
     * @see ResourceEvents#ATTRIB_RESOURCE_ID
     */
    public static final String ID_CREATE_RESOURCE = "create_resource";

    /**
     * Constant to indicate the event of a resource removal from the
     * resource manager. <p>
     * 
     * This event contains the following attributes: ATTRIB_RESOURCE_ID <p>
     * 
     * @see ResourceEvents#ATTRIB_RESOURCE_ID
     */
    public static final String ID_REMOVE_RESOURCE = "remove_resource";

    /**
     * Constant to indicate the event of a resource being added to the
     * include list of another resource. <p>
     * 
     * This event contains the following attributes: ATTRIB_RESOURCE_ID and
     * ATTRIB_HOST_RESOURCE_ID <p>
     * 
     * @see ResourceEvents#ATTRIB_RESOURCE_ID
     * @see ResourceEvents#ATTRIB_HOST_RESOURCE_ID
     */
    public static final String ID_INCLUDE_IN_RESOURCE = "inc_in_resource";

    /**
     * Constant to indicate the event of a resource being added to the
     * exclude list of another resource. <p>
     * 
     * This event contains the following attributes: ATTRIB_RESOURCE_ID and
     * ATTRIB_HOST_RESOURCE_ID <p>
     * 
     * @see ResourceEvents#ATTRIB_RESOURCE_ID
     * @see ResourceEvents#ATTRIB_HOST_RESOURCE_ID
     */
    public static final String ID_EXCLUDE_IN_RESOURCE = "exc_in_resource";

    /**
     * Constant to indicate the event of a resource being removed from the
     * lists of another resource. <p>
     * 
     * This event contains the following attributes: ATTRIB_RESOURCE_ID and
     * ATTRIB_HOST_RESOURCE_ID <p>
     * 
     * @see ResourceEvents#ATTRIB_RESOURCE_ID
     * @see ResourceEvents#ATTRIB_HOST_RESOURCE_ID
     */
    public static final String ID_REMOVE_FROM_RESOURCE = "rem_from_resource";

    /**
     * Constant to indicate the resource id attribute.
     */
    public static final String ATTRIB_RESOURCE_ID = "resource_id";

    /**
     * Constant to indicate the host resource id attribute.
     */
    public static final String ATTRIB_HOST_RESOURCE_ID = "host_resource_id";


    /**
     * Notify a single resource event. <p>
     * 
     * @param bossa the root of the bossa system.
     * @param notificationId the id of this event.
     * @param resource the resource involved.
     */
    static void notifySingleResource(Bossa bossa, String notificationId,
                                     Resource resource) {
        if (bossa != null) {
            Map attrib = new HashMap();
            attrib.put(ATTRIB_RESOURCE_ID, resource.getId());
            Event event =
                new Event(notificationId, Event.RESOURCE_EVENT, attrib,
                          bossa.getTimeSource().getTime());
            bossa.getNotificationBus().notifyEvent(event);
        }
    }

    /**
     * Notify an event with two resources. <p>
     * 
     * @param bossa the root of the bossa system.
     * @param notificationId the id of this event.
     * @param resource the resource being added or removed.
     * @param host the resource being manipulated.
     */
    static void notifyTwoResources(Bossa bossa, String notificationId,
                                   Resource resource, Resource host) {
        if (bossa != null) {
            Map attrib = new HashMap();
            attrib.put(ATTRIB_RESOURCE_ID, resource.getId());
            attrib.put(ATTRIB_HOST_RESOURCE_ID, host.getId());
            Event event =
                new Event(notificationId, Event.RESOURCE_EVENT, attrib,
                          bossa.getTimeSource().getTime());
            bossa.getNotificationBus().notifyEvent(event);
        }
    }
}
