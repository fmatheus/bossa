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

import junit.framework.TestCase;

import com.bigbross.bossa.BossaException;

public class CommandsTest extends TestCase {

    private ResourceManager resourceManager;

    public CommandsTest(String name) {
	super(name);
    }

    protected void setUp() {
	System.out.println("Setting up a resource command test.");
    
        resourceManager = new ResourceManager();
    }

    public void testCreateResource() {
        CreateResource command = new CreateResource("joe");
        Resource resource = (Resource) command.execute(resourceManager);
        assertSame(resource, resourceManager.getResource("joe"));
    }

    public void testRemoveResource() {
        Resource resource = resourceManager.createResourceImpl("joe");
        assertNotNull(resource);
        
        RemoveResource command = new RemoveResource(resource);
        assertTrue(((Boolean) command.execute(resourceManager)).
                   booleanValue());
        assertNull(resourceManager.getResource("joe"));
    }
    
    public void testIncludeInResource() {
        Resource group = resourceManager.createResourceImpl("trumps");
        Resource element = resourceManager.createResourceImpl("joe");

        IncludeInResource command = new IncludeInResource(group, element);        
        assertTrue(((Boolean) command.execute(resourceManager)).
                   booleanValue());
        assertTrue(group.contains(element));
    }
    
    public void testExcludeInResource() {
        Resource group1 = resourceManager.createResourceImpl("trumps");
        Resource group2 = resourceManager.createResourceImpl("good");
        Resource element = resourceManager.createResourceImpl("joe");
        group1.includeImpl(group2);
        group2.includeImpl(element);
        assertTrue(group1.contains(element));
        
        ExcludeInResource command = new ExcludeInResource(group1, element);        
        assertTrue(((Boolean) command.execute(resourceManager)).
                   booleanValue());
        assertFalse(group1.contains(element));
    }
    
    public void testRemoveFromResource() {
        Resource group = resourceManager.createResourceImpl("trumps");
        Resource element = resourceManager.createResourceImpl("joe");
        group.includeImpl(element);
        assertTrue(group.contains(element));

        RemoveFromResource command = new RemoveFromResource(group, element);        
        command.execute(resourceManager);
        assertFalse(group.contains(element));
    }
}
