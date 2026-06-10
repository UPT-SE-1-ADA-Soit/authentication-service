# Authentication Service

Spring Boot microservice for user registration, authentication, and profile management. Issues and validates JWT tokens used by other services.

## Tech Stack

- **Java 21** / Spring Boot 4.0.6
- **Spring Security** with stateless JWT (JJWT 0.12.6)
- **PostgreSQL** (Supabase) via Spring Data JPA
- **BCrypt** password hashing
- **Gradle** build tool

## API Endpoints

Base path: `/auth`

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/auth/register` | No | Register a new user, returns JWT |
| POST | `/auth/login` | No | Authenticate user, returns JWT |
| GET | `/auth/validate` | Yes | Validate JWT and return user info |
| PUT | `/auth/profile` | Yes | Update name, location, or avatar |
| GET | `/auth/user/{userId}` | No | Get public profile of a user |

### Request / Response Examples

**POST /auth/register**
```json
// Request
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "secret123",
  "phone": "+49123456789"
}

// Response 201
{
  "token": "<jwt>",
  "userId": 1,
  "email": "jane@example.com",
  "name": "Jane Doe",
  "location": null,
  "avatarUrl": null
}
```

**POST /auth/login**
```json
// Request
{
  "email": "jane@example.com",
  "password": "secret123"
}

// Response 200 — same shape as register
```

**GET /auth/validate** — requires `Authorization: Bearer <token>`
```json
// Response 200
{
  "userId": 1,
  "email": "jane@example.com",
  "name": "Jane Doe",
  "location": "Berlin",
  "avatarUrl": "https://..."
}
```

**PUT /auth/profile** — requires `Authorization: Bearer <token>`
```json
// Request (all fields optional)
{
  "name": "Jane Smith",
  "location": "Hamburg",
  "avatarUrl": "https://..."
}
```

**GET /auth/user/{userId}**
```json
// Response 200
{
  "userId": 1,
  "name": "Jane Doe",
  "location": "Berlin",
  "avatarUrl": "https://..."
}
```

## Error Responses

| Status | Cause |
|--------|-------|
| 400 | Validation failure (missing or malformed fields) |
| 401 | Invalid or expired JWT |
| 409 | Email already registered |

## Configuration

| Variable | Default | Description |
|----------|---------|-------------|
| `AUTH_PORT` | `8081` | Server port |
| `DB_URL` | Supabase pooler URL | JDBC connection string |
| `DB_USER` | `postgres.*` | Database username |
| `DB_PASSWORD` | — | Database password |
| `DDL_AUTO` | `update` | Hibernate DDL strategy |
| `JWT_SECRET` | — | HMAC-SHA signing key **(set in production)** |
| `JWT_EXPIRY_DAYS` | `30` | Token lifetime in days |

Copy `.env.example` to `.env` and set `JWT_SECRET` before running.

## Running Locally

```bash
# Build
./gradlew bootJar

# Run (with env vars set)
java -jar build/libs/authentication-service-*.jar
```

Service starts on port **8081** by default.

## Docker

```bash
docker build -t authentication-service .
docker run -p 8081:8081 \
  -e JWT_SECRET=your-secret \
  -e DB_PASSWORD=your-password \
  authentication-service
```

## Security Notes

- Sessions are stateless; tokens must be included on every protected request.
- CORS is open to all origins — restrict in production.
- Use a strong random value for `JWT_SECRET` in production; the default is insecure.
