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

public class CommandsTest extends TestCase {

    private CaseTypeManager caseTypeManager;

    public CommandsTest(String name) {
	super(name);
    }

    protected void setUp() {
	System.out.println("Setting up a command test.");
    
        caseTypeManager = new CaseTypeManager();
        caseTypeManager.registerCaseTypeImpl(
            CaseTypeTest.createTestCaseType("theTestCaseType"));
        CaseType caseType = caseTypeManager.getCaseType("theTestCaseType");
        caseType.newCase(new int[] {1,0,0,0,0,0,0,0});
    }

    public void testRegisterCaseType() {
        CaseType caseType = CaseTypeTest.createTestCaseType("anotherTestCaseType");
        RegisterCaseType command = new RegisterCaseType(caseType);
        
        command.execute(caseTypeManager);
        
        CaseType stored = caseTypeManager.getCaseType("anotherTestCaseType");
        assertNotNull(stored);
        assertSame(caseType, stored);
    }

    public void testRemoveCaseType() {
        RemoveCaseType command = new RemoveCaseType("theTestCaseType");
        
        command.execute(caseTypeManager);
        
        assertNull(caseTypeManager.getCaseType("theTestCaseType"));
    }

    public void testOpenWorkItem() {
        Case caze = caseTypeManager.getCaseType("theTestCaseType").getCase(1);
        WorkItem wi = caze.getWorkItems()[0];
        OpenWorkItem command = new OpenWorkItem(wi);

        Activity act = (Activity) command.execute(caseTypeManager);
        assertNotNull(act);
        assertEquals(wi.getTransition().getId(), act.getTransition().getId());

        int[] expected = new int[] {0,0,0,0,0,0,0,0};
        int[] actual = caze.getMarking();
        assertTrue(CaseTest.sameState(expected, actual));
    }
}
