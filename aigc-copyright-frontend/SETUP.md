# AIGC Copyright System - Frontend Setup Guide

## 🚀 Quick Start

### Prerequisites
- Node.js 16+ 
- npm or yarn
- Backend server running on http://localhost:8080

### Installation

```bash
# Install dependencies
npm install

# Start development server
npm run dev

# Build for production
npm run build
```

The frontend will run on http://localhost:3000

## 📋 What's New

### Updated Architecture
- ✅ Complete TypeScript type definitions matching backend DTOs
- ✅ Modern Vue 3 Composition API with `<script setup>`
- ✅ Pinia store for state management
- ✅ Element Plus UI components
- ✅ Axios request interceptors with JWT authentication
- ✅ Vue Router with navigation guards

### API Integration
All API endpoints are correctly mapped to backend:

**Authentication**
- POST /api/users/register - User registration
- POST /api/users/login - User login
- GET /api/users/profile - Get user profile

**Works Management**
- POST /api/works/upload - Upload work (multipart/form-data)
- GET /api/works/my - Get current user's works (paginated)
- GET /api/works/{id} - Get work details
- POST /api/works/{id}/certify - Certify work on blockchain

**Copyright Transfer**
- POST /api/copyright/transfer - Transfer copyright
- GET /api/copyright/transfers - Get transfer history (with workId param)

**Blockchain Query**
- GET /api/blockchain/certificate/{workId} - Query certificate
- GET /api/blockchain/verify - Verify copyright
- GET /api/blockchain/transfer-history/{workId} - Get on-chain history

### UI Features

#### Modern Design
- Clean, professional gradient login/register pages
- Sidebar navigation with icon menu
- Responsive card-based layouts
- Status tags with color coding
- Loading states and error handling

#### Pages
1. **Login/Register** - Modern authentication UI with validation
2. **Dashboard** - Statistics cards, recent works, quick actions
3. **Works** - Paginated work list, detail view, certify/transfer actions
4. **Upload** - Drag-and-drop file upload with metadata form
5. **Transfers** - Transfer history search and display
6. **Blockchain** - Certificate query and copyright verification

### Type Definitions

**User Types**
```typescript
interface User {
  id: number
  username: string
  phone?: string
  email?: string
  authStatus: 'INIT' | 'AUTH'
  status: 'ENABLE' | 'DISABLE'
  walletAddress?: string
}
```

**Work Types**
```typescript
interface Work {
  id: number
  workId: string
  fileName: string
  fileType: 'TEXT' | 'IMAGE' | 'AUDIO' | 'VIDEO' | 'OTHER'
  workStatus: 'INIT' | 'UPLOADED' | 'CERTIFIED' | 'OFFLINE' | 'TRANSFERRED'
  rightType: 'RIGHT_OWNERSHIP' | 'RIGHT_USAGE'
  // ... more fields
}
```

**Transfer Types**
```typescript
interface CopyrightTransfer {
  transferType: 'FULL_TRANSFER' | 'LICENSE_GRANT'
  licenseType?: 'PERSONAL' | 'COMMERCIAL' | 'EXCLUSIVE'
  chainStatus: 'PENDING' | 'SUCCESS' | 'FAILED'
  transferStatus: 'INIT' | 'CONFIRMED' | 'CANCELLED' | 'EXPIRED'
  // ... more fields
}
```

## 🎨 Styling

The UI uses a modern, minimalist design with:
- Gradient backgrounds for auth pages
- Card-based layouts for content
- Color-coded status indicators
- Responsive grid system
- Smooth transitions and hover effects

## 🔧 Configuration

### API Base URL
The API base URL is configured in `vite.config.ts`:
```typescript
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

### Authentication
JWT tokens are stored in localStorage and automatically included in requests via Axios interceptors.

## 📝 Notes

- All API calls return a standard `ApiResponse<T>` format
- File uploads use FormData with multipart/form-data
- Pagination is handled on both frontend and backend
- Status types are strictly typed to match backend enums
- All dates are formatted as strings from backend

## 🐛 Troubleshooting

If you encounter module errors, run:
```bash
npm install
```

If the development server doesn't start:
```bash
# Check if port 3000 is available
# Or change the port in vite.config.ts
```

## 📦 Dependencies

Core:
- vue@^3.4.21
- vue-router@^4.3.0
- pinia@^2.1.7
- element-plus@^2.6.1
- axios@^1.6.8

Dev:
- typescript@~5.4.0
- vite@^5.1.6
- @vitejs/plugin-vue@^5.0.4
