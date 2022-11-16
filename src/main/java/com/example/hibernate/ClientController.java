package com.example.hibernate;

import com.example.hibernate.entity.Shop;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.WindowEvent;
import com.example.hibernate.service.ShopService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ClientController {
    private ObservableList<Shop> tableData = FXCollections.observableArrayList();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button AddButton;

    @FXML
    private Button DeleteButton;

    @FXML
    private Button UpdateButton;

    @FXML
    private Button UpdateDataBaseButton;

    @FXML
    private Button ExitButton;

    @FXML
    private Button ClearButton;

    @FXML
    private TextField amount_of_workers_field;

    @FXML
    private TextField average_salary_field;

    @FXML
    private TextArea information_field;

    @FXML
    private TextField name_field;

    @FXML
    private TextField profit_field;

    @FXML
    private TextField square_field;

    @FXML
    private TableColumn<?, ?> col_amount_of_workers;

    @FXML
    private TableColumn<?, ?> col_average_salary;

    @FXML
    private TableColumn<?, ?> col_id;

    @FXML
    private TableColumn<?, ?> col_name;

    @FXML
    private TableColumn<?, ?> col_profit;

    @FXML
    private TableColumn<?, ?> col_square;

    @FXML
    private TableView<Shop> table;

    private ShopService service;
    @FXML
    void initialize() {
        information_field.setText("Connecting to server... Please, wait...");
        service = new ShopService();
        information_field.setText("You are successfully connected.");

        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_profit.setCellValueFactory(new PropertyValueFactory<>("profit"));
        col_amount_of_workers.setCellValueFactory(new PropertyValueFactory<>("amountOfWorkers"));
        col_average_salary.setCellValueFactory(new PropertyValueFactory<>("averageSalary"));
        col_square.setCellValueFactory(new PropertyValueFactory<>("square"));

        loadDataFromDB();

        UpdateDataBaseButton.setOnAction(actionEvent -> {
            loadDataFromDB();
            information_field.setText("You successfully updated the database!");
        });

        AddButton.setOnAction(actionEvent -> {

            if (!name_field.getText().trim().equals("")) {
                if(isInputDataCorrect()) {
                    Shop shop = getShopFromInputData();
                    shop.setName(name_field.getText());

                    service.addNewShop(shop);
                    loadDataFromDB();
                    information_field.setText("You successfully created new shop!");
                }
            } else {
                information_field.setText("You must input shop name before creating new Shop!");
            }
        });

        DeleteButton.setOnAction(actionEvent -> {
            if (table.getSelectionModel().getSelectedItem() != null) {

                service.deleteByID(table.getSelectionModel().getSelectedItem().getId());
                information_field.setText("You successfully deleted one shop!");
            }
            loadDataFromDB();
        });

        UpdateButton.setOnAction(actionEvent -> {
            if (!name_field.getText().trim().equals("")
                    && !profit_field.getText().equals("")
                    && !square_field.getText().equals("")
                    && !average_salary_field.getText().equals("")
                    && !amount_of_workers_field.getText().equals("")) {
                if(isInputDataCorrect()) {
                    Shop shop = getShopFromInputData();
                    shop.setId(table.getSelectionModel().getSelectedItem().getId());
                    service.updateShop(shop);
                    loadDataFromDB();
                    information_field.setText("You successfully update one shop!");
                }
            } else {
                information_field.setText("You must fill in all fields before updating this shop!");
            }
        });

        ExitButton.setOnAction(actionEvent -> {
            service.disConnect();
            System.exit(0);
        });


        ClearButton.setOnAction(actionEvent -> {
            name_field.clear();
            profit_field.clear();
            average_salary_field.clear();
            amount_of_workers_field.clear();
            square_field.clear();
        });
    }

    private void loadDataFromDB() {
        tableData.clear();
        List<Shop> tempList = service.getAllStrings();
        tableData = FXCollections.observableArrayList(tempList);
        table.setItems(tableData);
    }

    public void onSelectOneRecord(javafx.scene.input.MouseEvent mouseEvent) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            Shop selectedShop = table.getSelectionModel().getSelectedItem();
            name_field.setText(selectedShop.getName());
            profit_field.setText(String.valueOf(selectedShop.getProfit()));
            amount_of_workers_field.setText(String.valueOf(selectedShop.getAmountOfWorkers()));
            square_field.setText(String.valueOf(selectedShop.getSquare()));
            average_salary_field.setText(String.valueOf(selectedShop.getAverageSalary()));
        }
    }

    private Shop getShopFromInputData(){
        Shop shop = new Shop();
        shop.setName(!name_field.getText().equals("") ? name_field.getText() : "");
        shop.setProfit(!profit_field.getText().equals("") ? Float.parseFloat(profit_field.getText().replace(",", ".")) : 0f);
        shop.setAverageSalary(!average_salary_field.getText().equals("") ? Float.parseFloat(average_salary_field.getText().replace(",", ".")) : 0f);
        shop.setSquare(!square_field.getText().equals("") ? Float.parseFloat(square_field.getText().replace(",", ".")) : 0f);
        shop.setAmountOfWorkers(!amount_of_workers_field.getText().equals("") ? Integer.parseInt(amount_of_workers_field.getText()) : 0);
        return shop;
    }

    private boolean isInputDataCorrect(){
        boolean flag = true;
        if(!new Scanner(average_salary_field.getText().replace(".", ",")).hasNextFloat()) {
            average_salary_field.setText("INCORRECT");
            flag = false;
        }
        if(!new Scanner(square_field.getText().replace(".", ",")).hasNextFloat()) {
            square_field.setText("INCORRECT");
            flag = false;
        }
        if(!new Scanner(amount_of_workers_field.getText()).hasNextInt()) {
            amount_of_workers_field.setText("INCORRECT");
            flag = false;
        }
        if(!new Scanner(profit_field.getText().replace(".", ",")).hasNextFloat()) {
            profit_field.setText("INCORRECT");
            flag = false;
        }
        if(!flag){
            information_field.setText("Input data is incorrect. Use FLOAT type to PROFIT, SQUARE, SALARY and INT type to AMOUNT.");
        }
        return flag;
    }

    public final EventHandler<WindowEvent> closeEventHandler = new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent windowEvent) {
            service.disConnect();
            System.exit(0);
        }
    };
}