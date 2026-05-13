# TalentAtlas

TalentAtlas is a full-stack web application for organizing a job search in one place. The name reflects two ideas behind the product: an **atlas** helps map complex information, and Atlas as a symbol represents strength and endurance under weight. In that spirit, TalentAtlas helps users make sense of their skills, roles, and opportunities while carrying more of the organizational burden of the job search for them.

## Features

- User registration and authentication
- Dashboard with application status summaries
- Create, view, edit, and delete job applications
- Track company, role, contact information, notes, and application dates
- Manage a personal skills inventory with category and proficiency level
- Analyze job descriptions against saved skills to identify matched and missing keywords
- Highlight active applications and surface status-based attention indicators

## Tech Stack

### Frontend
- Angular
- TypeScript
- HTML
- CSS

### Backend
- Java
- Spring Boot
- Spring Security
- Spring Data JPA

### Database
- MySQL

### Testing
- JUnit
- H2 (in-memory test database)

## Architecture

The application is split into two main parts:

- `talentatlas-frontend/` - Angular client application
- `src/` - Spring Boot backend API

The frontend communicates with the backend through REST endpoints for authentication, applications, skills, dashboard data, and keyword analysis.

## Core Functionality

TalentAtlas is designed to support job seekers by combining application tracking, skills management, and keyword analysis in a single workflow. On the mapping side of the metaphor, the application helps users see where they stand by organizing their saved skills, application history, and job-description keywords into a clearer picture.

On the load-bearing side of the metaphor, TalentAtlas is meant to reduce the mental overhead of job searching by centralizing the details that are often scattered across spreadsheets, notes, saved postings, and inboxes. Instead of forcing users to build their own tracking system, the application gives them one place to manage progress, identify gaps, and keep momentum.

### Application Tracking
Users can manage job applications from a central dashboard, including company name, position title, status, contact details, notes, and application dates.

### Skill Management
Users can maintain a list of technical and soft skills, organize them by category, and assign proficiency levels.

### Keyword Analysis
Users can paste a job description and compare it against their saved skills to identify matched and missing keywords. This helps surface skill gaps and improve resume tailoring for specific roles.

### Dashboard Reporting
The dashboard summarizes application progress across stages such as saved, applied, screening, interview, offer, accepted, rejected, and withdrawn.

## Running Locally

### Prerequisites
- Node.js and npm
- Java 21
- Maven
- MySQL

### Backend Setup

The backend uses environment-variable placeholders for configuration rather than hardcoded secrets.

Set the following environment variables before starting the backend:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `FRONTEND_URL` (optional, defaults to `http://localhost:4200`)

Start the backend from the project root:

```bash
mvn spring-boot:run
```

### Frontend Setup

From the frontend directory:

```bash
cd talentatlas-frontend
npm install
ng serve
```

Then open:

```text
http://localhost:4200
```

## Testing

The test configuration uses an H2 in-memory database for local and automated test runs, which keeps tests isolated from production data.

Run backend tests with:

```bash
mvn test
```

Frontend tests can be run from the frontend directory with the Angular test command configured in the project.

## Configuration Notes

- Sensitive backend values are externalized through environment variables instead of being stored directly in `application.properties`.
- The repository ignores the private JWT key file through `.gitignore`, so private signing material is not intended to be version-controlled.
- Build output, IDE metadata, and local dependency directories are also excluded from version control through ignore rules.

## Future Improvements

- Resume upload and parsing
- Interview scheduling and tracking
- Analytics and trend visualizations
- Follow-up reminders
- Exportable reports for applications and skills
- Improved mobile responsiveness

## Purpose

TalentAtlas is being developed as a full-stack application focused on making job searching more understandable and more manageable. The long-term goal is to give users a clearer map of their qualifications and opportunities while also carrying more of the organizational weight that usually makes the process exhausting.

This project demonstrates frontend development, backend API design, authentication, persistence, testing, and integration across a multi-part application stack.

## License

MIT License - see [LICENSE](LICENSE) file for details.

Built by Michael Hosler, 2026.
