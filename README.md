SPW4 - Exercise 3
=================

## 1. 2048 Web Application and CI/CD Pipeline

Use your implementation of the game 2048 from the last assignment to create a web application and a continuous 
integration and continuous delivery (CI/CD) pipeline for it. Your CI/CD pipeline should execute the following jobs:

1. **Build**  
      Automatically build your web application (```mvn compile```)
2. **Test**  
      Run all tests (```mvn test```)
3. **Package**  
      Create a web application archive (*.war) file to deploy the application (```mvn package```)
4. **Deploy**  
      Run your application in a Docker container on your machine using Apache Tomcat

This assignment consists of three tasks:

**Task 1.a (4 Points):**  
In this repository you find a project which contains a template for a simple Java Servlet and a web application. Add 
your source code of the 2048 game and the tests to this project and complete the web application. Test it locally on 
your machine, either by configuring Tomcat support in the IntelliJ run configuration of the project, or by calling 
```mvn tomcat7:run```. Verify and document that your application and all tests work as expected by executing the 
different Maven goals (```mvn compile```, ```mvn test```, ```mvn package```) on the command line.

**Task 1.b (10 Points):**  
Register yourself at *GitLab* (https://gitlab.com), create a new project and push the sources of your web 
application to the Git repository of the project. Install a GitLab runner on your machine and register the runner 
for the project, as explained in [SETUP.md](SETUP.md). Use the file named *.gitlab-ci.yml* in the root folder of the 
repository to 
define a GitLab CI/CD pipeline which executes the steps described above. Document that your pipeline works as expected.

**Task 1.c (10 Points):**  
Register yourself at *GitHub* (https://github.com) and repeat the process of creating your CI/CD pipeline using GitHub 
Actions. For deployment set up a self-hosted runner in a Docker container, as described in [SETUP.md](SETUP.md). 
Document that your pipeline works as expected.
