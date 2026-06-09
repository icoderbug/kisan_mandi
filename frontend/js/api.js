// API.JS — All fetch() calls to Spring Boot
// Base URL: http://localhost:8080/api
// ============================================

const BASE_URL = "http://localhost:8080/api"

// ============================================
// TOKEN HELPERS
// ============================================

function getToken() {
    return localStorage.getItem("token")
}

function getHeaders() {
    return {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + getToken()
    }
}

function saveUserData(data) {
    localStorage.setItem("token",    data.token)
    localStorage.setItem("role",     data.role)
    localStorage.setItem("name",     data.name)
    localStorage.setItem("userId",   data.userId)
    localStorage.setItem("location", data.location || "")
}

function clearUserData() {
    localStorage.clear()
}

function isLoggedIn() {
    return getToken() !== null
}

function getRole() {
    return localStorage.getItem("role")
}

function getName() {
    return localStorage.getItem("name")
}

function getUserId() {
    return localStorage.getItem("userId")
}

function getLocation() {
    return localStorage.getItem("location")
}

// ============================================
// SAFE RESPONSE PARSER
// Spring Boot sometimes returns plain text on errors.
// This helper safely parses whatever comes back.
// ============================================
async function safeJson(response) {
    const text = await response.text()
    try {
        return JSON.parse(text)
    } catch {
        // Plain text error from Spring Boot — wrap it so frontend can read it
        return { error: text }
    }
}

// ============================================
// AUTH APIs
// ============================================

async function registerUser(data) {
    const response = await fetch(`${BASE_URL}/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
    return await safeJson(response)   // ✅ safe parse
}

async function loginUser(data) {
    const response = await fetch(`${BASE_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
    return await safeJson(response)   // ✅ safe parse
}

// ============================================
// CROP APIs
// ============================================

async function browseCrops(state = "", search = "") {
    const params = new URLSearchParams()
    if (state)  params.append("state", state)
    if (search) params.append("search", search)
    const response = await fetch(`${BASE_URL}/crops/browse?${params}`)
    return await response.json()
}

async function getCropById(cropId) {
    const response = await fetch(`${BASE_URL}/crops/${cropId}`, {
        headers: getHeaders()
    })
    return await response.json()
}

async function addCrop(data) {
    const response = await fetch(`${BASE_URL}/crops/add`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify(data)
    })
    return await response.json()
}

async function getMyListings() {
    const response = await fetch(`${BASE_URL}/crops/my/listings`, {
        headers: getHeaders()
    })
    return await response.json()
}

async function deleteCrop(cropId) {
    const response = await fetch(`${BASE_URL}/crops/${cropId}`, {
        method: "DELETE",
        headers: getHeaders()
    })
    return await response.text()
}

async function markCropAsSold(cropId) {
    const response = await fetch(`${BASE_URL}/crops/${cropId}/sold`, {
        method: "PATCH",
        headers: getHeaders()
    })
    return await response.json()
}

// ============================================
// BID APIs
// ============================================

async function placeBid(cropId, amount) {
    const response = await fetch(`${BASE_URL}/bids/place`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify({ cropId, amount })
    })
    return await response.json()
}

async function getBidsForCrop(cropId) {
    const response = await fetch(`${BASE_URL}/bids/crop/${cropId}`, {
        headers: getHeaders()
    })
    return await response.json()
}

async function getMyBids() {
    const response = await fetch(`${BASE_URL}/bids/my`, {
        headers: getHeaders()
    })
    return await response.json()
}

async function getFarmerBids() {
    const response = await fetch(`${BASE_URL}/bids/farmer`, {
        headers: getHeaders()
    })
    return await response.json()
}

async function withdrawBid(bidId) {
    const response = await fetch(`${BASE_URL}/bids/${bidId}`, {
        method: "DELETE",
        headers: getHeaders()
    })
    return await response.text()
}

// ============================================
// ORDER APIs
// ============================================

async function acceptBid(cropId) {
    const response = await fetch(`${BASE_URL}/orders/accept/${cropId}`, {
        method: "POST",
        headers: getHeaders()
    })
    return await response.json()
}

async function getFarmerOrders() {
    const response = await fetch(`${BASE_URL}/orders/farmer`, {
        headers: getHeaders()
    })
    return await response.json()
}

async function getBuyerOrders() {
    const response = await fetch(`${BASE_URL}/orders/buyer`, {
        headers: getHeaders()
    })
    return await response.json()
}

async function updatePaymentStatus(orderId, status) {
    const response = await fetch(`${BASE_URL}/orders/${orderId}/payment?status=${status}`, {
        method: "PATCH",
        headers: getHeaders()
    })
    return await response.json()
}

async function updateDeliveryStatus(orderId, status) {
    const response = await fetch(`${BASE_URL}/orders/${orderId}/delivery?status=${status}`, {
        method: "PATCH",
        headers: getHeaders()
    })
    return await response.json()
}

// ============================================
// ADMIN APIs
// ============================================

async function getAdminStats() {
    const response = await fetch(`${BASE_URL}/admin/stats`, {
        headers: getHeaders()
    })
    return await response.json()
}

async function getAllFarmers() {
    const response = await fetch(`${BASE_URL}/admin/farmers`, {
        headers: getHeaders()
    })
    return await response.json()
}

async function getAllBuyers() {
    const response = await fetch(`${BASE_URL}/admin/buyers`, {
        headers: getHeaders()
    })
    return await response.json()
}

async function getPendingUsers() {
    const response = await fetch(`${BASE_URL}/admin/pending`, {
        headers: getHeaders()
    })
    return await response.json()
}

async function verifyUser(userId) {
    const response = await fetch(`${BASE_URL}/admin/verify/${userId}`, {
        method: "PATCH",
        headers: getHeaders()
    })
    return await response.text()
}

async function blockUser(userId) {
    const response = await fetch(`${BASE_URL}/admin/block/${userId}`, {
        method: "PATCH",
        headers: getHeaders()
    })
    return await response.text()
}

async function getAllOrders() {
    const response = await fetch(`${BASE_URL}/admin/orders`, {
        headers: getHeaders()
    })
    return await response.json()
}

// ============================================
// REDIRECT HELPERS
// ============================================

function redirectToDashboard() {
    const role = getRole()
    if (role === "FARMER") window.location.href = "farmer/dashboard.html"
    else if (role === "BUYER") window.location.href = "buyer/dashboard.html"
    else if (role === "ADMIN") window.location.href = "admin/dashboard.html"
}

function logout() {
    clearUserData()
    window.location.href = "login.html"
}

function checkAuth() {
    if (!isLoggedIn()) {
        window.location.href = "../login.html"
    }
}

function checkAuthAdmin() {
    if (!isLoggedIn() || getRole() !== "ADMIN") {
        window.location.href = "../login.html"
    }
}