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

/**
 * This class manages groups of resources. Resources are only string
 * identifiers and groups are sets of these identifiers. It is possible
 * to create new groups, add and remove resources, get all resources of a
 * group and check if a resource is in a group. <p>
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
     * Returns the bossa engine this resorce manager is part. <p>
     * 
     * @return the bossa engine this resorce manager is part.
     */
    Bossa getBossa() {
        return engine;
    }

    /**
     * Returns the resorce with the given id. <p>
     *
     * @param id the resource id.
     * @return a <code>Resource</code>, <code>null</code> if there is no resource with this id.
     */
    public Resource getResource(String id) {
        return (Resource) resources.get(id);
    }

    /**
     * Creates a new resource in the manager. <p>
     * 
     * @param id the id of the resource to be created.
     * @return a <code>Resource</code>, <code>null</code> if there is already a resource with this id.
     */    
    public Resource createResource(String id) {
        Resource resource = null;
        if (!resources.containsKey(id)) {
            resource = new Resource(id);
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
     */
    public boolean removeResource(Resource resource) {
        return resources.remove(resource.getId()) != null;
    }

}
