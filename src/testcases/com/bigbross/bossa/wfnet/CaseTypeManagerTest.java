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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;

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
        
        assertTrue(caseTypeManager.registerCaseTypeImpl(
                        CaseTypeTest.createTestCaseType("test1")));
        assertFalse(caseTypeManager.registerCaseTypeImpl(
                        CaseTypeTest.createTestCaseType("test1")));
    }
    
    public void testQueryCaseType() {
        CaseTypeManager caseTypeManager = new CaseTypeManager();
        CaseType expected = CaseTypeTest.createTestCaseType("test1");
        
        assertTrue(caseTypeManager.registerCaseTypeImpl(expected));
        assertSame(expected, caseTypeManager.getCaseType("test1"));
    }

    public void testQueryAllCaseTypes() {
        CaseTypeManager caseTypeManager = new CaseTypeManager();
        CaseType ct1 = CaseTypeTest.createTestCaseType("test1");
        CaseType ct2 = CaseTypeTest.createTestCaseType("test2");

        assertTrue(caseTypeManager.registerCaseTypeImpl(ct1));
        assertTrue(caseTypeManager.registerCaseTypeImpl(ct2));
        
        Iterator i = caseTypeManager.getCaseTypes();
        String id = ((CaseType) i.next()).getId();
        assertTrue(id.equals("test1") || id.equals("test2"));
        id = ((CaseType) i.next()).getId();
        assertTrue(id.equals("test1") || id.equals("test2"));
        assertFalse(i.hasNext());
    }
    
    public void testRemoveCaseType() {
        CaseTypeManager caseTypeManager = new CaseTypeManager();
        CaseType expected = CaseTypeTest.createTestCaseType("test1");
        
        assertTrue(caseTypeManager.registerCaseTypeImpl(expected));
        caseTypeManager.removeCaseTypeImpl("test1");
        assertNull(caseTypeManager.getCaseType("test1"));
    }
    
    public static void main(String[] args) throws Exception {
  
        System.out.print("Starting system...");
        CaseTypeManager caseTypeManager = CaseTypeManager.getInstance();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("ok");
        System.out.println();
 
        System.out.println("## WFNet Browser ##");
        System.out.println("Enter command (q to quit, h or help):");
        System.out.print("> ");
 
        String line;
        while (!"q".equals(line = in.readLine())) {
            if (line.equals("h")) {
                System.out.println("h\t\t\tThis help message.");
                System.out.println("l\t\t\tList case types.");
                System.out.println("g <id>\tRegister the test case type.");
                System.out.println("r <id>\tRemove a case type.");
                System.out.println("q\t\t\tQuits the browser.");
            } else if (line.equals("l")) {
                //caseTypeManager.
            } else if (line.equals("g")) {
                
            } else if (line.equals("r")) {
                
            } else if (line.equals("") || line.equals("q")) {
            } else {
                System.out.println("Invalid command.");
            }
            System.out.print("> ");
        }
    }
}
