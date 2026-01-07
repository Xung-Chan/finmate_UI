import { RoleStatus } from '@/enum/status';

function parseRolesFromPayload(payload: any): RoleStatus[] {
  if (!payload) return [];

  // common places where roles may be stored in JWT
  const maybe =
    payload.role ||
    payload.roles ||
    payload.authorities ||
    payload.authority ||
    payload.roles ||
    payload.realm_access?.roles ||
    payload.resource_access?.roles ||
    null;

  if (!maybe) return [];

  if (Array.isArray(maybe)) return maybe as RoleStatus[];
  if (typeof maybe === 'string') return [maybe as RoleStatus];

  return [];
}

export function getRolesFromToken(): RoleStatus[] {
  try {
    const token = localStorage.getItem('access_token');
    if (!token) return [];
    const parts = token.split('.');
    if (parts.length !== 3) return [];
    const payload = JSON.parse(atob(parts[1].replace(/-/g, '+').replace(/_/g, '/')));
    return parseRolesFromPayload(payload) as RoleStatus[];
  } catch (e) {
    return [];
  }
}

export function hasAnyRole(required?: RoleStatus | RoleStatus[], roles?: RoleStatus[]): boolean {
  if (!required) return true; // no requirement
  const userRoles = roles ?? getRolesFromToken();
  const requiredArr = Array.isArray(required) ? required : [required];
  return requiredArr.some((r) => userRoles.includes(r));
}

export default {
  getRolesFromToken,
  hasAnyRole,
};
