package cs151.application;

import cs151.application.StudentProfile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class ReportController {

    @FXML
    private TableView<StudentProfile> tableView;

    @FXML
    private TableColumn<StudentProfile, String> nameCol;

    @FXML
    private TableColumn<StudentProfile, String> listCol;

    private ObservableList<StudentProfile> allProfiles;

    @FXML
    public void initialize() {
        DataStore.load();
        allProfiles = DataStore.getFullName();

        tableView.setItems(allProfiles);

        tableView.setRowFactory(tv -> {
            javafx.scene.control.TableRow<StudentProfile> row = new javafx.scene.control.TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    StudentProfile selected = row.getItem();
                    openStudentDetailPage(selected);
                }
            });

            return row;
        });
    }

    @FXML
    private void getBReport(ActionEvent event) {
        List<StudentProfile> filtered = allProfiles.stream().filter(StudentProfile::isBlackList).collect(Collectors.toList());

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        listCol.setCellValueFactory(cellData -> {
            StudentProfile sp = cellData.getValue();
            String status = sp.isBlackList() ? "Blacklisted" :
                            "None";
            return new javafx.beans.property.SimpleStringProperty(status);
        });

        tableView.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void getWReport(ActionEvent event) {
        List<StudentProfile> filtered = allProfiles.stream().filter(StudentProfile::isWhiteList).collect(Collectors.toList());

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        listCol.setCellValueFactory(cellData -> {
            StudentProfile sp = cellData.getValue();
            String status = sp.isWhiteList() ? "Whitelisted" :
                            "None";
            return new javafx.beans.property.SimpleStringProperty(status);
        });

        tableView.setItems(FXCollections.observableArrayList(filtered));
    }

    private void openStudentDetailPage(StudentProfile student) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/cs151/application/student-report-detail.fxml")
            );
            Parent root = loader.load();

            // Pass the selected student to the new controller
            StudentReportDetailController controller = loader.getController();
            controller.setStudent(student);

            // Switch scene in the same window
            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.setTitle("Student Report Detail");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void goBackToHome(ActionEvent event) {
        swapScene(event, "/cs151/application/home.fxml", 340, 260, "KnowledgeTrack Home");
    }

    private void swapScene(ActionEvent event, String fxml, int width, int height, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}