package main.java.org.qcm;

import java.util.List;

public class QCM {
    private String qcmId;
    private String title;
    private List<Question> questions;

    public QCM() {}

    public QCM(String qcmId, String title, List<Question> questions) {
        this.qcmId = qcmId;
        this.title = title;
        this.questions = questions;
    }

    public String getQcmId() {
        return qcmId;
    }

    public void setQcmId(String qcmId) {
        this.qcmId = qcmId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}

