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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class represents the definition of a process (case type). It
 * also keeps track of all current cases of this case type and acts as
 * a proxy to their operations. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class CaseType {

    private Map transitions = new HashMap();

    private Map places = new HashMap();

    private int[][] map;

    private List cases = new ArrayList();

    private int caseSequence = 0;

    public Place getPlace(String id) {
	return (Place) places.get(id);
    }

    public Place[] getPlaces() {

	Place[] array = new Place[places.size()];
        Iterator it = places.values().iterator();

	while (it.hasNext()) {
	    Place curr = (Place) it.next();
	    array[curr.index] = curr;
	}

	return array;
    }

    public Place registerPlace(String id) {
	Place place = new Place(this, places.size(), id);
	places.put(id, place);
	return place;
    }

    public Transition getTransition(String id) {
	return (Transition) transitions.get(id);
    }

    public Transition[] getTransitions() {

	Transition[] array = new Transition[transitions.size()];
        Iterator it = transitions.values().iterator();

	while (it.hasNext()) {
	    Transition curr = (Transition) it.next();
	    array[curr.index] = curr;
	}

	return array;
    }

    public Transition registerTransition(String id, String resource) {
	Transition trans = new Transition(this, transitions.size(), id, resource);
	transitions.put(id, trans);
	return trans;
    }

    public int[] getMapping(Transition t) {
	return (int[]) map[t.index].clone();
    }

    public int getWeight(int t, int p) {
	sync();
	return map[t][p];
    }

    public int getWeight(Transition t, Place p) {
	return getWeight(t.index, p.index);
    }

    public void setWeight(int t, int p, int w) {
	sync();
	map[t][p] = w;
    }

    public void setWeight(Transition t, Place p, int weight) {
	setWeight(t.index, p.index, weight);
    }

    private void sync() {
	if (map == null) {
	    map = new int[transitions.size()][places.size()];
	}
    }

    public String toString() {

	StringBuffer string = new StringBuffer();
	Place[] p = getPlaces();
	Transition[] t = getTransitions();

	string.append("\t");
	for (int i = 0; i < p.length; ++i) {
	    string.append(p[i].id);
	    string.append("\t");
	}
	string.append("\n");

	for (int i = 0; i < t.length; ++i) {
	    string.append(t[i].toString());
 	    string.append("\n");
	}

	return string.toString();
    }

    int nextCaseId() {
	return ++caseSequence;
    }

    public Case getCase() {
	return new Case(this, new int[places.size()]);
    }

    public Case getCase(int[] marking) {
	return new Case(this, marking);
    }

}
