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

package com.bigbross.bossa.wfnet;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaException;
import com.bigbross.bossa.BossaFactory;
import com.bigbross.bossa.BossaTestUtil;
import com.bigbross.bossa.resource.Resource;

public class TransactionsTest extends TestCase {

    private CaseTypeManager caseTypeManager;
    private Resource jdoe;

    public TransactionsTest(String name) {
	super(name);
    }

    protected void setUp() throws Exception {
        Bossa bossa = BossaFactory.transientBossa();
        caseTypeManager = bossa.getCaseTypeManager();

        jdoe = bossa.getResourceManager().createResource("jdoe");

        caseTypeManager.registerCaseTypeImpl(
            BossaTestUtil.createCaseType("theTestCaseType"));
        CaseType caseType = caseTypeManager.getCaseType("theTestCaseType");
        caseType.openCaseImpl(null);
    }

    public void testRegisterCaseType() throws Exception {
        CaseType caseType = BossaTestUtil.createCaseType("anotherTestCaseType");
        RegisterCaseType transaction = new RegisterCaseType(caseType);
        
        transaction.execute(caseTypeManager);
        
        CaseType stored = caseTypeManager.getCaseType("anotherTestCaseType");
        assertNotNull(stored);
        assertSame(caseType, stored);
    }

    public void testRemoveCaseType() {
        RemoveCaseType transaction = new RemoveCaseType("theTestCaseType");
        
        transaction.execute(caseTypeManager);
        
        assertNull(caseTypeManager.getCaseType("theTestCaseType"));
    }

    public void testOpenWorkItem() throws Exception {
        Case caze = caseTypeManager.getCaseType("theTestCaseType").getCase(1);
        WorkItem wi = (WorkItem) caze.getWorkItems().get(0);
        OpenWorkItem transaction = new OpenWorkItem(wi, jdoe);

        Activity act = (Activity) transaction.execute(caseTypeManager);
        assertNotNull(act);
        assertEquals(wi.getId(), act.getWorkItemId());

        Map expected = new HashMap();
        expected.put("A", new Integer(0));
        CaseTest.sameState(expected, caze.getState());
    }

    public void testCloseActivity() throws Exception {
        Case caze = caseTypeManager.getCaseType("theTestCaseType").getCase(1);
        WorkItem wi = (WorkItem) caze.getWorkItems().get(0);
        Activity activity = caze.open(wi, jdoe);
        CloseActivity transaction = new CloseActivity(activity, null);
        
        assertEquals(1, caze.getActivities().size());
        transaction.execute(caseTypeManager);
        assertEquals(0, caze.getActivities().size());

        Map expected = new HashMap();
        expected.put("B", new Integer(1));
        CaseTest.sameState(expected, caze.getState());
    }

    public void testCancelActivity() throws Exception {
        Case caze = caseTypeManager.getCaseType("theTestCaseType").getCase(1);
        WorkItem wi = (WorkItem) caze.getWorkItems().get(0);
        Activity activity = caze.open(wi, jdoe);
        CancelActivity transaction = new CancelActivity(activity);
        
        assertEquals(1, caze.getActivities().size());
        transaction.execute(caseTypeManager);
        assertEquals(0, caze.getActivities().size());

        Map expected = new HashMap();
        expected.put("A", new Integer(1));
        CaseTest.sameState(expected, caze.getState());
    }
    
    public void testOpenCase() throws BossaException {
        Map state = new HashMap();
        state.put("A", new Integer(0));
        state.put("B", new Integer(0));
        state.put("C", new Integer(1));
        state.put("D", new Integer(0));
        state.put("E", new Integer(1));
        state.put("F", new Integer(0));
        state.put("G", new Integer(2));
        state.put("H", new Integer(0));
        OpenCase transaction = new OpenCase("theTestCaseType", state);
        
        Case caze = (Case) transaction.execute(caseTypeManager);
        assertSame(caze,
                   caseTypeManager.getCaseType("theTestCaseType").getCase(2));
        CaseTest.sameState(state, caze.getState());
    }

    public void testSetState() throws BossaException {
        Case caze = caseTypeManager.getCaseType("theTestCaseType").getCase(1);
        Map newState = new HashMap();
        newState.put("E", new Integer(1));
        SetState transaction = new SetState(caze, newState);
        
        transaction.execute(caseTypeManager);
        
        Map expected = new HashMap();
        expected.put("A", new Integer(1));
        expected.put("E", new Integer(1));
        CaseTest.sameState(expected, caze.getState());
    }

    public void testCloseCase() throws Exception {
        CaseType caseType = caseTypeManager.getCaseType("theTestCaseType");
        Case caze = caseType.getCase(1);
        CloseCase transaction = new CloseCase(caze);
        
        transaction.execute(caseTypeManager);
        assertNull(caseType.getCase(1));
    }
}
