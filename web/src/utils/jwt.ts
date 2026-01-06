export function isTokenValid(token?: string): boolean {
    if (!token) return false;
    try {
        const parts = token.split('.');
        if (parts.length !== 3) return false;
        const payload = JSON.parse(atob(parts[1].replace(/-/g, '+').replace(/_/g, '/')));
        const exp = payload.exp;
        if (typeof exp !== 'number') return false;
        const now = Math.floor(Date.now() / 1000);
        return exp > now + 5; // small leeway
    } catch (e) {
        return false;
    }
}

export function getTokenExpiry(token?: string): number | null {
    if (!token) return null;
    try {
        const parts = token.split('.');
        if (parts.length !== 3) return null;
        const payload = JSON.parse(atob(parts[1].replace(/-/g, '+').replace(/_/g, '/')));
        return typeof payload.exp === 'number' ? payload.exp : null;
    } catch (e) {
        return null;
    }
}
