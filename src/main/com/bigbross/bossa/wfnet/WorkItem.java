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

import com.bigbross.bossa.BossaException;
import com.bigbross.bossa.resource.Resource;

/**
 * This class represents a transition of a specific case instance. <p>
 * 
 * We use a somewhat non standard definition of a work item: instead of
 * a <emph>fireable</emph> transition a work item is a <emph>likely 
 * fireable</emph> transition. All methods of this class account for this
 * and it is possible to discover in advance if a work item is actually
 * fireable without opening it. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class WorkItem implements Serializable {

    private Case caze;

    private Transition transition;

    private boolean fireable;

    /**
     * Creates a new work item. <p>
     * 
     * @param caze the case.
     * @param transition the transition.
     * @param fireable the work item fireable status.
     */ 
    WorkItem(Case caze, Transition transition, boolean fireable) {
	this.caze = caze;
	this.transition = transition;
        this.fireable = fireable;
    }

    /**
     * Returns the case type of this work item. <p>
     * 
     * @return the case type of this work item.
     */
    public CaseType getCaseType() {
	return getCase().getCaseType();
    }

    /**
     * Returns the case of this work item. <p>
     * 
     * @return the case of this work item.
     */
    public Case getCase() {
	return caze;
    }

    /**
     * Returns the transition this work item represents. <p>
     * 
     * @return the transition this work item represents.
     */
    Transition getTransition() {
	return transition;
    }

    /**
     * Returns the id of this work item. It is the same id of the
     * transition this work item represents. <p>
     * 
     * @return the id of this work item.
     */
    public String getId() {
        return getTransition().getId();
    }

    /**
     * Indicates if this work item is fireable. <p>
     * 
     * @return <code>true</code> if the work item is fireable;
     *         <code>false</code> otherwise.
     */
    public boolean isFireable() {
	return fireable;
    }

    /**
     * Indicates if this work item can be open by the provided resource. <p>
     * 
     * @param resource the resource.
     * @return <code>true</code> if the resource can open this work item;
     *         <code>false</code> otherwise.
     */
    public boolean canBePerformedBy(Resource resource) {
        return getTransition().getResource().
                    contains(caze.getResourceRegistry(), resource);
    }

    /**
     * Updates the firing status of this work item. <p>
     * 
     * @return <code>true</code> if the work item is fireable,
     *         <code>false</code> otherwise.
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    boolean update() throws EvaluationException {
        return fireable = getCase().isFireable(transition);
    }

    /**
     * Opens this work item. A open work item is represented by
     * an activity and is locked to the resource who opened it. The actual
     * completion of the work item in handled by the created activity. <p>
     * 
     * @param resource the resource that is opening the work item.
     * @return the activity created by the opening of this work item,
     *         <code>null</code> if the work item could not be opened.
     * @exception EvaluationException if an expression evaluation error
     *            occurs. If this exception is thrown the state of the case
     *            may be left inconsistent.
     * @exception PersistenceException if an error occours when making the
     *            execution of this method persistent.
     */
    public Activity open(Resource resource) throws BossaException {
        WFNetTransaction openTransaction = new OpenWorkItem(this, resource);
        return (Activity) getCaseType().getCaseTypeManager().
            getBossa().execute(openTransaction);
    }
}
