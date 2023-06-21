package com.clases;

import static com.jcolor.Ansi.colorize;
import static com.jcolor.Attribute.*;

// Generador Laberinto (Backtracker)
// Adaptación: David López

class Laberinto {
	
	public static final byte MURO = -1;
	public static final byte ESPACIO = 0;
	public static final byte ENTRADA = -2;
	public static final byte SALIDA = -3;
	public static final byte PASO = -4;
	public static final byte ESCRUTADO = -5;
	

	private byte[][] lab;
	private byte[][][] map;
	private int ancho;
	private int alto;
	private int numThread;
	private java.util.Random rand = new java.util.Random();

	public Laberinto(int nr, int al, int an) {
		this.ancho = an;
		this.alto = al;
		this.numThread = nr;
		lab = new byte[alto][ancho];
		map = new byte[nr][alto][ancho];
	}

	private void excava(int y, int x) {
		// movimientos N,S,E,O
		final int[] upy = { 1, -1, 0, 0 };
		final int[] upx = { 0, 0, 1, -1 };

		int dir = rand.nextInt(4);
		int count = 0;
		while(count < 4) {
			final int y1 = y + upy[dir];
			final int x1 = x + upx[dir];
			final int y2 = y1 + upy[dir];
			final int x2 = x1 + upx[dir];
			if(lab[y1][x1] == MURO && lab[y2][x2] == MURO) {
				lab[y1][x1] = ESPACIO;
				lab[y2][x2] = ESPACIO;
				excava(y2, x2);
			} else {
				dir = (dir + 1) % 4;
				count += 1;
			}
		}
	}

	public void generaLab() {
		// genera un muro rodeado de espacio y con casilla
		// de entrada y salida
		for(int i = 0; i < alto; i++) {
			for(int j = 0; j < ancho; j++) {
				lab[i][j] = MURO;
			}
		}
		for(int j = 0; j < ancho; j++) {
			lab[0][j] = ESPACIO;
			lab[alto - 1][j] = ESPACIO;
		}
		for(int i = 0; i < alto; i++) {
			lab[i][0] = ESPACIO;
			lab[i][ancho - 1] = ESPACIO;
		}

		lab[2][2] = ESPACIO;
		excava(2, 2);

		lab[1][2] = ENTRADA;
		lab[alto - 2][ancho - 3] = SALIDA;
	}

	public void muestraLab() {
		for(int i = 0; i < alto; i++) {
			for(int j = 0; j < ancho; j++) {
				switch (lab[i][j]) {
				    case MURO: 
				    	System.out.print(colorize(" ", YELLOW_BACK()));
					    break;
				    case ESPACIO:
				    	System.out.print(" ");
				    	break;
				    case ENTRADA:
				    	System.out.print(colorize("E", GREEN_TEXT()));
				    	break;
				    case SALIDA:
				    	System.out.print(colorize("S", GREEN_TEXT()));
				    	break;
				}
			}
			System.out.println();
		}
	}
	
	public byte leeLab(int f, int c) {
		return lab[f][c];
	}
	
	public boolean salida(int f, int c) {
		return lab[f][c] == SALIDA;
	}

	public boolean espacio(int f, int c) {
		return lab[f][c] == ESPACIO;
	}
	
	public boolean muro(int f, int c) {
		return lab[f][c] == MURO || lab[f][c] == ENTRADA;
	}
	
	public void setLab(int f, int c, byte num) {
		lab[f][c] = num;
	}
	
	public void initMap() {
		for(int z = 0; z < numThread; z++) {
			for(int i = 0; i < alto; i++) {
				for(int j = 0; j < ancho; j++) {
					map[z][i][j] = lab[i][j];
				}	
			}
		}
	}
	
	public void setMap(int h, int f, int c, byte num) {
		map[h][f][c] = num;
	}
	
	public byte leeMap(int h, int f, int c) {
		return map[h][f][c];
	}
	
	// Busca un ESCRUTADO en el eje z, si no lo encuentra
	// devuelve el contenido de la celda en cuestión
	public byte leeZMap(int h, int f, int c) {
		int z = 0;
		while(z < this.numThread - 1 && map[z][f][c] != Laberinto.ESCRUTADO) {
			z++;
		}
		if(map[z][f][c] == Laberinto.ESCRUTADO) {
			return Laberinto.ESCRUTADO;
		} else {
			return map[h][f][c];
		}
			
	}
	
	public void muestraMap(int h) {
		for(int i = 0; i < alto; i++) {
			for(int j = 0; j < ancho; j++) {
				switch (map[h][i][j]) {
				    case MURO: 
				    	System.out.print(colorize(" ", YELLOW_BACK()));
					    break;
				    case ESPACIO:
				    	System.out.print(" ");
				    	break;
				    case ENTRADA:
				    	System.out.print(colorize("E", GREEN_TEXT()));
				    	break;
				    case SALIDA:
				    	System.out.print(colorize("S", GREEN_TEXT()));
				    	break;
				    case PASO:
				    	System.out.print(colorize("·", BRIGHT_BLACK_TEXT()));
				    	break;
				    case ESCRUTADO:
				    	System.out.print(colorize("*", BRIGHT_RED_TEXT()));
				    	break;				    	
				}
			}
			System.out.println();
		}
	}
}
