import java.io.File;

public class FilesHandler {

    File file = new File("D:\\Documentos HDD\\Sexto Semestre\\Sistemas Distribuidos\\ArchivosServidor");

    public String[] getFilesNames(){
        return file.list();
    }

    public File getFile(String fileName){
        file = new File("D:\\Documentos HDD\\Sexto Semestre\\Sistemas Distribuidos\\ArchivosServidor\\" + fileName);
//        if (!file.exists() || !file.isDirectory()) {
//            throw new IllegalArgumentException(file.getAbsolutePath()+ " is not a valid directory");
//        }
        if (file.isFile() && file.getName().equals(fileName)) {
            return file;
        }
        return null;
    }

}
