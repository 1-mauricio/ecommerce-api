# Sistema de E-commerce - API REST
Sistema completo de gerenciamento de pedidos e produtos para e-commerce desenvolvido com Spring Boot.

## Visão Geral
Este projeto implementa um sistema de e-commerce completo com as seguintes funcionalidades principais:
- **Autenticação e Autorização** com JWT
- **Gerenciamento de Produtos** com controle de estoque
- **Gerenciamento de Pedidos** com diferentes status
- **Sistema de Relatórios** para análise de dados
- **API REST** documentada e testada

## Tecnologias Utilizadas

### Backend
- **Java 17** - Linguagem principal
- **Spring Boot 3.5.3** - Framework principal
- **Spring Security** - Segurança e autenticação
- **Spring Data JPA** - Persistência de dados
- **MySQL 8.0** - Banco de dados principal
- **H2 Database** - Banco de dados para testes
- **JWT (JSON Web Tokens)** - Autenticação stateless
- **Maven** - Gerenciamento de dependências

### Testes
- **JUnit 5** - Framework de testes
- **Mockito** - Mocking para testes unitários
- **Spring Security Test** - Testes de segurança
- **Spring Boot Test** - Testes de integração

### Ferramentas
- **Lombok** - Redução de boilerplate
- **Spring Dotenv** - Carregamento de variáveis de ambiente
- **Bean Validation** - Validação de dados

## Arquitetura do Sistema

### Padrões Arquiteturais
- **Layered Architecture** - Separação clara entre camadas
- **Repository Pattern** - Abstração da camada de dados
- **Service Layer Pattern** - Lógica de negócio centralizada
- **DTO Pattern** - Transferência de dados entre camadas
- **REST API Design** - Endpoints RESTful padronizados

### Estrutura do Projeto
```
src/
├── main/java/com/ecommerce/
│   ├── config/           # Configurações do sistema
│   │   ├── SecurityConfig.java      # Configuração de segurança
│   │   ├── JwtConfig.java           # Configuração JWT
│   │   ├── JacksonConfig.java       # Configuração JSON
│   │   └── EnvironmentConfig.java   # Configuração de ambiente
│   ├── controller/       # Controllers REST
│   │   ├── AuthController.java      # Autenticação
│   │   ├── ProductController.java   # Produtos
│   │   ├── OrderController.java     # Pedidos
│   │   └── ReportController.java    # Relatórios
│   ├── dto/             # Data Transfer Objects
│   │   ├── auth/        # DTOs de autenticação
│   │   ├── product/     # DTOs de produtos
│   │   ├── order/       # DTOs de pedidos
│   │   └── report/      # DTOs de relatórios
│   ├── entity/          # Entidades JPA
│   │   ├── User.java    # Usuário
│   │   ├── Product.java # Produto
│   │   ├── Order.java   # Pedido
│   │   └── OrderItem.java # Item do pedido
│   ├── enums/           # Enums do sistema
│   │   ├── UserRole.java    # Roles de usuário
│   │   └── OrderStatus.java # Status de pedido
│   ├── exception/       # Tratamento de exceções
│   │   ├── CustomExceptions.java    # Exceções customizadas
│   │   └── GlobalExceptionHandler.java # Handler global
│   ├── repository/      # Repositórios JPA
│   │   ├── UserRepository.java      # Repositório de usuários
│   │   ├── ProductRepository.java   # Repositório de produtos
│   │   └── OrderRepository.java     # Repositório de pedidos
│   ├── security/        # Configurações de segurança
│   │   ├── JwtTokenProvider.java    # Geração de tokens JWT
│   │   ├── JwtAuthenticationFilter.java # Filtro de autenticação
│   │   └── JwtAuthenticationEntryPoint.java # Entry point
│   └── service/         # Lógica de negócio
│       ├── AuthService.java         # Serviço de autenticação
│       ├── ProductService.java      # Serviço de produtos
│       ├── OrderService.java        # Serviço de pedidos
│       └── ReportService.java       # Serviço de relatórios
├── resources/
│   ├── application.yml  # Configurações da aplicação
│   └── db/
│       └── database_dump.sql # Dump do banco de dados
└── test/                # Testes unitários e de integração
    ├── java/com/ecommerce/
    │   ├── service/     # Testes de serviços
    │   └── integration/ # Testes de integração
    └── resources/       # Recursos de teste
```

## Funcionalidades Detalhadas

### 1. Autenticação e Autorização
- **Registro de usuários** com validação de dados
- **Login com JWT** e expiração configurável
- **Controle de acesso baseado em roles** (USER/ADMIN)
- **Proteção de endpoints** com Spring Security
- **Validação de tokens** em todas as requisições autenticadas

### 2. Gerenciamento de Produtos
- **CRUD completo** de produtos
- **Busca por nome** com paginação
- **Filtro por categoria** com paginação
- **Controle de estoque** automático
- **Validação de dados** com Bean Validation
- **Acesso restrito** apenas para ADMIN (criar/editar/deletar)

### 3. Gerenciamento de Pedidos
- **Criação de pedidos** com múltiplos itens
- **Processamento de pagamento** com atualização de status
- **Histórico de pedidos** por usuário
- **Controle de status** (PENDING/PAID/CANCELLED/SHIPPED/DELIVERED)
- **Validação de estoque** durante criação
- **Cálculo automático** de valores

### 4. Sistema de Relatórios (ADMIN)
- **Top 5 usuários** por valor total de compras
- **Ticket médio** por usuário
- **Faturamento mensal** com contagem de pedidos
- **Consultas otimizadas** com queries nativas
- **Dados agregados** para análise

## Configuração e Instalação

### Pré-requisitos
- **Java 17** ou superior
- **Maven 3.6** ou superior
- **MySQL 8.0** ou superior
- **Git** para clonagem do repositório

### 1. Clone do Repositório
```bash
git clone <url-do-repositorio>
cd ecommerce-system
```

### 2. Configuração do Banco de Dados
#### Opção A: Usando o Dump do Banco (Recomendado)
O projeto inclui um dump completo do banco de dados com estrutura e dados de exemplo.
```bash
# 1. Criar o banco de dados
mysql -u root -p -e "CREATE DATABASE ecommerce_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
# 2. Importar o dump
mysql -u root -p ecommerce_db < src/main/resources/db/database_dump.sql
```
#### Opção B: Criação Manual
```sql
CREATE DATABASE ecommerce_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'ecommerce_user'@'localhost' IDENTIFIED BY 'sua_senha_segura';
GRANT ALL PRIVILEGES ON ecommerce_db.* TO 'ecommerce_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configuração das Variáveis de Ambiente
Copie o arquivo de exemplo e configure suas variáveis:
```bash
cp env.example .env
```
Edite o arquivo `.env` com suas configurações:
```env
# Configurações do Banco de Dados
DB_HOST=localhost
DB_PORT=3306
DB_NAME=ecommerce_db
DB_USERNAME=root
DB_PASSWORD=sua_senha_aqui
# Configurações de Segurança JWT
JWT_SECRET=sua_chave_secreta_jwt_muito_segura_aqui
JWT_EXPIRATION=86400000
# Configurações do Servidor
SERVER_PORT=8080
# Configurações de Logging
LOG_LEVEL=INFO
HIBERNATE_SQL_LOG=false
HIBERNATE_BINDER_LOG=false
# Configurações do Hibernate
HIBERNATE_DDL_AUTO=validate
SHOW_SQL=false
FORMAT_SQL=false
```

### 4. Execução da Aplicação
```bash
# Compilar o projeto
mvn clean compile
# Executar a aplicação
mvn spring-boot:run
```
A aplicação estará disponível em: `http://localhost:8080`

### 5. Verificação da Instalação
```bash
# Verificar se a aplicação está rodando
curl http://localhost:8080/api/products
# Verificar dados de exemplo
curl http://localhost:8080/api/products/categories
```

## Dados de Exemplo
O dump do banco inclui dados de exemplo para teste:

### Usuários Pré-cadastrados
- **Admin**: `admin@ecommerce.com` / `password` (Role: ADMIN)
- **Usuário**: `user@ecommerce.com` / `password` (Role: USER)
- **Cliente**: `cliente@ecommerce.com` / `password` (Role: USER)

### Produtos de Exemplo
- Smartphone Samsung Galaxy S23 - R$ 2.999,99
- Notebook Dell Inspiron 15 - R$ 2.499,99
- Camiseta Nike Dri-FIT - R$ 89,99
- Tênis Adidas Ultraboost - R$ 499,99
- Livro Spring Boot em Ação - R$ 79,99

### Pedidos de Exemplo
- Pedidos já processados com status PAID
- Itens de pedido com quantidades e preços

## API Endpoints

### Base URL
```
http://localhost:8080/api
```

### Autenticação
| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/auth/register` | Registrar novo usuário | Não |
| POST | `/auth/login` | Fazer login | Não |

### Produtos
| Método | Endpoint | Descrição | Autenticação | Role |
|--------|----------|-----------|--------------|------|
| GET | `/products` | Listar produtos (paginado) | Não | - |
| GET | `/products/{id}` | Buscar produto por ID | Não | - |
| GET | `/products/search?name={nome}` | Buscar por nome | Não | - |
| GET | `/products/category/{categoria}` | Buscar por categoria | Não | - |
| GET | `/products/categories` | Listar categorias | Não | - |
| POST | `/products` | Criar produto | Sim | ADMIN |
| PUT | `/products/{id}` | Atualizar produto | Sim | ADMIN |
| DELETE | `/products/{id}` | Deletar produto | Sim | ADMIN |

### Pedidos
| Método | Endpoint | Descrição | Autenticação | Role |
|--------|----------|-----------|--------------|------|
| GET | `/orders` | Listar pedidos do usuário | Sim | USER |
| GET | `/orders/{id}` | Buscar pedido por ID | Sim | USER |
| POST | `/orders` | Criar pedido | Sim | USER |
| POST | `/orders/{id}/pay` | Pagar pedido | Sim | USER |

### Relatórios
| Método | Endpoint | Descrição | Autenticação | Role |
|--------|----------|-----------|--------------|------|
| GET | `/reports/top-users` | Top 5 usuários | Sim | ADMIN |
| GET | `/reports/average-tickets` | Ticket médio por usuário | Sim | ADMIN |
| GET | `/reports/monthly-revenue?year={ano}&month={mes}` | Faturamento mensal | Sim | ADMIN |

## Exemplos de Uso

### 1. Login com Usuário de Exemplo
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@ecommerce.com",
    "password": "password"
  }'
```

### 2. Listar Produtos Disponíveis
```bash
curl -X GET "http://localhost:8080/api/products?page=0&size=10" \
  -H "Content-Type: application/json"
```

### 3. Criar Pedido (com token JWT)
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <seu_token_jwt>" \
  -d '{
    "items": [
      {
        "productId": "660e8400-e29b-41d4-a716-446655440000",
        "quantity": 1
      }
    ]
  }'
```

### 4. Gerar Relatório (ADMIN)
```bash
curl -X GET "http://localhost:8080/api/reports/top-users" \
  -H "Authorization: Bearer <token_jwt_admin>"
```

## Testes

### Executar Todos os Testes
```bash
mvn test
```

### Executar Testes Específicos
```bash
# Testes de serviços
mvn test -Dtest=ProductServiceTest
mvn test -Dtest=OrderServiceTest
mvn test -Dtest=AuthServiceTest
mvn test -Dtest=ReportServiceTest
# Testes de integração
mvn test -Dtest=EcommerceIntegrationTest
# Teste principal da aplicação
mvn test -Dtest=EcommerceSystemApplicationTests
```

### Cobertura de Testes
- **36 testes** implementados
- **100% de sucesso** em execução
- **Cobertura completa** dos serviços principais
- **Testes unitários** com Mockito
- **Testes de integração** com H2 Database

### Estrutura de Testes
```
src/test/java/com/ecommerce/
├── service/              # Testes de serviços
│   ├── AuthServiceTest.java
│   ├── ProductServiceTest.java
│   ├── OrderServiceTest.java
│   └── ReportServiceTest.java
├── integration/          # Testes de integração
│   └── EcommerceIntegrationTest.java
└── EcommerceSystemApplicationTests.java
```

## Configurações Avançadas

### Perfis Disponíveis
- **default** - Configuração padrão para desenvolvimento
- **test** - Configuração para testes (H2 in-memory)
- **dev** - Configuração de desenvolvimento
- **prod** - Configuração de produção

### Configurações de Segurança
- **JWT** com expiração configurável (padrão: 24 horas)
- **Roles**: USER e ADMIN
- **Endpoints protegidos** por autenticação
- **CORS** configurado para desenvolvimento
- **Validação de entrada** com Bean Validation
- **Proteção contra SQL Injection** via JPA/Hibernate

### Configurações de Banco de Dados
- **MySQL 8.0** como banco principal
- **H2 Database** para testes
- **JPA/Hibernate** para ORM
- **Timezone UTC** para consistência
- **Charset UTF-8** para suporte a caracteres especiais

### Configurações de Logging
- **Logs estruturados** com Spring Boot
- **Níveis configuráveis** por ambiente
- **Logs SQL** opcionais para debug
- **Tratamento global** de exceções

## Monitoramento e Observabilidade

### Logs
- **Logs estruturados** com Spring Boot
- **Níveis configuráveis** (INFO, DEBUG, ERROR)
- **Logs SQL** para debugging de queries
- **Tratamento global** de exceções

### Métricas (Futuro)
- **Health checks** para monitoramento
- **Métricas de performance** 
- **Métricas de negócio** (pedidos, vendas)
- **Alertas** para problemas críticos

## Segurança

### Autenticação
- **JWT Authentication** com tokens seguros
- **Expiração configurável** de tokens
- **Refresh tokens** (implementação futura)
- **Logout** com blacklist de tokens

### Autorização
- **Role-based Access Control** (RBAC)
- **Controle granular** por endpoint
- **Separação clara** entre USER e ADMIN
- **Validação de permissões** em tempo de execução

### Validação de Dados
- **Bean Validation** para entrada de dados
- **Sanitização** de inputs
- **Validação de tipos** e formatos
- **Mensagens de erro** claras

### Proteção de Dados
- **Senhas criptografadas** com BCrypt
- **Dados sensíveis** não expostos em logs
- **HTTPS** recomendado para produção
- **Headers de segurança** configuráveis

## Troubleshooting

### Problemas Comuns
#### 1. Erro de Conexão com Banco
```
Error: Could not create connection to database server
```
**Solução:**
- Verificar se o MySQL está rodando
- Confirmar credenciais no arquivo `.env`
- Verificar se o banco `ecommerce_db` existe
- Importar o dump: `mysql -u root -p ecommerce_db < src/main/resources/db/database_dump.sql`
#### 2. Erro de Porta em Uso
```
Error: Web server failed to start. Port 8080 was already in use
```
**Solução:**
```bash
# Verificar processos na porta 8080
lsof -i :8080
# Matar processo se necessário
kill -9 <PID>
# Ou alterar porta no .env
SERVER_PORT=8081
```
#### 3. Erro de JWT Secret
```
Error: JWT secret cannot be null or empty
```
**Solução:**
- Verificar se `JWT_SECRET` está configurado no `.env`
- Gerar uma chave secreta forte:
```bash
openssl rand -base64 64
```
#### 4. Erro de Compilação Java
```
Error: Source option 6 is no longer supported
```
**Solução:**
- Verificar se Java 17+ está instalado:
```bash
java -version
```
- Atualizar JAVA_HOME se necessário
#### 5. Testes Falhando
```
Error: Tests failed
```
**Solução:**
- Verificar se H2 Database está configurado para testes
- Limpar cache do Maven:
```bash
mvn clean
```
- Executar testes isoladamente
#### 6. Erro de Validação do Schema
```
Error: Schema validation failed
```
**Solução:**
- Verificar se o dump foi importado corretamente
- Alterar `HIBERNATE_DDL_AUTO` para `update` temporariamente
- Verificar se as tabelas existem no banco

### Logs de Debug
Para ativar logs detalhados, adicione ao `.env`:
```env
LOG_LEVEL=DEBUG
HIBERNATE_SQL_LOG=true
HIBERNATE_BINDER_LOG=true
SHOW_SQL=true
```

### Verificação de Saúde
```bash
# Verificar se a aplicação está rodando
curl http://localhost:8080/api/products
# Verificar dados de exemplo
curl http://localhost:8080/api/products/categories
# Verificar logs da aplicação
tail -f logs/application.log
```

## Contribuição

### Padrões de Código
- **Java 17** com recursos modernos
- **Spring Boot** seguindo best practices
- **Clean Code** com nomes descritivos
- **Documentação** em português
- **Testes** para todas as funcionalidades

### Processo de Desenvolvimento
1. **Fork** do repositório
2. **Branch** para nova funcionalidade
3. **Desenvolvimento** com testes
4. **Pull Request** com descrição clara
5. **Code Review** obrigatório
6. **Merge** após aprovação

### Checklist para PR
- [ ] Código segue padrões do projeto
- [ ] Testes implementados e passando
- [ ] Documentação atualizada
- [ ] Sem warnings de compilação
- [ ] Funcionalidade testada manualmente

## Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

## Contato
Para dúvidas, sugestões ou problemas:
- **Issues**: Abra uma issue no GitHub
- **Email**: [seu-email@example.com]
- **Documentação**: [link-para-docs]

## Roadmap

### Versão Atual (1.0.0)
- [x] Autenticação JWT
- [x] CRUD de produtos
- [x] Gerenciamento de pedidos
- [x] Sistema de relatórios
- [x] Testes completos
- [x] Dump do banco com dados de exemplo

### Próximas Versões
- [ ] **1.1.0**: Paginação avançada e filtros
- [ ] **1.2.0**: Upload de imagens de produtos
- [ ] **1.3.0**: Sistema de cupons e descontos
- [ ] **2.0.0**: Microserviços e cache distribuído
- [ ] **2.1.0**: Integração com gateways de pagamento
- [ ] **2.2.0**: Sistema de notificações
- [ ] **3.0.0**: Interface web completa
