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


/**
 * This class implements the create resource operation of
 * <code>ResourceManager</code> through the prevalence subsystem. <p>
 * 
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 * @see ResourceManager#createResource(String)
 * @see ResourceManager#createResourceImpl(String, boolean)
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
     * @see ResourceTransaction#execute(ResourceManager)
     */
    protected Object execute(ResourceManager resourceManager) {
        return resourceManager.createResourceImpl(id, true);
    }
}
