package main;

import vista.LoginView;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    LoginView login =
                            new LoginView();

                    login.setVisible(
                            true
                    );
                }
        );
    }
}