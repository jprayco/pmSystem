# Workbook API with PDF Attachment Support

## Modified Endpoint

**POST** `/project/workbook`

This endpoint now supports PDF file uploads along with workbook data.

## Request Format

### With Attachment (Multipart Form Data)
```bash
curl -X POST http://localhost:8080/project/workbook \
  -F "attachment=@/path/to/your/file.pdf" \
  -F "project='{\"id\": 1}'" \
  -F "revesionNo=1" \
  -F "date='2023-12-01'" \
  -F "changes='Major UI updates'" \
  -F "updater='John Doe'" \
  -F "status=1"
```

### Without Attachment (JSON Body)
```bash
curl -X POST http://localhost:8080/project/workbook \
  -H "Content-Type: application/json" \
  -d '{
    "project": {"id": 1},
    "revesionNo": 1,
    "date": "2023-12-01",
    "changes": "Major UI updates",
    "updater": "John Doe",
    "status": 1
  }'
```

## Response Format

### Success Response (200 OK)
```json
{
  "statusCode": 200,
  "message": "Created!",
  "workbook": {
    "id": 123,
    "project": {"id": 1},
    "revesionNo": 1,
    "date": "2023-12-01",
    "changes": "Major UI updates",
    "updater": "John Doe",
    "status": 1
  }
}
```

### Error Response (400 Bad Request)
```json
{
  "statusCode": 400,
  "message": "Error creating project: [error message]"
}
```

## Key Features

1. **File Storage**: PDF files are stored in the `uploads/workbook-attachments/` directory with unique filenames
2. **Database Records**: Attachment metadata is stored in the `WorkbookAttachment` table
3. **Validation**: Maintains existing validation constraints
4. **Error Handling**: Proper error handling for file operations
5. **Flexibility**: Works with or without attachments

## File Limitations
- Maximum file size: 10MB
- Files are renamed to UUIDs for security
- Original filenames are preserved in the database

## Database Schema
- `WorkbookAttachment` table stores:
  - `id`: Auto-increment primary key
  - `workbook_id`: Foreign key to WorkBook
  - `name`: Original filename
  - `path`: Storage path on filesystem
