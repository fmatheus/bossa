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
 * This class implements the include operation of <code>Resource</code>
 * through the prevalence subsystem. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 * @see Resource#include(Resource)
 * @see Resource#includeImpl(Resource, boolean)
 */
class IncludeInResource extends ResourceHandlerTransaction {

    /**
     * Creates a new include operation. <p>
     *
     * @param host the resource that will receive the include.
     * @param resource the resource that will be included.
     */
    IncludeInResource(Resource host, Resource resource) {
        super(host, resource);
    }

    /**
     * @see ResourceHandlerTransaction#execute(Resource, Resource)
     */
    protected Object execute(Resource host, Resource resource) {
        return new Boolean(host.includeImpl(resource, true));
    }
}
