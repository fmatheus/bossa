/*
 * Bossa Workflow System
 *
 * $Id$
 *
 * Copyright (C) 2003,2004 OpenBR Sistemas S/C Ltda.
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaException;
import com.bigbross.bossa.io.CaseTypeXMLLoader;

/**
 * This class manages all registered case types of the workflow
 * system. It provides all the case type life cycle functions and is
 * the entry point of the WFNet package. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class CaseTypeManager implements Serializable {

    private Bossa engine;

    private Map caseTypes;

    /**
     * Creates a new empty case type manager. <p>
     *
     * @param engine the bossa engine this case type manager is part.
     */
    public CaseTypeManager(Bossa engine) {
        this.engine = engine;
        this.caseTypes = new HashMap();
    }

    /**
     * Creates a new empty case type manager. <p>
     */
    public CaseTypeManager() {
        this(null);
    }

    /**
     * Returns the bossa engine this resorce manager is part. <p>
     *
     * @return The bossa engine this resorce manager is part.
     */
    Bossa getBossa() {
        return engine;
    }

    /**
     * Registers a new case type in the manager. <p>
     *
     * @param caseType the case type.
     * @return <code>true</code> if the case type is registered,
     *         <code>false</code> if there is already a case type
     *         registered with the same id.
     * @exception PersistenceException if an error occours when making the
     *            execution of this method persistent.
     */
    public boolean registerCaseType(CaseType caseType) throws BossaException {
        WFNetTransaction registerTransaction = new RegisterCaseType(caseType);
        return ((Boolean) getBossa().execute(registerTransaction)).
            booleanValue();
    }

    /**
     * Registers a new case type read from a PNML file in the manager. <p>
     *
     * @param file the PNML file.
     * @return <code>true</code> if the case type is registered,
     *         <code>false</code> if there is already a case type
     *         registered with the same id.
     * @exception DataTransferException if an error happens reading or parsing
     *                                  the PNML file.
     * @exception SetAttributeException if the underlying expression
     *            evaluation system has problems setting an attribute.
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     * @exception PersistenceException if an error occours when making the
     *            execution of this method persistent.
     */
    public boolean registerCaseType(String file) throws BossaException {
        CaseType caseType = new CaseTypeXMLLoader(file).createCaseType();
        return registerCaseType(caseType);
    }

    /**
     * Registers a new case type in the manager. <p>
     *
     * This method does not create a transaction in the prevalent system. The
     * execution of this method will not be persistent unless it is called
     * inside an appropriate transaction. <p>
     *
     * @param caseType the <code>CaseType</code> object containing the
     *                 case type.
     * @return <code>true</code> if the case type is registered,
     *         <code>false</code> if there is already a case type
     *         registered with the same id.
     */
    public boolean registerCaseTypeImpl(CaseType caseType) {
        if (caseTypes.containsKey(caseType.getId())) {
            return false;
        }
        caseTypes.put(caseType.getId(), caseType);
        caseType.setCaseTypeManager(this);
        if (getBossa() != null) {
            getBossa().getResourceManager().
                registerSubContext(caseType.getResourceRegistry());
        }
        WFNetEvents queue = new WFNetEvents();
        queue.newCaseTypeEvent(getBossa(),
                               WFNetEvents.ID_REGISTER_CASE_TYPE,
                               caseType);
        queue.notifyAll(getBossa());
        return true;
    }

    /**
     * Removes the case type from the manager, if present. This operation will
     * remove also <emph>all</emph> cases of this case type. <p>
     *
     * @param id the id of the case type.
     * @exception PersistenceException if an error occours when making the
     *            execution of this method persistent.
     */
    public void removeCaseType(String id) throws BossaException {
        WFNetTransaction removeTransaction = new RemoveCaseType(id);
        getBossa().execute(removeTransaction);
    }

    /**
     * Removes the case type from the manager, if present. This operation will
     * remove also <emph>all</emph> cases of this case type. <p>
     *
     * This method does not create a transaction in the prevalent system. The
     * execution of this method will not be persistent unless it is called
     * inside an appropriate transaction. <p>
     *
     * @param id the id of the case type.
     */
    public void removeCaseTypeImpl(String id) {
        CaseType caseType = (CaseType) caseTypes.remove(id);
        if (caseType != null) {
            caseType.setCaseTypeManager(null);
            if (getBossa() != null) {
                getBossa().getResourceManager().
                    removeSubContext(caseType.getResourceRegistry());
            }
            WFNetEvents queue = new WFNetEvents();
            queue.newCaseTypeEvent(getBossa(),
                                   WFNetEvents.ID_REMOVE_CASE_TYPE,
                                   caseType);
            queue.notifyAll(getBossa());
        }
    }

    /**
     * Returns the case type with the provided id. <p>
     *
     * @param id the id of the desired case type.
     * @return The case type if it exists, <code>null</code> otherwise.
     */
    public CaseType getCaseType(String id) {
        return (CaseType) caseTypes.get(id);
    }

    /**
     * Returns all registered case types. <p>
     *
     * @return A list of all case types registered.
     */
    public List getCaseTypes() {
        ArrayList caseTypeList = new ArrayList();
        caseTypeList.addAll(caseTypes.values());
        return caseTypeList;
    }

    /**
     * Returns the list of all work items of all registered case types. <p>
     *
     * @return The list of all work itens of all registered case types.
     */
    public List getWorkItems() {
        return getWorkItems(false);
    }

    /**
     * Returns the list of all work items of all registered case types.
     * If desired, the initial work item(s) can be returned. Opening an
     * initial work item will create a new case. <p>
     *
     * @param getInitial set to <code>true</code> to get the initial work
     *                   items and to <code>false</code> to only get the
     *                   standard work items.
     * @return The list of all work itens of all registered case types.
     */
    public List getWorkItems(boolean getInitial) {
        ArrayList items = new ArrayList();
        Iterator i = caseTypes.values().iterator();
        while (i.hasNext()) {
            items.addAll(((CaseType) i.next()).getWorkItems(getInitial));
        }
        return items;
    }

    /**
     * Returns the list of all activities of all registered case types. <p>
     *
     * @return The list of all activities of all registered case types.
     */
    public List getActivities() {
        ArrayList acts = new ArrayList();
        Iterator i = caseTypes.values().iterator();
        while (i.hasNext()) {
            acts.addAll(((CaseType) i.next()).getActivities());
        }
        return acts;
    }
}
