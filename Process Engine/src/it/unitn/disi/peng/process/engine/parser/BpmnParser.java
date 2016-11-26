package it.unitn.disi.peng.process.engine.parser;

import it.unitn.disi.peng.process.engine.model.Gateway;
import it.unitn.disi.peng.process.engine.model.SubProcess;
import it.unitn.disi.peng.process.engine.model.Task;
import it.unitn.disi.peng.process.engine.service.*;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.util.Log;

public class BpmnParser {

	private static final String LOG_TAG="mpe.BpmnParser";


	/**
	 * Parse and return the first subprocess instance for bpmn file.
	 *
	 * @param in BPMN 2.0 xml stream
     * @return
     */
	public SubProcess parseSubProcess(InputStream in) throws Exception {
		XPath xPath = XPathFactory.newInstance().newXPath();
		
		HashMap<String, String> prefMap = new HashMap<String, String>() {{
		    put("ns", "http://www.omg.org/spec/BPMN/20100524/MODEL");
		    put("mpe", "http://peng.disi.unitn.it");
		}};
		SimpleNamespaceContext namespaces = new SimpleNamespaceContext(prefMap);
		xPath.setNamespaceContext(namespaces);
		
		InputSource inputSource = new InputSource(in);

		NodeList subProcesses = (NodeList) xPath.evaluate("/ns:definitions/ns:process/ns:subProcess", inputSource, XPathConstants.NODESET);
		Log.i(LOG_TAG, "subProcess:" + subProcesses.getLength());

		if(subProcesses.getLength() == 0) throw new Exception("No suprocesses found in bpmn");
		if(subProcesses.getLength() > 1) Log.w(LOG_TAG,"Many subprocesses in given BPMN : only the first one will be parsed");

		//FIXME: I know there should be no for loop but ...
		Element subProcessElement = (Element) subProcesses.item(0);
		String subProcessName = xPath.evaluate("@name", subProcessElement);
		SubProcess subProcess = new SubProcess(subProcessName);

		NodeList taskElements = (NodeList) xPath.evaluate("ns:task|ns:startEvent|ns:endEvent", subProcessElement, XPathConstants.NODESET);
		Log.i(LOG_TAG, "task:" + subProcessElement.getChildNodes().getLength() + ":" + taskElements.getLength());

		for (int j = 0; j < taskElements.getLength(); j++) {
			Element taskElement = (Element) taskElements.item(j);

			Node service = (Node) xPath.evaluate(
					"ns:extensionElements/mpe:service", taskElement,
					XPathConstants.NODE);

			Task task;

			if(service != null) {

				StringWriter writer = new StringWriter();
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.transform(new DOMSource(service), new StreamResult(writer));

				task = new Task(xPath.evaluate("@id", taskElement), xPath.evaluate("@name", taskElement), xPath.evaluate("@class", service), writer.toString(), "true".equals(xPath.evaluate("@spawned", service)));
			} else {
				task = new Task(xPath.evaluate("@id", taskElement), xPath.evaluate("@name", taskElement), null, null, true);
			}

			subProcess.addElement(task);
			Log.i("tag","taskElement.getNodeName()="+taskElement.getLocalName());
			if (taskElement.getLocalName().equals("startEvent")) {
				subProcess.setStart(task);
			}
			else if (taskElement.getLocalName().equals("endEvent")) {
				subProcess.setEnd(task);
			}
//					Log.i(LOG_TAG, "nodename:" + taskElement.getNodeName());
		}


		NodeList gatewayElements = (NodeList) xPath.evaluate("ns:exclusiveGateway", subProcessElement, XPathConstants.NODESET);
		for (int j = 0; j < gatewayElements.getLength(); j++) {
			Element gatewayElement = (Element) gatewayElements.item(j);
			Gateway gateway = parseGateway(xPath, gatewayElement);
			subProcess.addElement(gateway);
		}

		NodeList flowElements = (NodeList) xPath.evaluate("ns:sequenceFlow", subProcessElement, XPathConstants.NODESET);
		for (int j = 0; j < flowElements.getLength(); j++) {
			Element flowElement = (Element) flowElements.item(j);
			String sourceRef = xPath.evaluate("@sourceRef", flowElement);
			String targetRef = xPath.evaluate("@targetRef", flowElement);
			String condition = xPath.evaluate("ns:conditionExpression", flowElement);
			Log.i(LOG_TAG,"condition:" + condition);
			subProcess.addFlow(sourceRef, targetRef, condition);
		}

		subProcess.print();
		return subProcess;
	}
	
	public Gateway parseGateway(XPath xPath, Object object) throws Exception {
		Gateway gateway = null;

		Element gatewayElement = (Element) object;
		String id;
		String name;
		id = xPath.evaluate("@id", gatewayElement);
		name = xPath.evaluate("@name", gatewayElement);
		gateway = new Gateway(id, name);

		return gateway;
	}

}
