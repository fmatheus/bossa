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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a specific instance of a case type. It
 * holds the current state of a case. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Case implements Serializable {

    private int id;

    private CaseType caseType;

    private int[] marking;

    private WorkItem[] workItems;

    private List activities;

    private int workSequence;

    Case(CaseType caseType, int[] marking) {

	this.id = caseType.nextCaseId();
	this.caseType = caseType;
	this.marking = marking;
        this.activities = new ArrayList();
        this.workSequence = 0;

	Transition[] ts = caseType.getTransitions();
	workItems = new WorkItem[ts.length];
	for (int i = 0; i < workItems.length; ++i) {
	    workItems[i] = new WorkItem(this, ts[i]);
	}

	deactivate();
    }

    /**
     * Returns the id of this case.
     * 
     * @return the id of this case.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the case type of this case.
     * 
     * @return the case type of this case.
     */
    public CaseType getCaseType() {
	return caseType;
    }

    /**
     * Returns a copy of the internal state of the case, that is, how many
     * tokens are in each place.
     * 
     * @return the marking.
     */
    int[] getMarking() {
        return (int[]) marking.clone();
    }

    /**
     * Returns the list of work items associated with this case. <p>
     *  
     * @return A list with the work items of this case.
     */
    public List getWorkItems() {
        ArrayList items = new ArrayList(workItems.length);
        
        for (int i = 0; i < workItems.length; i++) {
            if (workItems[i].isFireable()) {
                items.add(workItems[i]);
            }
        }        
        
	return items;
    }

    /**
     * Returns a specific work item, selected by its id. <p>
     * 
     * FIXME: Maybe we should return null if the work item is not
     * fireable.
     * 
     * @param id the work item id.
     * @return the work item, <code>null</code> if there is no work item
     *         with this id.
     */
    WorkItem getWorkItem(String id) {
        int index = caseType.getTransition(id).getIndex();
        return workItems[index];
    }

    /**
     * Returns a list of activities associated with the provided resource.
     * If the resource id is <code>null</code>, the list contains
     * all activities of this case. <p>
     * 
     * @param resource the resource id.
     * @return The list of activities associated with the resource. 
     */

    /**
     * Return a specific activity, selected by its id. <p>
     * 
     * @param id the activity id.
     * @return the activity, <code>null</code> if there is no activity
     *         with this id.
     */


    int nextWorkItemId() {
	return ++workSequence;
    }

    boolean isTemplate() {
	return id == 0;
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
	    if (marking[i] < caseType.getEdge(t.getIndex(), i).input()) {
		return false;
	    }
	}
	return true;
    }

    Activity open(WorkItem wi) {

	if (!wi.isFireable()) {
	    return null;
	}

	if (isTemplate()) {
	    Case caze = caseType.newCase(getMarking());
	    return caze.open(caze.getWorkItem(wi.getId()));
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

    public String toString() {

        StringBuffer string = new StringBuffer();

        string.append("\t");
        for (int i = 0; i < marking.length; ++i) {
            string.append(marking[i]);
            string.append("\t");
        }

        return string.toString();
    }
}
