# 🌾 Kisan Mandi — Direct Farm to Buyer Marketplace

<div align="center">

![Kisan Mandi](https://img.shields.io/badge/Kisan%20Mandi-Farm%20to%20Buyer-green?style=for-the-badge)
![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.11-green?style=for-the-badge&logo=springboot)
![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-green?style=for-the-badge&logo=mongodb)
![JavaScript](https://img.shields.io/badge/JavaScript-ES6-yellow?style=for-the-badge&logo=javascript)
![TailwindCSS](https://img.shields.io/badge/Tailwind-CSS-blue?style=for-the-badge&logo=tailwindcss)

**A full stack web marketplace that eliminates middlemen and connects Indian farmers directly with bulk buyers through a competitive bidding system.**

[🌐 Live Demo](https://icoderbug.github.io/kisan_mandi) • [⚙️ Backend API](https://kisan-mandi-api.onrender.com) • [📂 GitHub](https://github.com/icoderbug/kisan_mandi)

</div>

---

## 📌 Problem Statement

Indian farmers lose **30-40% of their income** to middlemen who buy crops at artificially low prices and sell at high margins. Kisan Mandi solves this by creating a **direct digital marketplace** where farmers list their crops and verified buyers place competitive bids — ensuring farmers get the **best market price**.

---

## ✨ Features

### 👨‍🌾 Farmer
- Register and create profile with location
- List crops with base price, quantity and bidding deadline
- View all incoming bids in real time
- Accept highest bid and create order
- Track order status and payment

### 🛒 Buyer
- Browse all active crop listings
- Filter crops by state and category
- Place competitive bids on listings
- Track winning and outbid status
- Pay and track order delivery

### 🛡️ Admin
- View platform statistics — farmers, buyers, listings
- Verify and approve new registrations
- Block suspicious users
- Manage disputes between farmers and buyers
- View all platform transactions

---

## 🛠️ Tech Stack

### Frontend
| Technology | Purpose |
|---|---|
| HTML5 | Structure |
| Tailwind CSS | Styling |
| JavaScript (ES6) | Interactivity |
| Fetch API | Backend communication |

### Backend
| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot 3.5.11 | REST API framework |
| Spring Security | Authentication & authorization |
| JWT (jjwt 0.11.5) | Token based authentication |
| BCrypt | Password hashing |
| Maven | Dependency management |

### Database & Deployment
| Technology | Purpose |
|---|---|
| MongoDB Atlas | Cloud database |
| Render.com | Backend hosting |
| GitHub Pages | Frontend hosting |
| Docker | Backend containerization |

---

## 🏗️ System Architecture

```
┌─────────────────┐         ┌──────────────────────┐         ┌───────────────┐
│    Frontend     │ ──────► │   Spring Boot API     │ ──────► │ MongoDB Atlas │
│  GitHub Pages   │ ◄────── │   Render.com          │ ◄────── │   Database    │
└─────────────────┘         └──────────────────────┘         └───────────────┘
        │                            │
        │                    ┌───────────────┐
        │                    │  JWT Security  │
        │                    │  BCrypt Hash   │
        │                    │  CORS Config   │
        │                    └───────────────┘
        │
┌───────────────────────────┐
│      3 User Roles          │
│  Farmer │ Buyer │ Admin    │
└───────────────────────────┘
```

---

## 📁 Project Structure

```
kisan_mandi/
├── docs/                          # Frontend (GitHub Pages)
│   ├── css/                       # Tailwind CSS
│   ├── js/
│   │   ├── api.js                 # All fetch() calls
│   │   └── auth.js                # Login/Register logic
│   ├── farmer/                    # Farmer pages (5 pages)
│   ├── buyer/                     # Buyer pages (4 pages)
│   ├── admin/                     # Admin pages (4 pages)
│   ├── index.html                 # Landing page
│   └── login.html                 # Login/Register page
│
└── backend/kisan_mandi_api/       # Spring Boot Backend
    ├── src/main/java/com/kisanmandi/
    │   ├── config/                # JWT, Security, CORS
    │   ├── controller/            # REST API endpoints
    │   ├── service/               # Business logic
    │   ├── repository/            # MongoDB queries
    │   ├── model/                 # Database schemas
    │   └── dto/                   # Request/Response objects
    ├── src/main/resources/
    │   └── application.properties
    └── Dockerfile
```

---

## 🔌 API Endpoints

### Auth
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/api/auth/register` | Register new user | Public |
| POST | `/api/auth/login` | Login user | Public |

### Crops
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| GET | `/api/crops/browse` | Browse all active crops | Public |
| GET | `/api/crops/{id}` | Get single crop | Token |
| POST | `/api/crops/add` | Add new listing | Farmer |
| GET | `/api/crops/my/listings` | Get farmer's crops | Farmer |
| DELETE | `/api/crops/{id}` | Delete listing | Farmer |
| PATCH | `/api/crops/{id}/sold` | Mark as sold | Farmer |

### Bids
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/api/bids/place` | Place a bid | Buyer |
| GET | `/api/bids/my` | Get buyer's bids | Buyer |
| GET | `/api/bids/farmer` | Get bids on farmer's crops | Farmer |
| GET | `/api/bids/crop/{id}` | Get bids for a crop | Token |
| DELETE | `/api/bids/{id}` | Withdraw bid | Buyer |

### Orders
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/api/orders/accept/{cropId}` | Accept highest bid | Farmer |
| GET | `/api/orders/farmer` | Get farmer's orders | Farmer |
| GET | `/api/orders/buyer` | Get buyer's orders | Buyer |
| PATCH | `/api/orders/{id}/payment` | Update payment status | Token |
| PATCH | `/api/orders/{id}/delivery` | Update delivery status | Token |

### Admin
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| GET | `/api/admin/stats` | Platform statistics | Admin |
| GET | `/api/admin/farmers` | All farmers | Admin |
| GET | `/api/admin/buyers` | All buyers | Admin |
| GET | `/api/admin/pending` | Pending verifications | Admin |
| PATCH | `/api/admin/verify/{id}` | Verify user | Admin |
| PATCH | `/api/admin/block/{id}` | Block user | Admin |

---

## 🚀 Getting Started

### Prerequisites
- Java 21
- Maven
- MongoDB (local) or MongoDB Atlas account
- Node.js (for Tailwind CSS)

### Backend Setup

```bash
# Clone the repository
git clone https://github.com/icoderbug/kisan_mandi.git

# Navigate to backend
cd kisan_mandi/backend/kisan_mandi_api

# Run with Maven
./mvnw spring-boot:run
```

### Frontend Setup

```bash
# Open docs/login.html with Live Server in VS Code
# OR simply open in browser
```

### Environment Variables

```properties
MONGODB_URI = mongodb+srv://username:password@cluster.mongodb.net/kisanmandi
JWT_SECRET  = your_secret_key_here
```

---

## 👥 Test Credentials

| Role | Phone | Password |
|---|---|---|
| Admin | 0000000000 | admin123 |
| Farmer | Register new | Your password |
| Buyer | Register new | Your password |

---

## 📊 Database Collections

```
kisanmandi/
├── users      # Farmers, Buyers, Admins
├── crops      # Crop listings
├── bids       # All bids placed
└── orders     # Accepted orders
```

---

## 🔮 Upcoming Features

- [ ] OTP verification via Firebase
- [ ] Real time bidding with WebSocket
- [ ] Email notifications
- [ ] Razorpay payment integration
- [ ] Mobile responsive design
- [ ] Government mandi price API integration

---

## 👨‍💻 Developer

**Prateek Singh**
- 📧 prateeksinghpk129@gmail.com
- 🎓 B.Tech CSE — SRMCEM Lucknow (2023-2027)
- 💼 [LinkedIn](www.linkedin.com/in/prateek-singh-860b90288)
- 🐙 [GitHub](https://github.com/icoderbug)

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

<div align="center">

**⭐ If you found this project helpful, please give it a star!**

Made with ❤️ by Prateek Singh

</div>
