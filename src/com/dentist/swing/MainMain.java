package com.dentist.swing;

import io.socket.client.IO;
import io.socket.client.Socket;

import javax.swing.*;
import java.net.URISyntaxException;

public class MainMain {

    private static String URL = "http://localhost:3000";
    //    private static String URL = "http://104.236.122.187:3000";

    private static Socket socket;

    public static void main(String[] args) {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
            // handle exception
        }
        catch (ClassNotFoundException e) {
            // handle exception
        }
        catch (InstantiationException e) {
            // handle exception
        }
        catch (IllegalAccessException e) {
            // handle exception
        }

        try {
            socket = IO.socket(URL);
        } catch (URISyntaxException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }

        new MostOuter(socket);
    }
}
