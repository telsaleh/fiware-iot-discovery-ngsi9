/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.endpoint.coap;

import com.siemens.ct.exi.api.sax.EXIResult;
import com.siemens.ct.exi.exceptions.EXIException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author te0003
 */
public class ExiTranscoder {
    
    //    protected void decode(XMLReader exiReader, String exiLocation)
//			throws SAXException, IOException, TransformerException {
//
//		TransformerFactory tf = TransformerFactory.newInstance();
//		Transformer transformer = tf.newTransformer();
//
//		InputStream exiIS = new FileInputStream(exiLocation);
//		SAXSource exiSource = new SAXSource(new InputSource(exiIS));
//		exiSource.setXMLReader(exiReader);
//
//		OutputStream os = new FileOutputStream(exiLocation + XML_EXTENSION);
//		transformer.transform(exiSource, new StreamResult(os));
//		os.close();
//	}
    protected byte[] codeSchemaLess(String result) {

        ByteArrayOutputStream exiOS2 = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(exiOS2);
//        printStream.print("String");
        printStream.print(result);
        printStream.close();
        EXIResult exiResult;
        try {
            exiResult = new EXIResult();
            exiResult.setOutputStream(printStream);
            encode(exiResult.getHandler());
        } catch (IOException | EXIException | SAXException ex) {
            Logger.getLogger(CoapR01_ContextRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] exiMessage = exiOS2.toByteArray();
        return exiMessage;
    }

    protected void encode(ContentHandler ch) throws SAXException, IOException {
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        xmlReader.setContentHandler(ch);
    }

    private static double round(double d, int n) {
        double factor = Math.pow(10, n);
        return Math.round(d * factor) / factor;
    }
    
}
