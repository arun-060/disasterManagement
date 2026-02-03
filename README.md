# Crowdsourced Disaster Relief and Coordination Platform

A comprehensive platform for coordinating disaster relief efforts by connecting donors, volunteers, and affected people.

## Overview

This platform serves as a centralized system for disaster relief coordination, allowing:

- **Donors** to contribute resources and funds
- **Volunteers** to offer their time and skills
- **Affected People** to request assistance
- **Administrators** to manage the overall system

## Recent Updates (July 2025)

- Enhanced security configurations
- Removed hardcoded API keys and secrets
- Improved authentication and authorization
- Updated volunteer dashboard with better task management
- Added secure payment processing

## Features

- **User Authentication**: Secure JWT-based authentication system
- **Role-Based Access Control**: Different dashboards and permissions for each user role
- **Real-Time Updates**: Information about ongoing relief efforts
- **Resource Matching**: Connect resources with needs efficiently
- **Donation Tracking**: Monitor contributions and their impact
- **Request Management**: Track and fulfill assistance requests

## Technology Stack

- **Backend**: Java with Spring Boot
- **Security**: Spring Security with JWT
- **Database**: H2 Database (embedded)
- **Frontend**: HTML, CSS, JavaScript with Thymeleaf templating
- **Build Tool**: Gradle 8.12.1

## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle 8.12.1 or higher
- Environment variables for sensitive data

### Security Configuration

1. Create the following environment variables:
   ```
   RAZORPAY_API_KEY=your_api_key_here
   RAZORPAY_API_SECRET=your_api_secret_here
   JWT_SECRET=your_jwt_secret_here
   ```

2. Update `application.properties`:
   ```properties
   razorpay.api.key=${RAZORPAY_API_KEY}
   razorpay.api.secret=${RAZORPAY_API_SECRET}
   app.jwt.secret=${JWT_SECRET}
   ```

### Installation

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/disaster-management.git
   cd disaster-management
   ```

2. Build the project:
   ```
   ./gradlew build
   ```

3. Run the application:
   ```
   ./gradlew bootRun
   ```

4. Access the application at `http://localhost:8080`

5. Access the H2 Database Console at `http://localhost:8080/h2-console` with these settings:
   - JDBC URL: `jdbc:h2:file:./data/disaster_management`
   - Username: `sa`
   - Password: `password`

### Default Users

The system comes with pre-configured users for testing:

- **Admin**: Username: `admin`, Password: `admin123`
- **Donor**: Username: `donor`, Password: `donor123`
- **Volunteer**: Username: `volunteer`, Password: `volunteer123`
- **Affected Person**: Username: `affected`, Password: `affected123`

## Project Structure

```
src/main/java/com/example/disasterManagement/
├── config/                  # Configuration classes
├── controller/              # REST and web controllers
├── model/                   # Entity classes
├── payload/                 # Request/Response DTOs
├── repository/              # Data access interfaces
├── security/                # Security configuration
│   ├── jwt/                 # JWT utilities
│   └── services/            # Security services
└── DisasterManagementApplication.java  # Main application class

src/main/resources/
├── static/                  # Static resources (CSS, JS)
├── templates/               # Thymeleaf templates
└── application.properties   # Application configuration
```

## API Endpoints

### Authentication
- `POST /api/auth/signin` - User login
- `POST /api/auth/signup` - User registration

### User Dashboards
- `/dashboard` - Main dashboard (redirects based on role)
- `/admin/dashboard` - Admin dashboard
- `/donor/dashboard` - Donor dashboard
- `/volunteer/dashboard` - Volunteer dashboard
- `/affected/dashboard` - Affected person dashboard

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Security Notice

- Never commit sensitive data (API keys, secrets, credentials)
- Always use environment variables or secure vaults
- Keep your environment variables secure
- Regularly update dependencies
- Monitor security advisories

## Acknowledgments

- Spring Boot and Spring Security teams
- Bootstrap team
- All contributors to this project 