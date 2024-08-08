@echo off
setlocal enabledelayedexpansion

REM
set LIB_DIR=lib
set BIN_DIR=bin

REM Find all .jar files in the lib directory and create a classpath string
set CLASSPATH=%BIN_DIR%
for %%f in (%LIB_DIR%\*.jar) do (
    set CLASSPATH=!CLASSPATH!;%%f
)

REM Run the AdminFrame class
java -cp "%CLASSPATH%" AdminFrame

endlocal
pause
