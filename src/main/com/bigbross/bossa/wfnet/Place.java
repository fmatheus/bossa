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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class represents a place.
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Place {

    CaseType ct;

    int index;

    String id;

    Place(CaseType ct, int index, String id) {
	this.ct = ct;
	this.index = index;
	this.id = id;
    }

    int weight(Transition t) {
	return ct.getWeight(t.index, index);
    }

    void input(Transition t, int weight) {
	ct.setWeight(t.index, index, -weight);
    }

    void output(Transition t, int weight) {
	ct.setWeight(t.index, index, weight);
    }

}