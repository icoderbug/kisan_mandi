checkAuth()

async function renderPage() {
    const name     = getName()
    const location = getLocation()

    document.getElementById("greeting-text").textContent =
        getGreeting() + ", " + name + " 👋"
    document.getElementById("nav-farmer-name").textContent     = name
    document.getElementById("nav-farmer-location").textContent = "📍 " + location

    // Load real crops from backend
    const crops = await getMyListings()
    // Load real bids from backend
    const bids  = await getFarmerBids()

    document.getElementById("stat-listings").textContent = crops.length
    document.getElementById("stat-bids").textContent     = bids.length

    // Render crops
    const cropList = document.getElementById("crop-list")
    if (crops.length === 0) {
        cropList.innerHTML = `<div class="p-4 text-center text-xs text-gray-400">No listings yet. Add your first crop!</div>`
        return
    }
    cropList.innerHTML = crops.slice(0, 4).map((crop, i) => `
        <div class="flex items-center justify-between px-5 py-4 ${i < 3 ? 'border-b border-gray-50' : ''}">
            <div class="flex items-center gap-3">
                <span class="text-2xl">🌾</span>
                <div>
                    <p class="text-sm font-medium text-gray-900">${crop.name} — ${crop.quantity} ${crop.unit}</p>
                    <p class="text-xs text-gray-400">📍 ${crop.state} · Base ₹${crop.basePrice}/qtl</p>
                </div>
            </div>
            <div class="text-right">
                <p class="text-sm font-medium text-green-600">₹${crop.highestBid}/qtl</p>
                <p class="text-xs text-gray-400">${crop.totalBids} bids</p>
                <span class="text-xs px-2 py-0.5 rounded-full ${getBadgeClass(crop.status)} mt-1 inline-block">
                    ${crop.status}
                </span>
            </div>
        </div>
    `).join("")
}

function getGreeting() {
    const h = new Date().getHours()
    if (h < 12) return "Good morning"
    if (h < 17) return "Good afternoon"
    return "Good evening"
}

function getBadgeClass(status) {
    if (status === "ACTIVE")  return "bg-green-50 text-green-700 border border-green-100"
    if (status === "SOLD")    return "bg-blue-50 text-blue-700 border border-blue-100"
    if (status === "EXPIRED") return "bg-red-50 text-red-500 border border-red-100"
    return ""
}

renderPage()