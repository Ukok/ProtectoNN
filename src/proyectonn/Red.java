package proyectonn;
import java.util.Random;
/**
 *Clase red: define matrices de pesos, metodos de entrenamiento,
 * propagacion y sus aspectos de campos y funciones de la red neuronal
 * @author carlo_000
 */
public class Red {
    Matriz wI; //matriz de pesos de entrada-oculta 18x35
    Matriz wO; //matriz de pesos de oculta salida 26x18
    Matriz trainMP; //matriz patrones de entrada 35x26
    Matriz trainMT; //matriz de salida 26x26
    Random generadorW = new Random(); //para inicializar pesos
    int epocas = 0;
    
    /*Constructores
    P matriz de patrones de entrada
    T matriz de targets
    */
    public Red(Matriz P, Matriz T){
        this.trainMP = P;
        this.trainMT = T;
    }
    
    public Red(){
        
    }
    
    /*
    Establecer pesos
    coloca las matrices de la capa oculta y la capa de 
    salida en el objeto Red
    pesosI pesos de la capa oculta
    pesosO pesos de la capa de salida
    */
    public void setPesos(Matriz pesosI, Matriz pesosO){
        this.wI = pesosI;
        this.wO = pesosO;
    }
    
    /*
    Inicializar Red
    inicializa los pesos de la red neuronal pasada como parámetro
    con un numero aleatorio dentro de una distribucion de probabilidad
    Gaussiana con valores entre [-0.5,0.5]
    */
    
    public static void init(Red redNeu){
        double tempI[][] = new double[18][35];
        double tempO[][] = new double[26][18];
        
        for(int i = 0; i < 26; i++){
            for(int j = 0; j < 35; j++){
                tempI[i][j] = redNeu.generadorW.nextGaussian() * 0.5;
            }
        }
        
        for(int i = 0; i < 26; i++){
            for(int j = 0; j < 18; j++){
                tempO[i][j] = redNeu.generadorW.nextGaussian() * 0.5;
            }
        }
        redNeu.wI = new Matriz(tempI);
        redNeu.wO = new Matriz(tempO);
    }
    
    /*
    Obtener Wi
    devuelve la matriz de pesos de la capa oculta
    */
    public Matriz getWi(){
        return this.wI;
    }
    
    /*
    Obtener Wo 
    devuelve la matriz de pesos de la capa de salida
    */
    public Matriz getWo(){
        return this.wO;
    }
    
    /*
    Obtener patrones de entrada
    devuelve la matriz con los patrones de entrada de entrenamiento
    */
    public Matriz getTrainMP(){
        return this.trainMP;
    }
    
    /*
    obtener salidas objetivo
    devuelve la matriz con los objetivos de entrenamiento
    */
    public Matriz getTrainMT(){
        return this.trainMT;
    }
    
    /*
    netlog evalua la funcion sigmoidal 1/(1+e^-n) de un valor 
    dado 
    */
    public static double NetLog(double value){
        double num = 1; 
        double den = 0;
        double f;
        double exp = (-1) * value;
        den = (1 + Math.pow(Math.E, (exp)) );
        f = num / den;
        return f;
    }
    
    /*
    f prima de net
    evalua la primera derivada de la funcion sigmoidal 1/(1+e^-n)
    */
    public static double PrimaNetLog(double value){
        double f;
        
        f = Red.NetLog(value) * (1 - Red.NetLog(value));
        return f;
    }
    
    /*
    F(net) a matriz
    evaluda la funcion sigmoidal a toda una matriz
    */
    public static Matriz NetMatrixLog(Matriz mat){
        double retorno[][];
        retorno = new double[mat.getFil()][mat.getCol()];
        Matriz res = new Matriz(retorno);
        
        for(int i = 0; i < mat.getFil(); i++){
            for(int j = 0; j < mat.getCol(); j++){
                res.setFC(i, j, Red.NetLog(mat.getFC(i, j)));
            }
        }
        return res;
    }
    
    /*
    evalua f' a matriz
    evalua la derivada de la funcion sigmoidal a toda una matriz
    */
    public static Matriz NetPrimaMatrizLog(Matriz mat){
        double retorno[][];
        retorno = new double[mat.getFil()][mat.getCol()];
        Matriz res = new Matriz(retorno);
        
        for(int i = 0; i < mat.getFil(); i++){
            for(int j = 0; j < mat.getCol(); j++){
                res.setFC(i, j, Red.PrimaNetLog(mat.getFC(i, j)));
            }
        }
        return res;
    }
    
    /*
    Simula el comportamineto de la red neuronal
    multiplica matrices, evalua las funciones de propagacion
    y propaga la red hacia adelante
    redNeu es una red neuronal modelo
    */
    public static Matriz simNet(Red redNeu, Matriz Ventrada){
        Matriz netI;
        Matriz fNetI;
        
        Matriz netO; 
        Matriz fNetO;
        
        //Multiplicacion net = [wI][wO]
        netI = Matriz.Multiplicar(redNeu.getWi(), Ventrada);
        fNetI = Red.NetMatrixLog(netI);
        
        //Multiplicacion net = [wO][fnetO]
        netO = Matriz.Multiplicar(redNeu.getWo(), fNetI);
        fNetO =  Red.NetMatrixLog(netO);
        
        return fNetO;
        //salida de 26x1
    }
    
    /*Establecer epocas
    establece el numero de epocas de entrenamiento
    */
    public void setEpocas(int num){
        this.epocas = num;
    }
    
    //obtener numero de epocas
    public int getEpocas(){
        return this.epocas;
    }
    
    /*
    Obtenet error cuadratico
    */
    public static double getErrorCuadratico(Matriz errores){
        double tmp = 0;
        for(int i = 0; i < errores.getFil(); i++)
            tmp += Math.pow(errores.getFC(i, 0), 2);
        tmp = tmp * 0.5;
        return tmp;
    }
    
    /**
     *Entrenamiento de la red neuronal
     * Entrena la red neuronal con el siguiente algoritmo:
     * 
     * 1. Se inicializa la red(valores aleatorios de wi y wo)
     * 2. Hubo error = true
     *      ciclo 1. Mientras (epocas < iteracionesMáximas) & (huboerror = true)
     *      ciclo 2. For i = 0; j < 26; i++
     *                  se presenta el patron i y se propaga la red hacia adelante
     *                  se calcula el error
     *                  condicion 1 if(error > error)
     *                          hubo error = true
     *                          se propaga la red hacia atras
     *                          se actualizan los pesos
     *       fin de ciclo 2
     *      epocas ++
     *      fin de ciclo 1
     * El algoritmo termina cuando los patrones tengan un valor de error medio
     * cuadratico menor que el establecido comoparametro o cuando se superan el 
     * numero de iteraciones
     */
    public static String trainNetLog(Red redNeu, double alpha, double error, int iteraciones){
        Red.init(redNeu);
        
        int contEpocas = 0;
        int j = 0;
        Matriz netI;
        Matriz fNetI;
        Matriz fNetPrimaI;
        Matriz wI;
        Matriz eI;
        Matriz dI;
        
        Matriz netO;
        Matriz fNetO;
        Matriz fNetPrimaO;
        Matriz wO;
        Matriz eO;
        Matriz dO;
        
        double errG[] = new double[26];
        double errGvalue = 0;
        double errP;
        boolean huboError = true;
        
        redNeu.setEpocas(0);
        while((contEpocas < iteraciones) && (huboError == true)){
            for(j = 0; j < 26; j++){
                //Paso hacia adelante
                //propagar la capa oculta
                netI = Matriz.Multiplicar(redNeu.getWi(), redNeu.getTrainMP().getColumna(j));
                fNetI = Red.NetMatrixLog(netI);
                
                //Propagar la salida
                netO = Matriz.Multiplicar(redNeu.getWo(), fNetI);
                fNetO = Red.NetMatrixLog(netO);
                
                //calcular errores
                eO = Matriz.Restar(redNeu.getTrainMT().getColumna(j), fNetO);
                
                //calcular el error cuadratico
                errP = Red.getErrorCuadratico(eO);
                errG[j] = errP;
                
                /****Condicion de error: paso hacia atras****/
                if(errP > error){
                    huboError = true;
                    
                    //se calculan las derivadas
                    fNetPrimaI = Red.NetPrimaMatrizLog(netI);
                    fNetPrimaO = Red.NetPrimaMatrizLog(fNetO);
                    
                    //calcular d0
                    dO = Matriz.multElementos(eO, fNetPrimaO);
                    
                    //calcular dI... error propagado
                    Matriz woT = Matriz.Transponer(redNeu.getWo());
                    Matriz tmp = Matriz.Multiplicar(woT, dO);
                    dI = Matriz.multElementos(tmp, fNetPrimaI);
                    
                    //actualizar pesos
                    Matriz deltaWO = Matriz.MultME(Matriz.Multiplicar(dO, Matriz.Transponer(fNetI)), alpha);
                    wO =  Matriz.Sumar(redNeu.getWo(), deltaWO);
                    
                    Matriz deltaWI = Matriz.MultME(Matriz.Multiplicar(dI, Matriz.Transponer(redNeu.getTrainMP().getColumna(j))), alpha);
                    wI = Matriz.Sumar(redNeu.getWo(), deltaWO);
                    
                    //se actualizan los pesos
                    redNeu.setPesos(wI, wO);
                }
            }
            contEpocas++;//una epoca mas iteracion
        }
        redNeu.setEpocas(contEpocas);
        
        String ep = String.valueOf(redNeu.getEpocas());
        
        if(huboError == false){
            for(int i = 0; i < 26; i++) errGvalue += errG[i];
            String errorG = String.valueOf(errGvalue);
            return "Red entrenada con éxito!\n" + "Epocas: " + ep + "\nValor de aerror alcanzado\nError Global: " + errorG + "\n";
        }
        else{
            return "Red entrenada con éxito! \n" + "Épocas: " + ep + "\nValor de error No alcanzado\n";
        }
    }
}