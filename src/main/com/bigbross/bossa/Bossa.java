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

package com.bigbross.bossa;

import java.io.IOException;
import java.io.Serializable;

import org.prevayler.Command;
import org.prevayler.Prevayler;
import org.prevayler.implementation.AbstractPrevalentSystem;
import org.prevayler.implementation.SnapshotPrevayler;

import com.bigbross.bossa.resource.ResourceManager;
import com.bigbross.bossa.wfnet.CaseTypeManager;

/**
 * This class represents a workflow engine in the Bossa workflow library. 
 * Use an instance of this class, created by the factory method
 * <code>createBossa</code>, to access all elements of the Bossa API. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Bossa extends AbstractPrevalentSystem {

    /**
     * This factory method creates a Bossa workflow engine. The engine
     * created can be a new one, if the persistence directory is empty,
     * or it can incarnate an already running one, if present in the
     * persistence directory. <p>
     * 
     * @param persistDir the directory that holds or will hold the state
     *        of the created engine.
     * @return The newly created bossa engine.
     * @exception PersistenceException if an error occours starting the
     *            persistence mechanism.
     */
    public static Bossa createBossa(String persistDir)
        throws PersistenceException {
        try {
            Prevayler prevayler =
                new SnapshotPrevayler(new Bossa(), persistDir, 1);
            Bossa instance = (Bossa) prevayler.system();
            instance.setPrevayler(prevayler);
            return instance;
        } catch (IOException e) {
            throw new PersistenceException("I/O error starting prevayler.",
                                           e);
        } catch (ClassNotFoundException e) {
            throw new PersistenceException("Reflection error in prevayler.",
                                           e);
        }
    }

    private CaseTypeManager caseTypeManager;

    private ResourceManager resourceManager;

    private transient Prevayler prevayler;

    /**
     * Creates an empty Bossa workflow engine. <p>
     */
    Bossa() {
        caseTypeManager = new CaseTypeManager(this);
        resourceManager = new ResourceManager(this);
    }

    /**
     * Associates a prevayler with this engine instance. <p>
     * 
     * @param prevayler the prevayler.
     */
    private void setPrevayler(Prevayler prevayler) {
        this.prevayler = prevayler;
    }

    /**
     * Executes a command using the current prevayler. <p>
     * 
     * @param command the command to be executed.
     * @return The value returned by the command.
     * @exception PersistenceException if an error occours when making the
     *            execution of this command persistent.
     * @exception BossaException if the command throws an exception.
     */
    public Serializable executeCommand(Command command) throws BossaException {
        try {
            return prevayler.executeCommand(command);
        } catch (IOException e) {
            throw new PersistenceException("I/O error in prevayler.", e);
        } catch (BossaException e) {
            throw e;
        } catch (Exception e) {
            throw new BossaException("Unexpected exception.", e);
        }
    }

    /**
     * Writes to disk the complete object tree of this engine instance.
     * This method only works if the prevayler used
     * is a <code>SnapshotPrevayler</code>. <p>
     */
    public void takeSnapshot() throws IOException {
        if (prevayler instanceof SnapshotPrevayler) {
            ((SnapshotPrevayler) prevayler).takeSnapshot();
        }
    }

    /**
     * Returns the case type manager of this engine. <p>
     * 
     * @return The case type manager of this engine.
     */
    public CaseTypeManager getCaseTypeManager() {
        return caseTypeManager;
    }

    /**
     * Returns the resource manager of this engine. <p>
     * 
     * @return The resource manager of this engine.
     */
    public ResourceManager getResourceManager() {
        return resourceManager;
    }
}
