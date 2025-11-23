package cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class StudentReportDetailController {

    @FXML private Label lblName;
    @FXML private Label lblMajor;
    @FXML private Label lblStatus;
    @FXML private Label lblEmployment;
    @FXML private Label lblJobDetails;
    @FXML private Label lblLanguages;
    @FXML private Label lblPreferredRole;
    @FXML private TableView<CommentRow> commentsTable;
    @FXML private TableColumn<CommentRow, String> dateColumn;
    @FXML private TableColumn<CommentRow, String> commentColumn;

    @FXML
    public void initialize() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("commentPreview"));

        commentsTable.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                CommentRow selected = commentsTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openFullCommentDialog(selected.getFullComment());
                }
            }
        });
    }

    public void setStudent(StudentProfile student) {

        lblName.setText("Name: " + student.getName());
        lblMajor.setText("Major: " + student.getMajor());
        lblStatus.setText("Academic Status: " + student.getAcademicStatus());
        lblEmployment.setText("Employment: " + (student.isEmployed() ? "Employed" : "Not Employed"));
        lblJobDetails.setText("Job Details: " + (student.getJobDetails() != null ? student.getJobDetails() : "N/A"));

        String languages = student.getLanguages() != null && !student.getLanguages().isEmpty()
                ? String.join(", ", student.getLanguages())
                : "None listed";
        lblLanguages.setText("Languages: " + languages);

        lblPreferredRole.setText("Preferred Role: " +
                (student.getPreferredRole() != null ? student.getPreferredRole() : "N/A"));

        loadComments(student.getCommentList());
    }

    private void loadComments(List<Comment> comments) {
        ObservableList<CommentRow> rows = FXCollections.observableArrayList();

        for (Comment comment : comments) {
            String fullText = comment.getText() != null ? comment.getText() : "";
            String preview = fullText.length() > 50
                    ? fullText.substring(0, 50) + "..."
                    : fullText;
            String date = comment.getDate() != null && !comment.getDate().isEmpty()
                    ? comment.getDate()
                    : "No Date";

            rows.add(new CommentRow(date, preview, fullText));
        }

        commentsTable.setItems(rows);
    }

    private void openFullCommentDialog(String fullComment) {
        try {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Full Comment");

            TextArea textArea = new TextArea(fullComment);
            textArea.setWrapText(true);
            textArea.setEditable(false);
            textArea.setPrefRowCount(10);
            textArea.setPrefColumnCount(50);

            Button closeButton = new Button("Close");
            closeButton.setOnAction(e -> dialog.close());

            javafx.scene.layout.VBox vbox = new javafx.scene.layout.VBox(10);
            vbox.setPadding(new javafx.geometry.Insets(15));
            vbox.getChildren().addAll(
                    new Label("Full Comment:"),
                    textArea,
                    closeButton
            );

            Scene scene = new Scene(vbox, 500, 300);
            dialog.setScene(scene);
            dialog.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Failed to display full comment: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleBackToReports(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/cs151/application/reports.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Reports");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class CommentRow {
        private final String date;
        private final String commentPreview;
        private final String fullComment;

        public CommentRow(String date, String commentPreview, String fullComment) {
            this.date = date;
            this.commentPreview = commentPreview;
            this.fullComment = fullComment;
        }

        public String getDate() {
            return date;
        }

        public String getCommentPreview() {
            return commentPreview;
        }

        public String getFullComment() {
            return fullComment;
        }
    }
}