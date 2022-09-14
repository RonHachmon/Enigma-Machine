package Engine.BruteForce;

public enum DifficultyLevel implements SetTask {
    EASY(){
        @Override
        public void setTask(TasksManager tasksManager) throws Exception {
            tasksManager.setEasyTasks();
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
