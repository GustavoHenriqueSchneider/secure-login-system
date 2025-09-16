#!/bin/bash

echo "🚀 Iniciando MongoDB com Docker Compose..."

# Iniciar MongoDB
docker-compose up -d mongodb

echo "⏳ Aguardando MongoDB inicializar..."
sleep 10

echo "🧪 Executando testes..."
mvn clean test

echo "📊 Resultados dos testes concluídos!"

# Parar MongoDB
echo "🛑 Parando MongoDB..."
docker-compose down

