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

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a specific instance of a case type. It
 * holds the current state of a case. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Case {

    int id = 0;

    CaseType caseType;

    int[] marking;

    WorkItem[] workItems;

    List activities = new ArrayList();

    int workSequence = 0;

    Case(CaseType caseType, int[] marking) {

	this.id = caseType.nextCaseId();
	this.caseType = caseType;
	this.marking = marking;

	Transition[] ts = caseType.getTransitions();
	workItems = new WorkItem[ts.length];
	for (int i = 0; i < workItems.length; ++i) {
	    workItems[i] = new WorkItem(this, ts[i]);
	}

	deactivate();

    }

    public CaseType getCaseType() {
	return caseType;
    }

    /**
     * Returns the list of work items associated with this case. <p>
     *  
     * @return An array with the work items of this case.
     */
    public WorkItem[] getWorkItems() {
        ArrayList items = new ArrayList(workItems.length);
        
        for (int i = 0; i < workItems.length; i++) {
            if (workItems[i].isFireable()) {
                items.add(workItems[i]);
            }
        }        
        
	return (WorkItem[]) items.toArray(new WorkItem[items.size()]);
    }

    public String toString() {

	StringBuffer string = new StringBuffer();

	string.append("\t");
	for (int i = 0; i < marking.length; ++i) {
	    string.append(marking[i]);
	    string.append("\t");
	}

	return string.toString();

    }

    int nextWorkItemId() {
	return ++workSequence;
    }

    private void activate() {
	for (int i = 0; i < workItems.length; ++i) {
	    if (!workItems[i].isFireable()) {
		workItems[i].update();
	    }
	}
    }

    private void deactivate() {
	for (int i = 0; i < workItems.length; ++i) {
	    if (workItems[i].isFireable()) {
		workItems[i].update();
	    }
	}
    }

    boolean isFireable(Transition t) {
	for(int i = 0; i < marking.length; ++i) {
	    if (marking[i] < caseType.getEdge(t.index, i).input()) {
		return false;
	    }
	}
	return true;
    }

    Activity open(WorkItem wi) {

	if (!wi.isFireable()) {
	    return null;
	}

	Activity activity = new Activity(wi);
	activities.add(activity);
	Edge[] edges = activity.getTransition().getEdges();
	for(int i = 0; i < marking.length; ++i) {
	    this.marking[i] -= edges[i].input();
	}
	deactivate();

	return activity;
    }

    boolean close(Activity activity) {

	if (!activities.contains(activity)) {
	    return false;
	}

	activities.remove(activity);
	Edge[] edges = activity.getTransition().getEdges();
	for(int i = 0; i < marking.length; ++i) {
	    this.marking[i] += edges[i].output();
	}
	activate();

	return true;
    }

    boolean cancel(Activity activity) {

	if (!activities.contains(activity)) {
	    return false;
	}

	activities.remove(activity);
	Edge[] edges = activity.getTransition().getEdges();
	for(int i = 0; i < marking.length; ++i) {
	    this.marking[i] += edges[i].input();
	}
	activate();

	return true;
    }

}
