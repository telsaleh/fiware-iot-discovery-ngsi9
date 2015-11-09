/**
 * *****************************************************************************
 * Copyright (c) 2012, Institute for Pervasive Computing, ETH Zurich. All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. 2. Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. 3. Neither the name of the
 * Institute nor the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * This file is part of the Californium (Cf) CoAP framework.
 * ****************************************************************************
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.endpoint.coap;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.siemens.ct.exi.api.sax.EXIResult;
import com.siemens.ct.exi.api.sax.EXISource;
import com.siemens.ct.exi.exceptions.EXIException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.*;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class CoapRC01_Individual extends CoapResource {

    public CoapRC01_Individual(String name) {
        super(name);
    }

    @Override
    public void handleGET(CoapExchange exchange) {

        Response response = new Response(CoAP.ResponseCode.CONTENT);

        int contentType = exchange.getRequestOptions().getContentFormat();
        int acceptType = exchange.getRequestOptions().getAccept();        

//        int queryCount = exchange.getRequestOptions().getURIQueryCount();
//        List<String> queries = exchange.getRequestOptions().getUriQuery();
//        String entityId = "";
//
//        for (int i = 0; i < queryCount; i++) {
//
//            String queryStr = queries.get(i);
//            String[] querySplit = queryStr.split("=");
//            String queryName = querySplit[0];
//            String queryValue = querySplit[1];
//
//            System.out.println("query name " + i + " is: " + queryName);
//            System.out.println("query value " + i + " is: " + queryValue);
//
//            switch (queryName) {
//                case "EntityID":
//                    entityId = queryValue;
//                    break;
//                default:
//                    response.setPayload("No ID value set");
//                    exchange.respond(response);
//                    return;
//            }
//        }

        exchange.respond(response);
    }

    @Override
    public void handlePOST(CoapExchange exchange) {
        

    }


}
