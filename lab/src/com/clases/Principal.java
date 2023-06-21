/* 
    Soluci�n del laberinto cl�sico por backtracking
    
    Descripci�n
    
    Un n�mero determinado de hilos intentan resolver mediante backtracking el laberinto cl�sico.
    El programa devuelve la primera soluci�n encontrada (la m�s r�pida)

    C�digo: David L�pez

*/

package com.clases;

public class Principal {	
	public static void main(String args[]) throws InterruptedException {
		// filas y columnas han de ser impares
		int filas = 121;
		int columnas = 351;

		int nHilos = 64;
		
		if(args.length == 3) {
			filas = Integer.parseInt(args[0]);
			columnas = Integer.parseInt(args[1]);	
			nHilos = Integer.parseInt(args[2]);
		}

		Laberinto lab = new Laberinto(nHilos, filas, columnas);
		Hilo hilos[];
		
		// objeto para establecer la sincronizaci�n
		Object mutex = new Object();
		// clase para controlar la sincronizaci�n de los hilos
        Control control = new Control(mutex);

	
		// generaci�n de un laberinto aleatorio y de los mapas de recorrido
		lab.generaLab();		
		lab.initMap();
   		
		int realFilas = filas - 2;
		int realColumnas = columnas - 2;
        System.out.println("HILOS BCK");
        System.out.println("Solucion mediante Backtracking de " + nHilos + " hilos en un laberinto de " + realFilas + " * " + realColumnas);
    	System.out.println("Buscando salida...");
    	System.out.println();
    			
   	   	hilos = new Hilo[nHilos];
	    for(int i = 0; i < nHilos; i++) {
	    	hilos[i] = new Hilo(i, lab, control);
   	    	System.out.println("Preparando hilo " + i + " ...");
   	    	hilos[i].start();
   	    }
	    
   	   	System.out.println();   	    	
   	   	System.out.println("Laberinto a resolver...");   	    	
  	    lab.muestraLab();
   	    control.continuarEjecucion();
   	   	System.out.println("Procesando...");   	    	
   	   	System.out.println();   	    	
    		    
   	    // el programa espera la finalizacion de los hilos
   	    for(int i = 0; i < nHilos; i++) {
   	    	hilos[i].join();
   	    }
    	        
   	   	System.out.println();   	    	
   	    System.out.println("Finalizado !!!");
   	   	System.out.println();   	    	
   	   /* for(int z = 0; z < nHilos; z++) {
       	   	System.out.println("Hilo " + z + " ...");   	    	
       	    lab.muestraMap(z); 
       	   	System.out.println();   	    	       	    
        }*/
   	    
   	    System.out.println("HILO GANADOR: " + control.getHiloGanador());
   	    System.out.println("Resultado del ganador...");   	    
   	    lab.muestraMap(control.getHiloGanador());
	}
}
