# Java-Probability-Collection
[![Scrutinizer Code Quality](https://scrutinizer-ci.com/g/lewysDavies/Java-Probability-Collection/badges/quality-score.png?b=master)](https://scrutinizer-ci.com/g/lewysDavies/Java-Probability-Collection/?branch=master) [![Build Status](https://scrutinizer-ci.com/g/lewysDavies/Java-Probability-Collection/badges/build.png?b=master)](https://scrutinizer-ci.com/g/lewysDavies/Java-Probability-Collection/build-status/master) [![](https://jitpack.io/v/lewysDavies/Java-Probability-Collection.svg)](https://jitpack.io/#lewysDavies/Java-Probability-Collection)<br>
Generic and Highly Optimised Java Data-Structure for Retrieving Random Elements with Probability

# Usage
```
ProbabilityCollection<String> collection = new ProbabilityCollection<>();
collection.add("A", 50); // 50 / 85 (total probability) = 0.588 * 100 = 58.8% Chance
collection.add("B", 25); // 25 / 85 (total probability) = 0.294 * 100 = 29.4% Chance
collection.add("C", 10); // 10 / 85 (total probability) = 0.117 * 100 = 11.7% Chance

String random = collection.get();
```

# Proven Probability
The probability test is run **1,000,000 times**. Each time getting **100,000** random elements and counting the spread. The test would not pass if the spread had over **3.5%** deviation from the expected probability.

Here is a real world example which used 1,000 selects:
```
A's Probability is 0.38% | Was selected 0.39% of the time
B's Probability is 0.38% | Was selected 0.38% of the time
C's Probability is 0.19% | Was selected 0.18% of the time
D's Probability is 0.04% | Was selected 0.04% of the time
RareAF's Probability is 0.01% | Was selected 0.01% of the time
```

# Performance
Get performance has been significantly reduced in comparison to my previous map implementation. This has been achieved with custom compared TreeSets. 
0.314ms to just 0.004.
```
Benchmark                Mode  Cnt  Score    Error   Units
new_collectionAddSingle  avgt   10  0.002 ±  0.001   s/op
new_collectionGet        avgt   10  0.004 ±  0.001   s/op
old_mapAddSingle         avgt   10  0.001 ±  0.001   s/op
old_mapGet               avgt   10  0.314 ±  0.069   s/op
```

# Installation
**Super Simple: Copy ProbabilityCollection.java into your project**<br><br>
or for the fancy users, you could use Maven:<br>
**Repository:**
```
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
**Dependency:**
```
<dependency>
    <groupId>com.github.lewysDavies</groupId>
    <artifactId>Java-Probability-Collection</artifactId>
    <version>0.5</version>
</dependency>
```
**Maven Shade This Dependency:**
```
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-shade-plugin</artifactId>
  <version>3.1.1</version>
  <executions>
    <execution>
      <configuration>
        <relocations>
           <relocation>
              <!-- Avoid Name Conflics -->
              <pattern>com.lewdev.probabilitylib</pattern>
              <shadedPattern>***<!--YOUR.PACKAGE.HERE-->***.probabilitylib</shadedPattern>
            </relocation>
          </relocations>
       </configuration>
     <phase>package</phase>
     <goals>
       <goal>shade</goal>
     </goals>
   </execution>
  </executions>
</plugin>
```
