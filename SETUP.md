# Hyperativa Application

## Prerequisites

### Option 1: Docker (Recommended for quick setup)
- Docker
- Docker Compose

### Option 2: Java & Maven (For development)
- Java 21
- Maven 3.9.11 or higher
- **MySQL Database** (must be provided separately) You can use a local MySQL installation or connect to a remote database instance

---

## 🐳 Docker Deployment (Recommended)

### Using Environment Variables with Docker Compose

The `docker-compose.yml` file is configured to use environment variables. You have two options:

#### Option 1: Use .env file (Recommended)
Create a `.env` file in the project root:

```env
# Database Configuration
DATABASE_PASSWORD=your_secure_password_here
DATABASE_NAME=hyperativa_db
DATABASE_USERNAME=app_user
MYSQL_PORT=3306
DATABASE_PORT=3306

# API Configuration
API_PORT=8080

# Security Configuration
SECRET=your_jwt_secret_key_here
SALT=your_password_salt_here
ISSUER=hyperativa-app
```

**Important**: The `.env` file will be automatically loaded by Docker Compose.

#### Option 2: Use system environment variables

**Linux/macOS:**
```bash
export DATABASE_PASSWORD=your_password
export DATABASE_NAME=hyperativa_db
export SECRET=your_secret
# ... set other variables
docker-compose up --build
```

**Windows (Command Prompt):**
```cmd
set DATABASE_PASSWORD=your_password
set DATABASE_NAME=hyperativa_db
set SECRET=your_secret
docker-compose up --build
```

**Windows (PowerShell):**
```powershell
$env:DATABASE_PASSWORD="your_password"
$env:DATABASE_NAME="hyperativa_db"
$env:SECRET="your_secret"
docker-compose up --build
```

### Quick Start with Docker

#### 1. Clone the repository and navigate to the project directory

#### 2. Create and configure the .env file (optional but recommended)

**Linux/macOS:**
```bash
# Copy the example template and edit it
cp .env.example .env
# Edit the .env file with your preferred values
nano .env  # or use your preferred editor (vim, code, etc.)
```

**Windows (Command Prompt):**
```cmd
copy .env.example .env
notepad .env
```

**Windows (PowerShell):**
```powershell
Copy-Item .env.example .env
notepad .env
```

#### 3. Build and start the application
```bash
docker-compose up --build
```

Docker Compose will automatically:
- Read variables from the `.env` file
- Use default values for any missing variables
- Set up both the database and API with your configuration

#### 4. Access the application
- **API Service**: http://localhost:8080 (or your custom API_PORT)
- **MySQL Database**: localhost:3306 (or your custom MYSQL_PORT)

### Docker Service Overview

#### hyperdb (MySQL Database)
- MySQL 8.0 database
- Persistent data storage using Docker volumes
- Automatically configured with your environment variables
- Accessible on port 3306 (configurable)

#### hyperapi (Application API)
- Spring Boot/Java application
- Connects automatically to the MySQL database
- Uses security settings from your environment variables
- Runs on port 8080 (configurable)

---

## ☕ Java & Maven Deployment (For Development)

### Important Prerequisites for Java Deployment
- **Java 21** must be installed
- **Maven 3.9.11** or higher recommended
- **MySQL Database must be installed and running separately**

### Manual Setup Steps

#### 1. Configure Application Properties
Create or modify `src/main/resources/application.yml`:

```yaml
# Database Configuration
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hyperativa_db
    username: app_user
    password: your_password_here
  
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect

# Server Configuration
server:
  port: 8080

# Custom Security Configuration
app:
  security:
    secret: your_jwt_secret_key_here
    salt: your_password_salt_here
    issuer: hyperativa-app
```

#### 2. Build the Application
```bash
mvn clean compile
```

#### 3. Run the Application
```bash
mvn spring-boot:run
```

### Using Environment Variables with Java/Maven

You can also use environment variables by modifying the `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/${DATABASE_NAME:hyperativa_db}
    username: ${DATABASE_USERNAME:app_user}
    password: ${DATABASE_PASSWORD:}

app:
  security:
    secret: ${SECRET:}
    salt: ${SALT:}
    issuer: ${ISSUER:hyperativa-app}
```

**Linux/macOS:**
```bash
export DATABASE_PASSWORD=your_password
export SECRET=your_secret
export SALT=your_salt
mvn spring-boot:run
```

**Windows (Command Prompt):**
```cmd
set DATABASE_PASSWORD=your_password
set SECRET=your_secret
set SALT=your_salt
mvn spring-boot:run
```

**Windows (PowerShell):**
```powershell
$env:DATABASE_PASSWORD="your_password"
$env:SECRET="your_secret"
$env:SALT="your_salt"
mvn spring-boot:run
```

## 🔧 Environment Variables Reference

### Database Variables
- `DATABASE_PASSWORD`: Root password for MySQL (required)
- `DATABASE_NAME`: Database name (default: hyperativa_db)
- `DATABASE_USERNAME`: Database username (default: app_user)
- `MYSQL_PORT`: External MySQL port (default: 3306)
- `DATABASE_PORT`: Internal database port (default: 3306)

### API Variables
- `API_PORT`: External API port (default: 8080)
- `DATABASE_HOST`: Database hostname (default: hyperdb)

### Security Variables
- `SECRET`: JWT secret key for token generation (required)
- `SALT`: Password hashing salt (required)
- `ISSUER`: JWT token issuer (default: hyperativa-app)

---

## 🌐 Deployment for Different Environments

### Using Docker (Simplest Method)
For different environments, simply use different `.env` files:

**Linux/macOS/Windows (same commands):**
```bash
# For development
cp .env.development .env
docker-compose up -d --build

# For production
cp .env.production .env
docker-compose up -d --build
```

### Using Java/Maven
Use Spring profiles and different configuration files:

```bash
# Development profile
mvn spring-boot:run -Dspring-boot.run.profiles=development

# Production profile  
mvn spring-boot:run -Dspring-boot.run.profiles=production
```

---

## 💾 Persistent Data

### Docker Deployment
- MySQL data is stored in a Docker volume named `mysqldata`
- Data persists between container restarts
- Volume is automatically managed by Docker

### Managing Docker Volumes

**List volumes:**
```bash
docker volume ls
```

**Remove volume (reset database):**
```bash
# Linux/macOS/Windows - same command
docker-compose down -v
```

**Backup volume (Linux/macOS):**
```bash
docker run --rm -v hyperativa_mysqldata:/source -v $(pwd):/backup alpine tar czf /backup/mysql-backup.tar.gz /source
```

**Backup volume (Windows PowerShell):**
```powershell
docker run --rm -v hyperativa_mysqldata:/source -v ${PWD}:/backup alpine tar czf /backup/mysql-backup.tar.gz /source
```

### Java/Maven Deployment
- You are responsible for MySQL data persistence
- Configure backups and maintenance at the database server level
- Data is stored in your local MySQL installation

---

## 🐛 Troubleshooting

### Docker Issues
1. **Check .env file**: Ensure it exists and variables are set correctly
2. **Port conflicts**: Change ports in `.env` file if needed
3. **Variable not loading**: Restart Docker Compose after modifying `.env`
4. **View logs**: `docker-compose logs hyperapi`

### Java/Maven Issues
1. **MySQL not running**: Start MySQL service first
2. **Connection refused**: Check MySQL is running on correct port
3. **Access denied**: Verify database user credentials
4. **Database not found**: Ensure database exists in MySQL

### Connection Testing

**Test MySQL connection (Linux/macOS):**
```bash
mysql -u app_user -p -h localhost hyperativa_db
```

**Test MySQL connection (Windows):**
```cmd
mysql -u app_user -p -h localhost hyperativa_db
```

**Test API health endpoint (all platforms):**
```bash
curl http://localhost:8080/health
```

**Windows alternative to curl (PowerShell):**
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/health" -UseBasicParsing
```

### Platform-Specific Notes

**Windows Users:**
- Use PowerShell for better compatibility with Linux/macOS commands
- Docker commands are the same across all platforms
- For file operations, PowerShell's `Copy-Item` is recommended over `copy`

**Linux/macOS Users:**
- Standard bash commands work as expected
- Environment variables use `export` syntax

**All Platforms:**
- Docker and Docker Compose commands are identical across platforms
- Maven commands (`mvn`) work the same way

---

## 📋 Recommendation

- **Use Docker**: Best for most users - includes automatic database setup, consistent across all platforms
- **Use Java/Maven**: Only for developers who need to modify code and have MySQL expertise

**Key Difference**: With Docker, the database is included. With Java/Maven, you must set up MySQL separately.

**Platform Compatibility**: Docker provides the most consistent experience across Windows, macOS, and Linux.