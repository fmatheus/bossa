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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class represents the definition of a process (case type). It
 * also keeps track of all current cases of this case type. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class CaseType {
    
    private String id;

    private Map transitions;

    private Map places;

    private Edge[][] edges;

    private Map cases;

    private int caseSequence;

    /**
     * Creates a new case type, without any places or transitions. <p>
     * 
     * @param id the id of the new case type.
     */
    public CaseType(String id) {
        this.id = id;
        this.transitions = new HashMap();
        this.places = new HashMap();
        this.cases = new HashMap();
        this.caseSequence = 0;
    }

    /**
     * Returns the id of this case type. <p>
     * 
     * @return the id of this case type.
     */
    public String getId() {
        return id;
    }
    
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

    public Edge[] getEdges(Transition t) {
	return edges[t.index];
    }

    public Edge getEdge(int t, int p) {
	sync();
	return edges[t][p];
    }

    public Edge getEdge(Transition t, Place p) {
	return getEdge(t.index, p.index);
    }

    public void setEdge(int t, int p, Edge edge) {
	sync();
	edges[t][p] = edge;
    }

    public void setEdge(Transition t, Place p, Edge edge) {
	setEdge(t.index, p.index, edge);
    }

    /**
     * Returns the next case id for this case type. The case id is a
     * positive integer. <p>
     * 
     * @return the next case id.
     */
    int nextCaseId() {
        return ++caseSequence;
    }

    /**
     * Creates a new case with no tokens. <p>
     * 
     * @return the newly created case.
     */
    Case newCase() {
        return newCase(new int[places.size()]);
    }

    /**
     * Creates a new case with the provided tokens. <p>
     * 
     * @return the newly created case.
     */
    Case newCase(int[] marking) {
        Case caze = new Case(this, marking);
        cases.put(new Integer(caze.getId()), caze);
        return caze;
    }
    
    /**
     * Returns a case by its id. <p>
     * 
     * @param id the case id.
     * @return the case with the provided id, <code>null</code> if
     *         this case does not exists.
     */
    public Case getCase(int id) {
        return (Case) cases.get(new Integer(id));
    }

    private void sync() {
	if (edges == null) {
	    Edge edge = new Edge();
	    edges = new Edge[transitions.size()][places.size()];
	    for (int i = 0; i < edges.length; ++i) {
		Arrays.fill(edges[i], edge);
	    }
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
}
