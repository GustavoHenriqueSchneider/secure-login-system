# 🔐 Sistema de Login Seguro

Sistema de autenticação completo desenvolvido com **Spring Boot**, **Thymeleaf** e **MongoDB**. Implementa login, cadastro, controle de acesso por roles, auditoria de segurança e monitoramento de tentativas de login.

## 📋 Índice

- [Características](#-características)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Configuração](#-instalação-e-configuração)
- [Executando a Aplicação](#-executando-a-aplicação)
- [Executando os Testes](#-executando-os-testes)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Funcionalidades](#-funcionalidades)
- [Segurança](#-segurança)
- [API Endpoints](#-api-endpoints)
- [Docker](#-docker)
- [Personalização](#-personalização)
- [Troubleshooting](#-troubleshooting)

## ✨ Características

### 🔒 Segurança Avançada
- **Criptografia de senhas** com BCrypt (strength 12)
- **Validação de dados** com Bean Validation
- **Headers de segurança** completos (HSTS, CSP, X-Frame-Options)
- **Rate limiting** por IP
- **Bloqueio automático** de contas após tentativas falhadas
- **Auditoria completa** de tentativas de login

### 👥 Gerenciamento de Usuários
- **Cadastro de usuários** com validação completa
- **Sistema de roles** (ADMIN, USER, MODERATOR)
- **Controle de acesso** granular por roles
- **Login e logout** seguros
- **Ativação/desativação** de usuários
- **Perfil do usuário** com edição de dados

### 🎨 Interface Moderna
- **Design responsivo** com Bootstrap 5.3
- **Templates Thymeleaf** modulares e reutilizáveis
- **Páginas de login, cadastro e dashboard**
- **Notificações toast** para feedback do usuário
- **Validação em tempo real** no frontend

### 📊 Monitoramento e Auditoria
- **Relatórios de segurança** em tempo real
- **Histórico de tentativas de login** por usuário e IP
- **Métricas de uso** do sistema
- **Logs estruturados** para análise
- **Dashboard administrativo** com estatísticas

## 🛠 Tecnologias Utilizadas

### Backend
- **Java 17+**
- **Spring Boot 3.2.0**
- **Spring Security 6.x**
- **Spring Data MongoDB**
- **Maven 3.8+**

### Frontend
- **Thymeleaf 3.x**
- **Bootstrap 5.3.0**
- **JavaScript ES6+**
- **Jest** (testes)

### Banco de Dados
- **MongoDB 7.0**
- **Docker Compose**

### Ferramentas de Desenvolvimento
- **Lombok**
- **JUnit 5**
- **Mockito**
- **Docker**

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java 17 ou superior** (JDK)
- **Maven 3.8 ou superior**
- **Docker e Docker Compose**
- **Git**
- **Node.js 16+** (para testes frontend)
- **IDE de desenvolvimento** (recomendado: IntelliJ IDEA)

## 🚀 Instalação e Configuração

### 1. Clone o Repositório

```bash
git clone https://github.com/seu-usuario/secure-login-system.git
cd secure-login-system
```

### 2. Configuração do Ambiente

#### 2.1 Configurar Java 17
```bash
# Verificar versão do Java
java -version

# Se necessário, configurar JAVA_HOME
export JAVA_HOME=/path/to/java17
# No Windows:
# set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.14.7-hotspot
```

#### 2.2 Configurar Maven
```bash
# Verificar versão do Maven
mvn -version

# Se necessário, instalar via Chocolatey (Windows)
choco install maven
```

### 3. Configuração do Banco de Dados

O projeto usa MongoDB via Docker Compose. Não é necessária configuração adicional.

## ▶️ Executando a Aplicação

### 1. Iniciar o MongoDB

```bash
# Usando Docker Compose
docker compose up -d mongodb

# Aguardar inicialização (15-30 segundos)
```

### 2. Compilar e Executar

```bash
# Compilar o projeto
mvn clean compile

# Executar a aplicação
mvn spring-boot:run
```

### 3. Acessar a Aplicação

- **URL:** http://localhost:8080
- **Login padrão:** `admin` / `admin123`

## 🧪 Executando os Testes

### Testes Backend (Java)

```bash
# Executar todos os testes
mvn test

# Executar com relatório de cobertura
mvn test jacoco:report
```

### Testes Frontend (JavaScript)

```bash
# Instalar dependências
npm install

# Executar testes
npm test

# Executar com cobertura
npm run test:coverage
```

### Scripts de Automação

#### Windows (PowerShell)
```powershell
.\run-tests.ps1
```

#### Linux/Mac (Bash)
```bash
./run-tests.sh
```

## 📁 Estrutura do Projeto

```
secure-login-system/
├── src/
│   ├── main/
│   │   ├── java/com/securelogin/
│   │   │   ├── config/              # Configurações (Security, MongoDB, etc.)
│   │   │   ├── controller/          # Controladores REST/MVC
│   │   │   ├── dto/                # Data Transfer Objects
│   │   │   ├── entity/             # Entidades MongoDB
│   │   │   ├── repository/         # Repositórios de dados
│   │   │   ├── service/            # Lógica de negócio
│   │   │   └── SecureLoginApplication.java
│   │   └── resources/
│   │       ├── static/             # Recursos estáticos
│   │       │   ├── css/           # Estilos CSS
│   │       │   └── js/            # Scripts JavaScript
│   │       ├── templates/          # Templates Thymeleaf
│   │       │   ├── auth/          # Páginas de autenticação
│   │       │   ├── dashboard/     # Páginas do dashboard
│   │       │   ├── fragments/     # Fragmentos reutilizáveis
│   │       │   └── layout/        # Layouts base
│   │       ├── application.yml    # Configurações da aplicação
│   │       └── application.properties
│   └── test/                      # Testes
│       ├── java/                  # Testes unitários Java
│       ├── js/                    # Testes JavaScript
│       └── resources/             # Configurações de teste
├── docker/                        # Scripts Docker
├── docker-compose.yml             # Orquestração de serviços
├── pom.xml                        # Configuração Maven
├── package.json                   # Configuração Node.js
├── run-tests.ps1                  # Script de testes (Windows)
├── run-tests.sh                   # Script de testes (Linux/Mac)
└── README.md                      # Documentação
```

## 🎯 Funcionalidades

### 🔐 Autenticação
- **Login seguro** com validação de credenciais
- **Cadastro de usuários** com validação completa
- **Logout** com limpeza de sessão
- **Recuperação de senha** (estrutura preparada)

### 👤 Gerenciamento de Usuários
- **Perfil do usuário** com informações detalhadas
- **Edição de dados** pessoais
- **Alteração de senha** segura
- **Histórico de atividades**
- **Ativação/desativação** de contas

### 🛡️ Administração
- **Dashboard administrativo** com métricas
- **Gerenciamento de usuários** (listar, ativar/desativar)
- **Relatórios de segurança** em tempo real
- **Auditoria de login** detalhada
- **Desbloqueio de contas**

### 📊 Monitoramento
- **Tentativas de login** em tempo real
- **Estatísticas de uso** do sistema
- **Alertas de segurança** automáticos
- **Logs estruturados** para análise
- **Métricas por IP e usuário**

## 🔒 Segurança

### Medidas Implementadas

1. **Criptografia de Senhas**
   - BCrypt com strength 12
   - Salt automático para cada senha
   - Verificação segura de credenciais

2. **Controle de Acesso**
   - Spring Security 6.x
   - Roles e permissões granulares
   - Proteção de rotas sensíveis
   - CSRF protection

3. **Proteção contra Ataques**
   - Limite de tentativas de login (5 tentativas)
   - Bloqueio automático de contas (30 minutos)
   - Rate limiting por IP
   - Validação de entrada rigorosa

4. **Auditoria e Monitoramento**
   - Log de todas as tentativas de login
   - Rastreamento de IP e User-Agent
   - Relatórios de segurança
   - Alertas de atividades suspeitas

5. **Headers de Segurança**
   - X-Frame-Options: DENY
   - X-Content-Type-Options: nosniff
   - Strict-Transport-Security
   - Content-Security-Policy
   - X-XSS-Protection

6. **Validação de Dados**
   - Bean Validation (JSR-303)
   - Sanitização de entrada
   - Validação de email e senha
   - Proteção contra SQL Injection

## 🌐 API Endpoints

### Autenticação
- `GET /login` - Página de login
- `POST /login` - Processar login
- `GET /register` - Página de cadastro
- `POST /register` - Processar cadastro
- `POST /logout` - Logout

### Dashboard
- `GET /dashboard` - Dashboard do usuário
- `GET /dashboard/profile` - Perfil do usuário
- `POST /dashboard/profile` - Atualizar perfil

### Administração
- `GET /admin/dashboard` - Dashboard administrativo
- `GET /admin/users` - Lista de usuários
- `POST /admin/users/{id}/toggle` - Ativar/desativar usuário
- `POST /admin/users/{id}/unlock` - Desbloquear usuário
- `GET /admin/security` - Relatórios de segurança

## 🐳 Docker

### Serviços Disponíveis

#### MongoDB
```yaml
mongodb:
  image: mongo:7.0
  ports:
    - "27017:27017"
  volumes:
    - mongodb_data:/data/db
```

#### Mongo Express (Interface Web)
```yaml
mongo-express:
  image: mongo-express:1.0.0
  ports:
    - "8081:8081"
  environment:
    ME_CONFIG_MONGODB_URL: mongodb://mongodb:27017/
```

### Comandos Docker

```bash
# Iniciar todos os serviços
docker compose up -d

# Iniciar apenas MongoDB
docker compose up -d mongodb

# Parar todos os serviços
docker compose down

# Ver logs
docker compose logs -f

# Limpar volumes (CUIDADO: apaga dados)
docker compose down -v
```

## 🎨 Personalização

### Temas Visuais

1. **Cores e Estilos**
   - Edite `src/main/resources/static/css/style.css`
   - Modifique as variáveis CSS no início do arquivo

2. **Layouts**
   - Customize templates em `src/main/resources/templates/`
   - Use fragmentos para reutilização de código

3. **Funcionalidades**
   - Adicione novos controladores em `controller/`
   - Implemente novos serviços em `service/`

### Configurações

1. **Segurança**
   - Ajuste limites em `application.yml`
   - Configure timeouts de sessão
   - Modifique políticas de bloqueio

2. **Banco de Dados**
   - Modifique configurações do MongoDB
   - Adicione novos índices conforme necessário

3. **Logs**
   - Configure níveis de log em `application.yml`
   - Personalize formatos de log

## 🔧 Troubleshooting

### Problemas Comuns

#### 1. Erro de Conexão com MongoDB
```
MongoTimeoutException: Timed out after 30000 ms
```

**Solução:**
- Verifique se o Docker está rodando
- Execute `docker compose up -d mongodb`
- Aguarde 15-30 segundos para inicialização

#### 2. Erro de Compilação Java
```
Maven compilation error
```

**Solução:**
- Verifique se o Java 17+ está instalado
- Configure JAVA_HOME corretamente
- Execute `mvn clean compile`

#### 3. Testes Falhando
```
Tests run: X, Failures: Y, Errors: Z
```

**Solução:**
- Verifique se o MongoDB está rodando
- Execute `docker compose up -d mongodb`
- Aguarde a inicialização completa

#### 4. Erro de Dependências Node.js
```
'jest' não é reconhecido como um comando
```

**Solução:**
- Execute `npm install`
- Verifique se o Node.js está instalado

### Logs e Debug

1. **Habilitar Logs Detalhados**
   ```yaml
   logging:
     level:
       com.securelogin: DEBUG
       org.springframework.security: DEBUG
   ```

2. **Verificar Logs de Aplicação**
   ```bash
   tail -f logs/secure-login-system.log
   ```

3. **Testar Conexão MongoDB**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.arguments="--debug"
   ```

4. **Verificar Status dos Containers**
   ```bash
   docker compose ps
   docker compose logs mongodb
   ```

## 📊 Status dos Testes

- **Backend (Java):** ✅ 45 testes passando
- **Frontend (JavaScript):** ✅ 18 testes passando
- **Total:** ✅ 63/63 testes (100% de sucesso)

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 👥 Autores

- **Desenvolvedor Principal** - [Seu Nome](https://github.com/seu-usuario)

## 🙏 Agradecimentos

- Spring Boot Team
- MongoDB Team
- Bootstrap Team
- Comunidade Open Source

---

**⭐ Se este projeto foi útil para você, considere dar uma estrela!**