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

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * This class represents an event in the notification bus. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Event implements Comparable, Serializable {

    private String id;
    
    private int type;
    
    private Map attributes;
    
    private Date time;

    /**
     * Constant to indicate a work item or activity related event type.
     */
    public static final int WFNET_EVENT = 1;
    
    /**
     * Constant to indicate a resource manipulation event type.
     */
    public static final int RESOURCE_EVENT = 2;
    
    /**
     * Creates an event. For a list of the possible event types, see the
     * constants defined in this class. <p>
     * 
     * The meaning of the id and of the attributes map are
     * defined by the generator of the event. The only exception is that a
     * event with type <code>WFNET_EVENT</code> <i>should</i> contain an
     * attribute named <code>WFNetEvents.ATTRIB_RESOURCE_ID</code>,
     * indicating the resource this event affects. <p>
     * 
     * @param id the id of this event.
     * @param type the type os this event.
     * @param attributes a <code>Map</code> containing the attributes.
     * @param time the time this event happened.
     * @see com.bigbross.bossa.wfnet.WFNetEvents#ATTRIB_RESOURCE_ID
     */
    public Event(String id, int type, Map attributes, Date time) {
        this.id = id;
        this.type = type;
        this.attributes = attributes;
        this.time = (Date) time.clone();
    }

    /**
     * Returns the id of this event. <p>
     * 
     * @return the id of this event.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Returns the type of this event. <p>
     * 
     * @return the type of this event.
     */
    public int getType() {
        return this.type;
    }

    /**
     * Returns the attributes map of this event. <p>
     * 
     * Notice that it is not possible to change this map. If tried, an
     * UnsupportedOperationException will be thrown.
     * 
     * @return the attributes map of this event.
     */
    public Map getAttributes() {
        return Collections.unmodifiableMap(this.attributes);
    }

    /**
     * Returns the time this event happened. <p>
     * 
     * @return the time this event happened.
     */
    public Date getTime() {
        return (Date) this.time.clone();
    }

    /**
     * Note: this class has a natural ordering that is inconsistent with
     * equals. <p>
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        return this.time.compareTo(((Event) o).getTime());
    }
}
