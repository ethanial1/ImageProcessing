// Librerias
import java.awt.Color; // Nos ayuda a trabajar con colores; transforma un color a un número o al revés
import java.awt.image.BufferedImage; // Sirve para trabajar con imágenes, permite traes una imagen y almacenarla en memoria dentro del programa
import javax.imageio.ImageIO; // Ayuda a abrir las imágenes.
import java.io.File; // Sirve para trabajar con archivos de imagen.
import java.io.FileInputStream; // Obtiene información de un archivo, entradas de información a partir de un archivo.
import java.io.IOException; // Sirve para capturas las excepciones al tratar de abrir o guardar en un archivo.
import java.io.InputStream;
//import java.text.DecimalFormat;
import java.text.DecimalFormat;

import javax.imageio.stream.ImageInputStream;
import javax.swing.BorderFactory;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
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
            input = new FileInputStream(path+"uno.jpg");
            imagen1 = ImageIO.createImageInputStream(input); // objeto que abre el archivo
            
            image = ImageIO.read(imagen1); // La podremos manipular
            
            //Obtenemos el número de filas de la imagen
            fila = image.getWidth(); // lo ancho
            //Obtenemos el número de columnas de la imagen
            columna = image.getHeight(); // lo alto
            
            // inicializamos las matrices
            ima = new int[fila][columna];
            auxi = new int[fila][columna]; // matriz sobre la que se trabaja.
            
            System.err.println("Número de filas: "+fila);
            System.err.println("Número de columnas"+columna);
            
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
        guardarImagen(auxi, "escalaGris");
        // Creamos el histograma de la imagen a escala de grises
        //histogramaEcualizado(auxi, "Niveles de gris");
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
                //colorAux = new Color(image.getRGB(j, i));
                //auxi [j][i] = ((255 - colorAux.getRed()) + (255 - colorAux.getGreen()) + (255 - colorAux.getBlue()));
                //r = colorAux.getRed();
                //g = colorAux.getGreen();
                //b = colorAux.getBlue();
                
                //negativo.setRGB(j, i, new Color(q-r,q-g,q-b).getRGB());
                negativo.setRGB(j, i, new Color(255-auxi[j][i],255-auxi[j][i],255-auxi[j][i]).getRGB());
            }
        }
        
        try{
            ImageIO.write(negativo, "jpg", new File(path+"negativoImagen.jpg"));
            System.err.print("Imagen creada");
            
        }catch(Exception e){
            System.err.print(e);
        }
    }
    
    // Modificar el brillo y contraste de una imagen, como consecuencia se  modifica el histograma
    public void modificarBrilloContraste(){
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
        histogramaEcualizado(imagenEcualizada, "Imagen m = 2");
        
    }

    // Ecualizar el histograma : Ethan
    public void ecualizarHistograma(){
        int niveles[]; // se guarda el número de píxeles por cada nivel de intensiada
        int imagenAuxi[][] = ima; // Matríz que es igual a la matríz auxiliar
        int intensidades[] = new int[256]; // Nuevas intensidades (S)
        int nuevaImagen[][] = new int[fila][columna]; // Nueva matriz para guardar la nueva imagen
        int L; // Número de niveles de intensidad

        // Multiplicación
        int total = fila * columna;

        // Indicamos que solo obtenemos 3 cífras despues del punto decimal
        //DecimalFormat formatter = new DecimalFormat("#.####");

        //Obtener las intensidades
        niveles = obtenerIntensidades(imagenAuxi);

        // Definosmos el número de los niveles de intensidad
        L = 255;

        double temporal = 0, result, temp;

        // Recorremos el arreglo de intensidades 
        for (int i = 0; i < L; i++) {
            //Se realiza la división de :    Nk / MN 
            result = (double) niveles[i] / total;
            // multiplicamos el valor 
            temp = (double) (L-1) * result;
            intensidades[i] = (int) Math.floor(temp + temporal);
            temporal = result;
        }

        
        // Crear la nueva imagen
        for (int y = 0; y < fila; y++) {
            for (int x = 0; x < columna; x++) {
                nuevaImagen[y][x] = intensidades[imagenAuxi[y][x]];
            }
        }

        //guardarImagen(nuevaImagen, "NuevaImagenEcualizada");
        histogramaEcualizado(nuevaImagen, "Ecualizado");
    }

    // 2da version de Ecualizar histograma
    public void ecualizacionHistograma(){
        int imagenEcualizada[][] = auxi;
        int instensidades[];

        int L = 255;
        int inte = 0;
        int result = fila * columna;
        Color color;
        int sk;

        // Formato decimal
        DecimalFormat format = new DecimalFormat("#.00");

        //double nuevasIntensidades[] = new double[256];
        //double intensidades2[] = new double[256];
        double s[] = new double[256];
        double redondear = 0, intensidad, resultadoMulti;

        // Obtenemos las intensidades
        instensidades = obtenerIntensidades(imagenEcualizada);

        for (int i = 0; i < instensidades.length; i++) {
            intensidad = Double.parseDouble(format.format(instensidades[i] / result));
            resultadoMulti = Double.parseDouble(format.format(intensidad * L));
            redondear = Math.round(redondear + resultadoMulti);
            s[i] = redondear;
        }


        BufferedImage imagenResult = new BufferedImage(fila, columna, BufferedImage.TYPE_INT_BGR);
        for (int x = 0; x < fila; x++) {
            for (int y = 0; y < columna; y++) {
                inte = imagenEcualizada[x][y];
                instensidades[inte]++;
                sk = (int)s[inte];
                color = new Color(sk,sk,sk);
                imagenResult.setRGB(x, y, color.getRGB());
            }
        }

        // Guardamos la imagen
        try {
            ImageIO.write(imagenResult, "jpg", new File(path+"imagenEcualizada.jpg"));
            System.err.println("Imagen creada en "+path);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    /*
    autor: Carlos vazquez
    */
    public void Ecualizar() {
        int intensidad;
        int ImagenEcualizada[][] = auxi;
        int intensidades[] = new int[256];
        int L = 256 - 1;
        double nuevasintensidades[] = new double[256];

        // Formato decimal
        DecimalFormat F1 = new DecimalFormat("#.00");
        double Intendidades2[] = new double[256];
        double s[] = new double[256];
        double redondear = 0;

        // Obtenemos los niveles de intensidad
        for (int x = 0; x < fila; x++) {
            for (int y = 0; y < columna; y++) {
                intensidad = ImagenEcualizada[x][y];                                 
                intensidades[intensidad]++;                          
            }
        }

        
        for (int i = 0; i < intensidades.length; i++) {
            Intendidades2[i] = intensidades[i];    
            nuevasintensidades[i] = Double.parseDouble(F1.format(Intendidades2[i] / (fila * columna)));                                              
            s[i] = Double.parseDouble(F1.format(nuevasintensidades[i] * L));
            redondear = Math.round(redondear + s[i]);
            s[i] = redondear;                     
        }

        // Creamos la imagen
        Color color_n;
        BufferedImage imag = new BufferedImage(fila, columna, BufferedImage.TYPE_INT_BGR);
        for (int x = 0; x < fila; x++) {
            for (int y = 0; y < columna; y++) {
                intensidad = ImagenEcualizada[x][y];               
                intensidades[intensidad]++;
                int sk = (int) s[intensidad];    
                color_n = new Color(sk, sk, sk);
                imag.setRGB(x, y,color_n.getRGB());
            }
        }
        try {
        ImageIO.write(imag, "jpeg", new File(path + "imagenEcualizada.jpeg"));
            System.err.println("Imagen Creada");

        } catch (Exception e) {
            System.err.println("Error");
        }
    }

    // Aplicación de Filtros de imagen
    /*
        Se trabaja sobre una copia de la imagen, debido a que se tiene que tener una referencia de los píxeles vecinos
        cuando se modifica un píxel
        ##  CONCEPTOS
        Filtro                              =   Procesado en el dominio frecuencial, 
        Pasa-bajas                          =   Suabizar la imagen, desenfoque
        Pasa-altas                          =   Realza o mejora los bordes, los bordes tienen mayor cantidad de información.
                                            Se puede hacer una combinación con el objetico de mejorar la calidad de la imagen.
        
        Los filtros espaciales son filtros que se realizan sobre la imagen y por lo tanto en el 
        dominio espacial, es por eso que se llama d    
        
        Aplicaciones
            Reduccion de ruido
            Detección de bordes en una dirección, horizontal o vertical.
            contornos 
            Suavisado
        
        Los pixeles de la nueva imagen, dependen de la imagen original y de sus vecinos, se desplaza, de izquierda a derecha y
        de arriba hacia abajo.

        El filtro: media = usa 1 en su máscara.
        Padding     :   Es añadir margenes de 0 en la imagen (Filas y columnas)

        Combolución                         =   Operación matemática que suma una función f consigo misma repetidas veces
                                                en todo el dominio de otra función h, utilizando en cada suma como valor de escala
                                                el valor de h en ese punto de su dominio.
        Combolución en el Dominio Espacial  =   Es la operación de una imagen un filtrado.
        Filtro espacial                     =   es la operación que se aplica a una imagen para resaltar o atenuar detalles espaciales
                                                con el fin de mejorar la interpretación visual.
        Frecuencia Espacial                 =   La frecuencia espacial define la magnitud de cambios en el nivel de gris por unidad de 
                                                distancia en una determinada zona de la imagen.
        Áreas de baja frecuencia            =   áreas de la imagen con pequeños cambios o con transiciones graduales en los valores.
        Áreas de alta frecuencia            =   Las áreas de grandes cambios o rápidas transiciones se conocen como áreas de altas frecuencias.

        ## Categoría de los filtros espaciales
        Filtro pasa bajas:
        Filtro pasa altas:

        Filtro espacial                     :   Se realiza trasladando una matriz rectangular de dos dimensiones (también llamada ventana, kernel, máscara o núcleo) 
                                                que contiene "pesos" o ponderaciones sobre la imagen en cada localización de píxel.
        Pesos                               =   Valores que puede tener el filtro.
        Máscara                             =   Matríz de coeficientes
    */
    // Filto de la media
    // Método que contien todos los filtros
    public void aplicarFiltro(){
        int[][] media = {{1,1,1},{1,1,1},{1,1,1}};

        // llamamos al método del filtro
        filtro(media);
    }
    
    /** 
    * @param mascara tipo de mascara
    */
    public void filtro(int[][] mascara){
        //int mascara[][] = {{1,1,1},{1,1,1},{1,1,1}};
        int tope = mascara.length/2;
        int imagen[][] = auxi;
        int copia[][] = new int[fila+1][columna+1];
        int nueva[][] = new int[fila+2][columna+2];
        
        // Añadir un margen de 0 en las filas y columnas
        for (int i = 0; i < nueva.length; i++) {
            for (int j = 0; j < nueva[i].length; j++) {
                if(i == 0 || i == nueva.length-1 || j == 0 || j == nueva[i].length-1){
                    nueva[i][j] = 0;
                }else{
                    nueva[i][j] = imagen[i-1][j-1];
                }
            }
        }
        
        // Recorremos la matriz en base a la máscara
        for (int i = tope; i < nueva.length-tope; i++) {
            for (int j = tope; j < nueva[i].length-tope; j++) {
                copia[i][j] = calcular(nueva, mascara, i, j);
                //System.out.println(copia[i][j]);
            }
        }

        // Guardamos la imagen
        guardarImagen(copia, "Filtrado");
        histogramaEcualizado(copia, "Filtro: media");
    }

    private int calcular(int[][] imagen, int mascara[][], int fila, int columna){
        int tope = mascara.length/2;
        int result = 0;
        int factor = 0;

        // Itersar en base a la mascara y la imagen
        for (int i = 0; i < mascara.length; i++) {
            for (int j = 0; j < mascara[0].length; j++) {
                factor += mascara[i][j];
                result += (int) (mascara[i][j] * imagen[fila-tope+i][columna-tope+j]);
            }
        }

        if(factor > 0){
            result /= factor;
        }

        return result;
    }

    // Creación del histograma
    // obtención de intensidades
    private int[] obtenerIntensidades(int imagenEcualizada[][]){
        int intensidadVecotor[] = new int[256];
        int intensidad;
        for (int y = 0; y < columna; y++) {
            for (int x = 0; x < fila; x++) {
                intensidad = imagenEcualizada[x][y];
                intensidadVecotor[intensidad]++;
            }
        }

        return intensidadVecotor;
    }
    
    //Grafica del histograma
    private void histogramaEcualizado(int imagenEcualizada[][], String titulo){
        int insidadesEcualizadas[] = new int[256];

        // Obtenemos el nivel de intensidad
        insidadesEcualizadas = obtenerIntensidades(imagenEcualizada);

        // Graficamos
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for (int i = 0; i < insidadesEcualizadas.length; i++) {
                dataset.addValue(insidadesEcualizadas[i], "Intensidades", ""+i);
            }

            JFreeChart histogram = ChartFactory.createBarChart(titulo, "Niveles de intensidad", "Pixeles", dataset ,PlotOrientation.VERTICAL, false, true, false);

            ChartFrame ventana = new ChartFrame(titulo, histogram);

            ventana.setSize(1000, 500);
            ventana.setVisible(true);
        } catch (Exception e) {
            System.out.println(e);
        }
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
            ImageIO.write(imagenResult, "jpeg", new File(path+nombre+".jpg"));
            System.err.print("Imagen creada");
            
        }catch(Exception e){
            System.err.print(e);
        }
    }





    // -------- Obtención del Histograma en base al Rojo, Vender y Azul
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