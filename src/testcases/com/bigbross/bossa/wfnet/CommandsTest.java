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

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaException;
import com.bigbross.bossa.BossaTestSuite;
import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.resource.ResourceUtil;
import junit.framework.TestCase;

public class CommandsTest extends TestCase {

    private CaseTypeManager caseTypeManager;

    private Resource jdoe;

    public CommandsTest(String name) {
	super(name);
    }

    protected void setUp() {
	System.out.println("Setting up a wfnet command test.");
    
        Bossa bossa = BossaTestSuite.createTestBossa();
        caseTypeManager = bossa.getCaseTypeManager();

        jdoe = ResourceUtil.createResource(bossa.getResourceManager(), "jdoe");

        caseTypeManager.registerCaseTypeImpl(
            WFNetUtil.createCaseType("theTestCaseType"));
        CaseType caseType = caseTypeManager.getCaseType("theTestCaseType");
        try {
            caseType.openCase(new int[] {1,0,0,0,0,0,0,0});
        } catch (EvaluationException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    public void testRegisterCaseType() {
        CaseType caseType = WFNetUtil.createCaseType("anotherTestCaseType");
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
        WorkItem wi = (WorkItem) caze.getWorkItems().get(0);
        OpenWorkItem command = new OpenWorkItem(wi, jdoe);

        try {
            Activity act = (Activity) command.execute(caseTypeManager);
            assertNotNull(act);
            assertEquals(wi.getTransition().getId(), act.getTransition().getId());
        } catch (EvaluationException e) {
            e.printStackTrace();
            fail(e.toString());
        }

        int[] expected = new int[] {0,0,0,0,0,0,0,0};
        int[] actual = caze.getMarking();
        assertTrue(CaseTest.sameState(expected, actual));
    }

    public void testCloseActivity() {
        Case caze = caseTypeManager.getCaseType("theTestCaseType").getCase(1);
        WorkItem wi = (WorkItem) caze.getWorkItems().get(0);
        try {
            Activity activity = caze.open(wi, jdoe);
            CloseActivity command = new CloseActivity(activity, null);
        
            assertEquals(1, caze.getActivities().size());
            command.execute(caseTypeManager);        
        } catch (BossaException e) {
            e.printStackTrace();
            fail(e.toString());
        }
        assertEquals(0, caze.getActivities().size());

        int[] expected = new int[] {0,1,0,0,0,0,0,0};
        int[] actual = caze.getMarking();
        assertTrue(CaseTest.sameState(expected, actual));
    }

    public void testCancelActivity() {
        Case caze = caseTypeManager.getCaseType("theTestCaseType").getCase(1);
        WorkItem wi = (WorkItem) caze.getWorkItems().get(0);
        try {
            Activity activity = caze.open(wi, jdoe);
            CancelActivity command = new CancelActivity(activity);
        
            assertEquals(1, caze.getActivities().size());
            command.execute(caseTypeManager);        
            assertEquals(0, caze.getActivities().size());
        } catch (EvaluationException e) {
            e.printStackTrace();
            fail(e.toString());
        }

        int[] expected = new int[] {1,0,0,0,0,0,0,0};
        int[] actual = caze.getMarking();
        assertTrue(CaseTest.sameState(expected, actual));
    }
}
