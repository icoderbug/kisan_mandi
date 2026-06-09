// ============================================
// AUTH.JS — handles login page logic
// ============================================

// These run immediately — safe because they don't use toast
if (typeof isLoggedIn === "function" && isLoggedIn()) {
    redirectToDashboard()
}

const urlParams = new URLSearchParams(window.location.search)
if (urlParams.get("role") && typeof selectRole === "function") {
    selectRole(urlParams.get("role"))
}

// ============================================
// LOGIN
// ============================================
async function handleLogin() {
    const phone    = document.getElementById("login-phone")?.value.trim()
    const password = document.getElementById("login-password")?.value

    if (!phone || !password) {
        toast.warning("Please enter your phone number and password!")
        return
    }

    const cleanPhone = phone.replace(/[^0-9]/g, "").replace(/^91/, "")

    toast.loading("Logging you in...")

    try {
        const data = await loginUser({
            phone: cleanPhone,
            password,
            role: currentRole.toUpperCase()
        })

        toast.dismiss()

        if (data.token) {
            saveUserData(data)
            toast.success("Welcome back, " + data.name + "! Redirecting...")
            setTimeout(() => redirectToDashboard(), 1200)
        } else {
            toast.error(data.error || data.message || "Login failed! Check your credentials.")
        }
    } catch (err) {
        toast.dismiss()
        console.error("Login error:", err)
        toast.error("Could not connect. Is Spring Boot running?")
    }
}

// ============================================
// REGISTER
// ============================================
async function handleRegister() {
    const firstName = document.getElementById("reg-firstName")?.value.trim()
    const lastName  = document.getElementById("reg-lastName")?.value.trim()
    const phone     = document.getElementById("reg-phone")?.value.trim()
    const password  = document.getElementById("reg-password")?.value
    const state     = document.getElementById("reg-state")?.value

    if (!firstName || !phone || !password || !state) {
        toast.warning("Please fill all required fields!")
        return
    }

    if (password.length < 6) {
        toast.warning("Password must be at least 6 characters!")
        return
    }

    // Admin secret code check — kept as prompt since it needs input
    if (currentRole === "admin") {
        const secretCode = prompt("🔐 Enter admin secret code to register as Admin:")
        if (!secretCode || secretCode !== "KISANMANDI@ADMIN2024") {
            toast.error("Invalid secret code! Admin registration not allowed.")
            return
        }
    }

    const cleanPhone = phone.replace(/[^0-9]/g, "").replace(/^91/, "")

    toast.loading("Creating your account...")

    try {
        const email = document.getElementById("reg-email")?.value
        const data = await registerUser({
            firstName, lastName, phone: cleanPhone, 
            email, password,
            role: currentRole.toUpperCase(),
            state
    })

        toast.dismiss()

        if (data.token) {
            saveUserData(data)
            toast.success("Account created! Welcome, " + data.name + "!")
            setTimeout(() => redirectToDashboard(), 1200)
        } else {
            toast.error(data.error || data.message || "Registration failed!")
        }
    } catch (err) {
        toast.dismiss()
        console.error("Register error:", err)
        toast.error("Could not connect. Is Spring Boot running?")
    }
}