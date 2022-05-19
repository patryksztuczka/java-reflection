package com.example.lab4;

public class Song {
    private String title = "Tyle słońca w całym mieście";
    private Integer tempo = 100;
    private String rythm = "4/4";
    private String album = "Tyle słońca w całym mieście";
    private String performer = "Anna Jantar";
    private String text = "Tyle słońca w całym mieście\n" +
            "Nie widziałeś tego jeszcze\n" +
            "Popatrz, o popatrz!";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTempo() {
        return tempo;
    }

    public void setTempo(Integer tempo) {
        this.tempo = tempo;
    }

    public String getRythm() {
        return rythm;
    }

    public void setRythm(String rythm) {
        this.rythm = rythm;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString()
    {
        return "Title: " + title + "\n" +
                "Tempo: " + tempo + "\n" +
                "Rythm: " + rythm + "\n" +
                "Album: " + album + "\n" +
                "Performer: " + performer + "\n" +
                "Text: " + text + "\n";
    }
}
