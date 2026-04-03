public class CheckJavaFx {
    public static void main(String[] args) {
        try {
            Class.forName("javafx.scene.media.Media");
            System.out.println("JavaFX media available");
        } catch (ClassNotFoundException e) {
            System.out.println("JavaFX media not available");
        }
    }
}
