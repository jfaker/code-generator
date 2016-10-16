/*
 * $RCSfile: DataExportAction.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-12-15 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.generator.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;

import com.skin.exchange.CsvDataExport;
import com.skin.exchange.DataExport;
import com.skin.exchange.DefaultDataExport;
import com.skin.exchange.SqlDataExport;
import com.skin.generator.database.Table;
import com.skin.generator.database.handler.TableHandler;
import com.skin.generator.datasource.ConnectionConfig;
import com.skin.generator.datasource.ConnectionConfigFactory;
import com.skin.generator.datasource.ConnectionManager;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;
import com.skin.j2ee.util.Response;
import com.skin.util.IO;

/**
 * <p>Title: DataExportAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class DataExportAction extends BaseAction {
    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/database/script.html")
    public void script() throws ServletException, IOException {
        String fileName = this.request.getParameter("fileName");
        String connectionName = this.request.getParameter("connectionName");

        if(fileName != null) {
            File file = new File(this.servletContext.getRealPath("/WEB-INF/sqls/" + fileName));
            String source = SqlFileAction.getSource(file.getAbsolutePath());
            byte[] bytes = source.getBytes("UTF-8");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            Response.download(this.request, this.response, "create.sql", inputStream, bytes.length);
        }
        else if(connectionName != null) {
            Connection connection = null;
            ConnectionConfig connectionConfig = ConnectionConfigFactory.getByName(connectionName);

            try {
                connection = ConnectionManager.getConnection(connectionConfig);
                TableHandler tableHandler = new TableHandler(connection);
                List<Table> tableList = tableHandler.getTableList(null, null, "%", new String[]{"TABLE"});
                StringBuilder buffer = new StringBuilder();

                for(Table table : tableList) {
                    if(table.getRemarks() != null) {
                        buffer.append("-- " + table.getRemarks());
                        buffer.append("\r\n");
                    }

                    buffer.append(table.getCreateString());
                    buffer.append("\r\n");
                }

                byte[] bytes = buffer.toString().getBytes("UTF-8");
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                Response.download(this.request, this.response, "create.sql", inputStream, bytes.length);
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/database/export.html")
    public void export() throws ServletException, IOException {
        Connection connection = null;
        String format = this.getParameter("format");
        String[] tableName = this.getParameterValues("tableName");
        String connectionName = this.request.getParameter("connectionName");
        ConnectionConfig connectionConfig = ConnectionConfigFactory.getByName(connectionName);
        File target = this.getTarget();

        try {
            List<String> tableList = new ArrayList<String>();
            connection = ConnectionManager.getConnection(connectionConfig);

            OutputStream outputStream = null;
            ZipOutputStream zipOutputStream = null;

            try {
                outputStream = new FileOutputStream(target);
                zipOutputStream = new ZipOutputStream(outputStream);
                DataExport dataExport = this.getDataExport(format, connection);

                for(String table : tableName) {
                    ZipEntry entry = new ZipEntry(table + ".txt");
                    zipOutputStream.putNextEntry(entry);
                    dataExport.execute(table, zipOutputStream, "UTF-8");
                    tableList.add(table);
                }

                zipOutputStream.finish();
                zipOutputStream.flush();
                outputStream.flush();
            }
            finally {
                IO.close(zipOutputStream);
                IO.close(outputStream);
            }
        }
        catch(Throwable t) {
            t.printStackTrace();
        }

        Response.download(this.request, this.response, target.getName(), target);
    }

    /**
     * @param format
     * @param connection
     * @return DataExport
     */
    public DataExport getDataExport(String format, Connection connection) {
        if(format == null) {
            return new DefaultDataExport(connection);
        }

        if(format.equals("sql")) {
            return new SqlDataExport(connection);
        }

        if(format.equals("csv")) {
            return new CsvDataExport(connection);
        }

        return new DefaultDataExport(connection);
    }

    /**
     * @param target
     * @param fileList
     */
    public void delete(File target, List<File> fileList) {
        for(File file : fileList) {
            try {
                file.delete();
            }
            catch(Exception e) {
            }
        }

        try {
            target.delete();
        }
        catch(Exception e) {
        }
    }

    /**
     * @return File
     * @throws IOException 
     */
    public File getTarget() throws IOException {
        File target = null;
        long timeMillis = System.currentTimeMillis();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dir = this.getRealPath("/WEB-INF/backup");
        File parent = new File(dir);

        if(!parent.exists()) {
            parent.mkdirs();
        }

        while(true) {
            timeMillis += 1000;
            Date currentTime = new Date(timeMillis);
            target = new File(dir, dateFormat.format(currentTime) + ".zip");

            if(target.exists() == false) {
                target.createNewFile();
                break;
            }
        }

        return target;
    }
}
