JavaFX with OpenJDK 11:
1. Download SDK from https://gluonhq.com/products/javafx/ and extract to /some/library/path/javafxsdk
2. Add "--module-path /some/library/path/javafxsdk/lib" to application VM arguments.
3. Add "--add-modules javafx.controls,javafx.fxml,javafx.graphics" to application VM arguments.
4. For run JavaFX on Linux  GTK+ required. For Fedora: https://developer.fedoraproject.org/tech/languages/c/gtk.html 
