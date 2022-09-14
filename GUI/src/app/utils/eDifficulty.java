package app.utils;

public enum eDifficulty {
        EASY("Easy"),
        MEDIUM("Medium"),
        HARD("Hard"),
        IMPOSSIBLE("Impossible :O");

        public static final int SIZE = values().length;
        //public static final int PIVOT = 2;
        private String message;

        private eDifficulty(String message) {
            this.message = message;
        }

        public String toString() {
            return this.message;
        }
}
