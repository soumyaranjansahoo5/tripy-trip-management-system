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

function requireAuth() {
    if (!getToken()) { window.location.href = '/index.html'; }
}

function requireAdmin() {
    if (!getToken() || getUserRole() !== 'ADMIN') {
        window.location.href = '/trips.html';
    }
}

function logout() {
    clearAuth();
    window.location.href = '/index.html';
}

// ── HTTP HELPERS ─────────────────────────────────────────
function authHeaders() {
    const h = { 'Content-Type': 'application/json' };
    const t = getToken();
    if (t) h['Authorization'] = 'Bearer ' + t;
    return h;
}

async function apiGet(url) {
    const res = await fetch(API + url, { headers: authHeaders() });
    return res.json();
}

async function apiPost(url, body, auth = true) {
    const headers = auth ? authHeaders() : { 'Content-Type': 'application/json' };

    const res = await fetch(API + url, {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(body)
    });

    return res.json();
}

async function apiPut(url, body = {}) {
    const res = await fetch(API + url, {
        method: 'PUT',
        headers: authHeaders(),
        body: JSON.stringify(body)
    });

    return res.json();
}

async function apiDelete(url) {
    const res = await fetch(API + url, {
        method: 'DELETE',
        headers: authHeaders()
    });

    return res.json();
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

// ── MODAL ────────────────────────────────────────────────
function openModal(id)  { document.getElementById(id).classList.add('show'); }
function closeModal(id) { document.getElementById(id).classList.remove('show'); }

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.overlay').forEach(o =>
        o.addEventListener('click', e => {
            if (e.target === o) o.classList.remove('show');
        })
    );

    document.addEventListener('keydown', e => {
        if (e.key === 'Escape') {
            document.querySelectorAll('.overlay.show')
                .forEach(o => o.classList.remove('show'));
        }
    });
});

// ── TOAST ────────────────────────────────────────────────
function toast(msg) {
    const t = document.getElementById('toast');
    if (!t) return;

    t.textContent = msg;
    t.classList.add('show');

    setTimeout(() => t.classList.remove('show'), 3200);
}

// ── UTILITIES ────────────────────────────────────────────
const val     = id => document.getElementById(id).value.trim();
const fmt     = n  => Number(n).toLocaleString('en-IN');
const fmtDate = d  => d ? new Date(d).toLocaleDateString('en-IN', {
    day:'numeric', month:'short', year:'numeric'
}) : '';

const grads = [
    '#c4622d,#8fa8a0',
    '#2d6ac4,#a08fc4',
    '#2da87a,#6ac48a',
    '#c4a02d,#c4622d',
    '#0e0e0e,#c4622d'
];

const rndGrad = () => grads[Math.floor(Math.random() * grads.length)];
