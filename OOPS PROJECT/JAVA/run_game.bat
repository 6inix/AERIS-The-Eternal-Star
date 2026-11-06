@echo off
setlocal
cd /d "%~dp0"
title Ashen Fall: The Burning King

echo ========================================================
echo        Ashen Fall: The Burning King - Launcher
echo ========================================================
echo.

REM 1. Verify Java Runtime
where java >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Java Runtime is not found in your system PATH.
    echo Please install Java [JDK or JRE 17 or later] to run the game.
    echo Download: https://adoptium.net/
    echo.
    pause
    exit /b 1
)

REM 2. Check JDK Compiler and compile sources
where javac >nul 2>nul
if %ERRORLEVEL% equ 0 (
    echo Compiling source files to ensure compatibility...
    if not exist "bin" mkdir "bin"
    javac -d bin src\com\ashenfall\game\*.java src\com\ashenfall\game\engine\*.java src\com\ashenfall\game\entities\*.java src\com\ashenfall\game\entities\player\*.java src\com\ashenfall\game\ui\*.java src\com\ashenfall\game\world\*.java
    if %ERRORLEVEL% neq 0 (
        echo.
        echo [WARNING] Compilation reported errors. Attempting to run existing compiled classes...
    ) else (
        echo Compilation completed successfully.
    )
) else (
    echo [INFO] javac compiler not found. Running with precompiled classes in bin...
)

REM 3. Verify compiled Main class exists
if not exist "bin\com\ashenfall\game\Main.class" (
    echo.
    echo [ERROR] Game executable 'bin\com\ashenfall\game\Main.class' not found!
    echo Please install the Java Development Kit JDK so the sources can be compiled.
    echo.
    pause
    exit /b 1
)

REM 4. Start the game
echo.
echo Launching Ashen Fall: The Burning King...
echo.
java -cp bin com.ashenfall.game.Main

if %ERRORLEVEL% neq 0 (
    echo.
    echo [NOTICE] Game closed with exit code %ERRORLEVEL%.
)

pause
