# PikPok Setup Guide

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24+ (Android 7.0)
- Firebase account

### Firebase Setup

1. **Create Firebase Project**
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Click "Add project" and follow the setup wizard
   - Choose your project name (e.g., "PikPok")

2. **Add Android App**
   - Click "Add app" and select Android
   - Package name: `com.mhrlive.pikpok`
   - App nickname: `PikPok`
   - Download the `google-services.json` file

3. **Enable Services**
   - **Authentication**: Go to Authentication → Sign-in method → Enable Email/Password
   - **Firestore**: Go to Firestore Database → Create database → Start in test mode
   - **Storage**: Go to Storage → Get started → Start in test mode

4. **Configure Security Rules**

   **Firestore Rules:**
   ```javascript
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /users/{userId} {
         allow read, write: if request.auth != null && request.auth.uid == userId;
       }
       match /posts/{postId} {
         allow read: if request.auth != null;
         allow write: if request.auth != null && request.auth.uid == resource.data.userId;
       }
       match /comments/{commentId} {
         allow read: if request.auth != null;
         allow write: if request.auth != null && request.auth.uid == resource.data.userId;
       }
     }
   }
   ```

   **Storage Rules:**
   ```javascript
   rules_version = '2';
   service firebase.storage {
     match /b/{bucket}/o {
       match /posts/{allPaths=**} {
         allow read: if request.auth != null;
         allow write: if request.auth != null 
           && request.resource.size < 10 * 1024 * 1024; // 10MB limit
       }
     }
   }
   ```

5. **Replace Configuration**
   - Replace `app/google-services.json` with your downloaded file

### Installation

1. **Clone and Open**
   ```bash
   git clone https://github.com/mhrlive/PikPok.git
   cd PikPok
   ```

2. **Open in Android Studio**
   - File → Open → Select PikPok folder
   - Wait for Gradle sync

3. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```

### Features Walkthrough

#### 1. Authentication Flow
- **Splash Screen**: Auto-login check
- **Login/Register**: Email/password authentication
- **User Profiles**: Stored in Firestore

#### 2. Main Feed
- **Vertical Scrolling**: TikTok-style feed
- **Real-time Updates**: Live post loading
- **Like System**: One-tap likes with animations
- **User Info**: Profile pictures and usernames

#### 3. Upload System
- **Camera**: Direct photo capture
- **Gallery**: Photo selection
- **Captions**: Text descriptions
- **Firebase Storage**: Automatic upload

#### 4. Technical Architecture
- **MVVM Ready**: Structured for ViewModels
- **Firebase SDK**: Latest stable versions
- **Material Design**: Modern UI components
- **Glide**: Efficient image loading

### Troubleshooting

**Common Issues:**

1. **Build Errors**
   - Ensure `google-services.json` is in `app/` folder
   - Check internet connection for Firebase
   - Sync Project with Gradle Files

2. **Authentication Issues**
   - Verify Email/Password is enabled in Firebase Console
   - Check SHA-1 fingerprint is added to Firebase project

3. **Upload Issues**
   - Grant camera and storage permissions
   - Check Firebase Storage rules
   - Ensure stable internet connection

4. **Empty Feed**
   - Upload at least one post to see content
   - Check Firestore security rules
   - Verify internet connection

### Next Steps for Enhancement

1. **Push Notifications**: FCM integration
2. **Comments**: Full comment system
3. **Stories**: 24-hour temporary posts
4. **Filters**: Image editing capabilities
5. **Following System**: User relationships
6. **Search**: User and hashtag search
7. **Profile Pages**: Detailed user profiles
8. **Video Support**: Short video posts

### Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

### License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.