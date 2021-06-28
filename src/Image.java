// Librerias
import java.awt.Color; // Nos ayuda a trabajar con colores; transforma un color a un número o al revés
import java.awt.image.BufferedImage; // Sirve para trabajar con imágenes, permite traes una imagen y almacenarla en memoria dentro del programa
import javax.imageio.ImageIO; // Ayuda a abrir las imágenes.
import java.io.File; // Sirve para trabajar con archivos de imagen.
import java.io.FileInputStream; // Obtiene información de un archivo, entradas de información a partir de un archivo.
import java.io.IOException; // Sirve para capturas las excepciones al tratar de abrir o guardar en un archivo.
import java.io.InputStream;
import javax.imageio.stream.ImageInputStream;

/**
 * @author ethan
 */

public class Image {
    private int columna, fila; // Obtener el tamaño de la matriz de la imagen.
    private Color color, colorAux;
    private int ima[][], auxi[][]; // serán del tamaño de la imagen que indican la fila y columna.
    
    int minimo = 0, maximo = 255; // Niveles de intensidad.
    InputStream input;
    ImageInputStream imagen1;
    
    BufferedImage image;

    // Método para obtener la matriz de la imagen
    public void matrizDatos(){
        try{
            // Creamos el objeto input, contiene la ruta de la imagen
            input = new FileInputStream("/Users/ethan/NetBeansProjects/BaseProgram/src/baseprogram/car.jpg");
            imagen1 = ImageIO.createImageInputStream(input); // objeto que abre el archivo
            
            image = ImageIO.read(imagen1); // La podremos manipular
            
            //Obtenemos el número de filas de la imagen
            fila = image.getWidth(); // lo ancho
            //Obtenemos el número de columnas de la imagen
            columna = image.getHeight(); // lo alto
            
            // inicializamos las matrices
            ima = new int[fila][columna];
            auxi = new int[fila][columna]; // matriz sobre la que se trabaja.
            
            System.err.println(fila);
            System.err.println(columna);
            
            // Llamamos al método Escala de grisies
            escalaGris();
            
        }catch(IOException e){
            System.err.println(e);
        }
    }

    // Aplicar una escala de gris
    public void escalaGris(){
        // ciclos for para recorrer la matriz
        // Escala de grises
        for (int y = 0; y < fila; y++) {
            for (int x = 0; x < columna; x++) {
                // 
                int pixel = image.getRGB(y, x);
                color = new Color(pixel); // regresa un color mediante un valor
                    
                // generamos/llenamos la matriz
                ima[y][x] = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
            }
        }
        
        // Guardamos la imagen
        //guardarImagen(ima, "escalaGris");
    }

    // Se usan dos matrices porque no se puede trabajar con la imagen original.
    // Se binariza la imagen 0 y 1
    public void binarizar(){
        //recorremos la matriz ima
        for (int y = 0; y < columna; y++) {
            for (int x = 0; x < fila; x++) {
                if(ima[x][y] <= 128){
                    auxi[x][y] = 0;
                }else{
                    auxi[x][y] = 255;
                }
            }
        }
    }
    
}