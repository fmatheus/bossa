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

/**
 * This class implements the remove operation of <code>Resource</code>
 * through the prevalence subsystem. <p>
 * 
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 * @see com.bigbross.bossa.resource.Resource#remove(Resource)
 * @see com.bigbross.bossa.resource.Resource#removeImpl(Resource)
 */
class RemoveFromResource extends ResourceCommand {

    private String hostId;
    private String resourceId;
    
    /**
     * Creates a new remove operation. <p>
     *
     * @param host the resource that will release the resource. 
     * @param resource the resource that will be removed.
     */    
    RemoveFromResource(Resource host, Resource resource) {
        this.hostId = host.getId();
        this.resourceId = resource.getId();
    }

    /**
     * Executes the operation. <p>
     * 
     * @see com.bigbross.bossa.resource.ResourceCommand#execute(ResourceManager)
     */
    protected Serializable execute(ResourceManager resourceManager) {
        Resource host = resourceManager.getResource(hostId);
        Resource resource = resourceManager.getResource(resourceId);
        host.removeImpl(resource);
        return null;
    }
}
