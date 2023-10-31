import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

/**
 * clase para buscar métodos dentro de clases de java
 * EJM: String => (int, boolean)
 */
public class Busqueda {
    /**
     * CLI options
     */
   private String[] options;
   private final String ANSI_YELLOW = "\u001B[33m";
   private final String ANSI_CYAN = "\u001B[46m";
   private final String ANSI_RED = "\u001B[41m";
   private final String ANSI_RESET = "\u001B[0m";
   /**
    * constructor
    */
   public Busqueda(String[] nOptions) {
      this.options = nOptions;
   }

   /**
    * tokens a ignorar en la busqueda
    * @return lista de tokens a ignorar
    */
   private ArrayList<String> TokenAccesorList() {
      String[] tokens = new String[]{"public", "private", "protected", "final", "abstract", "static", "record", "class", "interface", "extends", "implements"};
      ArrayList<String> lista = new ArrayList<String>();
      for(String t: tokens) {
         lista.add(t);
      }
      return lista;
   }
   /**
    * Genera un String con los valores del archivo
    * @param filePath: ruta del archivo a leer
    * @return String con los datos del archivo
    */
   private String GetTextFromFile(String filePath) {
      String sentencia = "";
      String build = "";
      FileReader miReader = null;
      BufferedReader miBufferReader = null;
      try {
          miReader = new FileReader(filePath);
          miBufferReader = new BufferedReader(miReader);
          while(miBufferReader.ready()) {
              sentencia += miBufferReader.readLine() + "\n";
          }
          String[] datos = sentencia.split("\n");
          for(String d: datos) {
              if(d.contains(")") || d.contains("{") || d.endsWith("\n")) {
                  build += d.trim() + "\n";
              }
          }
      } catch (Exception var48) {
         System.err.println(var48);
      } finally {
          if(miReader != null) {
              try {
                  miReader.close();
              } catch(Exception e) {
                  System.err.println(e);
              }
          }
          if(miBufferReader != null) {
              try {
                  miBufferReader.close();
              } catch(Exception e) {
                  System.err.println(e);
              } finally {
                  miBufferReader = null;
              }
          }
      }
      return build;
   }
   /**
    * genera un String con las sentencias que indican un método
    * @param filePath: ruta del archivo a leer
    * @return String con las lineas en donde hay métodos
    */
   private String GetSentences(String filePath) {
      String build = "";
      String[] partition = this.GetTextFromFile(filePath).split("\n");
      for(String p: partition) {
          for(String t: this.TokenAccesorList()) {
              if(p.startsWith(t)) {
                  build += p.replace("{", "").trim() +"\n";
              }
          }
      }
      return build;
   }
   /**
    * genera un String con los métodos del archivo
    * @param filePath: ruta del archivo a leer
    * @return String con los métodos del archivo
    */
   private String GetMethodName(String filePath) {
      String build = "";
      String[] partition = this.GetSentences(filePath).split("\n");
      for(String p: partition) {
         String rem = p.replace(";", "").trim();
         String[] datos = rem.split("\\(");
         for(int i=0; i<datos.length-1; ++i) {
             String[] separate = datos[i].split(" ");
             build += separate[separate.length-1].trim() +"\n";
         }
      }
      return build;
   }
   /**
    * genera un String con el tipo de retorno del método
    * @param filePath: ruta del archivo a leer
    * @return String con el tipo de retorno del método
    */
   private String GetReturnType(String filePath) {
      String build = "";
      String[] partition = this.GetSentences(filePath).split("\n");
      for(String p: partition) {
         String rem = p.trim() + ";";
         String datos = rem.split("\\(")[0];
         if(datos.contains(";") == false) {
             String[] separate = datos.split(" ");
             if(this.TokenAccesorList().contains(separate[1])) {
                 separate[1] = separate[2];
             }
             build += separate[1] + "\n";
         }
      }
      return build;
   }
   /**
    * genera un String con los argumentos que recibe el método
    * @param filePath: ruta del archivo a leer
    * @return String con los argumentos del método
    */
   private String GetArguments(String filePath) {
      String build = "";
      String method_names = "";
      String[] partition = this.GetSentences(filePath).split("\n");
      for(String p: partition) {
          String[] rem = p.split("\\(");
          if(rem.length > 1) {
              method_names += "(" + rem[1] + ";";
          }
      }

      String[] validate = method_names.split(";");
      for(int i=0; i<validate.length; ++i) {
          if(validate[i].contains(",")) {
              String[] coma = validate[i].split(",");
              String[] p = coma[0].split(" ");
              String[] s = coma[1].split(" ");
              String l = "";
              if(s[1].contains(")") == false) {
                    l += p[0]+ ", " + s[1] + ")";
              }
              validate[i] = l;
          } else {
              String[] d = validate[i].split(" ");
              String l = "";
              if(d[0].contains(")") == true) {
                  d[0] = "()";
                  l += d[0];
              }
              else if(d[0].contains(")") == false) {
                  d[0] = d[0] + ")";
                  l += d[0];
              }
              validate[i] = l;
          }
          build += validate[i].replace("(", "( ").replace(")", " )") + "\n";
      }
      return build;
   }
   /**
    * genera un string con la sentencia completa según el método buscado
    * @param filePath: ruta del archivo a leer
    * @param sentencia: sentencia a buscar
    * @return String con la sentencia completa del método buscado
    */
   private String LocalizarMetodo(String filePath, String sentencia) {
      String build = "";
      String[] partition = this.GetSentences(filePath).split("\n");

      for(String p: partition) {
         String r = sentencia.trim().replace("::", "");
         if (p.toLowerCase().contains(r.toLowerCase().trim())) {
            build = p;
         }
      }

      return build;
   }
   /**
    * da el número de linea del método buscado
    * @param filePath: ruta del archivo a leer
    * @param sentence: sentencia a buscar
    * @return número de linea del método buscado
    */
   private int GetLineNumber(String filePath, String sentence) {
      FileReader miReader = null;
      LineNumberReader miLineReader = null;
      int resultado = -1;

      try {
         miReader = new FileReader(filePath);
         miLineReader = new LineNumberReader(miReader);
         String localizar = this.LocalizarMetodo(filePath, sentence).replace(";", "");

         while(miLineReader.read() != -1) {
            if (miLineReader.readLine().contains(localizar)) {
               resultado = miLineReader.getLineNumber();
            }
         }
      } catch (Exception var19) {
         //System.err.println(var19);
      } finally {
         if (miReader != null) {
            try {
               miReader.close();
            } catch (Exception var18) {
               System.err.println(var18);
            }
         }

         if (miLineReader != null) {
            try {
               miLineReader.close();
            } catch (Exception var17) {
               System.err.println(var17);
            }
         }

      }

      return resultado;
   }
   /**
    * da el formato de respuesta a la busqueda
    * @param filePath: ruta del archivo a leer
    * @param method_name: nombre del método a mostrar como respuesta
    * @param type: tipo de retorno del método
    * @param argument: argumentos del método
    */
   private void BusquedaFormat(String filePath, String method_name, String type, String argument, int nivelIgualdad) {
      String build = "";
      int lineNumber = this.GetLineNumber(filePath, method_name);
      if(lineNumber != -1) {
          if(nivelIgualdad == 1) {
              build = ANSI_YELLOW + build + "| " + ANSI_RESET + ANSI_RED + lineNumber + ANSI_RESET + ANSI_YELLOW + " | " + method_name + " :: " + type + " => " + argument + ANSI_RESET + "\n";
          } else {
              build = build + "| " + lineNumber + " | " + method_name + " :: " + type + " => " + argument + "\n";
          }
      } else {
          build = build + "| " + "unknow" + " | " + method_name + " :: " + type + " => " + argument + "\n";
      }
      System.out.println(build);
   }
   /**
    * da el número de respuestas encontradas
    * @param filePath: ruta del archivo a leer
    * @param sentencia: sentencia a buscar
    * @return número de respuestas
    */
   private int CantidaConcurrency(String filePath, String sentencia) {
      String[] method_names = this.GetMethodName(filePath).split("\n");
      String[] types = this.GetReturnType(filePath).split("\n");
      String[] arguments = this.GetArguments(filePath).split("\n");
      int m = 0;
      try {
        for(int i = 0; i < method_names.length; ++i) {
           if (sentencia.equals("")) {
               m = method_names.length;
           } else {
              String type = types[i].replace(" ", "").toLowerCase();
              String s_type = sentencia.split("=>")[0].replace(" ", "").toLowerCase();
              String s_arg = sentencia.split("=>")[1].replace(" ", "").toLowerCase();
              String args = arguments[i].replace(" ", "").toLowerCase();
              if (type.contains(s_type) || args.contains(s_arg)) {
                  ++m;
              }
           }
        }
      } catch (Exception var11) {
          //
      }
      return m;
   }
   /**
    * da formato a la cantidad de respuestas encontradas
    * @param filePath: ruta del archivo a leer
    * @param sentencia: sentencia a buscar
    */
   private void ConcurrencyFormat(String filePath, String sentencia, int cantidad) {
       System.out.println(String.format("\n%s\n", ANSI_CYAN + filePath + ANSI_RESET));
       System.out.println(String.format("Resultados: %s\n", ANSI_RED + cantidad + ANSI_RESET));
   }
   /**
    * busca la sentencia según el tipo de retorno y los argumentos
    * @param filePath: ruta del archivo a leer
    * @param sentencia: sentencia a buscar
    */
   private void BuscarSentencia(String filePath, String sentencia) {
      String[] method_names = this.GetMethodName(filePath).split("\n");
      String[] types = this.GetReturnType(filePath).split("\n");
      String[] arguments = this.GetArguments(filePath).split("\n");
      try {
         File miFile = new File(filePath);
         if (miFile.exists()) {
            for(int i = 0; i < method_names.length; ++i) {
               if (sentencia.equals("")) {
                  this.BusquedaFormat(filePath, method_names[i], types[i], arguments[i], 1);
               } else {
                  String type = types[i].replace(" ", "").toLowerCase();
                  String s_type = sentencia.split("=>")[0].replace(" ", "").toLowerCase();
                  String s_arg = sentencia.split("=>")[1].replace(" ", "").toLowerCase();
                  String args = arguments[i].replace(" ", "").toLowerCase();
                  if (type.contains(s_type) && args.contains(s_arg)) {
                     this.BusquedaFormat(filePath, method_names[i], types[i], arguments[i], 1);
                  } else if(type.contains(s_type) || args.contains(s_arg)) {
                     this.BusquedaFormat(filePath, method_names[i], types[i], arguments[i], 0);
                  }
               }
            }
         } else {
            System.out.println("el archivo no existe");
         }
      } catch (Exception var11) {
          //System.err.println(var11);
      }

   }
   /**
    * genera un String con la ruta de los archivos dentro de los directorios
    * <br> pre: </br> busca dentro de los directorios un archivo; si el hijo es directorio ingresa y busca los archivos
    * @param miFiles: los archivos dentro del directorio designado
    * @throws IOException: error al buscar los archivos del directorio
    * @return String con la ruta de los archivos
    */
   private String GetFilesFromDirectory(File[] miFiles) throws IOException {
       String fileName = "";
       for(File f: miFiles) {
           if(f.isFile()) {
               fileName += f.getCanonicalPath() + "\n";
           }
           if(f.isDirectory()) {
               fileName += this.GetFilesFromDirectory(f.listFiles()) + "\n";
           }
       }
       return fileName;
   }
   /**
    * busca la sentencia en el archivo designado
    * @param filePath: ruta del archivo a leer
    */
   public void SearchInFile(String filePath) {
       try {
           File miFile = new File(filePath);
           if(miFile.isFile() ) {
               for(int i=0; i<options.length; ++i) {
                   if(options[i].contains("/")) {
                       String sentence = options[i].replace("/", "");
                       int cantidad = this.CantidaConcurrency(filePath, sentence);
                       if(cantidad > 0) {
                           this.ConcurrencyFormat(filePath, sentence, cantidad);
                           this.BuscarSentencia(filePath, sentence);
                       }
                   }
               }
           }
       } catch(Exception e) {
           System.out.println(e);
       }
   }
   /**
    * busca la sentencia dentro de los archivos del directorio designado
    * @param directory: directory con los archivos
    */
   public void SearcInDirectory(String directory) {
       try {
           File miFile = new File(directory);
           if(miFile.isDirectory()) {
               File[] files = miFile.listFiles();
               for(File f: files) {
                   if(f.getName().contains(".java")) {
                       this.SearchInFile(f.getCanonicalPath());
                   }
               }
           }
        } catch(Exception e) {
            System.err.println(e);
        }
   }
   /**
    * busca la sentencia dentro de los archivos de los directorios del directorio designado
    * @param directorys: directorys del directory designado
    */
   public void SearcInDirectorys(String directorys) {
       try {
           String filesName = "";
           File miFile = new File(directorys);
           if(miFile.isDirectory()) {
               filesName = this.GetFilesFromDirectory(miFile.listFiles());
               String[] partition = filesName.split("\n");
               for(String p: partition) {
                   if(p.contains(".java")) {
                       this.SearchInFile(p);
                   }
               }
           }
       } catch(Exception e) {
           System.err.println(e);
       }
   }
   /**
    * organiza la forma en la que se ejecutan los argumentos de la consola;
    *  -f es para buscar la sentencia dentro de un archivo 
    *  -d es para buscar la sentencia dentro de los archivos del directory designado, solo se tienen en cuenta los archivos no directorios
    *  -D es para buscar la sentencia dentro de los archivos del directory designado si el directorio tiene más directorios se busca tambien dentro de ellos
    */
   public void GetFilesFromPath() {
       try {
           String directory = "";
           String fileName = "";
           String directorys = "";
           for(int i=0; i<options.length; ++i) {
               if(options[i].contains("-d")) {
                   directory = options[i+1];
                   this.SearcInDirectory(directory);
               }
               if(options[i].contains("-f")) {
                   fileName = options[i+1];
                   this.SearchInFile(fileName);
               }
               if(options[i].contains("-D")) {
                   directorys = options[i+1];
                   this.SearcInDirectorys(directorys);
               }
           }
       } catch(Exception e) {
           System.err.println(e);
       }
   }
}

