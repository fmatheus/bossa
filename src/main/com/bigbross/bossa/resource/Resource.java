/*
 * Bossa Workflow System
 *
 * $Id$
 *
 * Copyright (C) 2003,2004 OpenBR Sistemas S/C Ltda.
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaException;

/**
 * This class represents a single resource or a group of resources.
 * A group of resources may include or exclude other resources. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Resource implements Container, Serializable {

    private ResourceRegistry resourceRegistry;

    private String id;

    private Set includes = new HashSet();

    private Set excludes = new HashSet();

    /**
     * Creates a new <code>Resource</code> instance with the given
     * identifier. <p>
     *
     * @param resourceRegistry the resource registry this resource is
     *        registered into.
     * @param id the resource id.
     */
    Resource(ResourceRegistry resourceRegistry, String id) {
        this.resourceRegistry = resourceRegistry;
        this.id = id;
    }

    /**
     * Returns the resource registry this resource is registered into. <p>
     *
     * @return the resource registry this resource is registered into.
     */
    ResourceRegistry getResourceRegistry() {
        return resourceRegistry;
    }

    /**
     * Returns the bossa engine this resource is part, if any. <p>
     *
     * @return the bossa engine this resource is part,
     *         <code>null</code> if not part of a bossa engine.
     */
    Bossa getBossa() {
        if (getResourceRegistry() != null) {
            return getResourceRegistry().getBossa();
        } else {
            return null;
        }
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
     * Returns the includes list of this resource. <p>
     *
     * @return the includes list of this resource.
     */
    public Set getIncludes() {
        return Collections.unmodifiableSet(includes);
    }

    /**
     * Returns the excludes list of this resource. <p>
     *
     * @return the excludes list of this resource.
     */
    public Set getExcludes() {
        return Collections.unmodifiableSet(excludes);
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
        ResourceTransaction includeTransaction =
            new IncludeInResource(this, resource);
        return
            ((Boolean) getBossa().execute(includeTransaction)).booleanValue();
    }

    /**
     * Includes a resource in this resource. Removes the resource from
     * the excludes list if needed. <p>
     *
     * This method does not create a transaction in the prevalent system. The
     * execution of this method will not be persistent unless it is called
     * inside an appropriate transaction. <p>
     *
     * @param resource the resource to be included.
     * @param notify if this operation should be notified.
     * @return <code>false</code> if resource includes this resource,
     *         <code>true</code> otherwise.
     */
    public boolean includeImpl(Resource resource, boolean notify) {
        if (resource.depends(this)) {
            return false;
        }
        excludes.remove(resource);
        includes.add(resource);
        if (notify) {
            ResourceEvents queue = new ResourceEvents();
            queue.newTwoResourcesEvent(getBossa(),
                                       ResourceEvents.ID_INCLUDE_IN_RESOURCE,
                                       resource, this);
            queue.notifyAll(getBossa());
        }
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
        ResourceTransaction excludeTransaction =
            new ExcludeInResource(this, resource);
        return
            ((Boolean) getBossa().execute(excludeTransaction)).booleanValue();
    }

    /**
     * Excludes a resource from this resource. Removes the resource from
     * the includes list if needed. <p>
     *
     * This method does not create a transaction in the prevalent system. The
     * execution of this method will not be persistent unless it is called
     * inside an appropriate transaction. <p>
     *
     * @param resource the resource to be excluded.
     * @param notify if this operation should be notified.
     * @return <code>false</code> if resource excludes this resource,
     *         <code>true</code> otherwise.
     */
    public boolean excludeImpl(Resource resource, boolean notify) {
        if (resource.depends(this)) {
            return false;
        }
        includes.remove(resource);
        excludes.add(resource);
        if (notify) {
            ResourceEvents queue = new ResourceEvents();
            queue.newTwoResourcesEvent(getBossa(),
                                       ResourceEvents.ID_EXCLUDE_IN_RESOURCE,
                                       resource, this);
            queue.notifyAll(getBossa());
        }
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
        ResourceTransaction removeTransaction =
            new RemoveFromResource(this, resource);
        getBossa().execute(removeTransaction);
    }

    /**
     * Removes a resource from this resource. Effectively removes it from
     * any of the includes or excludes list. <p>
     *
     * This method does not create a transaction in the prevalent system. The
     * execution of this method will not be persistent unless it is called
     * inside an appropriate transaction. <p>
     *
     * @param resource the resource to be removed.
     * @param notify if this operation should be notified.
     */
    public void removeImpl(Resource resource, boolean notify) {
        boolean inc = includes.remove(resource);
        boolean exc = excludes.remove(resource);
        if ((inc || exc) && notify) {
            ResourceEvents queue = new ResourceEvents();
            queue.newTwoResourcesEvent(getBossa(),
                                       ResourceEvents.ID_REMOVE_FROM_RESOURCE,
                                       resource, this);
            queue.notifyAll(getBossa());
        }
    }

    /**
     * Determines if a resource is contained in this resource. <p>
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

    /**
     * Checks if this resource depends on the given resource.
     * Specifically, checks if it is necessary to call
     * <code>contains()</code> in the given resource to check
     * <code>contains()</code> in this resource. <p>
     *
     * @param resource the resource to be checked.
     * @return <code>true</code> if this resource depends,
     *         <code>false</code> otherwise.
     */
    private boolean depends(Resource resource) {

        if (this.equals(resource)) {
            return true;
        }

        if (includes.contains(resource)) {
            return true;
        }

        Iterator it = includes.iterator();
        while (it.hasNext()) {
            Resource curr = (Resource) it.next();
            if (curr.depends(resource)) {
                return true;
            }
        }

        if (excludes.contains(resource)) {
            return true;
        }

        it = excludes.iterator();
        while (it.hasNext()) {
            Resource curr = (Resource) it.next();
            if (curr.depends(resource)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns a string with the resource identifier. <p>
     *
     * @return a string representation of this resource.
     */
    public String toString() {
        return id;
    }

}
