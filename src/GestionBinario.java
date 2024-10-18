import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GestionBinario {
    private final static String RUTA_FICHE_BIN= "resources/stardam_valley.bin";

    public void crearFicheroBinario(){
        try{
            if (!Files.exists(Paths.get(RUTA_FICHE_BIN))){
                Files.createFile(Paths.get(RUTA_FICHE_BIN));
            }
        }catch (IOException e){
            throw new RuntimeException("Error al crear el binario");
        }

    }
    public void eliminarBinarioPartida(){
        if (Files.exists(Paths.get(RUTA_FICHE_BIN))){
            try {
                Files.delete(Paths.get(RUTA_FICHE_BIN));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public boolean existeFichero(){
        return Files.exists(Paths.get(RUTA_FICHE_BIN));
    }
    public Granja cargarBinarioPartida(){
        Granja granja;
        try {
            InputStream ArchivoEntrada= Files.newInputStream(Paths.get(RUTA_FICHE_BIN));
            ObjectInputStream flujoEntrada= new ObjectInputStream(ArchivoEntrada);
             granja = (Granja) flujoEntrada.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Hay un error al cargar la partida");
        }
        return  granja;
    }
    public void guardarPartida(Granja g){
        try {
            OutputStream archivoSalida = Files.newOutputStream(Paths.get(RUTA_FICHE_BIN));
            ObjectOutputStream flujoSalida = new ObjectOutputStream(archivoSalida);
            flujoSalida.writeObject(g);
            flujoSalida.close();
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la partida en el archivo binario", e);
        }
    }
}
