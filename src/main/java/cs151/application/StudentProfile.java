package cs151.application;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class StudentProfile  implements Comparable<StudentProfile> {
    private String name;
    private String major;
    private List<String> languages;
    private String academicStatus;
    private boolean employed;
    private boolean unemployed;
    private String jobDetails;
    private String preferredRole;
    private String comments;
    private List<Comment> commentList;
    private boolean whiteList;
    private boolean blackList;
    private String achievments;
    private String skills;

    public StudentProfile(){
        this.name = "";
        this.major = "";
        this.languages = new ArrayList<>();
        this.academicStatus = "";
        this.jobDetails = "";
        this.preferredRole = "";
        this.comments = "";
        this.commentList = new ArrayList<>();
        this.whiteList = false;
        this.blackList = false;
        this.achievments = "";
        this.skills = "";
    }
    public StudentProfile(String name, String major, List<String> languages){
        this();
        setName(name);
        setMajor(major);
        setLanguages(languages);
    }

    public StudentProfile(String name){
        this();
        setName(name);
    }
    // Name
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    // Major
    public void setMajor(String major) {
        this.major = major;
    }
    public String getMajor() {
        return major;
    }
    // Languages
    public List<String> getLanguages() {
        if (languages == null) languages = new ArrayList<>();
        return languages;
    }
    public void setLanguages(List<String> languages) {
        this.languages = languages == null ? new ArrayList<>() : new ArrayList<>(languages);
    }
    // dropdown
    public String getAcademicStatus() {
        return academicStatus;
    }
    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus;
    }
    // radio
    public boolean isEmployed() {
        return employed;
    }
    public void setEmployeed(boolean employed) {
        this.employed = employed;
    }

    public boolean isUnemployed() {
        return unemployed;
    }
    public void setUnemployeed(boolean unemployed) {
        this.unemployed = unemployed;
    }

    public String getJobDetails() {
        return jobDetails;
    }
    public void setJobDetails(String jobDetails) {
        this.jobDetails = jobDetails;
    }
    // Dropdown
    public String getPreferredRole() {
        return preferredRole;
    }
    public void setPreferredRole(String preferredRole) {
        this.preferredRole = preferredRole;
    }

    //Achievements
    public String getAchievements() {
        return achievments == null ? "" : achievments;
    }
    public void setAchievements(String achievements) {
        this.achievments = achievements == null ? "" : achievements;
    }
    //skills
    public String getSkills() {
        return skills == null ? "" : skills;
    }
    public void setSkills(String skills) {
        this.skills = skills == null ? "" : skills;
    }

    public String getComments() {
        if (commentList != null && !commentList.isEmpty()) {
            return commentList.stream().map(comment -> comment == null ? "" : comment.toString())
                    .collect(Collectors.joining("\n"));
        }
        return comments == null ? "" : comments;
    }
    public void setComments(String comments) {
        this.comments = comments == null ? "" : comments;
        this.commentList = new ArrayList<>();
        if (comments != null && !comments.isEmpty()) {
            this.commentList.add(new Comment("", comments));
        }
    }
    // Structured Comment Text
    public List<Comment> getCommentList() {
        if(commentList == null) commentList = new ArrayList<>();
        return commentList;
    }
    public void setCommentList(List<Comment> comments) {
        this.commentList = comments == null ? new ArrayList<>() : new ArrayList<>(comments);
    }
    public void addComment(Comment comment){
        if(comment == null) return;
        if(commentList == null) commentList = new ArrayList<>();
        commentList.add(comment);
    }
    // Serialize structured comments
    public String getCommentsCell() {
        if (commentList == null || commentList.isEmpty()) return "";
        return commentList.stream()
                .map(comment -> (comment.getDate() == null ? "" : comment.getDate()) + ":::" +
                (comment.getText() == null ? "" : comment.getText())).collect(Collectors.joining("|||"));
    }
    // WhiteList
    public boolean isWhiteList() {
        return whiteList;
    }
    public void setWhiteList(boolean whiteList){
        if(whiteList){
            this.blackList = false;
        }
        this.whiteList = whiteList;
    }
    // Blacklist
    public boolean isBlackList() {
        return blackList;
    }
    public void setBlackList(boolean blackList) {
        if(blackList){
            this.whiteList = false;
        }
        this.blackList = blackList;
    }
    public String getLanguagesString(){
        return String.join(" | ", languages);
    }
    //compare names to sort
    @Override
    public int compareTo(StudentProfile other){
        return this.name.compareToIgnoreCase(other.name);
    }
    @Override
    public String toString(){
        return String.join(",", name, major, academicStatus, employed ? "Employed" : "Not Employed",
                jobDetails, String.join("|", languages), preferredRole,
                comments.replace(",", ";"), String.valueOf(whiteList), String.valueOf(blackList));
    }
}