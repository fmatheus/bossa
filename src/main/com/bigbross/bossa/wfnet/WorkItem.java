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

/**
 * This class represents a transition of a specific case instance.
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class WorkItem {

    Case caze;

    Transition transition;

    boolean fireable = true;

    WorkItem(Case caze, Transition transition) {
	this.caze = caze;
	this.transition = transition;
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

    public boolean isFireable() {
	return fireable;
    }

    public Activity open() {
	return getCase().open(this);
    }

}
