package sp.io.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileMain {

    //https://mkyong.com/java8/java-8-stream-read-a-file-line-by-line/
    public void readFile() {
        try {
            List<String> list = Files.lines(Paths.get("D:\\test.py")).collect(Collectors.toList());
            System.out.println(list);

            list = Files.readAllLines(Paths.get("D:\\test.py"));
            System.out.println(list);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void writeFile() throws IOException {
        //https://www.baeldung.com/java-write-to-file

        String str = "hello";

        //write with file class
        Path path = Paths.get("write_file.out");
        byte[] strToBytes = str.getBytes();

        Files.write(path, strToBytes);
        String read = Files.readAllLines(path).get(0);
        assert read == str;
        System.out.println(path.toAbsolutePath());

        //write with bufferedWriter
        try (
                BufferedWriter bw = new BufferedWriter(new FileWriter(path.toFile()));
                ) {
            bw.write(str);
        }
    }

    public void moveFile() throws IOException {
        String srcPath = "";
        String dstPath = "";
        File fileFrom = new File(srcPath);
        File fileTo = new File(dstPath);
        //overwrite
        fileTo.delete();
        fileFrom.renameTo(fileTo);
    }

    public void listFile() throws IOException {
        File directory = new File("D:\\");
        File[] list = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isFile() && file.getName().startsWith("prefix_");
            }
        });
    }

    public static void main(String[] args) throws Exception {
        FileMain m = new FileMain();
        m.readFile();
        m.writeFile();
    }
}
