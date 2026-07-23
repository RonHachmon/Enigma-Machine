#!/bin/sh
# Entrypoint: boot a virtual display + VNC + noVNC, then launch the JavaFX app.
set -e

export DISPLAY=:0
SCREEN_GEOMETRY="${SCREEN_GEOMETRY:-1280x800x24}"
NOVNC_PORT="${NOVNC_PORT:-6080}"
VNC_PORT="${VNC_PORT:-5900}"

# Virtual framebuffer (no physical display in a container).
Xvfb :0 -screen 0 "$SCREEN_GEOMETRY" -nolisten tcp &
XVFB_PID=$!

# Wait for the display to be ready.
for i in $(seq 1 50); do
    if xdpyinfo -display :0 >/dev/null 2>&1; then break; fi
    sleep 0.2
done

# Lightweight window manager so the app window has decorations / focus.
fluxbox >/dev/null 2>&1 &

# Expose the display over VNC, then bridge VNC -> WebSocket for noVNC.
x11vnc -display :0 -forever -nopw -shared -rfbport "$VNC_PORT" -quiet -bg
websockify --web=/usr/share/novnc "$NOVNC_PORT" "localhost:$VNC_PORT" &

echo "noVNC ready: open http://localhost:${NOVNC_PORT}/vnc.html (auto-connect)"

# Launch the app in the foreground so the container lives as long as the app.
# -Dprism.order=sw : force software rendering (no GPU under Xvfb)
# -Djdk.gtk.version=2 : JavaFX 8 uses the GTK2 backend
exec java \
    -Dprism.order=sw \
    -Djdk.gtk.version=2 \
    -Djava.awt.headless=false \
    -cp "/app/out:/app/libs/fontawesomefx-8.9.jar" \
    app.Main
