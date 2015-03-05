package Source;

import java.text.DecimalFormat;

public class RedesNeuronales {

    public RedesNeuronales() {

    }

    private int contPatrones = 0;//Variable para indicar el No de patrones ingresados
//Matriz para capturar el patrón en tiempo real
    private double matriz[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double matrizSalida[] = new double[35];
    private int ID = 0;

    private double matPatrones[][] = new double[26][35];//Array temporal con los patrones
    private String nomPatrones[] = new String[26];//Array String con los nombres de los patrones
    private double matObjetivos[][] = new double[26][26];//Array con los valores objetivo

    private Red miRedJA = null;

    private Matriz entrada;

//MATRICES (OBJETOS)
    private Matriz patronesM;
    private Matriz objetivosM;

    public void iniObjetivos() {
        //INICIAR VECTOR OBJETIVO
        int pos = 0;//variable para inicializar la matriz objetivo
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < 26; j++) {
                if (j == pos) {
                    matObjetivos[i][j] = 1;
                } else {
                    matObjetivos[i][j] = 0;
                }

            }
            pos++;
        }

    }

    public void agregarPatron(String nombre) {
        /*se captura el array de patrones temporal(1x35) y se guarda en el array
         * final de patrones en la fila=contPatrones*/
        for (int j = 0; j < 35; j++) {
            matPatrones[contPatrones][j] = matriz[j];
        }
        nomPatrones[contPatrones] = nombre;//Se guarda el nombre del patrón

        //Se pasa el valor del patrón capturado a String para visualizarlo en debug
        StringBuffer mat = new StringBuffer();
        mat.append(matriz[0]);
        for (int i = 1; i < 35; i++) {
            mat.append(" ");
            mat.append(matriz[i]);
        }
        String matS = mat.toString();

        System.out.println("¡Capturado patrón " + String.valueOf(contPatrones + 1) + " con éxito!\n");
        System.out.println("Nombre: \"" + nomPatrones[contPatrones] + "\" valor:\n");
        System.out.println("[" + matS + "]\n");

        //se incremente al contador de patrones y se actualizan las etiquetas
        contPatrones++;

        //SI SE HA ALCANZADO EL NÚMERO MÁXIMO DE PATRONES:
        if (contPatrones == 26) {
            //Deshabilito elementos gráficos y cambio el estado

            patronesM = new Matriz(matPatrones);//Se inicializa la matriz de patrones
            patronesM = Matriz.transponer(patronesM);//Se transpone => 35x10
            objetivosM = new Matriz(matObjetivos);//Se inicializa la matriz objetivos
            objetivosM = Matriz.transponer(objetivosM);//Se transpone => 10x10

            miRedJA = new Red(patronesM, objetivosM);

            System.out.println("\nMatriz de objetivos [" + objetivosM.getFilString() + "x" + objetivosM.getColString() + "]:\n");
            System.out.println(objetivosM.toStringM());

            System.out.println("\nMatriz de patrones [" + patronesM.getFilString() + "x" + patronesM.getColString() + "]:\n");
            System.out.println(patronesM.toStringM());

            //guardar las matrices para matlab
            System.out.println("\nMODO DE EJECUCIÓN\n");
            System.out.println("Para comenzar a utilizar la red neuronal calculada en JAVA presione primero \"Entrenar\" y luego\n ");
            System.out.println("\"CalcularJ\" bajo el título Java de ésta interfaz \n");
            //nuevos elementos gráficos
        }

        //Reiniciar vector de entrada
        for (int j = 0; j <= 34; j++) {
            matriz[j] = 0;

        }

        StringBuffer mat1 = new StringBuffer();
        mat1.append(matriz[0]);
        for (int i = 1; i < matriz.length; i++) {
            mat1.append(" ");
            mat1.append(matriz[i]);
        }

        String matS1 = mat1.toString();
        System.out.println("Vector de entrada:[" + matS1 + "]");
    }

    public void Entrenar(double alfa, double error) {

        // TODO add your handling code here:
        //String alfaS = JOptionPane.showInputDialog(null, "Por favor digite el factor de aprendizaje (alfa)");
        double alfaN = alfa;

        //String errorS = JOptionPane.showInputDialog(null, "Por favor digite el valor de error mínimo por patrón (ep)");
        double errorN = error;

        //String itS = JOptionPane.showInputDialog(null, "Por favor digite el número máximo de iteraciones (épocas)");
        int it = 100000;
        System.out.println("\nEntrenando la red por favor espere...");

        String res = Red.trainNetLog(miRedJA, alfaN, errorN, it);

        System.out.println(res);
    }

    public void Calcular() {
        Matriz tmp = new Matriz(matriz);
        tmp = Matriz.transponer(tmp);

        Matriz resp = Red.simNet(miRedJA, tmp);
        DecimalFormat df = new DecimalFormat("0.00");
        int indiceMayor = 0;
        System.out.println("\n\nRESULTADOS SEGÚN LA RED NEURONAL DE JAVA:\n");
        double mayor = 0;
        for (int i = 0; i < 26; i++) {
            String result = String.valueOf(df.format(Matriz.transponer(resp).toArray()[0][i]));
            System.out.println(nomPatrones[i] + ": " + result + "  ");
            if (mayor < Double.parseDouble(result)) {
                mayor = Double.parseDouble(result);
                indiceMayor = i;
            }
        }
        System.out.println(indiceMayor);
        for (int i = 0; i < 35; i++) {
            matrizSalida[i] = matPatrones[indiceMayor][i];
        }

        System.out.println("\n");

        //Inicializo de nuevo la matriz de captura y redibujo el lienzo
        for (int j = 0; j <= 34; j++) {
            matriz[j] = 0;
        }

        tmp = new Matriz(Matriz.transponer(resp).toArray()[0]);
        System.out.println(tmp.toStringM());
        tmp = null;
    }

    public int getContPatrones() {
        return contPatrones;
    }

    public void setContPatrones(int contPatrones) {
        this.contPatrones = contPatrones;
    }

    public double[] getMatriz() {
        return matriz;
    }

    public void setMatriz(double[] matriz) {
        this.matriz = matriz;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double[][] getMatPatrones() {
        return matPatrones;
    }

    public void setMatPatrones(double[][] matPatrones) {
        this.matPatrones = matPatrones;
    }

    public String[] getNomPatrones() {
        return nomPatrones;
    }

    public void setNomPatrones(String[] nomPatrones) {
        this.nomPatrones = nomPatrones;
    }

    public double[][] getMatObjetivos() {
        return matObjetivos;
    }

    public void setMatObjetivos(double[][] matObjetivos) {
        this.matObjetivos = matObjetivos;
    }

    public Red getMiRedJA() {
        return miRedJA;
    }

    public void setMiRedJA(Red miRedJA) {
        this.miRedJA = miRedJA;
    }

    public Matriz getEntrada() {
        return entrada;
    }

    public void setEntrada(Matriz entrada) {
        this.entrada = entrada;
    }

    public Matriz getPatronesM() {
        return patronesM;
    }

    public void setPatronesM(Matriz patronesM) {
        this.patronesM = patronesM;
    }

    public Matriz getObjetivosM() {
        return objetivosM;
    }

    public void setObjetivosM(Matriz objetivosM) {
        this.objetivosM = objetivosM;
    }

    public double[] getMatrizSalida() {
        return matrizSalida;
    }

}
