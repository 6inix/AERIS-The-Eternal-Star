@echo off
cd /d "%~dp0"
if exist "OOPS PROJECT\JAVA\run_game.bat" (
    cd /d "%~dp0OOPS PROJECT\JAVA"
    call run_game.bat
) else if exist "JAVA\run_game.bat" (
    cd /d "%~dp0JAVA"
    call run_game.bat
) else (
    echo [ERROR] Could not locate the game folder or run_game.bat.
    pause
)
