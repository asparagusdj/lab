package com.clases;

public class Control {
	private boolean sincronizado;
	private boolean finalizado;
	private Object  mutex;
	private int hiloGanador; 
	
	public Control(Object mutex) {
		this.sincronizado = false;
		this.finalizado = false;
		this.mutex = mutex;
	}
	
	public synchronized void esperarSincronia() {
		try {
			while (!this.sincronizado) {
				wait();
			}
		} catch (InterruptedException e) {
			System.out.println("OOOPS!!!");
		}
	}
	
	public synchronized void continuarEjecucion() {
		this.sincronizado = true;
		notifyAll();
	}
	
	public void notificaFinalizado(int hg) {
		synchronized(mutex) {
	     	this.finalizado = true;		
	     	this.hiloGanador = hg;
		}
	}
	
	public int getHiloGanador() {
		return this.hiloGanador;
	}
	
	public boolean finalizar() {
		synchronized(mutex) {
			return this.finalizado;
		}
	}
}

