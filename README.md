# rtl4
This is a port of **rtl** version 3.0 to USE 4.2.0 (https://sourceforge.net/projects/useocl/).

## How to build
The source code is available as an Eclipse project. From Eclipse's main window, go to *File - Import - Existing Projects into Workspace* and browse to the project's root directory.

The imported project depends on a project named `use-4.2.0` in the same workspace, which is USE's source code. In order to change this dependency, go to *Projects - Properties - Java Build Path - Projects*.

To use the plugin, export the project as a JAR file: Choose *File - Export - JAR File* and select the project's source files. In the *JAR Manifest Specification* step, browse to `META-INF/MANIFEST.MF`. Place the exported JAR file into USE's plugin directory (`lib/plugins`).

Prebuilt binaries are available on GitHub under the *Releases* section.

## How to use
Demo files are available on **RestrictedGraphTrafo**'s repository (https://github.com/vnu-dse/rtl), under `demo`.

* Open the source metamodel's USE specification file.

* Click the *Restricted Graph Trafo Parser* button (the RTL icon). Specify the target metamodel (`.use` file) and the transformation rules file (`.tgg`).

* Transformation rules are shown in a new window. Click on a rule to visualise it.

* Create objects using the `.soil/.cmd` files provided with the demos: From the USE command line, type `open /path/to/file`. Then create the *object diagram* view to visualise the transformations.

* The plugin provides these functions:
 * *Next match*: Find the next rule that matches the objects
 * *Previous match*: Go back to the last found rule
 * *Run match*: Execute the current transformation
 * *Auto run forward*: Execute all transformations
 * *Run test case*: Validate the executed transformations

* Changes to the objects are reflected in the *Object diagram* window.
