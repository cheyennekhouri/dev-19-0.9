package cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public final class DataStore {
    private static final Path DATA_DIR = Paths.get(System.getProperty("user.home"), ".knowledgetrack");
    private static final Path DATA_FILE = DATA_DIR.resolve("languages.csv");
    private static final Path PROFILE_FILE = DATA_DIR.resolve("profiles.csv");

    private static final ObservableList<ProgrammingLanguages> LIST = FXCollections.observableArrayList();

    private static final ObservableList<StudentProfile> NAME = FXCollections.observableArrayList();

    private static boolean loadedOnce = false;

    private DataStore() {}

    public static ObservableList<ProgrammingLanguages> getList() {
        return LIST;
    }

    public static ObservableList<StudentProfile> getFullName() {
        return NAME;
    }

    private static void seedDefaultLanguagesIfAbsent() {
        if (Files.exists(DATA_FILE)) return;
        LIST.setAll(
                new ProgrammingLanguages("Java"),
                new ProgrammingLanguages("Python"),
                new ProgrammingLanguages("C++")
        );
        save();
    }

    public static void load() {
        if (loadedOnce) return;
        loadedOnce = true;

        try {
            if (!Files.exists(DATA_DIR)) Files.createDirectories(DATA_DIR);
        } catch (IOException ignored) {
        }
        seedDefaultLanguagesIfAbsent();

        LIST.clear();
        try (BufferedReader br = Files.newBufferedReader(DATA_FILE, StandardCharsets.UTF_8)) {
            String header = br.readLine();
            if (header == null) return;

            String row;
            while ((row = br.readLine()) != null) {
                parseLineIntoList(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        seedDefaultLanguagesIfAbsent();
        seedDefaultProfilesIfAbsent();
        loadProfiles();
    }

    public static void save() {
        try {
            if (!Files.exists(DATA_DIR)) Files.createDirectories(DATA_DIR);
            try (BufferedWriter bw = Files.newBufferedWriter(DATA_FILE, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                bw.write("programmingLanguage");
                bw.newLine();

                for (ProgrammingLanguages pl : LIST) {
                    //  bw.write(csv(pl.getFullName()));
                    //bw.write(',');
                    bw.write(csv(pl.getProgrammingLanguage()));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void seedDefaultProfilesIfAbsent() {
        // If profiles.csv already exists, do nothing
        if (Files.exists(PROFILE_FILE)) return;

        try {
            if (!Files.exists(DATA_DIR)) Files.createDirectories(DATA_DIR);

            try (BufferedWriter bw = Files.newBufferedWriter(
                    PROFILE_FILE, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {


                bw.write(String.join(",", "name", "major", "academicStatus", "employment",
                        "jobDetails", "languages", "preferredRole",
                        "achievements", "skills", "comments", "whiteList", "blackList"));
                bw.newLine();


                bw.write(String.join(",",
                        csv("Hoang"),
                        csv("Software Engineering"),
                        csv("Junior"),
                        csv("Employed"),
                        csv("TA at SJSU"),
                        csv("Java|Python|SQL"),
                        csv("Backend"),
                        csv("Prefers APIs and backend services"),
                        csv("REST APIs, debugging"),
                        csv("2025-01-15:::Hoang has been an exceptional TA this semester " +
                                "He is consistently going above and beyond in helping students understand difficult " +
                                "concepts in data structures and algorithms. His explanations are clear and he has " +
                                "received positive feedback from students who appreciate his approachable demeanor and " +
                                "technical knowledge of backend development.|||2025-02-10:::Interested in microservices"),
                        csv("true"),
                        csv("false")
                ));
                bw.newLine();

                bw.write(String.join(",",
                        csv("Rhys"),
                        csv("Software Engineering"),
                        csv("Senior"),
                        csv("Employed"),
                        csv("QA Engineer"),
                        csv("Java|Spring|JUnit"),
                        csv("QA/DevOps"),
                        csv("Testing focus"),
                        csv("JUnit, CI/CD"),
                        csv("2025-01-22:::Rhys has demonstrated a strong aptitude for quality assurance " +
                                "and testing methodologies throughout his time at the company. He has been instrumental " +
                                "in identifying critical bugs and improving the testing frameworks. Rhys is proactive in " +
                                "staying updated with the latest QA tools and practices, which has greatly benefited our " +
                                "development process.|||2025-03-05:::Looking to transition into DevOps role"),
                        csv("true"),
                        csv("false")
                ));
                bw.newLine();

                bw.write(String.join(",",
                        csv("Maria"),
                        csv("Computer Science"),
                        csv("Sophomore"),
                        csv("Not Employed"),
                        csv(""),
                        csv("Python|JavaScript"),
                        csv("Full Stack"),
                        csv("Interested in web apps"),
                        csv("HTML, CSS, React"),
                        csv("2025-01-22:::Maria is a very passionate student and has a great interest in learning " +
                                "new technologies. She has completed the advanced algorithms project two weeks ahead of " +
                                "schedule and volunteered to help struggling classmates understand dynamic programming " +
                                "concepts. Her code is well-documented and follows industry best practices, showing " +
                                "maturity beyond her sophomore status.|||2025-02-15:::Considering graduate school"),
                        csv("true"),
                        csv("false")
                ));
                bw.newLine();



                bw.write(String.join(",",
                        csv("Che"),
                        csv("Computer Science"),
                        csv("Senior"),
                        csv("Not Employed"),
                        csv(""),
                        csv("JavaScript|TypeScript|React"),
                        csv("Frontend"),
                        csv("Likes UI work"),
                        csv("React, TypeScript"),
                        csv("2025-01-22::: Che has struggled with meeting project deadlines and maintaining consistent " +
                                "communication with team members this semester. While she demonstrates technical capability " +
                                "in frontend development, her pattern of submitting work late without prior notification " +
                                "has disrupted group project timelines. Multiple teammates have expressed frustration about " +
                                "having to adjust their schedules to accommodate her delays.|||2025-02-18:::Attendance concerns raised"),
                        csv("false"),
                        csv("true")
                ));
                bw.newLine();

                bw.write(String.join(",",
                        csv("Lyly"),
                        csv("Computer Engineering"),
                        csv("Junior"),
                        csv("Not Employed"),
                        csv(""),
                        csv("C++|Embedded C|Python"),
                        csv("Embedded"),
                        csv("Boards & sensors"),
                        csv("Microcontrollers, C++"),
                        csv("2025-01-22:::Lyly has encountered significant difficulties working effectively in team " +
                                "environments throughout the semester. Her code submissions often lack proper documentation " +
                                "and comments, making it difficult for teammates to integrate her work. She has missed " +
                                "several critical project meetings without notification, forcing the team to proceed without " +
                                "her input and later redo integration work.|||2025-03-12:::Needs improvement in collaboration skills"),
                        csv("false"),
                        csv("true")
                ));
                bw.newLine();

                bw.write(String.join(",",
                        csv("David"),
                        csv("Software Engineering"),
                        csv("Senior"),
                        csv("Employed"),
                        csv("Part-time IT support"),
                        csv("Java|SQL"),
                        csv("Backend"),
                        csv("Database-heavy projects"),
                        csv("SQL, Git"),
                        csv("2025-01-22:::David has shown inconsistent code quality and testing practices that have " +
                                "led to production issues on multiple occasions. His database queries often lack proper " +
                                "optimization, resulting in performance bottlenecks. While he possesses fundamental SQL " +
                                "knowledge, his tendency to skip thorough testing before deployment has created additional " +
                                "work for the QA team and delayed several release cycles.|||2025-02-25:::Performance " +
                                "improvement plan initiated"),
                        csv("false"),
                        csv("true")
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String csv(String s) {
        if (s == null) s = "";
        String q = s.replace("\"", "\"\"");
        return "\"" + q + "\"";
    }

    private static void parseLineIntoList(String line) {
        String[] cols = parseCsvLine(line, 1);
        if (cols == null) return;
        String lang = cols[0];
        if (!lang.isEmpty()) {
            LIST.add(new ProgrammingLanguages(lang));
        }
    }

    private static String[] parseCsvLine(String line, int expectedCols) {
        if (line == null) return null;
        String[] out = new String[expectedCols];
        int idx = 0;
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        sb.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    sb.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == ',') {
                    if (idx < expectedCols) out[idx++] = sb.toString();
                    sb.setLength(0);
                } else {
                    sb.append(c);
                }
            }
        }
        if (idx < expectedCols) out[idx++] = sb.toString();
        if (idx != expectedCols) return null;
        return out;
    }

    public static boolean existsByExactName(String name) {
        if (name == null) return false;
        for (StudentProfile sp : NAME) {
            if (name.equalsIgnoreCase(sp.getName())) return true;
        }
        return false;
    }

    public static void deleteByName(String name) {
        if (name == null) return;
        NAME.removeIf(sp -> name.equalsIgnoreCase(sp.getName()));
        saveProfiles();
    }

    public static void replaceByName(StudentProfile incoming) {
        if (incoming == null || incoming.getName() == null) return;
        for (int i = 0; i < NAME.size(); i++) {
            if (incoming.getName().equalsIgnoreCase(NAME.get(i).getName())) {
                NAME.set(i, incoming);
                saveProfiles();
                return;
            }
        }
        NAME.add(incoming);
        saveProfiles();
    }

    public static void saveProfiles() {
        try {
            if (!Files.exists(DATA_DIR)) Files.createDirectories(DATA_DIR);
            try (BufferedWriter bw = Files.newBufferedWriter(
                    PROFILE_FILE, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

                // header (12 columns)
                bw.write(String.join(",", "name", "major", "academicStatus", "employment",
                        "jobDetails", "languages", "preferredRole", "achievements", "skills", "comments", "whiteList", "blackList"));
                bw.newLine();

                for (StudentProfile sp : NAME) {
                    String langsJoined = (sp.getLanguages() == null) ? "" : String.join("|", sp.getLanguages());
                    String line = String.join(",",
                            csv(sp.getName()),
                            csv(sp.getMajor()),
                            csv(sp.getAcademicStatus()),
                            csv(sp.isEmployed() ? "Employed" : "Not Employed"),
                            csv(sp.getJobDetails()),
                            csv(langsJoined),
                            csv(sp.getPreferredRole()),
//                            csv(sp.getComments()),
                            csv(sp.getAchievements()),
                            csv(sp.getSkills()),
                            csv(sp.getCommentsCell()),
                            csv(Boolean.toString(sp.isWhiteList())),
                            csv(Boolean.toString(sp.isBlackList())));
                    bw.write(line);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadProfiles() {
        NAME.clear();
        if (!Files.exists(PROFILE_FILE)) return;

        try (BufferedReader br = Files.newBufferedReader(PROFILE_FILE, StandardCharsets.UTF_8)) {
            String header = br.readLine(); // skip header
            for (String row; (row = br.readLine()) != null; ) {
                String[] c = parseCsvLine(row, 12);
                if (c == null) continue;

                // c[0]=name, c[1]=major, c[2]=academicStatus, c[3]=employment,
                // c[4]=jobDetails, c[5]=languages (pipe-separated),
                // c[6]=preferredRole, c[7]=comments, c[8]=whiteList, c[9]=blackList

                StudentProfile sp = new StudentProfile(c[0]);
                sp.setMajor(c[1]);
                sp.setAcademicStatus(c[2]);
                sp.setEmployeed("Employed".equalsIgnoreCase(c[3]));
                sp.setJobDetails(c[4]);

                if (c[5] != null && !c[5].isEmpty()) {
                    sp.setLanguages(List.of(c[5].split("\\|")));
                } else {
                    sp.setLanguages(List.of());
                }

                sp.setPreferredRole(c[6]);
//                sp.setComments(c[7]);
                sp.setAchievements(c[7]);
                sp.setSkills(c[8]);
                sp.setCommentList(parseCommentsCell(c[9]));
                sp.setWhiteList(Boolean.parseBoolean(c[10]));
                sp.setBlackList(Boolean.parseBoolean(c[11]));

                NAME.add(sp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static List<Comment> parseCommentsCell(String cell) {
        List<Comment> out = new ArrayList<>();
        if (cell == null || cell.isEmpty()) return out;
        if (!cell.contains(":::")) {
            out.add(new Comment("", cell));
            return out;
        }
        String[] entries = cell.split("\\|\\|\\|", -1);
        for (String e : entries) {
            if (e == null || e.isEmpty()) continue;
            String[] parts = e.split(":::", 2);
            String date = parts.length > 0 ? parts[0] : "";
            String text = parts.length > 1 ? parts[1] : "";
            out.add(new Comment(date, text));
        }
        return out;
    }
}

