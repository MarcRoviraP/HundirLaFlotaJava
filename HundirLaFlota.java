import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class posicions {
	public void setFila(int fila) {
		this.fila = fila;
	}

	public void setColumna(int columna) {
		this.columna = columna;
	}

	private int fila;
	private int columna;

	public posicions(int fila, int columna) {
		this.fila = fila;
		this.columna = columna;
	}

	public int getFila() {
		return fila;
	}

	public int getColumna() {
		return columna;
	}

}

public class HundirLaFlota {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int tamany, tamanyBarco, tamanyIA = 0;
		int[][] tableroPlayer, tableroIA;
		String nombre = "", veredictoPlayer = "", veredictoIA = "";

		posicions posicioReal1, posicioReal2, iniciCordIA, finCordIA;

		ArrayList<posicions> posicioTableroIA = new ArrayList<>();
		ArrayList<posicions> posicioTableroPlayer = new ArrayList<>();

		System.out.println("¿Cual es tu nombre de marniero?");
		nombre = sc.nextLine();

		tamany = 10 + 1;
		tableroIA = generarTablero(tamany);

		tableroPlayer = generarTablero(tamany);

		int minFila = 1, maxColumna = tableroPlayer.length - 1, minColumna = minFila, maxFila = maxColumna;

		ArrayList<posicions> tirsDeLaIA = new ArrayList<>();
		HashMap<Integer, Integer> cantitatBarcosPlayer = new HashMap<Integer, Integer>();

		cantitatBarcosPlayer.put(4, 1);
		cantitatBarcosPlayer.put(3, 2);
		cantitatBarcosPlayer.put(2, 3);
		cantitatBarcosPlayer.put(1, 4);
		HashMap<Integer, Integer> cantitatBarcosIA = new HashMap<Integer, Integer>();

		cantitatBarcosIA.put(4, 1);
		cantitatBarcosIA.put(3, 2);
		cantitatBarcosIA.put(2, 3);
		cantitatBarcosIA.put(1, 4);
		HashMap<Integer, Integer> posicionsTotals = new HashMap<Integer, Integer>();

		posicionsTotals.put(4, 4);
		posicionsTotals.put(3, 6);
		posicionsTotals.put(2, 6);
		posicionsTotals.put(1, 4);
		HashMap<Integer, Integer> cantitatBarcos = new HashMap<Integer, Integer>();

		cantitatBarcos.put(4, 1);
		cantitatBarcos.put(3, 2);
		cantitatBarcos.put(2, 3);
		cantitatBarcos.put(1, 4);

		imprimirTablero(tableroPlayer, false);

		imprimirInstruccionesBarcos();

		// Posicionar player
		for (int i = 0; i < 10;) {

			posicioReal1 = transformarCordsUsuariANums(sc.next());

			if (validarCoords(posicioReal1, tableroPlayer)) {
				System.out.println("Vuelve a itroducir la posicion");
				continue;
			}

			posicioReal2 = transformarCordsUsuariANums(sc.next());

			if (validarCoords(posicioReal2, tableroPlayer)) {
				System.out.println("Vuelve a itroducir la posicion");
				continue;
			}
			if (comprobarDireccio(posicioReal1, posicioReal2)) {
				tamanyBarco = calcularTamanyBarco(posicioReal1, posicioReal2);
				if (tamanyBarco >= 1 && tamanyBarco <= 4) {
					if (cantitatBarcos.get(tamanyBarco) > 0) {
						if (posicionsQueOcupa(tableroPlayer, posicioReal1, posicioReal2)) {
							tableroPlayer = rellenarTablero(tableroPlayer, posicioReal1, posicioReal2,
									posicioTableroPlayer);
							cantitatBarcos.put(tamanyBarco, cantitatBarcos.get(tamanyBarco) - 1);
							i++;
							System.out.println("El barco se ha añadido a tu flota: " + "(" + i + "/10)");
							imprimirTablero(tableroPlayer, false);
						} else {
							System.out.println("La posicion ya esta ocupada");
						}
					} else {
						System.out.println("No te quedan barcos de tamaño " + tamanyBarco + " en la flota");
					}
				} else {
					imprimirInstruccionesBarcos();
				}
			} else {
				System.out.println("El barco no es válido, recuerda: ");
				imprimirInstruccionesBarcos();
			}
		}

		// Posicionar IA
		for (int i = 0; i <= 9;) {
			if (i == 0) {
				tamanyIA = 3;
			} else if (i >= 1 && i <= 2) {
				tamanyIA = 2;
			} else if (i >= 3 && i <= 5) {
				tamanyIA = 1;
			} else if (i >= 6 && i <= 9) {
				tamanyIA = 0;
			}

			iniciCordIA = inciCordIA(tableroIA);
			finCordIA = finCordIA(tableroIA, tamanyIA, iniciCordIA);

			if (posicionsQueOcupa(tableroIA, iniciCordIA, finCordIA)) {
				tableroIA = rellenarTablero(tableroIA, iniciCordIA, finCordIA, posicioTableroIA);
				i++;

			}
		}
		System.out.println("Ahora dispara a tu enemigo!!");

		for (int i = 0; i <= 100; i++) {

			posicions tirUsuari = transformarCordsUsuariANums(sc.next());
			if (validarCoords(tirUsuari, tableroPlayer)) {
				System.out.println("Vuelve a disparar");
				continue;
			}

			boolean tocado = false;
			int longitudBarcoTocado = 0;

			for (posicions posicio : posicioTableroIA) {
				if (posicio.getFila() == tirUsuari.getFila() && posicio.getColumna() == tirUsuari.getColumna()) {
					veredictoPlayer = "TOCADO";
					tocado = true;

					break;
				}
			}

			if (tocado) {

				tableroIA[tirUsuari.getColumna()][tirUsuari.getFila()] = -3;
				// Calcular la longitud del barco tocado
				posicions limites = sacarLimites(tirUsuari, posicioTableroIA);
				longitudBarcoTocado = calcularLongitudBarcoTocado(tirUsuari, posicioTableroIA);

				if (comprovarBarcoHundido(posicioTableroIA, longitudBarcoTocado, tirUsuari, limites)) {

					if (comprovarLosBarcosHundidos(cantitatBarcosIA, longitudBarcoTocado)) {
						System.out.println("GANA " + nombre);
						break;
					} else {

						veredictoPlayer = "HUNDIDO!!!!";
					}
				}

			} else {
				veredictoPlayer = "AGUA º-º";
				tableroIA[tirUsuari.getColumna()][tirUsuari.getFila()] = -2;

			}

			// Tir ia
			tocado = false;
			longitudBarcoTocado = 0;
			posicions tirIA = tirIA(tableroIA, minFila, maxFila, minColumna, maxColumna, tirsDeLaIA);

			for (posicions posicio : posicioTableroPlayer) {
				if (posicio.getFila() == tirIA.getFila() && posicio.getColumna() == tirIA.getColumna()) {

					veredictoIA = "TOCADO";
					tocado = true;

					break;
				}
			}

			if (tocado) {

				tableroPlayer[tirIA.getColumna()][tirIA.getFila()] = -3;

				minFila = tirIA.getFila() - 1;
				maxFila = tirIA.getFila() + 1;
				minColumna = tirIA.getColumna() - 1;
				maxColumna = tirIA.getColumna() + 1;

				if (minFila <= 0) {

					minFila++;
				}
				if (maxFila >= tableroPlayer.length) {
					maxFila--;
				}
				if (minColumna <= 0) {

					minColumna++;
				}
				if (maxFila >= tableroPlayer.length) {
					maxColumna--;
				}

				posicions limites = sacarLimites(tirIA, posicioTableroPlayer);
				longitudBarcoTocado = calcularLongitudBarcoTocado(tirIA, posicioTableroPlayer);

				if (comprovarBarcoHundido(posicioTableroPlayer, longitudBarcoTocado, tirUsuari, limites)) {

					if (comprovarLosBarcosHundidos(cantitatBarcosPlayer, longitudBarcoTocado)) {
						System.out.println("GANA IA");
						break;
					} else {

						veredictoIA = "HUNDIDO";
					}
				}

			} else {
				minFila = 1;
				maxFila = tableroPlayer.length - 1;
				minColumna = 1;
				maxColumna = tableroPlayer.length - 1;

				veredictoIA = "AGUA º-º";
				tableroPlayer[tirIA.getColumna()][tirIA.getFila()] = -2;

			}

			System.out.println("          __TABLERO " + nombre + "__");
			System.out.println();
			imprimirTablero(tableroPlayer, false);

			System.out.println(veredictoIA);

			System.out.println("          __Tablero IA__");
			System.out.println();
			imprimirTablero(tableroIA, true);
			System.out.println(veredictoPlayer);

			for (int j = 0; j < 15; j++) {
				System.out.println();
			}
		}
		sc.close();
	}

	public static posicions tirIA(int[][] tablero, int minFila, int maxFila, int minColumna, int maxColumna,
			ArrayList<posicions> tirs) {

		posicions posicio = new posicions(0, 0);
		boolean validar;
		int cont = 0;

		while (true) {

			cont++;

			if (cont == 100) {
				minFila = 1;
				maxFila = tablero.length - 1;
				minColumna = 1;
				maxColumna = tablero.length - 1;
			}
			posicio.setFila((int) (Math.random() * (maxFila - minFila + 1) + minFila));
			posicio.setColumna((int) (Math.random() * (maxColumna - minColumna + 1) + minColumna));

			validar = false;

			for (posicions tiros : tirs) {
				if (posicio.getFila() == tiros.getFila() && posicio.getColumna() == tiros.getColumna()) {
					validar = true;
					break;
				}
			}

			if (!validar) {
				tirs.add(posicio);
				break;
			}
		}

		return posicio;
	}

	public static boolean comprovarLosBarcosHundidos(HashMap<Integer, Integer> barcos, int longitud) {
		boolean valor = false;
		barcos.put(longitud, barcos.get(longitud) - 1);

		if (barcos.get(1) == 0 && barcos.get(2) == 0 && barcos.get(3) == 0 && barcos.get(4) == 0) {
			valor = true;
		}
		return valor;
	}

	public static posicions sacarLimites(posicions tirUsuari, ArrayList<posicions> posicioTableroIA) {
		int fila = tirUsuari.getFila();
		int columna = tirUsuari.getColumna();
		// Buscar la posición del disparo en la lista de posiciones del tablero de la IA
		int inicio = -1;
		int fin = -1;
		posicions limits = new posicions(-1, -1);
		for (int i = 0; i < posicioTableroIA.size(); i++) {
			posicions posicio = posicioTableroIA.get(i);
			if (posicio.getFila() == fila && posicio.getColumna() == columna && posicio.getColumna() != -2) {
				// Encontrar el inicio y fin del barco en el tablero
				inicio = i;
				while (inicio > 0 && posicioTableroIA.get(inicio - 1).getColumna() != -1) {
					inicio--;
				}

				fin = i;
				while (fin < posicioTableroIA.size() - 1 && posicioTableroIA.get(fin + 1).getColumna() != -1) {
					fin++;
				}

			}
		}
		limits.setFila(inicio);
		limits.setColumna(fin);
		return limits;
	}

	public static boolean comprovarBarcoHundido(ArrayList<posicions> posicioTableroIA, int longitud,
			posicions tirUsuari, posicions limites) {

		int cont = 0;

		for (int j = limites.getFila(); j <= limites.getColumna(); j++) {

			if (posicioTableroIA.get(j).getColumna() == -2) {
				cont++;
			}
		}

		return cont == longitud;
	}

	public static int calcularLongitudBarcoTocado(posicions tirUsuari, ArrayList<posicions> posicioTableroIA) {
		int fila = tirUsuari.getFila();
		int columna = tirUsuari.getColumna();

		// Buscar la posición del disparo en la lista de posiciones del tablero de la IA
		int inicio = -1;
		int fin = -1;
		int p = 0;
		for (int i = 0; i < posicioTableroIA.size(); i++) {
			posicions posicio = posicioTableroIA.get(i);
			if (posicio.getFila() == fila && posicio.getColumna() == columna && posicio.getColumna() != -2) {
				// Encontrar el inicio y fin del barco en el tablero
				inicio = i;
				while (inicio > 0 && posicioTableroIA.get(inicio - 1).getColumna() != -1) {
					inicio--;
				}

				fin = i;
				while (fin < posicioTableroIA.size() - 1 && posicioTableroIA.get(fin + 1).getColumna() != -1) {
					fin++;
				}

				for (posicions posicions : posicioTableroIA) {

					if (posicions.getColumna() == tirUsuari.getColumna()
							&& posicions.getFila() == tirUsuari.getFila()) {
						posicioTableroIA.get(p).setColumna(-2);
						break;
					}
					p++;
				}

				// Calcular la longitud del barco
				return fin - inicio + 1;
			}
		}

		return 0;
	}

	public static boolean validarCoords(posicions valor, int[][] tablero) {

		boolean value = true;

		if ((valor.getFila() > 0 && valor.getFila() < tablero.length)
				&& (valor.getColumna() > 0 && valor.getColumna() < tablero.length)) {

			value = false;
		}

		return value;
	}

	public static posicions finCordIA(int[][] tablero, int tamany, posicions inici) {

		int random = (int) Math.round(Math.random());
		int[] fin = new int[2];
		int valor;
		posicions finl;
		fin[0] = -1;
		fin[1] = -1;
		while ((fin[0] > tablero.length - 1 || fin[0] <= 0) || (fin[1] > tablero.length - 1 || fin[1] <= 0)) {

			int random2 = (int) Math.round(Math.random());
			fin[0] = -1;
			fin[1] = -1;

			valor = random2 == 0 ? -tamany : +tamany;

			if (random == 0) {

				fin[0] = inici.getFila();
				fin[1] = inici.getColumna() + valor;

			} else {
				fin[0] = inici.getFila() + valor;
				fin[1] = inici.getColumna();

			}

		}
		finl = new posicions(fin[0], fin[1]);

		return finl;

	}

	public static posicions inciCordIA(int[][] tablero) {
		posicions posicio = new posicions(0, 0);

		int min = 1;
		int max = tablero.length - 1;

		posicio.setFila((int) (Math.random() * (max - min + 1) + min));
		posicio.setColumna((int) (Math.random() * (max - min + 1) + min));

		return posicio;
	}

	public static int[][] rellenarTablero(int[][] tablero, posicions inici, posicions fin,
			ArrayList<posicions> posicio) {
		int minFila = Math.min(inici.getFila(), fin.getFila());
		int maxFila = Math.max(inici.getFila(), fin.getFila());
		int minColumna = Math.min(inici.getColumna(), fin.getColumna());
		int maxColumna = Math.max(inici.getColumna(), fin.getColumna());

		for (int fila = minFila; fila <= maxFila; fila++) {
			for (int columna = minColumna; columna <= maxColumna; columna++) {
				tablero[columna][fila] = 1;
				posicio.add(new posicions(fila, columna));
			}
		}

		posicio.add(new posicions(-1, -1));

		return tablero;
	}

	public static boolean posicionsQueOcupa(int[][] tablero, posicions inici, posicions fin) {

		int minFila = Math.min(inici.getFila(), fin.getFila());
		int maxFila = Math.max(inici.getFila(), fin.getFila());
		int minColumna = Math.min(inici.getColumna(), fin.getColumna());
		int maxColumna = Math.max(inici.getColumna(), fin.getColumna());

		for (int fila = minFila; fila <= maxFila; fila++) {
			for (int columna = minColumna; columna <= maxColumna; columna++) {
				if (tablero[columna][fila] == 1) {
					return false;
				}
			}
		}
		return true;
	}

	// Transformar cordeandes a char i recorrer diferenciant si es digit o lletra
	public static posicions transformarCordsUsuariANums(String cord) {

		posicions posicio;
		int[] coords = new int[2];
		String almacenarNums = "";

		for (int j = 0; j < cord.length(); j++) {

			char at = cord.charAt(j);

			if (Character.isLetter(at)) {
				at = Character.toUpperCase(at);
				coords[0] = (char) at - 65;
			}
			if (Character.isDigit(at)) {

				almacenarNums += at;
			}

		}
		if (almacenarNums.equals("")) {
			almacenarNums = "0";
		}
		coords[1] = Integer.parseInt(almacenarNums);

		posicio = new posicions(coords[0] + 1, coords[1]);
		return posicio;
	}

	public static int calcularTamanyBarco(posicions inici, posicions fin) {

		int ancho, largo;

		ancho = Math.abs(inici.getFila() - fin.getFila());
		largo = Math.abs(inici.getColumna() - fin.getColumna());

		return Math.max(ancho, largo) + 1;

	}

	public static boolean comprobarDireccio(posicions inici, posicions fin) {
		boolean valor;

		if (inici.getFila() == fin.getFila() || inici.getColumna() == fin.getColumna()) {
			valor = true;
		} else {
			valor = false;
		}

		return valor;

	}

	public static void imprimirInstruccionesBarcos() {

		System.out.println("Estos son los posibles tamaños de los barcos: ");

		System.out.println("┌───┬───┬───┬───┐");
		System.out.println("│   │   │   │   │");
		System.out.println("└───┴───┴───┴───┘");
		System.out.println("┌───┬───┬───┐  ┌───┬───┬───┐");
		System.out.println("│   │   │   │  │   │   │   │");
		System.out.println("└───┴───┴───┘  └───┴───┴───┘");

		System.out.println("┌───┬───┐  ┌───┬───┐  ┌───┬───┐");
		System.out.println("│   │   │  │   │   │  │   │   │");
		System.out.println("└───┴───┘  └───┴───┘  └───┴───┘");
		System.out.println("┌───┐  ┌───┐  ┌───┐  ┌───┐");
		System.out.println("│   │  │   │  │   │  │   │");
		System.out.println("└───┘  └───┘  └───┘  └───┘");

		System.out.println("Introduce la posicion del barco (Ejempl: a1 a1)");
	}

	public static void imprimirTablero(int[][] tablero, boolean valor) {
		char letra;

		String color = "\u001B[37m";
		if (valor) {
			color = "\u001B[34m";
		}
		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero.length; j++) {
				letra = (char) (64 + j);

				if (i == 0 && j == 0) {
					System.out.printf("%3s", " ");
				}

				if (i > 0 || j > 0) {
					if (j == 0) {
						System.out.printf("%s%2d%s", " ", i, " ");
					}
					if (i == 0) {
						System.out.printf("%3s", letra);
					}
				}

				if (i > 0 && j > 0) {
					if (tablero[i][j] == 1) {
						// Barco sin tocar (gris)
						System.out.print(color + "███" + "\u001B[0m");
					} else if (tablero[i][j] == 0) {
						// Agua (azul)
						System.out.print("\u001B[34m" + "███" + "\u001B[0m");
					} else if (tablero[i][j] == -3) {
						// Barco tocado (amarillo)
						System.out.print("\u001B[33m" + "███" + "\u001B[0m");
					} else if (tablero[i][j] == -2) {
						System.out.print("\u001B[94m" + "███" + "\u001B[0m");

					}
				}
			}
			System.out.println();
		}
	}

	public static int[][] generarTablero(int tamany) {

		int[][] tablero = new int[tamany][tamany];

		return tablero;
	}

}
