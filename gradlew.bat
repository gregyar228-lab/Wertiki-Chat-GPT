@echo off
setlocal
set "GRADLE_VERSION=8.1.1"
if not defined GRADLE_USER_HOME set "GRADLE_USER_HOME=%USERPROFILE%\.gradle"
set "BASE=%GRADLE_USER_HOME%\skyrotor-wrapper"
set "GRADLE_HOME=%BASE%\gradle-%GRADLE_VERSION%"
if not exist "%GRADLE_HOME%\bin\gradle.bat" (
  if not exist "%BASE%" mkdir "%BASE%"
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $zip=Join-Path $env:GRADLE_USER_HOME 'skyrotor-wrapper\gradle-8.1.1-bin.zip'; if (!(Test-Path $zip)) { Invoke-WebRequest 'https://services.gradle.org/distributions/gradle-8.1.1-bin.zip' -OutFile $zip }; Expand-Archive -Path $zip -DestinationPath (Join-Path $env:GRADLE_USER_HOME 'skyrotor-wrapper') -Force"
  if errorlevel 1 exit /b 1
)
call "%GRADLE_HOME%\bin\gradle.bat" %*
endlocal
