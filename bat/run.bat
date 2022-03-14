@ECHO OFF
IF "%1"=="start" (
    ECHO start Word2PdfBatchTranser-1.0.jar
    start "Word2PdfBatchTranser-1.0.jar" java -jar Word2PdfBatchTranser-1.0.jar
) ELSE IF "%1"=="stop" (
    ECHO stop Word2PdfBatchTranser-1.0-jar-with-dependencies.jar
    TASKKILL /FI "WINDOWTITLE eq Word2PdfBatchTranser-1.0.jar"
) ELSE (
    ECHO please, use "run.bat start" or "run.bat stop"
)
pause