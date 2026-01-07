import { RoleStatus } from '@/enum/status';

// Default roles allowed when not specified explicitly
export const DEFAULT_ALLOWED: RoleStatus[] = [RoleStatus.ADMIN, RoleStatus.STAFF];

// Menu and feature level permissions mapping.
// Keys correspond to top-level menu keys or `parent/child` for submenu entries.
// Update this map to change which roles can see/use a feature.
export const MENU_PERMISSIONS: Record<string, RoleStatus[]> = {
  analytics: DEFAULT_ALLOWED,
  users: DEFAULT_ALLOWED,
  'users/list': [RoleStatus.ADMIN],
  'users/logout': DEFAULT_ALLOWED,

  'wallets-management': DEFAULT_ALLOWED,
  'wallets-management/wallets': DEFAULT_ALLOWED,
  'wallets-management/wallet-verifications': DEFAULT_ALLOWED,
  'wallets-management/paylaters': DEFAULT_ALLOWED,
  'wallets-management/paylater-applications': DEFAULT_ALLOWED,

  transactions: DEFAULT_ALLOWED,

  notifications: DEFAULT_ALLOWED,
  'notifications/list': DEFAULT_ALLOWED,
  'notifications/log-mails': [RoleStatus.ADMIN],
  'notifications/create': [RoleStatus.ADMIN],

  // Example admin-only features
  'ai-training-data': [RoleStatus.ADMIN],
  'credit-policy': [RoleStatus.ADMIN],
};

export default MENU_PERMISSIONS;
