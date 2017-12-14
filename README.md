# Build-System-Inference
Build-System-Inference is a system that allows user to extract information that are needed to compile and run projects from bulks of Ant Build Files and Ant Projects. By feeding the program appropriate directories containing projects or build files, the analyzer will write essential information to property files.  This implementation is intended to help with software testing by enabling the compilation and running tests of multiple projects.

There are two ways to run this project:

1. Import this project into an IDEA (Ideally, Eclipse or IntelliJ)
2. To analyze build files only:
    Run Driver and input a directory containing the build files
3. To analyze projects that contain build files:
    Run Driver_2 and input a directory containing the actual projects
4. Finally, a list of .properties files containing the essential information will be generated

Alternatively, you can run this analyzer from terminal:

1. cd into the analyzer
2. ant build
3. ant inferFiles (for build files only) and input a directory containing the build files
4. ant inferProjects and input a directory containing the actual projects
5. a "build.properties" file listing all of the outputs will be generated

To run the tests:

ant test

Program Features:
Properties Inferred from projects:
The analyzer will automatically find the build file and the .properties file defined in the project directory, and output
1. target to compile sources
2. target to compile tests
3. dependencies needed to compile sources
4. dependencies needed to compile tests
5. directory of sources
6. directory of tests
7. directory of compiled sources
8. directory of compiled tests
9. a list of developer written tests

Properties Inferred from build file only:
The analyzer will automatically read the build files and output
1. target to compile sources
2. target to compile tests
3. dependencies needed to compile sources (in wildcard form if it is listed as wildcards in the build file)
4. dependencies needed to compile tests (in wildcard form if it is listed as wildcards in the build file)
5. directory of sources
6. directory of tests
7. directory of compiled sources
8. directory of compiled tests
9. a list of developer written tests in wildcard form and the directory containing the tests

Empirical Evaluation:
