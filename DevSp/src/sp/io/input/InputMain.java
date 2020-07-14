package sp.io.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputMain {

    /**
     * https://www.baeldung.com/bufferedreader-vs-console-vs-scanner-in-java
     */
    public void keyin() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String strInput = "";
        while (true) {
            strInput = br.readLine();
            if("B".equals(strInput)) {
                break;
            }
            System.out.println(strInput);
        }
        System.out.println("Exit");
    }

    public static void main(String[] args) throws Exception {
        InputMain m = new InputMain();
        m.keyin();
    }
}
