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

import org.apache.log4j.Logger;

/**
 * This class represents a open (firing) work item.
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Activity implements Serializable {

    /**
     * The logger object used by this class. <p>
     *
     * @see <a href="http://jakarta.apache.org/log4j/docs/index.html"
     *      target=_top>Log4J HomePage</a>
     */
    private static Logger logger =
        Logger.getLogger(Activity.class.getName());

    private int id;

    private WorkItem workItem;
    
    private String resource;

    Activity(WorkItem workItem, String resource) {
        this.workItem = workItem;
        this.id = getCase().nextActivityId();
        this.resource = resource;
    }

    /**
     * Returns the id of this activity. <p>
     * 
     * @return the id of thi activity.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the resource responsible by this activity. <p>
     * 
     * @return the resource id.
     */
    public String getResource() {
        return resource;
    }

    public CaseType getCaseType() {
	return workItem.getCaseType();
    }

    public Case getCase() {
	return workItem.getCase();
    }

    public Transition getTransition() {
	return workItem.getTransition();
    }

    private boolean dispatchCommand(WFNetCommand command) {
        Boolean result = null;
        try {
         result = 
          (Boolean) CaseTypeManager.getInstance().executeCommand(command);
        } catch (Exception e) {
            /* FIXME: Exceptions, please. */
            logger.error("Prevayler error!", e);
        }
        return result.booleanValue();
    }

    /**
     * Closes and finishes this activity. Call this method when the
     * activity is successfuly completed. <p>
     * 
     * @return <code>true</code> is the activity is succesfuly opened,
     *         <code>false</code> otherwise.
     */
    public boolean close() {
        return dispatchCommand(new CloseActivity(this, null));
    }

    /**
     * Closes and finishes an activity. Call this method when the
     * activity is successfuly completed. An attribute mapping shuld be
     * passed when this method is called. The attributes provided will
     * overwrite current set attributes and the value of these attributes
     * will be used when evaluating edge expressions. <p>
     * 
     * @param attributes the attributes mapping.
     * @return <code>true</code> is the activity is succesfuly opened,
     *         <code>false</code> otherwise.
     */
    public boolean close(Map attributes) {
        return dispatchCommand(new CloseActivity(this, attributes));
    }

    /**
     * Cancel this activity. Call this method if the activity could not
     * be completed. The related work item will return to the list of
     * available work items and can be opened again. <p>
     * 
     * @return <code>true</code> is the activity is succesfuly canceled,
     *         <code>false</code> otherwise.
     */
    public boolean cancel() {
        return dispatchCommand(new CancelActivity(this));
    }

    public String toString() {
	StringBuffer string = new StringBuffer();

	string.append(id);
	string.append(".");
	string.append(workItem.getTransition().toString());

	return string.toString();
    }
}
