/*
 * Copyright (C) 2010 LG CNS Inc.
 * All rights reserved.
 *
 * 모든 권한은 LG CNS(http://www.lgcns.com)에 있으며,
 * LG CNS의 허락없이 소스 및 이진형식으로 재배포, 사용하는 행위를 금지합니다.
 */
package sp.io.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TODO Javadoc주석작성
 *
 * @author R11222
 * @version $Id: lgcns-code-templates-java.xml 426 2012-04-10 07:49:33Z designtker $
 */
public class SendFile {

    public void send() {
        String srcDir = "";
        Socket socket = null;
        DataOutputStream os = null;
        try {
            byte[] buffer = new byte[4096];
            int length;
            socket = new Socket("localhost", 55555);
            os = new DataOutputStream(socket.getOutputStream());

            File directory = new File(srcDir);
            File[] fList = directory.listFiles();
            for (File file : fList) {
                if (file.isFile()) {
                    os.writeUTF(file.getName());
                    os.writeInt((int) file.length());

                    FileInputStream is = null;
                    try {
                        is = new FileInputStream(file.getPath());
                        while ((length = is.read(buffer)) != -1) {
                            os.write(buffer, 0, length);
                        }
                    } finally {
                        if (is != null) { is.close(); }
                    }

                    // file move
                    // TODO
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receive() {
        try (
                ServerSocket serverSocket = new ServerSocket(55555)
                ) {
            int length = 0;
            byte[] buffer = new byte[4096];

            ;
            while (true) {
                DataInputStream is = null;
                try (
                        Socket socket = serverSocket.accept();
                        ){
                    is = new DataInputStream(socket.getInputStream());
                    while (true) {
                        String fileName = is.readUTF();
                        int fileSize = is.readInt();

                        while (fileSize > 0) {
                            length = is.read(buffer, 0, Math.min(fileSize, buffer.length));
                            fileSize -= length;
                            writeFile(fileName, buffer, 0, length);
                        }
                    }
                } catch (EOFException e) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFile(String fileName, byte[] buffer, int offset, int length)
            throws IOException {
        String dstDir = "";
        try (
                FileOutputStream fw = new FileOutputStream(dstDir+ fileName, true);
                ){
            fw.write(buffer, offset, length);
        }
    }
}
