package homework2.api;

import java.io.IOException;

public class Main {

    //Пример чистой архитектуры
    public static void main(String[] args) {
        try {
            Application app = new Application();
            app.start();
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
