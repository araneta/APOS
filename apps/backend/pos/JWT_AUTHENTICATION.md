# JWT Authentication Implementation

## Overview

This application uses JWT (JSON Web Token) authentication with a filter-based approach. The authentication is handled entirely by the `JwtAuthenticationFilter` in the security filter chain, eliminating the need for manual token validation in individual controllers.

## Architecture

### 1. JWT Authentication Filter (`JwtAuthenticationFilter`)
- **Location**: `src/main/java/com/example/pos/config/JwtAuthenticationFilter.java`
- **Purpose**: Validates JWT tokens from the `Authorization` header
- **Process**:
  1. Extracts JWT token from `Authorization: Bearer <token>` header
  2. Validates token format and extracts username
  3. Loads user details from database
  4. Checks token expiration
  5. Sets up Spring Security authentication context

### 2. Security Configuration (`SecurityConfig`)
- **Location**: `src/main/java/com/example/pos/config/SecurityConfig.java`
- **Purpose**: Configures security rules and filter chain
- **Key Features**:
  - Stateless session management
  - JWT filter integration
  - Public endpoints for auth and documentation
  - Authentication required for all other endpoints

### 3. JWT Token Utility (`JwtTokenUtil`)
- **Location**: `src/main/java/com/example/pos/config/JwtTokenUtil.java`
- **Purpose**: Handles JWT token generation, validation, and extraction

## How It Works

### Authentication Flow

1. **Client Login**: POST to `/api/auth/login` with credentials
2. **Token Generation**: Server validates credentials and returns JWT token
3. **API Requests**: Client includes `Authorization: Bearer <token>` header
4. **Filter Processing**: `JwtAuthenticationFilter` validates token on every request
5. **Access Control**: Spring Security enforces authentication requirements

### Security Rules

- **Public Endpoints** (no authentication required):
  - `/api/auth/**` - Authentication endpoints
  - `/v3/api-docs/**` - API documentation
  - `/swagger-ui/**` - Swagger UI
  - `/error` - Error pages

- **Protected Endpoints** (authentication required):
  - `/api/accounts/**` - Account management
  - All other API endpoints

## Usage Examples

### 1. Login to Get JWT Token

```bash
curl -X POST http://localhost:8888/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "your_username",
    "password": "your_password"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 2. Access Protected Endpoints

```bash
curl -X GET http://localhost:8888/api/accounts \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 3. Search Accounts with Parameters

```bash
curl -X GET "http://localhost:8888/api/accounts/search?page=1&pageSize=10&filter=test" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

## Error Handling

### Authentication Failures

- **401 Unauthorized**: Invalid or missing JWT token
- **403 Forbidden**: Valid token but insufficient permissions
- **Token Expired**: Automatic rejection with 401 response

### Common Issues

1. **Missing Authorization Header**: Returns 401
2. **Invalid Token Format**: Returns 401
3. **Expired Token**: Returns 401
4. **User Not Found**: Returns 401

## Benefits of Filter-Based Approach

1. **Centralized Authentication**: All JWT validation in one place
2. **Clean Controllers**: No authentication code in business logic
3. **Consistent Security**: Same authentication for all endpoints
4. **Easy Maintenance**: Single point of authentication logic
5. **Spring Security Integration**: Leverages built-in security features

## Configuration

### JWT Properties (`application.properties`)

```properties
# JWT Configuration
jwt.secret-key=your-secret-key-here
jwt.expiration-time=36000000  # 10 hours in milliseconds
```

### CORS Configuration

The application is configured to allow requests from `http://localhost:4200` (Angular frontend) with proper CORS headers including `Authorization`.

## Testing

Run the tests to verify authentication:

```bash
mvn test -Dtest=AccountControllerTest
```

The tests verify that:
- Authenticated requests return 200 OK
- Unauthenticated requests return 401 Unauthorized 