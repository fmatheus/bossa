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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bigbross.bossa.BossaException;
import com.bigbross.bossa.resource.ResourceRegistry;

/**
 * This class represents the definition of a process (case type). It
 * also keeps track of all current cases of this case type. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class CaseType implements Serializable {
    
    private String id;

    private Map transitions;

    private Map places;

    private Edge[][] edges;

    private ResourceRegistry resources;

    private Map cases;

    private Case template;

    private int caseSequence;
    
    private CaseTypeManager caseTypeManager;

    /**
     * Creates a new case type, without any places or transitions. <p>
     * 
     * @param id the id of the new case type.
     */
    public CaseType(String id) {
        this.id = id;
        this.transitions = new HashMap();
        this.places = new HashMap();
        this.resources = new ResourceRegistry(id);
        this.cases = new HashMap();
        this.caseSequence = 0;
        this.caseTypeManager = null;
    }

    /**
     * Returns the id of this case type. <p>
     * 
     * @return the id of this case type.
     */
    public String getId() {
        return id;
    }
    
    /**
     * Returns the <code>Place</code> of this case type with the specified
     * id. <p>
     *
     * @param id the <code>Place</code> id.
     * @return the <code>Place</code>, 
     *         or <code>null</code> if there is no such a place.
     */
    public Place getPlace(String id) {
	return (Place) places.get(id);
    }

    /**
     * Returns all places of this case type. <p>
     * 
     * @return a list of all places of this case type.
     */
    public Place[] getPlaces() {

	Place[] array = new Place[places.size()];
        Iterator it = places.values().iterator();

	while (it.hasNext()) {
	    Place curr = (Place) it.next();
	    array[curr.index] = curr;
	}

	return array;
    }

    /**
     * Creates a <code>Place</code> with the specified id in this case
     * type. <p>
     *
     * @param id the <code>Place</code> id.
     * @return the created <code>Place</code>.
     */
    public Place registerPlace(String id) {
	Place place = new Place(this, places.size(), id);
	places.put(id, place);
	return place;
    }

    /**
     * Returns the <code>Transition</code> of this case type with the
     * specified id. <p>
     *
     * @param id the <code>Transition</code> id.
     * @return the <code>Transition</code>,
     *         or <code>null</code> if there is no such transition.
     */
    public Transition getTransition(String id) {
	return (Transition) transitions.get(id);
    }

    /**
     * Returns all transitions of this case type. <p>
     * 
     * @return a list of all transitions of this case type.
     */
    public Transition[] getTransitions() {

	Transition[] array = new Transition[transitions.size()];
        Iterator it = transitions.values().iterator();

	while (it.hasNext()) {
	    Transition curr = (Transition) it.next();
	    array[curr.getIndex()] = curr;
	}

	return array;
    }

    /**
     * Creates a <code>Transition</code> with the specified id in this case
     * type. <p>
     *
     * @param id the <code>Transition</code> id.
     * @param resource the expression to select the resource responsible by
     *        this <code>Transition</code>.
     * @return the created <code>Transition</code>.
     * @see com.bigbross.bossa.resource.Expression
     */
    public Transition registerTransition(String id, String resource) {
	Transition trans = new Transition(this, transitions.size(), id, resource);
	transitions.put(id, trans);
	return trans;
    }

    /**
     * Returns all the edges incident (input, output and incative) to a
     * transition. <p>
     * 
     * @param t the transition.
     * @return an array of all edges incident to a transition.
     */
    Edge[] getEdges(Transition t) {
	return edges[t.getIndex()];
    }

    /**
     * Returns the edge that connects the transition with index <code>t</code>
     * to the place with index <code>p</code>. The edge can be an input,
     * output or inactive edge (if there is no edge connecting them). <p>
     * 
     * @param t the transition index.
     * @param p the place index.
     * @return the edge connecting them.
     */
    Edge getEdge(int t, int p) {
	return edges[t][p];
    }

    /**
     * Returns the edge that connects a transition to a place. The edge can
     * be an input, output or inactive edge (if there is no edge connecting
     * them). <p>
     * 
     * @param t the transition.
     * @param p the place.
     * @return the edge connecting them.
     */
    Edge getEdge(Transition t, Place p) {
	return getEdge(t.getIndex(), p.index);
    }

    /**
     * Sets the edge that connects the transition with index <code>t</code>
     * to the place with index <code>p</code>. <p>
     * 
     * @param t the transition index.
     * @param p the place index.
     * @param edge the edge connecting them.
     */
    void setEdge(int t, int p, Edge edge) {
	edges[t][p] = edge;
    }

    /**
     * Sets the edge that connects a transition to a place. <p>
     * 
     * @param t the transition.
     * @param p the place.
     * @param edge the edge connecting them.
     */
    void setEdge(Transition t, Place p, Edge edge) {
	setEdge(t.getIndex(), p.index, edge);
    }

    /**
     * Returns the resource registry with the local resources of this
     * case type. <p>
     * 
     * @return the resource registry with the local resources of this
     * case type.
     */
    ResourceRegistry getResourceRegistry() {
        return resources;
    }

    /**
     * Returns all resource groups of this case type. Use these resources to
     * associate (by includes and excludes) system resources with case type
     * resources. <p>
     * 
     * @return a list of all resource groups of this case type.
     */
    public List getResources() {
        return resources.getResources();
    }

    /**
     * Returns the next case id for this case type. The case id is a
     * positive integer. <p>
     * 
     * @return the next case id.
     */
    int nextCaseId() {
        return caseSequence++;
    }

    /**
     * Creates a new case using the case type template as initial marking. <p>
     * 
     * @return the newly created case.
     * @exception SetAttributeException if the underlying expression
     *            evaluation system has problems setting an attribute.
     */
    Case openCase() throws BossaException {
        Case caze = new Case(template);
        cases.put(new Integer(caze.getId()), caze);
        return caze;
    }

    /**
     * Closes an open case. <p>
     *
     * @param caze the <code>Case</code> to be closed.
     * @return <code>true</code> if the case was open,
     *         <code>false</code> otherwise.
     */
    boolean closeCase(Case caze) {
        return cases.remove(new Integer(caze.getId())) != null;
    }
    
    /**
     * Builds the transition map. Call this method after you have created
     * all places and transitions and before you start creating edges. <p>
     */
    public void buildMap() {
	if (edges == null) {
	    Edge edge = new Edge();
	    edges = new Edge[transitions.size()][places.size()];
	    for (int i = 0; i < edges.length; ++i) {
		Arrays.fill(edges[i], edge);
	    }
	}
    }

    /**
     * Builds the template case that will spawn all other cases. Call this
     * method after you have finished building the case type. <p>
     * 
     * @param marking the marking of the template case.
     * @param attributes the attributes of the template case.
     * @exception SetAttributeException if the underlying expression
     *            evaluation system has problems setting an attribute.
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */ 
    public void buildTemplate(int[] marking, Map attributes)
        throws BossaException {
        if (template == null) {
            template = new Case(this, marking, attributes);
        }

        for (int i = 0; i < edges.length; ++i) {
            for (int j = 0; j < edges[i].length; ++j) {
                edges[i][j].weight(template);
            }
        }
    }

    /**
     * Returns the case type manager this case type is registered into. <p>
     * 
     * @return The case type manager this case type is registered into.
     */
    CaseTypeManager getCaseTypeManager() {
        return caseTypeManager;
    }

    /**
     * Sets the case type manager this case type is registered into. <p>
     * 
     * @param caseTypeManager The case type manager this case type is
     *        registered into.
     */
    void setCaseTypeManager(CaseTypeManager caseTypeManager) {
        this.caseTypeManager = caseTypeManager;
    }

    /**
     * Returns a case by its id. <p>
     * 
     * @param id the case id.
     * @return the case with the provided id, <code>null</code> if
     *         this case does not exist.
     */
    public Case getCase(int id) {
        if (id == 0) {
            return template;
        } else {
            return (Case) cases.get(new Integer(id));
        }
    }

    /**
     * Returns all cases of this case type. <p>
     * 
     * @return A list of all active cases.
     */
    public List getCases() {
        ArrayList caseList = new ArrayList();
        caseList.addAll(cases.values());
        return caseList;
    }

    /**
     * Returns the list of all work items of the cases of this case type. <p>
     * 
     * @return The list of work itens of this case type.
     */
    public List getWorkItems() {
        return getWorkItems(false);
    }

    /**
     * Returns the list of all work items of the cases of this case type.
     * If desired, the initial work item(s) can be returned. Opening an
     * initial work item will create a new case. <p>
     * 
     * @param getInitial set to <code>true</code> to get the initial work
     *                   items and to <code>false</code> to only get the
     *                   standard work items. 
     * @return The list of work itens of this case type.
     */
    public List getWorkItems(boolean getInitial) {
        ArrayList items = new ArrayList();
        if (getInitial) {
            items.addAll(template.getWorkItems());
        }
        Iterator i = cases.values().iterator();   
        while (i.hasNext()) {
            items.addAll(((Case) i.next()).getWorkItems());
        }
        return items;
    }

    /**
     * Returns the list of all activities of the cases of this case type. <p>
     * 
     * @return The list of activities of this case type.
     */
    public List getActivities() {
        ArrayList acts = new ArrayList();
        Iterator i = cases.values().iterator();   
        while (i.hasNext()) {
            acts.addAll(((Case) i.next()).getActivities());
        }
        return acts;
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
