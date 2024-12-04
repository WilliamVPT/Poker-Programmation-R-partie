package main.java.org.qcm;

public class QCMRecord {
    private String qcmId;
    private String title;
    private int score;

    // Constructeur par défaut requis pour Jackson
    public QCMRecord() {}

    // Constructeur avec paramètres
    public QCMRecord(String qcmId, String title, int s) {
        this.qcmId = qcmId;
        this.title = title;
        this.score = s;
    }

    // Getters et Setters
    public String getQcmId() {
        return qcmId;
    }

    public void setQcmId(String qcmId) {
        this.qcmId = qcmId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
