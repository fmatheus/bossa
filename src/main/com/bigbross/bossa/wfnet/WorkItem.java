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

import org.apache.log4j.Logger;

/**
 * This class represents a transition of a specific case instance.
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class WorkItem implements Serializable {

    /**
     * The logger object used by this class. <p>
     *
     * @see <a href="http://jakarta.apache.org/log4j/docs/index.html"
     *      target=_top>Log4J HomePage</a>
     */
    private static Logger logger =
        Logger.getLogger(Activity.class.getName());

    private Case caze;

    private Transition transition;

    private boolean fireable;

    WorkItem(Case caze, Transition transition) {
	this.caze = caze;
	this.transition = transition;
        this.fireable = true;
    }

    void update() {
	fireable = getCase().isFireable(transition);
    }

    public CaseType getCaseType() {
	return getCase().getCaseType();
    }

    public Case getCase() {
	return caze;
    }

    public Transition getTransition() {
	return transition;
    }

    public String getId() {
        return getTransition().getId();
    }

    public boolean isFireable() {
	return fireable;
    }

    /**
     * Opens this work item. A open work item is represented by
     * an activity and is locked to the resource who opened it. The actual
     * completion of the work item in handled by the created activity. <p>
     * 
     * @param resource the resource that is opening the work item.
     * @return The activity created the opening of this work item.
     */
    public Activity open(String resource) {
        WFNetCommand openCommand = new OpenWorkItem(this, resource);
        try {
          return (Activity) CaseTypeManager.getInstance().executeCommand(openCommand);
        } catch (Exception e) {
            /* FIXME: Exceptions, please. */
            logger.error("Prevayler error!", e);
        }
        return null;
    }
}
