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

public class ResourceRegistryTest extends TestCase {

    private ResourceRegistry resourceRegistry;
    private Resource r1, r2;

    public ResourceRegistryTest(String name) {
	super(name);
    }

    protected void setUp() {
	System.out.println("Setting up a resource registry test.");
        resourceRegistry = new ResourceRegistry("null");
        r1 = new Resource(null, "r1");
        r2 = new Resource(null, "r2");
    }

    public void testAddResource() {
        assertTrue(resourceRegistry.addResource(r1));
        assertFalse(resourceRegistry.addResource(r1));
    }
    
    public void testGetResource() {
        assertNull(resourceRegistry.getResource("r1"));
        assertTrue(resourceRegistry.addResource(r1));
        assertSame(r1, resourceRegistry.getResource("r1"));
    }

    public void testRemoveResource() {
        assertTrue(resourceRegistry.addResource(r1));
        assertTrue(resourceRegistry.removeResource(r1));
        assertTrue(resourceRegistry.addResource(r1));
    }

    public void testNestedRemoveResource() {
        assertTrue(resourceRegistry.addResource(r1));
        assertTrue(resourceRegistry.addResource(r2));
        r1.includeImpl(r2);
        
        assertTrue(resourceRegistry.removeResource(r2));
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
        assertTrue(resourceRegistry.addResource(r1));
        assertTrue(subContext.addResource(r2));
        r2.includeImpl(r1);

        assertTrue(resourceRegistry.removeResource(r1));
        assertFalse(r2.contains(r1));
    }
}
