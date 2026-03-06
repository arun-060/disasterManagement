# Docker Deployment Guide

## Dockerization Overview

This application has been prepared for containerized deployment on Render or similar container platforms.

## Files Created

1. **Dockerfile** - Multi-stage build configuration for efficient image creation
2. **.dockerignore** - Excludes unnecessary files from Docker build context
3. **application-prod.properties** - Production configuration with environment variables
4. **render.yaml** - Render-specific deployment configuration
5. **docker-compose.yml** - Local development and testing with Docker Compose

## Local Testing

### Using Docker Compose

1. **Build and run locally:**
   ```bash
   docker-compose up --build
   ```

2. **Access the application:**
   - Open http://localhost:8080 in your browser

3. **Stop the application:**
   ```bash
   docker-compose down
   ```

### Using Docker Commands Directly

1. **Build the Docker image:**
   ```bash
   docker build -t disaster-management:latest .
   ```

2. **Run the container:**
   ```bash
   docker run -p 8080:8080 \
     -e SPRING_PROFILES_ACTIVE=prod \
     -e JWT_SECRET=your_jwt_secret_here \
     -e DB_PASSWORD=your_db_password_here \
     -e RAZORPAY_KEY_ID=your_razorpay_key_id \
     -e RAZORPAY_KEY_SECRET=your_razorpay_key_secret \
     disaster-management:latest
   ```

## Deployment on Render

### Prerequisites

- Render account (https://render.com)
- GitHub repository with this code
- Environment variables configured in Render dashboard

### Setup Steps

1. **Connect GitHub Repository:**
   - Go to Render Dashboard
   - Click "New +" → "Web Service"
   - Select "Build and deploy from a Git repository"
   - Connect your GitHub account and select the repository

2. **Configure Service:**
   - **Name:** disaster-management
   - **Runtime:** Docker
   - **Region:** Choose your preferred region
   - **Plan:** Free (or paid for production)

3. **Set Environment Variables:**
   In the Render dashboard, add these environment variables:
   - `JWT_SECRET` - Your JWT secret key
   - `DB_PASSWORD` - Database password for H2
   - `RAZORPAY_KEY_ID` - Razorpay API key ID
   - `RAZORPAY_KEY_SECRET` - Razorpay API secret key

4. **Deploy:**
   - Click "Create Web Service"
   - Render will automatically build and deploy your application

### Important Notes

- **Health Check:** The application responds to root path `/` for health checks
- **Database:** Uses H2 in-memory/file-based database (data persists in `/tmp/disaster_management`)
- **Port:** Application runs on port 8080
- **Storage:** Render's free tier has ephemeral storage. Consider upgrading for persistent data

## Render Free Tier Limitations

- **Memory:** 512 MB
- **Storage:** Ephemeral (resets on redeploy)
- **CPU:** Shared
- **Timeout:** 30-second deploy timeout

### For Production Upgrade

For a more robust deployment, consider:
- Upgrading to Render's Standard plan ($7/month)
- Using PostgreSQL instead of H2
- Adding cron jobs for backups
- Monitoring and alerting

## Monitoring

After deployment, monitor your application:

1. **Logs:** Check Render dashboard for application logs
2. **Health Check:** Render automatically monitors the `/` endpoint
3. **Performance:** Use Render's metrics dashboard

## Troubleshooting

### Build Fails

- Check that `build.gradle` is valid
- Ensure Java 17 compatibility in your code
- Check for circular dependencies

### Application Won't Start

- Verify environment variables are set correctly
- Check logs in Render dashboard
- Ensure database directory permissions

### High Memory Usage

- The free tier (512 MB) may be tight
- Consider upgrading plan
- Monitor with `docker stats` locally

## Local Development vs Production

**Development (application.properties):**
- H2 console enabled
- SQL queries logged
- Detailed debug logs

**Production (application-prod.properties):**
- H2 console disabled
- SQL queries not logged
- Minimal logging for performance

## Security Considerations

1. Change `JWT_SECRET` to a strong, random value
2. Use HTTPS in production (Render provides automatic SSL)
3. Secure environment variables in Render dashboard
4. Consider adding database encryption
5. Regular security audits

## Next Steps

1. Test locally with `docker-compose up`
2. Commit all Docker files to your repository
3. Push to GitHub
4. Connect repository to Render
5. Configure environment variables
6. Deploy and monitor

For more information:
- Render Documentation: https://render.com/docs
- Docker Documentation: https://docs.docker.com
- Spring Boot Containerization: https://spring.io/guides/topicals/spring-boot-docker
