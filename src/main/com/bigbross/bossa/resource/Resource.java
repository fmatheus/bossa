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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This represents a resource or a group of resources.
 * This may include or exclude other resources.
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Resource {

    protected String id;

    protected Set includes = new HashSet();

    protected Set excludes = new HashSet();

    /**
     * Creates a new <code>Resource</code> instance with given identifier. <p>
     *
     * @param id the resource id.
     */
    Resource(String id) {
        this.id = id;
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
     * Returns true if this resource includes other resource, so is a group.
     *
     * @return <code>true</code> if is a group, <code>false</code> otherwise.
     */
    public boolean isGroup() {
        return includes.size() > 0;
    }

    /**
     * Includes a resource to this. <p>
     * Removes the resource from excludes if needed.
     *
     * @param resource the resource to be included.
     * @return <code>false</code> if resource includes this, <code>true</code> otherwise.
     */
    public boolean include(Resource resource) {
        if (resource.contains(this)) {
            return false;
        }
        excludes.remove(resource);
        includes.add(resource);
        return true;
    }

    /**
     * Excludes a resource from this. <p>
     * Removes the resource from includes if needed.
     *
     * @param resource the resource to be excluded.
     * @return <code>false</code> if resource excludes this, <code>true</code> otherwise.
     */
    public boolean exclude(Resource resource) {
        if (resource.excludes(this)) {
            return false;
        }
        includes.remove(resource);
        excludes.add(resource);
        return true;
    }

    /**
     * Removes a resource from this. <p>
     * Removes from any of includes or excludes.
     *
     * @param resource the resource to be removed.
     */
    public void remove(Resource resource) {
        includes.remove(resource);
        excludes.remove(resource);
    }

    /**
     * Checks if a resource is excluded by this. <p>
     *
     * @param resource the resource to be looked for.
     * @return <code>true</code> if the resource is found, <code>false</code> otherwise.
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
     * @return <code>true</code> if the resource is found, <code>false</code> otherwise.
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
