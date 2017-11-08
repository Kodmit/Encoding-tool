import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
 
public class Cipher {
   
    public String key;
    private byte[] encryptionKey;
   
    public void setKey(String key){
        this.key = key;
    }
   
    public byte[] getKey(){ 
        this.encryptionKey = this.key.getBytes();
        return this.encryptionKey;
    }
    
    private static String getFileExtension(File file) {
        String fileName = file.getName(); 										// |
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)   // | Thanks StackOverflow
        return fileName.substring(fileName.lastIndexOf(".")+1);					// |
        else return "";
    }
    
    @SuppressWarnings("null")
	public void encodeExt(String ext){ // encoding the extension of the file
    	byte[] keys = getKey();
    	String data[] = null;
    	
    	for(int i = 0; i < ext.length(); i++){
    		for(int j = 0; j < keys.length; j++){
    			data[i] = (String) (data[i] + keys[j]);
    		}
    	}
    	return;
    }
    
    
    public void encode(String file) throws IOException{
       
        byte[] keys = getKey(); // fetch the key
       
        Path path = Paths.get(file); // get the file
        byte[] data = Files.readAllBytes(path); // read all the bytes
           
        for(int i = 0; i < data.length; i++){ // encode the file by adding the key for each byte
            for(int j = 0; j < keys.length; j++){
                data[i] = (byte) (data[i] + keys[j]);
            }            
        }
           
        // Changing, storing and encoding file extension
		String fileName = path.getFileName().toString();		
		File originalFileName = new File(file);
		Path dir = path.getParent();	
		String fileNameWithOutExt = fileName.replaceFirst("[.][^.]+$", "");	
		File newFileName = new File(dir + "\\" + fileNameWithOutExt + ".kod");	
		String ext = getFileExtension(originalFileName);
		byte[] b = ext.getBytes();
		
		for(int k = 0; k < b.length; k++){
			for(int l = 0; l < keys.length; l++){
				b[k] = (byte) (b[k] + keys[l]);
			}
		}
		byte[] indic = "ext=".getBytes(); // Adding the extension marker in the file
		Files.write(path, data); // Write the bytes in the file
		Files.write(path, indic, StandardOpenOption.APPEND); 
		Files.write(path, b, StandardOpenOption.APPEND);
		originalFileName.renameTo(newFileName);
		// End
       
    }
   
    public void decode(String file) throws IOException{ // function for decoding
       
        byte[] keys = getKey();      // fetch the key
        Path path = Paths.get(file);    // fetch the file
        
        Charset charset = Charset.forName("ISO-8859-1");      // initialize charset
        List<String> output = Files.readAllLines(path, charset); // Read all the lines of the file
        
        String strOutput = output.toString(); // fetch file extension
        String encodedExt1 = strOutput.substring(strOutput.lastIndexOf("ext=") + 4); // storing file extension in the file
        String encodedExt = encodedExt1.replace("]", ""); // replace the ] by nothing at the end of the extension
        
        byte[] data = Files.readAllBytes(path); // read all the bytes of the file
        
        for(int i = 0; i < data.length; i++){      // decoding the bytes by soustracting the key for each bytes
            for(int j = 0; j < keys.length; j++){
                data[i] = (byte) (data[i] - keys[j]);
            }              
        }
        Files.write(path, data); // writing decoded data in the file
        
        // Decoding extension with key & changing for good extension
        
        byte[] b = encodedExt.getBytes(); // get the bytes of the encoded extension
        
        for(int k = 0; k < b.length; k++){ // same process but for the extension
			for(int l = 0; l < keys.length; l++){
				b[k] = (byte) (b[k] - keys[l]);
			}
		}
        String decodedExt = new String(b, "UTF-8"); // UTF 8 because it work and not ISO
        
        String fileName = path.getFileName().toString(); // converting the filename to a string
		File originalFileName = new File(file); // storing the original name
		Path dir = path.getParent();	
		String fileNameWithOutExt = fileName.replaceFirst("[.][^.]+$", "");	
		File newFileName = new File(dir + "\\" + fileNameWithOutExt + "." + decodedExt);	// all this code is for getting the name without parents (file.txt instead of c:\dddd\ddd\file.txt)
        
		originalFileName.renameTo(newFileName); // renaming the file
    }
}
