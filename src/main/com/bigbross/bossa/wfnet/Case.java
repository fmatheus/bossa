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

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaException;
import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.resource.ResourceRegistry;

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

    private ResourceRegistry resources;

    private Map workItems;

    private Map activities;

    private int activitySequence;

    private Map attributes;

    private transient BSFManager bsf;
    
    private WFNetEvents eventQueue;

    /**
     * Creates a new case, using the provided token count and attributes.
     * Appropriate work items are activated according to the provided
     * data. <p>
     * 
     * @param caseType the case type of this case.
     * @param state the initial token count as a map (<code>String</code>, 
     *              <code>Integer</code>), indexed by the place id.
     *              This state map must have a token count for every place.
     * @param attributes the initial attributes as a map (<code>String</code>,
     *                   <code>Object</code>) of variables names (as used in
     *                   edge weight expressions) and Java objects. The Java
     *                   objects should be understandable by the underlying
     *                   BSF engine being used.
     * @exception SetAttributeException if the underlying expression
     *            evaluation system has problems setting an attribute.
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    Case(CaseType caseType, Map state, Map attributes)
        throws BossaException {

	this.caseType = caseType;

	this.marking = new int[state.size()];
        Iterator i = state.keySet().iterator();
        while (i.hasNext()) {
            String placeId = (String) i.next(); 
            int newCount = ((Integer) state.get(placeId)).intValue();
            marking[getCaseType().getPlace(placeId).getIndex()] = newCount;
        }

        this.activities = new HashMap();
        this.activitySequence = 1;
        this.eventQueue = new WFNetEvents();

        this.attributes = new HashMap();
	this.bsf = new BSFManager();
        /* A SetAttributeException can be thrown here. */
        declare(attributes);

        Collection ts = caseType.getTransitions();
        workItems = new HashMap(ts.size());
        for (i = ts.iterator(); i.hasNext(); ) {
            Transition t = (Transition) i.next();
            /* An EvaluationException can be thrown here. */
            workItems.put(t.getId(), new WorkItem(this, t, isFireable(t)));
        }

        this.id = caseType.nextCaseId();
        this.resources = new ResourceRegistry(Integer.toString(id));
    }

    /**
     * Returns the id of this case. <p>
     * 
     * @return the id of this case.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the case type of this case. <p>
     * 
     * @return the case type of this case.
     */
    public CaseType getCaseType() {
	return this.caseType;
    }

    /**
     * Returns the bossa engine this case is part, if any. <p>
     * 
     * @return the bossa engine this case is part, <code>null</code> if not
     *         part of a bossa engine.
     */
    Bossa getBossa() {
        if (getCaseType() != null && 
            getCaseType().getCaseTypeManager() != null) {
            return getCaseType().getCaseTypeManager().getBossa();
        } else {
            return null;
        }
    }

    /**
     * Returns the resource registry with the local resources of this
     * case. <p>
     * 
     * @return the resource registry with the local resources of this
     * case.
     */
    ResourceRegistry getResourceRegistry() {
        return resources;
    }

    /**
     * Returns all local resources of this case. <p>
     * 
     * @return a list of all local resources of this case.
     */
    public List getResources() {
        return resources.getResources();
    }

    /**
     * Returns the state of the case, that is, how many tokens are in each
     * place. <p>
     * 
     * @return the token count as a map (<code>String</code>, 
     *         <code>Integer</code>), indexed by the place id.
     */
    public Map getState() {
        HashMap state = new HashMap(marking.length);
        Iterator i = getCaseType().getPlaces().iterator();
        while (i.hasNext()) {
            Place p = (Place) i.next();
            state.put(p.getId(), new Integer(marking[p.getIndex()]));
        }
        return state;
    }

    /**
     * Changes the state of the case, that is, how many tokens are in each
     * place. This method bypasses the usual transition firing process and
     * should be used with caution to override the normal sequence of
     * activities. <p>
     * 
     * @param state the token count as a map (<code>String</code>, 
     *              <code>Integer</code>), indexed by the place id. Only the
     *              places present in this map have their token count modified,
     *              the others are unchanged.
     * @exception EvaluationException if an expression evaluation error
     *            occurs. If this exception is thrown the state of this case
     *            may be left inconsistent.
     */
    public void setState(Map state) throws BossaException {
        WFNetTransaction setState = new SetState(this, state);
        getBossa().execute(setState);
    }

    /**
     * Changes the state of the case, that is, how many tokens are in each
     * place. This method bypasses the usual transition firing process and
     * should be used with caution to override the normal sequence of
     * activities. <p>
     * 
     * This method will not persist the result of its activation and should
     * be used only internally as a part of a persistent transaction. <p>
     * 
     * @param state the token count as a map (<code>String</code>, 
     *              <code>Integer</code>), indexed by the place id. Only the
     *              places present in this map have their token count modified,
     *              the others are unchanged.
     * @exception EvaluationException if an expression evaluation error
     *            occurs. If this exception is thrown the state of this case
     *            may be left inconsistent.
     */
    void setStateImpl(Map state) throws BossaException {
        if (state.size() == 0) {
            return;
        }
        Iterator i = state.keySet().iterator();
        while (i.hasNext()) {
            String placeId = (String) i.next(); 
            int newCount = ((Integer) state.get(placeId)).intValue();
            marking[getCaseType().getPlace(placeId).getIndex()] = newCount;
            eventQueue.newPlaceEvent(getBossa(), WFNetEvents.ID_SET_TOKENS,
                                     this, getCaseType().getPlace(placeId),
                                     newCount);
        }
        deactivate();
        List activated = activate();
        eventQueue.newCaseEvent(getBossa(), WFNetEvents.ID_SET_STATE, this);
        eventQueue.notifyAll(getBossa());
        processTimedFiring(activated);
    }

    /**
     * Returns the current attributes of this case. <p>
     * 
     * @return the attributes as an unmodifiable map (<code>String</code>,
     *         <code>Object</code>) of variables names (as used in
     *         edge weight expressions) and Java objects.
     */
    public Map getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    /**
     * Returns the list of currently fireable work items associated
     * with this case. <p>
     *  
     * @return A list with the fireable work items of this case.
     */
    public List getWorkItems() {
        ArrayList items = new ArrayList(workItems.size());

        for (Iterator i = workItems.values().iterator(); i.hasNext(); ) {
            WorkItem wi = (WorkItem) i.next();
            if (wi.isFireable()) {
                items.add(wi);
            }
        }        

	return items;
    }

    /**
     * Returns a specific work item, selected by its id. <p>
     * 
     * @param id the work item id.
     * @return the work item, <code>null</code> if there is no work item
     *         with this id.
     */
    public WorkItem getWorkItem(String id) {
        return (WorkItem) workItems.get(id);
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
     * @return the next activity id.
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
     * Declares an attribute to be used at expression evaluation. <p>
     *
     * @param id the attribute identifier.
     * @param value a Java object with the attribute value. This object
     *              should be understandable by the underlying
     *              BSF engine being used.
     * @exception SetAttributeException if the underlying expression
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
     * Declares all the attributes to be used at expression evaluation. <p>
     *
     * @param attributes the attributes to be declared as a map
     *                   (<code>String</code>, <code>Object</code>) of
     *                   variables names (as used in edge weight expressions)
     *                   and Java objects. The Java objects should be
     *                   understandable by the underlying BSF engine being used.
     * @exception SetAttributeException if the underlying expression
     *            evaluation system has problems setting an attribute.
     */
    void declare(Map attributes) throws SetAttributeException {
        if (attributes != null) {
            Iterator it = attributes.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry attribute = (Map.Entry) it.next();
                declare((String) attribute.getKey(), attribute.getValue());
            }
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
     * @return a list of the activated work items.
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    private List activate() throws EvaluationException {
        ArrayList activated = new ArrayList(workItems.size());
        for (Iterator i = workItems.values().iterator(); i.hasNext(); ) {
            WorkItem wi = (WorkItem) i.next();
            if (!wi.isFireable()) {
                if (wi.update()) {
                  activated.add(wi);
                  eventQueue.newWorkItemEvent(getBossa(),
                                              WFNetEvents.ID_WORK_ITEM_ACTIVE,
                                              wi, null);
                }
            }
        }
        return activated;
    }

    /**
     * Tries to deactivate all activated transitions of this case. <p>
     * 
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    private void deactivate() throws EvaluationException {
        for (Iterator i = workItems.values().iterator(); i.hasNext(); ) {
            WorkItem wi = (WorkItem) i.next();
            if (wi.isFireable()) {
                if (!wi.update()) {
                  eventQueue.newWorkItemEvent(getBossa(),
                                              WFNetEvents.ID_WORK_ITEM_INACTIVE,
                                              wi, null);
                }
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
        for (Iterator i = t.getInputEdges().iterator(); i.hasNext(); ) {
            Edge e = (Edge) i.next();
            if (marking[e.getPlace().getIndex()] < e.input(this)) {
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
     * @return The activity created by the opening of this work item,
     *         <code>null</code> if the work item could not be opened.
     * @exception EvaluationException if an expression evaluation error
     *            occurs. If this exception is thrown the state of this case
     *            may be left inconsistent.
     */
    Activity open(WorkItem wi, Resource resource)
        throws BossaException {

	if (!wi.isFireable()) {
	    return null;
	}

	if (isTemplate()) {
            /* An EvaluationException can be consistently thrown here. */
            Case caze = caseType.openCaseImpl(null);
	    return caze.open(caze.getWorkItem(wi.getId()), resource);
	}

        List edges = wi.getTransition().getInputEdges();
        for (Iterator i = edges.iterator(); i.hasNext(); ) {
            Edge e = (Edge) i.next();
            /* An EvaluationException can be inconsistently thrown here. */
            int tokenNumber = e.input(this);
            this.marking[e.getPlace().getIndex()] -= tokenNumber;
            eventQueue.newPlaceEvent(getBossa(), WFNetEvents.ID_REMOVE_TOKENS,
                                     this, e.getPlace(), tokenNumber);
        }
        /* An EvaluationException can be inconsistently thrown here. */
        deactivate();

        Resource group =
            getResourceRegistry().getResource(wi.getId());
        if (group == null) {
            group = getResourceRegistry().createResourceImpl(wi.getId(), false);
        }
        group.includeImpl(resource, false);

        Activity activity = new Activity(wi, resource);
        activities.put(new Integer(activity.getId()), activity);

        eventQueue.newWorkItemEvent(getBossa(), WFNetEvents.ID_OPEN_WORK_ITEM,
                                    wi, resource);
        eventQueue.notifyAll(getBossa());

	return activity;
    }

    /**
     * Closes and finishes an activity. Call this method when the
     * activity is successfully completed. <p>
     * 
     * Closes the <code>Case</code> when the last activity is closed, that is,
     * no work items to open nor open activities remain. <p>
     *
     * An attribute mapping should be passed when this method is called.
     * This is a (<code>String</code>, <code>Object</code>) mapping of
     * variables names (as used in edge weight expressions) and Java objects.
     * The Java objects should be understandable by the underlying BSF
     * engine being used (for JavaScript, <code>Boolean</code>,
     * <code>Integer</code> and <code>String</code> are known to work).
     * The attributes provided will overwrite current set attributes and
     * the value of these attributes will be used when evaluating edge
     * weights. If you do not want to set any new attribute, use
     * <code>null</code> as the attribute mapping. <p>
     * 
     * This method will not persist the result of its activation and should
     * be used only internally as a part of a persistent transaction. <p>
     * 
     * @param activity the activity to be closed.
     * @param newAttributes the attributes mapping.
     * @return <code>true</code> if the activity is succesfully opened,
     *         <code>false</code> otherwise.
     * @exception SetAttributeException if the underlying expression
     *            evaluation system has problems setting an attribute.
     * @exception EvaluationException if an expression evaluation error
     *            occurs. If this exception is thrown the state of this case
     *            may be left inconsistent.
     */
    boolean close(Activity activity, Map newAttributes)
        throws BossaException {

	if (!activities.containsKey(new Integer(activity.getId()))) {
	    return false;
	}

        declare(newAttributes);

        List edges = activity.getTransition().getOutputEdges();
        for (Iterator i = edges.iterator(); i.hasNext(); ) {
            Edge e = (Edge) i.next();
            /* An EvaluationException can be inconsistently thrown here. */
            int tokenNumber = e.output(this);
            this.marking[e.getPlace().getIndex()] += tokenNumber;
            eventQueue.newPlaceEvent(getBossa(), WFNetEvents.ID_ADD_TOKENS,
                                     this, e.getPlace(), tokenNumber);
        }

        /* An EvaluationException can be inconsistently thrown here. */
	List activated = activate();
        activities.remove(new Integer(activity.getId()));

        eventQueue.newActivityEvent(getBossa(), WFNetEvents.ID_CLOSE_ACTIVITY,
                                    activity);
        eventQueue.notifyAll(getBossa());

        if (getWorkItems().size() == 0 && activities.size() == 0) {
            caseType.closeCase(this);
        } else {
            processTimedFiring(activated);
        }

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
     * @return <code>true</code> if the activity is succesfully canceled,
     *         <code>false</code> otherwise.
     * @exception EvaluationException if an expression evaluation error
     *            occurs. If this exception is thrown the state of this case
     *            may be left inconsistent.
     */
    boolean cancel(Activity activity) throws EvaluationException {

	if (!activities.containsKey(new Integer(activity.getId()))) {
	    return false;
	}

        List edges = activity.getTransition().getInputEdges();
        for (Iterator i = edges.iterator(); i.hasNext(); ) {
            Edge e = (Edge) i.next();
            /* An EvaluationException can be inconsistently thrown here. */
            int tokenNumber = e.input(this);
            this.marking[e.getPlace().getIndex()] += tokenNumber;
            eventQueue.newPlaceEvent(getBossa(), WFNetEvents.ID_ADD_TOKENS,
                                     this, e.getPlace(), tokenNumber);
        }

        Resource resource = activity.getResource();
        Resource group =
            getResourceRegistry().getResource(activity.getWorkItemId());
        group.removeImpl(resource, false);

        /* An EvaluationException can be inconsistently thrown here. */
	activate();
        activities.remove(new Integer(activity.getId()));

        eventQueue.newActivityEvent(getBossa(), WFNetEvents.ID_CANCEL_ACTIVITY,
                                    activity);
        eventQueue.notifyAll(getBossa());

	return true;
    }

    /**
     * Performs the required actions related to timed firing in a list
     * of activated work items. <p>
     * 
     * @param activated the list of activated work items.
     * @exception EvaluationException if an expression evaluation error
     *            occurs. If this exception is thrown the state of this case
     *            may be left inconsistent.
     */
    private void processTimedFiring(List activated) throws BossaException {
        for (Iterator i = activated.iterator(); i.hasNext(); ) {
            WorkItem wi = (WorkItem) i.next();
            /*
             * We may have a deep, nested chain of timed firings.
             * Before acting, check if the work item is still active.
             * *And* we only process zero timeouts for now.
             */ 
            if (wi.isFireable() && wi.getTransition().getTimeout() == 0) {
                Resource timerResource =
                    getResourceRegistry().getResource("__timer");
                if (timerResource == null) {
                    timerResource = getResourceRegistry().
                                          createResourceImpl("__timer", false);
                } 
                Activity a = open(wi, timerResource);
                close(a, null);
            }
        }
    }

    /**
     * Closes this case. A case will close automatically when there are no
     * active work items. This method should only be used to override
     * the normal case closing procedure and prematurely abort a case. <p>
     *
     * @return <code>true</code> if the case could be closed,
     *         <code>false</code> otherwise.
     * @exception PersistenceException if an error occours when making the
     *            execution of this method persistent.
     */
    public boolean closeCase() throws BossaException {
        WFNetTransaction closeCase = new CloseCase(this);
        return ((Boolean) getBossa().execute(closeCase)).booleanValue();
    }

    /**
     * @see java.io.Serializable
     */
    private void readObject(java.io.ObjectInputStream in)
	throws IOException, ClassNotFoundException {
	in.defaultReadObject();
        /*
         * Restores the state of the non serializable BSFManager object.
         */
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
