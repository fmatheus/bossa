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

package com.bigbross.bossa.wfnet;

import java.util.List;

import com.bigbross.bossa.BossaException;
import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.resource.ResourceUtil;
import junit.framework.TestCase;

public class CaseTypeManagerTest extends TestCase {

    static final Resource joe = ResourceUtil.createResource("joe");

    CaseTypeManager caseTypeManager;

    public CaseTypeManagerTest(String name) {
	super(name);
    }

    protected void setUp() {
    	System.out.println("Setting up a case type manager test.");
        caseTypeManager = new CaseTypeManager();
    }

    public void testRegisterCaseType() {
        assertTrue(caseTypeManager.registerCaseTypeImpl(
                        WFNetUtil.createCaseType("test1")));
        assertFalse(caseTypeManager.registerCaseTypeImpl(
                        WFNetUtil.createCaseType("test1")));
    }
    
    public void testQueryCaseType() {
        CaseType expected = WFNetUtil.createCaseType("test1");
        
        assertTrue(caseTypeManager.registerCaseTypeImpl(expected));
        assertSame(expected, caseTypeManager.getCaseType("test1"));
    }

    public void testQueryAllCaseTypes() {
        CaseType ct1 = WFNetUtil.createCaseType("test1");
        CaseType ct2 = WFNetUtil.createCaseType("test2");

        assertTrue(caseTypeManager.registerCaseTypeImpl(ct1));
        assertTrue(caseTypeManager.registerCaseTypeImpl(ct2));
        
        List l = caseTypeManager.getCaseTypes();
        assertEquals(2, l.size());
        String id = ((CaseType) l.get(0)).getId();
        assertTrue(id.equals("test1") || id.equals("test2"));
        id = ((CaseType) l.get(1)).getId();
        assertTrue(id.equals("test1") || id.equals("test2"));
    }
    
    public void testRemoveCaseType() {
        CaseType expected = WFNetUtil.createCaseType("test1");
        
        assertTrue(caseTypeManager.registerCaseTypeImpl(expected));
        caseTypeManager.removeCaseTypeImpl("test1");
        assertNull(caseTypeManager.getCaseType("test1"));
    }
    
    public void testGetWorkItems() {
        CaseType ct1 = WFNetUtil.createCaseType("test1");
        CaseType ct2 = WFNetUtil.createCaseType("test2");
        assertTrue(caseTypeManager.registerCaseTypeImpl(ct1));
        assertTrue(caseTypeManager.registerCaseTypeImpl(ct2));
        
        assertEquals(0, caseTypeManager.getWorkItems().size());
        assertEquals(2, caseTypeManager.getWorkItems(true).size());
    }

    public void testGetActivities() {
        CaseType ct1 = WFNetUtil.createCaseType("test1");
        CaseType ct2 = WFNetUtil.createCaseType("test2");
        assertTrue(caseTypeManager.registerCaseTypeImpl(ct1));
        assertTrue(caseTypeManager.registerCaseTypeImpl(ct2));
        WorkItem wi = (WorkItem) caseTypeManager.getWorkItems(true).get(0);
        try {
            wi.getCase().open(wi, joe);
        } catch (EvaluationException e) {
            e.printStackTrace();
            fail(e.toString());
        }
        
        assertEquals(1, caseTypeManager.getActivities().size());
    }
}
