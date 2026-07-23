# Cracking The Enigma - JavaFX 8 desktop app, viewable in the browser via noVNC.
#
#   docker build -t enigma-gui .
#   docker run --rm -p 6080:6080 enigma-gui
#   # then open http://localhost:6080/vnc.html
#
# On Apple Silicon / non-amd64 hosts add:  --platform=linux/amd64
FROM debian:bullseye-slim

ENV DEBIAN_FRONTEND=noninteractive

# --- System deps -----------------------------------------------------------
# VNC/noVNC stack + a window manager, the native libraries JavaFX 8 needs
# (it uses the GTK2 backend), plus fetch tooling.
RUN apt-get update && apt-get install -y --no-install-recommends \
        curl ca-certificates \
        xvfb x11vnc x11-utils fluxbox novnc websockify \
        libgtk2.0-0 libxtst6 libxrender1 libxext6 libxi6 libx11-6 \
        libgl1-mesa-glx libglib2.0-0 libasound2 fontconfig libfreetype6 \
    && rm -rf /var/lib/apt/lists/*

# --- JavaFX-bundled JDK 8 --------------------------------------------------
# BellSoft Liberica "Full" JDK 8 bundles JavaFX (LibericaFX). A plain OpenJDK 8
# is not enough: the app compiles against internal com.sun.javafx.* classes.
ENV JAVA_HOME=/opt/jdk8
ENV PATH="${JAVA_HOME}/bin:${PATH}"
ARG LIBERICA_VERSION=8u452+11
RUN curl -fsSL -o /tmp/jdk.tar.gz \
        "https://download.bell-sw.com/java/${LIBERICA_VERSION}/bellsoft-jdk${LIBERICA_VERSION}-linux-amd64-full.tar.gz" \
    && mkdir -p "${JAVA_HOME}" \
    && tar -xzf /tmp/jdk.tar.gz -C "${JAVA_HOME}" --strip-components=1 \
    && rm /tmp/jdk.tar.gz \
    && java -version

# --- External library ------------------------------------------------------
# FontAwesomeFX 8.9 is not in the repo (jars are gitignored); fetch it.
RUN mkdir -p /app/libs \
    && curl -fsSL -o /app/libs/fontawesomefx-8.9.jar \
        "https://repo1.maven.org/maven2/de/jensd/fontawesomefx/8.9/fontawesomefx-8.9.jar"

# --- Build the app ---------------------------------------------------------
WORKDIR /src
COPY CommoUtils/ CommoUtils/
COPY DTO/ DTO/
COPY Machine/ Machine/
COPY GUI/ GUI/

# Compile every module source together against the FontAwesomeFX jar.
RUN mkdir -p /app/out \
    && find CommoUtils/src DTO/src Machine/src GUI/src -name '*.java' > /tmp/sources.txt \
    && javac -encoding UTF-8 \
        -cp /app/libs/fontawesomefx-8.9.jar \
        -d /app/out @/tmp/sources.txt

# Copy runtime resources (fxml/css/ttf) so they sit at the classpath root,
# matching the absolute paths Main.java loads (e.g. /app/mainApp.fxml).
RUN cd GUI/src && find . -type f ! -name '*.java' -exec cp --parents -t /app/out {} +

# Machine configuration files (the ex2 format this app reads). Loading one of
# these is the user's first step, so they sit in the app's working directory
# (= HOME) and the in-app file chooser, which sets no initial directory, opens
# right next to them.
COPY configuration_files/ /app/configuration_files/

# --- Runtime ---------------------------------------------------------------
COPY docker/start.sh /start.sh
RUN sed -i 's/\r$//' /start.sh && chmod +x /start.sh

WORKDIR /app
ENV HOME=/app

EXPOSE 6080
ENTRYPOINT ["/start.sh"]
