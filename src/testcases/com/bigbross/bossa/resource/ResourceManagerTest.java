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

    public ResourceManagerTest(String name) {
	super(name);
    }

    protected void setUp() {
	System.out.println("Setting up a resource manager test.");
    }

    public void testCreateGroup() {
        ResourceManager resourceManager = new ResourceManager();
        
        assertTrue(resourceManager.createGroup("testGroup"));
        assertFalse(resourceManager.createGroup("testGroup"));
    }
    
    public void testRemoveGroup() {
        ResourceManager resourceManager = new ResourceManager();
        
        assertTrue(resourceManager.createGroup("testGroup"));
        assertTrue(resourceManager.removeGroup("testGroup"));
        assertTrue(resourceManager.createGroup("testGroup"));
    }

    public void testAddRemoveResource() {
        ResourceManager resourceManager = new ResourceManager();
        assertTrue(resourceManager.createGroup("testGroup"));
        
        resourceManager.addResource("testGroup", "jdoe");
        assertTrue(resourceManager.removeResource("testGroup", "jdoe"));
        assertFalse(resourceManager.removeResource("testGroup", "jdoe"));
    }
    
    public void testGetAllResources() {
        ResourceManager resourceManager = new ResourceManager();
        assertTrue(resourceManager.createGroup("testGroup"));
        resourceManager.addResource("testGroup", "jdoe");
        resourceManager.addResource("testGroup", "mdoe");
        resourceManager.addResource("testGroup", "adoe");
        
        List resources = resourceManager.getAllResources("testGroup");
        assertEquals(3, resources.size());
        String r0 = (String) resources.get(0);
        String r1 = (String) resources.get(1);
        String r2 = (String) resources.get(2);
        assertFalse(r0.equals(r1));
        assertFalse(r0.equals(r2));
        assertFalse(r1.equals(r2));
        assertTrue(r0.equals("jdoe") || r0.equals("mdoe") || r0.equals("adoe"));
        assertTrue(r1.equals("jdoe") || r1.equals("mdoe") || r1.equals("adoe"));
        assertTrue(r2.equals("jdoe") || r2.equals("mdoe") || r2.equals("adoe"));
    }
    
    public void testGroupContains() {
        ResourceManager resourceManager = new ResourceManager();
        assertTrue(resourceManager.createGroup("testGroup"));
        resourceManager.addResource("testGroup", "jdoe");

        assertTrue(resourceManager.groupContains("testGroup", "jdoe"));
        assertFalse(resourceManager.groupContains("testGroup", "mdoe"));
    }
}
