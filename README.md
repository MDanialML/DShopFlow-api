# ShopFlow API

Smart inventory and order management platform for small shops.
Built with Spring Boot, React, PostgreSQL, and JWT authentication.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21 + Spring Boot 3.4.3 |
| Database | PostgreSQL (Supabase in production) |
| Security | Spring Security + JWT (jjwt) |
| ORM | Spring Data JPA + Hibernate |
| Payments | Stripe (Safepay/JazzCash for Pakistan) |
| AI Search | Claude API |
| Frontend | React + Tailwind CSS |
| Deployment | Railway (API) + Vercel (React) |

---

## Architecture

Multi-tenant SaaS platform. Each shop's data is completely
isolated. JWT token carries shopId — every database query
is scoped to the authenticated shop automatically.
React → Spring Security → Controller → Service → Repository → PostgreSQL
---

## Modules Completed

- [x] Module 1 — Project setup, PostgreSQL, Git
- [x] Module 2 — Product entity, CRUD API, soft delete
- [x] Module 3 — Order system, transactions, DTOs
- [x] Module 4 — JWT auth, multi-tenancy, role-based access
- [ ] Module 5 — Stripe payments (in progress)
- [ ] Module 6 — Real-time WebSockets
- [ ] Module 7 — AI product search
- [ ] Module 8 — Analytics dashboard
- [ ] Module 9 — React frontend
- [ ] Module 10 — Deployment

---

## API Documentation

Full documentation for each API module:

| Module | Endpoints | Documentation |
|---|---|---|
| Authentication | POST /api/auth/register, POST /api/auth/login | [View Docs](src/main/docs/ShopFlow_Auth_API_Documentation.docx) |
| Products | GET/POST/PUT/DELETE /api/products | Coming soon |
| Orders | GET/POST/PATCH /api/orders | Coming soon |
| Payments | POST /api/payments/checkout | Coming soon |

---

## Setup

1. Clone the repository
```bash
   git clone git@github.com:MDanialML/shopflow-api.git
```

2. Copy the example config
```bash
   cp application.example.yml application.yml
```

3. Fill in your database credentials in application.yml

4. Run the application
```bash
   mvn spring-boot:run
```

---

## Development Journey

Building this project in public on LinkedIn.
Every module documented. Every mistake shared.

[Follow the journey on LinkedIn](www.linkedin.com/in/the-muhammad-danial

)

---

## Key Design Decisions

**Multi-tenancy** — shop_id on every entity. Every query
filtered by shopId extracted from JWT. No shop sees
another shop's data.

**Soft delete** — products are never hard deleted.
isActive = false. Order history stays intact forever.

**Transactional orders** — @Transactional on createOrder.
Stock validation before deduction. All or nothing.

**Price snapshots** — unitPrice stored at time of purchase.
Price changes never affect historical orders.