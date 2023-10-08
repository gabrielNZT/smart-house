package com.example;

import java.io.Serializable;

public class ComunicationObject implements Serializable {

	private static final long serialVersionUID = 1L;

	public String metodo;
	public String payload;
	public String response;

	public ComunicationObject(String metodo, String payload, String response) {
		super();
		this.metodo = metodo;
		this.payload = payload;
		this.response = response;
	}

	@Override
	public String toString() {
		return this.metodo + ", " + this.payload + ", " + this.response;
	}
}
