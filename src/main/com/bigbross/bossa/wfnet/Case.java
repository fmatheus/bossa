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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.apache.log4j.Logger;

/**
 * This class represents a specific instance of a case type. It
 * holds the current state of a case. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Case implements Serializable {

    /**
     * The logger object used by this class. <p>
     *
     * @see <a href="http://jakarta.apache.org/log4j/docs/index.html"
     *      target=_top>Log4J HomePage</a>
     */
    private static Logger logger = Logger.getLogger(Case.class.getName());

    private int id;

    private CaseType caseType;

    private int[] marking;

    private WorkItem[] workItems;

    private Map activities;

    private int activitySequence;

    private Map attributes;

    private transient BSFManager bsf;

    Case(CaseType caseType, int[] marking) {

	this.id = caseType.nextCaseId();
	this.caseType = caseType;
	this.marking = marking;
        this.activities = new HashMap();
        this.attributes = new HashMap();
        this.activitySequence = 1;
	this.bsf = new BSFManager();

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
     * Returns a list of activities associated with this case. <p>
     * 
     * @return The list of activities of this case. 
     */
    public List getActivities() {
        List acts = new ArrayList(activities.size());
        acts.addAll(activities.values());
        return acts;
    }

    /**
     * Return a specific activity, selected by its id. <p>
     * 
     * @param id the activity id.
     * @return the activity, <code>null</code> if there is no activity
     *         with this id.
     */
    public Activity getActivity(int id) {
        return (Activity) activities.get(new Integer(id));
    }

    int nextActivityId() {
	return activitySequence++;
    }

    boolean isTemplate() {
	return id == 0;
    }

    /**
     * Declares an attribute to be used at expression evaluation.
     *
     * @param id the attribute identifier.
     * @param value an <code>Object</code> with the attribute value.
     */
    void declare(String id, Object value) {
	try {
	    attributes.put(id, value);
	    bsf.declareBean(id, value, value.getClass());
	} catch (BSFException e) {
	    logger.warn(e.getMessage());
	}
    }

    /**
     * Evaluates an integer expression using the local attributes of this
     * case. <p>
     * 
     * @return the expression result.
     * @exception BSFException if an error occurs.
     */
    int eval(String expression) throws BSFException {
	Object result = bsf.eval("javascript", "WFNet", 0, 0, expression);
	if (result instanceof Number) {
	    return ((Number) result).intValue();
	}
	if (result instanceof Boolean) {
	    return ((Boolean) result).booleanValue() ? 1 : 0;
	}
	throw new BSFException("'" + result + "' is not a number.");
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
	    if (marking[i] < caseType.getEdge(t.getIndex(), i).input(this)) {
		return false;
	    }
	}
	return true;
    }

    /**
     * Opens a work item. A open work item is represented by
     * an activity and is locked to the resource who opened it. The actual
     * completion of the work item in handled by the created activity. <p>
     * 
     * This method will not persist the result of its activation and should
     * be used only internally as a part of a persistent transaction. <p>
     * 
     * @param wi the work item to be opened.
     * @param resource the resource that is opening the work item.
     * @return The activity created the opening of this work item.
     */
    Activity open(WorkItem wi, String resource) {

	if (!wi.isFireable()) {
	    return null;
	}

	if (isTemplate()) {
	    Case caze = caseType.newCase(getMarking());
	    return caze.open(caze.getWorkItem(wi.getId()), resource);
	}

	Activity activity = new Activity(wi, resource);
	activities.put(new Integer(activity.getId()), activity);
	Edge[] edges = activity.getTransition().getEdges();
	for(int i = 0; i < marking.length; ++i) {
	    this.marking[i] -= edges[i].input(this);
	}
	deactivate();

	return activity;
    }

    /**
     * Closes and finishes an activity. Call this method when the
     * activity is successfuly completed. <p>
     * 
     * An attribute mapping can be passed when this method is called.
     * The attributes provided will overwrite current set attributes and
     * the value of these attributes will be used when evaluating edge
     * expressions. If you do not want to set any new attribute, use
     * <code>null</code> as the attribute mapping. <p>
     * 
     * This method will not persist the result of its activation and should
     * be used only internally as a part of a persistent transaction. <p>
     * 
     * @param activity the activity to be closed.
     * @param newAttributes the attributes mapping.
     * @return <code>true</code> is the activity is succesfuly opened,
     *         <code>false</code> otherwise.
     */
    boolean close(Activity activity, Map newAttributes) {

	if (!activities.containsKey(new Integer(activity.getId()))) {
	    return false;
	}

        if (newAttributes != null) {
            Iterator it = newAttributes.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry attribute = (Map.Entry) it.next();
                declare((String) attribute.getKey(), attribute.getValue());
            }
        }

	activities.remove(new Integer(activity.getId()));
	Edge[] edges = activity.getTransition().getEdges();
	for(int i = 0; i < marking.length; ++i) {
	    this.marking[i] += edges[i].output(this);
	}
	activate();

	return true;
    }

    /**
     * Cancel an activity. Call this method if the activity could not
     * be completed. The related work item will return to the list of
     * available work items and can be opened again. <p>
     * 
     * This method will not persist the result of its activation and should
     * be used only internally as a part of a persistent transaction. <p>
     * 
     * @param activity the activity to be canceled.
     * @return <code>true</code> is the activity is succesfuly canceled,
     *         <code>false</code> otherwise.
     */
    boolean cancel(Activity activity) {

	if (!activities.containsKey(new Integer(activity.getId()))) {
	    return false;
	}

	activities.remove(new Integer(activity.getId()));
	Edge[] edges = activity.getTransition().getEdges();
	for(int i = 0; i < marking.length; ++i) {
	    this.marking[i] += edges[i].input(this);
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

    private void readObject(java.io.ObjectInputStream in)
	throws IOException, ClassNotFoundException {
	in.defaultReadObject();
	bsf = new BSFManager();
	Iterator it = attributes.entrySet().iterator();
	while (it.hasNext()) {
	    Map.Entry attr = (Map.Entry) it.next();
	    try {
		bsf.declareBean((String) attr.getKey(), attr.getValue(), attr.getValue().getClass());
	    } catch (org.apache.bsf.BSFException e) {}
	}
    }

}
