package main.java.org.qcm;

import java.util.List;

public class Question {
    private String questionId;
    private String text;
    private List<String> options;
    private int correctOption;

    public Question() {}

    public Question(String questionId, String text, List<String> options, int correctOption) {
        this.questionId = questionId;
        this.text = text;
        this.options = options;
        this.correctOption = correctOption;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }
}
