# Build-System-Inference
Build-System-Inference is a system that extracts information needed to compile and run Ant projects. By feeding the program appropriate directories containing projects or build files, the analyzer will write essential information to output files.  This implementation is intended to help with software testing by automatically inferring projects' build properties.


### There are two ways to run this project:

1. Import this project into an IDEA (Ideally, Eclipse or IntelliJ)
2. To analyze build files only:
    Run Driver and input a directory containing the build files
3. To analyze projects that contain Ant build files and/or .properties files:
    Run Driver_2 and input a directory containing the actual projects
4. Finally, a list of .properties files containing the essential information will be generated


#### Alternatively, you can run this analyzer from terminal:

1. cd into the analyzer
2. ```ant build```
3. ```ant inferFiles``` (for build files only) and input a directory containing the build files
4. ```ant inferProjects``` and input a directory containing the actual projects
5. a "build.properties" file listing all of the outputs will be generated

### To run the tests:

```ant test```
or
Run the test suite in Eclipse


Program Features:
-----
### Properties Inferred from projects:

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

### Properties Inferred from build file only:

The analyzer will automatically read the build files and output
1. target to compile sources
2. target to compile tests
3. dependencies needed to compile sources (in wildcard form if it is listed as wildcards in the build file)
4. dependencies needed to compile tests (in wildcard form if it is listed as wildcards in the build file)
5. directory of sources
6. directory of tests
7. directory of compiled sources
8. directory of compiled tests
9. directories that contain tests, and a list of "includes" and "excludes" in wildcard form

Empirical Evaluation:
----

10 build files from different projects were selected for testing Ant Analyzer.  
*Selection Criteria*: Java projects with Junit tests.  There are no more specific guidelines for selecting the projects because we want to make this analyzer more generalizable (bad choice).
The table below indicates the number of properties that work, out of these 10 build files:


| Property          | # of Correct Inferral (out of 10) |Why Does It Not Work |
| -------------     |:-----------------------:          | -----|
| compile target    | 10                                | |
| compile test target     | 10     |   |
| dependencies for compiling source |8| <li>Dependencies that need to be downloaded from online library is not handled</li> <li> File sets inside path are not handled</li>|
| dependencies for compiling test   |8 |   <li>Dependencies that need to be downloaded from online library is not handled</li> <li> File sets inside path are not handled</li>|
| directory of sources | 8 | ``<javac><src><pathelement location = xxxx></src></javac>`` pattern not handled |
| directory of tests | 8 |  ``<javac><src><pathelement location = xxxx></src></javac>`` pattern not handled |
| directory of compiled sources |10| |
| directory of compiled tests   |10| |
| test list | 10 | |

Project Structure:
----
### Default Package:
##### BuildAnalyzer Interface
Interface that lists essential properties to infer.

##### BuildFileAnalyzerAdapter.java
A class that will allow flexible calls of the methods for different types of build files.

##### AntBuildAnalyzer.java
Analyzes ant build file.

##### PropertyWriter.java
Writes the output from analyzer to property files.

### src.util:
This package contains utility classes that help us utilize Ant API for our objectives, and do other miscellaneous taks.

##### Debugger.java:
This class is for controlling console printing. `Debugger.log()` is a wrapper method for `System.out.println()`.

##### FileUtility.java:
This class contains methods for manipulating file path (in `String`).

##### PathParser.java:
This class is for parsing attributes from `Task` or `Target`, specifically for attributes that represent a file path. `PathParser.parse()` is a static method that takes in a `String` as parameter. It will looks for unresolve property(`${.*}`) in the `String` and resolve it. This includes using Ant Project API's `getProperty()`, looking for properties define under `Target`.  

##### TargetHelper.java:
This class contains methods to get important targets. It contains mechanism to infer compile source/test targets, and it also contains other helpful methods such as getting a list of target of interest.

##### TaskHelper.java:
This class contains methods to do task-related operation.  For example: given a target, returns tasks of interest.

##### TestListHelper.java:
This class contains methods to get information(directory, includes, and excludes wildcards) from junit targets.

##### WildCardResolver.java:
This class uses Directory Scanner to resolve wildcards.  Given a directory and wildcards, it will return all file names that match the pattern.

### scripts:
We have also included scripts that will find compile targets from the property files, and compile all the projects.

Checklist for Future Development:
----
1. Solve overlapping property file problem
2. Improve the accuracy of inferral by handling more edge cases
3. Write more flexible helper methods and classify them 
4. Check Issue Board
5. Implement Maven Analyzer

