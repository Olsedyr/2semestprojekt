package com.example.demo1;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;



public class HelloController implements Initializable{

    @FXML
    private WebView webView;

    @FXML
    private WebView webView2;

    private WebEngine engine;

    private String url2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        engine = webView.getEngine();
        engine.load("https://www.komplett.dk/category/28003/hardware/pc-komponenter");

        engine = webView2.getEngine();
        engine.load("https://www.instructables.com/How-To-Replace-the-Processor-in-a-Desktop-Computer/");
    }


}