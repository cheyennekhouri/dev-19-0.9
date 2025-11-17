package cs151.application;

import java.time.LocalDate;

public class Comment {
    private String date;
    private String text;

    public Comment(){
        this.date = "";
        this.text = "";
    }
    public Comment(String date, String text){
        setDate(date);
        setText(text);
    }
    // Creates dated comment using today's date
    public Comment(String text){
        this(LocalDate.now().toString(), text);
    }

    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date = date == null ? "" : date.trim();
    }

    public String getText(){
        return text;
    }
    public void setText(String text){
        this.text = text == null ? "" : text.trim();
    }

    @Override
    public String toString(){
        if(date == null || date.isEmpty()) return getText();
        return date + " - " + getText();
    }
}
