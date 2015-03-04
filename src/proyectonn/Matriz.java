/*
Clase que define objetos de tipo matriz y posibles 
operaciones para con ellas
*/
package proyectonn;
import java.text.DecimalFormat;

/**
 *
 * @author spyller
 */
public class Matriz {
    int fil = 0;
    int col = 0;
    double mat[][];
    
    //Constructores
    
    //arreglo bidimensional 
    public Matriz(double m[][]){
        this.mat = m;
        this.fil = m.length;
        this.col = m[0].length;
    }
    
    //arreglo unidimensional
    public Matriz(double m[]){
        this.mat = new double[1][m.length];
        for(int i = 0; i < m.length; i++){
            this.mat[0][i] = m[i];
            this.fil = 1;
            this.col = m.length;
        }
      }
    
    /*
    Metodos
    */
    
    //numero de columnas
    public static int getCol(Matriz m){
        return m.col;
    }
    
    public int getCol(){
        return this.col;
    }
    
    public String getColString(){
        return String.valueOf(this.col);
    }
    
    //numero de filas
    
    public static int getFil(Matriz m){
        return m.fil;
    }
    
    public int getFil(){
        return this.fil;
    }
    
    public String getFilString(){
        return String.valueOf(this.fil);
    }
    
    //Convertir Matriz en array
    public double[][] toArray(){
        return this.mat;
    }
    
    public static double[][] toArray(Matriz m){
        return m.mat;
    }
    
    /*
    colocar valor en posicion[f][c]
    f indice de fila
    c indice de columna
    value valor que se coloca en el arreglo de matriz
    */
    
    public void setFC(int f, int c, double value){
        this.mat[f][c] = value;
    }
    
    public double getFC(int f,int c){
        return this.mat[f][c];
    }
    
    
    /*
    convertir la matriz en un string
    retorna string con las filas y las columnas de la matriz
    debidamente ordenadas
    */
    
    public String toStringM(Matriz m){
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuffer mat1 = new StringBuffer();
        
        for(int i = 0; i < m.getFil(); i++){
            for(int j = 0; j < m.getCol(); j++){
                mat1.append(" ");
                mat1.append(df.format(m.toArray()[j][i]));
            }
            mat1.append("\n");
        }
        String salida =  mat1.toString();
        return salida;
    }
    
    public String toStringM(){
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuffer mat1= new StringBuffer();
        for (int j=0;j<this.getFil();j++){
            for (int i=0;i<this.getCol();i++){
                mat1.append(" ");
                mat1.append(df.format((this.toArray()[j][i])));
            }
            mat1.append("\n");
        }
        String salida = mat1.toString();
        return salida;
    }
    
    //Transponer matriz
    public static Matriz Transponer(Matriz m){
        double retorno[][];
        retorno = new double[m.getCol()][m.getFil()];
        
        for(int i = 0; i < m.getFil(); i++){
            for(int j = 0; j < m.getCol(); j++){
                retorno[j][i] = m.toArray()[i][j];
            }
        }
        Matriz ret = new Matriz(retorno);
        return ret;
    }
    
    //Sumar Matrices
    public static Matriz Sumar(Matriz A, Matriz B){
        double retorno[][];
        retorno = new double[B.getFil()][B.getCol()];
        Matriz ret = new Matriz(retorno);
        
        for(int i = 0; i < A.getFil(); i++){
            for(int j = 0; j < A.getCol(); j++){
                ret.setFC(i, j, (A.getFC(j, j) + B.getFC(i, j)));
            }
        }
        return ret;
    }
    
    //Restar matrices
    public static Matriz Restar(Matriz A, Matriz B){
        double retorno[][];
        retorno =  new double[B.getFil()][B.getCol()];
        Matriz ret = new Matriz(retorno);
        
        for(int i = 0; i < A.getFil(); i++){
            for(int j = 0; j < A.getCol(); j++){
                ret.setFC(i, j, ( A.getFC(i, j) - B.getFC(i, j)));
            }
        }
        return ret;
    }
    
    //Multiplicar Matrices
    public static Matriz Multiplicar(Matriz A, Matriz B){
        double retorno[][];
        double tmp = 0;
        retorno = new double[A.getFil()][B.getCol()];
        Matriz ret = new Matriz(retorno);
        
        for(int i = 0; i < A.getFil(); i++){
            for(int j = 0; j < B.getCol(); j++){
                tmp = 0; 
                for(int k = 0; k < A.getCol(); k++){
                    tmp += A.getFC(i, k) * B.getFC(k, j);
                }
                ret.setFC(i, j, tmp);
            }
        }
        return ret;
    }
    
    /*Calcular la respuesta mas probable
    resp es una matriz con las respuestas de la simulacion
    nom arreglo de strings con los nombres de los patrones
    */
    public static String masProbable(Matriz resp, String nom[]){
        int index = 0; 
        double tmp = resp.toArray()[0][0];
        
        for(int i = 1; i < resp.toArray()[0].length; i++){
            if(resp.toArray()[0][i] > tmp){
                tmp = resp.toArray()[0][i];
                index = i;
            }
        }
        return nom[index];
    }
    
    //Obtener columna
    //Index es el indice de la columna a obtener
    public Matriz getColumna(int index){
        double tmp[][] = new double[this.getFil()][1];
        
        for(int i = 0; i < this.getFil(); i++)
            tmp[i][0] = this.toArray()[i][index];
        Matriz retorno = new Matriz(tmp);
        return retorno;
    }
    
    //Multiplicar Matriz por un escalar
    //esc es factor escalar
    public static Matriz MultME(Matriz m, double esc){
        double tmp[][] = new double[m.getFil()][m.getCol()];
        
        for(int i = 0; i < m.getFil(); i++){
            for(int j = 0; j < m.getCol(); j++){
                tmp[i][j] = m.toArray()[i][j] * esc;
            }
        }
        Matriz ret =  new Matriz(tmp);
        return ret;
    }
    
    //multiplicar elementos entre matrices
    public static Matriz multElementos(Matriz A, Matriz B){
        double tmp[][] = new double[A.getFil()][B.getCol()];
        
        for(int i = 0; i < A.getFil(); i++){
            for(int j = 0; j < A.getCol(); j++){
                tmp[i][j] = A.toArray()[i][j] * B.toArray()[i][j];
            }
        }
        Matriz ret = new Matriz(tmp);
        return ret;
    }
}
