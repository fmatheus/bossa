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
import java.util.StringTokenizer;

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
  
        System.out.println("Starting system...");
        CaseTypeManager caseTypeManager = CaseTypeManager.getInstance();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("ok.");
        System.out.println();
 
        System.out.println("## WFNet Browser ##");
        System.out.println("Enter command (q to quit, h or help):");
        System.out.print("> ");
 
        String line;
        StringTokenizer tokenizer;
        String operation;
        while (!"q".equals(line = in.readLine())) {
            tokenizer = new StringTokenizer(line);
            if (tokenizer.hasMoreTokens()) {
                operation = tokenizer.nextToken();
            } else {
                operation = "";
            }
            if (operation.equals("h")) {
                System.out.println("h\t\t\tThis help message.");
                System.out.println("l\t\t\tList case types.");
                System.out.println("g <id>\tRegister the test case type.");
                System.out.println("r <id>\tRemove a case type.");
                System.out.println("c <id>\tList cases of a case type.");
                System.out.println("w <id>\tList work itens of a case type.");
                System.out.println("a <id>\tList activities of a case type.");
                System.out.println("s\t\t\tTakes a snapshot.");
                System.out.println("q\t\t\tQuits the browser.");
            } else if (operation.equals("l")) {
                Iterator i = caseTypeManager.getCaseTypes();
                int count = 1;
                while (i.hasNext()) {
                    System.out.println(" " + count++ + " " +
                                       ((CaseType) i.next()).getId());
                }    
            } else if (operation.equals("g")) {
                String id = tokenizer.nextToken();
                CaseType caseType = CaseTypeTest.createTestCaseType(id);
                caseTypeManager.registerCaseType(caseType);
                System.out.println("ok.");
            } else if (operation.equals("r")) {
                String id = tokenizer.nextToken();
                caseTypeManager.removeCaseType(id);
                System.out.println("ok.");
            } else if (operation.equals("c")) {
                String id = tokenizer.nextToken();
                Iterator i = caseTypeManager.getCaseType(id).getCases();
                int count = 1;
                while (i.hasNext()) {
                    Case caze = (Case) i.next();
                    System.out.println(" " + count++ + " " + caze.getId() +
                                       " " + caze);
                }    
            } else if (operation.equals("w")) {
                String id = tokenizer.nextToken();

            } else if (operation.equals("a")) {
                String id = tokenizer.nextToken();

            } else if (operation.equals("s")) {
                caseTypeManager.takeSnapshot();
                System.out.println("ok.");
            } else if (operation.equals("")) {
            } else {
                System.out.println("Invalid command.");
            }
            System.out.print("> ");
        }
    }
}
