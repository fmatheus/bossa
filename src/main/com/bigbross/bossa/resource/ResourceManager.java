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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaException;

/**
 * This class manages the creation and removal of resources. Resources 
 * are groups of resources, indexed by a string id. Single users are
 * empty resources, their presence indicates the id is in use. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class ResourceManager extends ResourceRegistry {

    private Bossa engine;
    
    private Map allRegistries;

    /**
     * Creates a new empty resource manager. <p>
     * 
     * @param engine the bossa engine this resorce manager is part.
     */
    public ResourceManager(Bossa engine) {
        super("root");
        this.engine = engine;
        this.allRegistries = new HashMap();
        addRegistry(this);
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
     * Returns the top level resource registry, the resource manager. <p>
     * 
     * @return the resource manager, <code>null</code> if the root registry
     *         is not a resource manager.
     */
    ResourceManager getResourceManager() {
        return this;
    }

    /**
     * Adds a resource registry to the global registry index. <p>
     * 
     * @param registry the resource registry.
     * @return <code>true</code> if the registry was added,
     *         <code>false</code> if the registry was already present.
     * @see com.bigbross.bossa.resource.ResourceManager#getRegistry(String)
     * @see com.bigbross.bossa.resource.ResourceManager#removeRegistry(
     *      ResourceRegistry)
     */
    boolean addRegistry(ResourceRegistry registry) {
        String id = registry.getGlobalId();
        if (!allRegistries.containsKey(id)) {
            allRegistries.put(id, registry);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Returns the resource registry indexed by its global id. Using this
     * method it is possible to retrieve any resource registry in the
     * system.
     * 
     * @param globalId the global id of the registry.
     * @return the resource registry,
     *         <code>null</code> if the registry was not found.
     * @see com.bigbross.bossa.resource.ResourceManager#addRegistry(
     *      ResourceRegistry)
     * @see com.bigbross.bossa.resource.ResourceManager#removeRegistry(
     *      ResourceRegistry)
     */
    ResourceRegistry getRegistry(String globalId) {
        return (ResourceRegistry) allRegistries.get(globalId);
    }
    
    /**
     * Removes a resource registry from the global registry index. <p>
     * 
     * @param registry the resource registry.
     * @return <code>true</code> if the registry was removed,
     *         <code>false</code> if the registry was not found.
     * @see com.bigbross.bossa.resource.ResourceManager#addRegistry(
     *      ResourceRegistry)
     * @see com.bigbross.bossa.resource.ResourceManager#getRegistry(String)
     */
    boolean removeRegistry(ResourceRegistry registry) {
        String id = registry.getGlobalId();
        return (allRegistries.remove(id) != null);
    }
}
