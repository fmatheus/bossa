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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaException;

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
     * @param the <code>CaseType</code> object containing the case type.
     * @return <code>true</code> if the case type is registered,
     *         <code>false</code> if there is already a case type
     *         registered with the same id.
     * @exception PersistenceException if an error occours when making the
     *            execution of this method persistent.
     */    
    public boolean registerCaseType(CaseType caseType)
        throws BossaException {
        WFNetCommand registerCommand = new RegisterCaseType(caseType);
        return ((Boolean) getBossa().executeCommand(registerCommand)).
               booleanValue();
    }

    /**
     * Registers a new case type in the manager. <p>
     * 
     * This method does not creates a command to the prevalent system. The
     * execution of this method will not be persistent unless it is called by
     * an appropriate command. <p>
     * 
     * @param the <code>CaseType</code> object containing the case type.
     * @return <code>true</code> if the case type is registered,
     *         <code>false</code> if there is already a case type
     *         registered with the same id.
     */    
    boolean registerCaseTypeImpl(CaseType caseType) {
        if (caseTypes.containsKey(caseType.getId())) {
            return false;
        }
        caseTypes.put(caseType.getId(), caseType);
        caseType.setCaseTypeManager(this);
        return true;
    }
    
    /**
     * Removes the case type from the manager. This operation will remove also
     * <emph>all</emph> cases of this case type.
     * 
     * @param id the id of the case type.
     * @exception PersistenceException if an error occours when making the
     *            execution of this method persistent.
     */
    public void removeCaseType(String id) throws BossaException {
        WFNetCommand removeCommand = new RemoveCaseType(id);
        getBossa().executeCommand(removeCommand);
    }

    /**
     * Removes the case type from the manager. This operation will remove also
     * <emph>all</emph> cases of this case type. <p>
     * 
     * This method does not creates a command to the prevalent system. The
     * execution of this method will not be persistent unless it is called by
     * an appropriate command. <p>
     * 
     * @param id the id of the case type.
     */
    void removeCaseTypeImpl(String id) {
        caseTypes.remove(id);
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
}
