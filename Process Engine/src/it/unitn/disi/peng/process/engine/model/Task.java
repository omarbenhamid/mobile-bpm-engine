package it.unitn.disi.peng.process.engine.model;

import java.util.HashMap;

import it.unitn.disi.peng.process.engine.service.EmailService;
import it.unitn.disi.peng.process.engine.service.Service;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import org.w3c.dom.Node;

public class Task extends BpmnElement {

	private String className;
	private String serviceXML;


	public Task(String id, String name, String className, String serviceXML) {
		super(id, name);
		this.className = className;
		this.serviceXML = serviceXML;
	}

	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}

	public String getServiceXML() {
		return serviceXML;
	}

	@Override
	public String toString() {
		return "Task{" +
				"className='" + className + '\'' +
				", serviceXML='" + serviceXML + '\'' +
				"} " + super.toString();
	}
}
