package proyectonn;

import javax.swing.JOptionPane;
import java.text.DecimalFormat;

/**
 *
 * @author carlo_000
 */
public class CallF {
    int contPatrones = 0; //variable para indicar el numero de patrones ingresados
    double matPatrones[][] = new double[26][35];//array con los patrones
    double matriz[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    double matObjetivos[][] = new double[26][26];
    String nombrePatr[] = new String[26];
    
    Matriz patronesM;
    Matriz objetivosM;
    
    Red RedNN = null;
    
    public CallF(){
        int pos = 0;//variable para inicializar la matriz objetivo
        for(int i = 0; i < 26 ; i++){
            for (int j = 0; j < 26; j++){
                if(j == pos){
                    matObjetivos[i][j]=1;
                }
                else{
                    matObjetivos[i][j]=0;
                }
            }
            pos++;
        }
    }
    
    /*
    Funcion para capturar el array de patrones temporal 1x35
    y se guarda en un array
    Final de patrones en la fila = contPatrones
    recibe un string que es el nombre del patron 
    */
    public void ReadPannel(String nombre){
        for(int j = 0; j < 26; j++) matPatrones[contPatrones][j] = matriz[j];
        
        nombrePatr[contPatrones] = nombre;//se guarda el nombre de los patrones
        
        //se pasa el valor del patron solo para imprimirlo
        StringBuffer mat = new StringBuffer();
        mat.append(matriz[0]);
        for(int i = 1; i < 35; i++){
            mat.append(" ");
            mat.append(matriz[i]);
        }
        
        String matString = mat.toString();
        
        
        System.out.println("Capturado patron " + nombre + " con exito!");
        System.out.println("Nombre: \"" + nombrePatr[contPatrones] +  "\"\n Valor:");
        System.out.println("[" + matString + "]");
        
        //se incrementa el contador de patrones y hay que actualizar etiquetas
        contPatrones++;
        /**
         * Hay que actualizar etiquetas
         */
        
        //si se ha alcanzado el numero maximo de patrones
        if(contPatrones  == 26){
            //deshabilitar elementos graficos y se cambia estado
            
            patronesM =  new Matriz(matPatrones);//se inicializa la matriz de patrones
            patronesM = Matriz.Transponer(patronesM);//se transpone la matriz
            objetivosM = new Matriz(matObjetivos);//se inicializa la matriz de objetivos
            objetivosM = Matriz.Transponer(objetivosM);//se trnaspone
            
            RedNN = new Red(patronesM, objetivosM);
            
            System.out.println("\nMatriz de objetivos [" +
                    objetivosM.getFilString() + "x" +
                    objetivosM.getColString() + "]:\n");
            System.out.println(objetivosM.toStringM());
            
            System.out.println("\nMatriz de patrones [" +
                    patronesM.getFilString() + "x" +
                    patronesM.getColString() + "]:\n");
            System.out.println(patronesM.toStringM());
            
            System.out.println("\nModo de ejecucion");
            System.out.println("Para comenzar a utilizar la red neuronal" +
                    " calculada en Java presione Entrenar y luego calcular");
            //nuevos elementos graficos
            
            //reiniciar vector de entrada
            for(int j = 0; j <= 34 ; j++)
                matriz[j] = 0;
            
            //repintar
            /**
             * zona.repaint();
             * dibujarguias();
             */
            
            StringBuffer mat1 = new StringBuffer();
            mat1.append(matriz[0]);
            for(int i = 1; i < matriz.length; i++){
                mat1.append(" ");
                mat1.append(matriz[i]);
            }
            
            //zona.repaint();
            //dibujarguias();
            String matS1 = mat1.toString();
            //label2.setText("Vector de entrada:["+ matS1 +"]");
        }
    }

    public void EntrenaRed(){
        //calcularJ.setenabled(false);
        
        String alfaS = JOptionPane.showInputDialog(null, "Inserte el factor de aprendizaje (alfa):");
        double alfa = Double.valueOf(alfaS);
        
        String errorS = JOptionPane.showInputDialog(null, "Inserte el factor de error minimo por patron: ");
        double error = Double.valueOf(errorS);
        
        String iteracionesS = JOptionPane.showInputDialog(null, "Inserte el número máximo de iteraciones (épocas):");
        int iteraciones = Integer.valueOf(iteracionesS);
        
        System.out.println("Entrenando la red. Espere...");
        
        String res = Red.trainNetLog(RedNN, alfa, error, iteraciones);
        
        System.out.println(res);
        //calcular.setEnabled(true);
        //zona.repaint();
        //dibujarguias();
    }
    
    public void Calcular(){
        Matriz tmp = new Matriz(matriz);
        tmp = Matriz.Transponer(tmp);
        
        Matriz resp =  Red.simNet(RedNN, tmp);
        DecimalFormat df = new DecimalFormat("0.00");
        
        System.out.println("Resultados según la red neuronal");
        
        for(int i = 0; i < 26; i++)
            System.out.println(nombrePatr[i] + ": " + 
                    String.valueOf(df.format(Matriz.Transponer(resp).toArray()[0][i])) + " ");
         
        //inicializando de nuevo la matriz de captura y redibujo del lienzo
        for(int j = 0; j <= 34; j++)
            matriz[j] = 0;
        
        tmp = new Matriz(Matriz.Transponer(resp).toArray()[0]);
        //respuesta.setText(Matriz.masProbable(tmp,nombrePatr));
        //zona.repaint();
        //dibujarGuias();
    }
}
