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
import java.util.Date;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.TransactionWithQuery;

import com.bigbross.bossa.notify.NotificationBus;
import com.bigbross.bossa.resource.ResourceManager;
import com.bigbross.bossa.wfnet.CaseTypeManager;
import com.bigbross.bossa.work.WorkManager;

/**
 * This class represents a workflow engine in the Bossa workflow library. 
 * Use an instance of this class, created by the factory method
 * <code>createBossa</code>, to access all elements of the Bossa API. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Bossa implements Serializable {

    /**
     * This factory method creates a Bossa workflow engine. <p>
     * 
     * The engine created is persistent if a valid persistence directory is
     * provided. If the persistence directory is empty, a new Bossa
     * engine will be created. If the persistence directory contains data
     * of an already running Bossa engine, it will be restarted. <p>
     * 
     * The engine created is transient if <code>null</code> is passed as
     * the persistence directory. A transient Bossa engine can be embeded in
     * a prevalent system, being persisted with it, otherwise all operations
     * will be lost in case of system shutdown. <p>
     * 
     * @param persistDir the directory that holds or will hold the state
     *        of the created engine.
     * @return the newly created bossa engine.
     * @exception PersistenceException if an error occours starting the
     *            persistence mechanism.
     */
    public static Bossa createBossa(String persistDir)
        throws PersistenceException {
        Bossa newBossa = new Bossa();
        newBossa.setNotificationBus(new NotificationBus());
        if (persistDir != null) {
            try {
                Prevayler prevayler =
                    PrevaylerFactory.createPrevayler(newBossa, persistDir);
                newBossa = (Bossa) prevayler.prevalentSystem();
                newBossa.setPrevayler(prevayler);
                return newBossa;
            } catch (IOException e) {
                throw new PersistenceException("I/O error starting prevayler.",
                                                e);
            } catch (ClassNotFoundException e) {
                throw new PersistenceException("Reflection error in prevayler.",
                                                e);
            }
        } else {
            return newBossa;
        }
    }

    private CaseTypeManager caseTypeManager;

    private ResourceManager resourceManager;
    
    private WorkManager workManager;
    
    private NotificationBus notificationBus;

    private transient Prevayler prevayler;

    /**
     * Creates an empty Bossa workflow engine. <p>
     */
    Bossa() {
        caseTypeManager = new CaseTypeManager(this);
        resourceManager = new ResourceManager(this);
        workManager = new WorkManager(this);
        notificationBus = null;
        prevayler = null;
    }

    /**
     * Associates a notification bus with this engine instance. <p>
     * 
     * @param notificationBus the notification bus.
     */ 
    private void setNotificationBus(NotificationBus notificationBus) {
        this.notificationBus = notificationBus;
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
     * Executes a transaction using the current prevayler. <p>
     * 
     * @param transaction the transaction to be executed.
     * @return The value returned by the transaction.
     * @exception BossaException if the transaction throws an exception.
     */
    public Object execute(TransactionWithQuery transaction)
        throws BossaException {
        try {
            if (prevayler != null) {
                return prevayler.execute(transaction);
            } else {
                /* 
                 * FIXME: This is not deterministic if we are inside another
                 * prevalent system.
                 */
                return transaction.executeAndQuery(this, new Date());
            }
        } catch (BossaException e) {
            throw e;
        } catch (Exception e) {
            throw new BossaException("Unexpected exception.", e);
        }
    }

    /**
     * Writes to disk the complete object tree of this engine instance. <p>
     */
    public void takeSnapshot() throws IOException {
        prevayler.takeSnapshot();
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

    /**
     * Returns the work manager of this engine. <p>
     * 
     * @return The work manager of this engine.
     */
    public WorkManager getWorkManager() {
        return workManager;
    }

    /**
     * Returns the notification bus of this engine. <p>
     * 
     * @return The notification bus of this engine.
     */
    public NotificationBus getNotificationBus() {
        return notificationBus;
    }
}
