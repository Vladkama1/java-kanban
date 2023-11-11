package model;

import service.Type;

import java.util.ArrayList;

public class Epic extends Task {//большая задача, которя разбивается на подзадачи
    private ArrayList<Integer> subtasksId;

    public Epic(String name, String description) {
        super(name, description);
        subtasksId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setSubtasksId(ArrayList<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }

    @Override
    public String toString() {
        return "\n" + id + "," + Type.EPIC + "," + name + "," +
                status + "," + description + ",";
    }
}

