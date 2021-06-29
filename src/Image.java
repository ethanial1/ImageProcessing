// Librerias
import java.awt.Color; // Nos ayuda a trabajar con colores; transforma un color a un número o al revés
import java.awt.image.BufferedImage; // Sirve para trabajar con imágenes, permite traes una imagen y almacenarla en memoria dentro del programa
import javax.imageio.ImageIO; // Ayuda a abrir las imágenes.
import java.io.File; // Sirve para trabajar con archivos de imagen.
import java.io.FileInputStream; // Obtiene información de un archivo, entradas de información a partir de un archivo.
import java.io.IOException; // Sirve para capturas las excepciones al tratar de abrir o guardar en un archivo.
import java.io.InputStream;
import javax.imageio.stream.ImageInputStream;
import javax.swing.BorderFactory;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;


/**
 * @author ethan
 */

public class Image {
    private int columna, fila; // Obtener el tamaño de la matriz de la imagen.
    private Color color, colorAux;
    private int ima[][], auxi[][]; // serán del tamaño de la imagen que indican la fila y columna.
    
    //private int minimo = 0, maximo = 255; // Niveles de intensidad.
    private InputStream input;
    private ImageInputStream imagen1;
    
    private BufferedImage image;

    private String path = "/Users/ethan/John/Java/BaseProgram/src/assets/";

    // Método para obtener la matriz de la imagen
    public void matrizDatos(){
        try{
            // Creamos el objeto input, contiene la ruta de la imagen
            input = new FileInputStream(path+"car.jpg");
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
                auxi[y][x] = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
            }
        }
        
        // Guardamos la imagen
        //guardarImagen(ima, "escalaGris");
        // Creamos el histograma de la imagen a escala de grises
        //generarHistograma(auxi);
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

    // Aplicar negativo a una imagen
    public void negativo(){
        BufferedImage negativo = new BufferedImage(fila, columna, BufferedImage.TYPE_INT_BGR);
        int q = 255, r,g,b;
        // Recorremos la matriz con la imagen 
        for (int i = 0; i < columna; i++) {
            for (int j = 0; j < fila; j++) {
                colorAux = new Color(image.getRGB(j, i));
                //auxi [j][i] = ((255 - colorAux.getRed()) + (255 - colorAux.getGreen()) + (255 - colorAux.getBlue()));
                r = colorAux.getRed();
                g = colorAux.getGreen();
                b = colorAux.getBlue();
                
                negativo.setRGB(j, i, new Color(q-r,q-g,q-b).getRGB());
            }
        }
        
        try{
            ImageIO.write(negativo, "jpg", new File("/Users/ethan/NetBeansProjects/ProgramaBase/src/programabase/"+"negativoImagen.jpg"));
            System.err.print("Imagen creada");
            
        }catch(Exception e){
            System.err.print(e);
        }
    }
    
    // Ecualizar imagen
    public void ecualizarImagen(){
        // matriz sobre la que trabajamos
        int imagenEcualizada[][] = auxi;
        // L = nivel máximo de intensidad
        // m = 2 : cuadrática
        // m = 3 : cúbica
        // m = 1/2 = 0.5 : raíz cuadrada
        // m = 1/3 = 0.33 : raíz cúbica.
        double L = 255, m = 0.5;
        
        // Recorremos la matriz 
        for (int i = 0; i < columna; i++) {
            for (int j = 0; j < fila; j++) {
                imagenEcualizada[j][i] =(int)((Math.pow(L, (1 - m))) * (Math.pow(imagenEcualizada[j][i], m)));
              
            }
        }
        // Llamamos al metodoa para guardar la imagen
        guardarImagen(imagenEcualizada, "ImagenEcualizada2");
        
    }

    //Guardar imagen
    private void guardarImagen(int ima[][], String nombre){
        Color color_c;
        BufferedImage imagenResult = new BufferedImage(fila, columna, BufferedImage.TYPE_INT_BGR);
        
        // recorremos toda la matriz para crear la imagen
        for (int y = 0; y < columna; y++) {
            for (int x = 0; x < fila; x++) {
                color_c = new Color(ima[x][y], ima[x][y], ima[x][y]); // a cada canal le pasamos el número que tiene nuestra matriz.
                
                imagenResult.setRGB(x, y, color_c.getRGB());
            }
        }
        
        try{
            
            ///Users/ethan/NetBeansProjects/BaseProgram/src/baseprogram/
            ImageIO.write(imagenResult, "jpg", new File(path+nombre+".jpg"));
            System.err.print("Imagen creada");
            
        }catch(Exception e){
            System.err.print(e);
        }
    }

    // -------- Histograma de la imagen
    //calcularmedia
    private int calcularMedia(Color color){
        int mediaColor;
        mediaColor = (int)((color.getRed() + color.getGreen() + color.getBlue()) / 3);
        return mediaColor;
    }

    //Histograma
    private int[][] histograma(int[][] imagen){
        Color colorAux;
        int histogramaReturn[][] = new int[5][256];

        // Recorremos con ayuda de dos ciclos 
        for (int i = 0; i < columna; i++) {
            for (int j = 0; j < fila; j++) {
                // Obtenemos el color del pixel actual
                colorAux = new Color(imagen[j][i],imagen[j][i],imagen[j][i]);
                histogramaReturn[0][colorAux.getRed()] += 1;
                histogramaReturn[1][colorAux.getGreen()] += 1;
                histogramaReturn[2][colorAux.getBlue()] += 1;
                histogramaReturn[3][colorAux.getAlpha()] += 1;
                histogramaReturn[4][calcularMedia(colorAux)] += 1;
            }
        }

        return histogramaReturn;
    }

    // Dibujar histograma
    private void crearHistograma(int[] histograma, Color colorBarras, String nombre, int op){
        // Creamos el dataset y añadimos el histograma
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < histograma.length; i++) {
            dataset.addValue(histograma[i], "Número de píxeles", ""+i);
        }

        // Creamos el chart
        JFreeChart chart = ChartFactory.createBarChart(nombre,null, null, dataset, PlotOrientation.VERTICAL, true, true, false);

        // Modificamos el diseño del chart
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, colorBarras);
        chart.setAntiAlias(true);
        chart.setBackgroundPaint(new Color(214,217,223));
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4), BorderFactory.createLineBorder(Color.black)));
        
        // Guardamos los histogramas
        try {
            switch (op) {
                case 0:
                    ChartUtilities.saveChartAsPNG(new File(path+"salidaRojo.png"), chart, 600, 600);
                    break;
                case 1:
                    ChartUtilities.saveChartAsPNG(new File(path+"salidaVerde.png"), chart, 600, 600);
                    break;
                case 2:
                    ChartUtilities.saveChartAsPNG(new File(path+"salidaAzul.png"), chart, 600, 600);
                    break;
                case 3:
                    ChartUtilities.saveChartAsPNG(new File(path+"salida4.png"), chart, 600, 600);
                    break;
                case 4:
                    ChartUtilities.saveChartAsPNG(new File(path+"salida5.png"), chart, 600, 600);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Crear Histograma
    public void generarHistograma(int imagen[][]){
        int[][] histo =  histograma(imagen);
        int[] histoCanal = new int[256];
        for (int i = 0; i < 5; i++) {
            System.arraycopy(histo[i], 0, histoCanal, 0, histo[i].length);
            switch (i) {
                case 0:
                    crearHistograma(histoCanal, Color.red, "Color Rojo",0);
                    break;
                case 1:
                    crearHistograma(histoCanal, Color.green, "Color Verde",1);
                    break;
                case 2:
                    crearHistograma(histoCanal, Color.blue, "Color Azul",2);
                    break;
                case 3:
                    crearHistograma(histoCanal, Color.black, "Color Alpha",3);
                    break;
                case 4:
                    crearHistograma(histoCanal, Color.gray, "Color Gris",4);
                    break;
                default:
                    break;
            }
        }
    }
}