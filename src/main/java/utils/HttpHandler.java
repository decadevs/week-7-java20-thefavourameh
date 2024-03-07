package utils;

import java.io.*;
import java.net.Socket;


import static commons.MyHttpServer.HTML_FILE_PATH;
import static commons.MyHttpServer.JSON_FILE_PATH;

public class HttpHandler implements Runnable {
    private Socket socket;

    public HttpHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            String request = input.readLine();

            if (request != null) {
                String[] parts = request.split("\\s+");
                if (parts.length >= 2 && parts[0].equals("GET")) {
                    String path = parts[1];
                    switch (path) {
                        case "/":
                        case "/index.html":
                            returnNewHtmlResponse(output);
                            break;
                        case "/json":
                            returnNewJsonResponse(output);
                            break;
                        default:
                            returnNewNotFoundResponse(output);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void returnNewHtmlResponse(BufferedWriter output) throws IOException {
        File file = new File(HTML_FILE_PATH);

        if (file.exists()) {
            sendResponse(output, file, "text/html");
        } else {
            returnNewNotFoundResponse(output);
        }
    }

    private void returnNewJsonResponse(BufferedWriter output) throws IOException {
        File file = new File(JSON_FILE_PATH);

        if (file.exists()) {
            sendResponse(output, file, "application/json");
        } else {
            returnNewNotFoundResponse(output);
        }
    }

    private void returnNewNotFoundResponse(BufferedWriter output) throws IOException {
        output.write("HTTP/1.1 404 Not Found\r\n");
        output.write("Content-type: text/plain\r\n");
        output.write("\r\n");
        output.write("404 Not Found - The requested resource was not found on this server");
        output.flush();
    }

    private void sendResponse(BufferedWriter output, File file, String contentType) throws IOException {
        output.write("HTTP/1.1 200 OK\r\n");
        output.write("Content-type: " + contentType + "\r\n");
        output.write("\r\n");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.write(line + "\r\n");
            }
        }
        output.flush();
    }

}
