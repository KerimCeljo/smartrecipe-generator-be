# 🍳 Smart Recipe Generator - Backend

A Spring Boot backend service that generates personalized recipes based on user preferences, ingredients, and cooking constraints.

## 🚀 Features

- **Recipe Generation API**: Generate recipes based on ingredients and preferences
- **Flexible Input**: Support for meal type, cuisine, cooking time, and complexity
- **CORS Enabled**: Ready to work with React frontend
- **H2 Database**: In-memory database for development
- **Actuator Endpoints**: Health checks and monitoring
- **Validation**: Input validation and error handling
- **Logging**: Comprehensive logging for debugging

## 🏗️ Architecture

```
src/main/java/com/recipe/smartrecipe/
├── SmartRecipeApplication.java    # Main Spring Boot application
├── controller/
│   └── RecipeController.java      # REST API endpoints
├── service/
│   └── RecipeService.java         # Business logic
├── dto/
│   ├── RecipeRequest.java         # Request data transfer object
│   └── RecipeResponse.java        # Response data transfer object
└── config/
    └── CorsConfig.java            # CORS configuration
```

## 🛠️ Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- IntelliJ IDEA (recommended)

## 📦 Installation & Setup

### 1. Clone and Navigate
```bash
cd backend
```

### 2. Import to IntelliJ
1. Open IntelliJ IDEA
2. Select "Open" or "Import Project"
3. Navigate to the `backend` folder
4. Select "Import project from external model" → Maven
5. Click "Next" and "Finish"

### 3. Wait for Maven Sync
- IntelliJ will automatically download dependencies
- Wait for the Maven sync to complete

## 🚀 Running the Application

### Option 1: Run from IntelliJ
1. Find `SmartRecipeApplication.java` in the Project Explorer
2. Right-click on it
3. Select "Run 'SmartRecipeApplication.main()'"

### Option 2: Run from Terminal
```bash
mvn spring-boot:run
```

### Option 3: Build and Run
```bash
mvn clean compile
mvn spring-boot:run
```

## 🌐 API Endpoints

### Generate Recipe
```
POST /api/recipes/generate
```

**Headers:**
```
Content-Type: application/json
X-USER-ID: {user_id}
```

**Request Body:**
```json
{
  "ingredients": "tomato, potato, onion",
  "mealType": "LUNCH",
  "cuisine": "ITALIAN",
  "cookingTime": "MIN_30_60",
  "complexity": "BEGINNER"
}
```

**Response:**
```json
{
  "content": "Generated recipe text...",
  "status": "success",
  "message": "Recipe generated successfully"
}
```

### Health Check
```
GET /api/recipes/health
```

### Actuator Endpoints
```
GET /actuator/health
GET /actuator/info
GET /actuator/metrics
```

## 🔧 Configuration

### Application Properties
- **Port**: 8080
- **Database**: H2 in-memory
- **CORS**: Enabled for localhost:5173
- **Logging**: DEBUG level for development

### Environment Variables
You can override these in `application.properties`:
```properties
server.port=8080
spring.datasource.url=jdbc:h2:mem:testdb
```

## 🧪 Testing

### Test the API
1. Start the backend
2. Use the frontend's "Backend Connection Test" component
3. Or test with curl:

```bash
curl -X POST http://localhost:8080/api/recipes/generate \
  -H "Content-Type: application/json" \
  -H "X-USER-ID: 1" \
  -d '{
    "ingredients": "chicken, rice",
    "mealType": "LUNCH",
    "cuisine": "AMERICAN",
    "cookingTime": "MIN_30_60",
    "complexity": "BEGINNER"
  }'
```

## 🐛 Troubleshooting

### Common Issues

| Issue | Solution |
|-------|----------|
| **Port 8080 already in use** | Change port in `application.properties` |
| **Maven dependencies not found** | Right-click project → Maven → Reload Project |
| **CORS errors** | Check CORS configuration and frontend URL |
| **Validation errors** | Check request body format and required fields |

### Logs
Check the console output for detailed logs. The application logs:
- API requests and responses
- Recipe generation process
- Error details
- Performance metrics

## 🔮 Future Enhancements

- [ ] AI integration for real recipe generation
- [ ] Database persistence for recipes
- [ ] User authentication and profiles
- [ ] Recipe ratings and reviews
- [ ] Email service integration
- [ ] Recipe categories and tags
- [ ] Advanced filtering and search
- [ ] Recipe bookmarks and favorites

## 📚 Dependencies

- **Spring Boot 3.2.0**: Core framework
- **Spring Web**: REST API support
- **Spring Data JPA**: Database operations
- **Spring Validation**: Input validation
- **Spring Actuator**: Monitoring and health checks
- **H2 Database**: In-memory database
- **Lombok**: Reduces boilerplate code
- **Jackson**: JSON processing

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

---

**Happy Cooking! 🍽️**
