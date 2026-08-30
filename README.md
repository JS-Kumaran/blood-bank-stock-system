# 🩸 Blood Bank Stock System - Backend API

A containerized REST API built with Spring Boot and MySQL for managing blood bank inventory, donors, patients, and blood requests.

---

## 📋 Overview

This is the **backend REST API** for a Blood Bank Management System. It provides CRUD operations for:
- Blood stock inventory
- Donor records
- Patient records  
- Blood requests from hospitals
- Transaction logging (IN/OUT)

**Frontend not included** - This is a backend-only service meant to be consumed by a separate frontend application.

---

## 🛠️ Tech Stack

| Technology | Version |
|------------|---------|
| Java | 21 LTS |
| Spring Boot | 3.x |
| MySQL | 8.0 |
| Docker | 24.x |
| Maven | 3.9+ |

---

## 🚀 Quick Start

```bash
# Clone the repository
git clone https://github.com/JS-Kumaran/blood-bank-stock-system.git
cd blood-bank-stock-system

# Build and start with Docker
docker compose up --build

# API runs at: http://localhost:8080
# MySQL runs at: localhost:3307
```

---

## 🐳 Docker Setup

The project includes two services:

| Service | Container | Port |
|---------|-----------|------|
| Spring Boot API | bloodbank-app | 8080 |
| MySQL Database | bloodbank-mysql | 3307 |

```bash
# Start services
docker compose up -d

# View logs
docker compose logs -f

# Stop services
docker compose down

# Reset everything (remove volumes)
docker compose down -v
```

---

## ⚙️ Configuration

Create a `.env` file:

```env
DB_PASSWORD=your_password
DB_URL=jdbc:mysql://mysql:3306/bloodbank?useSSL=false&allowPublicKeyRetrieval=true
DB_USERNAME=root
SPRING_PROFILES_ACTIVE=docker
```

---

## 📊 API Endpoints

Base URL: `http://localhost:8080/api`

| Resource | Endpoints |
|----------|-----------|
| **Blood Stock** | `GET/POST /stocks`, `GET/PUT/DELETE /stocks/{id}` |
| **Donors** | `GET/POST /donors`, `GET/PUT/DELETE /donors/{id}` |
| **Patients** | `GET/POST /patients`, `GET/PUT/DELETE /patients/{id}` |
| **Requests** | `GET/POST /requests`, `PUT /requests/{id}/status` |
| **Transactions** | `GET/POST /transactions`, `GET /transactions/date` |

### Example Request

```bash
# Get all blood stocks
curl http://localhost:8080/api/stocks

# Add new blood stock
curl -X POST http://localhost:8080/api/stocks \
  -H "Content-Type: application/json" \
  -d '{"bloodType":"A_POSITIVE","quantity":10,"expiryDate":"2024-12-31"}'
```

---

## 💻 Development

```bash
# Build without Docker
mvn clean package

# Run locally
mvn spring-boot:run

# Run tests
mvn test
```

---

## 🔧 Troubleshooting

| Issue | Solution |
|-------|----------|
| Port 8080 in use | `netstat -ano \| findstr :8080` then kill the process |
| MySQL connection | Check MySQL is healthy: `docker compose exec mysql mysqladmin ping` |
| Build fails | Clear cache: `docker system prune -a` |

---

## 📁 Project Structure

```
blood-bank-stock-system/
├── src/
│   ├── main/java/com/bloodbank/
│   │   ├── controller/   # REST endpoints
│   │   ├── service/      # Business logic
│   │   ├── repository/   # Database access
│   │   └── model/        # Entities
│   └── resources/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## 🤝 Contributing

1. Fork the repo
2. Create a branch: `git checkout -b feature/your-feature`
3. Commit changes: `git commit -m 'Add feature'`
4. Push: `git push origin feature/your-feature`
5. Open a Pull Request

---

## 📄 License

MIT License - feel free to use this project for any purpose.

---

## 🙏 Acknowledgments

- Spring Boot for the REST API framework
- Docker for containerization
- MySQL for database

---

**Made by JS-Kumaran**

---