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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaException;

/**
 * This class manages the creation and removal of resources. Resources 
 * are groups of resources, indexed by a string id. Single users are
 * empty resources, their presence indicates the id is in use. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class ResourceManager implements Serializable {

    private Bossa engine;

    private Map resources;

    /**
     * Creates a new empty resource manager. <p>
     * 
     * @param engine the bossa engine this resorce manager is part.
     */
    public ResourceManager(Bossa engine) {
        this.engine = engine;
        this.resources = new HashMap();
    }

    /**
     * Creates a new empty resource manager. <p>
     */
    public ResourceManager() {
        this(null);
    }
    
    /**
     * Returns the bossa engine this resource manager is part. <p>
     * 
     * @return the bossa engine this resource manager is part.
     */
    Bossa getBossa() {
        return engine;
    }

    /**
     * Returns the resource with the given id. <p>
     *
     * @param id the resource id.
     * @return a <code>Resource</code>, <code>null</code> if there
     *         is no resource with this id.
     */
    public Resource getResource(String id) {
        return (Resource) resources.get(id);
    }

    /**
     * Creates a new resource in the manager. <p>
     * 
     * @param id the id of the resource to be created.
     * @return a <code>Resource</code>, <code>null</code> if there is
     *         already a resource with this id.
     * @exception PersistenceException if an error occours when making the
     *            execution of this method persistent.
     */    
    public Resource createResource(String id) throws BossaException {
        ResourceCommand createCommand = new CreateResource(id);
        return (Resource) getBossa().executeCommand(createCommand);
    }

    /**
     * Creates a new resource in the manager. <p>
     * 
     * This method does not creates a command to the prevalent system. The
     * execution of this method will not be persistent unless it is called by
     * an appropriate command. <p>
     * 
     * @param id the id of the resource to be created.
     * @return a <code>Resource</code>, <code>null</code> if there is
     *         already a resource with this id.
     */    
    Resource createResourceImpl(String id) {
        Resource resource = null;
        if (!resources.containsKey(id)) {
            resource = new Resource(this, id);
            resources.put(id, resource);
        }
        return resource;
    }

    /**
     * Removes a resource from the manager. <p>
     *
     * @param resource a <code>Resource</code> to be removed.
     * @return <code>true</code> if the resource was removed,
     *         <code>false</code> if the resource was not found.
     * @exception PersistenceException if an error occours when making the
     *            execution of this method persistent.
     */
    public boolean removeResource(Resource resource) throws BossaException {
        ResourceCommand removeCommand = new RemoveResource(resource);
        return ((Boolean) getBossa().executeCommand(removeCommand)).
                booleanValue();
    }

    /**
     * Removes a resource from the manager. <p>
     *
     * This method does not creates a command to the prevalent system. The
     * execution of this method will not be persistent unless it is called by
     * an appropriate command. <p>
     * 
     * @param resource a <code>Resource</code> to be removed.
     * @return <code>true</code> if the resource was removed,
     *         <code>false</code> if the resource was not found.
     */
    public boolean removeResourceImpl(Resource resource) {
        return resources.remove(resource.getId()) != null;
    }
}
