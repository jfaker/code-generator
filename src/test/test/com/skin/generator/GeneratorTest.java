package test.com.skin.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.skin.generator.GenericGenerator;
import com.skin.generator.TableDefinition;
import com.skin.generator.TemplateParser;
import com.skin.generator.database.Table;
import com.skin.util.IO;

/**
 * @author weixian
 * @version 1.0
 */
public class GeneratorTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
	        String xml = IO.read(new File("table.xml"), "utf-8");
	        TableDefinition tableDefinition = TemplateParser.parse(xml);

	        if(tableDefinition != null) {
	            Table table = tableDefinition.getTable();

	            if(table != null) {
	                String home = new File("webapp/config/template").getCanonicalPath();
	                String work = new File("gen").getCanonicalPath();

	                try {
	                 	writeUTF8(new File(work, table.getClassName() + ".xml").getCanonicalFile(), xml);
	                    GenericGenerator generator = new GenericGenerator(home, work);
	                    generator.setTemplates(tableDefinition.getTemplates());
	                    generator.setEncoding(tableDefinition.getEncoding());
	                    generator.generate(table);
	                }
	                catch(Exception e) {
	                    e.printStackTrace();
	                }
	            }
	            else {
	                System.err.println("Can't get Table !");
	            }
	        }
	        else {
	        	System.err.println("Can't get TableDefinition !");
	        }
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * @param file
     * @param text
     */
    protected static void writeUTF8(File file, String text) {
        File dir = file.getParentFile();

        if(!dir.exists()) {
            dir.mkdirs();
        }

        OutputStream outputStream = null;
        OutputStreamWriter writer = null;

        try {
            outputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(outputStream, "UTF-8");
            writer.write(text);
            writer.flush();
            outputStream.flush();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            if(writer != null) {
                try {
                    writer.close();
                }
                catch(IOException e) {
                }
            }

            if(outputStream != null) {
                try {
                    outputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
    }
}
