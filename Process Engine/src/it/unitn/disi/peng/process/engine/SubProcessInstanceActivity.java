package it.unitn.disi.peng.process.engine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
	private static final String KEY_CURRENT_ELEMENTID="KEY_CURRENT_ELEMENTID";

	public static final String EXTRA_BPMN_TASK = "it.unitn.disi.peng.process.engine.model.task.EXTRA_BPMN_TASK";
	private SubProcess subProcess;
	private SharedPreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		prefs = getPreferences(MODE_PRIVATE);

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


		if(savedInstanceState != null) {
			Log.d(LOG_TAG, "Resotre subprocess state but do nothing more as we are probably restoring to process onActivityResult");
			subProcess.setCurrentElementId(savedInstanceState.getString(KEY_CURRENT_ELEMENTID));

		}else{
			String currElementId = prefs.getString(KEY_CURRENT_ELEMENTID, null);
			if(currElementId== null) {
				Log.d(LOG_TAG, "No saved state : starting a fresh instance.");
				subProcess.executeNext(this);
			}
			else {
				Log.d(LOG_TAG, "Found saved state, recovering process.");
				subProcess.setCurrentElementId(currElementId);
				subProcess.executeCurrent(this);
			}
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CURRENT_ELEMENTID, subProcess.getCurrentElementId());
	}

	@Override
	protected void onPause() {
		super.onPause();

		SharedPreferences.Editor ed = prefs.edit();
		ed.putString(KEY_CURRENT_ELEMENTID, subProcess.getCurrentElementId());
		ed.commit();
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
		if(subProcess.getCurrentElement().isSpawned() || resultCode == RESULT_OK) subProcess.executeNext(this);
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
							//FIXME: offer an option in the menu to Reset or resume process
							//FIXME: display the process and current state ?
						}
					})
					.setIcon(android.R.drawable.ic_dialog_alert)
					.show();
		}
	}



}
