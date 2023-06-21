/* 
    Soluci�n del laberinto cl�sico por backtracking
    
    Descripci�n
    
    Clase Hilo que resuelve el problema del laberinto.

    C�digo: David L�pez

*/

package com.clases;

public class Hilo extends Thread {
	
	private Laberinto lab;
	private int threadNum;
	private Control ctrl;
	private java.util.Random rand = new java.util.Random();
	
	public Hilo(int threadNum, Laberinto l, Control c) {
		this.threadNum = threadNum;
		this.lab = l;		
		this.ctrl = c;
	}
	
	public void run() {
		// espero a que el programa principal me de sincron�a 
		ctrl.esperarSincronia();

        lab.setMap(this.threadNum, 2, 2, Laberinto.PASO); 	
		try {
			recorreLab(2,2);
		} catch (InterruptedException e) {
			this.interrupt();
			System.out.println("Interrumpido Hilo " + this.threadNum);
		}
	}
	
	private void recorreLab(int y, int x) throws InterruptedException {
		if(!lab.salida(y, x)) {
			// movimientos N,S,E,O
			final int[] upy = { 1, -1, 0, 0 };			
			final int[] upx = { 0, 0, 1, -1 };

			int dir = rand.nextInt(4);
			int count = 0;
			while(count < 4 && !lab.salida(y, x) && !ctrl.finalizar()) {
				// desmarcar s�lo para TEST
				//lab.muestraMap(this.threadNum);
				sleep(1);
				// compruebo antes de mover
		        if(ctrl.finalizar()) {
		        	// workaround: this.interrupt s�lo me funciona en el m�todo run
					throw new InterruptedException();
				}

				final int y1 = y + upy[dir];
				final int x1 = x + upx[dir];
				/* Recorro si no es muro (#), ni ha pasado (*) y no est� escrutado (�)
                  
				   LeeZMap -> No pasa si encuentra un ESCRUTADO en la casilla de cualquier hilo
				              en todo el eje z (en vez del mapa del hilo actual). Es una mejora de la 
				              heur�stica sin que afecte apenas del rendimiento (+ complex). 
				              Con �sto se consigue que el hilo actual no pase por caminos ya marcados 
				              por otros hilos que no tengan salida.
				
				   Se puede utilizar las dos formas, la primera sin tener en cuenta el recorrido del
				   resto de hilos y la segunda que permite saber si esa casilla pertenece a un
				   camino sin salida.
				    
				   forma 1 -> lab.leeMap(this.threadNum, y1, x1) != Laberinto.ESCRUTADO 
				   forma 2 -> lab.leeZMap(this.threadNum, y1, x1) != Laberinto.ESCRUTADO */
				if(!lab.muro(y1, x1) && lab.leeMap(this.threadNum, y1, x1) != Laberinto.PASO && lab.leeZMap(this.threadNum, y1, x1) != Laberinto.ESCRUTADO) {			        
					lab.setMap(this.threadNum, y1, x1, Laberinto.PASO); 	
			        recorreLab(y1, x1);
				} else {
					dir = (dir + 1) % 4;
					count += 1;					
				}					
			}
			/* Aqu� el programa no tiene m�s opciones y ha de dar vuelta atr�s (descarga)
			   por lo que marco la casilla como escrutada. La recursividad me devuelve
			   a la casilla anterior e intentar� salir a trav�s de la siguiente
			   opci�n, si la hay, sino otra vez descargar� y volver� a intentar. */
			if (!ctrl.finalizar()) {
		        lab.setMap(this.threadNum, y, x, Laberinto.ESCRUTADO); 				
			}
		} else {
			// salida encontrada, se notifica al resto
			if(!ctrl.finalizar()) {
				ctrl.notificaFinalizado(this.threadNum);
			} 
		}

	}
}