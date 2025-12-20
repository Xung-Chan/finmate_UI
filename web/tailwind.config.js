// tailwind.config.js
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        customblue: {
          1: "#3629B7",
          2: "#6B75FF",
          3: "#608EE9",
          4: "#0890FE",
          5: "#1573FF",
          6: "#4EB4FF",
        },
      },
    },
  },
  plugins: [],
};
