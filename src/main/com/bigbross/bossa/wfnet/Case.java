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
import java.io.InvalidObjectException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

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

    private Map activities;

    private int activitySequence;

    private Map attributes;

    private transient BSFManager bsf;

    /**
     * Creates a new case, using the provided marking. An initial scan of
     * the provided marking is performed, and appropriate work items are
     * created.
     * 
     * @param caseType the case type of this case.
     * @param marking the initial marking (tokens).
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    Case(CaseType caseType, int[] marking) throws EvaluationException {

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

        /* An EvaluationException can be thrown here. */
	deactivate();

        this.id = caseType.nextCaseId();
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
     * FIXME: Maybe we should return null also if the work item is not
     * fireable.
     * 
     * @param id the work item id.
     * @return the work item, <code>null</code> if there is no work item
     *         with this id.
     */
    WorkItem getWorkItem(String id) {
        Transition t = caseType.getTransition(id);
        if (t != null) {
            return workItems[t.getIndex()];
        } else {
            return null;
        }
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
     * Returns a specific activity, selected by its id. <p>
     * 
     * @param id the activity id.
     * @return the activity, <code>null</code> if there is no activity
     *         with this id.
     */
    public Activity getActivity(int id) {
        return (Activity) activities.get(new Integer(id));
    }

    /**
     * Returns the next activity id for this case. <p>
     * 
     * @return The next activity id.
     */ 
    int nextActivityId() {
	return activitySequence++;
    }

    /**
     * Indicates if this case is a template case of some case type. <p>
     * 
     * @return <code>true</code> is this case is a template,
     *         <code>false</code> otherwise.
     */
    boolean isTemplate() {
	return id == 0;
    }

    /**
     * Declares an attribute to be used at expression evaluation.
     *
     * @param id the attribute identifier.
     * @param value an <code>Object</code> with the attribute value.
     * @exception SetAttributeException if the underling expression
     *            evaluation system has problems setting an attribute.
     */
    void declare(String id, Object value) throws SetAttributeException {
	try {
            bsf.declareBean(id, value, value.getClass());
	    attributes.put(id, value);
	} catch (BSFException e) {
            throw new SetAttributeException("Could not set variable '" +
                                            id + "'", e);
	}
    }

    /**
     * Evaluates an integer expression using the local attributes of this
     * case. <p>
     * 
     * @param expression the expression to be evaluated.
     * @return The expression result.
     * @exception EvaluationException if an evaluation error occurs.
     */
    int eval(String expression) throws EvaluationException {
        try {
            Object result = bsf.eval("javascript", "WFNet", 
                                     0, 0, expression);
            if (result instanceof Number) {
                return ((Number) result).intValue();
            } else if (result instanceof Boolean) {
	       return ((Boolean) result).booleanValue() ? 1 : 0;
            } else {
                throw new EvaluationException("'" + result + 
                                            "' is not a number or boolean.");
            }
        } catch (BSFException e) {
                throw new EvaluationException("Error in the expression " +
                                              "evaluation sub-system.", e);
        }
    }

    /**
     * Tries to activate all deactivated transitions of this case. <p>
     * 
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    private void activate() throws EvaluationException {
	for (int i = 0; i < workItems.length; ++i) {
	    if (!workItems[i].isFireable()) {
		workItems[i].update();
	    }
	}
    }

    /**
     * Tries to deactivate all activated transitions of this case. <p>
     * 
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    private void deactivate() throws EvaluationException {
	for (int i = 0; i < workItems.length; ++i) {
	    if (workItems[i].isFireable()) {
		workItems[i].update();
	    }
	}
    }

    /**
     * Indicates if a transition is fireable, that is, if it is an actual
     * work item. <p>
     * 
     * @param t the transition.
     * @return <code>true</code> if the transition is fireable;
     *         <code>false</code> otherwise.
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    boolean isFireable(Transition t) throws EvaluationException {
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
     * @return The activity created the opening of this work item,
     *         <code>null</code> if the work item could not be opened.
     * @exception EvaluationException if an expression evaluation error
     *            occurs. If this exception is thrown the state of this case
     *            may be left inconsistent.
     */
    Activity open(WorkItem wi, String resource) throws EvaluationException {

	if (!wi.isFireable()) {
	    return null;
	}

	if (isTemplate()) {
            /* An EvaluationException can be consistently thrown here. */
            Case caze = caseType.newCase(getMarking());
	    return caze.open(caze.getWorkItem(wi.getId()), resource);
	}

        Activity activity = new Activity(wi, resource);
        activities.put(new Integer(activity.getId()), activity);
	Edge[] edges = wi.getTransition().getEdges();
        for(int i = 0; i < marking.length; ++i) {
            /* An EvaluationException can be inconsistently thrown here. */
            this.marking[i] -= edges[i].input(this);
        }
        /* An EvaluationException can be inconsistently thrown here. */
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
     * @exception SetAttributeException if the underling expression
     *            evaluation system has problems setting an attribute.
     * @exception EvaluationException if an expression evaluation error
     *            occurs. If this exception is thrown the state of this case
     *            may be left inconsistent.
     */
    boolean close(Activity activity, Map newAttributes) 
        throws SetAttributeException, EvaluationException {

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
            /* An EvaluationException can be inconsistently thrown here. */
	    this.marking[i] += edges[i].output(this);
	}
        /* An EvaluationException can be inconsistently thrown here. */
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
     * @exception EvaluationException if an expression evaluation error
     *            occurs. If this exception is thrown the state of this case
     *            may be left inconsistent.
     */
    boolean cancel(Activity activity) throws EvaluationException {

	if (!activities.containsKey(new Integer(activity.getId()))) {
	    return false;
	}

	activities.remove(new Integer(activity.getId()));
	Edge[] edges = activity.getTransition().getEdges();
	for(int i = 0; i < marking.length; ++i) {
            /* An EvaluationException can be inconsistently thrown here. */
	    this.marking[i] += edges[i].input(this);
	}
        /* An EvaluationException can be inconsistently thrown here. */
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

    /**
     * Restores the state of the not serializable object
     * <code>BSFManager</code> after this case state is restored. <p>
     */
    private void readObject(java.io.ObjectInputStream in)
	throws IOException, ClassNotFoundException {
	in.defaultReadObject();
	bsf = new BSFManager();
	Iterator it = attributes.entrySet().iterator();
	while (it.hasNext()) {
	    Map.Entry attr = (Map.Entry) it.next();
	    try {
		bsf.declareBean((String) attr.getKey(), attr.getValue(),
                                attr.getValue().getClass());
	    } catch (BSFException e) {
                throw new InvalidObjectException("Could not restore the " +
                                                 "BSFmanager object: " +
                                                 e.toString());
            }
	}
    }
}
