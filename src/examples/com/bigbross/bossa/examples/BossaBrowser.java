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

package com.bigbross.bossa.examples;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaFactory;
import com.bigbross.bossa.BossaTestUtil;
import com.bigbross.bossa.history.Historian;
import com.bigbross.bossa.notify.Event;
import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.resource.ResourceManager;
import com.bigbross.bossa.wfnet.Activity;
import com.bigbross.bossa.wfnet.Case;
import com.bigbross.bossa.wfnet.CaseType;
import com.bigbross.bossa.wfnet.CaseTypeManager;
import com.bigbross.bossa.wfnet.WorkItem;
import com.bigbross.bossa.work.WorkManager;

/**
 * This class implements a crude command line browser to the
 * API of the bossa engine. <p>
 */
public class BossaBrowser {

    Bossa bossa;
    CaseTypeManager caseTypeManager;
    ResourceManager resourceManager;
    WorkManager workManager;
    Historian historian;
    HashMap cases;
    List lastCaseList;
    List lastWorkItemList;
    List lastActivitiesList;
    List lastResourceList;
    List lastCaseTypeResourceList;

    public BossaBrowser() throws Exception {
        BossaFactory factory = new BossaFactory();
        factory.setStateDir("build/BossaBrowserState");
        bossa = factory.createBossa();
        caseTypeManager = bossa.getCaseTypeManager();
        resourceManager = bossa.getResourceManager();
        workManager = bossa.getWorkManager();
        historian = bossa.getHistorian();

        cases = new HashMap();
        lastCaseList = null;
        lastWorkItemList = null;
        lastActivitiesList = null;
        lastResourceList = null;
        lastCaseTypeResourceList = null;
    }

    private Resource retrieveResource(int listId) {
        Resource r;
        if (listId < 100) {
            r = (Resource) lastResourceList.get(listId);
        } else {
            r = (Resource) lastCaseTypeResourceList.get(listId - 100);
        }
        return r;
    }
    
    private void printHistory(List l) {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, 
                                                       DateFormat.SHORT);
        System.out.println(" Date     Time  T Id {Attributes}");
        System.out.println("-------------------------------------");
        for (int i = 0; i < l.size(); i++) {
            Event e = (Event) l.get(i);
            System.out.println(" " + df.format(e.getTime()) + " " + 
                               e.getType() + " " + 
                               e.getId() + " " + 
                               e.getAttributes());
        }
    }

    public void run() throws Exception {

        BufferedReader in =
            new BufferedReader(new InputStreamReader(System.in));

        System.out.println();
        System.out.println("## WFNet Browser ##");
        System.out.println("Enter command (q to quit, ? for help):");
        System.out.print("> ");
        System.out.flush();

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
            if (operation.equals("?")) {
              String options =
               "?                           This help message.\n" +
               "s                           Takes a snapshot.\n" +
               "q                           Quits the browser.\n" +
               "-----------------------------------------------------------\n" +
               "l                           List case types.\n" +
               "g <id>                      Register the test case type.\n" +
               "r <id>                      Remove a case type.\n" +
               "c <id>                      List cases of a case type.\n" +
               "w <id>                      List work itens of a case type.\n" +
               "a <id>                      List activities of a case type.\n" +
               "t <id>                      List resources of a case type.\n" +
               "y <listId>                  List resources of a case.\n" +
               "-----------------------------------------------------------\n" +
               "lr                          List all resources.\n" +
               "gr <id>                     Register a resource.\n" +
               "rr <listId>                 Remove a resource.\n" +
               "dr <listId>                 Detail a resource.\n" +
               "ir <listId> <listId>        Include a resource.\n" +
               "er <listId> <listId>        Exclude a resource.\n" +
               "cr <listId> <listId>        Cancel include or exclude.\n" +
               "ct <listId> <listId>        Contains resource?\n" +
               "-----------------------------------------------------------\n" +
               "wr <listId>                 List work itens of a resource.\n" +
               "ar <listId>                 List activities of a resource.\n" +
               "-----------------------------------------------------------\n" +
               "o  <listId> <resListId>          Open a work item.\n" +
               "cl <listId>                      Close an activity.\n" +
               "ca <listId>                      Cancel an activity.\n" +
               "f  <listId> <resListId>          Fire a work item.\n" +
               "vs <caseType> <case> <id> <int>  Declare a case attribute.\n" +
               "vl <caseType> <case>             List case attributes.\n" +
               "-----------------------------------------------------------\n" +
               "h                           Display all history.\n" +
               "ht <id>                     Display a case type history.\n" +
               "hc <listId>                 Display a case history.\n" +
               "hr <listId>                 Display a resource history.\n" +
               "hx                          Export and purge all history.\n";
              System.out.print(options);
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
                CaseType caseType = BossaTestUtil.createCaseType(id);
                caseTypeManager.registerCaseType(caseType);
                System.out.println("ok.");
            } else if (operation.equals("r")) {
                String id = tokenizer.nextToken();
                caseTypeManager.removeCaseType(id);
                System.out.println("ok.");
            } else if (operation.equals("c")) {
                System.out.println("\tcID\tmarking");
                System.out.println("-------------------------------------");
                String id = tokenizer.nextToken();
                lastCaseList = caseTypeManager.getCaseType(id).getCases();
                for (int i = 0; i < lastCaseList.size(); i++) {
                    Case caze = (Case) lastCaseList.get(i);
                    System.out.println(i + ":\t" + caze.getId() + "\t" + caze);
                }
            } else if (operation.equals("w")) {
                System.out.println("\tctID\tcID\twiID");
                System.out.println("-------------------------------------");
                String id = tokenizer.nextToken();
                lastWorkItemList =
                    caseTypeManager.getCaseType(id).getWorkItems(true);
                for (int i = 0; i < lastWorkItemList.size(); i++) {
                    WorkItem wi = (WorkItem) lastWorkItemList.get(i);
                    System.out.println(
                        i
                            + ":\t"
                            + id
                            + "\t\t"
                            + wi.getCase().getId()
                            + "\t\t"
                            + wi.getId());
                }
            } else if (operation.equals("a")) {
                System.out.println("\tctID\tcID\taID\ttransition");
                System.out.println("-------------------------------------");
                String id = tokenizer.nextToken();
                lastActivitiesList =
                    caseTypeManager.getCaseType(id).getActivities();
                for (int i = 0; i < lastActivitiesList.size(); i++) {
                    Activity a = (Activity) lastActivitiesList.get(i);
                    System.out.println(
                        i
                            + ":\t"
                            + id
                            + "\t\t"
                            + a.getCase().getId()
                            + "\t\t"
                            + a.getId()
                            + "\t\t"
                            + a.getWorkItemId());
                }
            } else if (operation.equals("t")) {
                System.out.println("\trID\tgroup?");
                System.out.println("-------------------------------------");
                String id = tokenizer.nextToken();
                lastCaseTypeResourceList =
                    caseTypeManager.getCaseType(id).getResources();
                for (int i = 0; i < lastCaseTypeResourceList.size(); i++) {
                    Resource r = (Resource) lastCaseTypeResourceList.get(i);
                    System.out.println(
                        i
                            + 100
                            + ":\t"
                            + r.getId()
                            + "\t"
                            + (r.isGroup() ? "yes" : "no"));
                }
            } else if (operation.equals("y")) {
                System.out.println("\trID\tgroup?");
                System.out.println("-------------------------------------");
                int listId = Integer.parseInt(tokenizer.nextToken());
                lastCaseTypeResourceList =
                    ((Case) lastCaseList.get(listId)).getResources();
                for (int i = 0; i < lastCaseTypeResourceList.size(); i++) {
                    Resource r = (Resource) lastCaseTypeResourceList.get(i);
                    System.out.println(
                        i
                            + 100
                            + ":\t"
                            + r.getId()
                            + "\t"
                            + (r.isGroup() ? "yes" : "no"));
                }
            } else if (operation.equals("lr")) {
                System.out.println("\trID\tgroup?");
                System.out.println("-------------------------------------");
                lastResourceList = resourceManager.getResources();
                for (int i = 0; i < lastResourceList.size(); i++) {
                    Resource r = (Resource) lastResourceList.get(i);
                    System.out.println(
                        i
                            + ":\t"
                            + r.getId()
                            + "\t"
                            + (r.isGroup() ? "yes" : "no"));
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
                Resource r = retrieveResource(listId);
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
                Resource host = retrieveResource(hostId);
                Resource r = retrieveResource(resId);
                System.out.println(host.include(r) ? "ok." : "NOT ok.");
            } else if (operation.equals("er")) {
                int hostId = Integer.parseInt(tokenizer.nextToken());
                int resId = Integer.parseInt(tokenizer.nextToken());
                Resource host = retrieveResource(hostId);
                Resource r = retrieveResource(resId);
                System.out.println(host.exclude(r) ? "ok." : "NOT ok.");
            } else if (operation.equals("cr")) {
                int hostId = Integer.parseInt(tokenizer.nextToken());
                int resId = Integer.parseInt(tokenizer.nextToken());
                Resource host = retrieveResource(hostId);
                Resource r = retrieveResource(resId);
                host.remove(r);
                System.out.println("ok.");
            } else if (operation.equals("ct")) {
                int hostId = Integer.parseInt(tokenizer.nextToken());
                int resId = Integer.parseInt(tokenizer.nextToken());
                Resource host = retrieveResource(hostId);
                Resource r = retrieveResource(resId);
                System.out.println(
                    host.contains(r) ? "contains." : "NOT contains.");
            } else if (operation.equals("wr")) {
                System.out.println("\tctID\tcID\twiID");
                System.out.println("-------------------------------------");
                int resId = Integer.parseInt(tokenizer.nextToken());
                Resource resource = (Resource) lastResourceList.get(resId);
                lastWorkItemList = workManager.getWorkItems(resource, true);
                for (int i = 0; i < lastWorkItemList.size(); i++) {
                    WorkItem wi = (WorkItem) lastWorkItemList.get(i);
                    System.out.println(
                        i
                            + ":\t"
                            + wi.getCaseType().getId()
                            + "\t\t"
                            + wi.getCase().getId()
                            + "\t\t"
                            + wi.getId());
                }
            } else if (operation.equals("ar")) {
                System.out.println("\tctID\tcID\taID\ttransition");
                System.out.println("-------------------------------------");
                int resId = Integer.parseInt(tokenizer.nextToken());
                Resource resource = (Resource) lastResourceList.get(resId);
                lastActivitiesList = workManager.getActivities(resource);
                for (int i = 0; i < lastActivitiesList.size(); i++) {
                    Activity a = (Activity) lastActivitiesList.get(i);
                    System.out.println(
                        i
                            + ":\t"
                            + a.getCaseType().getId()
                            + "\t\t"
                            + a.getCase().getId()
                            + "\t\t"
                            + a.getId()
                            + "\t\t"
                            + a.getWorkItemId());
                }
            } else if (operation.equals("o")) {
                int listId = Integer.parseInt(tokenizer.nextToken());
                int resId = Integer.parseInt(tokenizer.nextToken());
                WorkItem wi = (WorkItem) lastWorkItemList.get(listId);
                Resource res = (Resource) lastResourceList.get(resId);
                Activity a = wi.open(res);
                System.out.println("ok. Activity: " + a.getId());
            } else if (operation.equals("cl")) {
                int listId = Integer.parseInt(tokenizer.nextToken());
                Activity a = (Activity) lastActivitiesList.get(listId);
                HashMap attributes =
                    (HashMap) cases.get(
                        a.getCaseType().getId() + a.getCase().getId());
                boolean result = a.close(attributes);
                System.out.println("ok. Success=" + result);
            } else if (operation.equals("ca")) {
                int listId = Integer.parseInt(tokenizer.nextToken());
                Activity a = (Activity) lastActivitiesList.get(listId);
                boolean result = a.cancel();
                System.out.println("ok. Success=" + result);
            } else if (operation.equals("f")) {
                int listId = Integer.parseInt(tokenizer.nextToken());
                int resId = Integer.parseInt(tokenizer.nextToken());
                WorkItem wi = (WorkItem) lastWorkItemList.get(listId);
                Resource res = (Resource) lastResourceList.get(resId);
                HashMap attributes =
                    (HashMap) cases.get(
                        wi.getCaseType().getId() + wi.getCase().getId());
                Activity a = wi.open(res);
                boolean result = a.close(attributes);
                System.out.println("ok. Success=" + result);
            } else if (operation.equals("vs")) {
                String caseTypeId = tokenizer.nextToken();
                String caseId = tokenizer.nextToken();
                String id = tokenizer.nextToken();
                String valueStr = tokenizer.nextToken();
                Object value;
                try {
                    value = new Integer(valueStr);
                } catch (NumberFormatException e) {
                    value = valueStr;
                }
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
                        System.out.println(
                            attribute.getKey().toString()
                                + " == "
                                + attribute.getValue());
                    }
                }
            } else if (operation.equals("h")) {
                List l = historian.getHistory();
                printHistory(l);
            } else if (operation.equals("ht")) {
                String id = tokenizer.nextToken();
                List l = historian.getCaseTypeHistory(id);
                printHistory(l);
            } else if (operation.equals("hc")) {
                int listId = Integer.parseInt(tokenizer.nextToken());
                Case caze = (Case) lastCaseList.get(listId);
                List l = historian.getCaseHistory(caze.getCaseType().getId(),
                                                  caze.getId());
                printHistory(l);
            } else if (operation.equals("hr")) {
                int listId = Integer.parseInt(tokenizer.nextToken());
                Resource resource = retrieveResource(listId);
                List l = historian.getResourceHistory(resource.getId());
                printHistory(l);
            } else if (operation.equals("hx")) {
                Date now = new Date();
                historian.exportHistory(now, "history.xml");
                historian.purgeHistory(now);
                System.out.println("History written to history.xml.");
            } else if (operation.equals("")) {
            } else {
                System.out.println("Invalid command.");
            }
            System.out.print("> ");
            System.out.flush();
        }
    }

    public static void main(String[] args) throws Exception {
        new BossaBrowser().run();
    }
}
