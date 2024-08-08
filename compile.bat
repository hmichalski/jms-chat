@echo off
setlocal enabledelayedexpansion

REM 
set SRC_DIR=src
set LIB_DIR=lib
set BIN_DIR=bin

REM Find all .jar files in the lib directory and create a classpath string
set CLASSPATH=
for %%f in (%LIB_DIR%\*.jar) do (
    if defined CLASSPATH (
        set CLASSPATH=!CLASSPATH!;%%f
    ) else (
        set CLASSPATH=%%f
    )
)

REM Compile all .java files in the src directory
javac -d %BIN_DIR% -cp "%CLASSPATH%" %SRC_DIR%\*.java

REM Check if the compilation was successful
if %ERRORLEVEL% equ 0 (
    echo Compilation successful.
) else (
    echo Compilation failed.
)

endlocal
pause
