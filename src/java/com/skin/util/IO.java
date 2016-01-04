/*
 * $RCSfile: IO.java,v $$
 * $Revision: 1.1  $
 * $Date: 2013-2-19  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: IO</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class IO {
    /**
     * @param file
     * @param charset
     * @return String
     */
    public static String read(File file, String charset, int bufferSize) throws IOException {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;

        try {
            inputStream = new FileInputStream(file);

            if(charset == null || charset.length() < 1) {
                inputStreamReader = new InputStreamReader(inputStream);
            }
            else {
                inputStreamReader = new InputStreamReader(inputStream, charset);
            }

            int length = 0;
            char[] buffer = new char[bufferSize];
            StringBuilder result = new StringBuilder();

            while((length = inputStreamReader.read(buffer, 0, bufferSize)) > -1) {
                result.append(buffer, 0, length);
            }

            return result.toString();
        }
        finally {
            close(inputStreamReader);
            close(inputStream);
        }
    }

    /**
     * @param file
     * @param charset
     * @return String
     */
    public static List<String> list(File file, String charset) throws IOException {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;

        try {
            inputStream = new FileInputStream(file);

            if(charset == null || charset.length() < 1) {
                inputStreamReader = new InputStreamReader(inputStream);
            }
            else {
                inputStreamReader = new InputStreamReader(inputStream, charset);
            }

            return list(inputStreamReader);
        }
        finally {
            close(inputStreamReader);
            close(inputStream);
        }
    }

    /**
     * @param reader
     * @return List<String>
     * @throws IOException
     */
    public static List<String> list(Reader reader) throws IOException {
        BufferedReader buffer = null;
        List<String> list = new ArrayList<String>();

        if(reader instanceof BufferedReader) {
            buffer = (BufferedReader)reader;
        }
        else {
            buffer = new BufferedReader(reader);
        }

        try {
            String line = null;
            while((line = buffer.readLine()) != null) {
                list.add(line);
            }
            return list;
        }
        finally {
            // never closed
            if(reader == null) {
                buffer.close();
            }
        }
    }

    /**
     * @param file
     * @param charset
     * @return String
     */
    public static void write(File file, byte[] buffer) throws IOException {
        OutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(buffer);
            outputStream.flush();
        }
        finally {
            close(outputStream);
        }
    }

    /**
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        copy(inputStream, outputStream, 4096);
    }

    /**
     * @param inputStream
     * @param outputStream
     * @param bufferSize
     * @throws IOException
     */
    public static void copy(InputStream inputStream, OutputStream outputStream, int bufferSize) throws IOException {
        int length = 0;
        byte[] buffer = new byte[bufferSize];

        while((length = inputStream.read(buffer, 0, bufferSize)) > -1) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
    }

    /**
     * @param inputStream
     * @param outputStream
     * @param bufferSize
     * @param size
     * @throws IOException
     */
    public static void copy(InputStream inputStream, OutputStream outputStream, int bufferSize, long size) throws IOException {
        if(size > 0) {
            int readBytes = 0;
            long count = size;
            int length = Math.min(bufferSize, (int)(size));
            byte[] buffer = new byte[length];

            while(count > 0) {
                if(count > length) {
                    readBytes = inputStream.read(buffer, 0, length);
                }
                else {
                    readBytes = inputStream.read(buffer, 0, (int)count);
                }

                if(readBytes > 0) {
                    outputStream.write(buffer, 0, readBytes);
                    count -= readBytes;
                }
                else {
                    break;
                }
            }

            outputStream.flush();
        }
    }

    /**
     * @param reader
     * @param writer
     * @throws IOException
     */
    public static void copy(Reader reader, Writer writer) throws IOException {
        copy(reader, writer, 2048);
    }

    /**
     * @param reader
     * @param writer
     * @param bufferSize
     * @throws IOException
     */
    public static void copy(Reader reader, Writer writer, int bufferSize) throws IOException {
        int length = 0;
        char[] buffer = new char[bufferSize];

        while((length = reader.read(buffer, 0, bufferSize)) > -1) {
            writer.write(buffer, 0, length);
        }

        writer.flush();
    }

    /**
     * @param file
     */
    public static void toucn(File file) throws IOException {
        OutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
        }
        finally {
            close(outputStream);
        }
    }

    /**
     *
     * @param file
     * @param charset
     * @return String
     * @throws IOException
     */
    public static String toString(File file, String charset) throws IOException {
        InputStream in = null;

        try {
            in = new FileInputStream(file);
            return toString(in, charset);
        }
        finally {
            close(in);
        }
    }

    /**
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String toString(InputStream inputStream) throws IOException {
        return toString(new java.io.InputStreamReader(inputStream));
    }

    /**
     *
     * @param inputStream
     * @param bufferSize
     * @return
     * @throws IOException
     */
    public static String toString(InputStream inputStream, String charset) throws IOException {
        Reader reader = null;

        try {
            reader = new java.io.InputStreamReader(inputStream, charset);
            return toString(reader);
        }
        finally {
            IO.close(reader);
        }
    }

    /**
     *
     * @param reader
     * @return
     * @throws IOException
     */
    public static String toString(Reader reader) throws IOException {
        return toString(reader, 2048);
    }

    /**
     *
     * @param reader
     * @param bufferSize
     * @return
     * @throws IOException
     */
    public static String toString(Reader reader, int bufferSize) throws IOException {
        try {
            int length = 0;
            char[] buffer = new char[Math.max(bufferSize, 2048)];
            StringWriter out = new StringWriter();

            while((length = reader.read(buffer)) > -1) {
                out.write(buffer, 0, length);
            }

            return out.toString();
        }
        finally {
        }
    }

    /**
     * @param resource
     */
    public static void close(java.io.Closeable resource) {
        if(resource != null) {
            try {
                resource.close();
            }
            catch(IOException e) {
            }
        }
    }

    /**
     * @param inputStream
     */
    public static void close(InputStream inputStream) {
        if(inputStream != null) {
            try {
                inputStream.close();
            }
            catch(IOException e) {
            }
        }
    }

    /**
     * @param out
     */
    public static void close(OutputStream outputStream) {
        if(outputStream != null) {
            try {
                outputStream.close();
            }
            catch(IOException e) {
            }
        }
    }

    /**
     * @param reader
     */
    public static void close(Reader reader) {
        if(reader != null) {
            try {
                reader.close();
            }
            catch(IOException e) {
            }
        }
    }

    /**
     * @param writer
     */
    public static void close(Writer writer) {
        if(writer != null) {
            try {
                writer.close();
            }
            catch(IOException e) {
            }
        }
    }
}
