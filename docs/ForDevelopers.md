# Purpose
This document is a reference for developers.

# Design principles
Do not change the numbering of these design principles because comments in code refer to them by their number. 
I do not foresee the need to eliminate any of the design principles.
Of course, I am aware that the only thing I can foresee with absolute certainty is that things will change. 
Therefore, if you want to deprecate a design principle, just mark it so, but do not delete it.

1. Use dependency injection and inversion of control.
  * Use ContextConfiguration and SpringJUnit4TestRunner to Test that the injected entities are configured correctly for 
   dependency injection.
2. Use the observer pattern to decouple the various components of the application.

# Development principles 
* Use a school project to learn and experiment new concepts.  
  * Add copious documentation to allow future generations to learn from past school experience.
* Use the google java coding style. 
  * Follow instructions [here](google-java-coding-style) 
    to configure IntelliJ to use the google coding style. 
* Use comments to convey decisions and intent. 
  * Minimize description of implementation that can be understood by just looking at the code.
* Eliminate all warnings reported by Intellij prior to code check-in.
  * Use Intellij's VCS plugin to commit changes. This action will display a dialog if there are errors or warnings 
    in the set of files in the changelist.

# Decisions
## IDE
* Use Intellij for Java development. Based on past experience Intellij provides the best out of the box experience with deep Java support. 
## Implementation Details
### Observer pattern
* Use [spring event handling][event-handling-in-spring] for the observer pattern.
  * It is type safe.
  * The application is a spring application.
  * Decouples the publisher from the observable.
* Implement the classes that implement the observer pattern in an ```event``` package.


# Development environment setup
## Add advisor as a known word to the dictionary
Intellij flags advisor in data_adivsor as an incorrect word. When you see this issue, choose the option to save adivsor to the dictionary 
to eliminate this issue.
 
# References
## Spring observer pattern
[Event handling in spring][event-handling-in-spring]

## Storm
* [Understanding the parallelism of a Storm topology](http://www.michael-noll.com/blog/2012/10/16/understanding-the-parallelism-of-a-storm-topology/)

## JUnit and Spring tests
* https://spring.io/blog/2011/06/21/spring-3-1-m2-testing-with-configuration-classes-and-profiles

[event-handling-in-spring]: http://www.programmingforliving.com/2012/10/event-handling-in-spring.html
[google-java-coding-style]: https://github.com/HPI-Information-Systems/Metanome/wiki/Installing-the-google-styleguide-settings-in-intellij-and-eclipse