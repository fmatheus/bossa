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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.bigbross.bossa.BossaException;

/**
 * This class represents a single resource or a group of resources.
 * A group of resources may include or exclude other resources. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Resource implements Serializable {

    private ResourceManager resourceManager;

    private String id;

    private Set includes = new HashSet();

    private Set excludes = new HashSet();

    /**
     * Creates a new <code>Resource</code> instance with the given
     * identifier. <p>
     *
     * @param resourceManager the resource manager this resource is
     *        registered into.
     * @param id the resource id.
     */
    Resource(ResourceManager resourceManager, String id) {
        this.resourceManager = resourceManager;
        this.id = id;
    }

    /**
     * Returns the resource manager this resource is registered into. <p>
     * 
     * @return the resource manager this resource is registered into.
     */
    ResourceManager getResourceManager() {
        return resourceManager;
    }

    /**
     * Returns the resource identifier. <p>
     *
     * @return the resource id.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns <code>true</code> if this resource includes any other
     * resources, so it is a group.
     *
     * @return <code>true</code> if it is a group,
     *         <code>false</code> otherwise.
     */
    public boolean isGroup() {
        return includes.size() > 0;
    }

    /**
     * Includes a resource in this resource. Removes the resource from
     * the excludes list if needed. <p>
     *
     * @param resource the resource to be included.
     * @return <code>false</code> if resource includes this resource,
     *         <code>true</code> otherwise.
     * @exception PersistenceException if an error occours when making the
     *            execution of this method persistent.
     */
    public boolean include(Resource resource) throws BossaException {
        ResourceCommand includeCommand = 
            new IncludeInResource(this, resource);
        return ((Boolean) getResourceManager().getBossa().
                          executeCommand(includeCommand)).booleanValue();
    }

    /**
     * Includes a resource in this resource. Removes the resource from
     * the excludes list if needed. <p>
     *
     * This method does not creates a command to the prevalent system. The
     * execution of this method will not be persistent unless it is called by
     * an appropriate command. <p>
     * 
     * @param resource the resource to be included.
     * @return <code>false</code> if resource includes this resource,
     *         <code>true</code> otherwise.
     */
    public boolean includeImpl(Resource resource) {
        if (resource.contains(this)) {
            return false;
        }
        excludes.remove(resource);
        includes.add(resource);
        return true;
    }

    /**
     * Excludes a resource from this resource. Removes the resource from
     * the includes list if needed. <p>
     * 
     * @param resource the resource to be excluded.
     * @return <code>false</code> if resource excludes this resource,
     *         <code>true</code> otherwise.
     * @exception PersistenceException if an error occours when making the
     *            execution of this method persistent.
     */
    public boolean exclude(Resource resource) throws BossaException {
        ResourceCommand excludeCommand =
            new ExcludeInResource(this, resource);
        return ((Boolean) getResourceManager().getBossa().
                          executeCommand(excludeCommand)).booleanValue();
    }

    /**
     * Excludes a resource from this resource. Removes the resource from
     * the includes list if needed. <p>
     *
     * This method does not creates a command to the prevalent system. The
     * execution of this method will not be persistent unless it is called by
     * an appropriate command. <p>
     * 
     * @param resource the resource to be excluded.
     * @return <code>false</code> if resource excludes this resource,
     *         <code>true</code> otherwise.
     */
    public boolean excludeImpl(Resource resource) {
        if (resource.excludes(this)) {
            return false;
        }
        includes.remove(resource);
        excludes.add(resource);
        return true;
    }

    /**
     * Removes a resource from this resource. Effectively removes it from
     * any of the includes or excludes list. <p>
     *
     * @param resource the resource to be removed.
     * @exception PersistenceException if an error occours when making the
     *            execution of this method persistent.
     */
    public void remove(Resource resource) throws BossaException {
        ResourceCommand removeCommand =
            new RemoveFromResource(this, resource);
        getResourceManager().getBossa().executeCommand(removeCommand);
    }

    /**
     * Removes a resource from this resource. Effectively removes it from
     * any of the includes or excludes list. <p>
     *
     * This method does not creates a command to the prevalent system. The
     * execution of this method will not be persistent unless it is called by
     * an appropriate command. <p>
     * 
     * @param resource the resource to be removed.
     */
    public void removeImpl(Resource resource) {
        includes.remove(resource);
        excludes.remove(resource);
    }

    /**
     * Checks if a resource is excluded by this resource. <p>
     *
     * @param resource the resource to be looked for.
     * @return <code>true</code> if the resource is found,
     *         <code>false</code> otherwise.
     */
    private boolean excludes(Resource resource) {

        if (this.equals(resource)) {
            return true;
        }

        if (excludes.contains(resource)) {
            return true;
        }

        Iterator it = excludes.iterator();
        while (it.hasNext()) {
            Resource curr = (Resource) it.next();
            if (curr.excludes(resource)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if a resource is contained in this. <p>
     *
     * @param resource the resource to be looked for.
     * @return <code>true</code> if the resource is found,
     *         <code>false</code> otherwise.
     */
    public boolean contains(Resource resource) {

        if (this.equals(resource)) {
            return true;
        }

        if (!isGroup()) {
            return false;
        }

        if (excludes.contains(resource)) {
            return false;
        }

        Iterator it = excludes.iterator();
        while (it.hasNext()) {
            Resource curr = (Resource) it.next();
            if (curr.contains(resource)) {
                return false;
            }
        }

        if (includes.contains(resource)) {
            return true;
        }

        it = includes.iterator();
        while (it.hasNext()) {
            Resource curr = (Resource) it.next();
            if (curr.contains(resource)) {
                return true;
            }
        }

        return false;
    }
}
