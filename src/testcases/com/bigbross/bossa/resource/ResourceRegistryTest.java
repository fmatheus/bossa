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

import junit.framework.TestCase;

public class ResourceRegistryTest extends TestCase {

    private ResourceRegistry resourceRegistry;

    public ResourceRegistryTest(String name) {
	super(name);
    }

    protected void setUp() {
        resourceRegistry = new ResourceRegistry("null");
    }

    public void testAddResource() {
        assertNotNull(resourceRegistry.createResourceImpl("r1", false));
        assertNull(resourceRegistry.createResourceImpl("r1", false));
    }

    public void testGetResource() {
        assertNull(resourceRegistry.getResource("r1"));
        Resource r = resourceRegistry.createResourceImpl("r1", false);
        assertSame(r, resourceRegistry.getResource("r1"));
    }

    public void testNullResourceId() {
        assertNull(resourceRegistry.createResourceImpl(null, false));
        assertNull(resourceRegistry.getResource(null));
    }

    public void testRemoveResource() {
        Resource r = resourceRegistry.createResourceImpl("r1", false);
        assertTrue(resourceRegistry.removeResourceImpl(r, false));
        assertNotNull(resourceRegistry.createResourceImpl("r1", false));
    }

    public void testNestedRemoveResource() {
        Resource r1 = resourceRegistry.createResourceImpl("r1", false);
        Resource r2 = resourceRegistry.createResourceImpl("r2", false);
        r1.includeImpl(r2, false);

        assertTrue(resourceRegistry.removeResourceImpl(r2, false));
        assertFalse(r1.contains(r2));
    }

    public void testAddRemoveSubContext() {
        ResourceRegistry subContext = new ResourceRegistry("test");
        assertFalse(resourceRegistry.removeSubContext(subContext));
        assertTrue(resourceRegistry.registerSubContext(subContext));
        assertSame(resourceRegistry, subContext.getSuperContext());
        assertFalse(resourceRegistry.registerSubContext(subContext));
        assertTrue(resourceRegistry.removeSubContext(subContext));
        assertNull(subContext.getSuperContext());
        assertTrue(resourceRegistry.registerSubContext(subContext));
    }

    public void testGetSubContext() {
        ResourceRegistry subContext = new ResourceRegistry("test");
        assertTrue(resourceRegistry.registerSubContext(subContext));
        assertSame(subContext, resourceRegistry.getSubContext("test"));
        assertNull(resourceRegistry.getSubContext("invalid"));
    }

    public void testRemoveResourceInSubContext() {
        ResourceRegistry subContext = new ResourceRegistry("test");
        assertTrue(resourceRegistry.registerSubContext(subContext));
        Resource r1 = resourceRegistry.createResourceImpl("r1", false);
        Resource r2 = subContext.createResourceImpl("r2", false);
        r2.includeImpl(r1, false);

        assertTrue(resourceRegistry.removeResourceImpl(r1, false));
        assertFalse(r2.contains(r1));
    }

    public void testGetResourceManager() {
        ResourceManager resourceManager = new ResourceManager();
        ResourceRegistry subContext = new ResourceRegistry("test");
        resourceManager.registerSubContext(resourceRegistry);
        resourceRegistry.registerSubContext(subContext);

        assertSame(resourceManager, subContext.getResourceManager());

        resourceRegistry.removeSubContext(subContext);
        assertNull(subContext.getResourceManager());
    }

    public void testGetGlobalId() {
        ResourceManager resourceManager = new ResourceManager();
        ResourceRegistry subContext = new ResourceRegistry("test");
        resourceManager.registerSubContext(resourceRegistry);
        resourceRegistry.registerSubContext(subContext);

        assertEquals("root.null.test", subContext.getGlobalId());
    }
}
