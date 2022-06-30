package homework2.entity;

public class Task {
    private int id;
    private int idUser;
    private String header;
    private String description;
    private String date;
    private String status;

    public Task(int id, int idUser, String header, String description, String date) {
        this.id = id;
        this.idUser = idUser;
        this.header = header;
        this.description = description;
        this.date = date;
        status = "Новая";
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHeader (String header) {
        this.header = header;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate (String date) {
        this.date = date;
    }

    public boolean setStatus(String status) {
        if(status.compareTo("Новое") == 0 || status.compareTo("В работе") == 0 || status.compareTo("Готово") == 0) {
            this.status = status;
            return true;
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return idUser;
    }

    public String getHeader() {
        return header;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
