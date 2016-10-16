/*
 * $RCSfile: FileTypeTest.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-3-29 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package test.com.skin.finder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>Title: FileTypeTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class FileTypeTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        /*
        Collection<String> list = FileType.map.values();

        for(String type : list) {
            System.out.println("map.put(\"" + type + "\", \"" + type + "\");");
        }
        touch(new File("webapp/gen/webapp/files/1.1"));
        */
    }

    /**
     * @param file
     */
    public static void touch(File file) {
        OutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
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
