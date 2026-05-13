# TalentAtlas Frontend

This is the Angular frontend for TalentAtlas, a full-stack job application tracking application built to help users organize applications, manage skills, and analyze job descriptions for keyword relevance.

The frontend provides the user-facing experience for authentication, application management, skill tracking, dashboard navigation, and keyword analysis. It is designed to work with the TalentAtlas Spring Boot backend included in the root of this repository.

## Features

- User registration and login
- Protected routes using an authentication guard
- JWT-based request handling with an auth interceptor
- Dashboard and app navigation
- Job application creation, editing, and detail views
- Skill management workflows
- Keyword analysis for job descriptions
- Landing page and account-related flows

## Tech Stack

- Angular 21
- TypeScript
- Angular Router
- Angular Forms
- RxJS
- Vitest

## Project Structure

The frontend lives in `talentatlas-frontend/`.

Key areas include:

- `src/app/core/` for shared logic such as auth, guards, interceptors, environment configuration, analysis services, and skills services
- `src/app/features/` for feature components such as dashboard, applications, application detail, login, register, skills, and keyword analysis
- `public/` for static assets such as the keyword dictionary and favicon

## Running Locally

From the `talentatlas-frontend` directory, install dependencies:

```bash
npm install
```

Start the Angular development server:

```bash
npm start
```

The frontend will run at:

```text
http://localhost:4200/
```

## Backend Integration

This frontend is configured to communicate with the TalentAtlas backend through the API URL defined in:

```text
src/app/core/environment.ts
```

The current local API base URL is:

```text
http://localhost:8080/api
```

For full functionality, start the Spring Boot backend from the repository root before using the frontend.

## Build

To create a production build:

```bash
npm run build
```

Build artifacts are generated in the `dist/` directory.

## Testing

To run unit tests:

```bash
npm test
```

This project uses Angular’s unit test builder with Vitest.

## Notes



