# Kortisan Android Framework
Kortisan a mobile framework aimed to implement the best practices and help to build big and 
large-scale applications, prioritising code reusability and modularity.

# How to start
+ Check the documentation [(In spanish)](https://docs.google.com/document/d/1wuJT3hf586eP1Cgk791YWSEfBMrYi3ZrScmb3qDwFg4/edit?usp=sharing)
+ Configure live templates in IDE
  + In File > Settings menu, look for Editor > File and Code Templates
    + Configure your File Header.java
    + Add the fileTemplates included in the project all of these are .kt:
        + This files must be suited in your personalFolder/.config/Google/AndroidStudio202x.x/fileTemplates 
  
  #### Business Logic Component (BLoC)

  | File in project | FileName for template |
  |------|----------|
  | Business Logic Component (BLoC).kt | ./presentation/components/${NAME}Bloc |
  | Business Logic Component (BLoC).kt.child.0.kt | ./presentation/components/${NAME}Component |
  | Business Logic Component (BLoC).kt.child.1.kt | ./presentation/tagging/decorators/${NAME}SceneDecorator |
  | Business Logic Component (BLoC).kt.child.2.kt | ./presentation/actions/${NAME}Actions |
  | Business Logic Component (BLoC).kt.child.3.kt | ./domain/models/${NAME}Model |
  | Business Logic Component (BLoC).kt.child.4.kt | ./presentation/states/${NAME}State |

  #### Content caretaker

  | File in project | FileName for template |
  |------|----------|
  | Content Caretaker.kt | ./presentation/states/caretakers/${NAME}CaretakerStrategy |

  #### Storage Security Strategy

  | File in project | FileName for template |
  |------|----------|
  | Storage Security Strategy.kt | ./storage/security/${NAME}SecurityStrategy |

  #### ViewModel with ActionDispatcher

  | File in project | FileName for template |
  |------|----------|
  | ViewModel with ActionDispatcher.kt | ./domain/viewmodels/${NAME}ViewModel |
  | ViewModel with ActionDispatcher.kt.child.0.kt | ./presentation/tagging/${NAME}SceneBuilder |

  #### Redux Framework Gate
  Note: Only for the Framework module

  | File in project | FileName for template |
  |------|----------|
  | Framework Gate.kt | ./redux/gates/${NAME}Gate |

  #### KSP Annotation

  | File in project | FileName for template |
  |------|----------|
  | KSP Annotation.kt | ./${NAME}Annotation |
  | KSP Annotation.kt.child.0.kt | ./${NAME}Processor |
  | KSP Annotation.kt.child.1.kt | ./${NAME}ProcessorProvider |

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

## Core
Contains all the common functionalities of the application, connect all the modules and persist
the general application state.

## Content
Contains functionalities that aren't necessary for other modules. This is the framework module.

### Directory structure for Content type module
+ DOMAIN
    + MODELS
        + ItemModel FileName: ./domain/models/${NAME}Model
    + SectionViewModel FileName: ./domain/viewmodels/${NAME}ViewModel

+ STORAGE
    + LOCAL
        + DATABASE
            + DAO
            + RELATIONS
            + TABLES
        + DATASTORE
            + SERIALIZERS
            + VAULTS
    + REMOTE
        + ${NAME}SERVICE FileName: ${NAME}Service
            + DTO
                + REQUESTS
                    + ItemRequest
                + RESPONSES
                    + ItemResponse
    + REPOSITORIES
        + SectionRepository FileName: ./storage/repositories/${NAME}Repository
    + SECURITY
        + ItemSecurityStrategy FileName: ./storage/security/${NAME}SecurityStrategy

+ GATES
    + SectionGate FileName: ./gates/${NAME}Gate

+ PRESENTATION
    + SectionActivity FileName: ./presentation/${NAME}Activity
    + ACTIONS
        + ItemActions FileName: ./presentation/actions/${NAME}Actions
    + BLOCS
        + ItemBloc FileName: ./presentation/blocs/${NAME}Bloc
    + COMPONENTS
        + SubItemComponent FileName: ./presentation/components/${NAME}Component
    + STATES
        + ItemState FileName: ./presentation/states/${NAME}State
        + CARETAKERS
           + ItemCaretaker FileName: ./presentation/states/caretakers/${NAME}CaretakerStrategy
    + TAGGING
        + SectionSceneBuilder FileName: ./presentation/tagging/${NAME}SceneBuilder
        + DECORATORS
            + ItemDecorator FileName: ./presentation/tagging/decorators/${NAME}SceneDecorator

## Helpers
Generates code for the core and the content type modules. KSP is an example of this.

## Considerations
+ This project is based on the Single Activity principle
+ Fragments are not recommended, use compose instead.

# Licence
The Kortisan framework is open-sourced software licensed under the
[MIT license](https://opensource.org/licenses/MIT).
