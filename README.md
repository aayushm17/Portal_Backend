### MAVEN INSTALLATION

https://maven.apache.org/download.cgi
Under the "Files" section, download the "Binary zip archive" for the latest version of Maven.
Extract the Archive:
Extract the downloaded ZIP file to a directory of your choice (e.g., C:\Maven on Windows or /usr/local/apache-maven on Linux/Mac).
On Windows
Open Environment Variables:

Right-click on This PC or My Computer and select Properties.
Click on Advanced system settings.
Click on the Environment Variables button.
Add MAVEN_HOME Variable:

In the System Variables section, click New.
Set the Variable name as MAVEN_HOME.
Set the Variable value as the path where Maven is extracted (e.g., C:\Maven\apache-maven-3.8.6).
Update the PATH Variable:

Find the Path variable in the System Variables section and select it, then click Edit.
Click New and add the path to Maven’s bin directory (e.g., C:\Maven\apache-maven-3.8.6\bin).
Click OK to close all dialogs.
Verify the Installation:

Open a new Command Prompt and run:
```sh
mvn -version
```
If Maven is installed correctly, it will display the Maven version, Java version, and other information.
