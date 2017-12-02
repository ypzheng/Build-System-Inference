# Build-System-Inference
Infer Relevant Properties in Ant Build File

Developers:
Please import this repo into Eclipse as an "existing project"

Project Structure
------
```
Project Root
	|
	| --- src
		|
		| --- default package
			|
			| --- BuildAnalyzer Interface
			| --- AntBuildAnalyzer (implements BuildFileAnalyzer)
			| --- BuildAnalyzerAdapter (implements BuildFileAnalyzer)
			| --- PropertyWriter (writes inferred properties into .properties file)
			| --- Driver (specifies input build file and output file, runs the analyzer)
			|
		| --- util
			|
			| --- Debugger (manages console printing)
			| --- PathParser (parses paths in property files that come along with build files)
			|
	| --- test
		|
		| --- TestSuite1 (tests if the output strings are correct)
		| --- TestSuite2 (tests if the output property files are correct)
		| --- A list of test build files
		| --- A list of property files (coming soon)
		|
	| --- build files that need to be analyzed
	| --- output.properties file ("build.properties") for now
		
```