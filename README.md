# Simulador Financeiro Fullstack

> **⚠️ AVISO IMPORTANTE (DEPLOY GRATUITO):**
> O Backend deste projeto está hospedado no plano gratuito do **Render**. Devido à inatividade, o servidor entra em modo de hibernação.
> **A primeira requisição pode levar de 1 a 5 minutos para acordar o servidor.** Por favor, aguarde o carregamento inicial até que o Frontend consiga conectar.

![Badge Java](https://img.shields.io/badge/Java-21-orange)
![Badge Spring Boot](https://img.shields.io/badge/Spring_Boot-3-green)
![Badge Database](https://img.shields.io/badge/PostgreSQL-Neon-blue)
![Badge Frontend](https://img.shields.io/badge/Frontend-HTML%2FJS-yellow)
![Badge Status](https://img.shields.io/badge/Status-Concluído-brightgreen)

### 👨‍💻 Autor
Desenvolvido por Gabriel Engenheiro de Software Fullstack

https://www.linkedin.com/in/gabriel-kran-milhomem/ | https://github.com/GabrielKran

---

## 🌐 Links do Projeto (Live Demo)
- **Aplicação (Frontend):** https://simulador-financeiro-three.vercel.app/
- **API (Backend):** https://simulador-financeiro-fullstack.onrender.com

---

## 📖 Sobre o Projeto
O **Simulador Financeiro** é uma aplicação Fullstack desenvolvida para auxiliar usuários no planejamento de metas financeiras de longo prazo. O sistema permite que o usuário cadastre objetivos (ex: "Aposentadoria", "Carro Novo"), defina aportes mensais e taxas de juros, e visualize graficamente a evolução do patrimônio ao longo do tempo.

O foco principal do projeto foi a **Segurança** e a **Arquitetura**, implementando autenticação Stateless com JWT auth0, proteção de dados sensíveis e uma infraestrutura robusta em nuvem.

---

## 🚀 Tecnologias e Arquitetura

### Backend (API RESTful)
Construído com **Java 21** e **Spring Boot 3**.
- **Spring Security + JWT:** Autenticação Stateless segura. O Token é gerado no login e validado automaticamente nas rotas protegidas.
- **BCrypt:** As senhas são criptografadas antes de serem salvas no banco.
- **Spring Data JPA:** Camada de persistência otimizada com PostgreSQL.
- **Transient Calculation:** O cálculo de "meses estimados" para atingir a meta é feito em tempo de execução (memória), evitando redundância no banco de dados.
- **Validation & Lombok:** Validação de dados na entrada (DTOs) e redução de código repetitivo.
- **Cors Configuration:** Configurado para aceitar apenas requisições da origem confiável (Vercel).

### Frontend (Client)
Interface responsiva desenvolvida com **Mobile First** mindset.
- **HTML5, CSS3, JavaScript (Vanilla):** Arquitetura sem frameworks para controle total do DOM e performance máxima.
- **Fetch API:** Camada de serviço (`utils.js`) que intercepta requisições HTTP, injeta o Token JWT automaticamente e trata erros 401/403 (Sessão Expirada).
- **Chart.js:** Renderização de gráficos financeiros interativos e dinâmicos.

### Banco de Dados & Infraestrutura
- **Produção:** PostgreSQL (Hospedado na Neon.tech).
- **Desenvolvimento Local:** PostgreSQL (Recomendado via Docker).
- **Deploy:** Backend no Render (Dockerizado) e Frontend na Vercel.

---

## 📚 Documentação da API (Endpoints)

A API segue o padrão REST. Todos os endpoints protegidos exigem o cabeçalho `Authorization: Bearer <token>`.

| Método | Endpoint | Descrição | Autenticação |
| :--- | :--- | :--- | :--- |
| **POST** | `/auth/login` | Recebe email/senha e retorna o Token JWT. | ❌ Pública |
| **POST** | `/auth/register` | Registra um novo usuário no sistema. | ❌ Pública |
| **GET** | `/planos-financeiros` | Lista todos os planos do usuário logado. | 🔒 Privada |
| **GET** | `/planos-financeiros/{id}` | Detalhes de um plano específico. | 🔒 Privada |
| **POST** | `/planos-financeiros` | Cria um novo objetivo financeiro. | 🔒 Privada |
| **PUT** | `/planos-financeiros/{id}` | Atualiza metas ou aportes de um plano. | 🔒 Privada |
| **DELETE** | `/planos-financeiros/{id}` | Remove um plano do banco de dados. | 🔒 Privada |
| **PATCH** | `/usuarios/me/nome` | Troca nome de usuário. | 🔒 Privada |
| **PATCH** | `/usuarios/me/senha` | Troca senha de usuário. | 🔒 Privada |
| **DELETE** | `/usuarios/me` | Apaga o usuário. | 🔒 Privada |

---

## 🧪 Qualidade de Código
- **Testes Unitários:** Implementados com JUnit 5 e Mockito para validar regras de negócio no `Service Layer`.

---

## ⚙️ Como Rodar Localmente

Siga os passos abaixo para clonar e configurar o ambiente de desenvolvimento.

### 1. Pré-requisitos
- Java 21 JDK
- Maven
- PostgreSQL (Instalado localmente ou rodando via Docker)

### 2. Configuração do Banco de Dados
Certifique-se de que o serviço do PostgreSQL esteja rodando. Crie um banco de dados vazio (ex: `simulador_db`).

### 3. Variáveis de Ambiente
Por motivos de segurança, este projeto não contém senhas hardcoded. Para rodar, você deve configurar as variáveis de ambiente no seu sistema ou IDE, correspondendo às chaves definidas no `application.properties`.

**Exemplo de variáveis necessárias:**

```properties
# Ajuste as chaves (esquerda) conforme seu application.properties
DB_URL=jdbc:postgresql://localhost:5432/simulador_db
DB_USERNAME=postgres
DB_PASSWORD=sua_senha_local
JWT_SECRET=sua_chave_secreta_para_assinatura_jwt
```

### 4. Instalação e Execução

```bash
# Clone o repositório
git clone https://github.com/GabrielKran/simulador-financeiro-fullstack

# Entre na pasta do projeto
cd simulador-financeiro-fullstack

# Instale as dependências e compile o projeto
mvn clean install

# Execute a aplicação
mvn spring-boot:run