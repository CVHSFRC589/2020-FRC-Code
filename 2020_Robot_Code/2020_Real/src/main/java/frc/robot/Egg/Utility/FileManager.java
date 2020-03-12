package frc.robot.Egg.Utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class FileManager {
	
	//This class is similar to the file creator, but it is used to interact with class files.
	//There is a save and load class, to save a serializable class to a file or load a class
	//from a file

	//This method saves a class by converting it into a byte array then writing the binary
	//to a file.  Because of how a binary file works, it can have any file type and can still 
	//be read
	
	public static <T> void save(T object, String path) {
		byte[] stream = null;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);) {
		    oos.writeObject(object);
		    stream = baos.toByteArray();
		} catch (IOException e) {
		    new Error("Cannot save - Error in serialization: " + e.getMessage(), ErrorType.NonFatal);
		    return;
		}
		
		try { 
			
			new File(path).delete();
			
            OutputStream os = new FileOutputStream(path);   
            
           
            os.write(stream);   
            os.close(); 
        } 
  
        catch (Exception e) { 
        	new Error("Cannot save - Error writting to file: " + e.getMessage(), ErrorType.NonFatal);
        	return;
        } 
	}
	
	public static <T> void save(T object, File path) {
		save(object, path.getPath());
	}
	
	//This method works in a similar method to the above, except it reads a binary file then
	//creates an instance of the class it finds and outputs a version of the class.  Make sure
	//you use this in situations where you write the output to the correct instance of the class
	
	@SuppressWarnings("unchecked")
	public static <T> T load(String path) {
		
		byte[] stream = null;
		
		try {
			InputStream is = new FileInputStream(path);
			stream = is.readAllBytes();
			is.close();
		} catch (FileNotFoundException e1) {
			new Error("Cannot load - Error reading file: " + e1.getMessage(), ErrorType.NonFatal);
		} catch (IOException e) {
			new Error("Cannot load - Error reading file: " + e.getMessage(), ErrorType.NonFatal);
		}
		
		T obj = null;

        try (ByteArrayInputStream bais = new ByteArrayInputStream(stream);
                ObjectInputStream ois = new ObjectInputStream(bais);) {
            obj = (T) ois.readObject();
        } catch (IOException e) {
            new Error("Cannot load - Error in Deserialization: " + e.getMessage(), ErrorType.NonFatal);
        } catch (ClassNotFoundException e) {
            new Error("Cannot load - Error in class casting: " + e.getMessage(), ErrorType.NonFatal);
        }
        return obj;
	}
	
	public static <T> T load(File path) {
		return load(path.getPath());
	}	
}
