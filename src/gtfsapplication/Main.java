/*
 * Course: SE 2030
 * Fall 2020
 * GTFS Application
 * Name: SE 2030 011 Team C
 * Created: 10/11/2020
 * MIT License
 *
 * Copyright (c) 2020 Team C (Kenneth McDonough, William Lauer, Thy Lee, Luke Miller)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package gtfsapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Driver
 *
 * @author Kenneth McDonough, William Lauer, Luke Miller, Thy Le
 * @version 10/6/2020
 */
public class Main extends Application {

    private final int width = 900;
    private final int height = 600;

    private final int width2 = 500;
    private final int height2 = 400;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader main = new FXMLLoader();
        Parent root = main.load(getClass().getResource("gtfs.fxml").openStream());
        primaryStage.setTitle("GTFS Application");
        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.setResizable(false);
        primaryStage.show();
        Controller mainController = main.getController();

        FXMLLoader second = new FXMLLoader();
        Parent secondRoot = second.load(getClass().getResource("secWindow.fxml").openStream());
        Stage secondStage = new Stage();
        secondStage.setTitle("Update Attributes");
        secondStage.setScene(new Scene(secondRoot, width2, height2));
        secondStage.hide();
        SecondController secondController = second.getController();

        mainController.setSecStage(secondStage);
        mainController.setSecondController(secondController);
        secondController.setMainStage(primaryStage);
        secondController.setMainController(mainController);
    }


    public static void main(String[] args) {
        launch(args);
    }
}


