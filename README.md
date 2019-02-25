# fx-calculator
We have two ways of running jar. 
   a. java -jar ForexCalculator-1.0.0-RELEASE-jar-with-dependencies.jar - This will pick up Main class on its own(which is mentioned in pom.xml) and will pick up 
      fxrates.properties and precision.properties from classpath in jar.
	  
   b. java -DFxRate_PropertyfilePath="<path>/fxrates.properties" -DCurrPrecision_PropertyfilePath="<path>/precision.properties" -jar ForexCalculator-1.0.0-RELEASE-jar-with-dependencies.jar
