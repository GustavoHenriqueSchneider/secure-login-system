// Script de inicialização do MongoDB para o projeto Secure Login System
db = db.getSiblingDB('secure-login');

// Criar usuário para a aplicação
db.createUser({
  user: 'appuser',
  pwd: 'apppassword',
  roles: [
    {
      role: 'readWrite',
      db: 'secure-login'
    }
  ]
});

// Criar coleções e índices
db.createCollection('users');
db.createCollection('login_attempts');
db.createCollection('roles');

// Criar índices para a coleção users
db.users.createIndex({ "username": 1 }, { unique: true });
db.users.createIndex({ "email": 1 }, { unique: true });
db.users.createIndex({ "isActive": 1 });
db.users.createIndex({ "roles": 1 });
db.users.createIndex({ "createdAt": 1 });

// Criar índices para a coleção login_attempts
db.login_attempts.createIndex({ "username": 1 });
db.login_attempts.createIndex({ "ipAddress": 1 });
db.login_attempts.createIndex({ "success": 1 });
db.login_attempts.createIndex({ "attemptTime": 1 });
db.login_attempts.createIndex({ "username": 1, "success": 1 });
db.login_attempts.createIndex({ "ipAddress": 1, "success": 1, "attemptTime": 1 });

// Criar índices para a coleção roles
db.roles.createIndex({ "name": 1 }, { unique: true });

// Inserir roles padrão
db.roles.insertMany([
  {
    name: "USER",
    description: "Usuário padrão do sistema"
  },
  {
    name: "ADMIN",
    description: "Administrador do sistema"
  }
]);

print('MongoDB inicializado com sucesso para o Secure Login System!');

