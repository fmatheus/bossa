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

import org.prevayler.Prevayler;

import com.bigbross.bossa.history.Historian;
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
    
    private Historian historian;

    private NotificationBus notificationBus;
    
    private TimeSource timeSource;

    private transient Prevayler prevayler;

    /**
     * Creates an empty Bossa workflow engine. <p>
     */
    Bossa() {
        caseTypeManager = new CaseTypeManager(this);
        resourceManager = new ResourceManager(this);
        workManager = new WorkManager(this);
        historian = new Historian();
        notificationBus = null;
        timeSource = new DeterministicTimeSource();
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
     * Associates a time source with this engine instance. <p>
     * 
     * @param timeSource the time source.
     */ 
    void setTimeSource(TimeSource timeSource) {
        this.timeSource = timeSource;
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
     * @return the value returned by the transaction.
     * @exception BossaException if the transaction throws an exception.
     */
    public Object execute(BossaTransaction transaction)
        throws BossaException {
        try {
            if (prevayler != null) {
                return prevayler.execute(transaction);
            } else {
                return transaction.execute(this);
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
     * @return the case type manager of this engine.
     */
    public CaseTypeManager getCaseTypeManager() {
        return caseTypeManager;
    }

    /**
     * Returns the resource manager of this engine. <p>
     * 
     * @return the resource manager of this engine.
     */
    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    /**
     * Returns the work manager of this engine. <p>
     * 
     * @return the work manager of this engine.
     */
    public WorkManager getWorkManager() {
        return workManager;
    }

    /**
     * Returns the historian of this engine. <p>
     * 
     * @return the historian of this engine.
     */
    public Historian getHistorian() {
        return historian;
    }

    /**
     * Returns the time source of this engine. <p>
     * 
     * @return the time source of this engine.
     */
    public TimeSource getTimeSource() {
        return timeSource;
    }

    /**
     * Returns the notification bus of this engine. <p>
     * 
     * @return the notification bus of this engine.
     */
    public NotificationBus getNotificationBus() {
        return notificationBus;
    }
}
