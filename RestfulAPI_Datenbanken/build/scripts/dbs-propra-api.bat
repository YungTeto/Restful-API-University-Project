@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  dbs-propra-api startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and DBS_PROPRA_API_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\dbs-propra-api-1.0.0.jar;%APP_HOME%\lib\commons-lang3-3.10.jar;%APP_HOME%\lib\javax.activation-1.2.0.jar;%APP_HOME%\lib\jaxws-rt-2.3.3.jar;%APP_HOME%\lib\jersey-container-jdk-http-2.31.jar;%APP_HOME%\lib\jersey-hk2-2.31.jar;%APP_HOME%\lib\jersey-media-multipart-2.31.jar;%APP_HOME%\lib\sqlite-jdbc-3.32.3.jar;%APP_HOME%\lib\jersey-media-json-jackson-2.31.jar;%APP_HOME%\lib\policy-2.7.10.jar;%APP_HOME%\lib\jaxb-impl-2.3.3.jar;%APP_HOME%\lib\ha-api-3.1.12.jar;%APP_HOME%\lib\management-api-3.2.2.jar;%APP_HOME%\lib\gmbal-4.0.1.jar;%APP_HOME%\lib\pfl-tf-4.1.0.jar;%APP_HOME%\lib\pfl-basic-4.1.0.jar;%APP_HOME%\lib\streambuffer-1.5.9.jar;%APP_HOME%\lib\saaj-impl-1.5.2.jar;%APP_HOME%\lib\stax-ex-1.8.3.jar;%APP_HOME%\lib\mimepull-1.9.13.jar;%APP_HOME%\lib\FastInfoset-1.2.18.jar;%APP_HOME%\lib\jakarta.activation-1.2.2.jar;%APP_HOME%\lib\woodstox-core-5.1.0.jar;%APP_HOME%\lib\stax2-api-4.1.jar;%APP_HOME%\lib\jakarta.xml.ws-api-2.3.3.jar;%APP_HOME%\lib\jackson-module-jaxb-annotations-2.10.1.jar;%APP_HOME%\lib\jakarta.xml.bind-api-2.3.3.jar;%APP_HOME%\lib\jakarta.xml.soap-api-1.4.2.jar;%APP_HOME%\lib\jakarta.jws-api-2.1.0.jar;%APP_HOME%\lib\jersey-server-2.31.jar;%APP_HOME%\lib\jersey-client-2.31.jar;%APP_HOME%\lib\jersey-media-jaxb-2.31.jar;%APP_HOME%\lib\jersey-common-2.31.jar;%APP_HOME%\lib\jakarta.annotation-api-1.3.5.jar;%APP_HOME%\lib\jersey-entity-filtering-2.31.jar;%APP_HOME%\lib\jakarta.ws.rs-api-2.1.6.jar;%APP_HOME%\lib\hk2-locator-2.6.1.jar;%APP_HOME%\lib\javassist-3.25.0-GA.jar;%APP_HOME%\lib\commons-io-1.3.2.jar;%APP_HOME%\lib\jackson-databind-2.10.1.jar;%APP_HOME%\lib\jackson-annotations-2.10.1.jar;%APP_HOME%\lib\jakarta.activation-api-1.2.2.jar;%APP_HOME%\lib\hk2-api-2.6.1.jar;%APP_HOME%\lib\hk2-utils-2.6.1.jar;%APP_HOME%\lib\jakarta.inject-2.6.1.jar;%APP_HOME%\lib\osgi-resource-locator-1.0.3.jar;%APP_HOME%\lib\jakarta.validation-api-2.0.2.jar;%APP_HOME%\lib\aopalliance-repackaged-2.6.1.jar;%APP_HOME%\lib\jackson-core-2.10.1.jar


@rem Execute dbs-propra-api
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %DBS_PROPRA_API_OPTS%  -classpath "%CLASSPATH%" de.hhu.cs.dbs.propra.Application %*

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable DBS_PROPRA_API_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%DBS_PROPRA_API_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
