// ── CONFIG ──────────────────────────────────────────────
const API = 'https://tripy-trip-management-system-production.up.railway.app/api';

// ── AUTH HELPERS ─────────────────────────────────────────
function getToken()    { return localStorage.getItem('tripy_token'); }
function getUserRole() { return localStorage.getItem('tripy_role'); }
function getUserName() { return localStorage.getItem('tripy_name'); }

function storeAuth(data) {
    localStorage.setItem('tripy_token', data.token);
    localStorage.setItem('tripy_role', data.role);
    localStorage.setItem('tripy_name', data.name);
}

function clearAuth() {
    ['tripy_token','tripy_role','tripy_name'].forEach(k => localStorage.removeItem(k));
}

function logout() {
    clearAuth();
    window.location.href = '/index.html';
}

// ── HTTP HELPERS (FIXED) ─────────────────────────────────
function authHeaders(auth = true) {
    const headers = { 'Content-Type': 'application/json' };

    if (auth) {
        const token = getToken();
        if (token) headers['Authorization'] = 'Bearer ' + token;
    }

    return headers;
}

async function handleResponse(res) {
    const data = await res.json();

    if (!res.ok) {
        throw new Error(data.message || "Server error");
    }

    return data;
}

async function apiGet(url) {
    const res = await fetch(API + url, {
        headers: authHeaders(true)
    });
    return handleResponse(res);
}

async function apiPost(url, body, auth = true) {
    const res = await fetch(API + url, {
        method: 'POST',
        headers: authHeaders(auth),
        body: JSON.stringify(body)
    });
    return handleResponse(res);
}

// ── LOGIN FUNCTION (IMPORTANT FIX) ───────────────────────
async function login() {
    try {
        const res = await apiPost('/auth/login', {
            email: val('email'),
            password: val('password')
        }, false);

        if (res.success) {
            storeAuth(res.data);
            window.location.href = '/trips.html';
        } else {
            toast(res.message);
        }

    } catch (err) {
        console.error(err);
        toast(err.message);
    }
}

// ── REGISTER FUNCTION ───────────────────────────────────
async function register() {
    try {
        const res = await apiPost('/auth/register', {
            name: val('name'),
            email: val('email'),
            password: val('password')
        }, false);

        if (res.success) {
            storeAuth(res.data);
            toast("Registered successfully!");
            window.location.href = '/trips.html';
        } else {
            toast(res.message);
        }

    } catch (err) {
        console.error(err);
        toast(err.message);
    }
}

// ── NAVBAR ───────────────────────────────────────────────
function renderNav(activePage) {
    const role = getUserRole();
    const name = getUserName();
    const nav = document.getElementById('navbar');
    if (!nav) return;

    nav.innerHTML = `
        <a class="nav-logo" href="/trips.html">Trip<span>y</span></a>
        <div class="nav-links">
            <span class="nav-user">👤 ${name || ''}</span>
            <a class="nav-btn ${activePage==='trips'?'active':''}" href="/trips.html">🗺 Explore</a>
            <a class="nav-btn ${activePage==='bookings'?'active':''}" href="/bookings.html">🎫 My Bookings</a>
            <a class="nav-btn ${activePage==='reviews'?'active':''}" href="/reviews.html">⭐ Reviews</a>
            ${role === 'ADMIN' ? `<a class="nav-btn ${activePage==='admin'?'active':''}" href="/admin.html">🛡 Admin</a>` : ''}
            <button class="nav-btn logout" onclick="logout()">Sign Out</button>
        </div>`;
}

// ── TOAST ────────────────────────────────────────────────
function toast(msg) {
    const t = document.getElementById('toast');
    if (!t) return;

    t.textContent = msg;
    t.classList.add('show');

    setTimeout(() => t.classList.remove('show'), 3000);
}

// ── UTILITIES ────────────────────────────────────────────
const val = id => document.getElementById(id).value.trim();
