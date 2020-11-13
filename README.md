# Epsilon Transformation Language Demo

## About

The Epsilon Transformation Language is an eclipse extension for model transformations. Despite many code snippets available online, it is hard to find a standalone demo that is:
 * well documented
 * minimal
 * working
 * fully standalone (no eclipse required, builds into self-contained jar)

I therefore parted from the standalone examples of [Hyacinths fork](https://github.com/Hyacinth-Ali/epsilon-repo) and [the official repo](https://git.eclipse.org/c/epsilon/org.eclipse.epsilon.git/tree/examples/org.eclipse.epsilon.examples.standalone). After a lot of cherry-picking and refactoring, I ended up with this simple demo project.

### Changelog

 * Added this markdown file, as a place to start.
 * Decluttered [original repo](git://git.eclipse.org/gitroot/epsilon/org.eclipse.epsilon.git) from everything, not of particular interest for a minimal ETL demo.
   * Removed non-standalone sibling projects
   * Removed non-ETL examples
   * Removed irrelevant / unused etl / meta-models / xmi files.
   * Removed eclipse / html / bloat / meta files
 * Added util class to correctly resolve metamodel / etl-rule as java resources
 * Cleaned up and added many comments to exlain what the code actually does.
 * Fixed maven configuration
   * Refactored project structure, so it complies to maven conventions
   * Pom entry to correctly reference main class for direct launch
   * Pom configuration for creation of self-contained JAR

### Scenario

The code of this demo project transforms a [Tree input model](Tree.xmi) into a Tree output model. Specifically, it copies every node of the input model and prefixes it's label by ```copyOf```.

 * Example output model:  
  ```xml
  <?xml version="1.0" encoding="ASCII"?>
  <Tree xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="TreeDsl" xsi:schemaLocation="TreeDsl file:///Users/schieder/Code/EtlStandaloneExample/target/classes/metamodels/Tree.ecore" label="CopyOfa">
    <children label="CopyOfb">
      <children label="CopyOfc"/>
    </children>
  </Tree>
  ```

 * The syntax of tree models is defined by an [ecore meta-model](src/main/resources/metamodels/Tree.ecore). The input model ([Tree.xmi](Tree.xmi)), as well as the generated deep copy conform to this meta-model.
![tree](docs/tree-mm.png)

## Hands-On

This is a true standalone project. You do not need an IDE to run this, especially you do not need eclipse.

### Clone instructions

```git clone https://github.com/kartoffelquadrat/EtlTrueStandalone.git```

### Run instructions

You can either directly run the code, or first build a self-contained jar.

#### Direct launch

 * Update the input / output model locations in ```pom.xml:87/88```:  
  ```xml
  <argument>/Users/schieder/Code/EtlStandaloneExample/Tree.xmi</argument>
  <argument>/Users/schieder/Desktop/Copy.xmi</argument>
  ```

 * Then run the demo:  
```mvn clean package exec:java```  

#### Launch from JAR

 * Build a self-contained jar:  
  ```mvn clean package```

 * Run the self-contained jar with:  
  ```java -jar target/EtlPlayground.jar /Users/schieder/Code/EtlTrueStandalone/Tree.xmi /Users/schieder/Desktop/Copy.xmi```


 > Again, don't forget to update the runtime arguments.

## Contact / Pull Requests

 * Modifications: Maximilian Schiedermeier ![email](email.png)
 * Github: Kartoffelquadrat
 * Webpage: https://www.cs.mcgill.ca/~mschie3
 * [Original license](EPL-2.0.html)

