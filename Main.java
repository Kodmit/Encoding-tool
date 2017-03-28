import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
 
public class Main {
 
    public static void main(String[] args) throws IOException {
 
        Window window = new Window();
        window.openWindow();
    }
    
    
    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
    
    
    public void execute(String pass, String path){
    	
    	Cipher file = new Cipher();
    	
    	File originalFileName = new File(path);
        String ext = getFileExtension(originalFileName);
    	
    	if(pass.length() >= 5){ // If the key length is superior or equal to 5
            file.setKey(pass);
           
            if(ext.equals("kod")){ // Suivi des conseils de votre mail (equals)
                try {
                    file.decode(path);
                    JOptionPane.showMessageDialog(null, "File decoded succesfuly");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else{
                try {
                    file.encode(path);
                    JOptionPane.showMessageDialog(null, "File encoded succefuly");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "The encryption key is too short");
        }
    }
 
}