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

import java.util.Date;

/**
 * This class implements the create resource operation of
 * <code>ResourceManager</code> through the prevalence subsystem. <p>
 * 
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 * @see com.bigbross.bossa.resource.ResourceManager#createResource(String)
 * @see com.bigbross.bossa.resource.ResourceManager#createResourceImpl(String)
 */
class CreateResource extends ResourceTransaction {

    private String id;
    
    /**
     * Creates a new create resource operation. <p>
     * 
     * @param id the resource id.
     */    
    CreateResource(String id) {
        this.id = id;
    }

    /**
     * Executes the operation. <p>
     * 
     * @see com.bigbross.bossa.resource.ResourceTransaction#execute(
     *      ResourceManager, Date)
     */
    protected Object execute(ResourceManager resourceManager, Date time) {
        return resourceManager.createResourceImpl(id);
    }
}
