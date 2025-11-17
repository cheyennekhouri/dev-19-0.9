package cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.control.ListView;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CommentController {

    private StudentProfile current;
    @FXML private Label lblStudentName;
    @FXML private TextArea taNewComment;
    @FXML private ListView<String> lvComments;
    @FXML
    public void initialize(){
        if(lvComments != null){
            lvComments.setItems(FXCollections.observableArrayList());
        }
    }

    public void loadComments(StudentProfile p) {
        this.current = p;
        if(lblStudentName != null) lblStudentName.setText("Comments for: " + p.getName());
        refreshListView();
        if(taNewComment != null) taNewComment.clear();
        System.out.println("[Comments] Loaded: " + p.getName());
    }

    public void refreshListView(){
        if(current == null || lvComments == null) return;
        List<String> lines = current.getCommentList().stream().map(c -> {
                    if (c == null) return "";
                    String text = c.getText() == null ? "" : c.getText();
                    if (c.getDate() == null || c.getDate().isEmpty()) return text;
                    return c.getDate() + " - " + text;
                }).collect(Collectors.toList());
        ObservableList<String> items = FXCollections.observableArrayList(lines);
        lvComments.setItems(items);
    }

    @FXML
    public void addComment(ActionEvent event) {
        if (current == null){
            new Alert(Alert.AlertType.ERROR, "No profile loaded").showAndWait();
            return;
        }
        String comment = taNewComment == null ? "" : taNewComment.getText().trim();
        if (comment.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Comment is empty").showAndWait();
            return;
        }
        String today = LocalDate.now().toString();
        Comment newComment = new Comment(today, comment);
        current.addComment(newComment);
        if (taNewComment != null) taNewComment.clear();
        refreshListView();
    }

    @FXML
    private void save() {
        if (current == null) {
            new Alert(Alert.AlertType.ERROR, "No Comments loaded.").showAndWait();
            return;
        }
//        current.setComments(taComments.getText() == null ? "" : taComments.getText().trim());
        DataStore.replaceByName(current);
        new Alert(Alert.AlertType.INFORMATION, "Saved comments for: " + current.getName()).showAndWait();
    }

    @FXML
    protected void searchProf(ActionEvent event) {
        swapScene(event, "/cs151/application/search.fxml", 1000, 680, "Search Student Profiles");
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
            new Alert(Alert.AlertType.ERROR, "Navigation error").showAndWait();
        }
    }

    private static String nvl(String s) {
        return (s == null || s.isBlank()) ? "" : s;
    }
}
