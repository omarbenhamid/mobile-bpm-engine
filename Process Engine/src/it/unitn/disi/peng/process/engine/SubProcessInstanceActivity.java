package it.unitn.disi.peng.process.engine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;

import org.w3c.dom.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import it.unitn.disi.peng.process.engine.model.SubProcess;
import it.unitn.disi.peng.process.engine.model.Task;
import it.unitn.disi.peng.process.engine.parser.BpmnParser;

public class SubProcessInstanceActivity extends Activity {
	private static final String LOG_TAG = "mpe.SubProcInstanceAct";

	public static final String EXTRA_BPMN_TASK = "it.unitn.disi.peng.process.engine.model.task.EXTRA_BPMN_TASK";
	private SubProcess subProcess;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		File SDCardRoot = Environment.getExternalStorageDirectory();
		File file = new File(SDCardRoot, "sample.bpmn");
//		File file = new File(SDCardRoot, "MDO_control_annotated.bpmn");
//		File file = new File(SDCardRoot, "MDO_control_simplest_annotated.bpmn");
//		File file = new File("MDO control_annotated.bpmn");
		InputStream in = null;

		BpmnParser parser = new BpmnParser();

		try {
			try {
				in = new FileInputStream(file);
				subProcess = parser.parseSubProcess(in);
			} finally {
				if (in != null) in.close();
			}
		}catch(Exception ex){
			throw new RuntimeException("Failed parsing subprocess!",ex);
		}

		subProcess.executeNext(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


	//Called by subprocesss ...
	public void executeTask(Task task) {

		Log.i(LOG_TAG, "Executing task: " + task.getName());
		String className = task.getClassName();

		if(className == null) {
			subProcess.executeNext(this); //Nothing to do go to auead
			return;
		}

		Intent intent = new Intent();
		intent.setClassName(this, className);
		intent.putExtra(EXTRA_BPMN_TASK, task);


		//FIXME: provide content provider to manipulate variables!?

		startActivityForResult(intent,0); //FIXME: pass in the "process instance id as second param ?"

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) subProcess.executeNext(this);
		else {
			new AlertDialog.Builder(this)
					.setTitle("Not ok process activity")
					.setMessage("Activity finished without result ok, restartit ?")
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							subProcess.executeCurrent(SubProcessInstanceActivity.this);
						}
					})
					.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// do nothing
						}
					})
					.setIcon(android.R.drawable.ic_dialog_alert)
					.show();
		}
	}


}
