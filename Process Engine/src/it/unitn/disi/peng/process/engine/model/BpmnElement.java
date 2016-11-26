package it.unitn.disi.peng.process.engine.model;

import java.io.Serializable;

public class BpmnElement implements Serializable{
	
	private String id;
	private String name;
	private boolean spawned = false;


	BpmnElement(String id, String name, boolean spawned) {
		this.id = id;
		this.name = name;
		this.spawned = spawned;
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

	public boolean isSpawned() {
		return spawned;
	}

	@Override
	public String toString() {
		return "BpmnElement{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", spawned=" + spawned +
				'}';
	}
}
