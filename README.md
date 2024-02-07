[![License Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=true)](http://www.apache.org/licenses/LICENSE-2.0)
<h1 align="center">Hi there, I'm <a href="www.linkedin.com/in/konstantyn-zakharchenko" target="_blank">Konstantyn</a> 
<img src="https://github.com/blackcater/blackcater/raw/main/images/Hi.gif" height="32"/></h1>
<h3 align="center">Welcome to Family Budget</h3>

<img alt="Icon" src="app/src/main/res/drawable/icon.png" align="left" hspace="1" vspace="1" height="50" width="50">

Pet project using Clean Architecture + MVVM + Jetpack Compose + Android Architecture Components + Room + Firebase(as Backend).</br>

#### The main purpose is using the latest practices and libraries. And supposed to be released to GooglePlay after MVP is finished.

### Current tasks are logged in <a href="https://trello.com/b/FmJ6gOiQ/familly-budget">Trello</a>
### Outdated design reference is stored in <a href="https://www.figma.com/file/4Cw2uln0NidMxZ05QjmumA/FamillySpandings?type=design&node-id=0-1&mode=design&t=XiyCtvm1NepIhYcm-0">Figma</a>

## Architecture

Uses concepts of the notorious Uncle Bob's architecture called [Clean Architecture].</br>
The software produced by this architecture is going to be:

* Independent of Frameworks.
* Testable.
* Independent of UI.
* Independent of Database.

<img alt='Clean' src="https://raw.githubusercontent.com/andremion/Theatre/master/art/clean.png" align="right" width="50%"/>

### The Dependency Rule

The overriding rule of this architecture says that the source code dependencies always point inwards.</br>
The outer tiers can only dependent of inner tiers. Therefore, the inner tiers know nothing about the outer tiers.</br>
The more further you go through the concentric circles, the higher level the software becomes. Which means that the level of abstraction increases.

### Entities

An entity is a set of data structures. These entities are the business objects of the application and encapsulate the most general and high-level rules, such as [Event] or [Rating].

### Use Cases

They are the operations of the application and may contain specific business rules.</br>
This layer is isolated from database, UI, or any of the common frameworks.</br>
All use case classes extends [UseCase] abstract class that sets up the schedulers of Reactive Extensions.</br>

### Adapters

It is a set of adapters that convert data from the format most convenient for the use cases and entities, to the format most convenient for some external agency such as the UI or Database.</br>
It is this layer that will wholly contain the ViewModels of [MVVM] architectural pattern.</br>
The models are likely just data structures that are passed from the view to the use cases, and vice versa.</br>
Similarly, data is converted, in this layer, from the form most convenient for entities and use cases, into the form most convenient for whatever persistence framework is being used.

### Frameworks

The outermost layer is composed of frameworks and tools such as the Database and the Android Framework.</br>
The Repository pattern is used to encapsulate the details about caching mechanism.

### The Dependency Inversion

In order to not violate the Dependency Rule, the [Dependency Inversion principle] must be used whenever complex data needs to be passed across a boundary to an inward layer. Instead of expecting and directly referencing a low-level component (e.g. as a function parameter), the high-level layer provides and references an interface that must be implemented and inherited from by the caller. This way, the conventional dependency relationship is inverted and the high-level layer is decoupled from the low-level component.

<p align="center">
  <img alt='Inversion of Control' src="https://raw.githubusercontent.com/andremion/Theatre/master/art/inversion_of_control.png"></br>
</p>

### The Dependency Injection

To make the application more testable and avoid having to deal with object instantiations in many points, the [Dependency Injection technique] is used. 

> Dependency injection is one form of the broader technique of inversion of control. As with other forms of inversion of control, dependency injection supports the dependency inversion principle.
 
[Dagger] is the tool used for managing and injection of our dependencies. 

