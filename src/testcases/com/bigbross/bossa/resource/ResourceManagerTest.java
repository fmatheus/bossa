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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class ResourceManagerTest extends TestCase {

    private ResourceManager resourceManager;

    public ResourceManagerTest(String name) {
	super(name);
    }

    protected void setUp() {
	System.out.println("Setting up a resource manager test.");
        resourceManager = new ResourceManager();
    }

    public void testAddRemoveRegistries() {
        ResourceRegistry context1 = new ResourceRegistry("c1");
        assertFalse(resourceManager.removeRegistry(context1));
        assertTrue(resourceManager.addRegistry(context1));
        assertFalse(resourceManager.addRegistry(context1));
        assertTrue(resourceManager.removeRegistry(context1));
    }

    public void testFindSelf() {
        String globalId = resourceManager.getGlobalId();
        assertSame(resourceManager, resourceManager.getRegistry(globalId));
    }

    public void testFindRegistry() {
        ResourceRegistry context1 = new ResourceRegistry("c1");
        ResourceRegistry context2 = new ResourceRegistry("c2");
        resourceManager.registerSubContext(context1);
        context1.registerSubContext(context2);

        assertSame(context1,
                   resourceManager.getRegistry(context1.getGlobalId()));        
        assertSame(context2,
                   resourceManager.getRegistry(context2.getGlobalId()));
    }

    public void testFindRegistryTree() {
        ResourceRegistry context1 = new ResourceRegistry("c1");
        ResourceRegistry context2 = new ResourceRegistry("c2");
        context1.registerSubContext(context2);
        resourceManager.registerSubContext(context1);

        assertSame(context1,
                   resourceManager.getRegistry(context1.getGlobalId()));        
        assertSame(context2,
                   resourceManager.getRegistry(context2.getGlobalId()));
    }

    public void testRemoveRegistry() {
        ResourceRegistry context1 = new ResourceRegistry("c1");
        ResourceRegistry context2 = new ResourceRegistry("c2");
        resourceManager.registerSubContext(context1);
        context1.registerSubContext(context2);
        String id1 = context1.getGlobalId();
        String id2 = context2.getGlobalId();
        resourceManager.removeSubContext(context1);

        assertNull(resourceManager.getRegistry(id1));
        assertNull(resourceManager.getRegistry(id2));
    }
}
