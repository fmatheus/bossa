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

/**
 * This class represents a open (firing) work item.
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Activity implements Serializable {

    private int id;

    private WorkItem workItem;

    Activity(WorkItem workItem) {
	this.workItem = workItem;
	this.id = getCase().nextActivityId();
    }

    /**
     * Returns the id of this activity. <p>
     * 
     * @return the id of thi activity.
     */
    public int getId() {
        return id;
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
            System.out.println("Prevayler error!"); e.printStackTrace();
        }
        return result.booleanValue();
    }

    /**
     * Closes and finishes this activity. Call this method when the
     * activity is successfuly completed. <p>
     */
    public boolean close() {
        return dispatchCommand(new CloseActivity(this));
    }

    /**
     * Cancel this activity. Call this method if the activity could not
     * be completed. The related work item will return to the list. <p>
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
