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
     */ 
    WorkItem(Case caze, Transition transition) {
	this.caze = caze;
	this.transition = transition;
        this.fireable = true;
    }

    /**
     * Returns the case type of this work item. <p>
     * 
     * @return The case type of this work item.
     */
    public CaseType getCaseType() {
	return getCase().getCaseType();
    }

    /**
     * Returns the case of this work item. <p>
     * 
     * @return The case of this work item.
     */
    public Case getCase() {
	return caze;
    }

    /**
     * Returns the transition this work item represents. <p>
     * 
     * @return The transition this work item represents.
     */
    public Transition getTransition() {
	return transition;
    }

    /**
     * Returns the id of this work item. It is the same id of the
     * transition this work item represents. <p>
     * 
     * @return The id of this work item.
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
     * Updates the firing status of this work item. <p>
     * 
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    void update() throws EvaluationException {
        fireable = getCase().isFireable(transition);
    }

    /**
     * Opens this work item. A open work item is represented by
     * an activity and is locked to the resource who opened it. The actual
     * completion of the work item in handled by the created activity. <p>
     * 
     * @param resource the resource that is opening the work item.
     * @return The activity created by the opening of this work item,
     *         <code>null</code> if the work item could not be opened.
     * @exception EvaluationException if an expression evaluation error
     *            occurs. If this exception is thrown the state of the case
     *            may be left inconsistent.
     * @exception PersistenceException if an error occours when making the
     *            execution of this method persistent.
     */
    public Activity open(String resource) throws BossaException {
        WFNetCommand openCommand = new OpenWorkItem(this, resource);
        return (Activity) CaseTypeManager.getInstance().executeCommand(openCommand);
    }
}
