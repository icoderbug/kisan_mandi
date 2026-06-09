// ============================================
// TOAST.JS — Kisan Mandi notification system
// Drop-in replacement for all alert() calls
// ============================================
// Usage:
//   toast.success("Crop added successfully!")
//   toast.error("Phone number not registered!")
//   toast.warning("Please fill all fields!")
//   toast.info("Loading your dashboard...")
//   toast.loading("Placing your bid...")
//   toast.dismiss()  ← dismiss the loading toast
// ============================================

const toast = (() => {

    // Create container once
    function getContainer() {
        let container = document.getElementById("toast-container")
        if (!container) {
            container = document.createElement("div")
            container.id = "toast-container"
            document.body.appendChild(container)
        }
        return container
    }

    // Icons for each type
    const icons = {
        success: "✓",
        error:   "✕",
        warning: "⚠",
        info:    "ℹ",
        loading: "spinner"
    }

    // Titles for each type
    const titles = {
        success: "Success",
        error:   "Error",
        warning: "Warning",
        info:    "Info",
        loading: "Please wait"
    }

    // Duration in ms (loading has no auto-dismiss)
    const durations = {
        success: 3500,
        error:   5000,
        warning: 4000,
        info:    3500,
        loading: null
    }

    let loadingToast = null

    function show(type, message, options = {}) {
        const container  = getContainer()
        const duration   = options.duration ?? durations[type]
        const title      = options.title    ?? titles[type]
        const iconHtml   = type === "loading"
            ? `<div class="toast-spinner"></div>`
            : icons[type]

        // Build toast element
        const el = document.createElement("div")
        el.className = `toast ${type}`

        el.innerHTML = `
            <div class="toast-icon">${iconHtml}</div>
            <div class="toast-body">
                <div class="toast-title">${title}</div>
                <div class="toast-message">${message}</div>
            </div>
            <button class="toast-close" onclick="this.closest('.toast')._dismiss()">✕</button>
        `

        // Set progress bar duration
        if (duration) {
            el.style.setProperty("--duration", duration + "ms")
            el.style.cssText += `
                --duration: ${duration}ms;
            `
            const style = el.querySelector("style") || el
            el.style.setProperty("animation-duration", duration + "ms")
            // Apply to ::after via inline style trick
            const sheet = document.createElement("style")
            const id = "toast-" + Date.now()
            el.id = id
            sheet.textContent = `#${id}::after { animation-duration: ${duration}ms; }`
            document.head.appendChild(sheet)
            el._sheet = sheet
        }

        // Dismiss function
        el._dismiss = () => {
            el.classList.add("hide")
            el.classList.remove("show")
            setTimeout(() => {
                el.remove()
                if (el._sheet) el._sheet.remove()
            }, 350)
        }

        container.appendChild(el)

        // Trigger animation
        requestAnimationFrame(() => {
            requestAnimationFrame(() => {
                el.classList.add("show")
            })
        })

        // Auto dismiss
        if (duration) {
            setTimeout(() => el._dismiss(), duration)
        }

        return el
    }

    return {
        success(message, options) {
            return show("success", message, options)
        },
        error(message, options) {
            return show("error", message, options)
        },
        warning(message, options) {
            return show("warning", message, options)
        },
        info(message, options) {
            return show("info", message, options)
        },
        loading(message, options) {
            // Dismiss any existing loading toast first
            if (loadingToast) loadingToast._dismiss()
            loadingToast = show("loading", message, options)
            return loadingToast
        },
        dismiss() {
            if (loadingToast) {
                loadingToast._dismiss()
                loadingToast = null
            }
        }
    }
})()