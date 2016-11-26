package it.unitn.disi.peng.process.engine;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;

import it.unitn.disi.peng.process.engine.service.Button;
import it.unitn.disi.peng.process.engine.service.FormService;
import it.unitn.disi.peng.process.engine.service.Hidden;
import it.unitn.disi.peng.process.engine.service.Input;
import it.unitn.disi.peng.process.engine.service.Select;
import it.unitn.disi.peng.process.engine.service.Text;

/**
 * Created by omar on 26/11/16.
 */
public class FormActivity extends Activity {
    private static final String LOG_TAG = "mpe.FormActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.i(LOG_TAG,"You know what : i shall show this form : " + getIntent().getSerializableExtra(SubProcessInstanceActivity.EXTRA_BPMN_TASK).toString());

        //setResult(RESULT_OK);

        //finish();
        /*
        this.setContentView(FormService.getView(activity));

        FormService fs = new FormService();
        NodeList serviceElements = service.getChildNodes();
        for (int k = 0; k < serviceElements.getLength(); k++) {
            Node serviceElement = serviceElements.item(k);
            String serviceElementName = serviceElement.getNodeName();
            if (serviceElementName.equals("mpe:text")) {
                String id = xPath.evaluate("@id", serviceElement);
                String value = xPath.evaluate("@value", serviceElement);
                Text text = new Text(id, "", value);
                fs.addElement(text);
                Log.i(LOG_TAG, "mpe:text");
            }
            else if (serviceElementName.equals("mpe:input")) {
                String id = xPath.evaluate("@id", serviceElement);
                String type = xPath.evaluate("@type", serviceElement);
                String value = xPath.evaluate("@value", serviceElement);
                if(type.equals("text")) {
                    Input input = new Input(id, type, value);
                    fs.addElement(input);
                }
                else if(type.equals("submit")) {
                    Button button = new Button(id, type, value);
                    fs.addElement(button);
                }
                else if(type.equals("hidden")) {
                    Hidden hidden = new Hidden(id, type, value);
                    fs.addElement(hidden);
                }
                Log.i(LOG_TAG, "mpe:input");
            }
            else if (serviceElementName.equals("mpe:select")) {
                String id = xPath.evaluate("@id", serviceElement);
                NodeList optionElements = (NodeList) xPath.evaluate("mpe:option", serviceElement, XPathConstants.NODESET);
                String[] textArray = new String[optionElements.getLength()];
                String[] valueArray = new String[optionElements.getLength()];
                for (int m = 0; m < optionElements.getLength(); m++) {
                    Node optionElement = optionElements.item(m);
                    String text = optionElement.getTextContent();
                    String value = xPath.evaluate("@value", optionElement);
                    textArray[m] = text;
                    valueArray[m] = value;
                }
                Select select = new Select(id, "", "");
                select.setOptions(textArray, valueArray);
                fs.addElement(select);
            }
        }

        */
    }
}