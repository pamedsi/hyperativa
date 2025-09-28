# Hyperativa API - Usage Guide

## 📋 About the Application

This API was developed as a solution for the **Hyperativa Challenge**, focusing on secure credit card number registration and querying. The application allows:

- ✅ **Secure authentication** via JWT
- ✅ **Card registration** individually or in batch via file
- ✅ **Card querying** for existing numbers in the database
- ✅ **Secure storage** of sensitive data

## 🔌 API Documentation

### OpenAPI/Swagger Documentation

The complete API documentation is available in two formats in the `src/main/resources` folder:

- **`openapi.json`** (Recommended) - Standard JSON format
- **`openapi.yml`** - YAML alternative

**We recommend using the `.json` file** for better compatibility with tools like:
- Swagger UI
- Postman
- Insomnia
- API client automation

### 🔍 Live Swagger Documentation

You can access the interactive Swagger UI when the application is running:

**URL:** `http://localhost:8080/swagger-ui.html`

The Swagger UI provides:
- Interactive API documentation
- Direct endpoint testing from the browser
- Request/response examples
- Schema definitions

## 🔐 Authentication Flow

To use the API, follow this flow:

1. **Register user** → `POST /user`
2. **Authenticate** → `POST /authentication/login` (receives JWT token)
3. **Use protected endpoints** with the token in the `Authorization` header

**⚠️ Important:** JWT tokens expire after **15 minutes** for security reasons. You'll need to re-authenticate when the token expires.

## 📚 Available Endpoints

### 1. 🆕 User Registration
**POST** `/user`
```json
{
  "name": "John Smith",
  "email": "john@email.com",
  "password": "securePassword123",
  "birthdate": "15/05/1990"
}
```
**Date Format:** DD/MM/YYYY

### 2. 🔑 Authentication
**POST** `/authentication/login`
```json
{
  "login": "john@email.com",
  "password": "securePassword123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "role": "CUSTOMER"
}
```

### 3. ✅ Token Validation
**GET** `/authentication/validation`
**Header:** `Authorization: Bearer {token}`

**Response:**
```json
{
  "name": "John Smith",
  "role": "CUSTOMER"
}
```

### 4. 💳 Add Single Card
**POST** `/card/single`
**Header:** `Authorization: Bearer {token}`
```json
{
  "cardNumber": "1234567890123456"
}
```

### 5. 📁 Add Batch Cards
**POST** `/card/batch`
**Header:** `Authorization: Bearer {token}`
**Form Data:** `file` (.txt file with specific format)

### 6. 🔍 Query Card
**GET** `/card/{cardNumber}`
**Header:** `Authorization: Bearer {token}`
**Path:** `/card/1234567890123456`

**Response:**
```json
{
  "cardIdentifier": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

### 7. 🗑️ Delete User
**DELETE** `/user/{identifier}`
**Header:** `Authorization: Bearer {token}`
**Path:** `/user/a1b2c3d4-e5f6-7890-abcd-ef1234567890`

## 📁 Batch File Format

The batch file must follow this **specific fixed-length format**:

### Header Line (Line 1)
```
DESAFIO-HYPERATIVA           20180524LOTE000100007
```
- **Positions 0-29**: Name/Identifier (`DESAFIO-HYPERATIVA`)
- **Positions 29-37**: Date (`20180524`)
- **Positions 37-45**: Batch Number (`LOTE0001`)
- **Positions 45-51**: Record Count (`000007`)

### Card Lines (Lines 2 to n-1)
```
C1     4456897922969999
C4     4456897998199999
C5     4456897999999999124
```
- **Positions 0-1**: Card identifier starting with 'C'
- **Positions 1-7**: Batch sequence number
- **Positions 7-26**: Card number

### Footer Line (Last line)
```
LOTE0001000010
```

### Complete Example File:
```
DESAFIO-HYPERATIVA           20180524LOTE000100007
C1     4456897922969999
C4     4456897998199999
C5     4456897999999999124
C6     4456897912999999
C7     445689799999998
C9     4456897999099999
C10    4456897919999999
LOTE0001000010
```

## 🛡️ Security Features

- All card numbers are **encrypted** before storage using AES encryption
- Card numbers are **hashed** for quick lookup while maintaining security
- **JWT tokens** with 15-minute expiration for enhanced security
- Input data validation and sanitization
- Secure password hashing with bcrypt

## 💡 Usage Examples

### Example 1: Complete Flow
```bash
# 1. Register user
curl -X POST http://localhost:8080/user \
  -H "Content-Type: application/json" \
  -d '{"name":"Maria","email":"maria@email.com","password":"password123","birthdate":"15/05/1985"}'

# 2. Authenticate (token valid for 15 minutes)
curl -X POST http://localhost:8080/authentication/login \
  -H "Content-Type: application/json" \
  -d '{"login":"maria@email.com","password":"password123"}'

# 3. Save card (using returned token)
curl -X POST http://localhost:8080/card/single \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -d '{"cardNumber":"4111111111111111"}'

# 4. Query card
curl -X GET "http://localhost:8080/card/4111111111111111" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### Example 2: Batch Upload
```bash
# Create batch file with correct format
cat > batch.txt << EOF
DESAFIO-HYPERATIVA           20180524LOTE000100003
C1     4111111111111111
C2     5555555555554444
C3     378282246310005
LOTE0001000010
EOF

# Upload batch
curl -X POST http://localhost:8080/card/batch \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -F "file=@batch.txt"
```

### Example 3: Using Swagger UI
1. Start the application
2. Navigate to `http://localhost:8080/swagger-ui.html`
3. Expand endpoints to see details
4. Click "Try it out" to test directly from the browser
5. Use the "Authorize" button to set your JWT token

## 🐛 Response Codes

- `201 Created` - Resource created successfully
- `202 Accepted` - Request accepted for processing
- `200 OK` - Operation successful
- `401 Unauthorized` - Invalid or missing token (expired after 15 minutes)
- `403 Forbidden` - Insufficient permissions
- `400 Bad Request` - Invalid input data or file format
- `500 Internal Server Error` - Non predicted error

## 📊 Batch Processing Status

When processing batch files, the system tracks:

- **Total records** processed
- **Successful records** saved
- **Failed records** with errors
- **Processing status**: `PROCESSING`, `COMPLETED`, `FAILED`, `PARTIAL_SUCCESS`
- **Error messages** for failed batches

### Batch Status Meanings:
- **`PROCESSING`**: Batch is currently being processed
- **`COMPLETED`**: All cards were processed successfully
- **`FAILED`**: No cards could be processed due to errors
- **`PARTIAL_SUCCESS`**: Some cards succeeded, some failed

## 📊 Data Structure

### User
- `id` (UUID) - Unique identifier
- `name` - Full name
- `email` - Email (used for login)
- `password` - Encrypted password
- `birthdate` - Date of birth (DD/MM/YYYY format)
- `role` - Profile (CUSTOMER or ADMIN)

### Card
- `id` (UUID) - Unique card identifier in the system
- `encryptedCardNumber` - Encrypted card number (AES)
- `hashedCardNumber` - Hashed card number for lookup
- `user` - Owner user
- `batch` - Reference to batch processing (if applicable)
- `createdAt` - Creation date

### Batch
- `filename` - Original uploaded filename
- `processedAt` - Processing timestamp
- `totalRecords` - Total cards in batch
- `successfulRecords` - Successfully processed cards
- `failedRecords` - Cards that failed processing
- `status` - Processing status
- `errorMessage` - Error details if processing failed

## 🔄 Next Steps

1. **Set up the environment** following the main `README.md`
2. **Test authentication** with the examples above
3. **Use Swagger UI** at `http://localhost:8080/swagger-ui.html` for interactive testing
4. **Prepare batch files** following the fixed-length format specification
5. **Implement your client** as needed

**Note:** The application performs comprehensive internal logging of all batch processing operations, including individual card processing attempts and their status. However, the API does not expose public endpoints for accessing these audit logs or batch processing statistics. All security and processing logs are maintained internally for system administration purposes.