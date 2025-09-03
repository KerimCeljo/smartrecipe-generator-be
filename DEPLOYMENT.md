# 🚀 Smart Recipe Generator Backend - Deployment Guide

## 📋 Prerequisites

- Docker installed on your system
- Render account (for cloud deployment)
- Aiven MySQL database
- SendGrid account with API key

## 🐳 Local Development with Docker

### 1. Build and run with Docker Compose
```bash
# Build and start the service
docker-compose up --build

# Run in background
docker-compose up -d --build

# View logs
docker-compose logs -f

# Stop the service
docker-compose down
```

### 2. Build and run with Docker directly
```bash
# Build the image
docker build -t smart-recipe-backend .

# Run the container
docker run -p 8080:8080 --env-file .env smart-recipe-backend
```

## ☁️ Deploy to Render

### 1. Prepare your repository
1. Push your code to GitHub
2. Make sure all environment variables are set in your `.env` file
3. Ensure `render.yaml` is in the root of your backend directory

### 2. Deploy on Render
1. Go to [Render Dashboard](https://dashboard.render.com)
2. Click "New +" → "Web Service"
3. Connect your GitHub repository
4. Select your backend repository
5. Configure the service:
   - **Name**: `smart-recipe-backend`
   - **Environment**: `Docker`
   - **Dockerfile Path**: `./Dockerfile`
   - **Docker Context**: `.`
   - **Plan**: `Starter` (or higher)

### 3. Set Environment Variables in Render
In the Render dashboard, go to your service → Environment tab and add:

```
DB_HOST=your-aiven-mysql-host
DB_PORT=your-aiven-mysql-port
DB_NAME=your-database-name
DB_USERNAME=your-username
DB_PASSWORD=your-password
SENDGRID_API_KEY=your-sendgrid-api-key
SENDGRID_FROM_EMAIL=your-verified-sender-email
SENDGRID_FROM_NAME=your-sender-name
SERVER_PORT=10000
SPRING_PROFILES_ACTIVE=production
CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com
```

### 4. Deploy
Click "Deploy" and wait for the build to complete.

## 🔧 Environment Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `DB_HOST` | Aiven MySQL host | `smart-recipe-mysql-stu-c8e8.e.aivencloud.com` |
| `DB_PORT` | Aiven MySQL port | `16309` |
| `DB_NAME` | Database name | `defaultdb` |
| `DB_USERNAME` | Database username | `avnadmin` |
| `DB_PASSWORD` | Database password | `AVNS_...` |
| `SENDGRID_API_KEY` | SendGrid API key | `SG....` |
| `SENDGRID_FROM_EMAIL` | Verified sender email | `your-email@domain.com` |
| `SENDGRID_FROM_NAME` | Sender name | `Recipe Generator` |
| `SERVER_PORT` | Server port (Render uses 10000) | `10000` |
| `CORS_ALLOWED_ORIGINS` | Allowed frontend origins | `https://your-app.onrender.com` |

## 🏥 Health Checks

The application includes health checks:
- **Local**: `http://localhost:8080/actuator/health`
- **Render**: `https://your-app.onrender.com/actuator/health`

## 📖 API Documentation

- **Local**: `http://localhost:8080/swagger-ui.html`
- **Render**: `https://your-app.onrender.com/swagger-ui.html`

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Check your Aiven MySQL credentials
   - Ensure the database is accessible from Render's IP ranges
   - Verify SSL settings

2. **SendGrid Email Not Working**
   - Verify your SendGrid API key
   - Ensure sender email is verified in SendGrid
   - Check SendGrid account limits

3. **CORS Issues**
   - Update `CORS_ALLOWED_ORIGINS` with your frontend URL
   - Ensure frontend URL is correct (no trailing slash)

4. **Build Failures**
   - Check Dockerfile syntax
   - Ensure all dependencies are in `pom.xml`
   - Verify Java version compatibility

### Logs
- **Local**: `docker-compose logs -f`
- **Render**: Check the "Logs" tab in Render dashboard

## 🔄 Updates and Redeployment

1. Make changes to your code
2. Commit and push to GitHub
3. Render will automatically redeploy (if auto-deploy is enabled)
4. Or manually trigger deployment from Render dashboard

## 📞 Support

If you encounter issues:
1. Check the logs first
2. Verify all environment variables are set correctly
3. Test locally with Docker first
4. Check Render's status page for service issues
