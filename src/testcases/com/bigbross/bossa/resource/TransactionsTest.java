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

import junit.framework.TestCase;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaFactory;
import com.bigbross.bossa.BossaTestUtil;
import com.bigbross.bossa.wfnet.CaseTypeManager;

public class TransactionsTest extends TestCase {

    private ResourceManager resourceManager;
    private Date now;

    public TransactionsTest(String name) {
	super(name);
    }

    protected void setUp() {
        resourceManager = new ResourceManager();
        now = new Date();
    }

    public void testCreateResource() {
        CreateResource transaction = new CreateResource("joe");
        Resource resource =
            (Resource) transaction.execute(resourceManager, now);
        assertSame(resource, resourceManager.getResource("joe"));
    }

    public void testRemoveResource() {
        Resource resource = resourceManager.createResourceImpl("joe");
        assertNotNull(resource);
        
        RemoveResource transaction = new RemoveResource(resource);
        assertTrue(((Boolean) transaction.execute(resourceManager, now)).
                   booleanValue());
        assertNull(resourceManager.getResource("joe"));
    }
    
    public void testIncludeInResource() throws Exception {
        Resource group = resourceManager.createResourceImpl("trumps");
        Resource element = resourceManager.createResourceImpl("joe");

        IncludeInResource transaction = new IncludeInResource(group, element);        
        assertTrue(((Boolean) transaction.execute(resourceManager, now)).
                   booleanValue());
        assertTrue(group.contains(element));
    }

    public void testIncludeInCaseTypeResource()  throws Exception {
        Bossa bossa = BossaFactory.transientBossa();
        CaseTypeManager caseTypeManager = bossa.getCaseTypeManager();
        caseTypeManager.registerCaseType(BossaTestUtil.createCaseType("test"));
        ResourceManager myResourceManager = bossa.getResourceManager();
        
        Resource group = (Resource)
            caseTypeManager.getCaseType("test").getResources().get(0);
        Resource element = myResourceManager.createResourceImpl("joe");

        IncludeInResource transaction = new IncludeInResource(group, element);        
        assertTrue(((Boolean) transaction.execute(myResourceManager, now)).
                   booleanValue());
        assertTrue(group.contains(element));
    }
    
    public void testExcludeInResource() throws Exception {
        Resource group1 = resourceManager.createResourceImpl("trumps");
        Resource group2 = resourceManager.createResourceImpl("good");
        Resource element = resourceManager.createResourceImpl("joe");
        group1.includeImpl(group2);
        group2.includeImpl(element);
        assertTrue(group1.contains(element));
        
        ExcludeInResource transaction = new ExcludeInResource(group1, element);        
        assertTrue(((Boolean) transaction.execute(resourceManager, now)).
                   booleanValue());
        assertFalse(group1.contains(element));
    }

    public void testExcludeInCaseTypeResource() throws Exception {
        Bossa bossa = BossaFactory.transientBossa();
        CaseTypeManager caseTypeManager = bossa.getCaseTypeManager();
        caseTypeManager.registerCaseType(BossaTestUtil.createCaseType("test"));
        ResourceManager myResourceManager = bossa.getResourceManager();
        
        Resource group1 = (Resource)
            caseTypeManager.getCaseType("test").getResources().get(0);
        Resource group2 = myResourceManager.createResourceImpl("good");
        Resource element = myResourceManager.createResourceImpl("joe");
        group1.includeImpl(group2);
        group2.includeImpl(element);
        assertTrue(group1.contains(element));
        
        ExcludeInResource transaction = new ExcludeInResource(group1, element);        
        assertTrue(((Boolean) transaction.execute(myResourceManager, now)).
                   booleanValue());
        assertFalse(group1.contains(element));
    }
    
    public void testRemoveFromResource() throws Exception {
        Resource group = resourceManager.createResourceImpl("trumps");
        Resource element = resourceManager.createResourceImpl("joe");
        group.includeImpl(element);
        assertTrue(group.contains(element));

        RemoveFromResource transaction = new RemoveFromResource(group, element);        
        transaction.execute(resourceManager, now);
        assertFalse(group.contains(element));
    }

    public void testRemoveFromCaseTypeResource() throws Exception {
        Bossa bossa = BossaFactory.transientBossa();
        CaseTypeManager caseTypeManager = bossa.getCaseTypeManager();
        caseTypeManager.registerCaseType(BossaTestUtil.createCaseType("test"));
        ResourceManager myResourceManager = bossa.getResourceManager();
        
        Resource group = (Resource)
            caseTypeManager.getCaseType("test").getResources().get(0);
        Resource element = myResourceManager.createResourceImpl("joe");
        group.includeImpl(element);
        assertTrue(group.contains(element));

        RemoveFromResource transaction = new RemoveFromResource(group, element);        
        transaction.execute(myResourceManager, now);
        assertFalse(group.contains(element));
    }
}
