# Stuffleading system
This document describes the design according to the requirements presented in assignment 2.

## Architectural Overview
The application uses the model-view-controller (MVC) architectural pattern. The view is passive and gets called from the controller. The view may only read information from the model, not directly change it.

![class diagram](img/classDiagram.png)
![Sequence diagram](img/SequenceDiagram.png)
![Object diagram](img/ObjectDiagram.png)