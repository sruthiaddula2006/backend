# SkillSwap

Skill-exchange web app: users list what they know and want to learn, get ranked matches, send exchange requests, and chat once a request is accepted.

```
skillswap/
├── backend/    Spring Boot 3 (Web, JPA, Security, Validation), PostgreSQL, JWT + BCrypt
├── frontend/   React 18 + TypeScript + Vite + React Router + Axios
└── docker-compose.yml   local PostgreSQL
```

## Run locally
Requires Java 17, Maven, Node 18+, Docker (or any PostgreSQL).

```bash
docker compose up -d                       # PostgreSQL on :5432

cd backend
cp .env.example .env                       # then export these vars in your shell / IDE run config
export JWT_SECRET=$(openssl rand -base64 48) SEED_DEMO=true
mvn spring-boot:run                        # API on :8080

cd ../frontend
cp .env.example .env
npm install && npm run dev                 # app on :5173
```
With `SEED_DEMO=true` you can log in as `demo@skillswap.com` / `demo123` (all demo users use `demo123`).

## Environment variables
| Backend | Purpose |
|---|---|
| `JWT_SECRET` | **Required**, 32+ chars. App won't start without it |
| `DB_URL`, `DB_USER`, `DB_PASSWORD` | JDBC URL and credentials (e.g. `jdbc:postgresql://HOST/DB?sslmode=require`) |
| `CORS_ORIGINS` | Comma-separated allowed frontend origins |
| `JWT_EXPIRY_MINUTES` | Default 1440 |
| `SEED_DEMO` | `true` inserts demo users into an empty DB. Keep `false` in production |
| `DDL_AUTO` | Default `update`; use Flyway/`validate` for stricter production control |

Frontend: `VITE_API_URL` – base URL of the backend (no trailing slash, no `/api`).

## API (all except `/api/auth/*` need `Authorization: Bearer <jwt>`)
| Method & path | Description |
|---|---|
| `POST /api/auth/register`, `/api/auth/login` | Returns `{token, user}` |
| `GET/PUT /api/users/me`, `GET /api/users/{id}` | Profile |
| `GET /api/matches?q=&skill=&location=&minScore=` | Ranked matches / search |
| `GET/POST /api/requests`, `PUT /api/requests/{id}/accept\|reject`, `DELETE /api/requests/{id}` | Exchange requests (accept creates an exchange) |
| `GET /api/exchanges`, `PUT /api/exchanges/{id}/complete` | Exchanges |
| `GET /api/messages/{userId}`, `POST /api/messages` | Chat (only between exchange partners) |
| `GET /api/notifications`, `PUT /api/notifications/read-all` | Notifications |

## Matching rules (`MatchService`)
Score out of 100, no ML: up to 70 for how much of each person's wanted list the other can teach, +15 if it works both ways, up to +6 for shared interests, +5 for same location.

## Deploy
1. **Database (Neon/Supabase/Railway):** create a Postgres DB; note host, db, user, password.
2. **Backend (Render/Railway):** deploy `backend/` using its `Dockerfile`. Set `DB_URL`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET`, `CORS_ORIGINS=https://<your-vercel-domain>`, `SEED_DEMO=false`.
3. **Frontend (Vercel):** root directory `frontend/`, framework Vite, env `VITE_API_URL=https://<your-backend-host>`. `vercel.json` handles SPA routing.
4. Add the Vercel URL to `CORS_ORIGINS` and redeploy the backend.
