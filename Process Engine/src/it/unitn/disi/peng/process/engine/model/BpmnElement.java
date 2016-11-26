package it.unitn.disi.peng.process.engine.model;

import java.io.Serializable;

public class BpmnElement implements Serializable{
	
	private String id;
	private String name;
	
	BpmnElement(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "BpmnElement{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
