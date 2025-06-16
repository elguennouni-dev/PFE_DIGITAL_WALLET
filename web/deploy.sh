#!/bin/bash
# AUTO DEPLOYMENT BASH SCRIPT
# DEVELOPED BY EL GUENNOUNI ABDELILAH

REMOTE_USER="" # USER
REMOTE_HOST="" # SERVER ADDRESS
LOCAL_PATH="../front-end/" # LOCAL PATH
REMOTE_PATH="/root/spa-app/web" # REPLACE WITH CURRENT REMOTE PATH
PM2_APP_NAME="wra9i" # REPLACE WITH YOUR APP NAME :)

set -e
echo "Start..."
echo "Syncing files from '$LOCAL_PATH' to '$REMOTE_USER@$REMOTE_HOST:$REMOTE_PATH'..."
rsync -avz --delete "$LOCAL_PATH" "$REMOTE_USER@$REMOTE_HOST:$REMOTE_PATH"
echo "Finish."
echo "Restarting application '$PM2_APP_NAME' on the server..."
ssh "$REMOTE_USER@$REMOTE_HOST" "pm2 restart $PM2_APP_NAME"
echo "Application deployed successfully!"