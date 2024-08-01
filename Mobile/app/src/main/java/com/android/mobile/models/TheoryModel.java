package com.android.mobile.models;

import java.util.Date;

public class TheoryModel {
    private int TheoryID;
    private String Title;
    private Date Upload_Date;
    private String Theory_Type;
    private String Content;
    private String Img_Url;
    private String Video_Url;
    private String Source;
    private String Difficulty_Level;

    public TheoryModel(String content, String difficulty_Level, String img_Url, String source, String theory_Type, int theoryID, String title, Date upload_Date, String video_Url) {
        Content = content;
        Difficulty_Level = difficulty_Level;
        Img_Url = img_Url;
        Source = source;
        Theory_Type = theory_Type;
        TheoryID = theoryID;
        Title = title;
        Upload_Date = upload_Date;
        Video_Url = video_Url;
    }

    public TheoryModel() {
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDifficulty_Level() {
        return Difficulty_Level;
    }

    public void setDifficulty_Level(String difficulty_Level) {
        Difficulty_Level = difficulty_Level;
    }

    public String getImg_Url() {
        return Img_Url;
    }

    public void setImg_Url(String img_Url) {
        Img_Url = img_Url;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getTheory_Type() {
        return Theory_Type;
    }

    public void setTheory_Type(String theory_Type) {
        Theory_Type = theory_Type;
    }

    public int getTheoryID() {
        return TheoryID;
    }

    public void setTheoryID(int theoryID) {
        TheoryID = theoryID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Date getUpload_Date() {
        return Upload_Date;
    }

    public void setUpload_Date(Date upload_Date) {
        Upload_Date = upload_Date;
    }

    public String getVideo_Url() {
        return Video_Url;
    }

    public void setVideo_Url(String video_Url) {
        Video_Url = video_Url;
    }
}
