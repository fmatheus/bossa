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
 * This class implements the exclude operation of <code>Resource</code>
 * through the prevalence subsystem. <p>
 * 
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 * @see com.bigbross.bossa.resource.Resource#exclude(Resource)
 * @see com.bigbross.bossa.resource.Resource#excludeImpl(Resource)
 */
class ExcludeInResource extends ResourceCommand {

    private String hostId;
    private String resourceId;
    
    /**
     * Creates a new exclude operation. <p>
     *
     * @param host the resource that will receive the exclude. 
     * @param resource the resource that will be excluded.
     */    
    ExcludeInResource(Resource host, Resource resource) {
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
        return new Boolean(host.excludeImpl(resource));
    }
}
