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
 * This class implements an abstract test listener. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public abstract class TestListener implements Listener {

    private String id;
    private int type;
    private Resource resource;
    private int runs;

    public TestListener(String id, int type, Resource resource) {
        this.id = id;
        this.type = type;
        this.resource = resource;
        this.runs = 0;
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
        if (this.type == 0) {
            return true;
        } else {
            return this.type == type;
        } 
    }

    /**
     * @see com.bigbross.bossa.notify.Listener#getResource()
     */
    public Resource getResource() {
        return resource;
    }

    public int runs() {
        return this.runs;
    }
    
    /**
     * @see com.bigbross.bossa.notify.Listener#notifyEvent(
     *      com.bigbross.bossa.notify.Event)
     */
    public void notifyEvent(Event event) {
        runs++;
    }
}
