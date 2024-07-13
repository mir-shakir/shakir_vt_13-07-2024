# Spring Boot Authorization Service

This project implements a role and permission-based authorization system using Spring Boot.

## Features

- Manages permissions and roles stored in a database.
- Maps permissions to roles and users.
- Uses custom annotations on controllers to specify required permissions for API endpoints.
- Implements an interceptor to verify user permissions before accessing APIs.
- Uses JWT for authentication, including a login API to generate tokens for testing.

## Database Schema

- **PERMISSIONS**: Stores permission information.
- **USER_ROLES**: Stores role information.
- **PER_ROLE_MAP**: Maps permissions to roles.
- **USERS**: Stores user information.
- **USER_ROLES_MAP**: Maps users to roles.

## Custom Annotations

### @Permission Annotation

Defines required permissions on controller methods:

- **permissions**: An array specifying the required permissions.
- **type**: Logic type specifying whether all or any permissions are required (`All` or `Any`).

### LogicEnum

- **All**: User must have all specified permissions to access the API.
- **Any**: User must have at least one of the specified permissions to access the API.

## JWT Authentication

JWT (JSON Web Token) is used for authentication. The token is generated upon successful login and must be included in the `Authorization` header for protected endpoints.

### Login API

A login API is provided to generate JWT tokens for testing purposes. Users must provide their username and password to receive a token.

## Endpoints

### Authentication

- **POST /auth/login**: Authenticates a user and returns a JWT token.

### Book Management (Protected Endpoints)

- **GET /book/all**: Requires all specified permissions.
- **GET /book/any**: Requires any of the specified permissions.

## Usage

1. **Clone the Repository**: Clone the project repository to your local machine.
2. **Configure Database**: Set up your database connection details in `application.properties` and add some dummy data to the database.

### Accessing Protected Endpoints

1. **Obtain a JWT Token**:
   - Send a POST request to `/auth/login` with valid user credentials.
   - Receive a JWT token in the response.

2. **Include JWT Token in Requests**:
   - Include the token in the `Authorization` header as `Bearer <token>` when accessing protected endpoints.

### Example Workflow

1. **Login to Obtain Token**:
   - Send a POST request to `/auth/login` with username and password.
   - Receive a JWT token upon successful authentication.

2. **Access Protected Endpoints**:
   - Use the obtained JWT token to access endpoints such as `/book/all` or `/book/any`.
   - The interceptor will check the user's permissions before allowing access based on the `@Permission` annotation.

---

**Note**: This project is intended for demonstration of role and permission-based authorization and thus may ignore other standard practices.
