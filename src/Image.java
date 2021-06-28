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
    
}