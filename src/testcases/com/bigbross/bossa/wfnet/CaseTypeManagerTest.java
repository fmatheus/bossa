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

import junit.framework.TestCase;

public class CaseTypeManagerTest extends TestCase {

    public CaseTypeManagerTest(String name) {
	super(name);
    }

    protected void setUp() {
    	System.out.println("Setting up a case type manager test.");
    }

    public void testRegisterCaseType() {
        CaseTypeManager caseTypeManager = new CaseTypeManager();
        
        assertTrue(caseTypeManager.registerCaseType(
                        CaseTypeTest.createTestCaseType("test1")));
        assertFalse(caseTypeManager.registerCaseType(
                        CaseTypeTest.createTestCaseType("test1")));
    }
    
    public void testQueryCaseType() {
        CaseTypeManager caseTypeManager = new CaseTypeManager();
        CaseType expected = CaseTypeTest.createTestCaseType("test1");
        
        assertTrue(caseTypeManager.registerCaseType(expected));
        assertSame(expected, caseTypeManager.getCaseType("test1"));
    }
    
    public void testRemoveCaseType() {
        CaseTypeManager caseTypeManager = new CaseTypeManager();
        CaseType expected = CaseTypeTest.createTestCaseType("test1");
        
        assertTrue(caseTypeManager.registerCaseType(expected));
        caseTypeManager.removeCaseType("test1");
        assertNull(caseTypeManager.getCaseType("test1"));
    }
}
