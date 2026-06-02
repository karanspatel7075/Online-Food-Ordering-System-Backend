# Food Delivery Platform Backend (Swiggy‑like System)

A scalable, role‑based backend system inspired by **Swiggy/Zomato**, built using **Spring Boot**. This project focuses on restaurant onboarding, menu management, cart & order processing, admin controls, and user experience — designed with real‑world production patterns in mind.

> **Current Status:** ~75% complete (Delivery & Payments planned)

---

## 🧠 Project Overview

This backend application enables:

* Users to discover restaurants, manage carts, place orders, and give reviews
* Restaurants to manage menus and process orders
* Admins to control platform operations like approvals and moderation

The architecture follows **clean separation of concerns**, **RESTful APIs**, and **role‑based access control**.

---

## 🏗️ Tech Stack

* **Backend:** Java, Spring Boot 3
* **Security:** Spring Security, JWT
* **Database:** MongoDB
* **ORM:** Spring Data JPA / Hibernate
* **Build Tool:** Maven
* **API Style:** REST
* **Email:** SMTP (OTP & notifications)

---

## 👥 User Roles

* **USER** – Browse restaurants, manage cart, place orders, review food
* **RESTAURANT_ADMIN** – Manage restaurant, menu, and orders
* **ADMIN** – Platform control, approvals, moderation

---

## ✅ Implemented Features

### 1️⃣ Authentication & User Management

**Status: Completed**

* User registration & login
* JWT‑based authentication
* Role‑based authorization (USER / RESTAURANT_ADMIN / ADMIN)
* Forgot password with OTP via email
* Password reset
* Profile view & update

**Controllers:** `AuthController`, `UserController`

---

### 2️⃣ Address Management

**Status: Completed**

* Add / update / delete delivery addresses
* Support for multiple addresses per user
* Address selection during checkout

**Controller:** `UserController`

---

### 3️⃣ Restaurant Onboarding & Discovery

**Status: Completed**

* Restaurant registration
* Admin approval / rejection
* Block / unblock restaurants
* Open / close restaurant availability
* Restaurant listing with pagination & search
* View single restaurant details

**Controllers:** `RestaurantController`, `AdminController`

---

### 4️⃣ Menu Management (Restaurant Side)

**Status: Completed**

* Add / update menu items
* Enable / disable menu items
* Toggle item availability
* Public menu view for users

**Services / Controllers:** `MenuService`, `RestaurantController`

---

### 5️⃣ Cart System

**Status: Completed**

* Add items to cart
* View cart
* Update cart implicitly (quantity & items)
* Checkout initiation

**Controller:** `CartController`

---

### 6️⃣ Order Management (Core System)

**Status: Completed**

* Create orders from cart
* Order lifecycle management
* User order history
* Restaurant updates order status
* Order cancellation (user & restaurant)

**Controller:** `OrderController`

---

### 7️⃣ Admin Platform Controls

**Status: Completed**

* View pending restaurant registrations
* Approve / reject restaurants
* Block / unblock restaurants
* Transfer restaurant ownership
* Full platform‑level moderation

**Controller:** `AdminController`

---

### 8️⃣ Reviews & Ratings

**Status: Partially Implemented**

* Users can submit reviews
* View restaurant reviews

⚠️ Pending:

* Average rating calculation
* Rating persistence on restaurant
* Rating‑based sorting

**Controller:** `ReviewController`

---

## 📊 Project Completion Status

* **Backend Core:** ✅ Strong & stable
* **Swiggy‑like Coverage:** ~75%
* **Production Readiness:** Medium–High (after payments & delivery)

---

## 🚀 Upcoming Features (Planned Enhancements)

### 🔴 Payments Integration

* Razorpay / Stripe payment gateway
* Payment entity & order‑payment mapping
* Payment status tracking (CREATED, SUCCESS, FAILED)
* Webhook handling & signature verification

---

### 🔴 Delivery Partner System

* New role: `DELIVERY_PARTNER`
* Delivery assignment entity
* Order delivery lifecycle:

  * READY → OUT_FOR_DELIVERY → DELIVERED
* Delivery partner dashboards & APIs

---

### ⚡ Redis Integration (Planned)

* Cache frequently accessed data (restaurants, menus)
* Reduce DB load
* Improve response times
* Possible use cases:

  * Restaurant listings
  * Menu data
  * OTP / temporary tokens

---

### 🔄 WebSocket Integration (Planned)

* Real‑time order status updates
* Live notifications for:

  * Order confirmation
  * Order out for delivery
  * Order delivered
* Improved user & restaurant experience

---

### 🔔 Notifications System

* Email notifications
* Future scope: SMS / Push notifications
* Event‑driven alerts

---

## 📌 Future Scope (Optional Enhancements)

* Coupons & offers
* Refund handling
* Order tracking timeline
* Rating‑based restaurant ranking
* Analytics dashboard (Admin)

---

## 📄 License

This project is built for **learning, internships, and portfolio demonstration** purposes.

---

## 🙌 Author

**Karan Patel**
Aspiring Software Engineer | Backend Developer

---

⭐ If you find this project useful, consider giving it a star!
