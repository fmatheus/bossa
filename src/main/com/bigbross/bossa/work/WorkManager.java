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

package com.bigbross.bossa.work;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.wfnet.WorkItem;

/**
 * This class manages the generation of work item lists. <p>
 * 
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class WorkManager implements Serializable {

    private Bossa engine;

    /**
     * Creates a new empty work manager. <p>
     * 
     * @param engine the bossa engine this work manager is part.
     */
    public WorkManager(Bossa engine) {
        this.engine = engine;
    }

    /**
     * Creates a new empty work manager. <p>
     */
    public WorkManager() {
        this(null);
    }
    
    /**
     * Returns the bossa engine this work manager is part. <p>
     * 
     * @return The bossa engine this work manager is part.
     */
    Bossa getBossa() {
        return engine;
    }

    /**
     * Returns a list of all work items in the engine that can be opened
     * by the provided resource. <p>
     * 
     * @param resourceId the resource id.
     * @return the list of work items.
     */
    public List getWorkItems(String resourceId) {
        return getWorkItems(resourceId, false);
    }

    /**
     * Returns a list of all work items in the engine that can be opened
     * by the provided resource, including the initial work items. <p>
     * 
     * @param resourceId the resource id.
     * @param getInitial set to <code>true</code> to get the initial work
     *                   items and to <code>false</code> to only get the
     *                   standard work items. 
     * @return the list of work items.
     */
    public List getWorkItems(String resourceId, boolean getInitial) {
        Resource resource =
            getBossa().getResourceManager().getResource(resourceId);
        List workItems =
            getBossa().getCaseTypeManager().getWorkItems(getInitial);
        Iterator i = workItems.iterator();
        while (i.hasNext()) {
            String groupId =
                ((WorkItem) i.next()).getTransition().getResource();
            Resource group = 
                getBossa().getResourceManager().getResource(groupId);
            if (!group.contains(resource)) {
                i.remove();
            }
        }
        return workItems;
    }
}
