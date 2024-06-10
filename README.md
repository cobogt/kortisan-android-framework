# Kortisan Android Framework

Kortisan a mobile framework aimed to implement the best practices and help to build big and
large-scale applications, prioritising code re usability and modularity.

## Inspiration

This framework is mainly inspired in the excellent organization of the funcionalities of Laravel, the modern architecture of Flutter, taking of this the BLoC and MVI patterns, the Event Bus design helps to connect all the modules and the MVI pattern with the Redux pattern, this last came from the React Framework.

## A brief history

This project has born as a way to reduce the boilerplate code in the repositories a common mistake when using the CLEAN architecture, after that a lot of new functionalities were requierd, as encriptation, a plain navigation model to abstract the use of activities and compose navigation routes from any entrypoint. This led to use the BLoC pattern in order to control the hierarchy of the graphic interfaces while persists the fast refactoring feature, this is aimed to every component can control his own repository without increment the complexity of the other components, to achieve this were necessary to split the execution of the repository from the state representation, at this point the best way was using MVI because the state mutation is away of the action emiter, so all this needs to work togheter which can be achieved using MVI. Last but not least the state manipulation, the Redux pattern is a litte weak control of the state transitioning and this is a perfect use case to implement graphs, all the states should be defined in a sealed class and can be mutated using production rules in his own container using mutableStateFlow, but this transitions needs a litte of help becuse the code is very similar in multiple states, inspired in the validator of Laravel you can use this objects to reuse in all the states that need it.

## Main Features

There are three main reasons to use this framework:

1 - A lot of boilerplate avoided thanks to the built-in functionalities with a standarized structure, you'll know exactly where to found whatever you want to do.

2 - Designed to follow the KISS principle so you won't need to write the same code twice.

3 - In the shared modules the code can be shared with other projects that use this framework, reducing the cost and time of starting a new app.

## What'll you find out of the box?

* **Flow control** modularize and compact all your functionalities and use it as you want.
* **Repositories**, a lot of functions to reduce your repositories to a bunch of lamdas.
* **Tagging**, follow all the behaviour of your users with rules that let you filter and adapt the data before send it to your providers.
* **Remote config**, load and parse your configurations and change the behaviour of your app on the fly.
* **Plain route navigation model**, map and design your app navigation in a flat way using access rules and common objects, forget the concept of activity and commpose route, they are the same.
* **BLoC Components**, combine and encapsule your UI logic, state and behaviour without afect other components.
* **Built in Http client**, using strategies you can configure your client in a clear way.
* **Entrypoints control**, abstract how the users access and interact with your app, connect the MVI pattern with the external world.
* **State oriented architecture**, you'll have full control of what happens in your app clearing the fuzzy states and trackig what is happening and why.

## General schematic

![image](https://github.com/cobogt/kortisan-android-framework/assets/6600651/2306dbf0-aea7-4a9e-9068-6dc61c0811cc)

[Link to the detailed description of process](https://docs.google.com/document/d/1wuJT3hf586eP1Cgk791YWSEfBMrYi3ZrScmb3qDwFg4/edit#heading=h.zf8ogtuiva64)

# How to start

+ Check the documentation [(In spanish)](https://docs.google.com/document/d/1wuJT3hf586eP1Cgk791YWSEfBMrYi3ZrScmb3qDwFg4/edit?usp=sharing)
+ Configure routes for navigation
  + Currently it's done in :framework/redux/controllers/navigation/targets using navigation target groups.
+ Configure [live templates in IDE](https://docs.google.com/document/d/1wuJT3hf586eP1Cgk791YWSEfBMrYi3ZrScmb3qDwFg4/edit#heading=h.k7ovjem11yu9)

+ Check project build config
  + Change project name in ./settings.gradle
  + Update core dependencies in ./projectBuildSettings/projectDependencies.gradle
  + Update content dependencies in ./projectBuildSettings/projectDependenciesFramework.gradle
+ Configure Firebase account (Optional)

  + Uncomment id 'com.google.gms.google-services' in build.gradle for :framework module
  + Follow the Firebase [setup instructions](https://firebase.google.com/docs/android/setup).
+ Configure main gate (Useful for login) (Optional)

  + In app module > StartActivity > StartAction apply gate.

# Project structure

There are tree kinds of modules: Core, Content and Helpers.

### Core

Contains all the common functionalities of the application, connect all the modules and persist
the general application state. This is the :framework module.

### Content

Contains functionalities that aren't necessary for other modules.
[Check the directory structure for a Content type module.](https://docs.google.com/document/d/1wuJT3hf586eP1Cgk791YWSEfBMrYi3ZrScmb3qDwFg4/edit#heading=h.tn6pvjskp2wu)

### Helpers

Generates code for the core and the content type modules. KSP is an example of this.

## Considerations

+ This project is based on the Single Activity principle
+ Fragments are not recommended, use compose instead.

# Licence

The Kortisan framework is open-sourced software licensed under the
[MIT license](https://opensource.org/licenses/MIT).
