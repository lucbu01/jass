cd frontend
npm install
npm run lint
npm run build:prod
cd ../backend
./gradlew assemble
./gradlew test