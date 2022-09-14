package app.utils.threads;

import app.utils.eDifficulty;

public class DMData {
    private int amountOfAgents;
    private eDifficulty eDifficulty;
    private int assignmentSize;

    public int getAmountOfAgents() {
        return amountOfAgents;
    }

    public void setAmountOfAgents(int amountOfAgents) {
        this.amountOfAgents = amountOfAgents;
    }

    public app.utils.eDifficulty geteDifficulty() {
        return eDifficulty;
    }

    public void seteDifficulty(app.utils.eDifficulty eDifficulty) {
        this.eDifficulty = eDifficulty;
    }

    public int getAssignmentSize() {
        return assignmentSize;
    }

    public void setAssignmentSize(int assignmentSize) {
        this.assignmentSize = assignmentSize;
    }
}
