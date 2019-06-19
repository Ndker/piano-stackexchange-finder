package ru.ndker.piano.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.springframework.stereotype.Controller;

import ru.ndker.piano.model.SearchResult;

import java.util.List;

@Controller
public class SearchResultController {
    @FXML
    private TableColumn<Integer, SearchResult> id;
    @FXML
    private TableColumn<String, SearchResult> date;
    @FXML
    private TableColumn<Integer, SearchResult> title;
    @FXML
    private TableColumn<String, SearchResult> author;
    @FXML
    private TableColumn<String, SearchResult> sourceLink;
    @FXML
    private TableView<SearchResult> searchResultTable;

    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        sourceLink.setCellValueFactory(new PropertyValueFactory<>("sourceLink"));
    }

    public void fillTable(List<SearchResult> results) {
        searchResultTable.setItems(FXCollections.observableArrayList(results));
    }
}
