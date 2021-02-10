# Stubway Tests project

This is a project to test stubs delivered as demo content on particular AEM instance.
By default it runs tests on pre-defined publish AEM instance: http://localhost:4503. 
pom.xml contains credentials for AEM author and publish instances (host, port, user, password) 

## How to run tests

All commands should be run from stubway.tests folder in project structure.

If you have a running Author AEM instance you can build and package the whole project and deploy into AEM with  

    mvn clean test -PtestOnAuthor
    
If you have a running Publish AEM instance you can build and package the whole project and deploy into AEM with

    mvn clean test -PtestOnPublish
    
This command will run by default tests on publish instance

    mvn clean test

You can also run tests on any distance not pre-defined in pom.xml using parameters eg:  

    mvn clean test -DstubUrl=http://localhost:4502 -DstubUser=admin -DstubPassword=admin
