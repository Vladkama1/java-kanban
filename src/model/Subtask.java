package model;

import service.Type;

public class Subtask extends Task {//подзадача
    private Integer epicId;

    public Subtask(String name, String description, Integer epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "\n" + id + "," + Type.SUBTASK + "," + name + "," +
                status + "," + description + "," + epicId;
    }
}

