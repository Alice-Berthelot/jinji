<p align="center">
  <img src="frontend/public/logos/jinji_logo.svg" alt="logo street art hunter" width="10%"/>
</p>

<h1 align="center"><strong>Jinji</strong></h1>

## Introduction
Jinji is a multi-company HRIS application specializing in leave management. I am currently developing this project in preparation for my upcoming exam for the professional certification “Application Designer and Developer”.

Main technologies used :

- Next.js (ReactJS / TypeScript framework)
- Spring Boot (Java framework)
- PostgreSQL

3 user profile types: employee, manager, HR

## Main Stack
- Frontend: Next.js 15, TypeScript, TailwindCSS
- Backend: Spring Boot 3, Java 21, Flyway
- DB: PostgreSQL

## Prerequisites to use locally
- Node.js >= 18
- Java 21
- PostgreSQL (create a database and user with privileges, and use them as env variables in the run script)

## Run locally

Clone from GitHub.

Frontend:
```bash
npm install
npm run start
```

Backend:
Run Main class in IntelliJ, with the following environment variables: 
- INITIAL_HR_PASSWORD
- INITIAL_HR_USERNAME
- SPRING_DATASOURCE_PASSWORD
- SPRING_DATASOURCE_URL
- SPRING_DATASOURCE_USERNAME

## Docker
```bash
docker compose up --build
```

## Monorepo Structure
- /frontend
- /backend
