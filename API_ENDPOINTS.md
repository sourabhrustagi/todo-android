# API Endpoints Documentation

## Base URL
`https://api.todoapp.com/v1`

## Authentication

### POST /auth/login
**Send OTP to phone number**

**Request:**
```json
{
  "phoneNumber": "+1234567890"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "message": "OTP sent successfully",
    "expiresIn": 300
  }
}
```

### POST /auth/verify-otp
**Verify OTP and get access token**

**Request:**
```json
{
  "phoneNumber": "+1234567890",
  "otp": "123456"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "token": "access_token_123",
    "refreshToken": "refresh_token_123",
    "expiresIn": 3600,
    "user": {
      "id": "user_123",
      "phoneNumber": "+1234567890",
      "name": "John Doe"
    }
  }
}
```

### POST /auth/logout
**Logout user**

**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
{
  "success": true,
  "message": "Logged out successfully"
}
```

## Tasks

### GET /tasks
**Get all tasks**

**Headers:** `Authorization: Bearer {token}`

**Query Parameters:**
- `page` (optional): Page number (default: 1)
- `limit` (optional): Items per page (default: 20)
- `priority` (optional): Filter by priority (high, medium, low)
- `completed` (optional): Filter by status (true, false)
- `search` (optional): Search in title and description
- `sortBy` (optional): Sort field (title, priority, dueDate, createdAt)
- `sortOrder` (optional): Sort order (asc, desc)

**Response:**
```json
{
  "success": true,
  "data": {
    "tasks": [
      {
        "id": "task_123",
        "title": "Complete project documentation",
        "description": "Write comprehensive documentation",
        "priority": "high",
        "category": {
          "id": "cat_1",
          "name": "Work",
          "color": "#FF5722"
        },
        "dueDate": "2024-01-20T23:59:59Z",
        "completed": false,
        "createdAt": "2024-01-15T10:30:00Z",
        "updatedAt": "2024-01-15T10:30:00Z"
      }
    ],
    "pagination": {
      "page": 1,
      "limit": 20,
      "total": 1,
      "totalPages": 1
    }
  }
}
```

### POST /tasks
**Create new task**

**Headers:** `Authorization: Bearer {token}`

**Request:**
```json
{
  "title": "Complete project documentation",
  "description": "Write comprehensive documentation",
  "priority": "high",
  "categoryId": "cat_1",
  "dueDate": "2024-01-20T23:59:59Z"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": "task_123",
    "title": "Complete project documentation",
    "description": "Write comprehensive documentation",
    "priority": "high",
    "category": {
      "id": "cat_1",
      "name": "Work",
      "color": "#FF5722"
    },
    "dueDate": "2024-01-20T23:59:59Z",
    "completed": false,
    "createdAt": "2024-01-15T10:30:00Z",
    "updatedAt": "2024-01-15T10:30:00Z"
  }
}
```

### GET /tasks/{taskId}
**Get specific task**

**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
{
  "success": true,
  "data": {
    "id": "task_123",
    "title": "Complete project documentation",
    "description": "Write comprehensive documentation",
    "priority": "high",
    "category": {
      "id": "cat_1",
      "name": "Work",
      "color": "#FF5722"
    },
    "dueDate": "2024-01-20T23:59:59Z",
    "completed": false,
    "createdAt": "2024-01-15T10:30:00Z",
    "updatedAt": "2024-01-15T10:30:00Z"
  }
}
```

### PUT /tasks/{taskId}
**Update task**

**Headers:** `Authorization: Bearer {token}`

**Request:**
```json
{
  "title": "Updated task title",
  "description": "Updated description",
  "priority": "medium",
  "categoryId": "cat_2",
  "dueDate": "2024-01-25T23:59:59Z",
  "completed": true
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": "task_123",
    "title": "Updated task title",
    "description": "Updated description",
    "priority": "medium",
    "category": {
      "id": "cat_2",
      "name": "Personal",
      "color": "#4CAF50"
    },
    "dueDate": "2024-01-25T23:59:59Z",
    "completed": true,
    "createdAt": "2024-01-15T10:30:00Z",
    "updatedAt": "2024-01-15T11:45:00Z"
  }
}
```

### DELETE /tasks/{taskId}
**Delete task**

**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
{
  "success": true,
  "message": "Task deleted successfully"
}
```

### PATCH /tasks/{taskId}/complete
**Mark task as completed**

**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
{
  "success": true,
  "data": {
    "id": "task_123",
    "title": "Complete project documentation",
    "description": "Write comprehensive documentation",
    "priority": "high",
    "category": {
      "id": "cat_1",
      "name": "Work",
      "color": "#FF5722"
    },
    "dueDate": "2024-01-20T23:59:59Z",
    "completed": true,
    "createdAt": "2024-01-15T10:30:00Z",
    "updatedAt": "2024-01-15T11:45:00Z"
  }
}
```

## Categories

### GET /categories
**Get all categories**

**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": "cat_1",
      "name": "Work",
      "color": "#FF5722",
      "createdAt": "2024-01-15T10:30:00Z"
    },
    {
      "id": "cat_2",
      "name": "Personal",
      "color": "#4CAF50",
      "createdAt": "2024-01-15T10:30:00Z"
    }
  ]
}
```

### POST /categories
**Create new category**

**Headers:** `Authorization: Bearer {token}`

**Request:**
```json
{
  "name": "Work",
  "color": "#FF5722"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": "cat_1",
    "name": "Work",
    "color": "#FF5722",
    "createdAt": "2024-01-15T10:30:00Z"
  }
}
```

### PUT /categories/{categoryId}
**Update category**

**Headers:** `Authorization: Bearer {token}`

**Request:**
```json
{
  "name": "Updated Work",
  "color": "#FF9800"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": "cat_1",
    "name": "Updated Work",
    "color": "#FF9800",
    "createdAt": "2024-01-15T10:30:00Z",
    "updatedAt": "2024-01-15T11:45:00Z"
  }
}
```

### DELETE /categories/{categoryId}
**Delete category**

**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
{
  "success": true,
  "message": "Category deleted successfully"
}
```

## Bulk Operations

### POST /tasks/bulk
**Bulk operations on tasks**

**Headers:** `Authorization: Bearer {token}`

**Request:**
```json
{
  "operation": "complete",
  "taskIds": ["task_123", "task_124", "task_125"]
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "updatedCount": 3,
    "message": "Successfully completed 3 tasks"
  }
}
```

## Search

### GET /tasks/search
**Search tasks**

**Headers:** `Authorization: Bearer {token}`

**Query Parameters:**
- `q` (required): Search query
- `fields` (optional): Search fields (title, description, all)
- `fuzzy` (optional): Enable fuzzy search (true, false)

**Response:**
```json
{
  "success": true,
  "data": {
    "tasks": [
      {
        "id": "task_123",
        "title": "Complete project documentation",
        "description": "Write comprehensive documentation",
        "priority": "high",
        "category": {
          "id": "cat_1",
          "name": "Work",
          "color": "#FF5722"
        },
        "dueDate": "2024-01-20T23:59:59Z",
        "completed": false,
        "createdAt": "2024-01-15T10:30:00Z",
        "updatedAt": "2024-01-15T10:30:00Z"
      }
    ],
    "total": 1
  }
}
```

## Analytics

### GET /tasks/analytics
**Get task analytics**

**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
{
  "success": true,
  "data": {
    "total": 25,
    "completed": 15,
    "pending": 10,
    "overdue": 3,
    "byPriority": {
      "high": 8,
      "medium": 12,
      "low": 5
    },
    "byCategory": [
      {
        "category": "Work",
        "count": 15
      },
      {
        "category": "Personal",
        "count": 10
      }
    ],
    "completionRate": 60.0
  }
}
```

## Feedback

### POST /feedback
**Submit feedback**

**Headers:** `Authorization: Bearer {token}`

**Request:**
```json
{
  "rating": 5,
  "comment": "Great app! Very user-friendly interface.",
  "category": "general"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": "feedback_123",
    "rating": 5,
    "comment": "Great app! Very user-friendly interface.",
    "category": "general",
    "createdAt": "2024-01-15T10:30:00Z"
  }
}
```

### GET /feedback
**Get feedback history**

**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": "feedback_123",
      "rating": 5,
      "comment": "Great app! Very user-friendly interface.",
      "category": "general",
      "createdAt": "2024-01-15T10:30:00Z"
    }
  ]
}
```

## Error Responses

### Validation Error (422)
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Invalid request data",
    "details": {
      "title": "Title is required"
    }
  }
}
```

### Unauthorized (401)
```json
{
  "success": false,
  "error": {
    "code": "UNAUTHORIZED",
    "message": "Invalid or missing authentication token"
  }
}
```

### Not Found (404)
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "Resource not found"
  }
}
```

### Server Error (500)
```json
{
  "success": false,
  "error": {
    "code": "INTERNAL_SERVER_ERROR",
    "message": "An unexpected error occurred"
  }
}
``` 