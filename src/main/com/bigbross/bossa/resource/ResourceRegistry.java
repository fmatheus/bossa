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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaException;

/**
 * This class stores registered resources. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class ResourceRegistry implements Serializable {

    private String id;

    private ResourceRegistry superContext;

    private Map resources;

    private Map contexts;

    /**
     * Creates a new empty resource registry. <p>
     *
     * @param id the id of this registry.
     */
    public ResourceRegistry(String id) {
        this.id = id;
        this.superContext = null;
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
     * Sets a context as the super context of this registry. <p>
     *
     * @param context the super context.
     */
    public void setSuperContext(ResourceRegistry context) {
        this.superContext = context;
    }

    /**
     * Returns the super context of this registry. <p>
     *
     * @return the super context of this registry.
     */
    public ResourceRegistry getSuperContext() {
        return superContext;
    }

    /**
     * Returns the top level resource registry, the resource manager. <p>
     *
     * @return the resource manager, <code>null</code> if the root registry
     *         is not a resource manager.
     */
    ResourceManager getResourceManager() {
        if (superContext == null) {
            return null;
        } else {
            return superContext.getResourceManager();
        }
    }

    /**
     * Returns the bossa engine this resource registry is part, if any. <p>
     *
     * @return the bossa engine this resource registry is part,
     *         <code>null</code> if not part of a bossa engine.
     */
    Bossa getBossa() {
        if (getResourceManager() != null) {
            return getResourceManager().getBossa();
        } else {
            return null;
        }
    }

    /**
     * Returns the global id of this registry. This is the id that allows
     * the retrieval of this registry from the resource manager. <p>
     *
     * @return the resource manager.
     */
    String getGlobalId() {
        if (superContext == null) {
            return getId();
        } else {
            return superContext.getGlobalId() + "." + getId();
        }
    }

    /**
     * Registers this resource registry in the resource manager, if it
     * exists. If this registry contains sub contexts, they will be
     * registered also. <p>
     *
     * @return <code>true</code> if there is a resource manager and this
     *         registry was succesfully registered or if there is no
     *         resource manager,
     *         <code>false</code> if there is already a registry with the
     *         same global id in the resource manager.
     */
    private boolean registerInResourceManager() {
        ResourceManager resourceManager = getResourceManager();
        if (resourceManager == null) {
            return true;
        }
        if (!resourceManager.addRegistry(this)) {
            return false;
        }
        Iterator i = contexts.values().iterator();
        while (i.hasNext()) {
            if (!((ResourceRegistry) i.next()).registerInResourceManager()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Unregisters this resource registry in the resource manager, if it
     * exists. If this registry contains sub contexts, they will be
     * unregistered also. <p>
     *
     * @return <code>true</code> if there is a resource manager and this
     *         registry was succesfully unregistered or if there is no
     *         resource manager,
     *         <code>false</code> if the global id of this registry was
     *         not found in the resource manager.
     */
    private boolean unregisterInResourceManager() {
        ResourceManager resourceManager = getResourceManager();
        if (resourceManager == null) {
            return true;
        }
        if (!resourceManager.removeRegistry(this)) {
            return false;
        }
        Iterator i = contexts.values().iterator();
        while (i.hasNext()) {
           if (!((ResourceRegistry) i.next()).unregisterInResourceManager()) {
               return false;
           }
        }
        return true;
    }

    /**
     * Returns the resource with the given id. <p>
     *
     * @param id the resource id.
     * @return the <code>Resource</code> object,
     *         <code>null</code> if there is no resource with this id.
     */
    public Resource getResource(String id) {
        return (Resource) resources.get(id);
    }

    /**
     * Returns all registered resources. <p>
     *
     * @return a list of all resources registered.
     */
    public List getResources() {
        ArrayList resourceList = new ArrayList();
        resourceList.addAll(resources.values());
        return resourceList;
    }

    /**
     * Creates a new resource in this registry. <p>
     *
     * @param id the id of the resource to be created.
     * @return the created <code>Resource</code> object,
     *         <code>null</code> if there is already a resource with this id.
     * @exception PersistenceException if an error occours when making the
     *            execution of this method persistent.
     */
    public Resource createResource(String id) throws BossaException {
        ResourceTransaction createTransaction = new CreateResource(id);
        return (Resource) getBossa().execute(createTransaction);
    }

    /**
     * Creates a new resource in this registry. <p>
     *
     * This method does not create a transaction in the prevalent system. The
     * execution of this method will not be persistent unless it is called
     * inside an appropriate transaction. <p>
     *
     * @param id the id of the resource to be created.
     * @param notify if this operation should be notified.
     * @return the created <code>Resource</code> object,
     *         <code>null</code> if there is already a resource with this id.
     */
    public Resource createResourceImpl(String id, boolean notify) {
        if ((id != null) && !resources.containsKey(id)) {
            Resource resource = new Resource(this, id);
            resources.put(id, resource);
            if (notify) {
                ResourceEvents queue = new ResourceEvents();
                queue.newSingleResourceEvent(getBossa(),
                                             ResourceEvents.ID_CREATE_RESOURCE,
                                             resource);
                queue.notifyAll(getBossa());
            }
            return resource;
        } else {
            return null;
        }
    }

    /**
     * Removes a resource from this registry. <p>
     *
     * @param resource the resource to be removed.
     * @return <code>true</code> if the resource was removed,
     *         <code>false</code> if the resource was not found.
     * @exception PersistenceException if an error occours when making the
     *            execution of this method persistent.
     */
    public boolean removeResource(Resource resource) throws BossaException {
        ResourceTransaction removeTransaction = new RemoveResource(resource);
        return ((Boolean) getBossa().execute(removeTransaction)).booleanValue();
    }

    /**
     * Removes a resource from this registry. <p>
     *
     * This method does not create a transaction in the prevalent system. The
     * execution of this method will not be persistent unless it is called
     * inside an appropriate transaction. <p>
     *
     * @param resource the resource to be removed.
     * @param notify if this operation should be notified.
     * @return <code>true</code> if the resource was removed,
     *         <code>false</code> if the resource was not found.
     */
    public boolean removeResourceImpl(Resource resource, boolean notify) {
        if (resources.remove(resource.getId()) != null) {
            clearReferences(resource, notify);
            if (notify) {
                ResourceEvents queue = new ResourceEvents();
                queue.newSingleResourceEvent(getBossa(),
                                             ResourceEvents.ID_REMOVE_RESOURCE,
                                             resource);
                queue.notifyAll(getBossa());
            }
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
     * @param notify if removals triggered by this operation should be notified.
     */
    private void clearReferences(Resource resource, boolean notify) {
        Iterator i = resources.values().iterator();
        while (i.hasNext()) {
            ((Resource) i.next()).removeImpl(resource, notify);
        }
        i = contexts.values().iterator();
        while (i.hasNext()) {
            ((ResourceRegistry) i.next()).clearReferences(resource, notify);
        }
    }

    /**
     * Registers another resource registry as a sub context of this resource
     * registry. All entries in the sub context are expected to possibly
     * depend on the entries of this registry, and are notified of resource
     * removals. <p>
     *
     * Also, if this registry tree is rooted at a resource manager, the
     * registry tree being registered is put in the global registry index.
     * <p>
     *
     * @param context the sub context.
     * @return <code>true</code> if the sub context was added,
     *         <code>false</code> if the sub context was already present.
     */
    public boolean registerSubContext(ResourceRegistry context) {
        if (!contexts.containsKey(context.getId())) {
            context.setSuperContext(this);
            if (context.registerInResourceManager()) {
                contexts.put(context.getId(), context);
                return true;
            } else {
                context.setSuperContext(null);
            }
        }
        return false;
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
     * Also, if this registry tree is rooted at a resource manager, the
     * registry tree being removed is removed from the global registry index.
     * <p>
     *
     * @param context the sub context.
     * @return <code>true</code> if the sub context was removed,
     *         <code>false</code> if the sub context was not found.
     */
    public boolean removeSubContext(ResourceRegistry context) {
        ResourceRegistry myContext =
            (ResourceRegistry) contexts.get(context.getId());
        if (myContext != null) {
            myContext.unregisterInResourceManager();
            myContext.setSuperContext(null);
            contexts.remove(myContext.getId());
            return true;
        } else {
            return false;
        }
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
