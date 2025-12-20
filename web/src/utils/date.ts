import dayjs from "dayjs";

export const DATE_FORMAT = "DD/MM/YYYY";
export const DATE_TIME_FORMAT = "DD/MM/YYYY HH:mm";

/**
 * Format ISO string hoặc Date sang DD/MM/YYYY
 */
export const formatDate = (value?: string | Date) => {
  if (!value) return "";
  return dayjs(value).format(DATE_FORMAT);
};

/**
 * Format ISO string hoặc Date sang DD/MM/YYYY HH:mm
 */
export const formatDateTime = (value?: string | Date) => {
  if (!value) return "";
  return dayjs(value).format(DATE_TIME_FORMAT);
};
