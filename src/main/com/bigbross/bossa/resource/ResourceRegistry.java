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

/**
 * This class stores registered resources. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class ResourceRegistry implements Serializable {

    private String id;

    private Map resources;
    
    private Map contexts;

    /**
     * Creates a new empty resource registry. <p>
     * 
     * @param id the id of this registry.
     */
    public ResourceRegistry(String id) {
        this.id = id;
        this.resources = new HashMap();
        this.contexts = new HashMap();
    }

    /**
     * Returns the id of this registry. <p>
     * 
     * @return the id of this registry.
     */
    public String getId() {
        return id;
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
     * Returns all registered resources. <p>
     * 
     * @return A list of all resources registered.
     */
    public List getResources() {
        ArrayList resourceList = new ArrayList();
        resourceList.addAll(resources.values());
        return resourceList;
    }

    /**
     * Adds a resource to this registry. <p>
     * 
     * @param resource the resource to be added.
     * @return <code>true</code> if the resource was added,
     *         <code>false</code> if there was already a resource
     *         with the same id.
     */    
    public boolean addResource(Resource resource) {
        String id = resource.getId();
        if (!resources.containsKey(id)) {
            resources.put(id, resource);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes a resource from this registry. <p>
     * 
     * @param resource the resource to be removed.
     * @return <code>true</code> if the resource was removed,
     *         <code>false</code> if the resource was not found.
     */
    public boolean removeResource(Resource resource) {
        if (resources.remove(resource.getId()) != null) {
            clearReferences(resource);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes any direct reference to a resource from all resources in
     * this registry. <p>
     * 
     * @param resource the resource to be removed.
     */
    private void clearReferences(Resource resource) {
        Iterator i = resources.values().iterator();
        while (i.hasNext()) {
            ((Resource) i.next()).removeImpl(resource);
        }
        i = contexts.values().iterator();
        while (i.hasNext()) {
            ((ResourceRegistry) i.next()).clearReferences(resource);
        }
    }

    /**
     * Registers another resource registry as a sub context of this resource
     * registry. All entries in the sub context are expected to possibly
     * depend on the entries of this registry, and are notified of resource
     * removals. <p>
     * 
     * @param context the sub context.
     * @return <code>true</code> if the sub context was added,
     *         <code>false</code> if the sub context was already present.
     */
    public boolean registerSubContext(ResourceRegistry context) {
        if (!contexts.containsKey(context.getId())) {
            contexts.put(context.getId(), context);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the registered sub context with the given id. <p>
     * 
     * @param id the sub context id.
     * @return the sub context,
     *         <code>null</code> if the sub context was not found.
     */
    ResourceRegistry getSubContext(String id) {
        return (ResourceRegistry) contexts.get(id);
    }
    
    /**
     * Remove the provided registry from the list of registered sub contexts
     * of this registry. <p>
     * 
     * @param context the sub context.
     * @return <code>true</code> if the sub context was removed,
     *         <code>false</code> if the sub context was not found.
     */
    public boolean removeSubContext(ResourceRegistry context) {
        return (contexts.remove(context.getId()) != null);
    } 

    /**
     * Compiles a resource expression. <p>
     * 
     * It uses this <code>ResourceManager</code> to link the resources in
     * the expression.
     *
     * @param expression the resource expression to be compiled.
     * @return a <code>Expression</code> representing the compiled resource
     *         expression.
     */
    public Expression compile(String expression) {
        return Expression.compile(this, expression);
    }
}
