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
import java.util.Map;

import com.bigbross.bossa.BossaException;
import com.bigbross.bossa.resource.Resource;

/**
 * This class represents an open (firing) work item. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Activity implements Serializable {

    private int id;

    private WorkItem workItem;
    
    private Resource resource;

    /**
     * Creates a new activity. <p>
     * 
     * @param workItem the open work item this activity represents.
     * @param resource the resource that opened the work item.
     */
    Activity(WorkItem workItem, Resource resource) {
        this.workItem = workItem;
        this.id = getCase().nextActivityId();
        this.resource = resource;
    }

    /**
     * Returns the id of this activity. <p>
     * 
     * @return the id of this activity.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the resource responsible by this activity. <p>
     * 
     * @return the resource id.
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Returns the case type of this activity. <p>
     * 
     * @return The case type of this activity.
     */
    public CaseType getCaseType() {
	return workItem.getCaseType();
    }

    /**
     * Returns the case of this activity. <p>
     * 
     * @return The case of this activity.
     */
    public Case getCase() {
	return workItem.getCase();
    }

    /**
     * Returns the transition the open work item represents. <p>
     * 
     * @return The transition the open work item represents.
     */
    public Transition getTransition() {
	return workItem.getTransition();
    }

    /**
     * Closes and finishes this activity. Call this method when the
     * activity is successfuly completed. <p>
     * 
     * @return <code>true</code> if the activity is succesfully opened,
     *         <code>false</code> otherwise.
     * @exception SetAttributeException if the underling expression
     *            evaluation system has problems setting an attribute.
     * @exception EvaluationException if an expression evaluation error
     *            occurs. If this exception is thrown the state of the case
     *            may be left inconsistent.
     * @exception PersistenceException if an error occours when making the
     *            execution of this method persistent.
     */
    public boolean close() throws BossaException {
        return dispatchCommand(new CloseActivity(this, null));
    }

    /**
     * Closes and finishes an activity. Call this method when the
     * activity is successfuly completed. An attribute mapping should be
     * passed when this method is called. The attributes provided will
     * overwrite current set attributes and the value of these attributes
     * will be used when evaluating edge expressions. <p>
     * 
     * @param attributes the attributes mapping.
     * @return <code>true</code> if the activity is succesfully opened,
     *         <code>false</code> otherwise.
     * @exception SetAttributeException if the underling expression
     *            evaluation system has problems setting an attribute.
     * @exception EvaluationException if an expression evaluation error
     *            occurs. If this exception is thrown the state of the case
     *            may be left inconsistent.
     * @exception PersistenceException if an error occours when making the
     *            execution of this method persistent.
     */
    public boolean close(Map attributes) throws BossaException {
        return dispatchCommand(new CloseActivity(this, attributes));
    }

    /**
     * Cancel this activity. Call this method if the activity could not
     * be completed. The related work item will return to the list of
     * available work items and can be opened again. <p>
     * 
     * @return <code>true</code> if the activity is succesfully canceled,
     *         <code>false</code> otherwise.
     * @exception EvaluationException if an expression evaluation error
     *            occurs. If this exception is thrown the state of the case
     *            may be left inconsistent.
     * @exception PersistenceException if an error occours when making the
     *            execution of this method persistent.
     */
    public boolean cancel() throws BossaException {
        return dispatchCommand(new CancelActivity(this));
    }

    /**
     * Executes a command that returns a boolean value. <p>
     * 
     * @param command the command;
     * @return The <code>boolean</code> returned by the command execution.
     */
    private boolean dispatchCommand(WFNetCommand command)
        throws BossaException {
        Boolean result = (Boolean) getCaseType().getCaseTypeManager().
            getBossa().executeCommand(command);
        return result.booleanValue();
    }

    public String toString() {
	StringBuffer string = new StringBuffer();

	string.append(id);
	string.append(".");
	string.append(workItem.getTransition().toString());

	return string.toString();
    }
}
