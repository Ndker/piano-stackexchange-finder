package ru.ndker.piano.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import org.springframework.stereotype.Controller;

@Controller
public class MainController {

    @FXML
    private Button load;

    @FXML
    public void onClickLoad() {
        System.out.println("Loading...");
    }
}
