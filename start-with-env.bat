@echo off
REM This script sets environment variables and starts the backend
REM Copy your actual values from .env file

set DB_HOST=your-db-host
set DB_PORT=your-db-port
set DB_NAME=your-db-name
set DB_USERNAME=your-db-username
set DB_PASSWORD=your-db-password
set SENDGRID_API_KEY=your-sendgrid-api-key
set SENDGRID_FROM_EMAIL=your-sendgrid-email
set SENDGRID_FROM_NAME=your-sendgrid-name

echo Starting Smart Recipe Generator Backend...
mvn spring-boot:run
