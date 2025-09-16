Write-Host "🚀 Iniciando MongoDB com Docker Compose..." -ForegroundColor Green

# Iniciar MongoDB
docker-compose up -d mongodb

Write-Host "⏳ Aguardando MongoDB inicializar..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

Write-Host "🧪 Executando testes..." -ForegroundColor Cyan
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.14.7-hotspot"
$env:PATH = "C:\Program Files\Eclipse Adoptium\jdk-17.0.14.7-hotspot\bin;" + $env:PATH
C:\ProgramData\chocolatey\lib\maven\apache-maven-3.9.11\bin\mvn.cmd clean test

Write-Host "📊 Resultados dos testes concluídos!" -ForegroundColor Green

# Parar MongoDB
Write-Host "🛑 Parando MongoDB..." -ForegroundColor Red
docker-compose down

