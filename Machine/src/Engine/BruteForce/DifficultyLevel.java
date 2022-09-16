package Engine.BruteForce;

import Engine.EnigmaException.TaskIsCanceledException;

public enum DifficultyLevel implements SetTask {
    EASY(){
        @Override
        public void setTask(TasksManager tasksManager) throws Exception {
            try {
                tasksManager.setEasyTasks();
            } catch (TaskIsCanceledException e) {
                throw new RuntimeException(e);
            }
        }
    },
    MEDIUM(){
        @Override
        public void setTask(TasksManager tasksManager) throws Exception {
            tasksManager.setMediumTasks();
        }
    },
    HARD(){
        @Override
        public void setTask(TasksManager tasksManager) throws Exception {
            tasksManager.setHardTasks();
        }
    },
    IMPOSSIBLE(){
        @Override
        public void setTask(TasksManager tasksManager) throws Exception {
            tasksManager.setImpossibleTasks();
        }
    };

}