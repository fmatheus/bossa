/*
 * Bossa Workflow System
 *
 * $Id$
 *
 * Copyright (C) 2002 OpenBR Sistemas S/C Ltda.
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
 * This class represents the definition of a transition.
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Transition {

    CaseType caseType;

    int index;

    String id;

    String resource;

    Transition(Transition t) {
	this.caseType = t.caseType;
	this.index = t.index;
	this.id = t.id;
	this.resource = t.resource;
    }

    Transition(CaseType caseType, int index, String id, String resource) {
	this.caseType = caseType;
	this.index = index;
	this.id = id;
	this.resource = resource;
    }

    int weight(Place p) {
	return caseType.getWeight(index, p.index);
    }

    void input(Place p, int weight) {
	caseType.setWeight(index, p.index, -weight);
    }

    void output(Place p, int weight) {
	caseType.setWeight(index, p.index, weight);
    }

    int[] input() {

	int[] input = caseType.getMapping(this);

	for(int i = 0; i < input.length; ++i) {
	    if (input[i] > 0) {
		input[i] = 0;
	    }
	}

	return input;
    }

    int[] output() {

	int[] output = caseType.getMapping(this);

	for(int i = 0; i < output.length; ++i) {
	    if (output[i] < 0) {
		output[i] = 0;
	    }
	}

	return output;
    }

    public String toString() {

	StringBuffer string = new StringBuffer();
	Place[] p = caseType.getPlaces();

	string.append(this.id);
	string.append("\t");
	for (int j = 0; j < p.length; ++j) {
	    string.append(this.weight(p[j]));
	    string.append("\t");
	}
	string.append(this.resource);

	return string.toString();
    }

}
