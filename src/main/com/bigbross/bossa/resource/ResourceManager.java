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

    private Map groups;

    /**
     * Creates a new empty resource manager. <p>
     * 
     * @param engine the bossa engine this resorce manager is part.
     */
    public ResourceManager(Bossa engine) {
        this.engine = engine;
        this.groups = new HashMap();
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
     * @return The bossa engine this resorce manager is part.
     */
    Bossa getBossa() {
        return engine;
    }

    /**
     * Creates a new empty group in the manager. A group is a collection
     * of resources. <p>
     * 
     * @param the id of the group to be created.
     * @return <code>true</code> if the group was created,
     *         <code>false</code> if there is already a group
     *         with the same id.
     */    
    public boolean createGroup(String id) {
        if (groups.containsKey(id)) {
            return false;
        } else {
            groups.put(id, new HashSet());
            return true;
        }
    }

    /**
     * Removes a group from the manager. <p>
     * 
     * @param the id of the group to be removed.
     * @return <code>true</code> if the group was removed,
     *         <code>false</code> if the group was not found.
     */    
    public boolean removeGroup(String id) {
        if (groups.remove(id) == null) {
            return false;
        } else {
            return true;
        }
    }
   
    /**
     * Adds a resource to a group. <p>
     * 
     * @param groupId the id of the group to add the resource.
     * @param resource the resource to be added.
     */
    public void addResource(String groupId, String resource) {
        Set group = (Set) groups.get(groupId);
        group.add(resource);
    }

    /**
     * Removes a resource from a group. <p>
     * 
     * @param groupId the id of the group to remove the resource.
     * @param resource the resource to be removed.
     * @return <code>true</code> if the resource was successfully
     *         removed, <code>false</code> if the resource was not
     *         found in the indicated group.
     */
    public boolean removeResource(String groupId, String resource) {
        Set group = (Set) groups.get(groupId);
        return group.remove(resource);
    }
    
    /**
     * Lists all resources of a group. <p>
     * 
     * @param groupId the id of the group.
     * @return The list of all resources in the indicated group.
     */
    public List getAllResources(String groupId) {
        Set group = (Set) groups.get(groupId);
        ArrayList resources = new ArrayList();
        Iterator i = group.iterator();
        while (i.hasNext()) {
            resources.add(i.next());
        }
        return resources;
    }
    
    /**
     * Indicates if a resource is contained in a group. <p>
     * 
     * @param groupId the id of the group.
     * @param resource the resource.
     * @return <code>true</code> if the resource is in the group, 
     *         <code>false</code> otherwise.
     */
    public boolean groupContains(String groupId, String resource) {
        Set group = (Set) groups.get(groupId);
        return group.contains(resource);
    }
}
