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

package com.bigbross.bossa;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.resource.ResourceManager;
import com.bigbross.bossa.wfnet.Activity;
import com.bigbross.bossa.wfnet.Case;
import com.bigbross.bossa.wfnet.CaseType;
import com.bigbross.bossa.wfnet.CaseTypeManager;
import com.bigbross.bossa.wfnet.CaseTypeTest;
import com.bigbross.bossa.wfnet.WorkItem;

public class BossaBrowser {

    /**
     * This method implements a crude command line browser to the
     * API of the bossa engine. <p>
     */
    public static void main(String[] args) throws Exception {
  
        Bossa bossa = Bossa.createBossa("build/BossaState");
        CaseTypeManager caseTypeManager = bossa.getCaseTypeManager();
        ResourceManager resourceManager = bossa.getResourceManager();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        HashMap cases = new HashMap();
        List lastWorkItemList = null;
        List lastActivitiesList = null;
        List lastResourceList = null;

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
                System.out.println("h\t\t\t\tThis help message.");
                System.out.println("s\t\t\t\tTakes a snapshot.");
                System.out.println("q\t\t\t\tQuits the browser.");
                System.out.println("-------------------------------------");
                System.out.println("l\t\t\t\tList case types.");
                System.out.println("g <id>\t\tRegister the test case type.");
                System.out.println("r <id>\t\tRemove a case type.");
                System.out.println("c <id>\t\tList cases of a case type.");
                System.out.println("w <id>\t\tList work itens of a case type.");
                System.out.println("a <id>\t\tList activities of a case type.");
                System.out.println("-------------------------------------");
                System.out.println("lr\t\t\t\tList all resources.");
                System.out.println("gr <id>\t\tRegister a resource.");
                System.out.println("rr <listId>\t\tRemove a resource.");
                System.out.println("dr <listId>\t\tDetail a resource.");
                System.out.println("ir <listId> <listId> Include a resource.");
                System.out.println("er <listId> <listId> Exclude a resource.");
                System.out.println("cr <listId> <listId> Cancel include or exclude.");
                System.out.println("ct <listId> <listId> Contains resource?");
                System.out.println("-------------------------------------");
                System.out.println("o <listId>\t\tOpen a work item.");
                System.out.println("cl <listId>\tClose an activity.");
                System.out.println("ca <listId>\tCancel an activity.");
                System.out.println("f <listId>\t\tFire a work item.");
                System.out.println("vs <caseType> <case> <id> <int>\tDeclare a case attribute.");
                System.out.println("vl <caseType> <case>\tList case attributes.");
            } else if (operation.equals("s")) {
                bossa.takeSnapshot();
                System.out.println("ok.");
            } else if (operation.equals("l")) {
                System.out.println(" ctID");
                System.out.println("-------------------------------------");
                List l = caseTypeManager.getCaseTypes();
                for (int i = 0; i < l.size(); i++) {
                    System.out.println(" " + ((CaseType) l.get(i)).getId());
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
                System.out.println(" cID\tmarking");
                System.out.println("-------------------------------------");
                String id = tokenizer.nextToken();
                List l = caseTypeManager.getCaseType(id).getCases();
                for (int i = 0; i < l.size(); i++) {
                    Case caze = (Case) l.get(i);
                    System.out.println(" " + caze.getId() + "\t" + caze);
                }    
            } else if (operation.equals("w")) {
                System.out.println("\tctID\tcID\twiID");
                System.out.println("-------------------------------------");
                String id = tokenizer.nextToken();
                lastWorkItemList = 
                    caseTypeManager.getCaseType(id).getWorkItems(true);
                for (int i = 0; i < lastWorkItemList.size(); i++) {
                    WorkItem wi = (WorkItem) lastWorkItemList.get(i);
                    System.out.println(i + ":\t" + id + "\t\t" +
                                       wi.getCase().getId() +
                                       "\t\t" + wi.getId());
                }    
            } else if (operation.equals("a")) {
                System.out.println("\tctID\tcID\taID\ttransition");
                System.out.println("-------------------------------------");
                String id = tokenizer.nextToken();
                lastActivitiesList =
                    caseTypeManager.getCaseType(id).getActivities();
                for (int i = 0; i < lastActivitiesList.size(); i++) {
                    Activity a = (Activity) lastActivitiesList.get(i);
                    System.out.println(i + ":\t" + id + "\t\t" + 
                                       a.getCase().getId() + "\t\t" +
                                       a.getId() + "\t\t" +
                                       a.getTransition().getId());
                }
            } else if (operation.equals("lr")) {
                System.out.println("\trID\tgroup?");
                System.out.println("-------------------------------------");
                lastResourceList = resourceManager.getResources();
                for (int i = 0; i < lastResourceList.size(); i++) {
                    Resource r = (Resource) lastResourceList.get(i);
                    System.out.println(i + ":\t" + r.getId() + "\t" +
                                       (r.isGroup() ? "yes" : "no"));
                }    
            } else if (operation.equals("gr")) {
                String id = tokenizer.nextToken();
                resourceManager.createResource(id);
                System.out.println("ok.");
            } else if (operation.equals("rr")) {
                int listId = Integer.parseInt(tokenizer.nextToken());
                Resource r = (Resource) lastResourceList.get(listId);
                resourceManager.removeResource(r);
                System.out.println("ok.");
            } else if (operation.equals("dr")) {
                int listId = Integer.parseInt(tokenizer.nextToken());
                Resource r = (Resource) lastResourceList.get(listId);
                System.out.println("Includes:");
                Iterator i = r.getIncludes().iterator();
                while (i.hasNext()) {
                     System.out.println("  " + ((Resource) i.next()).getId());
                }
                System.out.println("Excludes:");
                i = r.getExcludes().iterator();
                while (i.hasNext()) {
                     System.out.println("  " + ((Resource) i.next()).getId());
                }
            } else if (operation.equals("ir")) {
                int hostId = Integer.parseInt(tokenizer.nextToken());
                int resId = Integer.parseInt(tokenizer.nextToken());
                Resource host = (Resource) lastResourceList.get(hostId);
                Resource r = (Resource) lastResourceList.get(resId);
                System.out.println(host.include(r) ? "ok." : "NOT ok.");
            } else if (operation.equals("er")) {
                int hostId = Integer.parseInt(tokenizer.nextToken());
                int resId = Integer.parseInt(tokenizer.nextToken());
                Resource host = (Resource) lastResourceList.get(hostId);
                Resource r = (Resource) lastResourceList.get(resId);
                System.out.println(host.exclude(r) ? "ok." : "NOT ok.");
            } else if (operation.equals("cr")) {
                int hostId = Integer.parseInt(tokenizer.nextToken());
                int resId = Integer.parseInt(tokenizer.nextToken());
                Resource host = (Resource) lastResourceList.get(hostId);
                Resource r = (Resource) lastResourceList.get(resId);
                host.remove(r);
                System.out.println("ok.");
            } else if (operation.equals("ct")) {
                int hostId = Integer.parseInt(tokenizer.nextToken());
                int resId = Integer.parseInt(tokenizer.nextToken());
                Resource host = (Resource) lastResourceList.get(hostId);
                Resource r = (Resource) lastResourceList.get(resId);
                System.out.println(host.contains(r) ?
                                   "contains." : "NOT contains.");
            } else if (operation.equals("o")) {
                int listId = Integer.parseInt(tokenizer.nextToken());
                WorkItem wi = (WorkItem) lastWorkItemList.get(listId);
                Activity a = wi.open("jdoe");
                System.out.println("ok. Activity: " + a.getId());
            } else if (operation.equals("cl")) {
                int listId = Integer.parseInt(tokenizer.nextToken());
                Activity a = (Activity) lastActivitiesList.get(listId);
                HashMap attributes =
                    (HashMap) cases.get(a.getCaseType().getId() + 
                                        a.getCase().getId());
                boolean result = a.close(attributes);
                System.out.println("ok. Success=" + result);
            } else if (operation.equals("ca")) {
                int listId = Integer.parseInt(tokenizer.nextToken());
                Activity a = (Activity) lastActivitiesList.get(listId);
                boolean result = a.cancel();
                System.out.println("ok. Success=" + result);
            } else if (operation.equals("f")) {
                int listId = Integer.parseInt(tokenizer.nextToken());
                WorkItem wi = (WorkItem) lastWorkItemList.get(listId);
                HashMap attributes =
                    (HashMap) cases.get(wi.getCaseType().getId() +
                                        wi.getCase().getId());
                Activity a = wi.open("jdoe");
                boolean result = a.close(attributes);
                System.out.println("ok. Success=" + result);
            } else if (operation.equals("vs")) {
                String caseTypeId = tokenizer.nextToken();
                String caseId = tokenizer.nextToken();
                String id = tokenizer.nextToken();
                Integer value = new Integer(tokenizer.nextToken());
                HashMap attributes = (HashMap) cases.get(caseTypeId + caseId);
                if (attributes == null) {
                    attributes = new HashMap();
                    cases.put(caseTypeId + caseId, attributes);
                }
                attributes.put(id, value);
                System.out.println("ok.");
            } else if (operation.equals("vl")) {
                String caseTypeId = tokenizer.nextToken();
                String caseId = tokenizer.nextToken();
                HashMap attributes = (HashMap) cases.get(caseTypeId + caseId);
                if (attributes != null) {
                    Iterator it = attributes.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry attribute = (Map.Entry) it.next();
                        System.out.println(attribute.getKey().toString() + 
                                           " == " + attribute.getValue());
                    }
                }
            } else if (operation.equals("")) {
            } else {
                System.out.println("Invalid command.");
            }
            System.out.print("> ");
        }
    }
}
