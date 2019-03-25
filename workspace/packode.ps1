echo "  ____        _                 _____           _     __          __         "
echo " |  _ \      (_)               |  __ \         | |    \ \        / /         "
echo " | |_) | __ _ ___      ____ _  | |__) |_ _  ___| | __  \ \  /\  / /_ _ _ __  "
echo " |  _ < / _* | \ \ /\ / / _* | |  ___/ _* |/ __| |/ /   \ \/  \/ / _* | '__| "
echo " | |_) | (_| | |\ V  V / (_| | | |  | (_| | (__|   <     \  /\  / (_| | |    "
echo " |____/ \__,_|_| \_/\_/ \__,_| |_|   \__,_|\___|_|\_\     \/  \/ \__,_|_|    "
echo "                                                                             "

cd .\front-end\angular-app\
echo "1. UPDATE LIB"
npm install
echo "2. Build Angular"
npm run build --prod --build-optimizer --base-href=/ecert-webapp/app/
cd ../..
echo "3. COPY TO STATIC WEP-APP"
Remove-Item .\back-end\ecert-webapp\src\main\resources\static\app -Force -Recurse
Copy-Item .\front-end\angular-app\dist\angular-app -Destination .\back-end\ecert-webapp\src\main\resources\static\app -Recurse
echo "3. COPY SUCCESS"

echo "3. PACK WAR"
cd .\back-end\ecert-webapp\
mvn clean package -DskipTests 
cd ../..

