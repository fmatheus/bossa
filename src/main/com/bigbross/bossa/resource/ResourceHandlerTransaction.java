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


/**
 * This class implements a generic operation of the <code>Resource</code>
 * class. It will locate two resources wherever they may be in the system.
 * <p>
 * 
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
abstract class ResourceHandlerTransaction extends ResourceTransaction {

    private String hostRegistryId;
    private String hostId;
    private String resourceRegistryId;
    private String resourceId;
    
    /**
     * Creates a new resource operation. <p>
     *
     * @param host the resource that will perform the operation. 
     * @param resource the resource that will be manipulated.
     */    
    ResourceHandlerTransaction(Resource host, Resource resource) {
        this.hostRegistryId =
            host.getResourceRegistry().getGlobalId();
        this.hostId = host.getId();
        this.resourceRegistryId =
            resource.getResourceRegistry().getGlobalId();
        this.resourceId = resource.getId();
    }

    /**
     * @see ResourceTransaction#execute(ResourceManager)
     */
    protected Object execute(ResourceManager resourceManager) {
        ResourceRegistry hostRegistry =
            resourceManager.getRegistry(hostRegistryId);
        Resource host = hostRegistry.getResource(hostId);
        ResourceRegistry resourceRegistry =
            resourceManager.getRegistry(resourceRegistryId);
        Resource resource = resourceRegistry.getResource(resourceId);
        return execute(host, resource);
    }

    /**
     * Executes a transaction using the two provided resources. <p>
     * 
     * @param host the resource that will perform the operation. 
     * @param resource the resource that will be manipulated.
     * @return the result of the transaction execution.
     */
    protected abstract Object execute(Resource host, Resource resource);
}
