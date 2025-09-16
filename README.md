# 🔐 Sistema de Login Seguro

Sistema de autenticação básico desenvolvido com **Spring Boot**, **Thymeleaf** e **MongoDB Atlas**. Implementa login, cadastro e controle de acesso por roles.

## 📋 Índice

- [Características](#-características)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Configuração](#-instalação-e-configuração)
- [Configuração do MongoDB Atlas](#-configuração-do-mongodb-atlas)
- [Executando a Aplicação](#-executando-a-aplicação)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Funcionalidades](#-funcionalidades)
- [Segurança](#-segurança)
- [API Endpoints](#-api-endpoints)
- [Personalização](#-personalização)
- [Troubleshooting](#-troubleshooting)
- [Contribuição](#-contribuição)
- [Licença](#-licença)

## ✨ Características

### 🔒 Segurança Básica
- **Criptografia de senhas** com BCrypt
- **Validação de dados** com Bean Validation
- **Headers de segurança** básicos

### 👥 Gerenciamento de Usuários
- **Cadastro de usuários** com validação
- **Sistema de roles** (ADMIN, USER)
- **Controle de acesso** por roles
- **Login e logout** funcionais

### 🎨 Interface Simples
- **Design responsivo** com Bootstrap
- **Templates Thymeleaf** modulares
- **Páginas de login, cadastro e dashboard**

## 🛠 Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot 3.2.0**
- **Spring Security 6.x**
- **Spring Data MongoDB**
- **Thymeleaf 3.x**
- **Bootstrap 5.3.0**
- **MongoDB Atlas**
- **Maven 3.8+**

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java 17 ou superior**
- **Maven 3.8 ou superior**
- **Git**
- **Conta no MongoDB Atlas** (gratuita)
- **IDE de desenvolvimento** (recomendado: IntelliJ IDEA)

## 🚀 Instalação e Configuração

### 1. Clone o Repositório

```bash
git clone https://github.com/seu-usuario/secure-login-system.git
cd secure-login-system
```

### 2. Configuração do MongoDB Atlas

#### 2.1 Criar Cluster no MongoDB Atlas

1. Acesse [MongoDB Atlas](https://www.mongodb.com/atlas)
2. Crie uma conta gratuita ou faça login
3. Crie um novo cluster (opção gratuita disponível)
4. Configure o nome do cluster (ex: `secure-login-cluster`)

#### 2.2 Configurar Acesso ao Banco

1. Vá para **Database Access**
2. Clique em **Add New Database User**
3. Crie um usuário com permissões de leitura e escrita
4. Anote o **username** e **password**

#### 2.3 Configurar Rede

1. Vá para **Network Access**
2. Clique em **Add IP Address**
3. Adicione `0.0.0.0/0` para permitir acesso de qualquer IP (ou configure IPs específicos)

#### 2.4 Obter String de Conexão

1. Vá para **Clusters**
2. Clique em **Connect** no seu cluster
3. Selecione **Connect your application**
4. Copie a string de conexão
5. Substitua `<password>` pela senha do usuário criado

### 3. Configurar Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto:

```env
MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/secure-login?retryWrites=true&w=majority
```

Ou configure diretamente no `application.properties`:

```properties
spring.data.mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/secure-login?retryWrites=true&w=majority
```

## ▶️ Executando a Aplicação

### 1. Compilar o Projeto

```bash
mvn clean compile
```

### 2. Executar a Aplicação

```bash
mvn spring-boot:run
```

### 3. Acessar a Aplicação

Abra seu navegador e acesse:
- **URL:** http://localhost:8080
- **Login padrão:** `admin` / `admin123`

## 📁 Estrutura do Projeto

```
secure-login-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/securelogin/
│   │   │       ├── config/          # Configurações
│   │   │       ├── controller/      # Controladores REST/MVC
│   │   │       ├── dto/            # Data Transfer Objects
│   │   │       ├── entity/         # Entidades JPA/MongoDB
│   │   │       ├── repository/     # Repositórios de dados
│   │   │       ├── service/        # Lógica de negócio
│   │   │       └── SecureLoginApplication.java
│   │   └── resources/
│   │       ├── static/             # Recursos estáticos
│   │       │   ├── css/           # Estilos CSS
│   │       │   ├── js/            # Scripts JavaScript
│   │       │   └── images/        # Imagens
│   │       ├── templates/          # Templates Thymeleaf
│   │       │   ├── auth/          # Páginas de autenticação
│   │       │   ├── dashboard/     # Páginas do dashboard
│   │       │   ├── admin/         # Páginas administrativas
│   │       │   ├── fragments/     # Fragmentos reutilizáveis
│   │       │   └── layout/        # Layouts base
│   │       ├── application.yml    # Configurações da aplicação
│   │       └── application.properties
│   └── test/                      # Testes unitários
├── pom.xml                        # Configuração Maven
└── README.md                      # Documentação
```

## 🎯 Funcionalidades

### 🔐 Autenticação
- **Login seguro** com validação de credenciais
- **Cadastro de usuários** com validação completa
- **Logout** com limpeza de sessão
- **Recuperação de senha** (em desenvolvimento)

### 👤 Gerenciamento de Usuários
- **Perfil do usuário** com informações detalhadas
- **Edição de dados** pessoais
- **Alteração de senha** segura
- **Histórico de atividades**

### 🛡️ Administração
- **Dashboard administrativo** com métricas
- **Gerenciamento de usuários** (ativar/desativar)
- **Relatórios de segurança** em tempo real
- **Auditoria de login** detalhada
- **Desbloqueio de contas**

### 📊 Monitoramento
- **Tentativas de login** em tempo real
- **Estatísticas de uso** do sistema
- **Alertas de segurança** automáticos
- **Logs estruturados** para análise

## 🔒 Segurança

### Medidas Implementadas

1. **Criptografia de Senhas**
   - BCrypt com strength 12
   - Salt automático para cada senha

2. **Controle de Acesso**
   - Spring Security 6.x
   - Roles e permissões granulares
   - Proteção de rotas sensíveis

3. **Proteção contra Ataques**
   - Limite de tentativas de login
   - Bloqueio automático de contas
   - Rate limiting por IP

4. **Auditoria e Monitoramento**
   - Log de todas as tentativas de login
   - Rastreamento de IP e User-Agent
   - Relatórios de segurança

5. **Headers de Segurança**
   - X-Frame-Options: DENY
   - X-Content-Type-Options: nosniff
   - Strict-Transport-Security
   - Content-Security-Policy

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

### Administração
- `GET /admin/dashboard` - Dashboard administrativo
- `GET /admin/users` - Lista de usuários
- `POST /admin/users/{id}/toggle` - Ativar/desativar usuário
- `POST /admin/users/{id}/unlock` - Desbloquear usuário
- `GET /admin/security` - Relatórios de segurança

## 🎨 Personalização

### Temas Visuais

O sistema foi desenvolvido para ser facilmente personalizável:

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
   - Ajuste limites em `application.properties`
   - Configure timeouts de sessão

2. **Banco de Dados**
   - Modifique configurações do MongoDB
   - Adicione novos índices conforme necessário

## 🔧 Troubleshooting

### Problemas Comuns

#### 1. Erro de Conexão com MongoDB
```
MongoTimeoutException: Timed out after 30000 ms
```

**Solução:**
- Verifique se a string de conexão está correta
- Confirme se o IP está liberado no MongoDB Atlas
- Teste a conectividade de rede

#### 2. Erro de Autenticação
```
Authentication failed
```

**Solução:**
- Verifique se o usuário existe no banco
- Confirme se a senha está correta
- Verifique se a conta não está bloqueada

#### 3. Erro de Compilação
```
Maven compilation error
```

**Solução:**
- Verifique se o Java 17+ está instalado
- Execute `mvn clean compile`
- Verifique dependências no `pom.xml`

### Logs e Debug

1. **Habilitar Logs Detalhados**
   ```properties
   logging.level.com.securelogin=DEBUG
   logging.level.org.springframework.security=DEBUG
   ```

2. **Verificar Logs de Aplicação**
   ```bash
   tail -f logs/secure-login-system.log
   ```

3. **Testar Conexão MongoDB**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.arguments="--debug"
   ```
