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

import java.util.HashMap;
import java.util.Map;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.notify.Event;
import com.bigbross.bossa.notify.NotificationQueue;
import com.bigbross.bossa.resource.Resource;

/**
 * This class holds as static constants all ids of WFNet events. It also
 * provides methods used by the WFNet classes to create and notify these
 * events. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class WFNetEvents extends NotificationQueue {

    /**
     * Constant to indicate the event of a case type registration. <p>
     *
     * This event contains the following attributes: ATTRIB_CASE_TYPE_ID <p>
     *
     * @see WFNetEvents#ATTRIB_CASE_TYPE_ID
     */
    public static final String ID_REGISTER_CASE_TYPE = "register_case_type";

    /**
     * Constant to indicate the event of a case type removal. <p>
     *
     * This event contains the following attributes: ATTRIB_CASE_TYPE_ID <p>
     *
     * @see WFNetEvents#ATTRIB_CASE_TYPE_ID
     */
    public static final String ID_REMOVE_CASE_TYPE = "remove_case_type";

    /**
     * Constant to indicate the event of opening a case. <p>
     *
     * This event contains the following attributes: ATTRIB_CASE_ID and
     * ATTRIB_CASE_TYPE_ID <p>
     *
     * @see WFNetEvents#ATTRIB_CASE_ID
     * @see WFNetEvents#ATTRIB_CASE_TYPE_ID
     */
    public static final String ID_OPEN_CASE  = "open_case";

    /**
     * Constant to indicate the event of closing a case. <p>
     *
     * This event contains the following attributes: ATTRIB_CASE_ID and
     * ATTRIB_CASE_TYPE_ID <p>
     *
     * @see WFNetEvents#ATTRIB_CASE_ID
     * @see WFNetEvents#ATTRIB_CASE_TYPE_ID
     */
    public static final String ID_CLOSE_CASE  = "close_case";

    /**
     * Constant to indicate the event of tokens being added to a place. <p>
     *
     * This event contains the following attributes: ATTRIB_PLACE_ID,
     * ATTRIB_CASE_ID and ATTRIB_CASE_TYPE_ID <p>
     *
     * @see WFNetEvents#ATTRIB_PLACE_ID
     * @see WFNetEvents#ATTRIB_CASE_ID
     * @see WFNetEvents#ATTRIB_CASE_TYPE_ID
     */
    public static final String ID_ADD_TOKENS  = "add_tokens";

    /**
     * Constant to indicate the event of tokens being removed from a place. <p>
     *
     * This event contains the following attributes: ATTRIB_PLACE_ID,
     * ATTRIB_TOKEN_NUMBER_ID, ATTRIB_CASE_ID and ATTRIB_CASE_TYPE_ID <p>
     *
     * @see WFNetEvents#ATTRIB_PLACE_ID
     * @see WFNetEvents#ATTRIB_TOKEN_NUMBER_ID
     * @see WFNetEvents#ATTRIB_CASE_ID
     * @see WFNetEvents#ATTRIB_CASE_TYPE_ID
     */
    public static final String ID_REMOVE_TOKENS  = "remove_tokens";

    /**
     * Constant to indicate the event of a specific number of tokens being
     * put in a place. <p>
     *
     * This event contains the following attributes: ATTRIB_PLACE_ID,
     * ATTRIB_TOKEN_NUMBER_ID, ATTRIB_CASE_ID and ATTRIB_CASE_TYPE_ID <p>
     *
     * @see WFNetEvents#ATTRIB_PLACE_ID
     * @see WFNetEvents#ATTRIB_TOKEN_NUMBER_ID
     * @see WFNetEvents#ATTRIB_CASE_ID
     * @see WFNetEvents#ATTRIB_CASE_TYPE_ID
     */
    public static final String ID_SET_TOKENS  = "set_tokens";

    /**
     * Constant to indicate the activation of an inactive work item. <p>
     *
     * This event contains the following attributes: ATTRIB_WORK_ITEM_ID,
     * ATTRIB_CASE_ID and ATTRIB_CASE_TYPE_ID <p>
     *
     * @see WFNetEvents#ATTRIB_WORK_ITEM_ID
     * @see WFNetEvents#ATTRIB_CASE_ID
     * @see WFNetEvents#ATTRIB_CASE_TYPE_ID
     */
    public static final String ID_WORK_ITEM_ACTIVE  = "work_item_active";

    /**
     * Constant to indicate the deactivation of an active work item. <p>
     *
     * This event contains the following attributes: ATTRIB_WORK_ITEM_ID,
     * ATTRIB_CASE_ID and ATTRIB_CASE_TYPE_ID <p>
     *
     * @see WFNetEvents#ATTRIB_WORK_ITEM_ID
     * @see WFNetEvents#ATTRIB_CASE_ID
     * @see WFNetEvents#ATTRIB_CASE_TYPE_ID
     */
    public static final String ID_WORK_ITEM_INACTIVE  = "work_item_inactive";

    /**
     * Constant to indicate the event of opening a work item. <p>
     *
     * This event contains the following attributes: ATTRIB_WORK_ITEM_ID,
     * ATTRIB_CASE_ID, ATTRIB_CASE_TYPE_ID and ATTRIB_RESOURCE_ID <p>
     *
     * @see WFNetEvents#ATTRIB_WORK_ITEM_ID
     * @see WFNetEvents#ATTRIB_CASE_ID
     * @see WFNetEvents#ATTRIB_CASE_TYPE_ID
     * @see WFNetEvents#ATTRIB_RESOURCE_ID
     */
    public static final String ID_OPEN_WORK_ITEM  = "open_work_item";

    /**
     * Constant to indicate the event of closing an activity. <p>
     *
     * This event contains the following attributes: ATTRIB_ACTIVITY_ID,
     * ATTRIB_ACTIVITY_WI_ID, ATTRIB_CASE_ID, ATTRIB_CASE_TYPE_ID and
     * ATTRIB_RESOURCE_ID <p>
     *
     * @see WFNetEvents#ATTRIB_ACTIVITY_ID
     * @see WFNetEvents#ATTRIB_ACTIVITY_WI_ID
     * @see WFNetEvents#ATTRIB_CASE_ID
     * @see WFNetEvents#ATTRIB_CASE_TYPE_ID
     * @see WFNetEvents#ATTRIB_RESOURCE_ID
     */
    public static final String ID_CLOSE_ACTIVITY  = "close_activity";

    /**
     * Constant to indicate the event of canceling an activity. <p>
     *
     * This event contains the following attributes: ATTRIB_ACTIVITY_ID,
     * ATTRIB_ACTIVITY_WI_ID, ATTRIB_CASE_ID, ATTRIB_CASE_TYPE_ID and
     * ATTRIB_RESOURCE_ID <p>
     *
     * @see WFNetEvents#ATTRIB_ACTIVITY_ID
     * @see WFNetEvents#ATTRIB_ACTIVITY_WI_ID
     * @see WFNetEvents#ATTRIB_CASE_ID
     * @see WFNetEvents#ATTRIB_CASE_TYPE_ID
     * @see WFNetEvents#ATTRIB_RESOURCE_ID
     */
    public static final String ID_CANCEL_ACTIVITY  = "cancel_activity";

    /**
     * Constant to indicate the event of a manual state change in a
     * case. <p>
     *
     * This event contains the following attributes: ATTRIB_CASE_ID and
     * ATTRIB_CASE_TYPE_ID <p>
     *
     * @see WFNetEvents#ATTRIB_CASE_ID
     * @see WFNetEvents#ATTRIB_CASE_TYPE_ID
     */
    public static final String ID_SET_STATE  = "set_state";

    /**
     * Constant to indicate the case type id attribute.
     */
    public static final String ATTRIB_CASE_TYPE_ID = "case_type_id";

    /**
     * Constant to indicate the case id attribute.
     */
    public static final String ATTRIB_CASE_ID = "case_id";

    /**
     * Constant to indicate the place id attribute.
     */
    public static final String ATTRIB_PLACE_ID = "place_id";

    /**
     * Constant to indicate the token number id attribute.
     */
    public static final String ATTRIB_TOKEN_NUMBER_ID = "token_number_id";

    /**
     * Constant to indicate the work item id attribute.
     */
    public static final String ATTRIB_WORK_ITEM_ID  = "work_item_id";

    /**
     * Constant to indicate the activity id attribute.
     */
    public static final String ATTRIB_ACTIVITY_ID = "activity_id";

    /**
     * Constant to indicate the work item id of an activity attribute.
     */
    public static final String ATTRIB_ACTIVITY_WI_ID = "activity_wi_id";

    /**
     * Constant to indicate the resource id attribute.
     */
    public static final String ATTRIB_RESOURCE_ID = "resource_id";


    /**
     * Creates a case type related event and puts it in the queue. <p>
     *
     * @param bossa the root of the bossa system.
     * @param notificationId the id of this event.
     * @param caseType the case type involved.
     */
    void newCaseTypeEvent(Bossa bossa, String notificationId,
                               CaseType caseType) {
        if (bossa != null) {
            Map attrib = new HashMap();
            attrib.put(ATTRIB_CASE_TYPE_ID, caseType.getId());
            addEvent(new Event(notificationId, Event.WFNET_EVENT, attrib,
                               bossa.getTimeSource().getTime()));
        }
    }

    /**
     * Creates a case related event and puts it in the queue. <p>
     *
     * @param bossa the root of the bossa system.
     * @param notificationId the id of this event.
     * @param caze the case involved.
     */
    void newCaseEvent(Bossa bossa, String notificationId, Case caze) {
        if (bossa != null) {
            Map attrib = new HashMap();
            attrib.put(ATTRIB_CASE_ID, Integer.toString(caze.getId()));
            attrib.put(ATTRIB_CASE_TYPE_ID, caze.getCaseType().getId());
            addEvent(new Event(notificationId, Event.WFNET_EVENT, attrib,
                               bossa.getTimeSource().getTime()));
        }
    }

    /**
     * Creates a place related event and puts it in the queue. <p>
     *
     * @param bossa the root of the bossa system.
     * @param notificationId the id of this event.
     * @param caze the case involved.
     * @param place the place involved.
     * @param amount the number of tokens involved.
     */
    void newPlaceEvent(Bossa bossa, String notificationId,
                       Case caze, Place place, int amount) {
        if (bossa != null &&
            (amount != 0 || notificationId.equals(ID_SET_TOKENS))) {
            Map attrib = new HashMap();
            attrib.put(ATTRIB_PLACE_ID, place.getId());
            attrib.put(ATTRIB_TOKEN_NUMBER_ID, Integer.toString(amount));
            attrib.put(ATTRIB_CASE_ID, Integer.toString(caze.getId()));
            attrib.put(ATTRIB_CASE_TYPE_ID, caze.getCaseType().getId());
            addEvent(new Event(notificationId, Event.WFNET_EVENT, attrib,
                               bossa.getTimeSource().getTime()));
        }
    }

    /**
     * Creates a work item related event and puts it in the queue. <p>
     *
     * @param bossa the root of the bossa system.
     * @param notificationId the id of this event.
     * @param wi the work item involved.
     * @param resource the resource involved.
     */
    void newWorkItemEvent(Bossa bossa, String notificationId,
                          WorkItem wi, Resource resource) {
        if (bossa != null) {
            Map attrib = new HashMap();
            attrib.put(ATTRIB_WORK_ITEM_ID, wi.getId());
            attrib.put(ATTRIB_CASE_ID, Integer.toString(wi.getCase().getId()));
            attrib.put(ATTRIB_CASE_TYPE_ID, wi.getCaseType().getId());
            if (resource != null) {
                attrib.put(ATTRIB_RESOURCE_ID, resource.getId());
            }
            addEvent(new Event(notificationId, Event.WFNET_EVENT, attrib,
                               bossa.getTimeSource().getTime()));
        }
    }

    /**
     * Creates an activity related event and puts it in the queue. <p>
     *
     * @param bossa the root of the bossa system.
     * @param notificationId the id of this event.
     * @param act the activity involved.
     */
    void newActivityEvent(Bossa bossa, String notificationId,
                          Activity act) {
        if (bossa != null) {
            Map attrib = new HashMap();
            attrib.put(ATTRIB_ACTIVITY_ID, Integer.toString(act.getId()));
            attrib.put(ATTRIB_ACTIVITY_WI_ID, act.getWorkItemId());
            attrib.put(ATTRIB_CASE_ID, Integer.toString(act.getCase().getId()));
            attrib.put(ATTRIB_CASE_TYPE_ID, act.getCaseType().getId());
            attrib.put(ATTRIB_RESOURCE_ID, act.getResource().getId());
            addEvent(new Event(notificationId, Event.WFNET_EVENT, attrib,
                               bossa.getTimeSource().getTime()));
        }
    }
}
