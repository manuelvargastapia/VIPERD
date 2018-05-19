@ECHO OFF & SETLOCAL

COPY /y PruebaDesktop\dist\PruebaDesktop.jar viperd\PruebaDesktop.jar
COPY /y viperd\lib\Desktop\ViperdDesktop.jar viperd\lib\ViperdDesktop.jar
COPY /y viperd\lib\Interno\ViperdInterno.jar viperd\lib\ViperdInterno.jar
COPY /y viperd\lib\Web\ViperdWeb.jar viperd\lib\ViperdWeb.jar
DEL /q /f viperd\mysql\crear.sql
DEL /q /f viperd\mysql\my.ini
DEL /q /f viperd\mysql\start.bat
RD /s /q viperd\mysql\bin
RD /s /q viperd\mysql\data
RD /s /q viperd\mysql\share
RD /s /q viperd\mysql\tmp
RD /s /q PruebaDesktop\build
RD /s /q PruebaDesktop\dist
RD /s /q PruebaDesktop\nbproject\private
RD /s /q PruebaWeb\build
RD /s /q PruebaWeb\dist
RD /s /q PruebaWeb\nbproject\private
RD /s /q ViperdInterno\build
RD /s /q ViperdInterno\nbproject\private
RD /s /q ViperdDesktop\build
RD /s /q ViperdDesktop\nbproject\private
RD /s /q ViperdWeb\build
RD /s /q ViperdWeb\nbproject\private

pause
EXIT