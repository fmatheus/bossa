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

package com.bigbross.bossa.notify;

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

import com.bigbross.bossa.DataTransferException;


/**
 * This class provides a static method to convert an event list to a
 * XML encoded file. <p>
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
            for (Iterator i = events.iterator(); i.hasNext(); ) {
                Event e = (Event) i.next();
                String id = e.getId();
                String time = e.getTime().toString();
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
