import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    // Variables globales
    static int[][] tableroLogico; // -1 = Mina, 0 = Nada, 1-8 = Cantidad minas
    static char[][] tableroVisible; // X = Tapado, ' ' = Vacio, 1-8 = Cantidad minas
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        //Declaración de variables
        int tableroSize;
        String nombre = "";
        int casillasDestapadas = 0;
        int minas;

        //Bienvenida del Juego
        nombre = pedirNombre();
        System.out.println("Hola " + nombre);
        bienvenida();
        tableroSize = pedirTamano();

        //Asignar Cantidad de minas calculada
        minas = calcularMinas(tableroSize);

        //Iniciando el juego
        inicializarTableros(tableroSize);
        calcularMinas(tableroSize);
        colocarMinas(tableroSize, minas);
        calcularAdyacentes();
        jugar(tableroSize, casillasDestapadas, minas);
    }

    //Metodo pedir nombre
    public static String pedirNombre(){
        String nombre;
        while (true){
            System.out.println("Cual es tu nombre: ");
            nombre = sc.nextLine().trim();
            return validarTexto(nombre);
        }
    }

    //Metodo para validar texto
    public static String validarTexto(String texto){
        while (true){
            if (texto.matches("[a-zA-ZÁÉÍÓÚáéíóúÑñ ]+")) {
                return texto;
            } else {
                System.out.println("Error: solo se permiten letras. Inténtalo de nuevo.");
                texto = sc.nextLine();
            }
        }
    }

    //Bienvenida al juego
    public static void bienvenida(){
        System.out.println("Bienvenido");
        System.out.println(" _______   __   __  _______  _______    ____    ___   ___  __  ____    __    ____   ________");
        System.out.println("|    _   ||  | |  ||       ||       |  |    |  |   |_|   ||  ||    |  |  |  |    |  |       |");
        System.out.println("|   |_|  ||  | |  ||    ___||    ___| |  ||  | |         ||  ||  ||  ||  | |  ||  | |    ___|");
        System.out.println("|       | |  | |  ||   |___ |   |    |  |__|  ||         ||  ||  | |  |  ||  |__|  ||   |___ ");
        System.out.println("|        ||  |_|  ||_____  ||   |    |        ||         ||  ||  |  |    ||        ||_____  |");
        System.out.println("|   |_|  ||       | _____| ||   |___ |   __   ||  ||_||  ||  ||  |   |   ||   __   | _____| |");
        System.out.println("|_______| |_______||_______||_______||__|  |__||__|   |__||__||__|    |__||__|  |__||_______|");
    }

    //Metodo para pedir el tamaño del tablero
    public static int pedirTamano(){
        String size;
        while (true){
            System.out.println("Ingresa el tamaño del tablero: ");
            size = sc.nextLine();
            return validarNumero(size);
        }
    }

    //Metodo para validar numeros
    public static int validarNumero(String numero){
        while (true){
            if (numero.matches("\\d+")) {
                return Integer.parseInt(numero);
            } else {
                System.out.println("Error: solo se permiten numeros. Inténtalo de nuevo.");
                numero = sc.nextLine();
            }
        }
    }

    //Metodo para poner coordenadas
    public static void coordenadas(){
        System.out.print("  ");
        for (int j = 0; j < tableroVisible.length; j++) {
            System.out.print(j + " ");
        }
        System.out.println();

        for (int i = 0; i < tableroVisible.length; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < tableroVisible.length; j++) {
                System.out.print(tableroVisible[i][j] + " ");
            }
            System.out.println();
        }
    }

    //Crear tableros de juego
    public static void inicializarTableros(int tableroSize) {
        tableroLogico = new int[tableroSize][tableroSize];
        tableroVisible = new char[tableroSize][tableroSize];
        for (int i = 0; i < tableroVisible.length; i++) {
            for (int j = 0; j < tableroVisible[0].length; j++) {
                tableroVisible[i][j] = 'X';
                System.out.print(tableroVisible[i][j] + " ");
            }
            System.out.println();
        }
        coordenadas();
    }

    //Calcular minas
    public static int calcularMinas(int tableroSize){

        double cMinas = (double) ((tableroSize * tableroSize) * 15) / 100;
        return (int) Math.ceil(cMinas);
    }

    //Colocar minas
    public static void colocarMinas(int tableroSize, int minas){
        Random rd = new Random();
        int cantidadMinas = minas;
        while (cantidadMinas > 0) {
            int f = rd.nextInt(tableroSize - 1);
            int c = rd.nextInt(tableroSize - 1);

            if (tableroLogico[f][c] == 0){
                tableroLogico[f][c] = -1;
                cantidadMinas--;
            }
        }
    }

    //Revisar casillas adyacentes
    public static void calcularAdyacentes(){
        for (int i = 0; i < tableroLogico.length; i++) {
            for (int j = 0; j < tableroLogico.length; j++) {
                if (tableroLogico[i][j] == 0){
                    int contador = 0;
                    for (int k = i - 1; k <= i + 1 ; k++) {
                        for (int l = j - 1; l <= j + 1; l++) {
                            if (k >= 0 && k < tableroLogico.length && l >= 0 && l < tableroLogico.length){
                                if (tableroLogico[k][l] == -1){
                                    contador++;
                                }
                            }
                        }
                    }
                    tableroLogico[i][j] = contador;
                }
            }
        }

    }

    //Mostrar el tablero actualizado
    public static void mostrarTablero(){

        for (int i = 0; i < tableroVisible.length; i++) {
            for (int j = 0; j < tableroVisible.length; j++) {
                System.out.print(tableroVisible[i][j] + " ");
            }
            System.out.println();
        }
    }

    //Revelar tablero
    public static void revelarTablero(){
        System.out.println("Posición de las minas (X)");
        for (int i = 0; i < tableroLogico.length; i++) {
            for (int j = 0; j < tableroLogico.length; j++) {
                if (tableroLogico[i][j] == -1){
                    tableroVisible[i][j] = (char) ('X');
                } else {
                    tableroVisible[i][j] = (char) ('0');
                }
                System.out.print(tableroVisible[i][j] + " ");
            }
            System.out.println();
        }
    }

    //Metodo de la interaccion con el usuario para jugar
    public static void jugar(int tableroSize, int casillasDestapadas, int minas){
        boolean juegoActivo = true;
        while (juegoActivo){
            System.out.println("Escribe la fila: ");
            String fila = sc.nextLine();
            int f = validarNumero(fila);
            System.out.println("Escribe la columna: ");
            String colum = sc.nextLine();
            int c = validarNumero(colum);

            if (f >= 0 && f < tableroSize && c >= 0 && c < tableroSize){
                if (tableroLogico[f][c] == -1){
                    System.out.println("¡Pisaste una mina!");
                    System.out.println(" _______  _______  __   __  _______           _______  __   __  _______  _________");
                    System.out.println("|       ||   _   ||  |_|  ||       |         |       ||  | |  ||       ||    _    |");
                    System.out.println("|    ___||  |_|  ||       ||    ___|         |   _   ||  | |  ||    ___||   |_|   |");
                    System.out.println("|   | __ |       ||       ||   |___   _____  |  | |  ||  |_|  ||   |___ |    __  |");
                    System.out.println("|   ||  ||       ||       ||    ___| |_____| |  |_|  | |     | |    ___||   | |  |");
                    System.out.println("|   |_| ||   _   || ||_|| ||   |___          |       |  |   |  |   |___ |   |  |  |");
                    System.out.println("|_______||__| |__||_|   |_||_______|         |_______|   |_|   |_______||___|   |__|");
                    juegoActivo = false;
                } else {
                    if (tableroVisible[f][c] == 'X'){
                        tableroVisible[f][c] = (char) (tableroLogico[f][c] + '0');
                        casillasDestapadas++;
                        if (casillasDestapadas == (tableroSize * tableroSize) - minas){
                            System.out.println("¡Haz ganado!");
                            juegoActivo = false;
                        }
                    } else {
                        System.out.println("¡Esa casilla ya esta destapada! Elige otra.");
                    }
                    if (juegoActivo){
                        mostrarTablero();
                    }
                }
                if (!juegoActivo){
                    revelarTablero();
                }
            } else {
                System.out.println("Debes ingresar un numero entre 0 y  " + (tableroSize - 1));
            }


        }
    }
}