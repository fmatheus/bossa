/*
 * Bossa Workflow System
 *
 * $Id$
 *
 * Copyright (C) 2004 OpenBR Sistemas S/C Ltda.
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

package com.bigbross.bossa.io;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.bigbross.bossa.notify.Event;


/**
 * This class provides a static method to convert an event list to a
 * XML encoded file. <p>
 *
 * The XML encoding is as follows:
 * <pre>&lt;events>
 *  &lt;event>
 *   &lt;id>open_work_item&lt;/id>
 *   &lt;time>1071696763305&lt;/time>
 *   &lt;attribute id="case_type_id">purchase&lt;/attribute>
 *   &lt;attribute id="resource_id">joedoe&lt;/attribute>
 *   &lt;attribute id="case_id">1&lt;/attribute>
 *   &lt;attribute id="work_item_id">check_price&lt;/attribute>
 *  &lt;/event>
 * &lt;/events></pre>
 *
 * The <code>events</code> element is used to group a list of events.
 * The <code>event</code> element represents a single event and it is
 * composed by a single <code>id</code> element, a single <code>time</code>
 * element and zero or more <code>attribute</code> elements. <p>
 *
 * The <code>id</code> element contains the id of the event. The
 * <code>time</code> element contains the time of the event as the
 * number of milliseconds since January 1, 1970, 00:00:00 GMT. Each
 * <code>attribute</code> element represents one attribute mapping of
 * the event. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class EventsXMLHelper {

    /**
     * Converts the provided event list to a XML file. If the file exists,
     * it will be overwritten. <p>
     *
     * @param events the events to be exported.
     * @param file the name of the file.
     * @throws DataTransferException if an error happens during the export.
     */
    public static void export(List events, String file)
        throws DataTransferException {
        try {
            SAXTransformerFactory factory =
                (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            TransformerHandler handler = factory.newTransformerHandler();
            Transformer transformer = handler.getTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            OutputStream out =
                new BufferedOutputStream(new FileOutputStream(file));
            StreamResult streamResult = new StreamResult(out);
            handler.setResult(streamResult);

            AttributesImpl atb = new AttributesImpl();
            handler.startDocument();
            handler.startElement("", "", "events", atb);
            for (Iterator i = events.iterator(); i.hasNext(); ) {
                Event e = (Event) i.next();
                String id = e.getId();
                String time = Long.toString(e.getTime().getTime());
                atb.clear();
                handler.startElement("", "", "event", atb);
                handler.startElement("", "", "id", atb);
                handler.characters(id.toCharArray(), 0, id.length());
                handler.endElement("", "", "id");
                handler.startElement("", "", "time", atb);
                handler.characters(time.toCharArray(), 0, time.length());
                handler.endElement("", "", "time");
                for (Iterator j = e.getAttributes().entrySet().iterator();
                     j.hasNext();) {
                    Map.Entry entry = (Map.Entry) j.next();
                    String propId = (String) entry.getKey();
                    String propValue = (String) entry.getValue();
                    atb.clear();
                    atb.addAttribute("", "", "id", "CDATA", propId);
                    handler.startElement("", "", "attribute", atb);
                    handler.characters(propValue.toCharArray(), 0,
                                       propValue.length());
                    handler.endElement("", "", "attribute");
                }
                handler.endElement("", "", "event");
            }
            handler.endElement("", "", "events");
            handler.endDocument();
            out.close();
        } catch (FileNotFoundException e) {
            throw new DataTransferException("File not found.", e);
        } catch (IOException e) {
            throw new DataTransferException("I/O error.", e);
        } catch (TransformerConfigurationException e) {
            throw new DataTransferException("Unable to start XML exporter.", e);
        } catch (SAXException e) {
            throw new DataTransferException("Error creating XML.", e);
        }
    }
}
