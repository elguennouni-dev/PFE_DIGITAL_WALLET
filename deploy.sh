#!/bin/bash

JAR_NAME="digitalWallet-0.0.1-SNAPSHOT.jar"
REMOTE_USER="root"
REMOTE_HOST="84.247.142.209"
REMOTE_DIR="/root/springboot-app"
APP_PORT=8080

echo "Start..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
  echo "❌ Problem"
  exit 1
fi

echo "🚀 Building $JAR_NAME just wait..."
scp target/$JAR_NAME $REMOTE_USER@$REMOTE_HOST:$REMOTE_DIR/

if [ $? -ne 0 ]; then
  echo "❌ Building failed"
  exit 1
fi

echo "🔁 Start deployment and restart the app in the server..."
ssh $REMOTE_USER@$REMOTE_HOST << EOF
  echo "🔍 Looking for old process..."
  PID=\$(lsof -ti tcp:$APP_PORT)

  if [ ! -z "\$PID" ]; then
    echo "🔪 9tel process on port $APP_PORT (PID=\$PID)..."
    kill -9 \$PID
  else
    echo "✅ Waloooo Makayn F port $APP_PORT."
  fi

  echo "🚀 BISMILAH new version..."
  nohup java -jar $REMOTE_DIR/$JAR_NAME > $REMOTE_DIR/output.log 2>&1 &
  echo "✅ App Raha Khadama F L'Background. Logs: $REMOTE_DIR/output.log"
EOF

echo "✅ MAKANTZARAWCH!"