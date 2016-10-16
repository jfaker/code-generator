@ECHO OFF
rd /s /q "webapp\WEB-INF\ayada"

@IF exist "E:\WorkSpace\finder" copy "conf\server-local.xml" "D:\Tomcat-7.0.37\conf\server.xml"
@IF exist "d:\workspace2\finder" copy "conf\server.xml" "D:\Tomcat-7.0.37\conf\server.xml"

del "D:\logs\generator.log"

del "D:\Tomcat-7.0.37\*.log"
del "D:\Tomcat-7.0.37\logs\*.log"
cd /d "D:\Tomcat-7.0.37\bin"
startup.bat