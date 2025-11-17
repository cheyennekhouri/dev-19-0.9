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
                        csv("Prefers APIs"),
                        csv("REST APIs, debugging"),
                        csv(""),
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
                        csv("Good with UX"),
                        csv("React, Figma"),
                        csv(""),
                        csv("false"),
                        csv("false")
                ));
                bw.newLine();


                bw.write(String.join(",",
                        csv("Kanishka"),
                        csv("Data Science"),
                        csv("Sophomore"),
                        csv("Employed"),
                        csv("Data Intern"),
                        csv("Python|R|Pandas"),
                        csv("Data"),
                        csv("Loves ML projects"),
                        csv("Pandas, NumPy"),
                        csv(""),
                        csv("false"),
                        csv("false")
                ));
                bw.newLine();


                bw.write(String.join(",",
                        csv("Ryhs"),
                        csv("Software Engineering"),
                        csv("Senior"),
                        csv("Employed"),
                        csv("QA Engineer"),
                        csv("Java|Spring|JUnit"),
                        csv("QA/DevOps"),
                        csv("Testing focus"),
                        csv("JUnit, CI/CD"),
                        csv(""),
                        csv("false"),
                        csv("false")
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
                        csv(""),
                        csv("false"),
                        csv("false")
                ));
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

