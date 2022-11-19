#!/bin/bash 

javac Aquarium.java
java Aquarium
find . -type f -name "*.class" -delete
