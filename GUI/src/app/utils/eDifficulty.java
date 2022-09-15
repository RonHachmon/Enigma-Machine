package app.utils;

public enum eDifficulty {
        EASY("Easy"),
        MEDIUM("Medium"),
        HARD("Hard"),
        IMPOSSIBLE("Impossible :O");

        private String message;

        private eDifficulty(String message) {
            this.message = message;
        }

        public String toString() {
            return this.message;
        }
}
