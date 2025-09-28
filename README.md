# PikPok

A TikTok-style Android app for sharing pictures instead of videos. Built with Kotlin, XML layouts, and Firebase.

## Features

- **User Authentication**: Login and register with Firebase Auth
- **Picture Feed**: Vertical scrolling feed similar to TikTok but for pictures
- **Firebase Integration**: 
  - Firebase Auth for user authentication
  - Firebase Firestore for storing post data
  - Firebase Storage for image storage
- **Social Features**:
  - Like pictures
  - Comment on pictures
  - Share pictures
  - User profiles

## Tech Stack

- **Language**: Kotlin
- **UI**: XML Layouts with RecyclerView
- **Backend**: Firebase (Auth, Firestore, Storage)
- **Image Loading**: Glide
- **Architecture**: MVVM pattern ready

## Project Structure

```
app/
├── src/main/java/com/mhrlive/pikpok/
│   ├── data/
│   │   └── Models.kt              # User, Post, Comment data models
│   ├── ui/
│   │   └── PostsAdapter.kt        # RecyclerView adapter for posts
│   ├── AuthActivity.kt            # Login/Register screen
│   ├── MainActivity.kt            # Main feed screen
│   └── SplashActivity.kt         # Splash screen
└── src/main/res/
    ├── layout/                   # XML layouts
    ├── drawable/                 # Icons and drawables
    ├── values/                   # Colors, strings, themes
    └── mipmap/                   # App icons
```

## Setup Instructions

1. Create a Firebase project at https://console.firebase.google.com/
2. Enable Authentication (Email/Password)
3. Create a Firestore database
4. Enable Firebase Storage
5. Download your `google-services.json` file and replace the placeholder in `app/google-services.json`
6. Update the Firebase configuration in the JSON file with your actual project details

## Firebase Collections Structure

### Users Collection
```
users/{uid}
- uid: string
- username: string
- email: string
- profilePictureUrl: string
- followersCount: number
- followingCount: number
- postsCount: number
```

### Posts Collection
```
posts/{postId}
- id: string
- userId: string
- username: string
- userProfilePicture: string
- imageUrl: string
- caption: string
- timestamp: number
- likesCount: number
- commentsCount: number
```

### Comments Collection
```
comments/{commentId}
- id: string
- postId: string
- userId: string
- username: string
- text: string
- timestamp: number
```

## Screenshots

The app includes:
- Splash screen with app branding
- Authentication screen for login/register
- Main feed with TikTok-style vertical scrolling
- Post cards with user info, image, and interaction buttons

## Future Enhancements

- Camera integration for taking pictures
- Image upload functionality
- Real-time like and comment features
- User profiles and following system
- Push notifications
- Image filters and editing
- Stories feature