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

package com.bigbross.bossa.history;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaException;
import com.bigbross.bossa.DataTransferException;
import com.bigbross.bossa.notify.Event;
import com.bigbross.bossa.notify.EventsXMLHelper;
import com.bigbross.bossa.resource.ResourceEvents;
import com.bigbross.bossa.wfnet.WFNetEvents;

/**
 * This class keeps the historical records of a Bossa engine. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Historian implements Serializable {
    
    private Bossa engine;

    private ArrayList history;

    /**
     * Creates a new empty historian. <p>
     * 
     * @param engine the bossa engine this historian is part.
     */
    public Historian(Bossa engine) {
        this.engine = engine;
        this.history = new ArrayList();
    }

    /**
     * Returns the bossa engine this historian is part. <p>
     * 
     * @return the bossa engine this historian is part.
     */
    Bossa getBossa() {
        return engine;
    }

    /**
     * Finds the place in history where an event happening in the provided time
     * would appear. <p>
     * 
     * @param time the time.
     * @return the position the event would appear.
     */
    private int findPlaceInHistory(Date time) {
        Event proxy = new Event(null, -1, null, time);
        int place = Collections.binarySearch(history, proxy);
        if (place < 0) {
            place = - (place + 1);
        }
        return place;
    }
    
    /**
     * Filters the events inside a time range by case, case type and
     * resource. The time range will be computed including the start date
     * and excluding the end date. <p>
     * 
     * @param start the start date.
     * @param end the end date.
     * @return the filtered events.
     */
    private List filterHistory(Date start, Date end, String caseTypeId,
                               int caseId, String resourceId) {
        int startPosition, endPosition;
        startPosition = start == null ? 0 : findPlaceInHistory(start);
        endPosition = end == null ? history.size() : findPlaceInHistory(end);
        List events = new ArrayList(Math.abs(endPosition - startPosition));
        for (int i = startPosition; i < endPosition; i++) {
            Event event = (Event) history.get(i);
            if (caseTypeId != null && ! hasCaseType(event, caseTypeId)) {
                continue;
            }
            if (caseId != -1 && caseTypeId != null &&
                ! hasCase(event, caseId)) {
                continue;
            }
            if (resourceId != null && ! hasResource(event, resourceId)) {
                continue;
            }
            events.add(event);
        }
        return events;
    }

    /**
     * Indicates if an event was related to a case type. <p> 
     * 
     * @param event the event.
     * @param caseTypeId the id of the case type.
     * @return <code>true</code> if the case type is present in the event;
     *         <code>false</code> otherwise.
     */
    private boolean hasCaseType(Event event, String caseTypeId) {
        if (event.getType() == Event.WFNET_EVENT) {
            return caseTypeId.equals(event.getAttributes().get(
                                           WFNetEvents.ATTRIB_CASE_TYPE_ID)); 
        }
        return false;
    }

    /**
     * Indicates if an event was related to a case. <p> 
     * 
     * @param event the event.
     * @param caseId the id of the case.
     * @return <code>true</code> if the case is present in the event;
     *         <code>false</code> otherwise.
     */
    private boolean hasCase(Event event, int caseId) {
        if (event.getType() == Event.WFNET_EVENT) {
            return Integer.toString(caseId).equals(event.getAttributes().get(
                                                   WFNetEvents.ATTRIB_CASE_ID)); 
        }
        return false;
    }

    /**
     * Indicates if an event was related to a resource. <p> 
     * 
     * @param event the event.
     * @param resourceId the resource id.
     * @return <code>true</code> if the resource is present in the event;
     *         <code>false</code> otherwise.
     */
    private boolean hasResource(Event event, String resourceId) {
        if (event.getType() == Event.RESOURCE_EVENT) {
            return resourceId.equals(event.getAttributes().get(
                                    ResourceEvents.ATTRIB_HOST_RESOURCE_ID)) || 
                   resourceId.equals(event.getAttributes().get(
                                    ResourceEvents.ATTRIB_RESOURCE_ID));
        }
        if (event.getType() == Event.WFNET_EVENT) {
            return resourceId.equals(event.getAttributes().get(
                                    WFNetEvents.ATTRIB_RESOURCE_ID));
        }
        return false;
    }

    /**
     * Return all events that took place until now. <p>
     * 
     * @return all events that took place until now.
     */
    public List getHistory() {
        return filterHistory(null, null, null, -1, null);
    }
    
    /**
     * Returns the events that took place after the start date, inclusive,
     * until now. <p>
     * 
     * @param start the start date.
     * @return the events that took place after the start date until now.
     */
    public List getHistory(Date start) {
        return filterHistory(start, null, null, -1, null);
    }
    
    /**
     * Returns the events that took place between the start date and
     * the end date, including the start date and excluding the end date. <p>
     * 
     * @param start the start date.
     * @param end the end date.
     * @return the events that took place between the two dates.
     */
    public List getHistory(Date start, Date end) {
        return filterHistory(start, end, null, -1, null);
    }
    
    /**
     * Return all events that took place until now and are related to a
     * case type. <p>
     * 
     * @param caseTypeId the id of the case type.
     * @return all events that took place until now related to the case type.
     */
    public List getCaseTypeHistory(String caseTypeId) {
        return filterHistory(null, null, caseTypeId, -1, null);
    }
    
    /**
     * Returns the events that took place after the start date, inclusive,
     * until now and are related to a case type. <p>
     * 
     * @param start the start date.
     * @param caseTypeId the id of the case type.
     * @return the events that took place after the start date until now
     *         related to the case type.
     */
    public List getCaseTypeHistory(Date start, String caseTypeId) {
        return filterHistory(start, null, caseTypeId, -1, null);
    }
    
    /**
     * Returns the events that took place between the start date and the
     * end date, including the start date and excluding the end date, and
     * are related to a case type. <p>
     * 
     * @param start the start date.
     * @param end the end date.
     * @param caseTypeId the id of the case type.
     * @return the events that took place between the two dates, related to
     *         the case type.
     */
    public List getCaseTypeHistory(Date start, Date end, String caseTypeId) {
        return filterHistory(start, end, caseTypeId, -1, null);
    }
    
    /**
     * Return all events that took place until now and are related to a
     * case. <p>
     * 
     * @param caseTypeId the id of the case type.
     * @param caseId the id of the case.
     * @return all events that took place util now related to the case.
     */
    public List getCaseHistory(String caseTypeId, int caseId) {
        return filterHistory(null, null, caseTypeId, caseId, null);
    }
    
    /**
     * Returns the events that took place after the start date, inclusive,
     * until now and are related to a case. <p>
     * 
     * @param start the start date.
     * @param caseTypeId the id of the case type.
     * @param caseId the id of the case.
     * @return the events that took place after the start date until now
     *         related to the case.
     */
    public List getCaseHistory(Date start, String caseTypeId, int caseId) {
        return filterHistory(start, null, caseTypeId, caseId, null);
    }
    
    /**
     * Returns the events that took place between the start date and the
     * end date, including the start date and excluding the end date, and
     * are related to a case. <p>
     * 
     * @param start the start date.
     * @param end the end date.
     * @param caseTypeId the id of the case type.
     * @param caseId the id of the case.
     * @return the events that took place between the two dates, related
     *         to the case.
     */
    public List getCaseHistory(Date start, Date end,
                               String caseTypeId, int caseId) {
        return filterHistory(start, end, caseTypeId, caseId, null);
    }
    
    /**
     * Return all events that took place until now and are related to a
     * resource. <p>
     * 
     * @param resourceId the id of the resource.
     * @return all events that took place until now related to the resource.
     */
    public List getResourceHistory(String resourceId) {
        return filterHistory(null, null, null, -1, resourceId);
    }
    
    /**
     * Returns the events that took place after the start date, inclusive,
     * until now and are related to a resource. <p>
     * 
     * @param start the start date.
     * @param resourceId the id of the resource.
     * @return the events that took place after the start date until now
     *         related to the resource.
     */
    public List getResourceHistory(Date start, String resourceId) {
        return filterHistory(start, null, null, -1, resourceId);
    }
    
    /**
     * Returns the events that took place between the start date and the end
     * date, including the start date and excluding the end date, and
     * are related to a resource. <p>
     * 
     * @param start the start date.
     * @param end the end date.
     * @param resourceId the id of the resource.
     * @return the events that took place between the two dates, related to
     *         the resource.
     */
    public List getResourceHistory(Date start, Date end, String resourceId) {
        return filterHistory(start, end, null, -1, resourceId);
    }
    
    /**
     * Adds a new event to the history. <p>
     * 
     * @param event the new event.
     */
    void newEvent(Event event) {
        /*
         * Let's see if the new event happened before the last event.
         * I'm not sure this can happen, but it won't hurt to deal with it.
         */
        int last = history.size() - 1;
        while (last >= 0 &&
               event.compareTo(history.get(last)) < 0) {
            last--;
        }
        history.add(last + 1, event);
    }

    /**
     * Removes from history all events that took place before the provided
     * date. Events that happened exactly at the provided date are not
     * removed. <p>
     * 
     * @param end the limit date for event removal.
     * @exception PersistenceException if an error occours when making the
     *            execution of this method persistent.
     */
    public void purgeHistory(Date end) throws BossaException {
        HistorianTransaction purgeTransaction = new PurgeHistory(end);
        getBossa().execute(purgeTransaction);
    }
    
    /**
     * Removes from history all events that took place before the provided
     * date. Events that happened exactly at the provided date are not
     * removed. <p>
     * 
     * This method does not create a transaction in the prevalent system. The
     * execution of this method will not be persistent unless it is called
     * inside an appropriate transaction. <p>
     * 
     * @param end the limit date for event removal.
     */
    void purgeHistoryImpl(Date end) {
        history.subList(0, findPlaceInHistory(end)).clear();
    }
    
    /**
     * Exports to a XML file all events that took place before the provided
     * date. Events that happened exactly at the provided date are not
     * exported. This method complements the <code>purgeHistory</code> method,
     * providing a way to save events before purging them. <p>
     * 
     * The format of the exported file is the same used by the
     * <code>EventsXMLHelper</code> class. This class may also be used to
     * export any event list obtained from the other historian methods. <p>
     * 
     * @param end the limit date for event removal.
     * @param file the name of the file.
     * @see Historian#purgeHistory(Date)
     * @see com.bigbross.bossa.notify.EventsXMLHelper
     */
    public void exportHistory(Date end, String file)
        throws DataTransferException {
        EventsXMLHelper.export(filterHistory(null, end, null, -1, null), file);
    }
}
