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
import org.prevayler.TransactionWithQuery;

import com.bigbross.bossa.notify.NotificationBus;
import com.bigbross.bossa.resource.ResourceManager;
import com.bigbross.bossa.wfnet.CaseTypeManager;
import com.bigbross.bossa.work.WorkManager;

/**
 * This class represents a workflow engine in the Bossa workflow library. 
 * Use an instance of this class, created by the factory class
 * <code>BossaFactory</code>, to access all elements of the Bossa API. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 * @see com.bigbross.bossa.BossaFactory
 */
public class Bossa implements Serializable {

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
    void setNotificationBus(NotificationBus notificationBus) {
        this.notificationBus = notificationBus;
    }

    /**
     * Associates a prevayler with this engine instance. <p>
     * 
     * @param prevayler the prevayler.
     */
    void setPrevayler(Prevayler prevayler) {
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
