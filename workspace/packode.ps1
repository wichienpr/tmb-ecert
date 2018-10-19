cd .\front-end\angular-app\
echo "1. UPDATE LIB"
npm install
echo "2. Build Angular"
ng build --prod --build-optimizer --base-href=/ecert-webapp/app/
cd ../..
echo "3. COPY TO STATIC WEP-APP"
Remove-Item .\back-end\ecert-webapp\src\main\resources\static\app -Force -Recurse
Copy-Item .\front-end\angular-app\dist\angular-app -Destination .\back-end\ecert-webapp\src\main\resources\static\app -Recurse
echo "3. COPY SUCCESS"

cd .\back-end\ecert-webapp\
mvn clean package -DskipTests
cd ../..