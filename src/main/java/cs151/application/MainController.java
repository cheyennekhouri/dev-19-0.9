package cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {
    @FXML private Label popUp;
    @FXML private TextField nameField;
    @FXML private TextField languageField;

    @FXML private TableView<ProgrammingLanguages> tableView;
    @FXML private TableView<StudentProfile> nameTable;

    @FXML private TableColumn<ProgrammingLanguages, String> langCol;
    @FXML private TableColumn<StudentProfile, String> nameCol;
    @FXML private ListView<String> languagesList;

    @FXML private ComboBox<String> dropdown, dropDown;
    @FXML private RadioButton toggleButton;
    @FXML private TextField textField;

    @FXML private TableColumn<StudentProfile, String> statusCol, empCol, roleCol;
    @FXML private ListView<String> multiSelectListView;

    @FXML
    public void initializer() {
        ObservableList<String> items = FXCollections.observableArrayList("MySQL", "Postgres", "MongoDB");
        if (multiSelectListView != null) {
            multiSelectListView.setItems(items);
            multiSelectListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }
    }

    @FXML
    private void initialize() {
        // Programming languages table
        if (tableView != null) {
            langCol.setCellValueFactory(new PropertyValueFactory<>("programmingLanguage"));
            ObservableList<ProgrammingLanguages> langs = DataStore.getList(); // use backing list
            tableView.setItems(langs);
            langCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.5));
        }

        // Multi-select ListView for programming languages
        if (languagesList != null) {
            ObservableList<String> opts = FXCollections.observableArrayList(
                    DataStore.getList().stream()
                            .map(ProgrammingLanguages::getProgrammingLanguage).collect(Collectors.toList())
            );
            languagesList.setItems(opts);
            languagesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }

        // Multi-select ListView for databases
        if (multiSelectListView != null && (multiSelectListView.getItems() == null || multiSelectListView.getItems().isEmpty())) {
            multiSelectListView.setItems(FXCollections.observableArrayList("MySQL", "Postgres", "MongoDB"));
            multiSelectListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }

        // Student profiles table
        if (nameTable != null && nameCol != null) {
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            ObservableList<StudentProfile> profiles = DataStore.getFullName(); // backing list
            nameTable.setItems(profiles);
            nameTable.getItems().sort(Comparator.comparing(
                    StudentProfile::getName, String.CASE_INSENSITIVE_ORDER));
            nameCol.prefWidthProperty().bind(nameTable.widthProperty().multiply(0.5));
        }
    }

    //navigate to home page
    @FXML
    protected void onNavigateButtonClick(ActionEvent event) {
        swapScene(event, "/cs151/application/hello-view.fxml", 320, 240, "KnowledgeTrack");
    }
    //home page
    @FXML
    private void goBackToHome(ActionEvent event) {
        swapScene(event, "/cs151/application/home.fxml", 380, 340, "KnowledgeTrack Home");
    }
    //programming languages table, contains: table, back to define lang
    @FXML
    protected void programmingLanguagesTable(ActionEvent event){
        swapScene(event, "/cs151/application/program_table.fxml", 400, 300, "Saved Languages");
    }
    //student profile, contains: form, save button, saved profiles, search profiles
    @FXML
    protected void studentProfile(ActionEvent event) {
        swapScene(event, "/cs151/application/student.fxml", 600, 400, "Student Profile");
    }
    //saved profiles, contains: table, back to student profile
    @FXML
    protected void savedProfile(ActionEvent event) {
        swapScene(event, "/cs151/application/saved_profile.fxml", 400, 300, "Saved Profiles");
    }
    //define programming languages
    @FXML
    protected void programmingLang(ActionEvent event) {
        swapScene(event, "/cs151/application/programming_languages.fxml", 640, 420, "Programming Languages");
    }

    //search page
    @FXML
    protected void searchProf(ActionEvent event) {
        swapScene(event, "/cs151/application/search.fxml", 1000, 680, "Search Student Profiles");
    }

    @FXML
    protected void report(ActionEvent event) {
        swapScene(event, "/cs151/application/reports.fxml", 600, 500, "Show Reports");
    }

    private boolean requiredFields() {
        String name   = nameField != null && nameField.getText() != null ? nameField.getText().trim() : "";
        String status = dropdown  != null && dropdown.getValue() != null ? dropdown.getValue().trim() : "";
        String role   = dropDown  != null && dropDown.getValue() != null ? dropDown.getValue().trim() : "";
        boolean employed = toggleButton != null && toggleButton.isSelected();
        String job    = textField != null && textField.getText() != null ? textField.getText().trim() : "";

        if (name.isEmpty())   return error("Full Name is required.");
        if (status.isEmpty()) return error("Academic Status is required.");
        if (role.isEmpty())   return error("Preferred Professional Role is required.");
        if (employed && job.isEmpty()) return error("Job details are required when Employed.");
        return true;
    }
    private boolean error(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
        return false;
    }

    //saves languages
    @FXML
    private void onSave() {
        if (languageField == null) return;

        var langsList = DataStore.getList();
        if (langsList.size() >= 3) {
            error("Only 3 programming languages are allowed.");
            return;
        }
        String lang  = languageField.getText() == null ? "" : languageField.getText().trim();
        if (lang.isEmpty()) return;
        boolean duplicate = langsList.stream().anyMatch(pl ->
                pl.getProgrammingLanguage() != null &&
                        pl.getProgrammingLanguage().equalsIgnoreCase(lang));
        if (duplicate) {
            error("Language already exists.");
            return;
        }
        langsList.add(new ProgrammingLanguages(lang));
        DataStore.save();
        languageField.clear();
    }

    //saves profile
    @FXML
    private void save() {
        if (!requiredFields()) return;
        final String name     = nameField.getText().trim();

        var profiles = DataStore.getFullName();
        StudentProfile target = null;
        for (StudentProfile sp : profiles) {
            if (sp.getName() != null && sp.getName().equalsIgnoreCase(name)) {
                target = sp; break;
            }
        }
        if (target == null && profiles.size() >= 5) {
            error("Only 5 student profiles are allowed.");
            return;
        }
        final String status   = dropdown.getValue();
        final boolean employed= toggleButton.isSelected();
        final String job      = textField.getText().trim();
        final String role     = dropDown.getValue();

        if (target == null) {
            target = new StudentProfile(name);
            DataStore.getFullName().add(target);
        }
        target.setAcademicStatus(status);
        target.setEmployeed(employed);
        target.setJobDetails(job);
        target.setPreferredRole(role);

        DataStore.saveProfiles();
    }
    private void swapScene(ActionEvent event, String fxml, int w, int h, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root, w, h));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}