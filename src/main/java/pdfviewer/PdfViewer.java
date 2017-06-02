/*
 * Copyright (c) TAKAHASHI,Toru 2015
 */
package pdfviewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * PDF文書�?�ページを分割�?��?�り�?�?�?��?��?�り�?��?��?��?�簡易�?��?作を行�?�ツール。
 * 
 * @author toru
 */
public class PdfViewer extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {

//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("/PdfView.fxml"));
//        Parent content = loader.load();

        Parent root = FXMLLoader.load(getClass().getResource("/PdfView.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("PDF simple viewer by PDFBox");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
