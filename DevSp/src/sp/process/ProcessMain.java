package sp.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessMain {

    /**
     * https://mkyong.com/java/java-processbuilder-examples/
     */
    public void byProcessBuilder() {
        ProcessBuilder processBuilder = new ProcessBuilder();

        // Run this on Windows, cmd, /c = terminate after this run
        processBuilder.command("cmd.exe", "/c", "ping -n 3 google.com");
        //processBuilder.directory(new File("C:\\users"));

        try {
            Process process = processBuilder.start();

            // blocked :(
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * https://www.baeldung.com/java-lang-processbuilder-api
     */
    public void byProcessBuilderRedirectOutput() {
        ProcessBuilder processBuilder = new ProcessBuilder();

        // Run this on Windows, cmd, /c = terminate after this run
        processBuilder.command("cmd.exe", "/c", "ping -n 3 google.com");
        //processBuilder.directory(new File("C:\\users"));

        processBuilder.redirectOutput(new File("L:\\ping.out"));

        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ProcessMain m = new ProcessMain();
        m.byProcessBuilder();
        m.byProcessBuilderRedirectOutput();
    }
}
